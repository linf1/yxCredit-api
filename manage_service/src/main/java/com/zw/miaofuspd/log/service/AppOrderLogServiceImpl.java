package com.zw.miaofuspd.log.service;

import com.zw.miaofuspd.facade.log.entity.MessageBean;
import com.zw.miaofuspd.facade.log.entity.OrderLogBean;
import com.zw.miaofuspd.facade.log.service.IAppOrderLogService;
import com.zw.service.base.AbsServiceBase;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * <strong>Title : <br>
 * </strong> <strong>Description :订单日志实现类 </strong>@类注释说明写在此处@<br>
 * <strong>Create on : 2017年02月21日<br>
 * </strong>
 * <p>
 * <strong>Copyright (C) Vbill Co.,Ltd.<br>
 * </strong>
 * <p>
 *
 * @author department:技术开发部 <br>
 *         username:张涛<br>
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
public class AppOrderLogServiceImpl extends AbsServiceBase implements IAppOrderLogService {
    /**
     * 根据订单ID获取订单列表
     * @param order_id
     * @return
     * @throws Exception
     */
    @Override
    public OrderLogBean getOrderById(String order_id) throws Exception {
        OrderLogBean orderLog = null;
        //查询订单
        StringBuffer sql = new StringBuffer("select ID,order_no,user_id,periods,merchant_id,merchant_name,merchandise_id,merchandise_type,merchandise_name,");
        sql.append("customer_id,customer_name,sex_name,tel,card_type,card,credit,precredit,amount,company,branch,emp_id,manager,");
        sql.append("creat_time,alter_time,state,repay_type,rate,merchandise_brand,merchandise_model,merchandise_code,");
        sql.append("provinces_id,city_id,distric_id,provinces,city,distric,product_type,product_type_name,product_name,product_name_name,");
        sql.append("product_detail,product_detail_name,product_detail_code,contract_amount,loan_amount,sex,");
        sql.append("merchandise_brand_id,merchandise_type_id,emp_number from mag_order where id='" + order_id + "'");
        List list = sunbmpDaoSupport.findForList(sql+"");
        if (list.size() > 0) {
            orderLog = new OrderLogBean();
            Map map = (Map) list.get(0);
            //String id=UUID.randomUUID()+"";
            //orderLog.setId(id);
            String orderId = map.get("ID")+"";
            orderLog.setOrderId(orderId);
            String order_no = map.get("order_no")+"";
            orderLog.setOrderNo(order_no);
            String user_id = map.get("user_id")+"";
            orderLog.setUserId(user_id);
            String PERIODS = map.get("periods")+"";
            orderLog.setPeriods(PERIODS);
            String MERCHANT_ID = map.get("merchant_id")+"";
            orderLog.setMerchantId(MERCHANT_ID);
            String MERCHANT_NAME = map.get("merchant_name")+"";
            orderLog.setMerchantName(MERCHANT_NAME);
            String merchandise_id = map.get("merchandise_id")+"";
            orderLog.setMerchandiseId(merchandise_id);
            String merchandise_type = map.get("merchandise_type")+"";
            orderLog.setMerchandiseType(merchandise_type);
            String merchandise_name = map.get("merchandise_name")+"";
            orderLog.setMerchandiseName(merchandise_name);
            String CUSTOMER_ID = map.get("customer_id")+"";
            orderLog.setCustomerId(CUSTOMER_ID);
            String CUSTOMER_NAME = map.get("customer_name")+"";
            orderLog.setCustomerName(CUSTOMER_NAME);
            String sex_name = map.get("sex_name")+"";
            orderLog.setSexName(sex_name);
            String TEL = map.get("tel")+"";
            orderLog.setTel(TEL);
            String CARD_TYPE = map.get("card_type")+"";
            orderLog.setCardType(CARD_TYPE);
            String CARD = map.get("card")+"";
            orderLog.setCard(CARD);
            String CREDIT = map.get("credit")+"";
            orderLog.setCredit(CREDIT);
            String PRECREDIT = map.get("precredit")+"";
            orderLog.setPrecredit(PRECREDIT);
            String AMOUNT = map.get("amount")+"";
            orderLog.setAmount(AMOUNT);
            String COMPANY = map.get("company")+"";
            orderLog.setCompany(COMPANY);
            String BRANCH = map.get("branch")+"";
            orderLog.setBranch(BRANCH);
            String emp_id = map.get("emp_id")+"";
            orderLog.setEmpId(emp_id);
            String MANAGER = map.get("manager")+"";
            orderLog.setManager(MANAGER);
            String CREAT_TIME = map.get("creat_time")+"";
            orderLog.setCreatTime(CREAT_TIME);
            String ALTER_TIME = map.get("alter_time")+"";
            orderLog.setAlterTime(ALTER_TIME);
            String state = map.get("state")+"";
            orderLog.setState(state);
            String repay_type = map.get("repay_type")+"";
            orderLog.setRepayType(repay_type);
            String rate = map.get("rate")+"";
            orderLog.setRate(rate);
            String merchandise_brand = map.get("merchandise_brand")+"";
            orderLog.setMerchandiseBrand(merchandise_brand);
            String merchandise_model = map.get("merchandise_model")+"";
            orderLog.setMerchandiseModel(merchandise_model);
            String merchandise_code = map.get("merchandise_code")+"";
            orderLog.setMerchandiseCode(merchandise_code);
            String provinces_id = map.get("provinces_id")+"";
            orderLog.setProvincesId(provinces_id);
            String city_id = map.get("city_id")+"";
            orderLog.setCityId(city_id);
            String distric_id = map.get("distric_id")+"";
            orderLog.setDistricId(distric_id);
            String provinces = map.get("provinces")+"";
            orderLog.setProvinces(provinces);
            String city = map.get("city")+"";
            orderLog.setCity(city);
            String distric = map.get("distric")+"";
            orderLog.setDistric(distric);
            String product_type = map.get("product_type")+"";
            orderLog.setProductType(product_type);
            String product_type_name = map.get("product_type_name")+"";
            orderLog.setProductTypeName(product_type_name);
            String product_name = map.get("product_name")+"";
            orderLog.setProductName(product_name);
            String product_name_name = map.get("product_name_name")+"";
            orderLog.setProductNameName(product_name_name);
            String product_detail = map.get("product_detail")+"";
            orderLog.setProductDetail(product_detail);
            String product_detail_name = map.get("product_detail_name")+"";
            orderLog.setProductDetailName(product_detail_name);
            String product_detail_code = map.get("product_detail_code")+"";
            orderLog.setProductDetailCode(product_detail_code);
            String contract_amount = map.get("contract_amount")+"";
            orderLog.setContractAmount(contract_amount);
            String loan_amount = map.get("loan_amount")+"";
            orderLog.setLoanAmount(loan_amount);
            String sex = map.get("sex")+"";
            orderLog.setSex(sex);
            String merchandise_brand_id = map.get("merchandise_brand_id")+"";
            orderLog.setMerchandiseBrandId(merchandise_brand_id);
            String merchandise_type_id = map.get("merchandise_type_id")+"";
            orderLog.setMerchandiseTypeId(merchandise_type_id);
            String emp_number = map.get("emp_number")+"";
            orderLog.setEmpNumber(emp_number);
        } else {
            return null;
        }
        return  orderLog;
    }

