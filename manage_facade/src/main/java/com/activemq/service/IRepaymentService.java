package com.activemq.service;

import com.activemq.entity.respose.LoanDetailResponse;
import com.activemq.entity.respose.RepaymentResponse;
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
    boolean repaymentHandel(BYXResponse byxResponse);

}
