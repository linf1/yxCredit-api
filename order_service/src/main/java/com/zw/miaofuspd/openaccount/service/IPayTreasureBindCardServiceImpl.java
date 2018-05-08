package com.zw.miaofuspd.openaccount.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.util.DateUtils;
import com.base.util.DoubleUtils;
import com.base.util.StringUtils;
import com.base.util.TraceLoggerUtil;
import com.zhiwang.zwfinance.app.jiguang.util.JiGuangUtils;
import com.zw.miaofuspd.facade.dict.service.IDictService;
import com.zw.miaofuspd.facade.dict.service.ISystemDictService;
import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.log.entity.OrderLogBean;
import com.zw.miaofuspd.facade.log.service.IAppOrderLogService;
import com.zw.miaofuspd.facade.openaccount.service.IPayTreasureBindCardService;
import com.zw.miaofuspd.util.JXMConvertUtil;
import com.zw.miaofuspd.util.MapToXml;
import com.zw.miaofuspd.util.SecurityUtil;
import com.zw.miaofuspd.util.rsa.HttpPayTreUtil;
import com.zw.miaofuspd.util.rsa.RsaCodingUtil;
import com.zw.service.base.AbsServiceBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by Administrator on 2017/12/7 0007.
 */
@Service("payTreasureBindCardServiceImpl")
public class IPayTreasureBindCardServiceImpl extends AbsServiceBase implements IPayTreasureBindCardService {
    @Autowired
    public ISystemDictService iSystemDictService;
    @Autowired
    private IDictService iDictService;
    @Autowired
    private IAppOrderLogService iAppOrderLogService;
    /**
     * 宝付预支付绑卡接口
     * @param name 姓名
     * @param idNo 身份证
     * @param cardNo 卡号
     * @param bankName 银行
     * @param bankCode 银行code
     * @param mobile 开户电话
     * @param userInfo
     * @return
     * @throws Exception
     */
    @Override
    public Map payTreasureBindingCard(String name, String idNo, String cardNo, String bankName, String bankCode, String mobile, AppUserInfo userInfo) throws Exception {
        Map returnMap = new HashMap();
        String error_num = iDictService.getDictInfo("绑卡错误次数","0");
        if(StringUtils.isEmpty(error_num)){
            error_num = "5";
        }
        int errorNum = Integer.valueOf(error_num);//总共多少次
        int bank_errorNum_spd = 0;
        //走数据库查出来
        String twoCodeSql = "SELECT bank_errorNum_spd FROM mag_customer where id = '"+userInfo.getCustomer_id()+"'";
        Map twoCodeMap = sunbmpDaoSupport.findForMap(twoCodeSql);
        if(StringUtils.isNotEmpty(twoCodeMap.get("bank_errorNum_spd").toString())){
            bank_errorNum_spd = Integer.valueOf(twoCodeMap.get("bank_errorNum_spd").toString());
        }
        if(bank_errorNum_spd>=errorNum){
            returnMap.put("msg","绑卡错误次数已超过"+errorNum+"次,您已被拒绝绑卡");
            returnMap.put("flag",true);
            returnMap.put("respCode","over");
            return returnMap;
        }
        TraceLoggerUtil.info("=======预支付 11接口=========");
        Map<String,String> HeadPostParam = new HashMap<>();
        HeadPostParam.put("txn_sub_type", "11");//子交易
        String terminal_id = iSystemDictService.getInfo("payTreasureBindCard.terminalId");//终端号
        String member_id = iSystemDictService.getInfo("payTreasure.memberId");//商户编号
        String trade_date=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());//交易日期
        Map<String,Object> XMLArray = new HashMap<String,Object>();
        XMLArray.put("txn_sub_type", "11");//交易子类
        XMLArray.put("biz_type", "0000");//接入类型默认0000
        XMLArray.put("terminal_id", terminal_id);//终端号
        XMLArray.put("member_id", member_id);//商户编号
        XMLArray.put("acc_no", cardNo);//卡号
        XMLArray.put("trans_id", "Bind"+System.currentTimeMillis());//商户订单号
        XMLArray.put("id_card_type", "01");//证件类型固定01（身份证）
        XMLArray.put("id_card", idNo);//身份证号码
        XMLArray.put("id_holder", name);//姓名
        XMLArray.put("mobile", mobile);//开户手机号码
        XMLArray.put("acc_pwd", "");
        XMLArray.put("valid_date", "");
        XMLArray.put("valid_no", "");
        XMLArray.put("pay_code", bankCode);//银行卡编码
        XMLArray.put("trans_serial_no", "TISN"+System.currentTimeMillis());
        XMLArray.put("trade_date", trade_date);
        XMLArray.put("additional_info", "附加信息");
        XMLArray.put("req_reserved", "保留");
        String result = payTreasure(XMLArray,HeadPostParam,terminal_id);
        TraceLoggerUtil.info("请求返回："+ result);
        JSONObject json = JSON.parseObject(result);
        TraceLoggerUtil.info("=====返回结果转JSONObject:"+json);
        if(!"0000".equals(json.get("resp_code"))){
            bank_errorNum_spd = bank_errorNum_spd +1;
            if(bank_errorNum_spd>=errorNum){
                returnMap.put("msg","绑卡错误次数已超过"+errorNum+"次,您已被拒绝绑卡");
            }else{
                int num=errorNum-bank_errorNum_spd;
                returnMap.put("msg","您的信息有误,请确认信息再进行绑卡,您还有"+num+"次绑卡机会");
            }
            //更新客户表的绑卡错误次数
            String sql = "UPDATE mag_customer SET bank_errorNum_spd = '"+bank_errorNum_spd+"' where id = '"+userInfo.getCustomer_id()+"'";
            sunbmpDaoSupport.exeSql(sql);
            returnMap.put("flag",false);
        }else{
            String sql = "UPDATE mag_customer SET bank_errorNum_spd = 0 where id = '"+userInfo.getCustomer_id()+"'";
            sunbmpDaoSupport.exeSql(sql);
            returnMap.put("msg",json.get("resp_msg"));
            returnMap.put("flag",true);
        }
        returnMap.put("respCode",json.get("resp_code"));
        returnMap.put("transId",json.get("trans_id"));//商户订单号
        returnMap.put("flag",true);
        return returnMap;
    }
    /**
     *宝付支付绑卡确认接口
     * @param name 姓名
     * @param idNo 身份证
     * @param cardNo 卡号
     * @param bankName 银行
     * @param bankCode 银行code
     * @param tel 开户电话
     * @param userInfo
     * @return
     * @throws Exception
     */
    @Override
    public Map payTreasureConfimCard(String name, String idNo, String cardNo, String bankName, String smsCode,
                                     String bankCode, String transId,String tel, AppUserInfo userInfo) throws Exception {
        String error_num = iDictService.getDictInfo("绑卡错误次数","0");
        Map returnMap = new HashMap();
        if(StringUtils.isEmpty(error_num)){
            error_num = "5";
        }
        int errorNum = Integer.valueOf(error_num);//总共多少次
        int bank_errorNum_spd = 0;
        //走数据库查出来
        String twoCodeSql = "SELECT bank_errorNum_spd FROM mag_customer where id = '"+userInfo.getCustomer_id()+"'";
        Map twoCodeMap = sunbmpDaoSupport.findForMap(twoCodeSql);
        if(StringUtils.isNotEmpty(twoCodeMap.get("bank_errorNum_spd").toString())){
            bank_errorNum_spd = Integer.valueOf(twoCodeMap.get("bank_errorNum_spd").toString());
        }
        if(bank_errorNum_spd>=errorNum){//绑卡次数超过5次，拒绝绑卡
            returnMap.put("msg","绑卡错误次数已超过"+errorNum+"次,您已被拒绝绑卡");
            returnMap.put("flag",true);
            returnMap.put("respCode","over");
            return returnMap;
        }
        TraceLoggerUtil.info("=======确认绑卡12接口=========");

        Map<String,String> HeadPostParam = new HashMap<>();//明文参数
        HeadPostParam.put("txn_sub_type", "12");
        String trade_date=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());//交易日期
        String terminal_id = iSystemDictService.getInfo("payTreasureBindCard.terminalId");//终端号
        String member_id = iSystemDictService.getInfo("payTreasure.memberId");//商户编号
        Map<String,Object> XMLArray = new HashMap<String,Object>();
        XMLArray.put("txn_sub_type", "12");
        XMLArray.put("biz_type", "0000");
        XMLArray.put("terminal_id", terminal_id);
        XMLArray.put("member_id", member_id);
        XMLArray.put("sms_code", smsCode);
        XMLArray.put("trans_id", transId);//商户订单号
        XMLArray.put("trans_serial_no", "TISN"+System.currentTimeMillis());
        XMLArray.put("trade_date", trade_date);
        XMLArray.put("additional_info", "附加信息");
        XMLArray.put("req_reserved", "保留");
        String result = payTreasure(XMLArray,HeadPostParam,terminal_id);
        JSONObject json = JSON.parseObject(result);
        TraceLoggerUtil.info("=====返回结果转JSONObject:"+json);
        if(!"0000".equals(json.get("resp_code"))){
            bank_errorNum_spd = bank_errorNum_spd +1;
            if(bank_errorNum_spd>=errorNum){
                returnMap.put("msg","绑卡错误次数已超过"+errorNum+"次,您已被拒绝绑卡");
            }else{
                int num=errorNum-bank_errorNum_spd;
                returnMap.put("msg","您的信息有误,请确认信息再进行绑卡,您还有"+num+"次绑卡机会");
            }
            //更新客户表的绑卡错误次数
            String sql = "UPDATE mag_customer SET bank_errorNum_spd = '"+bank_errorNum_spd+"' where id = '"+userInfo.getCustomer_id()+"'";
            sunbmpDaoSupport.exeSql(sql);
            returnMap.put("flag",false);
        }else{
            String accountSql = "select * from mag_customer_account where CUSTOMER_ID = '"+userInfo.getCustomer_id()+"' and channel = '1'";
            List list = sunbmpDaoSupport.findForList(accountSql);
            if(list!=null && list.size()>0){//更新开户信息
                String updateAccountSql = "update mag_customer_account set count_name = '"+userInfo.getName()+"'," +
                        "tel = '"+tel+"',account_bank_id = '"+bankCode+"',account_bank = '"+bankName+"',bank_card='"+cardNo + "'," +
                        "user_name = '"+userInfo.getName()+"',USER_ID = '"+userInfo.getId()+"',card_num = '"+userInfo.getCard()+"' " +
                        "where CUSTOMER_ID = '"+userInfo.getCustomer_id()+"' and channel='1'";
                sunbmpDaoSupport.exeSql(updateAccountSql);
            }else{//保存开户信息
                String id = UUID.randomUUID().toString();
                String insertSql = "insert into mag_customer_account (id,CUSTOMER_ID,user_id,channel,count_name,tel,user_name,card_num,bank_card," +
                        "account_bank,account_bank_id,CREAT_TIME,ALTER_TIME,state) values" +
                        "('"+id+"','"+userInfo.getCustomer_id()+"','"+userInfo.getId()+"','1','"+userInfo.getName()+"','"+tel+"','"
                        +userInfo.getName()+"','"+userInfo.getCard()+"','"+cardNo+"','"+bankName+"','"+bankCode+"','"+ DateUtils.getDateString(new Date())+"','"+ DateUtils.getDateString(new Date())+"','0')";
                sunbmpDaoSupport.exeSql(insertSql);
            }
            //插入订单消息日志
            try {
                String title = "绑卡成功";
                String order_state = "99";
                insertMessageNoOrder(userInfo,"mySet",title,"您的银行卡绑定成功。",order_state);
            }catch (Exception e){
            }
            //将客户表的开户状态改为1
            String sql = "UPDATE mag_customer SET bank_errorNum_spd = 0 where id = '"+userInfo.getCustomer_id()+"'";
            sunbmpDaoSupport.exeSql(sql);
            returnMap.put("msg",json.get("resp_msg"));
            returnMap.put("flag",true);
        }
        returnMap.put("respCode",json.get("resp_code"));
        returnMap.put("transId",json.get("trans_id"));//商户订单号
        returnMap.put("flag",true);
        return returnMap;
    }
    /**
     * 宝付主动还款接口
     * @param amount 钱数
     * @param cardNo 银行 卡号
     * @param userInfo 实体类
     * @return
     * @throws Exception
     */
    @Override
    public Map payTreasureCardPay(String amount,String derateAmount, String cardNo, AppUserInfo userInfo,String derateId) throws Exception {
        /*先走开户表查询该用户的开户信息*/
        String accountSql = "select account_bank_id,account_bank,bank_card,card_num,count_name,tel from mag_customer_account where USER_ID='"+userInfo.getId()+"' and state='0' and channel ='1'";
        Map accountMap = sunbmpDaoSupport.findForMap(accountSql);
        Map returnMap = new HashMap();
        String pay_code = "";//银行卡编码
        String acc_no = "";//银行卡卡号
        String id_card = "";//身份证号码
        String id_holder = "";//姓名
        String mobile ="";//银行预留手机号
        String account_bank="";
        if(accountMap!=null){
            account_bank=accountMap.get("account_bank").toString();//银行名称
            pay_code = accountMap.get("account_bank_id").toString();//银行卡编码
            acc_no = accountMap.get("bank_card").toString();//银行卡卡号
            id_card = accountMap.get("card_num").toString();//身份证号码
            id_holder = accountMap.get("count_name").toString();//姓名
            mobile = accountMap.get("tel").toString();//银行预留手机号
        }else{//如果没有开户信息，则提示用户进行开户
            returnMap.put("flag",false);
            returnMap.put("msg","您当前未绑定银行卡");
            return returnMap;
        }
        Map<String,String> HeadPostParam = new HashMap<>();//明文参数
        HeadPostParam.put("txn_sub_type", "13"); //交易子类（代扣）
        String trade_date=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());//交易日期
        String terminal_id = iSystemDictService.getInfo("payTreasurePay.terminalId");//终端号
        String member_id = iSystemDictService.getInfo("payTreasure.memberId");//商户编号
        Map<String,Object> XMLArray = new HashMap<>();
        XMLArray.put("txn_sub_type", "13");//交易子类（代扣）
        XMLArray.put("biz_type", "0000");//默认0000,表示为储蓄卡支付。
        XMLArray.put("terminal_id", terminal_id);
        XMLArray.put("member_id",member_id);
        String  txn_amt = String.valueOf(new BigDecimal("0.01").multiply(BigDecimal.valueOf(100)).setScale(0));//支付金额转换成分
       // String  txn_amt = String.valueOf(new BigDecimal(amount).multiply(BigDecimal.valueOf(100)).setScale(0,BigDecimal.ROUND_HALF_UP));//支付金额转换成分
        XMLArray.put("pay_code", pay_code);
        XMLArray.put("pay_cm", "2");//传默认值
        XMLArray.put("id_card_type", "01");//身份证传固定值。
        XMLArray.put("acc_no", acc_no);//银行卡号
        XMLArray.put("id_card", id_card);//身份证号码
        XMLArray.put("id_holder", id_holder);//姓名
        XMLArray.put("mobile", mobile);//开户手机号码
        XMLArray.put("valid_date", "");//信用卡有效期
        XMLArray.put("valid_no", "");//信用卡安全码
        String trans_id = "TID"+System.currentTimeMillis();//商户订单号
        XMLArray.put("trans_id",trans_id);//商户订单号
        XMLArray.put("txn_amt", txn_amt);
        XMLArray.put("trans_serial_no", "TISN"+System.currentTimeMillis());
        XMLArray.put("trade_date", trade_date);
        XMLArray.put("additional_info", "附加信息");
        XMLArray.put("req_reserved", "保留");
        String result = "";
        String orderSql = "SELECT id,order_id FROM mag_repayment WHERE order_id = (select id from mag_order where user_id = '"+userInfo.getId()+"' and order_type='2' ORDER BY CREAT_TIME desc limit 1)";
        Map orderMap = sunbmpDaoSupport.findForMap(orderSql);
        String orig_trans_id = "";//原始订单号
        String orig_trade_date = "";////原始订单时间
        String state = "";//该条还款记录的状态
        String code = "";//该条记录所存在的记录状态、
        try {
            result = payTreasure(XMLArray,HeadPostParam,terminal_id);
            JSONObject json = JSON.parseObject(result);
            String respCode = json.getString("resp_code");//返回code
            orig_trans_id = json.getString("trans_id");//原始订单号
            TraceLoggerUtil.info("主动扣款返回的code吗"+respCode);
            if("0000".equals(respCode) || "BF00114".equals(respCode)){
                updatePayState(userInfo,derateAmount,amount,derateId);
                returnMap.put("order_stat","");//交易状态
                state = "1";//还款成功
            }else if("BF00100".equals(respCode) || "BF00112".equals(respCode)
                    || "BF00113".equals(respCode) || "BF00115".equals(respCode)
                    || "BF00144".equals(respCode) || "BF00202".equals(respCode)){ //出现这几种情况，我们则需要调查询类接口，来看该用户的真实还款状态
                //更新还款计划表 将状态更新为还款确认中，然后在定时任务里面查询处理
//                String  repaymentSql = "update mag_repayment set state ='" + 4 + "' where order_id ='" + orderMap.get("id").toString() + "'";
//                sunbmpDaoSupport.exeSql(repaymentSql);
                state = "0";//待确认
            }else{//这种交易失败被认定为失败
                returnMap.put("order_stat","");//交易状态
                state = "2";//失败
            }
            code = respCode;
            //向还款明细表插入一条记录
            String detailId = UUID.randomUUID().toString();
            String repaySql="select pay_count from mag_repayment where order_id ='" + orderMap.get("id").toString() + "'";
            String insertSql = "insert into mag_transaction_details(id,batch_no,amount,repayment_id,order_id,baofu_mag," +
                    "baofu_code,type,state,creat_time,derate_id,derate_amount,success_time,source)values" +
                    "('"+detailId+"','"+orig_trans_id+"','"+amount+"','"+orderMap.get("id").toString()+"','"+orderMap.get("order_id").toString()+"','"+json.getString("resp_msg")+"','"+code+"','2','"
                    +state +"','"+ DateUtils.getDateString(new Date())+"','"+derateId+"','"+derateAmount+"','"+ DateUtils.getDateString(new Date())+"','1')";
            sunbmpDaoSupport.exeSql(insertSql);
            returnMap.put("respCode",respCode);//返回code
            returnMap.put("msg",json.getString("resp_msg"));//返回的消息
        }catch (Exception e){
            try{
                orig_trans_id = trans_id;
                orig_trade_date = trade_date;
                String resultCheck = payTreasureCheck(orig_trans_id,orig_trade_date);
                JSONObject jsonObject = JSON.parseObject(resultCheck);
                if("S".equals(jsonObject.getString("order_stat"))){
                    updatePayState(userInfo,amount,derateAmount,derateId);
                    state = "1";
                }else if("I".equals(jsonObject.getString("order_stat"))){
                    //更新还款计划表 将状态更新为还款确认中，然后在定时任务里面查询处理
                    String  repaymentSql = "update mag_repayment set state ='" + 4 + "' where id ='" + orderMap.get("id").toString() + "'";
                    sunbmpDaoSupport.exeSql(repaymentSql);
                    state = "0";//确认中
                }else{
                    state = "2";//失败
                }
                code = jsonObject.getString("order_stat");//查询接口中已order_stat状态为准
                returnMap.put("order_stat",jsonObject.getString("order_stat"));//返回code
            }catch (Exception ea){
                //向异常表插入一条记录
                String exceptionId = UUID.randomUUID().toString();
                String insertSql = "insert into mag_transaction_exception(id,batch_no,amount,repayment_id,order_id,baofu_mag," +
                        "baofu_code,type,state,creat_time,success_time,again_batch_no,source)values" +
                        "('"+exceptionId+"','"+orig_trans_id+"','"+amount+"','"+orderMap.get("id").toString()+"','"+orderMap.get("order_id").toString()+"','接口异常','','2','0','"+ DateUtils.getDateString(new Date())+"','"+ DateUtils.getDateString(new Date())+"','1')";
                sunbmpDaoSupport.exeSql(insertSql);
                state ="3";//异常
            }
            //向还款明细表插入一条记录
            String detailId = UUID.randomUUID().toString();
            String insertSql = "insert into mag_transaction_details(id,batch_no,amount,repayment_id,order_id,baofu_mag," +
                    "baofu_code,type,state,creat_time,success_time,source)values" +
                    "('"+detailId+"','"+orig_trans_id+"','"+amount+"','"+orderMap.get("id").toString()+"','"+orderMap.get("order_id").toString()+"','接口异常','"+code+"','2','"
                    +state +"','"+ DateUtils.getDateString(new Date())+"','"+ DateUtils.getDateString(new Date())+"','1')";
            sunbmpDaoSupport.exeSql(insertSql);
        }
        return returnMap;
    }

    /**
     * 支付前置包费用
     * @param userInfo
     * @param orderId
     * @return
     * @throws Exception
     */
    public Map payServicePackageCardPay(AppUserInfo userInfo,String orderId) throws Exception {
        /*先走开户表查询该用户的开户信息*/
        String accountSql = "select account_bank_id,account_bank,bank_card,card_num,count_name,tel from mag_customer_account where USER_ID='"+userInfo.getId()+"' and state='0' and channel ='1'";
        Map accountMap = sunbmpDaoSupport.findForMap(accountSql);
        Map returnMap = new HashMap();
        String pay_code = "";//银行卡编码
        String acc_no = "";//银行卡卡号
        String id_card = "";//身份证号码
        String id_holder = "";//姓名
        String mobile ="";//银行预留手机号
        String account_bank="";
        if(accountMap!=null){
            account_bank=accountMap.get("account_bank").toString();//银行名称
            pay_code = accountMap.get("account_bank_id").toString();//银行卡编码
            acc_no = accountMap.get("bank_card").toString();//银行卡卡号
            id_card = accountMap.get("card_num").toString();//身份证号码
            id_holder = accountMap.get("count_name").toString();//姓名
            mobile = accountMap.get("tel").toString();//银行预留手机号
        }else{//如果没有开户信息，则提示用户进行开户
            returnMap.put("flag",false);
            returnMap.put("msg","您当前未绑定银行卡");
            return returnMap;
        }
        //查询该订单下前置服务包的金额
        String serPackSql = "  SELECT a.id as packageId, b.amount_collection as amount FROM mag_servicepag_order a" +
                " LEFT JOIN mag_service_package b on b.id = a.package_service_id WHERE a.type = '1' AND a.state != '1' AND a.state != '3' AND a.order_id = '"+orderId+"';";
        Map serPackMap = sunbmpDaoSupport.findForMap(serPackSql);
        if(serPackMap == null){
            returnMap.put("flag",false);
            returnMap.put("msg","您当前订单未存在需要支付的前置服务包");
            return returnMap;
        }
        String amount = serPackMap.get("amount").toString();
        String packageId = serPackMap.get("packageId").toString();
        Map<String,String> HeadPostParam = new HashMap<>();//明文参数
        HeadPostParam.put("txn_sub_type", "13"); //交易子类（代扣）
        String trade_date=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());//交易日期
        String terminal_id = iSystemDictService.getInfo("payTreasurePay.terminalId");//终端号
        String member_id = iSystemDictService.getInfo("payTreasure.memberId");//商户编号
        Map<String,Object> XMLArray = new HashMap<>();
        XMLArray.put("txn_sub_type", "13");//交易子类（代扣）
        XMLArray.put("biz_type", "0000");//默认0000,表示为储蓄卡支付。
        XMLArray.put("terminal_id", terminal_id);
        XMLArray.put("member_id",member_id);
        String  txn_amt = String.valueOf(new BigDecimal("0.01").multiply(BigDecimal.valueOf(100)).setScale(0));//支付金额转换成分
      //  String  txn_amt = String.valueOf(new BigDecimal(amount).multiply(BigDecimal.valueOf(100)).setScale(0,BigDecimal.ROUND_HALF_UP));//支付金额转换成分
        XMLArray.put("pay_code", pay_code);
        XMLArray.put("pay_cm", "2");//传默认值
        XMLArray.put("id_card_type", "01");//身份证传固定值。
        XMLArray.put("acc_no", acc_no);//银行卡号
        XMLArray.put("id_card", id_card);//身份证号码
        XMLArray.put("id_holder", id_holder);//姓名
        XMLArray.put("mobile", mobile);//开户手机号码
        XMLArray.put("valid_date", "");//信用卡有效期
        XMLArray.put("valid_no", "");//信用卡安全码
        String trans_id = "TID"+System.currentTimeMillis();//商户订单号
        XMLArray.put("trans_id",trans_id);//商户订单号
        XMLArray.put("txn_amt", txn_amt);
        XMLArray.put("trans_serial_no", "TISN"+System.currentTimeMillis());
        XMLArray.put("trade_date", trade_date);
        XMLArray.put("additional_info", "附加信息");
        XMLArray.put("req_reserved", "保留");
        String result = "";
        String orig_trans_id = "";//原始订单号
        String orig_trade_date = "";////原始订单时间
        String state = "";//该条还款记录的状态
        String code = "";//该条记录所存在的记录状态、
        try {
            result = payTreasure(XMLArray,HeadPostParam,terminal_id);
            JSONObject json = JSON.parseObject(result);
            String respCode = json.getString("resp_code");//返回code
            orig_trans_id = json.getString("trans_id");//原始订单号
            TraceLoggerUtil.info("主动扣款返回的code吗"+respCode);
            if("0000".equals(respCode) || "BF00114".equals(respCode)){
               String sql = "update mag_servicepag_order set state = '1',orig_trans_id = '"+orig_trans_id+"' where id = '"+packageId+"'";
               sunbmpDaoSupport.exeSql(sql);
                //更新订单环节小状态
                sql = "update mag_order set commodity_state = '17',alter_time='" + DateUtils.formatDate("yyyyMMddHHmmss") + "' where id = '"+orderId+"'";
                sunbmpDaoSupport.exeSql(sql);
               state = "1";//成功
            }else if("BF00100".equals(respCode) || "BF00112".equals(respCode)
                    || "BF00113".equals(respCode) || "BF00115".equals(respCode)
                    || "BF00144".equals(respCode) || "BF00202".equals(respCode)){//出现这几种情况，我们则需要调查询类接口，来看该用户的真实还款状态
                state = "0";//待确认
                String sql = "update mag_servicepag_order set state = '3',orig_trans_id = '"+orig_trans_id+"' where id = '"+packageId+"'";
                sunbmpDaoSupport.exeSql(sql);
            }else{//这种交易失败被认定为失败
                String sql = "update mag_servicepag_order set state = '2',orig_trans_id = '"+orig_trans_id+"' where id = '"+packageId+"'";
                sunbmpDaoSupport.exeSql(sql);
                state = "2";//失败
            }
            code = respCode;
            //向还款明细表插入一条记录
            String detailId = UUID.randomUUID().toString();
            String insertSql = "insert into mag_transaction_details(id,batch_no,amount,order_id,baofu_mag," +
                    "baofu_code,type,state,creat_time,success_time,source,bank_name,bank_card,des)values" +
                    "('"+detailId+"','"+orig_trans_id+"','"+amount+"','"+orderId+"','"+json.getString("resp_msg")+"','"+code+"','1','"
                    +state +"','"+ DateUtils.getDateString(new Date())+"','"+ DateUtils.getDateString(new Date())+"','1','"+account_bank+"','"+acc_no+"','支付前置服务包')";
            sunbmpDaoSupport.exeSql(insertSql);
            returnMap.put("respCode",respCode);//返回code
            returnMap.put("msg",json.getString("resp_msg"));//返回的消息
        }catch (Exception e){

        }
        return returnMap;
    }

    /**
     * 支付前置服务包确认
     * @param orderId
     * @return
     * @throws Exception
     */
    @Override
    public Map payServicePackageResult(String orderId) throws Exception {
        Map map = new HashMap();
        String serPackSql = "SELECT a.id as packageId,a.orig_trans_id as origTransId, b.amount_collection as amount FROM mag_servicepag_order a" +
                " LEFT JOIN mag_service_package b on b.id = a.package_service_id WHERE a.type = '1'AND a.state = '3' AND a.order_id = '"+orderId+"'";
        Map serPcgMap = sunbmpDaoSupport.findForMap(serPackSql);
        if (serPcgMap == null){
            map.put("state","-1");
            return map;
        }
        String origTransId = serPcgMap.get("origTransId").toString();
        String packageId = serPcgMap.get("packageId").toString();
        String result =  payTreasureCheck(origTransId,DateUtils.getDateString(new Date()));
        JSONObject jsonObject = JSON.parseObject(result);
        String state = jsonObject.getString("order_stat");
        //state : I 确认中, S 已付款 ,F 付款失败
        if ("F".equals(state)){
            String sql = "update mag_servicepag_order set state = '2' where id = '"+packageId+"'";
            sunbmpDaoSupport.exeSql(sql);
        }else  if ("S".equals(state)){
            String sql = "update mag_servicepag_order set state = '1' where id = '"+packageId+"'";
            sunbmpDaoSupport.exeSql(sql);
            //更新订单环节小状态
            sql = "update mag_order set commodity_state = '17',alter_time='" + DateUtils.formatDate("yyyyMMddHHmmss") + "' where id = '"+orderId+"'";
            sunbmpDaoSupport.exeSql(sql);
        }else  if ("I".equals(state)){
            String sql = "update mag_servicepag_order set state = '3' where id = '"+packageId+"'";
            sunbmpDaoSupport.exeSql(sql);
        }
        map.put("state",state);
        return map;
    }
    /**
     * 宝付查询接口
     */
    public String payTreasureCheck(String orig_trans_id,String orig_trade_date) throws Exception{
        TraceLoggerUtil.info("=======查询订单接口31=========");
        String terminal_id = iSystemDictService.getInfo("payTreasurePay.terminalId");//终端号
        String member_id = iSystemDictService.getInfo("payTreasure.memberId");//商户编号
        Map<String,String> HeadPostParam = new HashMap<>();//明文参数
        HeadPostParam.put("txn_sub_type", "31");
        Map<String,Object> XMLArray = new HashMap<String,Object>();
        XMLArray.put("txn_sub_type", "31");
        XMLArray.put("biz_type", "0000");
        XMLArray.put("terminal_id", terminal_id);
        XMLArray.put("member_id", member_id);
        XMLArray.put("orig_trans_id",orig_trans_id);
        XMLArray.put("trans_serial_no", "TISN"+System.currentTimeMillis());
        XMLArray.put("orig_trade_date", orig_trade_date);
        XMLArray.put("additional_info", "附加信息");
        XMLArray.put("req_reserved", "保留");
        String result = payTreasure(XMLArray,HeadPostParam,terminal_id);
        TraceLoggerUtil.info("代扣查询类接口返回的参数"+result);
        return result;
    }
    /**
     * 宝付绑卡接口
     * @param XMLArray
     * @param HeadPostParam
     * @return
     * @throws Exception
     */
    public String payTreasure(Map<String,Object> XMLArray,Map<String,String> HeadPostParam,String terminal_id) throws Exception {
        String  pfxpath = iSystemDictService.getInfo("payTreasure.pfxName");//宝付公钥
        String  cerpath = iSystemDictService.getInfo("payTreasure.cerName");//商户私钥
        TraceLoggerUtil.info("密钥地址"+pfxpath);
        File pfxfile=new File(pfxpath);
        if(!pfxfile.exists()){
            TraceLoggerUtil.info("===========私钥文件不存在");
            return "私钥文件不存在！";

        }
        File cerfile=new File(cerpath);
        if(!cerfile.exists()){//判断宝付公钥是否为空
            TraceLoggerUtil.info("===========公钥文件不存在");
            return "公钥文件不存在！";
        }
        String data_type = iSystemDictService.getInfo("payTreasure.dataType");
        String XmlOrJson = "";
        if("xml".equals(data_type)){
            Map<Object,Object> ArrayToObj = new HashMap<Object,Object>();
            ArrayToObj.putAll(XMLArray);
            XmlOrJson = MapToXml.Coverter(ArrayToObj, "data_content");
        }else{
            JSONObject jsonObjectFromMap = JSONObject.parseObject(JSON.toJSONString(XMLArray));
            XmlOrJson = jsonObjectFromMap.toString();
        }
        TraceLoggerUtil.info("请求参数"+XmlOrJson);
        String base64str = SecurityUtil.Base64Encode(XmlOrJson);

        String pfxPwd = iSystemDictService.getInfo("payTreasure.pfxPwd");
        String version = iSystemDictService.getInfo("payTreasure.version");//接口版本号
        String txnType = iSystemDictService.getInfo("payTreasure.txnType");//交易类型
        String member_id = iSystemDictService.getInfo("payTreasure.memberId");//商户编号
        String postUrl = iSystemDictService.getInfo("payTreasure.url");//请求地址
        String data_content = RsaCodingUtil.encryptByPriPfxFile(base64str,pfxpath,pfxPwd);
        HeadPostParam.put("version", version);//接口版本号
        HeadPostParam.put("txn_type", txnType);//交易类型
        HeadPostParam.put("data_type", data_type);//传参类型
        HeadPostParam.put("member_id", member_id);//商户id
        HeadPostParam.put("terminal_id", terminal_id);//终端号
        HeadPostParam.put("data_content", data_content);//加入请求密文
        String PostString  = HttpPayTreUtil.RequestForm(postUrl, HeadPostParam);
        TraceLoggerUtil.info("请求返回："+ PostString);
        PostString = RsaCodingUtil.decryptByPubCerFile(PostString,cerpath);
        if(PostString.isEmpty() || PostString==null){//判断解密是否正确。如果为空则宝付公钥不正确
            TraceLoggerUtil.info("=====检查解密公钥是否正确！");
            return "检查解密公钥是否正确！";
        }
        PostString = SecurityUtil.Base64Decode(PostString);
        TraceLoggerUtil.info("=====返回查询数据解密结果:"+PostString);
        if("xml".equals(data_type)){
            PostString = JXMConvertUtil.XmlConvertJson(PostString);
            TraceLoggerUtil.info("=====返回结果转JSON:"+PostString);
        }
        return PostString;
    }
    /**
     * 绑卡成功，向订单日志中，加一条订单消息
     * @param userInfo
     * @throws Exception
     */
    public void  insertMessage(AppUserInfo userInfo,String type,String title, String content,String order_state) throws Exception {
        List list = new ArrayList();
        String messageId = UUID.randomUUID().toString();
        String date = DateUtils.getDateString(new Date());
        String push = "0";
        String user_id = userInfo.getId();//用户ID
        String jpush_state = "0";
        String orderId = "";
        Map orderIdMap = sunbmpDaoSupport.findForMap("select id from mag_order where USER_ID = '"+userInfo.getId()+"' and order_type='2' ORDER BY CREAT_TIME desc limit 1");
        if(orderIdMap!=null){
            orderId = orderIdMap.get("id").toString();
        }
        String messageSql = "update app_message set update_state = '0' where order_id = '"+orderId+"'";
        list.add(messageSql);
        String regId = userInfo.getRegistration_id();
        if(StringUtils.isNotEmpty(regId)){
            if(!JiGuangUtils.alias(title, content, userInfo.getRegistration_id())){
                jpush_state="1";
            }
        }
        String update = "1";
        if("mySet".equals(type)){
            update = "0";
        }
        String messageinsertSql = "insert into app_message(id,user_id,title,content,creat_time,alter_time,jpush_state,state,update_state,msg_type,push_state,order_state,order_id,order_type)values('"+messageId+"','"+user_id+"','"+title+"','"+content+"','"+date+"','"+date+"','"+jpush_state+"','0','"+update +"','1','"+push+"','"+order_state+"','"+orderId+"','2')";
        list.add(messageinsertSql);
        OrderLogBean orderLog = new OrderLogBean();
        orderLog.setMessageId(messageId);
        //记录订单日志
        orderLog.setTache(title);
        orderLog.setCreatTime(date);
        orderLog.setCreatTimeLog(date);
        orderLog.setAlterTime(date);
        orderLog.setOrderId(orderId);
        orderLog.setOperatorType("0");
        orderLog.setOperatorId(userInfo.getId());
        orderLog.setOperatorName(userInfo.getName());
        if (iAppOrderLogService.setOrderLog(orderLog) == null) {
            TraceLoggerUtil.error( "记录订单日志失败!");
        }
        sunbmpDaoSupport.exeSql(list);
    }

    /**
     * 绑卡成功，向订单日志中，加一条订单消息
     * @param userInfo
     * @throws Exception
     */
    public void  insertMessageNoOrder(AppUserInfo userInfo,String type,String title,String content,String order_state) throws Exception {
        List list = new ArrayList();
        String messageId = UUID.randomUUID().toString();
        String date = DateUtils.getDateString(new Date());
        String push = "0";
        String user_id = userInfo.getId();//用户ID
        String jpush_state = "0";
        String messageSql = "update app_message set update_state = '0' where order_state='" + order_state + "' and order_type='2' and user_id='" + user_id + "'";
        list.add(messageSql);
        String regId = userInfo.getRegistration_id();
        if(StringUtils.isNotEmpty(regId)){
            if(!JiGuangUtils.alias(title, content, userInfo.getRegistration_id())){
                jpush_state="1";
            }
        }
        String update = "1";
        if("mySet".equals(type)){
            update = "0";
        }
        String messageinsertSql = "insert into app_message(id,user_id,title,content,creat_time,alter_time,jpush_state,state,update_state,msg_type,push_state,order_state,order_id,order_type)values('"+messageId+"','"+user_id+"','"+title+"','"+content+"','"+date+"','"+date+"','"+jpush_state+"','0','"+update +"','1','"+push+"','"+order_state+"','','2')";
        list.add(messageinsertSql);
        sunbmpDaoSupport.exeSql(list);
    }
    /**
     * 更新订单表状态，还款计划表状态，以及推送消息
     * @param userInfo
     */
    public void updatePayState(AppUserInfo userInfo,String derateAmount, String amount,String derateId){
        //上一笔订单的提额
        String lifting_proportion = "";
        double person_proportion = 0;
        //先走订单表里面查询订单的id
        String customerSql = "select registration_id,lifting_proportion from  app_user where id = '"+userInfo.getId()+"'";
        Map map = sunbmpDaoSupport.findForMap(customerSql);
        String orderSql = "select id,order_no,periods,user_id,customer_id from mag_order where user_id = '"+userInfo.getId()+"' and order_type='2' ORDER BY CREAT_TIME desc limit 1";
        Map orderMap = sunbmpDaoSupport.findForMap(orderSql);
        orderMap.put("registration_id",map.get("registration_id")+"");
        //先走放款表查询出放款金额和放款时间,以及该笔订单的状态
        String sql = "select id,repayment_amount,state,loan_time,amount from mag_repayment where order_id ='"+orderMap.get("id")+"'";
        Map loanMap = sunbmpDaoSupport.findForMap(sql);
        try {
            //查询出后台配置的提额比例；
            String yieldSql = "select status,pro_quota_limit,pro_quota_proportion from pro_yield";
            List yieldList = sunbmpDaoSupport.findForList(yieldSql);
            Map yieldMap = new HashMap();
            if(yieldList!=null && yieldList.size()>0){
                yieldMap = (Map) yieldList.get(0);
            }
            String quotaType = "";//提额比例状态
            String remark = "";
            //上一笔订单的提额比例
            String beforeQuotaRatio = map.get("lifting_proportion").toString();
            //如果是逾期状态下，那么将提额比例改为0
            if("3".equals(String.valueOf(loanMap.get("state")))){
                lifting_proportion = "0";
                quotaType = "1";//逾期减额
                remark = "逾期减额";
            }else if("1".equals(String.valueOf(loanMap.get("state")))){
                //该状态下是开启提额
                if("1".equals(yieldMap.get("status").toString())){
                    if("0".equals(beforeQuotaRatio)){
                        //如果该客户数据比例为空那么，就用基础比例
                        person_proportion = Double.valueOf(yieldMap.get("pro_quota_proportion").toString());
                    }else{
                        //如果该客户数据的提额比例不为空，那么用这个比例加上基础比例
                        person_proportion =  Double.valueOf(beforeQuotaRatio) + Double.valueOf(yieldMap.get("pro_quota_proportion").toString());
                    }
                    lifting_proportion = String.format("%.0f", person_proportion).toString();
                }else{//未开启提额
                    lifting_proportion = beforeQuotaRatio;
                }
                quotaType = "0";//还款增额
                remark = "还款增额";
            }
            if("".equals(lifting_proportion)){
                lifting_proportion = "0";
            }
            //更新user表的提现额度
            String updateUserSql = "update app_user set lifting_proportion = '"+lifting_proportion+"' where id = '"+orderMap.get("user_id").toString()+"'";
            sunbmpDaoSupport.exeSql(updateUserSql);
            //向提额明细表，增加提额明细.
            String apply_money = "";
            String loan_time = "";
            if(loanMap!=null){
                apply_money = loanMap.get("amount").toString();
                loan_time = loanMap.get("loan_time").toString();
            }
            String quotaDelId = UUID.randomUUID().toString();
            String insertSql = "insert into mag_quota_del (id,order_no,customer_id,type,order_id,periods,current_quota_ratio,before_quota_ratio,loan_time,ALTER_TIME,apply_money,remark) values" +
                    "('" + quotaDelId + "','" + orderMap.get("order_no") + "','"+orderMap.get("customer_id")+"','"+quotaType+"','"+orderMap.get("id")+"','"+orderMap.get("periods")+"','"
                    + lifting_proportion + "','"+ beforeQuotaRatio + "','"+ loan_time + "','"+ DateUtils.getDateString(new Date())+"','"
                    + apply_money + "','" + remark + "')";
            sunbmpDaoSupport.exeSql(insertSql);
            //更新还款计划表
            String  repaymentSql = "update mag_repayment set derate_id = '"+derateId+"',actual_amount='"+amount+"',derate_amount = '"+derateAmount+"',actual_time='"+ DateUtils.getDateString(new Date())+"',state ='" + 2 + "' where order_id ='" + orderMap.get("id").toString() + "'";
            sunbmpDaoSupport.exeSql(repaymentSql);
            //更新订单表状态
            String updateSql = "update mag_order set state = '9',alter_time='" + DateUtils.formatDate("yyyyMMddHHmmss") + "' where id = '"+orderMap.get("id").toString()+"' and order_type='2'";
            sunbmpDaoSupport.exeSql(updateSql);
            //更新减免表状态
            //推送消息
            if(!"".equals(derateId)){
                String updateDerateSql = "update mag_derate set state = '3' where id = '"+derateId+"'";
                sunbmpDaoSupport.exeSql(updateDerateSql);
            }
            try {
                String title = "已结清";
                String order_state = "9";
                String type ="mySet";
                String content = iDictService.getDictInfo("消息内容","ddjq");
                insertMessage(userInfo,type,title,content,order_state);
            }catch (Exception e){
            }
        }catch (Exception e){
        }
    }

    /**
     * 还款支付
     * @param userInfo
     * @param repayIds
     * @return
     * @throws Exception
     */
    @Override
    public Map confirmPayByIds(AppUserInfo userInfo, String repayIds,String redIds) throws Exception {
           /*先走开户表查询该用户的开户信息*/
        String accountSql = "select account_bank_id,account_bank,bank_card,card_num,count_name,tel from mag_customer_account where USER_ID='"+userInfo.getId()+"' and state='0' and channel ='1'";
        Map accountMap = sunbmpDaoSupport.findForMap(accountSql);
        Map returnMap = new HashMap();
        String pay_code = "";//银行卡编码
        String acc_no = "";//银行卡卡号
        String id_card = "";//身份证号码
        String id_holder = "";//姓名
        String mobile ="";//银行预留手机号
        String account_bank="";
        if(accountMap!=null){
            account_bank=accountMap.get("account_bank").toString();//银行名称
            pay_code = accountMap.get("account_bank_id").toString();//银行卡编码
            acc_no = accountMap.get("bank_card").toString();//银行卡卡号
            id_card = accountMap.get("card_num").toString();//身份证号码
            id_holder = accountMap.get("count_name").toString();//姓名
            mobile = accountMap.get("tel").toString();//银行预留手机号
        }else{//如果没有开户信息，则提示用户进行开户
            returnMap.put("flag",false);
            returnMap.put("msg","您当前未绑定银行卡");
            return returnMap;
        }

        String orderId = "";//订单id
        //红包集合
        List<Map> redList = null;
        if(null != redIds && redIds.length() > 0)
        {
            String ids = "";
            for (String redid : redIds.split(","))
            {
                if ("".equals(ids))
                {
                    ids = "'" + redid + "'";
                }
                else
                {
                    ids += ",'" + redid + "'";
                }
            }
            String redSql = "select id,money from mag_red_info where id  in ("+ids+") and is_withdraw = '0'";
            redList = sunbmpDaoSupport.findForList(redSql);
        }
        //每期还款金额
        List<Map> repaymentList = null;
        String[] repaymentIds = repayIds.split(",");
        String ids = "";
        for (String repid : repaymentIds)
        {
            if ("".equals(ids))
            {
                ids = "'" + repid + "'";
            }
            else
            {
                ids += ",'" + repid + "'";
            }
        }
        String repaySql = "select id,order_id as orderId,pay_count,repayment_amount as repaymentAmount ,state ,penalty,default_interest as defaultInterest from mag_repayment " +
                "where id in (" + ids + ")";
        repaymentList = sunbmpDaoSupport.findForList(repaySql);

        if (null != repaymentList && repaymentList.size() > 0)
        {//每期服务包、逾期、减免的费用
            for (Map repayMap : repaymentList)
            {
                String repaymentId = repayMap.get("id").toString();
                Double payMoney = 0.0;//每期实还金额
                String repayState = repayMap.get("state").toString();
                orderId = repayMap.get("orderId").toString();
                if (!repayState.equals("1") && !repayState.equals("3")){
                    returnMap.put("flag",false);
                    returnMap.put("msg","还款失败!请刷新后重新选择还款。");
                    return returnMap;
                }

                Double repaymentAmount = Double.valueOf(repayMap.get("repaymentAmount").toString());//还款金额
                if (repayState.equals("1")){
                    payMoney = DoubleUtils.add(payMoney, repaymentAmount);//每期还款
                }else if (repayState.equals("3")){
                    Double penalty =  Double.valueOf(repayMap.get("penalty").toString());//逾期利息
                    Double defaultInterest =  Double.valueOf(repayMap.get("defaultInterest").toString());//罚息
                    //每期还款
                    payMoney = DoubleUtils.add(payMoney, repaymentAmount);
                    payMoney = DoubleUtils.add(payMoney, penalty);
                    payMoney = DoubleUtils.add(payMoney, defaultInterest);

                    //查询每期还款是否有减免
                    String derateSql = "select id,derate_amount as derateAmount,effective_data as  effectiveData from mag_derate where repayment_id = '"+repaymentId+"' and approval_state = '1' and effective_data > '"+DateUtils.getDateString(new Date())+"'";
                    Map derateMap = sunbmpDaoSupport.findForMap(derateSql);
                    if (derateMap != null && derateMap.size() >0){
                        double derateMoney = Double.valueOf(derateMap.get("derateAmount").toString());
                        //每期还款
                        payMoney = DoubleUtils.sub(payMoney, derateMoney);
                        repayMap.put("derateMap", derateMap);
                    }
                }
                //每期所有服务包的钱
                String sql = "select id,repayment_id as repaymentId,package_name as packageName,period,`month`," +
                        "type,amount, state from service_package_repayment where repayment_id = '"+repaymentId+"' order by period";
                List serPcgList = sunbmpDaoSupport.findForList(sql);
                if(serPcgList != null&& serPcgList.size() > 0){
                    for (int k = 0; k < serPcgList.size();k++){
                        Map map = (Map)serPcgList.get(k);
                        Double amount = Double.parseDouble(map.get("amount").toString());
                        //每期还款
                        payMoney = DoubleUtils.add(payMoney, amount);
                    }
                }

                repayMap.put("payMoney", payMoney);
            }
        }

        //生成红包的使用情况
        if (null != repaymentList && repaymentList.size() > 0 && null != redList && redList.size() > 0)
        {
            getRedUseInfo(repaymentList, redList);
            //设置红包已使用
            for (Map red : redList)
            {
                String redSql = "update mag_red_info set is_withdraw = '1',repayment_id = '"+red.get("repaymentId")+"' where id = '"+red.get("id")+"'";
                sunbmpDaoSupport.exeSql(redSql);
            }
        }
        Double allPayMoney = 0.00;
        if (null != repaymentList && repaymentList.size() > 0)
        {
            for (Map repay : repaymentList)
            {
                if (null == repay.get("payMoney"))
                {
                    repay.put("payMoney", 0.00);
                }
                Double payMoney = Double.valueOf(repay.get("payMoney").toString());
                allPayMoney = DoubleUtils.add(allPayMoney, payMoney);
            }
        }

        //付款金额为0时
        if (allPayMoney == 0)
        {
            returnMap.put("order_stat","");//交易状态
            String actualTime = DateUtils.getDateString(new Date());
            String batch_no = "TID"+System.currentTimeMillis();
            for (int i = 0; i < repaymentList.size();i++){
                String repaymentId = repaymentList.get(i).get("id").toString();
                String nowPeriods=repaymentList.get(i).get("pay_count").toString();
                String redMoney = repaymentList.get(i).get("redMoney") == null ? "" : repaymentList.get(i).get("redMoney").toString();
                String derateMoney = "0";
                if (null != repaymentList.get(i).get("derateMap"))
                {
                    derateMoney = ((Map)repaymentList.get(i).get("derateMap")).get("derateAmount").toString();
                }
                String  repaymentSql = "update mag_repayment set state ='2',red_amount='" + redMoney + "',derate_amount='" + derateMoney + "',actual_amount='"+repaymentList.get(i).get("payMoney")+"',actual_time= '"+actualTime+"' where id ='" + repaymentId + "'";
                sunbmpDaoSupport.exeSql(repaymentSql);
                String order_state = setOrderState(repaymentList.get(i).get("orderId").toString());
                //更新减免状态
                if (null != repaymentList.get(i).get("derateMap"))
                {
                    String updateDerateState="update mag_derate set state='3' where id = '"+((Map)repaymentList.get(i).get("derateMap")).get("id")+"'";
                    sunbmpDaoSupport.exeSql(updateDerateState);
                }

                String content = iDictService.getDictInfo("消息内容","xshkcg");
                //获取合同编号
                String contractSql = "select contract_no from mag_order_contract where order_id = '"+orderId+"'";
                List<Map> contractList = sunbmpDaoSupport.findForList(contractSql);
                String contractNo = "";
                if (null != contractList && contractList.size() > 0)
                {
                    contractNo = (String)contractList.get(0).get("contract_no");
                }
                content = content.replace("$contractNo$", contractNo).replace("$payCount$", nowPeriods);
                insertMessage(userInfo,"mySet","还款成功",content, order_state);
                if ("9".equals(order_state))
                {
                    String ddjq = iDictService.getDictInfo("消息内容","ddjq");
                    insertMessage(userInfo,"mySet","结清成功",ddjq,order_state);
                }

                //向还款明细表插入一条记录
                String detailId = UUID.randomUUID().toString();
                String derateId = "";
                String desc="第"+nowPeriods+"期还款";
                String insertSql = "insert into mag_transaction_details(id,batch_no,amount,repayment_id,order_id,baofu_mag," +
                        "baofu_code,type,state,creat_time,derate_id,derate_amount,success_time,source,bank_name,bank_card,des)values" +
                        "('"+detailId+"','"+batch_no+"','"+repaymentList.get(i).get("payMoney")+"','"+repaymentId+"','"+orderId+"','还款成功','0000','2','"
                        +"1','"+ DateUtils.getDateString(new Date())+"','"+derateId+"','"+derateMoney+"','"+ DateUtils.getDateString(new Date())+"','1','"+account_bank+"','"+acc_no+"','"+desc+"')";
                sunbmpDaoSupport.exeSql(insertSql);
            }
            returnMap.put("flag",true);
            returnMap.put("respCode","0000");//返回code
            returnMap.put("msg","还款成功");//返回的消息
            return returnMap;
        }


        Map<String,String> HeadPostParam = new HashMap<>();//明文参数
        HeadPostParam.put("txn_sub_type", "13"); //交易子类（代扣）
        String trade_date=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());//交易日期
        String terminal_id = iSystemDictService.getInfo("payTreasurePay.terminalId");//终端号
        String member_id = iSystemDictService.getInfo("payTreasure.memberId");//商户编号
        Map<String,Object> XMLArray = new HashMap<>();
        XMLArray.put("txn_sub_type", "13");//交易子类（代扣）
        XMLArray.put("biz_type", "0000");//默认0000,表示为储蓄卡支付。
        XMLArray.put("terminal_id", terminal_id);
        XMLArray.put("member_id",member_id);
        String  txn_amt = String.valueOf(new BigDecimal("0.01").multiply(BigDecimal.valueOf(100)).setScale(0));//支付金额转换成分
       // String  txn_amt = String.valueOf(new BigDecimal(allPayMoney).multiply(BigDecimal.valueOf(100)).setScale(0,BigDecimal.ROUND_HALF_UP));//支付金额转换成分
        XMLArray.put("pay_code", pay_code);
        XMLArray.put("pay_cm", "2");//传默认值
        XMLArray.put("id_card_type", "01");//身份证传固定值。
        XMLArray.put("acc_no", acc_no);//银行卡号
        XMLArray.put("id_card", id_card);//身份证号码
        XMLArray.put("id_holder", id_holder);//姓名
        XMLArray.put("mobile", mobile);//开户手机号码
        XMLArray.put("valid_date", "");//信用卡有效期
        XMLArray.put("valid_no", "");//信用卡安全码
        String trans_id = "TID"+System.currentTimeMillis();//商户订单号
        XMLArray.put("trans_id",trans_id);//商户订单号
        XMLArray.put("txn_amt", txn_amt);
        XMLArray.put("trans_serial_no", "TISN"+System.currentTimeMillis());
        XMLArray.put("trade_date", trade_date);
        XMLArray.put("additional_info", "附加信息");
        XMLArray.put("req_reserved", "保留");
        String result = "";
        String orig_trans_id = "";//原始订单号
        String orig_trade_date = "";////原始订单时间
        String state = "";//该条还款记录的状态
        String code = "";//该条记录所存在的记录状态、
        try {
            result = payTreasure(XMLArray,HeadPostParam,terminal_id);
            JSONObject json = JSON.parseObject(result);
            String respCode = json.getString("resp_code");//返回code
            orig_trans_id = json.getString("trans_id");//原始订单号
            TraceLoggerUtil.info("主动扣款返回的code吗"+respCode);
            if("0000".equals(respCode) || "BF00114".equals(respCode)){
                returnMap.put("order_stat","");//交易状态
                String actualTime = DateUtils.getDateString(new Date());
                for (int i = 0; i < repaymentList.size();i++){
                    String repaymentId = repaymentList.get(i).get("id").toString();
                    String nowPeriods=repaymentList.get(i).get("pay_count").toString();
                    String redMoney = repaymentList.get(i).get("redMoney") == null ? "" : repaymentList.get(i).get("redMoney").toString();
                    String derateMoney = "0";
                    if (null != repaymentList.get(i).get("derateMap"))
                    {
                        derateMoney = ((Map)repaymentList.get(i).get("derateMap")).get("derateAmount").toString();
                    }
                    String  repaymentSql = "update mag_repayment set state ='2',red_amount='" + redMoney + "',derate_amount='" + derateMoney + "',actual_amount='"+repaymentList.get(i).get("payMoney")+"',actual_time= '"+actualTime+"' where id ='" + repaymentId + "'";
                    sunbmpDaoSupport.exeSql(repaymentSql);
                    String order_state = setOrderState(repaymentList.get(i).get("orderId").toString());
                    //更新减免状态
                    if (null != repaymentList.get(i).get("derateMap"))
                    {
                        String updateDerateState="update mag_derate set state='3' where id = '"+((Map)repaymentList.get(i).get("derateMap")).get("id")+"'";
                        sunbmpDaoSupport.exeSql(updateDerateState);
                    }
                    String content = iDictService.getDictInfo("消息内容","xshkcg");
                    //获取合同编号
                    String contractSql = "select contract_no from mag_order_contract where order_id = '"+orderId+"'";
                    List<Map> contractList = sunbmpDaoSupport.findForList(contractSql);
                    String contractNo = "";
                    if (null != contractList && contractList.size() > 0)
                    {
                        contractNo = (String)contractList.get(0).get("contract_no");
                    }
                    content = content.replace("$contractNo$", contractNo).replace("$payCount$", nowPeriods);
                    insertMessage(userInfo,"mySet","还款成功",content,order_state);
                    if ("9".equals(order_state))
                    {
                        String ddjq = iDictService.getDictInfo("消息内容","ddjq");
                        insertMessage(userInfo,"mySet","结清成功",ddjq,order_state);
                    }
                }
                state = "1";//还款成功
            }else if("BF00100".equals(respCode) || "BF00112".equals(respCode)
                    || "BF00113".equals(respCode) || "BF00115".equals(respCode)
                    || "BF00144".equals(respCode) || "BF00202".equals(respCode)){//出现这几种情况，我们则需要调查询类接口，来看该用户的真实还款状态
//                //更新还款计划表 将状态更新为还款确认中，然后在定时任务里面查询处理
                String actualTime = DateUtils.getDateString(new Date());
                for (int i = 0; i < repaymentList.size();i++){
                    String repaymentId = repaymentList.get(i).get("id").toString();
                    String nowPeriods=repaymentList.get(i).get("pay_count").toString();
                    String redMoney = repaymentList.get(i).get("redMoney") == null ? "" : repaymentList.get(i).get("redMoney").toString();
                    String derateMoney = "0";
                    if (null != repaymentList.get(i).get("derateMap"))
                    {
                        derateMoney = ((Map)repaymentList.get(i).get("derateMap")).get("derateAmount").toString();
                    }
                    String  repaymentSql = "update mag_repayment set state ='4',red_amount='" + redMoney + "',derate_amount='" + derateMoney + "',actual_amount='"+repaymentList.get(i).get("payMoney")+"',actual_time= '"+actualTime+"' where id ='" + repaymentId + "'";
                    sunbmpDaoSupport.exeSql(repaymentSql);
                    //更新减免状态
                    if (null != repaymentList.get(i).get("derateMap"))
                    {
                        String updateDerateState="update mag_derate set state='3' where id = '"+((Map)repaymentList.get(i).get("derateMap")).get("id")+"'";
                        sunbmpDaoSupport.exeSql(updateDerateState);
                    }
                }
                state = "0";//待确认
            }else{//这种交易失败被认定为失败
                for (int i = 0; i < repaymentIds.length;i++){
                    String repaymentId = repaymentIds[i];
                    String reSql = "select pay_time from mag_repayment where id = '"+repaymentId+"'";
                    Map repayMap = sunbmpDaoSupport.findForMap(reSql);
                    String repaymentstate= "1";
                    if(null != repayMap && null != repayMap.get("pay_time"))
                    {
                        String nowDay = DateUtils.formatDate(DateUtils.STYLE_3);
                        if (Integer.valueOf(nowDay) > Integer.valueOf(repayMap.get("pay_time").toString().substring(0, 8)))
                        {
                            repaymentstate = "3";
                        }
                    }

                    String  repaymentSql = "update mag_repayment set state ='"+repaymentstate+"' where id ='" + repaymentId + "'";
                    sunbmpDaoSupport.exeSql(repaymentSql);
                    String redSql = "update mag_red_info set is_withdraw = '0' where repayment_id = '"+repaymentId+"'";
                    sunbmpDaoSupport.exeSql(redSql);
                }
                state = "2";//失败
            }
            code = respCode;
            for (int i = 0; i < repaymentList.size();i++){
                String repaymentId = repaymentList.get(i).get("id").toString();
                String nowPeriods=repaymentList.get(i).get("pay_count").toString();
                //向还款明细表插入一条记录
                String detailId = UUID.randomUUID().toString();
                String derateId = "";
                String derateMoney = "";
                if (null != repaymentList.get(i).get("derateMap"))
                {
                    derateId = ((Map)repaymentList.get(i).get("derateMap")).get("id").toString();
                    derateMoney = ((Map)repaymentList.get(i).get("derateMap")).get("derateAmount").toString();
                }
                String desc="第"+nowPeriods+"期还款";
                String insertSql = "insert into mag_transaction_details(id,batch_no,amount,repayment_id,order_id,baofu_mag," +
                        "baofu_code,type,state,creat_time,derate_id,derate_amount,success_time,source,bank_name,bank_card,des)values" +
                        "('"+detailId+"','"+orig_trans_id+"','"+repaymentList.get(i).get("payMoney")+"','"+repaymentId+"','"+orderId+"','"+json.getString("resp_msg")+"','"+code+"','2','"
                        +state +"','"+ DateUtils.getDateString(new Date())+"','"+derateId+"','"+derateMoney+"','"+ DateUtils.getDateString(new Date())+"','1','"+account_bank+"','"+acc_no+"','"+desc+"')";
                sunbmpDaoSupport.exeSql(insertSql);
            }
            returnMap.put("flag",true);
            returnMap.put("respCode",respCode);//返回code
            returnMap.put("msg",json.getString("resp_msg"));//返回的消息
        }catch (Exception e){
            try{
                orig_trans_id = trans_id;
                orig_trade_date = trade_date;
                String resultCheck = payTreasureCheck(orig_trans_id,orig_trade_date);
                JSONObject jsonObject = JSON.parseObject(resultCheck);
                if("S".equals(jsonObject.getString("order_stat"))){
                    String actualTime = DateUtils.getDateString(new Date());
                    for (int i = 0; i < repaymentList.size();i++){
                        String repaymentId = repaymentList.get(i).get("id").toString();
                        String nowPeriods=repaymentList.get(i).get("pay_count").toString();
                        //更新还款计划表 将状态更新为还款成功
                        String redMoney = repaymentList.get(i).get("redMoney") == null ? "" : repaymentList.get(i).get("redMoney").toString();
                        String derateMoney = "0";
                        if (null != repaymentList.get(i).get("derateMap"))
                        {
                            derateMoney = ((Map)repaymentList.get(i).get("derateMap")).get("derateAmount").toString();
                        }
                        String  repaymentSql = "update mag_repayment set state ='2',red_amount='" + redMoney + "',derate_amount='" + derateMoney + "',actual_amount='"+repaymentList.get(i).get("payMoney")+"',actual_time= '"+actualTime+"' where id ='" + repaymentId + "'";
                        sunbmpDaoSupport.exeSql(repaymentSql);
                        String order_state = setOrderState(repaymentList.get(i).get("orderId").toString());
                        String content = iDictService.getDictInfo("消息内容","xshkcg");
                        //获取合同编号
                        String contractSql = "select contract_no from mag_order_contract where order_id = '"+orderId+"'";
                        List<Map> contractList = sunbmpDaoSupport.findForList(contractSql);
                        String contractNo = "";
                        if (null != contractList && contractList.size() > 0)
                        {
                            contractNo = (String)contractList.get(0).get("contract_no");
                        }
                        content = content.replace("$contractNo$", contractNo).replace("$payCount$", nowPeriods);
                        insertMessage(userInfo,"mySet","还款成功",content,order_state);
                        if ("9".equals(order_state))
                        {
                            String ddjq = iDictService.getDictInfo("消息内容","ddjq");
                            insertMessage(userInfo,"mySet","结清成功",ddjq,order_state);
                        }
                        //还款成功更新减免表状态
                        if (null != repaymentList.get(i).get("derateMap"))
                        {
                            String updateDerateState="update mag_derate set state='3' where id = '"+((Map)repaymentList.get(i).get("derateMap")).get("id")+"'";
                            sunbmpDaoSupport.exeSql(updateDerateState);
                        }
                    }
                    state = "1";
                    returnMap.put("msg","支付成功");//返回的消息
                }else if("I".equals(jsonObject.getString("order_stat"))){
                    String actualTime = DateUtils.getDateString(new Date());
                    for (int i = 0; i < repaymentList.size();i++){
                        String repaymentId = repaymentList.get(i).get("id").toString();
                        String nowPeriods=repaymentList.get(i).get("pay_count").toString();
                        String redMoney = repaymentList.get(i).get("redMoney") == null ? "" : repaymentList.get(i).get("redMoney").toString();
                        String derateMoney = "0";
                        if (null != repaymentList.get(i).get("derateMap"))
                        {
                            derateMoney = ((Map)repaymentList.get(i).get("derateMap")).get("derateAmount").toString();
                        }
                        String  repaymentSql = "update mag_repayment set state ='4',red_amount='" + redMoney + "',derate_amount='" + derateMoney + "',actual_amount='"+repaymentList.get(i).get("payMoney")+"',actual_time= '"+actualTime+"' where id ='" + repaymentId + "'";
                        sunbmpDaoSupport.exeSql(repaymentSql);
                        //更新减免状态
                        if (null != repaymentList.get(i).get("derateMap"))
                        {
                            String updateDerateState="update mag_derate set state='3' where id = '"+((Map)repaymentList.get(i).get("derateMap")).get("id")+"'";
                            sunbmpDaoSupport.exeSql(updateDerateState);
                        }
                    }
                    state = "0";//确认中
                    returnMap.put("msg","还款确认中");//返回的消息
                }else{
                    for (int i = 0; i < repaymentIds.length;i++){
                        String repaymentId = repaymentIds[i];
                        String reSql = "select pay_time from mag_repayment where id = '"+repaymentId+"'";
                        Map repayMap = sunbmpDaoSupport.findForMap(reSql);
                        String repaymentstate= "1";
                        if(null != repayMap && null != repayMap.get("pay_time"))
                        {
                            String nowDay = DateUtils.formatDate(DateUtils.STYLE_3);
                            if (Integer.valueOf(nowDay) > Integer.valueOf(repayMap.get("pay_time").toString().substring(0, 8)))
                            {
                                repaymentstate = "3";
                            }
                        }
                        String redSql = "update mag_red_info set is_withdraw = '0' where repayment_id = '"+repaymentId+"'";
                        sunbmpDaoSupport.exeSql(redSql);
                        String  repaymentSql = "update mag_repayment set state ='"+repaymentstate+"' where id ='" + repaymentId + "'";
                        sunbmpDaoSupport.exeSql(repaymentSql);
                    }
                    state = "2";//失败
                    returnMap.put("msg","还款失败");//返回的消息
                }
                code = jsonObject.getString("order_stat");//查询接口中已order_stat状态为准
                returnMap.put("order_stat",jsonObject.getString("order_stat"));//返回code
            }catch (Exception ea){
                for (int i = 0; i < repaymentList.size();i++){
                    String repaymentId = repaymentList.get(i).get("id").toString();
                    //向异常表插入一条记录
                    String exceptionId = UUID.randomUUID().toString();
                    String insertSql = "insert into mag_transaction_exception(id,batch_no,amount,repayment_id,order_id,baofu_mag," +
                            "baofu_code,type,state,creat_time,success_time,source)values" +
                            "('"+exceptionId+"','"+orig_trans_id+"','"+repaymentList.get(i).get("payMoney")+"','"+repaymentId+"','"+orderId+"','主动还款查询接口异常','','2','0','"+DateUtils.getDateString(new Date())+"','"+DateUtils.getDateString(new Date())+"','1')";
                    sunbmpDaoSupport.exeSql(insertSql);

                }
                state ="3";//异常
            }
            for (int i = 0; i < repaymentList.size();i++){
                String repaymentId = repaymentList.get(i).get("id").toString();
                //向还款明细表插入一条记录
                String detailId = UUID.randomUUID().toString();
                String nowPeriods= repaymentList.get(i).get("pay_count").toString();
                String desc="第"+nowPeriods+"期还款";
                String insertSql = "insert into mag_transaction_details(id,batch_no,amount,repayment_id,order_id,baofu_mag," +
                        "baofu_code,type,state,creat_time,success_time,source,bank_name,bank_card,des)values" +
                        "('"+detailId+"','"+orig_trans_id+"','"+repaymentList.get(i).get("payMoney")+"','"+repaymentId+"','"+orderId+"','接口异常','"+code+"','2','"
                        +state +"','"+ DateUtils.getDateString(new Date())+"','"+ DateUtils.getDateString(new Date())+"','1','"+account_bank+"','"+acc_no+"','"+desc+"')";
                sunbmpDaoSupport.exeSql(insertSql);
            }
        }
        return returnMap;
    }

    private void getRedUseInfo(List<Map> repayments, List<Map> reds)
    {
        //存储剩余的还款
        List<Map> repaymentList = new ArrayList<Map>();
        //存储剩余的红包
        List<Map> redList = new ArrayList<Map>();
        //把对应的红包在对应的还款中使用
        for (int i = 0; i < repayments.size(); i++)
        {
            if (i < reds.size())
            {
                Double money = repayments.get(i).get("redMoney") == null ? 0.00 : Double.valueOf(repayments.get(i).get("redMoney").toString());
                String repayIds = null == reds.get(i).get("repaymentId") ? "" : reds.get(i).get("repaymentId").toString();
                if (null != repayments.get(i).get("payMoney") && null != reds.get(i).get("money"))
                {
                    Double repayMoney = Double.valueOf(repayments.get(i).get("payMoney").toString());
                    Double redMoney = Double.valueOf(reds.get(i).get("money").toString());
                    if (null == repayIds || "".equals(repayIds))
                    {
                        reds.get(i).put("repaymentId", String.valueOf(repayments.get(i).get("id")));
                    }
                    else
                    {
                        reds.get(i).put("repaymentId", repayIds + "," + String.valueOf(repayments.get(i).get("id")));
                    }
                    if (repayMoney > redMoney)
                    {
                        repayments.get(i).put("redMoney", DoubleUtils.add(money, redMoney));
                        repayments.get(i).put("payMoney", DoubleUtils.sub(repayMoney, redMoney));
                        repaymentList.add(repayments.get(i));

                    }
                    else if (repayMoney < redMoney)
                    {
                        repayments.get(i).put("redMoney", DoubleUtils.add(money, repayMoney));
                        repayments.get(i).put("payMoney", 0.00);
                        reds.get(i).put("money", DoubleUtils.sub(redMoney, repayMoney));
                        redList.add(reds.get(i));
                    }
                    else
                    {
                        repayments.get(i).put("redMoney", DoubleUtils.add(money, repayMoney));
                        repayments.get(i).put("payMoney", 0.00);
                    }
                }

            }
            else
            {
                repaymentList.add(repayments.get(i));
            }
        }
        if (reds.size() > repayments.size())
        {
            for (int i = repayments.size(); i < reds.size(); i++)
            {
                redList.add(reds.get(i));
            }
        }

        if (null != repaymentList && repaymentList.size() > 0 && null != redList && redList.size() > 0)
        {
            getRedUseInfo(repaymentList, redList);
        }
        else if ((null != redList && redList.size() > 0) && (null == repaymentList || repaymentList.size() == 0))
        {
            //红包有剩余
            for (Map<String, Object> r : redList)
            {
                Double redMoney = Double.valueOf(r.get("money").toString());
                String repayIds = null == r.get("repaymentId") ? "" : r.get("repaymentId").toString();
                Double money = repayments.get(0).get("redMoney") == null ? 0.00 : Double.valueOf(repayments.get(0).get("redMoney").toString());
                repayments.get(0).put("redMoney", DoubleUtils.add(money, redMoney));
                if (null == repayIds || "".equals(repayIds))
                {
                    r.put("repaymentId", String.valueOf(repayments.get(0).get("id")));
                }
                else
                {
                    r.put("repaymentId", repayIds + "," + String.valueOf(repayments.get(0).get("id")));
                }
            }
        }
    }

    //查看该笔还款订单是否为最后一笔,如果是订单状态改为结清,并返回订单状态
    private  String setOrderState(String orderId) throws Exception {
        String repaySql = "select state from mag_repayment  where order_id = '"+orderId+"' ";
        List<Map> repayList = sunbmpDaoSupport.findForList(repaySql);
        boolean orderFlag = true;
        if (null != repayList && repayList.size() > 0)
        {
            for (Map repay : repayList)
            {
                if (!"2".equals(repay.get("state")))
                {
                    orderFlag = false;
                }
            }
        }
        if (orderFlag){
            String orderSql = "update mag_order set state = '9',alter_time='" + DateUtils.formatDate("yyyyMMddHHmmss") + "' where id = '"+orderId+"'";
            sunbmpDaoSupport.exeSql(orderSql);
            return "9";
        }
        return "5";
    }

    //获取当期初始时间
    public static String getTime (String time){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            calendar.setTime(sdf.parse(time));
            calendar.add(Calendar.DAY_OF_MONTH,1);
            calendar.add(Calendar.MONTH,-1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dangqi =sdf.format(calendar.getTime());
        return dangqi;
    }

    /**
     * 提前结清
     * @param userInfo
     * @param orderId
     * @return
     * @throws Exception
     */
    @Override
    public Map earlyClearance(AppUserInfo userInfo, String orderId) throws Exception {
        Map checkMap = checkRepaymentByOrderId(orderId);
        Map returnMap = new HashMap();
        List usulList =  new ArrayList();
        boolean flag =(boolean)checkMap.get("flag");
        if (!flag){
            returnMap.putAll(checkMap);
            return returnMap;
        }
        //判断是否有正在还款的提前结清计划
        String repaySql = "select id,repayment_amount,state,penalty,default_interest as defaultInterest,trans_id as transId from mag_repayment " +
                "where order_id = '"+orderId+"' and state ='4' and settle_type in ('1','2')";
        List repayList = sunbmpDaoSupport.findForList(repaySql);
        if (repayList != null && repayList.size() > 0){
            returnMap.put("flag",false);
            returnMap.put("msg","当前订单存在还款确认中提前结清计划");
            return returnMap;
        }
        //首先获取非正常结清的数据
        String noSettleSql = "select id,settle_type as settleType ,settle_amount as amount,effective_time as effectiveTime,state from mag_settle_record where" +
                " order_id = '"+orderId+"' and settle_type = '1' and state = '1'";
        Map noSettleMap = sunbmpDaoSupport.findForMap(noSettleSql);
        String nowTime = DateUtils.getDateString(new Date()).substring(0,8);
        Double allMoney = 0.0;
        String settleId = "";//结清id
        String settleType = "";//结清类型
        //当存在非正常结清时
        if (noSettleMap != null && noSettleMap.size() > 0 && Integer.parseInt(noSettleMap.get("effectiveTime").toString().substring(0,8))>=Integer.parseInt(nowTime)){
            allMoney = Double.valueOf(noSettleMap.get("amount").toString());//获取非正常提前结清所有金额
            settleId = noSettleMap.get("id").toString();//获取非正常结清id
            settleType = "2";
        }else{
            //正常结清
            String settleSql = "select id,settle_type as settleType ,settle_fee as settleFee,effective_time as effectiveTime,state from mag_settle_record " +
                    "where order_id = '"+orderId+"' and settle_type = '0' and state = '1'";
            Map settleMap = sunbmpDaoSupport.findForMap(settleSql);
            //如果正常结清有数据
            if (settleMap != null && settleMap.size() > 0){
                settleType = "1";
                settleId = settleMap.get("id").toString();//获取正常结清id
                //获取所有还款计划
                List<Map> repaidList = sunbmpDaoSupport.findForList("select id as repaymentId,amount ,pay_count as periods,repayment_amount as repaymentAmount," +
                        "DATE_FORMAT(pay_time,'%Y%m%d') as repaymentTime,state,penalty,overdue_days as overdueDays,default_interest as defaultInterest  " +
                        "from mag_repayment where order_id = '"+orderId+"' and state !=0 and state !=4 and state!=2 order by pay_time desc");
                Double settleFee =Double.valueOf(settleMap.get("settleFee").toString());//结清费用
                if(repaidList!=null && repaidList.size()>0) {
//                    Double  allPrincipalSum = 0.0;//所有未到期本金;
//                    Double  nowRepay = 0.0;//当期待还总金额;
//                    Double  overdueMoney = 0.0;//逾期待还总额;
                    for (int i = 0; i <  repaidList.size(); i++) {
//                        Double repayMoney = 0.0;
//                        Map usulMap = new HashMap();
//                        Map rayMap = repaidList.get(i);
//                        String repayId = rayMap.get("repaymentId").toString();//还款计划Id
//                        String payTime = rayMap.get("repaymentTime").toString();
//                        String findSvcPcgSql = "select amount from service_package_repayment where repayment_id = '" + repayId + "'";
//                        List svcPcgList = sunbmpDaoSupport.findForList(findSvcPcgSql);
//                        double svcAmount = 0.00;
//                        if (svcPcgList != null && svcPcgList.size() > 0) {
//                            for (int j = 0; j < svcPcgList.size(); j++) {
//                                Map svcPcgMap = (Map) svcPcgList.get(j);
//                                svcAmount += svcPcgMap.get("amount") == null ? 0.00 : Double.valueOf(svcPcgMap.get("amount").toString());
//                            }
//                        }
//                        int beginTime = Integer.parseInt(getTime(payTime.substring(0,8)));//每期起始时间
//                        int endTime = Integer.parseInt(payTime.substring(0,8));//还款结束时间
//                        int timeNow = Integer.parseInt(nowTime.substring(0,8));//当前时间
//                        String state = rayMap.get("state").toString();//还款计划状态
//                        String repaymentAmount = rayMap.get("repaymentAmount").toString();//每月还款额
//                        String periods = rayMap.get("periods").toString();//期数
//                        if ("1".equals(state)) {//未还
//                            //当期待还
//                            if (beginTime < timeNow && timeNow < endTime || periods.equals("1")){
//                                allMoney += Double.valueOf(repaymentAmount) + Double.valueOf(svcAmount);//当期金额 = 应还款金额+当期所有服务包的金额
//                                repayMoney += Double.valueOf(repaymentAmount) + Double.valueOf(svcAmount);
//                            }else{
//                                Double principalSum =Double.valueOf(rayMap.get("amount").toString());
////                                allPrincipalSum += principalSum;//未到期总金额+=每期未到期本金
//                                allMoney += principalSum;
//                                repayMoney += principalSum;
//                            }
//                        } else if ("3".equals(state)) {//逾期
//                            Double penalty = Double.valueOf(rayMap.get("penalty").toString());//逾期利息
//                            Double defaultInterest = Double.valueOf(rayMap.get("defaultInterest").toString());//罚息
////                            overdueMoney += penalty+defaultInterest;
//                            allMoney += penalty+defaultInterest+Double.valueOf(repaymentAmount) + Double.valueOf(svcAmount);
//                            repayMoney += penalty+defaultInterest+Double.valueOf(repaymentAmount) + Double.valueOf(svcAmount);
//                        }
//                        usulMap.put("actual_amount",repayMoney);
//                        usulMap.put("repayId",repayId);
//                        usulList.add(i,usulMap);
                        Map rayMap = repaidList.get(i);
                        String repayId = rayMap.get("repaymentId").toString();//还款计划Id
                        String state = rayMap.get("state").toString();//还款计划状态
                        if ("1".equals(state) || "3".equals(state)) {
                            allMoney = DoubleUtils.add(allMoney, Double.valueOf(rayMap.get("amount").toString()));
                            String findSvcPcgSql = "select amount from service_package_repayment where repayment_id = '" + repayId + "'";
                            List svcPcgList = sunbmpDaoSupport.findForList(findSvcPcgSql);
                            if (svcPcgList != null && svcPcgList.size() > 0) {
                                for (int j = 0; j < svcPcgList.size(); j++) {
                                    Map svcPcgMap = (Map) svcPcgList.get(j);
                                    allMoney = DoubleUtils.add(allMoney, svcPcgMap.get("amount") == null ? 0.00 : Double.valueOf(svcPcgMap.get("amount").toString()));
                                }
                            }
                        }
                    }

                }
                allMoney += settleFee;
            }else {
                returnMap.put("flag",false);
                returnMap.put("msg","暂无提前结清计划");
                return  returnMap;
            }
        }
            if (allMoney > 0){
            /*先走开户表查询该用户的开户信息*/
                String accountSql = "select account_bank_id,account_bank,bank_card,card_num,count_name,tel from mag_customer_account where USER_ID='"+userInfo.getId()+"' and state='0' and channel ='1'";
                Map accountMap = sunbmpDaoSupport.findForMap(accountSql);
                String pay_code = "";//银行卡编码
                String acc_no = "";//银行卡卡号
                String id_card = "";//身份证号码
                String id_holder = "";//姓名
                String mobile ="";//银行预留手机号
                String account_bank="";
                if(accountMap!=null){
                    account_bank=accountMap.get("account_bank").toString();//银行名称
                    pay_code = accountMap.get("account_bank_id").toString();//银行卡编码
                    id_card = accountMap.get("card_num").toString();//身份证号码
                    acc_no = accountMap.get("bank_card").toString();//银行卡卡号
                    id_holder = accountMap.get("count_name").toString();//姓名
                    mobile = accountMap.get("tel").toString();//银行预留手机号
                }else{//如果没有开户信息，则提示用户进行开户
                    returnMap.put("flag",false);
                    returnMap.put("msg","您当前未绑定银行卡");
                    return returnMap;
                }
                Map<String,String> HeadPostParam = new HashMap<>();//明文参数
                HeadPostParam.put("txn_sub_type", "13"); //交易子类（代扣）
                String trade_date=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());//交易日期
                String terminal_id = iSystemDictService.getInfo("payTreasurePay.terminalId");//终端号
                String member_id = iSystemDictService.getInfo("payTreasure.memberId");//商户编号
                Map<String,Object> XMLArray = new HashMap<>();
                XMLArray.put("txn_sub_type", "13");//交易子类（代扣）
                XMLArray.put("biz_type", "0000");//默认0000,表示为储蓄卡支付。
                XMLArray.put("terminal_id", terminal_id);
                XMLArray.put("member_id",member_id);
                String  txn_amt = String.valueOf(new BigDecimal("0.01").multiply(BigDecimal.valueOf(100)).setScale(0));//支付金额转换成分
              //  String  txn_amt = String.valueOf(new BigDecimal(allMoney).multiply(BigDecimal.valueOf(100)).setScale(0,BigDecimal.ROUND_HALF_UP));//支付金额转换成分
                XMLArray.put("pay_code", pay_code);
                XMLArray.put("pay_cm", "2");//传默认值
                XMLArray.put("id_card_type", "01");//身份证传固定值。
                XMLArray.put("acc_no", acc_no);//银行卡号
                XMLArray.put("id_card", id_card);//身份证号码
                XMLArray.put("id_holder", id_holder);//姓名
                XMLArray.put("mobile", mobile);//开户手机号码
                XMLArray.put("valid_date", "");//信用卡有效期
                XMLArray.put("valid_no", "");//信用卡安全码
                String trans_id = "TID"+System.currentTimeMillis();//商户订单号
                XMLArray.put("trans_id",trans_id);//商户订单号
                XMLArray.put("txn_amt", txn_amt);
                XMLArray.put("trans_serial_no", "TISN"+System.currentTimeMillis());
                XMLArray.put("trade_date", trade_date);
                XMLArray.put("additional_info", "附加信息");
                XMLArray.put("req_reserved", "保留");
                String result = "";
                String orig_trans_id = "";//原始订单号
                String orig_trade_date = "";////原始订单时间
                String state = "";//该条还款记录的状态
                String code = "";//该条记录所存在的记录状态、
                try {
                    result = payTreasure(XMLArray,HeadPostParam,terminal_id);
                    JSONObject json = JSON.parseObject(result);
                    String respCode = json.getString("resp_code");//返回code
                    orig_trans_id = json.getString("trans_id");//原始订单号
                    TraceLoggerUtil.info("主动扣款返回的code吗"+respCode);
                    if("0000".equals(respCode) || "BF00114".equals(respCode)){
                        String sql =  "update mag_repayment set state = '2',settle_id = '"+settleId+"',settle_type = '"+settleType+"' where order_id = '"+orderId+"' and state in('1','3')";
                        sunbmpDaoSupport.exeSql(sql);
//                        if("2".equals(settleType)){
//                            sql = "update mag_repayment set state = '2',settle_id = '"+settleId+"',settle_type = '"+settleType+"' where order_id = '"+orderId+"' and state in('1','3')";
//                            sunbmpDaoSupport.exeSql(sql);
//                        }else{
//                            String actualTime = DateUtils.getCurrentTime(DateUtils.STYLE_10);
//                            for(int i=0;i<usulList.size();i++){
//                                Map usulMap = (Map)usulList.get(i);
//                                String repayId = usulMap.get("repayId").toString();
//                                String actualAmount = usulMap.get("actual_amount").toString();
//                                //actual_amount='"+actualAmount+"',actual_time='"+actualTime+"',(实际还款时间)
//                                sql = "update mag_repayment set state = '2',settle_id = '"+settleId+"',settle_type = '"+settleType+"' where id='"+repayId+"' and order_id = '"+orderId+"' and state in('1','3')";
//                                sunbmpDaoSupport.exeSql(sql);
//                            }
//                        }
                        state = "1";//还款成功
                        String ordSql = "update mag_order set state = '9',alter_time='" + DateUtils.formatDate("yyyyMMddHHmmss") + "' where id = '"+orderId+"'";//将订单改为结清状态
                        sunbmpDaoSupport.exeSql(ordSql);
                        String loanSql =  "update mag_loan set settle_state = '2' where order_id = '"+orderId+"'";//将放款表中提前结清状态改为提前结清完成 2
                        sunbmpDaoSupport.exeSql(loanSql);
                        String settleSql = "update mag_settle_record set state = '2' where id = '"+settleId+"'";//将提前结清记录表状态改为2已使用；
                        sunbmpDaoSupport.exeSql(settleSql);
                        String ddjq = iDictService.getDictInfo("消息内容","ddjq");
                        insertMessage(userInfo,"mySet","结清成功",ddjq, "9");
                    }else if("BF00100".equals(respCode) || "BF00112".equals(respCode)
                            || "BF00113".equals(respCode) || "BF00115".equals(respCode)
                            || "BF00144".equals(respCode) || "BF00202".equals(respCode)){//出现这几种情况，我们则需要调查询类接口，来看该用户的真实还款状态
                        //更新还款计划表 将状态更新为还款确认中，然后在定时任务里面查询处理
                        String sql = "update mag_repayment set state = '4',settle_id = '"+settleId+"',settle_type = '"+settleType+"' where order_id = '"+orderId+"' and state in('1','3')";
                        sunbmpDaoSupport.exeSql(sql);
                        state = "0";//待确认
                    }else{//这种交易失败被认定为失败
                        state = "2";//失败
                    }
                    code = respCode;
                    //向还款明细表插入一条记录
                    String detailId = UUID.randomUUID().toString();

                    String insertSql = "insert into mag_transaction_details(id,batch_no,amount,repayment_id,order_id,baofu_mag," +
                            "baofu_code,type,state,creat_time,derate_id,derate_amount,success_time,source,bank_name,bank_card,des )values" +
                            "('"+detailId+"','"+orig_trans_id+"','"+allMoney+"','','"+orderId+"','"+json.getString("resp_msg")+"','"+code+"','2','"
                            +state +"','"+ DateUtils.getDateString(new Date())+"','','','"+ DateUtils.getDateString(new Date())+"','1','"+account_bank+"','"+acc_no+"','提前结清')";
                    sunbmpDaoSupport.exeSql(insertSql);
                    returnMap.put("respCode",respCode);//返回code
                    returnMap.put("msg",json.getString("resp_msg"));//返回的消息
                }catch (Exception e){
                    try{
                        orig_trans_id = trans_id;
                        orig_trade_date = trade_date;
                        String resultCheck = payTreasureCheck(orig_trans_id,orig_trade_date);
                        JSONObject jsonObject = JSON.parseObject(resultCheck);
                        if("S".equals(jsonObject.getString("order_stat"))){
                            String sql = "update mag_repayment set state = '2',settle_id = '"+settleId+"',settle_type = '"+settleType+"' where order_id = '"+orderId+"' and state in('1','3')";
                            sunbmpDaoSupport.exeSql(sql);
//                            if("2".equals(settleType)){
//                                sql = "update mag_repayment set state = '2',settle_id = '"+settleId+"',settle_type = '"+settleType+"' where order_id = '"+orderId+"' and state in('1','3')";
//                                sunbmpDaoSupport.exeSql(sql);
//                            }else{
//                                String actualTime = DateUtils.getCurrentTime(DateUtils.STYLE_10);
//                                for(int i=0;i<usulList.size();i++){
//                                    Map usulMap = (Map)usulList.get(i);
//                                    String repayId = usulMap.get("repayId").toString();
//                                    String actualAmount = usulMap.get("actual_amount").toString();
//                                    String.format("%.0f", Float.valueOf(actualAmount)*100);
//                                    sql = "update mag_repayment set state = '2',actual_amount='"+actualAmount+"',actual_time='"+actualTime+"',settle_id = '"+settleId+"',settle_type = '"+settleType+"' where id='"+repayId+"' and order_id = '"+orderId+"' and state in('1','3')";
//                                    sunbmpDaoSupport.exeSql(sql);
//                                }
//                            }
                            String ordSql = "update mag_order set state = '9',alter_time='" + DateUtils.formatDate("yyyyMMddHHmmss") + "' where id = '"+orderId+"'";//将订单改为结清状态
                            sunbmpDaoSupport.exeSql(ordSql);
                            state = "1";//还款成功
                            String loanSql =  "update mag_loan set settle_state = '2' where order_id = '"+orderId+"'";//将放款表中提前结清状态改为提前结清完成 2
                            sunbmpDaoSupport.exeSql(loanSql);
                            String settleSql = "update mag_settle_record set state = '2' where id = '"+settleId+"'";//将提前结清记录表状态改为2已使用；
                            sunbmpDaoSupport.exeSql(settleSql);
                            String ddjq = iDictService.getDictInfo("消息内容","ddjq");
                            insertMessage(userInfo,"mySet","结清成功",ddjq, "9");
                        }else if("I".equals(jsonObject.getString("order_stat"))){
                            //更新还款计划表 将状态更新为还款确认中，然后在定时任务里面查询处理
                            state = "0";//确认中
                            String sql = "update mag_repayment set state = '4',settle_id = '"+settleId+"',settle_type = '"+settleType+"' where order_id = '"+orderId+"' and state in('1','3')";
                            sunbmpDaoSupport.exeSql(sql);
                        }else{
                            state = "2";//失败
                        }
                        code = jsonObject.getString("order_stat");//查询接口中已order_stat状态为准
                        returnMap.put("order_stat",jsonObject.getString("order_stat"));//返回code
                    }catch (Exception ea){
                        //向异常表插入一条记录
                        String exceptionId = UUID.randomUUID().toString();
                        String insertSql = "insert into mag_transaction_exception(id,batch_no,amount,repayment_id,order_id,baofu_mag," +
                                "baofu_code,type,state,creat_time,success_time,again_batch_no,source)values" +
                                "('"+exceptionId+"','"+orig_trans_id+"','"+allMoney+"','','"+orderId+"','接口异常','','2','0','"+ DateUtils.getDateString(new Date())+"','"+ DateUtils.getDateString(new Date())+"','1')";
                        sunbmpDaoSupport.exeSql(insertSql);
                        state ="3";//异常
                    }
                    //向还款明细表插入一条记录
                    String detailId = UUID.randomUUID().toString();
                    String insertSql = "insert into mag_transaction_details(id,batch_no,amount,repayment_id,order_id,baofu_mag," +
                            "baofu_code,type,state,creat_time,success_time,source,bank_name,bank_card,des)values" +
                            "('"+detailId+"','"+orig_trans_id+"','"+allMoney+"','','"+orderId+"','接口异常','"+code+"','2','"
                            +state +"','"+ DateUtils.getDateString(new Date())+"','"+ DateUtils.getDateString(new Date())+"','1','"+account_bank+"','"+acc_no+"','提前结清')";
                    sunbmpDaoSupport.exeSql(insertSql);
                }
            }
            return returnMap;
        }

    /**
     * 根据orderId去查询当前订单中是否存在还款确认中的还款计划
     * @param orderId
     * @return
     */
    private Map checkRepaymentByOrderId(String orderId) throws Exception{
        String repaySql = "select id,repayment_amount,state,penalty,default_interest as defaultInterest,trans_id as transId from mag_repayment " +
                "where order_id = '"+orderId+"' and state ='4' and settle_type = '0'";
        List repayList = sunbmpDaoSupport.findForList(repaySql);
        Map map = new HashMap();
        map.put("flag",true);
        if (repayList != null && repayList.size() >0){
            for (int i = 0; i < repayList.size(); i++){
                Map repayMap =(Map)repayList.get(i);
                String transId =repayMap.get("transId").toString();
                String result =  payTreasureCheck(transId,DateUtils.getDateString(new Date()));
                JSONObject jsonObject = JSON.parseObject(result);
                String state = jsonObject.getString("order_stat");
                //state : I 确认中, S 已付款 ,F 付款失败
                if ("F".equals(state)){
                    String sql = "update mag_order set state = '1' where id = '"+orderId+"'";
                    sunbmpDaoSupport.exeSql(sql);
                }else  if ("S".equals(state)){
                    String sql = "update mag_order set state = '2' where id = '"+orderId+"'";
                    sunbmpDaoSupport.exeSql(sql);
                }else  if ("I".equals(state)){
                    String sql = "update mag_order set state = '4' where id = '"+orderId+"'";
                    sunbmpDaoSupport.exeSql(sql);
                }
            }
        }
        repayList = sunbmpDaoSupport.findForList(repaySql);
        if (repayList != null && repayList.size() > 0){
            map.put("flag",false);
            map.put("msg","当前订单存在还款确认中还款计划");
        }
        return map;
    }

    public static void main(String[] args) {
        BigDecimal bd= new BigDecimal("3.14559265");
        bd=bd.setScale(2,BigDecimal.ROUND_HALF_UP);
        System.out.println(bd);
    }
}
