package com.zhiwang.zwfinance.app.jiguang.util.api.util;

/**
 * 订单类型【1.处理中、2.还款中、3.合同订单】
 * @author 仙海峰
 */
public enum OrderTypeEnum {

    /**
     * 处理中
     */
    HAVE_IN_HAND("1"),

    /**
     * 还款中
     */
    REPAYMENT("2"),

    /**
     * 合同订单
     */
    CONTRACT("3");

    OrderTypeEnum(String code) {
        this.code = code;
    }

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
