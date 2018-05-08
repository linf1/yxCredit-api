package com.zw.miaofuspd.openaccount.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.base.util.DateUtils;
import com.base.util.HttpClientUtil;
import com.base.util.StringUtils;
import com.base.util.TraceLoggerUtil;
import com.yeepay.g3.facade.yop.ca.dto.DigitalEnvelopeDTO;
import com.yeepay.g3.facade.yop.ca.enums.CertTypeEnum;
import com.yeepay.g3.frame.yop.ca.DigitalEnvelopeUtils;
import com.yeepay.g3.sdk.yop.utils.InternalConfig;
import com.zhiwang.zwfinance.app.jiguang.util.JiGuangUtils;
import com.zw.api.HttpUtil;
import com.zw.miaofuspd.facade.dict.service.IDictService;
import com.zw.miaofuspd.facade.dict.service.ISystemDictService;
import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.log.entity.OrderLogBean;
import com.zw.miaofuspd.facade.log.service.IAppOrderLogService;
import com.zw.miaofuspd.facade.openaccount.service.IAppYBOpenAccountService;
import com.zw.miaofuspd.util.YeepayService;
import com.zw.service.base.AbsServiceBase;
import org.apache.http.client.config.RequestConfig;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.*;


@Service("appYBOpenAccountServiceImpl")
public class AppYBOpenAccountServiceImpl extends AbsServiceBase implements IAppYBOpenAccountService {
    @Autowired
    private ISystemDictService iSystemDictService;
    @Autowired
    private IDictService iDictService;
    @Autowired
    private IAppOrderLogService iAppOrderLogService;
    /**
     * 鉴权绑卡请求接口
     * @param authbindcardreqUri
     * @param cardno
     * @param merchantno
     * @param userInfo
     * @return
     * @throws Exception
     */
    @Override
    public Map bindingCard(String authbindcardreqUri, String tel,String host, String cardno, String merchantno, AppUserInfo userInfo) throws Exception {
        String requestno = UUID.randomUUID().toString();
        String identityid = userInfo.getCard(); //用户标识6214855493105072
        String idcardno = userInfo.getCard(); //证件号
        String username = userInfo.getName(); //用户姓名
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String requesttime = format.format(date);//请求时间格式2017-11-01 18:19:20

        String callbackurl = host+"/ybCard/bindingCardTimeOut"; //回调地址
        Map<String,String> ybmap = new HashMap<String, String>();
        ybmap.put("merchantno", merchantno);//商户编号
        ybmap.put("requestno", requestno);//业务请号
        ybmap.put("identityid", identityid);//商户生成的唯一标识
        ybmap.put("identitytype", "ID_CARD");//商户生成的唯一标识类型
        ybmap.put("cardno", cardno);//卡号
        ybmap.put("idcardno", idcardno);//身份证号码
        ybmap.put("idcardtype", "ID");//身份证类型
        ybmap.put("username", username);//用户姓名
        ybmap.put("phone", tel);//手机号码
        ybmap.put("issms", "true");//是否存在短验
        ybmap.put("advicesmstype", "MESSAGE");//建议短信发送类型
        ybmap.put("smstemplateid", "");//定制短验模板ID
        ybmap.put("smstempldatemsg", "");//短信模板
        ybmap.put("avaliabletime", "");//绑卡订单有效期
        ybmap.put("callbackurl", callbackurl);//回调地址
        ybmap.put("requesttime", requesttime);//请求时间
        ybmap.put("authtype", "COMMON_FOUR");//鉴权类型
        ybmap.put("remark", "");//备注
        ybmap.put("extinfos", "");//扩展信息

        //保存业务请求号
        String requestnoSql = "update mag_customer set requestno = '"+requestno+"' where id ='"+userInfo.getCustomer_id()+"'";
        sunbmpDaoSupport.exeSql(requestnoSql);
        String error_num = "5";
        if(StringUtils.isEmpty(error_num)){
            error_num = "5";
        }
        int errorNum = Integer.valueOf(error_num);//总共多少次
        int bank_error_num = 0;
        //走数据库查出来
        String twoCodeSql = "SELECT bank_error_num FROM mag_customer where id = '"+userInfo.getCustomer_id()+"'";
        Map twoCodeMap = sunbmpDaoSupport.findForMap(twoCodeSql);
        if(StringUtils.isNotEmpty(twoCodeMap.get("bank_error_num").toString())){
            bank_error_num = Integer.valueOf(twoCodeMap.get("bank_error_num").toString());
        }
        Map returnMap = new HashMap();
        String appKey=iSystemDictService.getInfo("appKey").toString();
        String secretKey=iSystemDictService.getInfo("secretKey").toString();
        String serverRoot=iSystemDictService.getInfo("serverRoot").toString();//基地址
        String rulrUrl=iSystemDictService.getInfo("rule.url").toString();//规则引擎地址
        Map <String,Object> map = new HashMap<String,Object>();
        JSONObject jsonObject = fourBankVerification(rulrUrl,map);
        String status = jsonObject.getString("status");
        if("0".equals(status)){
            returnMap.put("status",status);
            returnMap.put("msg",jsonObject.get("remark"));
            return returnMap;
        }
        Map<String,String> bindCardMap = new YeepayService().yeepayYOP(ybmap,authbindcardreqUri,appKey,secretKey,serverRoot);
        if (bindCardMap != null) {
            if("FAIL".equals(bindCardMap.get("status"))){
                returnMap.put("msg","系统网络异常");
            }else if ("BIND_ERROR".equals(bindCardMap.get("status"))) {//重新调重发接口(前端调)
                returnMap.put("msg","绑卡异常,请重试");
                returnMap.put("flag",true);
            } else if ("TO_VALIDATE".equals(bindCardMap.get("status"))) { //带短验 调确认接口
                //这个状态下，是成功的，将数据的状态改为0
                String sql = "UPDATE mag_customer SET bank_error_num = 0 where id = '"+userInfo.getCustomer_id()+"'";
                sunbmpDaoSupport.exeSql(sql);
                returnMap.put("msg","绑卡请求成功,等待短信验证");
                returnMap.put("flag",true);
            } else if ("BIND_FAIL".equals(bindCardMap.get("status")) || ("TIME_OUT").equals(bindCardMap.get("status"))) {
                returnMap.put("msg","绑卡失败");
                returnMap.put("flag",false);
                bank_error_num = bank_error_num +1;
                if(bank_error_num>=errorNum){
                    returnMap.put("msg","绑卡错误次数已超过"+errorNum+"次,您已被拒绝绑卡");
                }else{
                    int num=errorNum-bank_error_num;
                    returnMap.put("msg","绑卡错误次数,您还有"+num+"次机会");
                }
                //更新客户表的绑卡错误次数
                String sql = "UPDATE mag_customer SET bank_error_num = '"+bank_error_num+"' where id = '"+userInfo.getCustomer_id()+"'";
                sunbmpDaoSupport.exeSql(sql);
                returnMap.put("flag",false);
            }
            returnMap.put("status",bindCardMap.get("status"));//将状态返给前台
        } else {
            returnMap.put("msg","系统网络异常,请联系客服人员");
            returnMap.put("flag",false);
        }
        return returnMap;
    }

