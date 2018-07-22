package com.enums;

/**
 * business_repayment 表 还款状态  1、还款中、2还款处理中、3已还款
 * @author 陈淸玉 create on 2018-07-22
 */
public enum RepaymentStatusEnum {

    /**
     *还款中
     */
    REPAYMENTS("1","还款中"),
    /**
     *还款处理中
     */
    REPAYMENT_PROCESSING("2","还款处理中"),
    /**
     * 已还款
     */
    REPAYMENT("3","已还款");
    RepaymentStatusEnum(String code, String name){
        this.code = code;
        this.name = name;
    }

    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RepaymentStatusEnum getBycode(String code){
        for (RepaymentStatusEnum repaymentStatusEnum : RepaymentStatusEnum.values()) {
            if(repaymentStatusEnum.code.equals(code)){
                return repaymentStatusEnum;
            }
        }
        return null;
    }
}
