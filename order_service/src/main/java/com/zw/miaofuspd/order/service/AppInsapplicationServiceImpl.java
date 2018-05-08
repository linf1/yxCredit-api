package com.zw.miaofuspd.order.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.util.DateUtils;
import com.base.util.TraceLoggerUtil;
import com.zhiwang.zwfinance.app.jiguang.util.JiGuangUtils;
import com.zw.miaofuspd.facade.dict.service.IDictService;
import com.zw.miaofuspd.facade.dict.service.ISystemDictService;
import com.zw.miaofuspd.facade.entity.*;
import com.zw.miaofuspd.facade.log.entity.MessageBean;
import com.zw.miaofuspd.facade.log.entity.OrderLogBean;
import com.zw.miaofuspd.facade.log.service.IAppOrderLogService;
import com.zw.miaofuspd.facade.order.service.AppOrderService;
import com.zw.miaofuspd.facade.order.service.IAppInsapplicationService;
import com.zw.miaofuspd.facade.user.service.IMsgService;
import com.zw.miaofuspd.util.CommonUtil;
import com.zw.miaofuspd.util.HttpUtil;
import com.zw.miaofuspd.util.NonceUtil;
import com.zw.service.base.AbsServiceBase;
import org.dom4j.Branch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("appInsapplicationServiceImpl")
public class AppInsapplicationServiceImpl extends AbsServiceBase implements IAppInsapplicationService {
    @Autowired
    private IAppOrderLogService iAppOrderLogService;
    @Autowired
    private IDictService iDictService;
    @Autowired
    private ISystemDictService iSystemDictService;
    @Autowired
    private IMsgService msgService;
    @Autowired
    private AppOrderService appOrderService;
    @Autowired
    private  IAppInsapplicationService iAppInsapplicationService;
    /**
     * 秒付金服-进件提交接口
     * @param userInfo
     * @param order_id
     * @return
     * @throws Exception
     */
    @Override
    public Map makeOrder(AppUserInfo userInfo, String order_id) throws Exception {
        Map outMap=new HashMap();
        String state = "";//状态
        state = "1"; //将订单状态改为1
        //在提交订单之前先查一下该笔订单的风控规则如果有一个风控拒绝，那么该笔订单就被定为失败
        String ruleSql = "select is_sign,emp_id,person_rule_state,identry_rule_state from mag_order where id = '"+order_id+"' and order_type='2'";
        Map map = sunbmpDaoSupport.findForMap(ruleSql);
        if(map!=null){
            if("0".equals(map.get("identry_rule_state").toString())|| "0".equals(map.get("person_rule_state").toString())){
                state = "3";
                String sql = "update app_user set order_refused_time='"+ DateUtils.getDateString(new Date())+"' where id ='"+userInfo.getId()+"'";
                sunbmpDaoSupport.exeSql(sql);
                msgService.insertOrderMsg("8",order_id);
            }
            if("1".equals(map.get("is_sign").toString())){//办单员标记内拒单
                state = "3.5";
                String sql = "update app_user set order_refused_time='"+ DateUtils.getDateString(new Date())+"' where id ='"+userInfo.getId()+"'";
                sunbmpDaoSupport.exeSql(sql);
                msgService.insertOrderMsg("8",order_id);
            }
        }
        //将订单表中的状态改为借款申请
        String orderSql = "update mag_order set state = '"+state+"',ALTER_TIME = '"+ DateUtils.getDateString(new Date())+"' where id = '"+order_id+"'";
        sunbmpDaoSupport.exeSql(orderSql);
        outMap.put("flag",true);
        outMap.put("msg","提交成功!");
            return outMap;
    }
//
//    //查询天域分(二次进件风控表order_id是否存在)
//    public Map isTrueScore(String id) {
//        String sql = "select id,order_id,USER_ID,risk_score from rule_result where engineId='286L' and rule_name = '天御' and order_id='"+id+"'";
//        Map map = sunbmpDaoSupport.findForMap(sql);
//        Map isMap = new HashMap();
//        if (map != null) {
//            isMap.put("order_id", map.get("id"));
//            isMap.put("flag", false);
//            return isMap;
//        }
//        isMap.put("flag", true);
//        return isMap;
//    }