    /**
     * 鉴权绑卡确认接口
     * @param authbindcardconfirmUri
     * @param smsCode
     * @param merchantno
     * @param userInfo
     * @param cardNo
     * @return
     * @throws Exception
     */
    @Override
    public Map bindingCardConfirm(String authbindcardconfirmUri, String smsCode, String merchantno,
                                  AppUserInfo userInfo, String cardNo,String tel,String accountBankName) throws Exception {
        Map ybmap = new HashMap<String, String>();
        String requestno = getRequestno(userInfo.getCustomer_id());
        ybmap.put("merchantno", merchantno);//商户编号
        ybmap.put("requestno", requestno);//业务请号
        ybmap.put("validatecode", smsCode);//短信验证码
        String error_num = iDictService.getDictInfo("绑卡错误次数","0");
        if(StringUtils.isEmpty(error_num)){
            error_num = "5";
        }
        int errorNum = Integer.valueOf(error_num);//总共多少次
        int bank_error_num = 0;
        //走数据库查出来
        String twoCodeSql = "SELECT bank_error_num,requestno FROM mag_customer where id = '"+userInfo.getCustomer_id()+"'";
        Map twoCodeMap = sunbmpDaoSupport.findForMap(twoCodeSql);
        if(StringUtils.isNotEmpty(twoCodeMap.get("bank_error_num").toString())){
            bank_error_num = Integer.valueOf(twoCodeMap.get("bank_error_num").toString());
        }
        Map returnMap = new HashMap();
        ybmap.put("requestno", twoCodeMap.get("requestno").toString());//业务请号
        String appKey=iSystemDictService.getInfo("appKey").toString();
        String secretKey=iSystemDictService.getInfo("secretKey").toString();
        String serverRoot=iSystemDictService.getInfo("serverRoot").toString();//基地址
        Map<String,String> bindCardConfirmMap = new YeepayService().yeepayYOP(ybmap,authbindcardconfirmUri,appKey,secretKey,serverRoot);
        if (bindCardConfirmMap != null) {
            if ("FAIL".equals(bindCardConfirmMap.get("status"))) {//系统异常调 查绑卡关系接口
                returnMap.put("msg","系统网络异常");
                returnMap.put("flag",false);
            } else if ("BIND_ERROR".equals(bindCardConfirmMap.get("status"))) {//重新调重发接口(前端调)
                returnMap.put("msg","绑卡异常,请重试");
                returnMap.put("flag",true);
            }else if ("TO_VALIDATE".equals(bindCardConfirmMap.get("status"))) {//重新调重发接口(前端调)
                returnMap.put("msg","验证码错误,请重试");
                returnMap.put("flag",true);
            }else if ("BIND_FAIL".equals(bindCardConfirmMap.get("status")) || ("TIME_OUT").equals(bindCardConfirmMap.get("status"))) {
                returnMap.put("msg","绑卡失败");
                returnMap.put("flag",false);
                bank_error_num = bank_error_num +1;
                if(bank_error_num>=errorNum){
                    returnMap.put("msg","绑卡错误次数已超过"+errorNum+"次,您已被拒绝绑卡");
                }else{
                    int num=errorNum-bank_error_num;
                    returnMap.put("msg","绑卡错误次数,您还有"+num+"次机会");
                }
                //更新客户表的绑卡错误次数
                String sql = "UPDATE mag_customer SET bank_error_num = '"+bank_error_num+"' where id = '"+userInfo.getCustomer_id()+"'";
                sunbmpDaoSupport.exeSql(sql);
                returnMap.put("flag",false);
            }else if("BIND_SUCCESS".equals(bindCardConfirmMap.get("status"))){//绑卡成功，将数据库状态改为0
                String sql = "UPDATE mag_customer SET bank_error_num = 0 where id = '"+userInfo.getCustomer_id()+"'";
                sunbmpDaoSupport.exeSql(sql);
                String bankcode = bindCardConfirmMap.get("bankcode");
                String bankName = getName(bankcode);
                String yborderid = bindCardConfirmMap.get("yborderid");
                String accountSql = "select * from mag_customer_account where CUSTOMER_ID = '"+userInfo.getCustomer_id()+"' ";
                List list = sunbmpDaoSupport.findForList(accountSql);
                if(list!=null && list.size()>0){//更新开户信息
                    String updateAccountSql = "update mag_customer_account set count_name = '"+userInfo.getName()+"',tel = '"+tel+"',tel = '"+tel+"',account_bank_id = '"+bankcode+"',account_bank = '"+bankName+"',user_name = '"+userInfo.getName()+"',USER_ID = '"+userInfo.getId()+"',card_num = '"+userInfo.getCard()+"',yborder_id = '"+yborderid+"' where CUSTOMER_ID = '"+userInfo.getCustomer_id()+"'";
                    sunbmpDaoSupport.exeSql(updateAccountSql);
                }else{//保存开户信息
                    String id = UUID.randomUUID().toString();
                    String insertSql = "insert into mag_customer_account (id,CUSTOMER_ID,user_id,count_name,tel,user_name,card_num,bank_card," +
                            "account_bank,account_bank_id,CREAT_TIME,ALTER_TIME,state) values" +
                            "('"+id+"','"+userInfo.getCustomer_id()+"','"+userInfo.getId()+"','"+userInfo.getName()+"','"+tel+"','"
                            +userInfo.getName()+"','"+userInfo.getCard()+"','"+cardNo+"','"+bankName+"','"+bankcode+"','"+ DateUtils.getDateString(new Date())+"','"+ DateUtils.getDateString(new Date())+"','0')";
                    sunbmpDaoSupport.exeSql(insertSql);
                }
                //插入订单消息日志
                try {
                    insertMessage(userInfo);
                }catch (Exception e){
                }
                //将客户表的开户状态改为1
                String updatecustomerSql = "update mag_customer set is_openAccount = '"+1+"' where id = '"+userInfo.getCustomer_id()+"'";
                sunbmpDaoSupport.exeSql(updatecustomerSql);
                returnMap.put("flag",true);
            }
            returnMap.put("status",bindCardConfirmMap.get("status"));//将状态返给前台
        } else {
            returnMap.put("msg","系统网络异常,请联系客服人员");
            returnMap.put("flag",false);
        }
        return returnMap;
    }

