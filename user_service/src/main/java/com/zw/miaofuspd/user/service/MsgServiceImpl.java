package com.zw.miaofuspd.user.service;

import com.base.util.DateUtils;
import com.base.util.TraceLoggerUtil;
import com.zhiwang.zwfinance.app.jiguang.util.JiGuangUtils;
import com.zw.miaofuspd.facade.dict.service.IDictService;
import com.zw.miaofuspd.facade.log.entity.OrderLogBean;
import com.zw.miaofuspd.facade.log.service.IAppOrderLogService;
import com.zw.miaofuspd.facade.user.service.IMsgService;
import com.zw.service.base.AbsServiceBase;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <strong>Title :向数据库中插入站内信 <br>
 * </strong> <strong>Description : </strong>@类注释说明写在此处@<br>
 * <strong>Create on : 2017年02月27日<br>
 * </strong>
 * <p>
 * <strong>Copyright (C) Vbill Co.,Ltd.<br>
 * </strong>
 * <p>
 *
 * @author department:技术开发部 <br>
 *         username:张涛 <br>
 *         email: <br>
 * @version <strong>zw有限公司-运营平台</strong><br>
 *          <br>
 *          <strong>修改历史:</strong><br>
 *          修改人 修改日期 修改描述<br>
 *          -------------------------------------------<br>
 *          <br>
 *          <br>
 */
@Service
public class MsgServiceImpl extends AbsServiceBase implements IMsgService {
    @Autowired
    private IAppOrderLogService iAppOrderLogService;
    @Autowired
    private IDictService iDictService;
    /**
     * 向数据库中插入站内信
     * map中必包含（user_id，title，content，registration_id）;
     */
    @Override
    public void insertMsg(Map map) throws Exception {
        String id = UUID.randomUUID().toString();
        String user_id = (String)map.get("user_id");//用户ID
        String title = (String)map.get("title");//站内信标题
        String content = (String)map.get("content");//站内信内容
        String msg_type = map.get("msg_type").toString();
        String update_state = "0";//该条消息是否可点击
        String jpush_state = "0";//jpush推送状态
        if(map.containsKey("update_state")){
            update_state =(String) map.get("update_state");
        }
        if(map.get("registration_id") != null) {
            String registration_id = "";//别名
            if (StringUtils.isNotEmpty(map.get("registration_id").toString())) {
                registration_id = map.get("registration_id").toString();//别名
                if (!JiGuangUtils.alias(title, content, registration_id)) {
                    jpush_state = "1";
                }
            }
        }
        String messageState = "0";//表示站内信未读
        String nowTime = DateUtils.getDateString(new Date()); //获取当前时间
        StringBuffer sql = new StringBuffer("INSERT INTO app_message(id,msg_type,jpush_state,update_state,user_id,title,content,state,order_type,creat_time,alter_time) VALUES('");
        sql.append(id + "','"+ msg_type+"','"+ jpush_state+"','"+update_state + "','"+ user_id + "','" + title + "','" + content + "','" + messageState + "','2','" + nowTime + "','" + nowTime + "')");
        sunbmpDaoSupport.exeSql(sql.toString());
    }

