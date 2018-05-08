package com.zw.miaofuspd.facade.openaccount.service;

import com.zw.miaofuspd.facade.entity.AppUserInfo;

import java.util.List;
import java.util.Map;

public interface IRepayPlanService {
    /**
     * 秒付金服-获取多笔订单的还款计划
     *
     * @return
     * @throws Exception
     */
    Map getRepayplanList(AppUserInfo userInfo) throws Exception;//还款计划
    /**
     * 秒付金服-获取一笔还款计划的详情
     * repaymentId 还款计划id
     * orderId  订单id
     * @return
     * @throws Exception
     */
    Map getRepayDetails(String repaymentId,String orderId,String customerId);//获取单个还款计划的详情
    //逾期还款说明
    Map getOverDueRemrak();

    void addRepayment(String orderId);

    Map settleAll(String orderId);

    Map getRepayDetailList(String orderId);

    List getBillingDetails(String orderId);

    Map getSettleAuth(String orderId) throws Exception;

}
