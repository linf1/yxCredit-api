package com.zhiwang.zwfinance.app.jiguang.util.api.util;

/**
 * 订单状态【1.待申请、2.审核中、3.待签约、4.待放款、5.待还款、6.已结清、7.已取消、8.审批拒绝、9.已放弃】
 * @author 仙海峰
 */
public enum OrderStateEnum {

    /**
     * 待申请
     */
    PENDING_APPLICATION(1),

    /**
     * 审核中
     */
    AUDIT(2),

    /**
     * 待签约
     */
    PENDING_CONTRACT(3),

    /**
     * 待放款
     */
    PENDING_LOAN(4),

    /**
     * 待还款
     */
    PENDING_REPAYMENT(5),

    /**
     * 已结清
     */
    ALREADY_SETTLED(6),

    /**
     * 已取消
     */
    CANCEL(7),


    /**
     * 审批拒绝
     */
    REFUSE(8),

    /**
     * 已放弃
      */
    ABANDON(9);


    private int code;
    OrderStateEnum(int code){
        this.code = code;
    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


}
