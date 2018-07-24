package com.enums;

/**
 * business_repayment 表 还款类型，0未还款,1 正常还款; 2 提前还款;3 部分提前还款;4逾期还款，5逾期未还;
 * @author luochaofang create on 2018-07-22
 */
public enum RepaymentTypeEnum {

    /**
     * 全部
     */
    REPAYMENT_ALL(-1,"全部"),
    /**
     *未还款
     */
    REPAYMENT_NO(0,"未还款"),
    /**
     *正常还款
     */
    REPAYMENT_NORMAL(1,"正常还款"),
    /**
     * 提前还款
     */
    PREPAYMENT(2,"提前还款"),
    /**
     * 部分提前还款
     */
    PREPAYMENT_PARTIAL(3,"部分提前还款"),
    /**
     * 逾期还款
     */
    PAYMENT_LATE(4,"逾期还款"),
    /**
     * 逾期未还
     */
    OVERDUE(5,"逾期未还");

    RepaymentTypeEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    private Integer code;
    private String name;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RepaymentTypeEnum getBycode(String code){
        for (RepaymentTypeEnum repaymentStatusEnum : RepaymentTypeEnum.values()) {
            if(repaymentStatusEnum.code.equals(code)){
                return repaymentStatusEnum;
            }
        }
        return null;
    }
}