    public void sendMessage(AppUserInfo userInfo,String order_id,String state,String code,String tache,String now,String changeValue) throws Exception{
        OrderLogBean orderLog = iAppOrderLogService.getOrderById(order_id);//获取订单
        if (orderLog == null) {
            //获取订单失败直接抛出异常
            TraceLoggerUtil.error("获取订单失败!");
        }
        //将订单表中的状态改为借款申请
        String orderSql = "update mag_order set state = '"+state+"',ALTER_TIME = '"+ DateUtils.getDateString(new Date())+"' where id = '"+order_id+"'";
        sunbmpDaoSupport.exeSql(orderSql);
        //插入消息日志
        String title = tache;
        String content = iDictService.getDictInfo("消息内容",code);
        String messageState = "0";//表示站内信未读
        String push_state = "0";
        if (userInfo.getRegistration_id() != null&&!userInfo.getRegistration_id().equals("")) {
            if (!JiGuangUtils.alias(title, content, userInfo.getRegistration_id())) {
                push_state = "1";
            }
        }
        MessageBean message = new MessageBean();
        message.setTitle(title);
        message.setContent(content);
        message.setUserId(userInfo.getId());
        message.setCreateTime(now);
        message.setUpdateTime(now);
        message.setState(messageState);
        message.setPushState(push_state);
        message.setOrder_state(state);
        message.setMsgType("1");
        message.setUpdateState("0");//将上一条数据的状态改为0
        String messageId = iAppOrderLogService.setAppMessage(message, order_id);
        if (messageId == null) {
            TraceLoggerUtil.error( "记录消息记录失败");
        }
        orderLog.setMessageId(messageId);
        //记录订单日志
        String orderInfoSql="select order_no, USER_ID,PERIODS,product_type,CUSTOMER_ID,CUSTOMER_NAME,CARD,tel,product_type_name,product_name_name,AMOUNT from mag_order where id='"+order_id+"'";
        Map map=sunbmpDaoSupport.findForMap(orderInfoSql);
        orderLog.setUserId(map.get("USER_ID").toString());
        orderLog.setCard(map.get("CARD").toString());
        orderLog.setCustomerId(map.get("CUSTOMER_ID").toString());
        orderLog.setPeriods(map.get("PERIODS").toString());
        orderLog.setCustomerName(map.get("CUSTOMER_NAME").toString());
        orderLog.setAmount(map.get("AMOUNT").toString());
        orderLog.setTel(map.get("tel").toString());
        orderLog.setProductTypeName(map.get("product_type_name").toString());
        orderLog.setProductNameName(map.get("product_name_name").toString());
        orderLog.setOrderNo(map.get("order_no").toString());
        orderLog.setProductType(map.get("product_type").toString());
        orderLog.setState(state);//借款申请
        orderLog.setTache(tache);
        orderLog.setChangeValue(changeValue);
        orderLog.setOperatorId(map.get("USER_ID").toString());
        orderLog.setOperatorType("0");
        orderLog.setCreatTimeLog(now);
        orderLog.setOperatorName(map.get("CUSTOMER_NAME").toString());
        if (iAppOrderLogService.setOrderLog(orderLog) == null) {
            TraceLoggerUtil.error( "记录订单日志失败!");
        }
    }

