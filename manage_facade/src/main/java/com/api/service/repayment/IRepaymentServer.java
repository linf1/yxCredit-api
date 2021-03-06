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
     * @throws Exception 异常
     */
    BYXResponse getLoan(Map map) throws Exception;

    /**
     * 查询还款计划
     * @param record
     *               businessId - 订单编号
     *               repaymentType 类 -1 全部；0未还款；1 正常还款; 2 提前还款;3 部分提前还款
     *
     * @return 返回结果
     * @throws Exception 异常
     */
    BYXResponse getRepaymentListByProjectId(BusinessRepayment record) throws Exception;

    /**
     * 删除当前还款计划
     * @param  orderNo 订单编号
     * @throws Exception 异常
     */
    void deleteRepayment(String orderNo) throws Exception;

    /**
     * 更新还款计划状态
     */
    boolean  updateRepayment(String repayId);
}