    @Override
    public void insertOrderMsg(String state,String order_id) throws Exception {
        String regsql = "select registration_id,id from app_user where id =(select user_id from mag_order where id = '" + order_id + "')";
        Map map = sunbmpDaoSupport.findForList(regsql).get(0);
        String registration_id = map.get("registration_id")+"";
        String user_id = map.get("id")+"";
        String id = UUID.randomUUID().toString();
        String date = DateUtils.getDateString(new Date());
        String title = "";
        String content = "";
        String push = "";
        String sql;
        String commodityState = "";
        String operatorType="";//操作人类型0：客户自己；1：后台用户 2:办单员
        String operatorId = "";//操作人id 客户id或后台用户id
        String operatorName = "";//操作人
        List list = new ArrayList();
        String orderSql1 = "select user_id,CUSTOMER_ID,CUSTOMER_NAME,emp_id from mag_order where id = '" + order_id + "'";
        Map orderMap1= sunbmpDaoSupport.findForMap(orderSql1);
        String customerId=orderMap1.get("CUSTOMER_ID").toString();
        String empId = orderMap1.get("emp_id").toString();
        String customerName = orderMap1.get("CUSTOMER_NAME").toString();
        String empSql = "select realname from mag_salesman where id='"+empId+"'";
        Map empMap = sunbmpDaoSupport.findForMap(empSql);
        String realname = empMap.get("realname").toString();
        if("0".equals(state)){
            title = "继续申请";
            content = "您还有一笔未完成的申请订单，点击前往按钮继续申请。";//iDictService.getDictInfo("商品消息","sptg");
            push = "1";
            if( JiGuangUtils.alias(title,content,registration_id)){
                push = "0";
            }
            sql = "insert into app_message(id,user_id,title,content,creat_time,alter_time,state,update_state,msg_type,push_state,order_type,order_state,order_id)values('"+id+"','"+user_id+"','"+title+"','"+content+"','"+date+"','"+date+"','0','1','1','"+push+"','2','"+state+"','"+order_id+"')";
            sunbmpDaoSupport.exeSql(sql);
            operatorType="0";
            operatorName=customerName==""?"系统操作":customerName;
            operatorId=user_id==""?"0":user_id;
        }
        if("1".equals(state)){
            Map findMap = findInfo(order_id);
            String merchant = findMap.get("merchant")==null?"":findMap.get("merchant").toString();//商品信息
            title = "商品分期订单";
            content = "您申请的商品分期（"+merchant+"）, 订单已生成，您的办单员正在努力为您处理订单中，请耐心等候。";//iDictService.getDictInfo("商品消息","sptg");
            push = "1";
            if( JiGuangUtils.alias(title,content,registration_id)){
                push = "0";
            }
            String updateSql = "update app_message set update_state='0' where order_id='"+order_id+"' and order_type='2'";
            sunbmpDaoSupport.exeSql(updateSql);
            sql = "insert into app_message(id,user_id,title,content,creat_time,alter_time,state,update_state,msg_type,push_state,order_type,order_state,order_id)values('"+id+"','"+user_id+"','"+title+"','"+content+"','"+date+"','"+date+"','0','1','1','"+push+"','2','"+state+"','"+order_id+"')";
            sunbmpDaoSupport.exeSql(sql);
            operatorType="0";
            operatorId=user_id;
            operatorName=customerName;
        }else if("2".equals(state)){
            Map findMap = findInfo(order_id);
            String merchant = findMap.get("merchant")==null?"":findMap.get("merchant").toString();//商品信息
            title = "商品分期申请";
            content = "您申请的商品分期（"+merchant+"）, 订单信息已完善，请仔细核实办单员为您补充的信息内容是否属实，确认无误后提交即可完成申请。"; //iDictService.getDictInfo("商品消息","sptg");
            push = "1";
            if( JiGuangUtils.alias(title,content,registration_id)){
                push = "0";
            }
            sql = "insert into app_message(id,user_id,title,content,creat_time,alter_time,state,update_state,msg_type,push_state,order_type,order_state,order_id)values('"+id+"','"+user_id+"','"+title+"','"+content+"','"+date+"','"+date+"','0','1','1','"+push+"','2','"+state+"','"+order_id+"')";
            sunbmpDaoSupport.exeSql(sql);
            operatorType="2";
            operatorId="empId";
            operatorName=realname;
        }else if("3".equals(state)){
            Map findMap = findInfo(order_id);
            String predict_price = findMap.get("predict_price")==null?"":findMap.get("predict_price").toString();//首付金额
            title = "审批通过";
            content = "恭喜您，您商品分期申请已审核通过！首付款项经过审核批准为"+predict_price+"元。您的办单员会协助您绑定银行卡开户，绑定的银行卡用于现金提现及还款。";//iDictService.getDictInfo("商品消息","sptg");
            push = "1";
            if( JiGuangUtils.alias(title,content,registration_id)){
                push = "0";
            }
            sql = "insert into app_message(id,user_id,title,content,creat_time,alter_time,state,update_state,msg_type,push_state,order_type,order_state,order_id)values('"+id+"','"+user_id+"','"+title+"','"+content+"','"+date+"','"+date+"','0','1','1','"+push+"','2','"+state+"','"+order_id+"')";
            sunbmpDaoSupport.exeSql(sql);
            //查询是否开户,如果已经开户,就之前将小状态改为提货的状态
            String accountSql = "select id from mag_customer_account where CUSTOMER_ID = '"+findMap.get("customerId")+"' and channel='1'";
            List accountList = sunbmpDaoSupport.findForList(accountSql);
            if(accountList!=null && accountList.size()>0){
                    commodityState = "16.5";//待收取前置服务包
            }else{
                commodityState = "16";
            }
          /*
            if(accountList!=null && accountList.size()>0){
                String serviceSql = "select id from mag_servicepag_order where order_id = '"+order_id+"' and type='1'";
                Map serviceMap = sunbmpDaoSupport.findForMap(serviceSql);
                if(serviceMap==null){
                    commodityState = "17";
                }else{
                    commodityState = "16.5";//待收取前置服务包
                }
            }else{
                commodityState = "16";
            }*/
            String orderSql = "update mag_order set commodity_state = '"+commodityState+"' where id = '"+order_id+"'";
            sunbmpDaoSupport.exeSql(orderSql);
            operatorType="1";
            operatorName="";
            operatorId="";
        }else if("4".equals(state)){
            Map findMap = findInfo(order_id);
            String name = findMap.get("name")==null?"":findMap.get("name").toString();//担保人姓名
            title = "订单被担保";
            content = "恭喜您，您的商品分期申请因担保人"+name+"为您进行担保。订单已特批通过。您的办单员会协助您绑定银行卡开户，绑定的银行卡用于现金提现及还款。";//iDictService.getDictInfo("商品消息","sptg");
            push = "1";
            if( JiGuangUtils.alias(title,content,registration_id)){
                push = "0";
            }
            sql = "insert into app_message(id,user_id,title,content,creat_time,alter_time,state,update_state,msg_type,push_state,order_type,order_state,order_id)values('"+id+"','"+user_id+"','"+title+"','"+content+"','"+date+"','"+date+"','0','1','1','"+push+"','2','"+state+"','"+order_id+"')";
            sunbmpDaoSupport.exeSql(sql);
            operatorType="1";
            operatorName="";
            operatorId="";
        }else if("5".equals(state)){
            Map findMap = findSerPcgInfo(order_id);
            if((boolean)findMap.get("flag")){
                String amount = findMap.get("amount")==null?"":findMap.get("amount").toString();//服务包费用
                String package_name = findMap.get("packageName")==null?"":findMap.get("packageName").toString();//服务包名称
                content = "您的银行卡已经绑定成功！您申请的商品分期，需缴纳"+package_name+amount+"元 。";
                sql = "insert into app_message(id,user_id,title,content,creat_time,alter_time,state,update_state,msg_type,push_state,order_type,order_state,order_id)values('"+id+"','"+user_id+"','"+title+"','"+content+"','"+date+"','"+date+"','0','1','1','"+push+"','2','"+state+"','"+order_id+"')";
                sunbmpDaoSupport.exeSql(sql);
            }else{
                sql = "insert into app_message(id,user_id,title,content,creat_time,alter_time,state,update_state,msg_type,push_state,order_type,order_state,order_id)values('"+id+"','"+user_id+"','"+title+"','"+content+"','"+date+"','"+date+"','0','0','1','"+push+"','2','"+state+"','"+order_id+"')";
                sunbmpDaoSupport.exeSql(sql);
                String msg = findMap.get("msg")==null?"":findMap.get("msg").toString();//服务包费用
                content = "您的银行卡已经绑定成功！您申请的商品分期，"+msg+" 。";
            }
            title = "绑卡成功";
            push = "1";
            if( JiGuangUtils.alias(title,content,registration_id)){
                push = "0";
            }
            operatorType="2";
            operatorName=realname;
            operatorId=empId;
        }else if("6".equals(state)){
            content = "您的银行卡已经绑定成功！绑定的银行卡将作为默认银行卡每月自动划扣应还款金额。请做好个人财务规划，避免产生逾期费用。";
            title = "绑卡成功";
            push = "1";
            if( JiGuangUtils.alias(title,content,registration_id)){
                push = "0";
            }
            sql = "insert into app_message(id,user_id,title,content,creat_time,alter_time,state,update_state,msg_type,push_state,order_type,order_state,order_id)values('"+id+"','"+user_id+"','"+title+"','"+content+"','"+date+"','"+date+"','0','1','1','"+push+"','2','"+state+"','"+order_id+"')";
            sunbmpDaoSupport.exeSql(sql);
            operatorType="2";
            operatorName=realname;
            operatorId=empId;
        }else if("7".equals(state)){
            Map findMap = findInfo(order_id);
            String merchant_name = findMap.get("merchant_name")==null?"":findMap.get("merchant_name").toString();//商户名称
            String merchant = findMap.get("merchant")==null?"":findMap.get("merchant").toString();//商品
            String pay_time = findMap.get("pay_time")==null?"":findMap.get("pay_time").toString();//还款时间
            String repayment_amount = findMap.get("repayment_amount")==null?"":findMap.get("repayment_amount").toString();//还款金额
            pay_time = pay_time.substring(0,4)+"年"+pay_time.substring(4,6)+"月"+pay_time.substring(6,8)+"日";
            title = "已提货";
            content = "您已在"+merchant_name+"门店提走"+merchant+"，完成本次商品分期。首次还款日在"+pay_time+"， 需还款"+repayment_amount+"元（若您订购月付服务包，服务包相关费用需随期缴纳）";
            push = "1";
            if( JiGuangUtils.alias(title,content,registration_id)){
                push = "0";
            }
            sql = "insert into app_message(id,user_id,title,content,creat_time,alter_time,state,update_state,msg_type,push_state,order_type,order_state,order_id)values('"+id+"','"+user_id+"','"+title+"','"+content+"','"+date+"','"+date+"','0','1','1','"+push+"','2','"+state+"','"+order_id+"')";
            sunbmpDaoSupport.exeSql(sql);
            operatorType="2";
            operatorName=realname;
            operatorId=empId;
        }else if("8".equals(state)){
            title = "审批未通过";
            content = "很抱歉的通知您，您的分期申请材料不符合我司借款要求。感谢您使用秒付金服款。全国服务热线：*****。";//iDictService.getDictInfo("消息内容","spjj");
            push = "1";
            if( JiGuangUtils.alias(title,content,registration_id)){
                push = "0";
            }
            sql = "update app_message set update_state = '0' where order_id = '"+order_id+"'";
            list.add(sql);
            sql = "update app_user set order_refused_time = '"+date+"' where id = '"+user_id+"'";
            list.add(sql);
            sql = "insert into app_message(id,user_id,title,content,creat_time,alter_time,state,update_state,msg_type,push_state,order_type,order_state,order_id)values('"+id+"','"+user_id+"','"+title+"','"+content+"','"+date+"','"+date+"','0','1','1','"+push+"','2','"+state+"','"+order_id+"')";
            list.add(sql);
            sunbmpDaoSupport.exeSql(list);
            operatorName="系统操作";
            operatorId="0";
        }else if("9".equals(state)){
            title = "审批未通过";
            content = "很抱歉的通知您，您的分期申请材料不符合我司借款要求。感谢您使用秒付金服款。全国服务热线：*****。";//iDictService.getDictInfo("消息内容","spjj");
            push = "1";
            if( JiGuangUtils.alias(title,content,registration_id)){
                push = "0";
            }
            sql = "update app_message set update_state = '0' where order_id = '"+order_id+"'";
            list.add(sql);
            sql = "update app_user set order_refused_time = '"+date+"' where id = '"+user_id+"'";
            list.add(sql);
            sql = "insert into app_message(id,user_id,title,content,creat_time,alter_time,state,update_state,msg_type,push_state,order_type,order_state,order_id)values('"+id+"','"+user_id+"','"+title+"','"+content+"','"+date+"','"+date+"','0','1','1','"+push+"','2','"+state+"','"+order_id+"')";
            list.add(sql);
            sunbmpDaoSupport.exeSql(list);
        }else if("10".equals(state)){
            Map findMap = findInfo(order_id);
            String merchant = findMap.get("merchant")==null?"":findMap.get("merchant").toString();//商品信息
            title = "合同确认";
            content = "您申请的商品分期（"+merchant+"）, 合同已确认。";
            push = "1";
            if( JiGuangUtils.alias(title,content,registration_id)){
                push = "0";
            }
            //查询服务包
            String serviceSql = "select id from mag_servicepag_order where order_id = '"+order_id+"' and type='1'";
            Map serviceMap = sunbmpDaoSupport.findForMap(serviceSql);
            if(serviceMap==null){
                commodityState = "17";
            }else{
                commodityState = "16.7";//合同已确认
            }
            String updateSql = "update app_message set update_state='0' where order_id='"+order_id+"' and order_type='2'";
            sunbmpDaoSupport.exeSql(updateSql);
            sql = "insert into app_message(id,user_id,title,content,creat_time,alter_time,state,update_state,msg_type,push_state,order_type,order_state,order_id)values('"+id+"','"+user_id+"','"+title+"','"+content+"','"+date+"','"+date+"','0','1','1','"+push+"','2','"+state+"','"+order_id+"')";
            sunbmpDaoSupport.exeSql(sql);
            operatorType="0";
            operatorId=user_id;
            operatorName=customerName;
        }
        if(!"3".equals(state) && !"4".equals(state) && !"9".equals(state)){
            OrderLogBean orderLog = new OrderLogBean();
            orderLog.setMessageId(id);
            //记录订单日志
            //记录订单日志
            String orderInfoSql="select merchant_name,merchandise_brand,merchandise_model,merchandise_version,order_no, USER_ID,PERIODS,product_type,CUSTOMER_ID,CUSTOMER_NAME,CARD,tel,product_type_name,product_name_name,AMOUNT,state,commodity_state from mag_order where id='"+order_id+"'";
            Map  orderMap=sunbmpDaoSupport.findForMap(orderInfoSql);
            orderLog.setUserId(orderMap.get("USER_ID")==null?"":orderMap.get("USER_ID").toString());
            orderLog.setCard(orderMap.get("CARD")==null?"":orderMap.get("CARD").toString());
            orderLog.setCustomerId(orderMap.get("CUSTOMER_ID")==null?"":orderMap.get("CUSTOMER_ID").toString());
            orderLog.setPeriods(orderMap.get("PERIODS")==null?"":orderMap.get("PERIODS").toString());
            orderLog.setCustomerName(orderMap.get("CUSTOMER_NAME")==null?"":orderMap.get("CUSTOMER_NAME").toString());
            orderLog.setAmount(orderMap.get("AMOUNT")==null?"":orderMap.get("AMOUNT").toString());
            orderLog.setTel(orderMap.get("tel")==null?"":orderMap.get("tel").toString());
            orderLog.setProductTypeName(orderMap.get("product_type_name")==null?"":orderMap.get("product_type_name").toString());
            orderLog.setProductNameName(orderMap.get("product_name_name")==null?"":orderMap.get("product_name_name").toString());
            orderLog.setOrderNo(orderMap.get("order_no")==null?"":orderMap.get("order_no").toString());
            orderLog.setProductType(orderMap.get("product_type")==null?"":orderMap.get("product_type").toString());
            orderLog.setOperatorName(map.get("CUSTOMER_NAME")==null?"":map.get("CUSTOMER_NAME").toString());
            orderLog.setCommodityState(map.get("commodity_state")==null?"":map.get("commodity_state").toString());
            orderLog.setMerchantName(map.get("merchant_name")==null?"":map.get("merchant_name").toString());
            orderLog.setMerchandiseBrand(map.get("merchandise_brand")==null?"":map.get("merchandise_brand").toString());
            orderLog.setMerchandiseModel(map.get("merchandise_model")==null?"":map.get("merchandise_model").toString());
            orderLog.setMerchandiseVersion(map.get("merchandise_version")==null?"":map.get("merchandise_version").toString());
            orderLog.setState(orderMap.get("state")==null?"":orderMap.get("state").toString());
            orderLog.setTache(title);
            orderLog.setOrderId(order_id);
            orderLog.setChangeValue(title);
            orderLog.setOperatorId(operatorId);
            orderLog.setOperatorType(operatorType);
            orderLog.setCreatTimeLog(date);
            orderLog.setCreatTime(date);
            orderLog.setAlterTime(date);
            orderLog.setOperatorName(operatorName);
            if (iAppOrderLogService.setOrderLog(orderLog) == null) {
                TraceLoggerUtil.error( "记录订单日志失败!");
            }
        }
    }

