package com.zw.miaofuspd.jxlStatusTask.task;

import com.alibaba.fastjson.JSONObject;
import com.base.util.DateUtils;
import com.base.util.TraceLoggerUtil;
import com.zw.miaofuspd.facade.dict.service.ISystemDictService;
import com.zw.miaofuspd.facade.entity.BranchInfo;
import com.zw.miaofuspd.facade.entity.CreditRequest;
import com.zw.miaofuspd.facade.entity.CreditRequestParam;
import com.zw.miaofuspd.facade.entity.CreditResponse;
import com.zw.miaofuspd.facade.order.service.AppOrderService;
import com.zw.miaofuspd.util.CommonUtil;
import com.zw.miaofuspd.util.NonceUtil;
import com.zw.service.task.abs.AbsTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * 定时查询手机运营商报告是否生成，如果生成则调用自动化审核风控规则
 */
public class JxlStatusTask extends AbsTask {
    @Resource
    private ISystemDictService iSystemDictService;
    @Autowired
    private AppOrderService appOrderService;

    @Override
    public void doWork() throws Exception {
        TraceLoggerUtil.info("================288跑批开始==================");
        String sql ="select id,user_id,customer_name,person_rule_state,company,customer_id,tel,card,overdue_state,emp_id from mag_order where state = '1' and order_type='2' and ALTER_TIME<=(NOW()-INTERVAL 5 MINUTE) order by ALTER_TIME desc";
        try {
            List list = this.sunbmpDaoSupport.findForList(sql);
            Map map = new HashMap();
            String date=DateUtils.formatDate("yyyyMMddHHmmss");
            if (list != null && list.size() > 0) {
                for (int i = 0, length = list.size(); i < length; i++) {
                    map = (Map) list.get(i);
                    String order_id = map.get("id").toString();
                    if(StringUtils.isEmpty(map.get("person_rule_state"))){
                      continue;
                    }
                    if("0".equals(map.get("person_rule_state").toString())){
                        String ordersql="update mag_order set state ='3',ALTER_TIME='"+date+"' where id ='" + order_id + "'";
                        sunbmpDaoSupport.exeSql(ordersql);
                        continue;
                    }else if("2".equals(map.get("person_rule_state").toString())){
                        String ordersql="update mag_order set state ='4',ALTER_TIME='"+date+"' where id ='" + order_id + "'";
                        sunbmpDaoSupport.exeSql(ordersql);
                        continue;
                    }
                    String overdue_state=map.get("overdue_state").toString();

                 /*   String empId = map.get("emp_id").toString();
                    Map saleMap=appOrderService.getSaleInfo(empId);
                    String branchId=saleMap.get("branch").toString();
                    BranchInfo branchInfo=appOrderService.getGongSiName(appOrderService.getBranchInfo(branchId));*/
                    String company=map.get("company").toString();
                    Map saleMap=appOrderService.getCompanyInfo(company);
                    String user_id=map.get("user_id").toString();
                    if("2".equals(overdue_state) ||"3".equals(overdue_state)) {
                        //  String taskId = custMap.get("phone_task_id")==null?"":custMap.get("phone_task_id").toString();
                        String linkmanSql = "select link_name,relationship,contact from mag_customer_linkman where customer_id='" + map.get("customer_id").toString() + "' and type='1'";
                        List list1 = sunbmpDaoSupport.findForList(linkmanSql);
                        Map linkmanMap1 = new HashMap();
                        Map linkmanMap2 = new HashMap();
                        String link_name1 = "";
                        String relationship1 = "";
                        String contact1 = "";
                        String link_name2 = "";
                        String relationship2 = "";
                        String contact2 = "";
                        if (list1 != null && list.size() > 0) {
                            linkmanMap1 = (Map) list1.get(0);
                            link_name1 = linkmanMap1.get("link_name").toString();
                            relationship1 = linkmanMap1.get("relationship").toString();
                            contact1 = linkmanMap1.get("contact").toString();
                            linkmanMap2 = (Map) list1.get(1);
                            link_name2 = linkmanMap2.get("link_name").toString();
                            relationship2 = linkmanMap2.get("relationship").toString();
                            contact2 = linkmanMap2.get("contact").toString();
                        }
                        CreditResponse response = new CreditResponse();
                        CreditRequest request = new CreditRequest();
                        //动作
                        request.setAct("query");
                        //时间戳
                        request.setTs(System.currentTimeMillis());
                        //API调用方生成的随机串：8-10位随机字符串(a-z,0-9)仅包含小写字母和数字
                        request.setNonce(NonceUtil.nonce(NonceUtil.LOWERCHARANDNUMBER, 8));
                        //流水号：唯一标识，每次调用不一样
                        String pid=UUID.randomUUID()+"";
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
                        request.setSync(true);
                        request.setEngineId(288L);
                        String engineId = "288L";
                        String rule_name="聚信立";
                        //用户编号
                        request.setUid(user_id);
                        //签名：sign签名算法:根据act,ts,nonce,pid,uid,token,按顺序用‘,’连接在一起，做md5签名。
                        //即sign = md5(act,ts,nonce,pid,uid,token)
                        request.setSign(CommonUtil.generateSign(request, "123456"));

                        CreditRequestParam param = new CreditRequestParam();
                        Map<String, Object> data = new HashMap<String, Object>();
                        data.put("taskId", "");
                        data.put("contact1_name", link_name1);
                        data.put("contact1_relation", relationship1);
                        data.put("contact1_mobile", contact1);
                        data.put("contact2_name", link_name2);
                        data.put("contact2_relation", relationship2);
                        data.put("contact2_mobile", contact2);
                        data.put("companyName",company);
                        data.put("companyId",saleMap.get("id").toString());
                        data.put("busType",2);
                        param.setData(data);
                        //=======================
                        String idNo = map.get("card").toString();//身份证号码
                        String phone = map.get("tel").toString();//手机号码
                        String name = map.get("customer_name").toString();//姓名
                        param.setMobile(phone);
                        param.setName(name);
                        param.setIdNum(idNo);
                        //==================================
                        request.setProductSeriesName("test");
                        request.setCreditRequestParam(param);
                        JSONObject json = (JSONObject) JSONObject.toJSON(request);
                        String result;
                        String host = iSystemDictService.getInfo("rule.url");
                        String url = host + "/szt/credit/application";
                        String updateSql = "";
                        JSONObject jsonResult;
                        String id1 = UUID.randomUUID().toString();
                        updateSql = "update mag_order set state ='" + 2 + "',order_submission_time='"+date+"' where id ='" + order_id + "'";
                        sunbmpDaoSupport.exeSql(updateSql);
                        String sql1 ="insert into rule_result (id,order_id,user_id,pid,engineId,state,create_time,rule_json,rule_name) values('"+id1+"','"+order_id+"','"+user_id+"','"+pid+"','"+engineId+"','通过','"+DateUtils.getDateString(new Date())+"','通过','聚信立')";
                        sunbmpDaoSupport.exeSql(sql1);
//                        try {
//                            result = HttpUtil.doPost(url, json.toString());
//                            jsonResult = JSON.parseObject(result);
//                            TraceLoggerUtil.info("==========" + result);
//                            String id1 = UUID.randomUUID().toString();
//                            List listSql = new ArrayList();
//                            updateSql = "update mag_order set state ='" + 2 + "' where id ='" + order_id + "'";
//                            sunbmpDaoSupport.exeSql(updateSql);
//                            String sql1 ="insert into rule_result (id,order_id,user_id,pid,engineId,state,create_time,rule_json,rule_name) values('"+id1+"','"+order_id+"','"+user_id+"','"+pid+"','"+engineId+"','通过','"+DateUtils.getDateString(new Date())+"','通过','聚信立')";
//                            sunbmpDaoSupport.exeSql(sql1);
////                            if ("拒绝".equals(jsonResult.getString("result"))) { //风控拒绝
////                                String id = UUID.randomUUID().toString();
////                                String title = "风控拒绝";
////                                String content = iDictService.getDictInfo("消息内容", "spjj");
////                                String push = "1";
////                                updateSql = "update mag_order set state ='" + 3 + "' where id ='" + order_id + "'";
////                                listSql.add(updateSql);
////                                String messageSql = "update app_message set update_state = '0' where order_id = '" + order_id + "'";
////                                listSql.add(messageSql);
////                                String userSql = "update app_user set order_refused_time = '" + date + "' where id = '" + user_id + "'";
////                                listSql.add(userSql);
////                                String messagesql = "insert into app_message(id,user_id,title,content,creat_time,alter_time,state,update_state,msg_type,push_state,order_state,order_id,order_type)values('" + id + "','" + user_id + "','" + title + "','" + content + "','" + date + "','" + date + "','0','0','1','" + push + "','3','" + order_id + "','1')";
////                                listSql.add(messagesql);
////                                sunbmpDaoSupport.exeSql(listSql);
////                                OrderLogBean orderLog = new OrderLogBean();
////                                orderLog.setMessageId(id);
////                                //记录订单日志
////                                //记录订单日志
////                                String orderInfoSql="select order_no, USER_ID,PERIODS,product_type,CUSTOMER_ID,CUSTOMER_NAME,CARD,tel,product_type_name,product_name_name,AMOUNT from mag_order where id='"+order_id+"'";
////                                Map orderMap=sunbmpDaoSupport.findForMap(orderInfoSql);
////                                orderLog.setUserId(orderMap.get("USER_ID").toString());
////                                orderLog.setCard(orderMap.get("CARD").toString());
////                                orderLog.setCustomerId(orderMap.get("CUSTOMER_ID").toString());
////                                orderLog.setPeriods(orderMap.get("PERIODS").toString());
////                                orderLog.setCustomerName(orderMap.get("CUSTOMER_NAME").toString());
////                                orderLog.setAmount(orderMap.get("AMOUNT").toString());
////                                orderLog.setTel(orderMap.get("tel").toString());
////                                orderLog.setProductTypeName(orderMap.get("product_type_name").toString());
////                                orderLog.setProductNameName(orderMap.get("product_name_name").toString());
////                                orderLog.setOrderNo(orderMap.get("order_no").toString());
////                                orderLog.setProductType(orderMap.get("product_type").toString());
////                                orderLog.setState("3");
////                                orderLog.setTache(title);
////                                orderLog.setOrderId(order_id);
////                                orderLog.setChangeValue(title);
////                                orderLog.setOperatorId(user_id);
////                                orderLog.setOperatorType("0");
////                                orderLog.setCreatTimeLog(date);
////                                orderLog.setCreatTime(date);
////                                orderLog.setAlterTime(date);
////                                orderLog.setOperatorName(map.get("customer_name").toString());
////                                if (iAppOrderLogService.setOrderLog(orderLog) == null) {
////                                    TraceLoggerUtil.error("记录订单日志失败!");
////                                }
////                                String sql1 ="insert into rule_result (id,order_id,user_id,pid,engineId,state,create_time,rule_json,rule_name) values('"+id1+"','"+order_id+"','"+user_id+"','"+pid+"','"+engineId+"','"+jsonResult.getString("result")+"','"+DateUtils.getDateString(new Date())+"','"+jsonResult+"','"+rule_name+"')";
////                                sunbmpDaoSupport.exeSql(sql1);
////                            } else if ("通过".equals(jsonResult.getString("result"))) { //风控通过
////                                updateSql = "update mag_order set state ='" + 2 + "' where id ='" + order_id + "'";
////                                sunbmpDaoSupport.exeSql(updateSql);
////                                String sql1 ="insert into rule_result (id,order_id,user_id,pid,engineId,state,create_time,rule_json,rule_name) values('"+id1+"','"+order_id+"','"+user_id+"','"+pid+"','"+engineId+"','"+jsonResult.getString("result")+"','"+DateUtils.getDateString(new Date())+"','"+jsonResult+"','"+rule_name+"')";
////                                sunbmpDaoSupport.exeSql(sql1);
////                            } else {//风控异常
////                                updateSql = "update mag_order set state ='" + 4 + "' where id ='" + order_id + "'";
////                                sunbmpDaoSupport.exeSql(updateSql);
////                                String id = UUID.randomUUID().toString();
////                                String content="285L风控异常";
////                                String orderSql ="insert into rule_result (id,order_id,user_id,pid,engineId,state,create_time,rule_json,rule_name) values('"+id+"','"+order_id+"','"+user_id+"','"+pid+"','"+engineId+"','异常','"+DateUtils.getDateString(new Date())+"','"+content+"','"+rule_name+"')";
////                                sunbmpDaoSupport.exeSql(orderSql);
////                            }
//                        } catch (Exception e) {//风控异常
//                            updateSql = "update mag_order set state ='" + 2 + "' where id ='" + order_id + "'";
//                            sunbmpDaoSupport.exeSql(updateSql);
//                            String sql1 ="insert into rule_result (id,order_id,user_id,pid,engineId,state,create_time,rule_json,rule_name) values('"+UUID.randomUUID().toString()+"','"+order_id+"','"+user_id+"','"+pid+"','"+engineId+"','通过','"+DateUtils.getDateString(new Date())+"','通过','聚信立')";
//                            sunbmpDaoSupport.exeSql(sql1);
//                            //e.printStackTrace();
//                        }
                    }else{
                        String  updateSql1 = "update mag_order set state ='" + 2 + "',order_submission_time='"+date+"' where id ='" + order_id + "'";
                        sunbmpDaoSupport.exeSql(updateSql1);
                        String sql1 ="insert into rule_result (id,order_id,user_id,pid,engineId,state,create_time,rule_json,rule_name) values('"+UUID.randomUUID().toString()+"','"+order_id+"','"+user_id+"','"+UUID.randomUUID()+"','288L','通过','"+DateUtils.getDateString(new Date())+"','通过','聚信立')";
                        sunbmpDaoSupport.exeSql(sql1);
                    }
                }
            }
        } catch (Exception e) {
            TraceLoggerUtil.error(e.getMessage(), e);
        }
    }
}
