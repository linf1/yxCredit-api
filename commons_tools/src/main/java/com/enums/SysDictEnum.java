package com.enums;

/**
 * 系统配置 parentCode = 0 为父节点
 * @author 韩梅生
 */
public enum SysDictEnum {

    /**
     * 还款配置
     */
    repayment_settings("repayment_settings","0"),
    /**
     * 预计还款时间减去的天数
     */
    advance_day("advance_day","repayment_settings");

    SysDictEnum(String code, String parentCode){
        this.code = code;
        this.parentCode = parentCode;
    }

    private String code;
    private String parentCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public SysDictEnum getBycode(String code){
        for (SysDictEnum identityEnum : SysDictEnum.values()) {
            if(identityEnum.code.equals(code)){
                return identityEnum;
            }
        }
        return null;
    }
}