    /**
     * 鉴权绑卡重发短信接口
     * @param authbindcardresendUri
     * @param merchantno
     * @param userInfo
     * @return
     * @throws Exception
     */
    @Override
    public Map bindingCardResend(String authbindcardresendUri, String merchantno, AppUserInfo userInfo) throws Exception {
        Map returnMap = new HashMap();
        String error_num = iDictService.getDictInfo("绑卡错误次数","0");
        if(StringUtils.isEmpty(error_num)){
            error_num = "5";
        }
        int errorNum = Integer.valueOf(error_num);//总共多少次
        int bank_error_num = 0;
        //走数据库查出来
        String twoCodeSql = "SELECT bank_error_num FROM mag_customer where id = '"+userInfo.getCustomer_id()+"'";
        Map twoCodeMap = sunbmpDaoSupport.findForMap(twoCodeSql);
        if(StringUtils.isNotEmpty(twoCodeMap.get("bank_error_num").toString())){
            bank_error_num = Integer.valueOf(twoCodeMap.get("bank_error_num").toString());
        }
        //发送验证码
        Map ybmap = new HashMap();
        String requestno = getRequestno(userInfo.getCustomer_id());//业务号
        ybmap.put("requestno",requestno);//业务请求号
        ybmap.put("merchantno", merchantno);//商户编号
        ybmap.put("advicesmstype", "MESSAGE");//短信模板
        String appKey=iSystemDictService.getInfo("appKey").toString();
        String secretKey=iSystemDictService.getInfo("secretKey").toString();
        String serverRoot=iSystemDictService.getInfo("serverRoot").toString();//基地址
        Map bindCardResendMap = new YeepayService().yeepayYOP(ybmap,authbindcardresendUri,appKey,secretKey,serverRoot);
        if (bindCardResendMap != null) {
            if ("FAIL".equals(bindCardResendMap.get("status"))) {
                returnMap.put("msg","系统网络异常");
                returnMap.put("flag",false);
            } else if ("TIME_OUT".equals(bindCardResendMap.get("status"))) {
                bank_error_num = bank_error_num +1;
                if(bank_error_num>=errorNum){
                    returnMap.put("msg","绑卡错误次数已超过"+errorNum+"次,您已被拒绝绑卡");
                }else{
                    int num=errorNum-bank_error_num;
                    returnMap.put("msg","绑卡错误次数,您还有"+num+"次机会");
                }
                //更新客户表的绑卡错误次数
                String sql = "UPDATE mag_customer SET bank_error_num = '"+bank_error_num+"' where id = '"+userInfo.getCustomer_id()+"'";
                sunbmpDaoSupport.exeSql(sql);
                returnMap.put("msg","系统超时,请联系客服人员");
                returnMap.put("flag",false);
            } else if ("TO_VALIDATE".equals(bindCardResendMap.get("status"))) {
                //这个状态下，是成功的，将数据的状态改为0
                String sql = "UPDATE mag_customer SET bank_error_num = 0 where id = '"+userInfo.getCustomer_id()+"'";
                sunbmpDaoSupport.exeSql(sql);
                returnMap.put("msg","等待短信验证");
                returnMap.put("flag",true);
            }
            returnMap.put("status",bindCardResendMap.get("status"));
        } else {
            returnMap.put("msg","系统网络异常");
            returnMap.put("flag",false);
        }
        return returnMap;
    }

