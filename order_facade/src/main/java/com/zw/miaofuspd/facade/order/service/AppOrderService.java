package com.zw.miaofuspd.facade.order.service;


import com.zw.miaofuspd.facade.entity.BranchInfo;

import java.util.Map;
import java.util.List;
/**
 * Created by Administrator on 2017/2/20 0020.
 */
public interface AppOrderService {

    /**
     * 通过业务员编号获取该业务员
     * 下所有的订单
     * @param employeeId
     * @return
     */
    Map getAllOrder(String employeeId,String type);

    /**
     * 办单员端模糊搜索订单
     * @param  employeeId 办单员Id
     * @param  searchContent 搜索内容
     * @param  state 订单的状态
     *  state
     *  1: 全部订单  2:未提交订单
     *  3:审核中订单 4:内拒单
     * */
    Map findOrderLike(String employeeId,String merchantId,String searchContent,String state,String type) throws Exception;

    /**
     * 清空订单搜索历史记录
     * @param employeeId 办单员Id
     * @return
     * @throws Exception
     */
    Map deleteOrderSearchHistory(String employeeId,String merchantId,String type) throws Exception;


    /**
     * 展示订单搜索历史的前几条记录
     * @param employeeId 办单员Id
     */
    List  showOrderSearchHistory(String employeeId,String merchantId,String type) throws Exception;

    /**
     * 通过订单id获取逾期天数
     * @param order_id
     * @return
     */
    String getOverdueState(String order_id);
    /**
     * 根据订单id修改订单状态
     * @param orderId
     * @param state
     * @return
     */
    Map updateOrderState(String orderId,String state) throws Exception;

    /**
     * 修改订单的状态，用于订单提交之前
     * 完善信息，上传影像资料，手签的环节
     * @param orderId 订单Id
     * @param state  订单状态
     */
    Map updateOrderStateBeforeSubmit (String orderId,String state) throws Exception;


    /**
     * 订单提交修改状态
     * 内拒单不予提交
     * @param orderId 订单Id
     * @param state  订单状态
     * @param isSign
     */
    Map submitUpdateStatus(String orderId,String state,String isSign) throws Exception;

    /**
     * 根据id修改商品订单状态状态
     * @param orderId
     * @param state
     * @return
     */
    Map updateOrderCommState(String orderId, String state) throws Exception;


    /**
     * 根据用户id获取该用户下所有订单
     * @param customerId
     * @return
     */
    Map getAllPersonOrder(String customerId);

    /**
     * 专用于我的分期申请
     * @param customerId
     * @return
     */
    Map getAllPersonOrder1(String customerId);

    /**
     * 通过orderId获取该订单信息
     * @param orderId
     * @return
     */
    Map getOrderById(String orderId);

    /**
     * 添加订单
     * @param map
     * @return
     * @throws Exception
     */
    Map addOrder(Map map) throws Exception;

    /**
     * 订单Id查找用户Id
     * @param orderId 订单Id
     * @return
     * @throws Exception
     */
    Map getCustomerIdByOrderId(String orderId);


    /**
     * 分期订单详情-根据订单id获取订单的详细信息
     * @param orderId
     * @return
     */
    Map getFenqiOrderInfo(String orderId);

    /**
     * 根据orderId设置对应订单商品串号
     * @param orderId
     * @param merCode
     * @return
     */
    void setMerchandiseCode(String orderId,String merCode,String state);

    /**
     * 查询当月还款计划是否已还
     * @param orderId
     * @return
     */
    String checkNowMonthPay(String orderId);

    Map getSaleInfo(String empId);

    BranchInfo getBranchInfo(String branchId);

    BranchInfo getGongSiName(BranchInfo branchInfo);

    Map getCompanyInfo(String company);

    Map calculationRepayment(Map orderMap);


    /****************************************************碧友信**********************************************/

    /**
     * 根据用户ID和订单状态获取订单信息列表(2.审核中、3.待签约、4.待放款)
     * @author 仙海峰
     * @param userId
     * @return
     */
    Map getOrderListByUserId(String userId) ;

    /**
     * 根据订单ID获取订单信息
     * @author 仙海峰
     * @param orderId
     * @return
     */
    Map getOrderInfoByOrderId(String orderId);


    /**
     * 根据订单ID获取订单签约信息
     * @author 仙海峰
     * @param orderId
     * @return
     */
    Map getPendingContractOrderInfoByOrderId(String orderId);

    /**
     * 根据订单ID提交签约信息
     * @author 仙海峰
     * @param orderId
     * @return
     */
    Map contractForSubmissionByOrderId(String orderId,String userId) throws Exception;

    /**
     *根据UserId 获取所有订单信息
     * @author 仙海峰
     * @param userId
     * @return
     */
    Map getAllOrderByUserId(String userId);

    /**
     * 根据orderId获取待放款订单信息
     * @author 仙海峰
     * @param orderId
     * @return
     */
    Map getPendingMoneyInfoByOrderId(String orderId);


    /**
     * 根据订单ID获取订单全部信息（包括订单操作流程信息）
     * @author 仙海峰
     * @param orderId
     * @return
     */
    Map getOrderAllInFoByOrderId(String orderId);

    /**
     * 根据订单ID修改订单状态
     * @author 仙海峰
     * @param orderId
     * @return
     */
    Map updateOrderStatusByOrderId(String orderId,String state) throws Exception;

}
