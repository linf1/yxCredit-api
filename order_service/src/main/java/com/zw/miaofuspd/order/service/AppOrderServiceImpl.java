package com.zw.miaofuspd.order.service;

import com.base.util.DateUtils;
import com.base.util.GeneratePrimaryKeyUtils;
import com.zhiwang.zwfinance.app.jiguang.util.api.util.OrderStateEnum;
import com.zhiwang.zwfinance.app.jiguang.util.api.util.OrderTypeEnum;
import com.zw.miaofuspd.facade.order.service.AppOrderService;
import com.zw.service.base.AbsServiceBase;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/******************************************************
 *Copyrights @ 2018，仙海峰  Co., Ltd.
 *         项目名称 碧有信
 *All rights reserved.
 *
 *Filename：
 *	    订单方法
 *Description：
 ********************************************************/
@Service("appOrderServiceImpl")
public class AppOrderServiceImpl extends AbsServiceBase implements AppOrderService {
    /****************************************************碧友信**********************************************/

    /**
     * 根据用户ID和订单状态获取订单信息列表(2.审核中、3.待签约、4.待放款)
     * @author 仙海峰
     * @param userId
     * @return
     */
    @Override
    public Map getOrderListByUserId(String userId) {
        Map returnMap = new HashMap();
        String  states = OrderStateEnum.AUDIT.getCode() +","+OrderStateEnum.PENDING_CONTRACT.getCode()+","+OrderStateEnum.PENDING_LOAN.getCode();
        String sql ="SELECT ID AS orderId , product_name_name AS productName , applay_money AS applayMoney , PERIODS AS periods , CREAT_TIME AS creatTime , Order_state AS orderState " +
                    "FROM mag_order WHERE USER_ID='"+userId+"' AND Order_state IN("+states+")";
        List list =sunbmpDaoSupport.findForList(sql);
        if(userId.isEmpty()){
            returnMap.put("success",false);
            returnMap.put("msg","未登录用户，请先登录");
            return returnMap;
        }else {
            returnMap.put("OrderList",list);
            return returnMap;
        }


    }


    /**
     * 根据订单ID获取订单信息
     * @author 仙海峰
     * @param orderId
     * @return
     */
    @Override
    public Map getOrderInfoByOrderId(String orderId){

        Map returnMap = new HashMap();
        String orderSql ="SELECT  ID AS orderId , order_no AS orderNo,USER_ID AS userId, CUSTOMER_ID AS customerId," +
                    "CUSTOMER_NAME AS customerName,sex_name AS sexName,TEL AS tel,CARD AS card, PERIODS AS periods," +
                    "DATE_FORMAT(STR_TO_DATE( applay_time,'%Y%m%d%H%i%s'),'%Y-%c-%d %H:%i:%s') AS applayTime , " +
                    "DATE_FORMAT(STR_TO_DATE( Examine_time,'%Y%m%d%H%i%s'),'%Y-%c-%d %H:%i:%s') AS examineTime , " +
                    "DATE_FORMAT(STR_TO_DATE( CREAT_TIME,'%Y%m%d%H%i%s'),'%Y-%c-%d %H:%i:%s') AS creatTime , " +
                    "DATE_FORMAT(STR_TO_DATE( ALTER_TIME,'%Y%m%d%H%i%s'),'%Y-%c-%d %H:%i:%s') AS alterTime , " +
                    "DATE_FORMAT(STR_TO_DATE( loan_time,'%Y%m%d%H%i%s'),'%Y-%c-%d %H:%i:%s') AS loanTime , " +
                    "repay_date AS repayDate , " +
                    "rate AS rate ,product_name_name AS productName ,loan_time AS loanTime, applay_money AS applayMoney," +
                    "loan_amount AS loanAmount ,contract_amount AS contractAmount , repay_money AS repayMoney , " +
                    "Order_state AS orderState,pay_back_user AS payBackUser ,pay_back_card AS payBackCard, " +
                    "DATEDIFF(NOW(),DATE(repay_date)) AS days "+
                    "FROM mag_order WHERE ID='"+orderId+"'";
        Map map =sunbmpDaoSupport.findForMap(orderSql);

        String feeSql="SELECT f.yuqi_fee AS yuQiFee " +
                    "FROM mag_order o INNER JOIN mag_product_fee f ON o.product_detail=f.product_id " +
                    "WHERE o.ID='"+orderId+"'";
        Map feeMap =sunbmpDaoSupport.findForMap(feeSql);

        //日利率
        Double rate = Double.parseDouble(map.get("rate").toString());

        //逾期利率
        Double yuQiFee = Double.parseDouble(feeMap.get("yuQiFee").toString());

        //合同金额
        Double contractAmount = Double.parseDouble(map.get("contractAmount").toString());

        //还款金额
        Double repayMoney = Double.parseDouble(map.get("repayMoney").toString());

        //期限
        Double periods = Double.parseDouble(map.get("periods").toString());

        //逾期天数（当前日期-还款日期）
        Double days = Double.parseDouble(map.get("days").toString());

        //利息（利息=日利率＊合同金额＊借款期限（日）/100）
        DecimalFormat df = new DecimalFormat("0.00");
        Double money = (rate*contractAmount*periods)/100;
        Double interest =Double.parseDouble(df.format(money)) ;

        //罚息
        Double defaultInterest=0.00;
        if (days>0){
            //罚息(罚息费用＝合同金额＊逾期费率＊逾期天数/100)
            Double money2 = ((repayMoney-interest)*yuQiFee*days)/100;
            defaultInterest=Double.parseDouble(df.format(money2));
        }

        //已还金额
        Double alreadyRepaid=0.00;

        //重新计算应还金额
        String finalRepaymentAmount =df.format(repayMoney+defaultInterest);

        map.put("repayMoney",finalRepaymentAmount);
        map.put("interest",interest);
        map.put("defaultInterest",defaultInterest);
        map.put("alreadyRepaid",alreadyRepaid);
        returnMap.put("orderInfo",map);
        return returnMap;
    }