    /**
     * 鉴权绑卡查询接口
     * @param authListqueryUri
     * @param merchantno
     * @param userInfo
     * @return
     * @throws Exception
     */
    @Override
    public Map bindingCardCheck(String cardNo,String authListqueryUri, String merchantno, AppUserInfo userInfo) throws Exception {
        Map returnMap = new HashMap();
        Map ybmap = new HashMap();
        String requestno = getRequestno(userInfo.getCustomer_id());//业务号
        ybmap.put("requestno",requestno);//业务请求号
        ybmap.put("identityid", userInfo.getCard());//身份标识
        ybmap.put("identitytype", "ID_CARD");//身份标识类型
        String appKey=iSystemDictService.getInfo("appKey").toString();
        String secretKey=iSystemDictService.getInfo("secretKey").toString();
        String serverRoot=iSystemDictService.getInfo("serverRoot").toString();//基地址
        Map<String,String> bindCardResendMap = new YeepayService().yeepayYOP(ybmap,authListqueryUri,appKey,secretKey,serverRoot);
        if("cardlist".equals(bindCardResendMap)){
            String cardList = bindCardResendMap.get("cardlist");
            String data1 = StringUtils.substringBefore(cardList,"]");
            String data2 = data1.substring(11);
            JSONObject json = JSONObject.parseObject(data2);
            String bankcode = json.getString("bankcode");
            String bankName =getName(bankcode);
            String accountSql = "select * from mag_customer_account where CUSTOMER_ID = '"+userInfo.getCustomer_id()+"' ";
            List list = sunbmpDaoSupport.findForList(accountSql);
            if(list!=null && list.size()>0){//更新开户信息
                String updateAccountSql = "update mag_customer_account set account_bank = '"+bankName+"',user_name = '"+userInfo.getName()+"',USER_ID = '"+userInfo.getId()+"',card_num = '"+userInfo.getCard()+"' where CUSTOMER_ID = '"+userInfo.getCustomer_id()+"'";
                sunbmpDaoSupport.exeSql(updateAccountSql);
            }else{//保存开户信息
                String id = UUID.randomUUID().toString();
                String insertSql = "insert into mag_customer_account (id,CUSTOMER_ID,user_id,tel,user_name,card_num,bank_card," +
                        "account_bank,account_bank_id,CREAT_TIME,ALTER_TIME) values" +
                        "('"+id+"','"+userInfo.getCustomer_id()+"','"+userInfo.getId()+"','"+userInfo.getTel()+"','"
                        +userInfo.getName()+"','"+cardNo+"','"+bankName+"','"+bankcode+"','"+ DateUtils.getDateString(new Date())+"','"+ DateUtils.getDateString(new Date())+"')";
                sunbmpDaoSupport.exeSql(insertSql);
            }
            //将客户表的开户状态改为1
            String updatecustomerSql = "update mag_customer set is_openAccount = '"+1+"' where id = '"+userInfo.getCustomer_id()+"'";
            sunbmpDaoSupport.exeSql(updatecustomerSql);
            returnMap.put("msg","已开户");
            returnMap.put("flag",true);
        }else{
            returnMap.put("msg",bindCardResendMap.get("errormsg"));
            returnMap.put("flag",false);
        }
        return returnMap;
    }