    public JSONObject CreditForRisk(Map inMap) throws Exception {
        Map orderMap = appOrderService.getOrderById(inMap.get("orderId").toString());
        String userId=orderMap.get("userId").toString();
        //根据业务员获取对应的部门，递归获取分公司以及总公司
        String empId=orderMap.get("empId").toString();
        Map saleMap=appOrderService.getSaleInfo(empId);
        String branchId=saleMap.get("branch").toString();
        BranchInfo branchInfo=appOrderService.getGongSiName(appOrderService.getBranchInfo(branchId));
        CreditResponse response = new CreditResponse();
        CreditRequest request = new CreditRequest();
        //动作
        request.setAct("query");

        //时间戳
        request.setTs(System.currentTimeMillis());

        //API调用方生成的随机串：8-10位随机字符串(a-z,0-9)仅包含小写字母和数字
        request.setNonce(NonceUtil.nonce(NonceUtil.LOWERCHARANDNUMBER,8));

        //流水号：唯一标识，每次调用不一样
//			request.setPid(654321000+"");
        String pid = UUID.randomUUID()+"";
        request.setPid(pid);

        // 产品ID
        request.setProductId("miaofu");

        //产品名称
        request.setProductName("miaofu");
        //产品系列名称
        request.setProductSeriesName("miaofu");
        //引擎版本，1:获取当前引擎正在运行版本,2:获取当前引擎中对应的版本，默认值：1

        request.setReqtype(1);
        //授权token
        request.setToken("123456");

        //引擎子版本,reqtype=2的时候才需赋值
        //request.setSubversion();

        //是否同步调用，默认值：true
        String engineId = "";
        String rule_name="";
        Map<String ,Object> data = new HashMap<String, Object>();
        if("0".equals(inMap.get("type").toString())){//天域规则
            request.setEngineId(286L);
            engineId = "286L";
            rule_name="天御";
            request.setSync(true);
        }else{//同盾规则
            request.setEngineId(287L);
            engineId = "287L";
            rule_name="增信通,同盾";
            request.setSync(false);
            data.put("type", inMap.get("tongduType"));
            if("WEB".equals(inMap.get("type"))){//微信端S
                data.put("tokenId", inMap.get("blackBox"));
                data.put("blackBox", "");
            }else{//app端
                data.put("tokenId", "");
                data.put("blackBox", inMap.get("blackBox"));
            }
        }
        //用户编号
        request.setUid("liudehua");
        //签名：sign签名算法:根据act,ts,nonce,pid,uid,token,按顺序用‘,’连接在一起，做md5签名。
        //即sign = md5(act,ts,nonce,pid,uid,token)
        request.setSign(CommonUtil.generateSign(request, "123456"));
        CreditRequestParam param = new CreditRequestParam();
        data.put("companyName",branchInfo.getDeptName());
        data.put("companyId",branchInfo.getId());
        data.put("busType",2);
        //=================================
        param.setIdNum(orderMap.get("card").toString());
        param.setName(orderMap.get("customerName").toString());
        param.setMobile(orderMap.get("tel").toString());
        param.setData(data);
        request.setProductSeriesName("test");
        request.setCreditRequestParam(param);
        JSONObject json = (JSONObject) JSONObject.toJSON(request);
        String result;
        String host= iSystemDictService.getInfo("rule.url");
        String url = host+"/szt/credit/application";
        JSONObject jsonResult;
        JSONObject jsonScore =new JSONObject();
        try{
            result  = HttpUtil.doPost(url, json.toString());
            jsonResult = JSON.parseObject(result);
            String id = UUID.randomUUID().toString();
            String sql="";
            if(!"287L".equals(engineId)){
                try{
                    //查天域分
                    TraceLoggerUtil.info("================查天域分开始==================");
                    String sql1="select tel,PERSON_NAME,card from mag_customer where user_id='"+userId+"'";
                    Map cusInfo=sunbmpDaoSupport.findForMap(sql1);
                    Map cusMap=new HashMap();
                    cusMap.put("tel",cusInfo.get("tel").toString());
                    cusMap.put("realname",cusInfo.get("PERSON_NAME").toString());
                    cusMap.put("card",cusInfo.get("card").toString());
                    cusMap.put("companyName",branchInfo.getDeptName());
                    cusMap.put("companyId",branchInfo.getId());
                    cusMap.put("busType","2");
                    jsonScore=iAppInsapplicationService.tencentRule(cusMap);
                    TraceLoggerUtil.info("================查天域分=================="+jsonScore);
                }catch (Exception e){
                }
                sql ="insert into rule_result (id,order_id,user_id,pid,engineId,state,create_time,rule_json,rule_name,risk_score) values('"+id+"','"+orderMap.get("id")+"','"+orderMap.get("userId")+"','"+pid+"','"+engineId+"','"+jsonResult.getString("result")+"','"+ DateUtils.getDateString(new Date())+"','"+jsonResult+"','"+rule_name+"','"+jsonScore+"')";
            }else{
                sql = "insert into rule_result (id,order_id,user_id,pid,engineId,create_time,rule_name) values('"+id+"','"+orderMap.get("id")+"','"+orderMap.get("userId")+"','"+pid+"','"+engineId+"','"+ DateUtils.getDateString(new Date())+"','"+rule_name+"')";
            }
            sunbmpDaoSupport.exeSql(sql);
            System.out.println("================================================="+result);
            System.out.println(jsonResult);
            System.out.println("================================================="+jsonResult.getString("result"));
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return jsonResult;
    }

    public String tongdunRule(Map inMap) throws Exception {  //同盾规则查询
        Map<String ,Object> mapRule = new HashMap<String, Object>();
        mapRule.put("phone",inMap.get("tel").toString());
        mapRule.put("name",inMap.get("realname").toString());
        mapRule.put("idNo",inMap.get("card").toString());
        mapRule.put("companyName",inMap.get("companyName").toString());
        mapRule.put("companyId",inMap.get("companyId").toString());
        mapRule.put("busType","2");
        mapRule.put("pid",UUID.randomUUID().toString());
        String host = iSystemDictService.getInfo("rule.url");
        String url = host + "/szt/tongdun/rule";
        JSONObject jsonResult;
        String result = "";
        String data="";
        try {
            result = HttpUtil.doPost(url, mapRule);
            jsonResult = JSON.parseObject(result);
            data=jsonResult.getString("data");
            System.out.println("=================================================" + result);
            System.out.println(jsonResult);
            System.out.println("=================================================" + data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return data;
    }

    public String  zenxintongRule(Map inMap) throws Exception {  //增信通规则查询
        Map<String ,Object> mapRule = new HashMap<String, Object>();
        mapRule.put("phone",inMap.get("tel").toString());
        mapRule.put("name",inMap.get("realname").toString());
        mapRule.put("idNo",inMap.get("card").toString());
        mapRule.put("companyName",inMap.get("companyName").toString());
        mapRule.put("companyId",inMap.get("companyId").toString());
        mapRule.put("busType","2");
        mapRule.put("pid",UUID.randomUUID().toString());
        String host = iSystemDictService.getInfo("rule.url");
        String url = host + "szt/zenxintong/search";
        JSONObject jsonResult;
        String result = "";
        String strResult="";
        try {
            result = HttpUtil.doPost(url, mapRule);
            jsonResult = JSON.parseObject(result);
            strResult=jsonResult.getString("data");
            System.out.println("=================================================" + result);
            System.out.println(jsonResult);
            System.out.println("=================================================" +strResult );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return strResult;
    }

    public JSONObject tencentRule(Map inMap) throws Exception {
        Map<String ,Object> mapRule = new HashMap<String, Object>();
        mapRule.put("phone",inMap.get("tel").toString());
        mapRule.put("name",inMap.get("realname").toString());
        mapRule.put("idNo",inMap.get("card").toString());
        mapRule.put("companyName",inMap.get("companyName").toString());
        mapRule.put("companyId",inMap.get("companyId").toString());
        mapRule.put("busType","2");
        mapRule.put("pid",UUID.randomUUID().toString());
        String host = iSystemDictService.getInfo("rule.url");
        String url = host + "/szt/tencent/antiFraud";
        JSONObject jsonResult;
        String result = "";
        try {
            result = HttpUtil.doPost(url, mapRule);
            jsonResult = JSON.parseObject(result);
            System.out.println("=================================================" + result);
            System.out.println(jsonResult);
            System.out.println("=================================================" + jsonResult.getString("result"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return jsonResult;
    }

    @Override
    public void zx(String response) throws Exception {
        JSONObject jsonResult = JSON.parseObject(response);
        TraceLoggerUtil.info("287开始回调"+jsonResult);
        String result = jsonResult.getString("result");
        String pid = jsonResult.getString("pid");
        String ruleSql = "select order_id as orderId,user_id as userId from rule_result where pid='"+pid+"'";
        Map map = sunbmpDaoSupport.findForMap(ruleSql);
        Map orderMap = appOrderService.getOrderById(map.get("orderId").toString());
        if("D00000".equals(jsonResult.getString("responseCode"))) {
           //根据业务员获取对应的部门，递归获取分公司以及总公司
           String empId=orderMap.get("empId").toString();
           Map saleMap=appOrderService.getSaleInfo(empId);
           String branchId=saleMap.get("branch").toString();
           BranchInfo branchInfo=appOrderService.getGongSiName(appOrderService.getBranchInfo(branchId));
            String orderId= "";
            if ("拒绝".equals(result)) {
                String sql = "UPDATE mag_order set person_rule_state = '0' where id = '" +map.get("orderId")+ "'";
                sunbmpDaoSupport.exeSql(sql);
                String usersql = "update app_user set order_refused_time='"+ DateUtils.getDateString(new Date())+"' where id ='"+orderMap.get("userId")+"'";
                sunbmpDaoSupport.exeSql(usersql);

            } else if ("通过".equals(result)) {
                String sql1 = "UPDATE mag_order set person_rule_state = '1' where id = '" +map.get("orderId")+ "'";
                sunbmpDaoSupport.exeSql(sql1);
            }
           try{
               map.put("companyName",branchInfo.getDeptName());
               map.put("companyId",branchInfo.getId());
               map.put("busType","2");
               String data=tongdunRule(map);
               String strResult=zenxintongRule(map);
               String ruleSql1="update rule_result set tongdun_rule='"+data+"' where engineId ='287L' and order_id='"+orderId+"'" ;
               String ruleSql2="update rule_result set zenxintong_rule='"+strResult+"' where engineId ='287L' and order_id='"+orderId+"'" ;
               List list=new ArrayList();
               list.add(ruleSql1);
               list.add(ruleSql2);
               sunbmpDaoSupport.exeSql(list);
           }catch (Exception e){

           }
           String sql ="update rule_result set state ='"+result+"',rule_json='"+jsonResult+"' where pid='"+pid+"'";
           sunbmpDaoSupport.exeSql(sql);
        }else if("D00006".equals(jsonResult.getString("responseCode"))){
           String sql = "UPDATE mag_order set person_rule_state = '2' where id = '" +map.get("orderId")+ "'";
           sunbmpDaoSupport.exeSql(sql);
            String rulsql ="update rule_result set state ='异常',rule_json='"+jsonResult+"' where pid='"+pid+"'";
            sunbmpDaoSupport.exeSql(rulsql);
       }

    }

    @Override
    public void callback(String response,String idNo) throws Exception {
        JSONObject jsonResult = JSON.parseObject(response);
        TraceLoggerUtil.info("91回调数据============="+jsonResult);
        String sql = "update mag_customer set ninetyOne_rule='"+jsonResult+"'  where card = '"+idNo+"'";
        sunbmpDaoSupport.findForMap(sql);
//        String userId = map.get("user_id").toString();
//        String ruleSql = "select id from rule_result where user_id='"+userId+"'";
//        Map ruleMap = sunbmpDaoSupport.findForMap(ruleSql);
//        if(ruleMap!=null){
//            String updateSql = "update rule_result set ninetyOne_rule='"+jsonResult+"' where id = '"+ruleMap.get("id")+"'";
//            sunbmpDaoSupport.exeSql(updateSql);
//        }else{
//            String insertSql = "insert into rule_result (id,user_id,create_time,rule_name,ninetyOne_rule) values('"+UUID.randomUUID().toString()+"','"+userId+"','"+ DateUtils.getDateString(new Date())+"','91','"+jsonResult+"')";
//            sunbmpDaoSupport.exeSql(insertSql);
//        }
    }

    public static void main(String [] args)throws Exception {
        Map param =new HashMap();
        param.put("name","1111");
        param.put("phone", "15656555717");
        param.put("idNo", "420102197704284055");
//        param.put("bankNo", "6215593********6720");
//        param.put("accountEmail", "test1qq.com");
//        param.put("accountPhone", "4008839258");
//        param.put("qqNumber", "12345123");
//        param.put("contactAddress", "湖北武汉江岸区后湖大道同安家园23-3-1102");
//        param.put("contact1Name", "李成");
//        param.put("contact1Mobile", "15802781577");
//        param.put("type", "AND");
//        param.put("blackBox", "eyJvcyI6ImFuZHJvaWQiLCJ2ZXJzaW9uIjoiMy4xLjEiLCJwYWNrYWdlcyI6ImNvbS5taWFvZnVqaW5mdS5jbGllbnRtZXJjaGFuZGlzZV8xLjAuMCIsInByb2ZpbGVfdGltZSI6MjU1LCJpbnRlcnZhbF90aW1lIjo1MDQxLCJ0b2tlbl9pZCI6IlZ3eittTlZOXC9DSVZQYWd2czZaTDUxZm1EWVBiUCtkNjdqOVdoZFJkWUhwXC9XWSttUzJWcXc5YW9oMWUwNlcwOFNBTDFRRjVuaWVxVGFtYVdnaDJHREE9PSJ9");
//        param.put("pid",UUID.randomUUID().toString());
//        String host = "http://47.96.17.50:8084";
//        String url = host+"/szt/tongdun/rule";

        String host = "http://47.96.17.50:8084";
        String url = "localhost:8084/szt/tongdun/rule";
//        JSONObject jsonResult;
//        String result = "";
//            result = HttpUtil.doPost(url, param);
//            jsonResult = JSON.parseObject(result);
//            System.out.println("=================================================" + result);
    }
}
