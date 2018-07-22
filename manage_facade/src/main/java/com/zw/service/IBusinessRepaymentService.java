package com.zw.service;

import com.zw.pojo.BusinessRepayment;

import java.util.List;
import java.util.Map;

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
     *              orderNo 订单编号
     *              period 期数
     * @return 影响行数
     */
    int updateByOrderIdAndPeriod(BusinessRepayment record);


    /**
     * 根据还款状态获取还款信息
     * @param status 还款状态
     * @return 还款信息
     */
    List<BusinessRepayment> findRepaymentInfoByStatus(String status);

    /**
     * 根据订单id获取还款计划
     * @param record
     * @return
     */
    Map getRepaymentByOrderId(BusinessRepayment record);

    /**
     * 根据订单id获取还款期数
     * @param record
     * @return
     */
    List<Map> findListByOrderId(BusinessRepayment record);
}