    /**
     * 根据订单ID获取订单签约信息
     * @author 仙海峰
     * @param orderId
     * @return
     */
    @Override
    public Map getPendingContractOrderInfoByOrderId(String orderId) {
        Map returnMap = new HashMap();
        String sql =" SELECT ID AS orderId , product_name_name AS productName ," +
                    " applay_money AS applayMoney , loan_amount AS loanAmount , contract_amount AS contractAmount , " +
                    " PERIODS AS periods , CREAT_TIME AS creatTime , Order_state AS orderState ,repay_type AS repayType  " +
                    " FROM mag_order WHERE ID='"+orderId+"' ";

        Map map =sunbmpDaoSupport.findForMap(sql);

        returnMap.put("PendingContractOrderInfo",map);
        return returnMap;
    }

    /**
     * 根据订单ID修改订单状态 并插入操作流程表(用户提交签约操作)
     * @author 仙海峰
     * @param orderId
     * @return
     */
    @Override
    public Map contractForSubmissionByOrderId(String orderId ,String userId) throws Exception {
        Map returnMap = new HashMap();
        String operationTime = DateUtils.getCurrentTime(DateUtils.STYLE_11);

        String checkSql="SELECT o.ID AS orderId , o.`CUSTOMER_NAME` AS customerName  ,o.loan_amount AS loanAmount " +
                    "FROM mag_order o WHERE o.`ID`='"+orderId+"' ";
        Map map =sunbmpDaoSupport.findForMap(checkSql);

        //获取批复金额
        String StringAmount = map.get("loanAmount").toString();
        BigDecimal amount=new BigDecimal(StringAmount);

        //获取用户姓名
        String customerName= map.get("customerName").toString();


        String insertSql="INSERT INTO order_operation_record (id,operation_node,operation_result,amount,order_id,operation_time,emp_id,emp_name,description) " +
                    "VALUES ('"+ GeneratePrimaryKeyUtils.getUUIDKey()+"',4,5,'"+amount+"',"+orderId+",'"+operationTime+"',"+userId+",'"+customerName+"','客户已签约完成')";

        String updateSql="UPDATE mag_order SET Order_state=4 WHERE ID='"+orderId+"'";

        if(!map.isEmpty()){
            int count = sunbmpDaoSupport.executeSql(insertSql);
            if(count !=0 ){
                int count2= sunbmpDaoSupport.executeSql(updateSql);
                if(count2 !=0){
                    returnMap.put("res_code",1);
                    returnMap.put("res_msg", "信息已提交，签约成功！");
                    return  returnMap;
                }else {
                    returnMap.put("res_code",0);
                    returnMap.put("res_msg", "签约失败！");
                }

            }
        }


        return returnMap;
    }

