package com.activemq.service;

import com.activemq.entity.respose.LoanDetailResponse;
import com.activemq.entity.respose.RepaymentListResponse;
import com.api.model.common.BYXResponse;

/**
 * 放款服务
 *
 * @author 陈淸玉 create on 2018-07-20
 */
public interface IRepaymentService {
    String BEAN_KEY = "repaymentServiceImpl";

    /**
     * 放款
     * @param loanDetailResponse 放款实体
     */
    boolean loanMoney(LoanDetailResponse loanDetailResponse);

    /**
     * 保存还款信息
     * @param repaymentListResponse 还款信息实体
     */
    boolean saveRepaymentInfo(RepaymentListResponse repaymentListResponse);
    /**
     * 更新还款信息
     * @param repaymentListResponse 还款信息实体
     */
    boolean updateRepaymentInfo(RepaymentListResponse repaymentListResponse);

    /**
     * 推送放款及还款计划 主要处理方法
     * @param byxResponse 获得的信息
     * @return 处理结果
     */
    boolean repaymentHandel(BYXResponse byxResponse);

}
