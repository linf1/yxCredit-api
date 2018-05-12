package com.zhiwang.zwfinance.app.jiguang.util.api.util;

/**
 * 操作状态（1.待申请，2.审核中，3.已审核，4.待签约，5.待放款，6.已放款，7.已拒绝，8.取消）
 * @author 仙海峰
 */
public enum OperationStateEnum {

    /**
     * 待申请
     */
    PENDING_APPLICATION(1),

    /**
     * 审核中
     */
    AUDITING(2),

    /**
     * 已审核
     */
    AUDITED(3),

    /**
     * 待签约
     */
    PENDING_CONTRACT(4),

    /**
     * 待放款
     */
    PENDING_MONEY(5),

    /**
     * 已放款
     */
    SEND_MONEY(6),

    /**
     * 已拒绝
     */
    REFUSE(7),

    /**
     * 取消
     */
    CANCEL(8);







    private int code;
    OperationStateEnum(int code){
        this.code = code;
    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


}
