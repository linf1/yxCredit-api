package com.api.service.repayment;

import com.api.model.common.BYXResponse;
import com.zw.pojo.BusinessRepayment;

import java.util.Map;

/**
 * 放款接口服务
 * @author 韩梅生
 */
public interface IRepaymentServer {

    String BEAN_KEY = "repaymentServerImpl";

    /**
     * 预算提前还款
     * @author 韩梅生
     *
     */
    BYXResponse trialRepayment(Map map) throws Exception;
    /**
     * 确认提前还款
     * @author 韩梅生
     *
     */
    BYXResponse prepaymentRecode(Map map) throws Exception;
    /**
     * 查询还款账号
     * @author 韩梅生
     *
     */
    BYXResponse getLoan(Map map) throws Exception;

    /**
     * 查询还款计划
     * @param record
     * @return 返回结果
     * @exception Exception 异常
     */
    BYXResponse getRepaymentListByProjectId(BusinessRepayment record) throws Exception;
}
