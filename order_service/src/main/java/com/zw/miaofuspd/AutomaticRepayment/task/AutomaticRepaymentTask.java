package com.zw.miaofuspd.AutomaticRepayment.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.util.DateUtils;
import com.base.util.StringUtils;
import com.base.util.TraceLoggerUtil;
import com.zhiwang.zwfinance.app.jiguang.util.JiGuangUtils;
import com.zw.miaofuspd.facade.dict.service.IDictService;
import com.zw.miaofuspd.facade.dict.service.ISystemDictService;
import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.log.entity.OrderLogBean;
import com.zw.miaofuspd.facade.log.service.IAppOrderLogService;
import com.zw.miaofuspd.facade.user.service.IUserService;
import com.zw.miaofuspd.util.JXMConvertUtil;
import com.zw.miaofuspd.util.MapToXml;
import com.zw.miaofuspd.util.SecurityUtil;
import com.zw.miaofuspd.util.rsa.HttpPayTreUtil;
import com.zw.miaofuspd.util.rsa.RsaCodingUtil;
import com.zw.service.task.abs.AbsTask;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author xiahaiyang
 * @Create 2018年1月17日13:55:13
 **/
public class AutomaticRepaymentTask extends AbsTask {

    @Autowired
    ISystemDictService iSystemDictService;
    @Autowired
    private IAppOrderLogService iAppOrderLogService;
    @Autowired
    private IUserService iUserService;

    @Autowired
    private IDictService iDictService;