    @Override
    public Map getOpenAccountInfo(AppUserInfo userInfo) throws Exception {
        String sql = "select mc.is_openAccount,mc.card,mc.person_name,mca.bank_card,mca.account_bank,mca.tel from mag_customer mc left join mag_customer_account mca on mc.id = mca.CUSTOMER_ID where mc.id='"+userInfo.getCustomer_id()+"'";
        return sunbmpDaoSupport.findForMap(sql);
    }

    @Override
    public Map openAccountCallBack(String response) throws Exception {
        try {
            //开始解密
            Map<String,String> jsonMap  = new HashMap<>();
            DigitalEnvelopeDTO dto = new DigitalEnvelopeDTO();
            dto.setCipherText(response);
            //InternalConfig internalConfig = InternalConfig.Factory.getInternalConfig();
            PrivateKey privateKey = InternalConfig.getISVPrivateKey(CertTypeEnum.RSA2048);
            System.out.println("privateKey: "+privateKey);
            PublicKey publicKey = InternalConfig.getYopPublicKey(CertTypeEnum.RSA2048);
            System.out.println("publicKey: "+publicKey);

            dto = DigitalEnvelopeUtils.decrypt(dto, privateKey, publicKey);
            System.out.println("-------:"+dto.getPlainText());
            jsonMap = parseResponse(dto.getPlainText());
            System.out.println(jsonMap);
            return jsonMap;
        } catch (Exception e) {
            throw new RuntimeException("回调解密失败！");
        }
    }