    /**
     * 根据UserId获取全部订单信息
     * @author 仙海峰
     * @param userId
     * @return
     */
    @Override
    public Map getAllOrderByUserId(String userId ,String pageNumber,String pageSize,String orderType) {
        Map returnMap = new HashMap();
        int unm= Integer.parseInt(pageNumber);
        int size= Integer.parseInt(pageSize);

        String sql;
        if (OrderTypeEnum.HAVE_IN_HAND.getCode().equals(orderType)){
            sql = "SELECT o.ID AS orderId ,o.order_no as orderNo,  o.CUSTOMER_NAME AS customerName ,  o.TEL AS tel ,  o.CARD AS card , " +
                    "o.product_name_name AS productName , o.applay_money AS applayMoney ,  " +
                    "o.loan_amount AS loanAmount ,  o.repay_money AS repayMoney , " +
                    "date_format(str_to_date( o.applay_time,'%Y%m%d%H%i%s'),'%Y-%c-%d %H:%i:%s') AS applayTime , " +
                    "date_format(str_to_date( o.Examine_time,'%Y%m%d%H%i%s'),'%Y-%c-%d %H:%i:%s') AS examineTime , " +
                    "DATE_FORMAT(STR_TO_DATE( o.CREAT_TIME,'%Y%m%d%H%i%s'),'%Y-%c-%d %H:%i:%s') AS creatTime , " +
                    "DATE_FORMAT(STR_TO_DATE( o.ALTER_TIME,'%Y%m%d%H%i%s'),'%Y-%c-%d %H:%i:%s') AS alterTime , " +
                    "DATE_FORMAT(STR_TO_DATE( o.loan_time,'%Y%m%d%H%i%s'),'%Y-%c-%d %H:%i:%s') AS loanTime , " +
                    "o.repay_date AS repayDate , " +
                    "o.contract_amount AS contractAmount , " +
                    "o.repay_type AS repayType ,  o.Job AS job ,  o.Service_fee AS serviceFee ,  o.loan_purpose AS loanPurpose , " +
                    "o.PERIODS AS periods ,  o.Order_state AS orderState " +
                    "FROM mag_order o WHERE o.USER_ID='"+userId+"' AND o.Order_state IN (2,3,4) ORDER BY CREAT_TIME DESC  limit "+((unm-1)*size)+","+size;
        }else if (OrderTypeEnum.REPAYMENT.getCode().equals(orderType)){
            sql = "SELECT o.ID AS orderId ,o.order_no as orderNo,  o.CUSTOMER_NAME AS customerName ,  o.TEL AS tel ,  o.CARD AS card , " +
                    "o.product_name_name AS productName , o.applay_money AS applayMoney ,  " +
                    "o.loan_amount AS loanAmount ,  o.repay_money AS repayMoney , " +
                    "date_format(str_to_date( o.applay_time,'%Y%m%d%H%i%s'),'%Y-%c-%d %H:%i:%s') AS applayTime , " +
                    "date_format(str_to_date( o.Examine_time,'%Y%m%d%H%i%s'),'%Y-%c-%d %H:%i:%s') AS examineTime , " +
                    "DATE_FORMAT(STR_TO_DATE( o.CREAT_TIME,'%Y%m%d%H%i%s'),'%Y-%c-%d %H:%i:%s') AS creatTime , " +
                    "DATE_FORMAT(STR_TO_DATE( o.ALTER_TIME,'%Y%m%d%H%i%s'),'%Y-%c-%d %H:%i:%s') AS alterTime , " +
                    "DATE_FORMAT(STR_TO_DATE( o.loan_time,'%Y%m%d%H%i%s'),'%Y-%c-%d %H:%i:%s') AS loanTime , " +
                    "o.repay_date AS repayDate , " +
                    "o.contract_amount AS contractAmount , " +
                    "o.repay_type AS repayType ,  o.Job AS job ,  o.Service_fee AS serviceFee ,  o.loan_purpose AS loanPurpose , " +
                    "o.PERIODS AS periods ,  o.Order_state AS orderState " +
                    "FROM mag_order o WHERE o.USER_ID='"+userId+"' AND o.Order_state='5' ORDER BY CREAT_TIME DESC  limit "+((unm-1)*size)+","+size;
        }else if (OrderTypeEnum.CONTRACT.getCode().equals(orderType)){
            sql = "SELECT o.ID AS orderId ,o.order_no as orderNo,  o.CUSTOMER_NAME AS customerName ,  o.TEL AS tel ,  o.CARD AS card , " +
                    "o.product_name_name AS productName , o.applay_money AS applayMoney ,  " +
                    "o.loan_amount AS loanAmount ,  o.repay_money AS repayMoney , " +
                    "date_format(str_to_date( o.applay_time,'%Y%m%d%H%i%s'),'%Y-%c-%d %H:%i:%s') AS applayTime , " +
                    "date_format(str_to_date( o.Examine_time,'%Y%m%d%H%i%s'),'%Y-%c-%d %H:%i:%s') AS examineTime , " +
                    "DATE_FORMAT(STR_TO_DATE( o.CREAT_TIME,'%Y%m%d%H%i%s'),'%Y-%c-%d %H:%i:%s') AS creatTime , " +
                    "DATE_FORMAT(STR_TO_DATE( o.ALTER_TIME,'%Y%m%d%H%i%s'),'%Y-%c-%d %H:%i:%s') AS alterTime , " +
                    "DATE_FORMAT(STR_TO_DATE( o.loan_time,'%Y%m%d%H%i%s'),'%Y-%c-%d %H:%i:%s') AS loanTime , " +
                    "o.repay_date AS repayDate , " +
                    "o.contract_amount AS contractAmount , " +
                    "o.repay_type AS repayType ,  o.Job AS job ,  o.Service_fee AS serviceFee ,  o.loan_purpose AS loanPurpose , " +
                    "o.PERIODS AS periods ,  o.Order_state AS orderState " +
                    "FROM mag_order o WHERE o.USER_ID='"+userId+"' AND o.Order_state IN (4,5,6) ORDER BY CREAT_TIME DESC  limit "+((unm-1)*size)+","+size;
        }else {
             sql = "SELECT o.ID AS orderId ,o.order_no as orderNo,  o.CUSTOMER_NAME AS customerName ,  o.TEL AS tel ,  o.CARD AS card , " +
                    "o.product_name_name AS productName , o.applay_money AS applayMoney ,  " +
                    "o.loan_amount AS loanAmount ,  o.repay_money AS repayMoney , " +
                    "date_format(str_to_date( o.applay_time,'%Y%m%d%H%i%s'),'%Y-%c-%d %H:%i:%s') AS applayTime , " +
                    "date_format(str_to_date( o.Examine_time,'%Y%m%d%H%i%s'),'%Y-%c-%d %H:%i:%s') AS examineTime , " +
                    "DATE_FORMAT(STR_TO_DATE( o.CREAT_TIME,'%Y%m%d%H%i%s'),'%Y-%c-%d %H:%i:%s') AS creatTime , " +
                    "DATE_FORMAT(STR_TO_DATE( o.ALTER_TIME,'%Y%m%d%H%i%s'),'%Y-%c-%d %H:%i:%s') AS alterTime , " +
                    "DATE_FORMAT(STR_TO_DATE( o.loan_time,'%Y%m%d%H%i%s'),'%Y-%c-%d %H:%i:%s') AS loanTime , " +
                    "o.repay_date AS repayDate , " +
                    "o.contract_amount AS contractAmount , " +
                    "o.repay_type AS repayType ,  o.Job AS job ,  o.Service_fee AS serviceFee ,  o.loan_purpose AS loanPurpose , " +
                    "o.PERIODS AS periods ,  o.Order_state AS orderState " +
                    "FROM mag_order o WHERE o.USER_ID='"+userId+"' ORDER BY CREAT_TIME DESC  limit "+((unm-1)*size)+","+size;
        }

        List allOrderList = sunbmpDaoSupport.findForList(sql);


        returnMap.put("allOrderList",allOrderList);
        return returnMap;
    }

