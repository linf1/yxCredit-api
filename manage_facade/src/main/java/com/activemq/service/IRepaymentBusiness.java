package com.activemq.service;

import com.activemq.entity.respose.LoanDetailResponse;
import com.activemq.entity.respose.RepaymentResponse;
import com.api.model.common.BYXResponse;
import com.sun.org.apache.xpath.internal.operations.Or;
import com.zw.pojo.Order;

/**
 * 放款业务服务
 * @author 陈淸玉 create on 2018-07-20
 */
public interface IRepaymentBusiness {
    String BEAN_KEY = "repaymentBusinessImpl";

    /**
     * 放款
     * @param loanDetailResponse 放款实体
     * @return 是否成功
     */
    boolean loanMoney(LoanDetailResponse loanDetailResponse);

    /**
     * 保存还款信息
     * @param repaymentResponse 还款信息实体
     * @return 是否成功
     */
    boolean saveRepaymentInfo(RepaymentResponse repaymentResponse);

    /**
     * 设置放款账户信息
     * @param loanDetailResponse 放款账户信息
     * @return 是否成功
     */
    boolean setLoanInfo(LoanDetailResponse loanDetailResponse);

    /**
     * 更新还款信息
     * @param repaymentResponse 还款信息实体
     * @return 是否成功
     */
    boolean updateRepaymentInfo(RepaymentResponse repaymentResponse);

    /**
     * 推送放款及还款计划 主要处理方法
     * @param byxResponse 获得的信息
     * @return 处理结果
     */
    boolean repaymentHandel(BYXResponse byxResponse) throws Exception;


    /**
     * 网贷发生还款，正常还款 or 提前还款消息 主要处理方法
     * @param byxResponse 获得的信息
     * @return 处理结果
     */
    boolean repaymentPushAssentHandel(BYXResponse byxResponse);

    /**
     * 发送信息（短信，站内信）
     * @param order 订单信息
     * @throws  Exception http异常
     */
    void sendMessage(Order order) throws Exception;

    /**
     * 如果还款计划已经全部还款 那么就更新订单状态为已经结清
     * @param businessId 订单编号
     * @return 是否更新成功
     */
    void settleOrder(String businessId);

}
