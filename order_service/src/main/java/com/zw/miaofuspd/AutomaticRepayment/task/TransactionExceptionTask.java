package com.zw.miaofuspd.AutomaticRepayment.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.util.DateUtils;
import com.base.util.StringUtils;
import com.base.util.TraceLoggerUtil;
import com.zhiwang.zwfinance.app.jiguang.util.JiGuangUtils;
import com.zw.api.SendSmsApi;
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
import java.util.*;

/**
 * @Author xiahaiyang
 * @Create 2018年1月19日10:18:01
 **/
public class TransactionExceptionTask extends AbsTask {

    @Autowired
    private ISystemDictService iSystemDictService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IAppOrderLogService iAppOrderLogService;

    @Autowired
    private IDictService iDictService;


    @Override
    public void doWork() throws Exception {
        logger.info("定时执行成功");
        String exceptionSql = "select id,batch_no,amount,derate_id,derate_amount,repayment_id," +
                "order_id,type,state,creat_time from mag_transaction_exception where state ='0' and type !='0' and source='1' ";
        List list = this.sunbmpDaoSupport.findForList(exceptionSql);
        if(list!=null && list.size()>0){
            for (int i = 0; i < list.size();i++){
                Map detailMap =(Map)list.get(i);
                String orderId = detailMap.get("order_id").toString();
                String id = (String)detailMap.get("id");
                String repaymentId = (String)detailMap.get("repayment_id");
                String origTransId = detailMap.get("batch_no").toString();
                String result =  payTreasureCheck(origTransId, DateUtils.getDateString(new Date()));
                JSONObject jsonObject = JSON.parseObject(result);
                String state ="";
                if("S".equals(jsonObject.getString("order_stat"))){
                    //更新还款计划表 将状态更新为还款成功
                    String order_state = "5";
                    order_state = setOrderState(orderId);
                    AppUserInfo userInfo = userService.getUserByOrderId(orderId);
                    if (null != repaymentId && repaymentId.length() > 0)
                    {//计划还款
                        String  repaymentSql = "update mag_repayment set state ='2' where id ='" + repaymentId + "'";
                        sunbmpDaoSupport.exeSql(repaymentSql);
                        //判断是否是提前结清
                        String content = iDictService.getDictInfo("消息内容","xshkcg");
                        String contractSql = "select contract_no from mag_order_contract where order_id = '"+orderId+"'";
                        String  repaySql = "select pay_count from mag_repayment where id ='" + repaymentId + "'";
                        List<Map> contractList = sunbmpDaoSupport.findForList(contractSql);
                        Map repMap = sunbmpDaoSupport.findForMap(repaySql);
                        String contractNo = "";
                        if (null != contractList && contractList.size() > 0)
                        {
                            contractNo = (String)contractList.get(0).get("contract_no");
                        }
                        content = content.replace("$contractNo$", contractNo).replace("$payCount$", repMap == null ? "" : repMap.get("pay_count").toString());
                        insertMessage(userInfo,"mySet","还款成功",content,order_state);
                        //发送短信
                        String contentMsg = iDictService.getDictInfo("短信模板","xshkcg");
                        contentMsg = contentMsg.replace("$contractNo$", contractNo).replace("$payCount$", repMap == null ? "" : repMap.get("pay_count").toString());
                        sendMessage(orderId, contentMsg);
                    }
                    else if ("2".equals(detailMap.get("type")))
                    {//提前结清
                        //更新提前结清表
                        String repaySql = "select settle_id as settleId,settle_type as settleType from mag_repayment where order_id = '"+orderId+"' and settle_id != '' and settle_id is not null limit 0,1";
                        Map repayMap = sunbmpDaoSupport.findForMap(repaySql);
                        if (null != repayMap && null != repayMap.get("settleId"))
                        {
                            String settleSql = "update mag_settle_record set state = '2' where id = '"+repayMap.get("settleId")+"'";
                            sunbmpDaoSupport.exeSql(settleSql);
                        }

                        //更新还款计划表
                        String  repaymentSql = "update mag_repayment set state ='2' where order_id ='" + orderId + "' and state='4' and settle_id != '' and settle_id is not null ";
                        sunbmpDaoSupport.exeSql(repaymentSql);

                    }

                    if ("9".equals(order_state))
                    {
                        String ddjq = iDictService.getDictInfo("消息内容","ddjq");
                        insertMessage(userInfo,"mySet","结清成功",ddjq, "9");
                        //发送短信
                        String ddjqMsg = iDictService.getDictInfo("短信模板","ddjq");
                        sendMessage(orderId, ddjqMsg);
                    }
                    state = "1";
                    String excSql = "update mag_transaction_exception set state = '1' where id = '"+id+"'";
                    sunbmpDaoSupport.exeSql(excSql);
                }else if("I".equals(jsonObject.getString("order_stat"))){
                    //更新还款计划表 将状态更新为还款确认中，然后在定时任务里面查询处理
                    String  repaymentSql = "update mag_repayment set state ='4' where id ='" + repaymentId + "'";
                    sunbmpDaoSupport.exeSql(repaymentSql);
                }else if("F".equals(jsonObject.getString("order_stat"))){
                    AppUserInfo userInfo = userService.getUserByOrderId(orderId);
                    if (null != repaymentId && repaymentId.length() > 0)
                    {
                        //判断是否逾期
                        String repaySql = "select pay_time from mag_repayment where id = '"+repaymentId+"'";
                        Map repayMap = sunbmpDaoSupport.findForMap(repaySql);
                        String repaymentstate= "1";
                        if(null != repayMap && null != repayMap.get("pay_time"))
                        {
                            String nowDay = DateUtils.formatDate(DateUtils.STYLE_3);
                            if (Integer.valueOf(nowDay) > Integer.valueOf(repayMap.get("pay_time").toString().substring(0, 8)))
                            {
                                repaymentstate = "3";
                            }
                        }
                        String  repaymentSql = "update mag_repayment set state ='" + repaymentstate + "',derate_amount='0',actual_amount='',actual_time='',red_amount='' where id ='" + repaymentId + "'";
                        sunbmpDaoSupport.exeSql(repaymentSql);

                        //红包
                        String redSql = "update mag_red_info set is_withdraw = '0' where repayment_id like '%"+repaymentId+"%'";
                        sunbmpDaoSupport.exeSql(redSql);

                        //减免
                        String updateDerateState="update mag_derate set state='1' where repayment_id = '"+repaymentId+"'";
                        sunbmpDaoSupport.exeSql(updateDerateState);

                        String content = iDictService.getDictInfo("消息内容","xshksb");
                        String contractSql = "select contract_no from mag_order_contract where order_id = '"+orderId+"'";
                        String  repSql = "select pay_count from mag_repayment where id ='" + repaymentId + "'";
                        List<Map> contractList = sunbmpDaoSupport.findForList(contractSql);
                        Map repMap = sunbmpDaoSupport.findForMap(repSql);
                        String contractNo = "";
                        if (null != contractList && contractList.size() > 0)
                        {
                            contractNo = (String)contractList.get(0).get("contract_no");
                        }
                        content = content.replace("$contractNo$", contractNo).replace("$payCount$", repMap == null ? "" : repMap.get("pay_count").toString());
                        insertMessage(userInfo,"mySet","还款失败",content,"5");
                        //发送短信
                        String msgcontent = iDictService.getDictInfo("短信模板","xshksb");
                        msgcontent = msgcontent.replace("$contractNo$", contractNo).replace("$payCount$", repMap == null ? "" : repMap.get("pay_count").toString());
                        sendMessage(orderId, msgcontent);
                    }
                    else if ("2".equals(detailMap.get("type")))
                    {//提前结清
                        //更新提前结清表
                        String repaySql = "select settle_id as settleId,settle_type as settleType,id,pay_time as payTime from mag_repayment where order_id = '"+orderId+"' and settle_id != '' and settle_id is not null ";
                        List<Map> repayList = sunbmpDaoSupport.findForList(repaySql);
                        if (null != repayList && repayList.size() > 0)
                        {
                            for (Map pay : repayList)
                            {
                                String repaymentstate= "1";
                                String nowDay = DateUtils.formatDate(DateUtils.STYLE_3);
                                if (Integer.valueOf(nowDay) > Integer.valueOf(pay.get("payTime").toString().substring(0, 8)))
                                {
                                    repaymentstate = "3";
                                }
                                //更新还款计划表
                                String  repaymentSql = "update mag_repayment set state ='" + repaymentstate + "',derate_amount='0',actual_amount='',actual_time='',red_amount='' where id ='" + pay.get("id") + "'";
                                sunbmpDaoSupport.exeSql(repaymentSql);
                            }
                        }
                        String content = iDictService.getDictInfo("消息内容","tqjq");
                        insertMessage(userInfo,"mySet","还款失败",content,"5");
                        //发送短信
                        String msgcontent = iDictService.getDictInfo("短信模板","tqjq");
                        sendMessage(orderId, msgcontent);
                    }
                    state = "2";//失败
                    String excSql = "update mag_transaction_exception set state = '2' where id = '"+id+"'";
                    sunbmpDaoSupport.exeSql(excSql);
                }
            }
        }
    }