    /**
     * 根据orderId获取待放款订单信息
     * @author 仙海峰
     * @param orderId
     * @return
     */
    @Override
    public Map getPendingMoneyInfoByOrderId(String orderId) {
        Map returnMap = new HashMap();

        String orderSql="SELECT o.ID AS orderId , o.`product_name_name` AS productName , o.`applay_money` AS applayMoney ," +
                                "o.`PERIODS` AS periods , o.`Order_state` AS orderStatus " +
                        "FROM mag_order o WHERE  o.`ID`='"+orderId+"' ";
        Map orderMap = sunbmpDaoSupport.findForMap(orderSql);
        String operationSql="SELECT operation_time AS operationTime , amount AS amount , operation_node AS operationNode  " +
                "FROM order_operation_record " +
                "WHERE  order_id='"+orderId+"' AND operation_node IN(1,4,5) ";

        List operationList = sunbmpDaoSupport.findForList(operationSql);
        returnMap.put("orderInfo",orderMap);
        returnMap.put("operationInfo",operationList);
        return  returnMap;

    }


    /**
     * 根据订单ID获取订单全部信息（包括订单操作流程信息）
     * @author 仙海峰
     * @param orderId
     * @return
     */
    @Override
    public Map getOrderAllInFoByOrderId(String orderId) {
        Map returnMap = new HashMap();
        Map operationMap = new HashMap();
        String orderSql = "SELECT   o.ID AS orderId ,  o.CUSTOMER_NAME AS customerName ,  o.TEL AS tel ,  o.CARD AS card , " +
                                    "o.product_name_name AS productName , o.applay_money AS applayMoney , o.CUSTOMER_ID AS customerId, " +
                                    "o.loan_amount AS loanAmount ,  o.repay_money AS repayMoney ," +
                                    "date_format(str_to_date( o.applay_time,'%Y%m%d%H%i%s'),'%Y-%c-%d %H:%i:%s') AS applayTime , " +
                                    "date_format(str_to_date( o.Examine_time,'%Y%m%d%H%i%s'),'%Y-%c-%d %H:%i:%s') AS examineTime , " +
                                    "DATE_FORMAT(STR_TO_DATE( o.CREAT_TIME,'%Y%m%d%H%i%s'),'%Y-%c-%d %H:%i:%s') AS creatTime , " +
                                    "DATE_FORMAT(STR_TO_DATE( o.ALTER_TIME,'%Y%m%d%H%i%s'),'%Y-%c-%d %H:%i:%s') AS alterTime , " +
                                    "DATE_FORMAT(STR_TO_DATE( o.loan_time,'%Y%m%d%H%i%s'),'%Y-%c-%d %H:%i:%s') AS loanTime , " +
                                    "o.repay_date AS repayDate , " +
                                    " o.contract_amount AS contractAmount , " +
                                    " o.repay_type AS repayType ,  o.Job AS job ,  o.Service_fee AS serviceFee ,  o.loan_purpose AS loanPurpose , " +
                                    " o.PERIODS AS periods ,  o.Order_state AS orderStatus  ,wpd.payment AS payment " +
                            "FROM mag_order o " +
                            "LEFT JOIN mag_product_fee pf ON o.product_detail = pf.product_id " +
                            "LEFT JOIN pro_working_product_detail wpd ON pf.product_id=wpd.id " +
                            "WHERE  o.ID='"+orderId+"' ";

        Map orderMap = sunbmpDaoSupport.findForMap(orderSql);

        String operationSql="SELECT id AS operationId , order_id AS orderId , emp_id AS empId , emp_name AS empName," +
                                    "date_format(str_to_date(operation_time,'%Y%m%d%H%i%s'),'%Y-%c-%d %H:%i:%s') AS operationTime , " +
                                    "amount AS amount , STATUS AS status , operation_node AS operationNode , " +
                                    "operation_result AS operationResult , description AS description " +
                            "FROM order_operation_record " +
                            "WHERE order_id='"+orderId+"' AND  operation_node NOT IN (2)  ORDER BY operation_time DESC";
        List operationList = sunbmpDaoSupport.findForList(operationSql);
        if (operationList!=null){

            //获取订单状态
             String orderStatus= orderMap.get("orderStatus").toString();

            operationMap.put("operationId","");
            operationMap.put("operationNode",orderStatus);
            operationMap.put("operationResult","");
            operationMap.put("status","");
            operationMap.put("amount","");
            operationMap.put("orderId",orderId);
            operationMap.put("operationTime","");
            operationMap.put("empId","");
            operationMap.put("empName","");
            operationMap.put("description","");
            operationMap.put("orderState",orderStatus);

            //添加到operationList 索引为0的位置
            operationList.add(0,operationMap);
          ;
        }
        returnMap.put("orderInfo",orderMap);
        returnMap.put("operationInfo",operationList);
        return  returnMap;
    }


    /**
     * 根据订单ID修改订单状态
     * @author 仙海峰
     * @param orderId
     * @return
     */
    @Override
    public Map updateOrderStatusByOrderId(String orderId,String state) throws Exception {
        Map returnMap = new HashMap();
        String updateSql="UPDATE mag_order SET Order_state='"+state+"'  WHERE ID='"+orderId+"'";
        int count= sunbmpDaoSupport.executeSql(updateSql);
        if(count != 0){
            returnMap.put("res_code",1);
            returnMap.put("res_msg","订单状态修改成功！");
        }else {
            returnMap.put("res_code",0);
            returnMap.put("res_msg","订单状态修改失败！");
        }

        return returnMap;

    }


}