    /**
     * 定时还款
     *
     * @throws Exception
     */
    @Override
    public void doWork() throws Exception {
        logger.info("定时执行成功");
        //获取所有还款中订单的还款计划
        String repaySql = "SELECT o.ID AS orderId, r.id AS repaymentId, r.repayment_amount AS repaymentAmount, r.pay_count," +
        "r.state as repayState, o.state as orderState, r.pay_time as payTime,o.USER_ID as userId FROM mag_order o JOIN " +
                "mag_repayment r ON r.order_id = o.ID and r.state = '1' and r.pay_time = date_format(now(),'%Y%m%d') " +
                "WHERE o.state = '5' AND o.commodity_state IN ('19', '20') AND o.order_type = '2' and offline_order!='1'";
        List repayList = sunbmpDaoSupport.findForList(repaySql);
        if (repayList != null && repayList.size() > 0) {
            for (int i = 0; i < repayList.size(); i++) {
                Map map = (Map) repayList.get(i);
                String userId = map.get("userId").toString();//用户Id
                String orderId = map.get("orderId").toString();//订单Id
                String nowPeriods=map.get("pay_count").toString();
                String checkSql = "select id,order_id from mag_repayment where order_id = '"+orderId+"' and state = '3'";
                //如果该订单存在逾期,则不进行定时还款
                List checkList = sunbmpDaoSupport.findForList(checkSql);
                if (checkList != null && checkList.size() > 0){
                    continue;
                }
                String repaymentId = map.get("repaymentId").toString();//还款Id
                Double repaymentAmount = Double.valueOf(map.get("repaymentAmount").toString());//应还金额;
                Double amount = repaymentAmount;//应还总金额 = 应还金额+服务包金额
                String servicePcgSql = "select amount from service_package_repayment where repayment_id = '" + repaymentId + "'";
                List servicePcgList = sunbmpDaoSupport.findForList(servicePcgSql);
                for (int j = 0; j < servicePcgList.size(); j++) {
                    Map serMap = (Map) servicePcgList.get(j);
                    Double serAmount = Double.valueOf(serMap.get("amount").toString());//获取每个服务包的金额
                    amount += serAmount;//应还总金额+服务包的金额
                }
                /*先走开户表查询该用户的开户信息*/
                String accountSql = "select account_bank_id,account_bank,bank_card,card_num,count_name,tel from mag_customer_account where USER_ID='"+userId+"' and state='0' and channel ='1'";
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
                    acc_no = accountMap.get("bank_card").toString();//银行卡卡号
                    id_holder = accountMap.get("count_name").toString();//姓名
                    id_card = accountMap.get("card_num").toString();//身份证号码
                    mobile = accountMap.get("tel").toString();//银行预留手机号
                }else{//如果没有开户信息，则提示用户进行开户
                    continue;
                }
                Map<String, String> HeadPostParam = new HashMap<>();//明文参数
                HeadPostParam.put("txn_sub_type", "13"); //交易子类（代扣）
                String trade_date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());//交易日期
                String terminal_id = iSystemDictService.getInfo("payTreasurePay.terminalId");//终端号
                String member_id = iSystemDictService.getInfo("payTreasure.memberId");//商户编号
                Map<String, Object> XMLArray = new HashMap<>();
                XMLArray.put("txn_sub_type", "13");//交易子类（代扣）
                XMLArray.put("biz_type", "0000");//默认0000,表示为储蓄卡支付。
                XMLArray.put("terminal_id", terminal_id);
                XMLArray.put("member_id", member_id);
                String  txn_amt = String.valueOf(new BigDecimal("0.01").multiply(BigDecimal.valueOf(100)).setScale(0));//支付金额转换成分
              //  String txn_amt = String.valueOf(new BigDecimal(amount).multiply(BigDecimal.valueOf(100)).setScale(0,BigDecimal.ROUND_HALF_UP));//支付金额转换成分
                XMLArray.put("pay_code", pay_code);
                XMLArray.put("pay_cm", "2");//传默认值
                XMLArray.put("id_card_type", "01");//身份证传固定值。
                XMLArray.put("acc_no", acc_no);//银行卡号
                XMLArray.put("id_card", id_card);//身份证号码
                XMLArray.put("id_holder", id_holder);//姓名
                XMLArray.put("mobile", mobile);//开户手机号码
                XMLArray.put("valid_date", "");//信用卡有效期
                XMLArray.put("valid_no", "");//信用卡安全码
                String trans_id = "TID" + System.currentTimeMillis();//商户订单号
                XMLArray.put("trans_id", trans_id);//商户订单号
                XMLArray.put("txn_amt", txn_amt);
                XMLArray.put("trans_serial_no", "TISN" + System.currentTimeMillis());
                XMLArray.put("trade_date", trade_date);
                XMLArray.put("additional_info", "附加信息");
                XMLArray.put("req_reserved", "保留");
                String result = "";
                String orig_trans_id = "";//原始订单号
                String orig_trade_date = "";////原始订单时间
                String state = "";//该条还款记录的状态
                String code = "";//该条记录所存在的记录状态、
                try {
                    result = payTreasure(XMLArray, HeadPostParam, terminal_id);
                    JSONObject json = JSON.parseObject(result);
                    String respCode = json.getString("resp_code");//返回code
                    orig_trans_id = json.getString("trans_id");//原始订单号
                    TraceLoggerUtil.info("主动扣款返回的code吗" + respCode);
                    if ("0000".equals(respCode) || "BF00114".equals(respCode)) {
                        String actualTime = DateUtils.getDateString(new Date());
                        String repaymentSql = "update mag_repayment set state ='2',actual_amount='"+amount+"',actual_time= '"+actualTime+"' where id ='" + repaymentId + "'";
                        sunbmpDaoSupport.exeSql(repaymentSql);
                        String order_state = setOrderState(orderId);
                        AppUserInfo userInfo = iUserService.getUserByOrderId(orderId);
                        String content = iDictService.getDictInfo("消息内容","xshkcg");
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
                            insertMessage(userInfo,"mySet","结清成功",ddjq, "9");
                        }
                        state = "1";//还款成功
                    } else if ("BF00100".equals(respCode) || "BF00112".equals(respCode)
                            || "BF00113".equals(respCode) || "BF00115".equals(respCode)
                            || "BF00144".equals(respCode) || "BF00202".equals(respCode)) {//出现这几种情况，我们则需要调查询类接口，来看该用户的真实还款状态
//                //更新还款计划表 将状态更新为还款确认中，然后在定时任务里面查询处理
                            String repaymentSql = "update mag_repayment set state ='4', actual_amount='"+amount+"',actual_time= '"+DateUtils.getDateString(new Date())+"' where id ='" + repaymentId + "'";
                            sunbmpDaoSupport.exeSql(repaymentSql);
                        state = "0";//待确认
                    } else {//这种交易失败被认定为失败
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
                            String repaymentSql = "update mag_repayment set state ='"+repaymentstate+"' where id ='" + repaymentId + "'";
                            sunbmpDaoSupport.exeSql(repaymentSql);
                        AppUserInfo userInfo = iUserService.getUserByOrderId(orderId);
                        String content = iDictService.getDictInfo("消息内容","xshksb");
                        String contractSql = "select contract_no from mag_order_contract where order_id = '"+orderId+"'";
                        List<Map> contractList = sunbmpDaoSupport.findForList(contractSql);
                        String contractNo = "";
                        if (null != contractList && contractList.size() > 0)
                        {
                            contractNo = (String)contractList.get(0).get("contract_no");
                        }
                        content = content.replace("$contractNo$", contractNo).replace("$payCount$", nowPeriods);
                        insertMessage(userInfo,"mySet","还款失败",content,"5");
                        state = "2";//失败
                    }
                    code = respCode;
                        //向还款明细表插入一条记录
                        String detailId = UUID.randomUUID().toString();
                        String desc="第"+nowPeriods+"期还款";
                        String insertSql = "insert into mag_transaction_details(id,batch_no,amount,repayment_id,order_id,baofu_mag," +
                                "baofu_code,type,state,creat_time,derate_id,derate_amount,success_time,source,bank_name,bank_card,des)values" +
                                "('" + detailId + "','" + orig_trans_id + "','" + amount + "','" + repaymentId + "','" + orderId + "','" + json.getString("resp_msg") + "','" + code + "','2','"
                                + state + "','" + DateUtils.getDateString(new Date()) + "','','','" + DateUtils.getDateString(new Date()) + "','1','"+account_bank+"','"+acc_no+"','"+desc+"')";
                        sunbmpDaoSupport.exeSql(insertSql);
                } catch (Exception e) {
                    try {
                        orig_trans_id = trans_id;
                        orig_trade_date = trade_date;
                        String resultCheck = payTreasureCheck(orig_trans_id, orig_trade_date);
                        JSONObject jsonObject = JSON.parseObject(resultCheck);
                        if ("S".equals(jsonObject.getString("order_stat"))) {
                                //更新还款计划表 将状态更新为还款成功
                                String repaymentSql = "update mag_repayment set state ='2' actual_amount='"+amount+"',actual_time= '"+DateUtils.getDateString(new Date())+"' where id ='" + repaymentId + "'";
                                sunbmpDaoSupport.exeSql(repaymentSql);
                            String order_state = setOrderState(orderId);
                            AppUserInfo userInfo = iUserService.getUserByOrderId(orderId);
                            String content = iDictService.getDictInfo("消息内容","xshkcg");
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
                                insertMessage(userInfo,"mySet","结清成功",ddjq, "9");
                            }
                            state = "1";
                        } else if ("I".equals(jsonObject.getString("order_stat"))) {
                                //更新还款计划表 将状态更新为还款确认中，然后在定时任务里面查询处理
                                String repaymentSql = "update mag_repayment set state ='4' actual_amount='"+amount+"',actual_time= '"+DateUtils.getDateString(new Date())+"' where id ='" + repaymentId + "'";
                                sunbmpDaoSupport.exeSql(repaymentSql);
                            state = "0";//确认中
                        } else {
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
                                String repaymentSql = "update mag_repayment set state ='"+repaymentstate+"' where id ='" + repaymentId + "'";
                                sunbmpDaoSupport.exeSql(repaymentSql);
                            AppUserInfo userInfo = iUserService.getUserByOrderId(orderId);
                            String content = iDictService.getDictInfo("消息内容","xshksb");
                            String contractSql = "select contract_no from mag_order_contract where order_id = '"+orderId+"'";
                            List<Map> contractList = sunbmpDaoSupport.findForList(contractSql);
                            String contractNo = "";
                            if (null != contractList && contractList.size() > 0)
                            {
                                contractNo = (String)contractList.get(0).get("contract_no");
                            }
                            content = content.replace("$contractNo$", contractNo).replace("$payCount$", nowPeriods);
                            insertMessage(userInfo,"mySet","还款失败",content,"5");
                            state = "2";//失败
                        }
                        code = jsonObject.getString("order_stat");//查询接口中已order_stat状态为准
                    } catch (Exception ea) {
                            //向异常表插入一条记录
                            String exceptionId = UUID.randomUUID().toString();
                            String insertSql = "insert into mag_transaction_exception(id,batch_no,amount,repayment_id,order_id,baofu_mag," +
                                    "baofu_code,type,state,creat_time,success_time,source)values" +
                                    "('" + exceptionId + "','" + orig_trans_id + "','" + amount + "','" + repaymentId + "','" + orderId + "','主动还款查询接口异常','','2','0','" + DateUtils.getDateString(new Date()) + "','" + DateUtils.getDateString(new Date()) + "','1')";
                            sunbmpDaoSupport.exeSql(insertSql);

                        state = "3";//异常
                    }
                        //向还款明细表插入一条记录
                        String detailId = UUID.randomUUID().toString();
                        String desc="第"+nowPeriods+"期还款";
                        String insertSql = "insert into mag_transaction_details(id,batch_no,amount,repayment_id,order_id,baofu_mag," +
                                "baofu_code,type,state,creat_time,success_time,source,bank_name,bank_card,des)values" +
                                "('" + detailId + "','" + orig_trans_id + "','" + amount + "','" + repaymentId + "','" + orderId + "','接口异常','" + code + "','2','"
                                + state + "','" + DateUtils.getDateString(new Date()) + "','" + DateUtils.getDateString(new Date()) + "','1' ,'"+account_bank+"','"+acc_no+"','"+desc+"')";
                        sunbmpDaoSupport.exeSql(insertSql);
                }
            }
        }
    }

    /**
     * 绑卡成功，向订单日志中，加一条订单消息
     * @param userInfo
     * @throws Exception
     */
    public void  insertMessage(AppUserInfo userInfo, String type, String title, String content, String order_state) throws Exception {
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
        orderLog.setAlterTime(date);
        orderLog.setOrderId(orderId);
        if (iAppOrderLogService.setOrderLog(orderLog) == null) {
            TraceLoggerUtil.error( "记录订单日志失败!");
        }
        sunbmpDaoSupport.exeSql(list);
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
     * 宝付查询接口
     */
    public String payTreasureCheck(String orig_trans_id,String orig_trade_date) throws Exception{
        TraceLoggerUtil.info("=======查询订单接口31=========");
        String terminal_id = iSystemDictService.getInfo("payTreasurePay.terminalId");//终端号`
        String member_id = iSystemDictService.getInfo("payTreasure.memberId");//商户编号
        Map<String,String> HeadPostParam = new HashMap<>();//明文参数
        HeadPostParam.put("txn_sub_type", "31");
        Map<String,Object> XMLArray = new HashMap<String,Object>();
        XMLArray.put("txn_sub_type", "31");
        XMLArray.put("terminal_id", terminal_id);
        XMLArray.put("biz_type", "0000");
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
}