    /**
     * 根据申请ID获取订单列表
     * @param crm_apply_id
     * @return
     * @throws Exception
     */
    @Override
    public OrderLogBean getOrderByCrmId(String crm_apply_id) throws Exception {
        OrderLogBean orderLog = null;
        //查询订单
        StringBuffer sql = new StringBuffer("select ID,order_no,USER_ID,PERIODS,MERCHANT_ID,MERCHANT_NAME,merchandise_id,merchandise_type,merchandise_name,");
        sql.append("CUSTOMER_ID,CUSTOMER_NAME,sex_name,channel,TEL,CARD_TYPE,CARD,CREDIT,PRECREDIT,AMOUNT,COMPANY,BRANCH,emp_id,MANAGER,TACHE,");
        sql.append("CREAT_TIME,ALTER_TIME,state,repay_type,rate,level,cus_source,merchandise_brand,merchandise_model,merchandise_code,");
        sql.append("provinces_id,city_id,distric_id,provinces,city,distric,product_type,product_type_name,product_name,product_name_name,");
        sql.append("product_detail,product_detail_name,product_detail_code,crm_apply_id,contract_amount,loan_amount,card_type_id,sex,channel_name,");
        sql.append("repay_type_id,level_id,cus_source_id,merchandise_brand_id,merchandise_type_id,emp_number from mag_order where crm_apply_id='" + crm_apply_id + "'");
        List list = sunbmpDaoSupport.findForList(sql.toString());
        if (list.size() > 0) {
            orderLog = new OrderLogBean();
            Map map = (Map) list.get(0);
            String id=UUID.randomUUID()+"";
            orderLog.setId(id);
            String orderId = map.get("ID")+"";
            orderLog.setOrderId(orderId);
            String order_no = map.get("order_no")+"";
            orderLog.setOrderNo(order_no);
            String user_id = map.get("USER_ID")+"";
            orderLog.setUserId(user_id);
            String PERIODS = map.get("PERIODS")+"";
            orderLog.setPeriods(PERIODS);
            String MERCHANT_ID = map.get("MERCHANT_ID")+"";
            orderLog.setMerchantId(MERCHANT_ID);
            String MERCHANT_NAME = map.get("MERCHANT_NAME")+"";
            orderLog.setMerchantName(MERCHANT_NAME);
            String merchandise_id = map.get("merchandise_id")+"";
            orderLog.setMerchandiseId(merchandise_id);
            String merchandise_type = map.get("merchandise_type")+"";
            orderLog.setMerchandiseType(merchandise_type);
            String merchandise_name = map.get("merchandise_name")+"";
            orderLog.setMerchandiseName(merchandise_name);
            String CUSTOMER_ID = map.get("CUSTOMER_ID")+"";
            orderLog.setCustomerId(CUSTOMER_ID);
            String CUSTOMER_NAME = map.get("CUSTOMER_NAME")+"";
            orderLog.setCustomerName(CUSTOMER_NAME);
            String sex_name = map.get("sex_name")+"";
            orderLog.setSexName(sex_name);
            String TEL = map.get("TEL")+"";
            orderLog.setTel(TEL);
            String CARD_TYPE = map.get("CARD_TYPE")+"";
            orderLog.setCardType(CARD_TYPE);
            String CARD = map.get("CARD")+"";
            orderLog.setCard(CARD);
            String CREDIT = map.get("CREDIT")+"";
            orderLog.setCredit(CREDIT);
            String PRECREDIT = map.get("PRECREDIT")+"";
            orderLog.setPrecredit(PRECREDIT);
            String AMOUNT = map.get("AMOUNT")+"";
            orderLog.setAmount(AMOUNT);
            String COMPANY = map.get("COMPANY")+"";
            orderLog.setCompany(COMPANY);
            String BRANCH = map.get("BRANCH")+"";
            orderLog.setBranch(BRANCH);
            String emp_id = map.get("emp_id")+"";
            orderLog.setEmpId(emp_id);
            String MANAGER = map.get("MANAGER")+"";
            orderLog.setManager(MANAGER);
            String TACHE = map.get("TACHE")+"";
            orderLog.setTache(TACHE);
            String CREAT_TIME = map.get("CREAT_TIME")+"";
            orderLog.setCreatTime(CREAT_TIME);
            String ALTER_TIME = map.get("ALTER_TIME")+"";
            orderLog.setAlterTime(ALTER_TIME);
            String state = map.get("state")+"";
            orderLog.setState(state);
            String repay_type = map.get("repay_type")+"";
            orderLog.setRepayType(repay_type);
            String rate = map.get("rate")+"";
            orderLog.setRate(rate);
            String merchandise_brand = map.get("merchandise_brand")+"";
            orderLog.setMerchandiseBrand(merchandise_brand);
            String merchandise_model = map.get("merchandise_model")+"";
            orderLog.setMerchandiseModel(merchandise_model);
            String merchandise_code = map.get("merchandise_code")+"";
            orderLog.setMerchandiseCode(merchandise_code);
            String provinces_id = map.get("provinces_id")+"";
            orderLog.setProvincesId(provinces_id);
            String city_id = map.get("city_id")+"";
            orderLog.setCityId(city_id);
            String distric_id = map.get("distric_id")+"";
            orderLog.setDistricId(distric_id);
            String provinces = map.get("provinces")+"";
            orderLog.setProvinces(provinces);
            String city = map.get("city")+"";
            orderLog.setCity(city);
            String distric = map.get("distric")+"";
            orderLog.setDistric(distric);
            String product_type = map.get("product_type")+"";
            orderLog.setProductType(product_type);
            String product_type_name = map.get("product_type_name")+"";
            orderLog.setProductTypeName(product_type_name);
            String product_name = map.get("product_name")+"";
            orderLog.setProductName(product_name);
            String product_name_name = map.get("product_name_name")+"";
            orderLog.setProductNameName(product_name_name);
            String product_detail = map.get("product_detail")+"";
            orderLog.setProductDetail(product_detail);
            String product_detail_name = map.get("product_detail_name")+"";
            orderLog.setProductDetailName(product_detail_name);
            String product_detail_code = map.get("product_detail_code")+"";
            orderLog.setProductDetailCode(product_detail_code);
            String contract_amount = map.get("contract_amount")+"";
            orderLog.setContractAmount(contract_amount);
            String loan_amount = map.get("loan_amount")+"";
            orderLog.setLoanAmount(loan_amount);
            String sex = map.get("sex")+"";
            orderLog.setSex(sex);
            String merchandise_brand_id = map.get("merchandise_brand_id")+"";
            orderLog.setMerchandiseBrandId(merchandise_brand_id);
            String merchandise_type_id = map.get("merchandise_type_id")+"";
            orderLog.setMerchandiseTypeId(merchandise_type_id);
            String emp_number = map.get("emp_number")+"";
            orderLog.setEmpNumber(emp_number);
        } else {
            return null;
        }
        return orderLog;
    }