    public static Map<String, String> parseResponse(String response){
        Map<String,String> jsonMap  = new HashMap<>();
        jsonMap	= JSON.parseObject(response,
                new TypeReference<TreeMap<String,String>>() {});
        return jsonMap;
    }
    /**
     * 根据code获取对应的银行
     * @param bankcode
     * @return
     */
    public static String  getName(String bankcode) {
        if("SPDB".equals(bankcode)){
            return "浦发银行";
        }else if("SZPA".equals(bankcode)){
            return "平安银行";
        }else if("CMBC".equals(bankcode)){
            return "民生银行";
        }else if("GDB".equals(bankcode)){
            return "广发银行";
        }else if("BCCB".equals(bankcode)){
            return "北京银行";
        }else if("ABC".equals(bankcode)){
            return "农业银行";
        }else if("BOCO".equals(bankcode)){
            return "交通银行";
        }else if("ICBC".equals(bankcode)){
            return "工商银行";
        }else if("BOC".equals(bankcode)){
            return "中国银行";
        }else if("CCB".equals(bankcode)){
            return "建设银行";
        }else if("PSBC".equals(bankcode)){
            return "邮政银行";
        }else if("ECITIC".equals(bankcode)){
            return "中信银行";
        }else if("SHB".equals(bankcode)){
            return "上海银行";
        }else if("HX".equals(bankcode)){
            return "华夏银行";
        }else if("CEB".equals(bankcode)){
            return "光大银行";
        }else if("CMBCHINA".equals(bankcode)){
            return "招商银行";
        }else if("CIB".equals(bankcode)){
            return "兴业银行";
        }

        return "";
    }

    /**
     * 获取绑卡第一次调用请求接口的请求编号，后面的接口都要使用
     * @param customerId
     * @return
     */
    public String getRequestno(String customerId){
        String sql = "select requestno from mag_customer where id = '"+customerId+"'";
        Map map = sunbmpDaoSupport.findForMap(sql);
        return  map.get("requestno").toString();
    }

    /**
     * 绑卡成功，向订单日志中，加一条订单消息
     * @param userInfo
     * @throws Exception
     */
    public void  insertMessage(AppUserInfo userInfo) throws Exception {
        List list = new ArrayList();
        String messageId = UUID.randomUUID().toString();
        String content = iDictService.getDictInfo("消息内容","bkcg");
        String title = "绑卡成功";
        String date = DateUtils.getDateString(new Date());
        String push = "0";
        String user_id = userInfo.getId();//用户ID
        String jpush_state = "0";
        String orderId = "";
        Map orderIdMap = sunbmpDaoSupport.findForMap("select id from mag_order where USER_ID = '"+userInfo.getId()+"' ORDER BY CREAT_TIME desc limit 1");
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
        messageSql = "insert into app_message(id,user_id,title,content,creat_time,alter_time,jpush_state,state,update_state,msg_type,push_state,order_id,order_type)values('"+messageId+"','"+user_id+"','"+title+"','"+content+"','"+date+"','"+date+"','"+jpush_state+"','0','1','1','"+push+"','"+orderId+"','2')";
        list.add(messageSql);
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
    //银行四要素验证
    private JSONObject fourBankVerification(String host, Map<String , Object> param) throws Exception {
        try {
            JSONObject json = (JSONObject) JSONObject.toJSON(param);
            String url = host + "/szt/zenxintong/fourElements";
            TraceLoggerUtil.info("三码认证接口发送参数--->" + param);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).setConnectionRequestTimeout(60000).build();
            HttpClientUtil.setRequestConfig(requestConfig);
            String data  = HttpUtil.doPost(url,json.toString());
            TraceLoggerUtil.info("三码认证接口接收结果--->" + data);
            return JSON.parseObject(data);
        } catch (Exception ex) {
            throw ex;
        }
    }
}

