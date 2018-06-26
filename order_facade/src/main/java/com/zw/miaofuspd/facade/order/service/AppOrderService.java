package com.zw.miaofuspd.facade.order.service;


import com.zw.miaofuspd.facade.entity.BranchInfo;

import java.util.Map;
import java.util.List;
/**
 * Created by Administrator on 2017/2/20 0020.
 */
public interface AppOrderService {

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
     *根据UserId 获取所有订单列表
     * @author 仙海峰
     * @param userId
     * @return
     */
    Map getAllOrderByUserId(String userId,String pageNumber,String pageSize,String orderType);

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
