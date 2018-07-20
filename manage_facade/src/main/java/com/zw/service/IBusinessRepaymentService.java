package com.zw.service;

import com.zw.pojo.BusinessRepayment;

/**
 * 还款信息服务
 * @author 陈清玉
 */
public interface IBusinessRepaymentService {

    String BEAN_KEY = "businessRepaymentServiceImpl";

    /**
     * 保存还还款信息
     * @param repayment 还款信息实体
     * @return true 成功 false 失败
     */
    boolean saveRepayment(BusinessRepayment repayment);

    /**
     *  获取还款信息
     * @param id 主键ID
     * @return true 成功 false 失败
     */
    BusinessRepayment selectById(String id);


    /**
     * 获取某个订单某期的还款计划
     * @param record
     *              orderId 订单id
     *              period 期数
     * @return 返回还款计划信息
     */
    BusinessRepayment getByOrderIdAndPeriod(BusinessRepayment record);

    /**
     * 更新某个订单某期的还款计划
     * @param record
     *              orderId 订单id
     *              period 期数
     * @return 影响行数
     */
    int updateByOrderIdAndPeriod(BusinessRepayment record);
}