    /**
     * 插入站内信crm_apply_id
     * @param message
     * @param order_id
     * @return
     * @throws Exception
     */
    @Override
    public String setAppMessage(MessageBean message, String order_id) throws Exception {
        String messageId = null;
        String id = UUID.randomUUID()+"";
        message.setId(id);
        //更新状态
        StringBuffer sql = new StringBuffer("update app_message  set update_state='0' where id in (select message_id from mag_order_logs where order_id='" + order_id + "')");
        sunbmpDaoSupport.exeSql(sql+"");
        sql = new StringBuffer("insert into app_message(id,user_id,title,content,state,creat_time,alter_time,push_state,update_state,order_id,order_type,order_state,msg_type) values('");
        sql.append(message.getId() + "','" + message.getUserId() + "','" + message.getTitle() + "','" + message.getContent() + "','" + message.getState() + "','" + message.getCreateTime() + "','" + message.getUpdateTime() + "','" + message.getPushState() + "','"+message.getUpdateState()+"','"+order_id+"','2','" + message.getOrder_state() + "','" + message.getMsgType() + "')");
        sunbmpDaoSupport.exeSql(sql+"");
        messageId = message.getId();
        return messageId;
    }

    /**
     * 插入订单日志
     * @param orderLog
     * @return
     * @throws Exception
     */
    @Override
    public String setOrderLog(OrderLogBean orderLog) throws Exception {
        String orderId = null;
        String id = UUID.randomUUID()+"";
        orderLog.setId(id);
        StringBuffer sql = new StringBuffer("insert into mag_order_logs(ID,USER_ID,periods,MERCHANT_ID,MERCHANT_NAME,merchandise_id,CUSTOMER_ID,CUSTOMER_NAME,TEL,");
        sql.append("CARD_TYPE,CARD,credit,amount,company,branch,manager,tache,CREAT_TIME,ALTER_TIME,state,order_id,");
        sql.append("precredit,merchandise_type,merchandise_brand,merchandise_model,product_type,product_type_name,product_name,");
        sql.append("product_name_name,product_detail,product_detail_name,");
        sql.append("merchandise_brand_id,sex_name,sex,emp_number,order_no,merchandise_name,emp_id,repay_type,rate,merchandise_code,");
        sql.append("provinces_id,city_id,distric_id,provinces,city,distric,product_detail_code,contract_amount,loan_amount,merchandise_type_id,message_id," +
                "change_value,operator_id,operator_type,operator_name,creat_time_log)values('");
        sql.append(orderLog.getId() + "','" + orderLog.getUserId() + "','" + orderLog.getPeriods() + "','" + orderLog.getMerchantId() + "','" + orderLog.getMerchantName() + "','");
        sql.append(orderLog.getMerchandiseId() + "','" + orderLog.getCustomerId() + "','" + orderLog.getCustomerName() + "','" + orderLog.getTel() + "','");
        sql.append(orderLog.getCardType() + "','" + orderLog.getCard() + "','" + orderLog.getCredit() + "','" + orderLog.getAmount() + "','" + orderLog.getCompany() + "','" + orderLog.getBranch() + "','");
        sql.append(orderLog.getManager() + "','" + orderLog.getTache() + "','" + orderLog.getCreatTime() + "','" + orderLog.getAlterTime() + "','" + orderLog.getState() + "','" + orderLog.getOrderId() + "','");
        sql.append(orderLog.getPrecredit() + "','" + orderLog.getMerchandiseType() + "','" + orderLog.getMerchandiseBrand() + "','" + orderLog.getMerchandiseModel() + "','");
        sql.append(orderLog.getProductType() + "','" + orderLog.getProductTypeName() + "','" + orderLog.getProductName() + "','" + orderLog.getProductNameName() + "','" + orderLog.getProductDetail() + "','" + orderLog.getProductDetailName() + "','");
        sql.append(orderLog.getMerchandiseBrandId() + "','" + orderLog.getSexName() + "','" + orderLog.getSex() + "','" + orderLog.getEmpNumber() + "','" + orderLog.getOrderNo() + "','" + orderLog.getMerchandiseName() + "','");
        sql.append(orderLog.getEmpId() + "','" + orderLog.getRepayType() + "','" + orderLog.getRate() + "','" + orderLog.getMerchandiseCode() + "','" + orderLog.getProvincesId() + "','" + orderLog.getCityId() + "','" + orderLog.getDistricId() + "','");
        sql.append(orderLog.getProvinces() + "','" + orderLog.getCity() + "','" + orderLog.getDistric() + "','" + orderLog.getProductDetailCode() + "','" + orderLog.getContractAmount() + "','" + orderLog.getLoanAmount() + "','" + orderLog.getMerchandiseTypeId()
                + "','" + orderLog.getMessageId() + "','" + orderLog.getChangeValue() + "','" + orderLog.getOperatorId() + "','" + orderLog.getOperatorType() + "','" + orderLog.getOperatorName() + "','" + orderLog.getCreatTimeLog() + "')");
        sunbmpDaoSupport.exeSql(sql+"");
        orderId = orderLog.getId();
        return orderId;
    }
}