    public void sendMessage(String orderId, String content) throws Exception
    {
        Map smsMap = new HashMap();//短信参数
        String host = iSystemDictService.getInfo("sms.app_sms_url");//短信地址
        String account = iSystemDictService.getInfo("sms.app_sms_account");//短信模板。数据字典拿
        String password = iSystemDictService.getInfo("sms.app_sms_password");//短信模板。数据字典拿
        smsMap.put("account",account);
        smsMap.put("password",password);
        String customerId="";
        String tel="";
        String customerSql="select id,tel from  mag_customer where id IN (select CUSTOMER_ID from mag_order  where id='"+orderId+"')";
        List customerList=sunbmpDaoSupport.findForList(customerSql);
        if (customerList!=null&&customerList.size()>0){
            Map customerMap = (Map) customerList.get(0);
            customerId=customerMap.get("id").toString();
            tel=customerMap.get("tel").toString();
        }
        smsMap.put("tel",tel);
        smsMap.put("msg",content);
        SendSmsApi.sendSms(host,smsMap);
    }

    /**
     * 绑卡成功，向订单日志中，加一条订单消息
     * @param userInfo
     * @throws Exception
     */
    public void  insertMessage(AppUserInfo userInfo,String type,String title,String content,String order_state) throws Exception {
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
        orderLog.setOperatorType("2");
        orderLog.setOperatorName("付款确认");
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
        XMLArray.put("terminal_id", terminal_id);
        XMLArray.put("member_id", member_id);
        XMLArray.put("orig_trans_id",orig_trans_id);
        XMLArray.put("biz_type", "0000");
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

        String version = iSystemDictService.getInfo("payTreasure.version");//接口版本号
        String pfxPwd = iSystemDictService.getInfo("payTreasure.pfxPwd");
        String txnType = iSystemDictService.getInfo("payTreasure.txnType");//交易类型
        String member_id = iSystemDictService.getInfo("payTreasure.memberId");//商户编号
        String postUrl = iSystemDictService.getInfo("payTreasure.url");//请求地址
        String data_content = RsaCodingUtil.encryptByPriPfxFile(base64str,pfxpath,pfxPwd);
        HeadPostParam.put("version", version);//接口版本号
        HeadPostParam.put("txn_type", txnType);//交易类型
        HeadPostParam.put("data_type", data_type);//传参类型
        HeadPostParam.put("terminal_id", terminal_id);//终端号
        HeadPostParam.put("member_id", member_id);//商户id
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
}

