package com.zhiwang.zwfinance.app.jiguang.util.api.util;

/**
 * 放款状态 1待放款，2放款中，3已放款，4放款失败
 * @author 陈淸玉 create on 2018-07-20
 */
public enum LoanStateEnum {

    /**
     * 1待放款
     */
    PENDING_LOAN(1,"待放款"),

    /**
     * 2放款中
     */
    LOANING(2,"放款中"),

    /**
     * 3已放款
     */
    LOANED(3,"已放款"),

    /**
     * 4放款失败
     */
    LOAN_FAIL(4,"放款失败");

    LoanStateEnum(int code,String label){
        this.code = code;
        this.label = label;
    }

    private int code;
    private String label;
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public static LoanStateEnum getLoanStateEnumByCode(int code){
        for (LoanStateEnum loanStateEnum : LoanStateEnum.values()) {
            if (loanStateEnum.getCode() == code) {
                return loanStateEnum;
            }
        }
        return null;
    }
}