    //查询消息内容需要的数据
    public Map findInfo(String order_id){
        Map retMap = new HashMap();
        String findSql = "select mr.pay_time,mr.repayment_amount,mspo.type,mspo.amount_collection,mspo.package_name,ms.name,mo.predict_price," +
                "mo.merchant_name,mo.merchandise_brand,mo.merchandise_model,mo.merchandise_version,mo.customer_id as customerId from mag_order mo LEFT JOIN " +
                "mag_surety_order mso on mo.id=mso.order_id LEFT JOIN mag_surety ms on ms.id = mso.surety_id LEFT JOIN " +
                "mag_servicepag_order mspo on mo.id=mspo.order_id LEFT JOIN mag_repayment mr on mo.id = mr.order_id where mo.id = '"+order_id+"' ORDER BY mr.pay_time limit 0,1";
        Map findMap = sunbmpDaoSupport.findForMap(findSql);
        String merchandise_brand = findMap.get("merchandise_brand")==null?"":findMap.get("merchandise_brand").toString();//商品品牌
        String merchandise_model = findMap.get("merchandise_model")==null?"":findMap.get("merchandise_model").toString();//商品型号
        String merchandise_version = findMap.get("merchandise_version")==null?"":findMap.get("merchandise_version").toString();//商品版本
        String merchant = merchandise_brand+merchandise_model+merchandise_version;
        findMap.put("merchant",merchant);//商品信息
        return findMap;
    }

    //获取服务包信息
    public Map findSerPcgInfo(String orderId){
        Map map = new HashMap();
        String serPackSql = "  SELECT a.id as packageId,package_name as packageName, b.amount_collection as amount FROM mag_servicepag_order a" +
                " LEFT JOIN mag_service_package b on b.id = a.package_service_id WHERE a.type = '1' AND a.state != '1' AND a.order_id = '"+orderId+"';";
        Map serPackMap = sunbmpDaoSupport.findForMap(serPackSql);
        if (serPackMap != null && serPackMap.size() > 0){
            map.put("flag",true);
            map.put("packageName",serPackMap.get("packageName").toString());
            map.put("amount",serPackMap.get("amount").toString());
        }else{
            map.put("flag",false);
            map.put("msg","该订单无前置提前还款包需要支付");
        }
        return map;
    }
}
