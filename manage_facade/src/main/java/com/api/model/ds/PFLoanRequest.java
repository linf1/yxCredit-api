package com.api.model.ds;

import java.io.Serializable;

/**
 * @author 韩梅生
 *保存放款账户接口实体
 */
public class PFLoanRequest implements Serializable {
    private static final long serialVersionUID = -2021848220657821076L;

    /**
     * 借款人主键id
     */
    private  Long userBorrowerId;
    /**
     *Y	String	第三方存储借款人的主键
     */
    private String borrowerThirdId;
    /**
     *Y	String	0：个人；1：企业
     */
    private String accountType;
    /**
     *Y	String	放款人户名
     */
    private String accountName;
    /**
     *Y	String	放款人证件号（当借款人与放款户名不一致时，必填项）
     */
    private String accountIdCard;
    /**
     *N	String	省编码
     */
    private String provinceCode;
    /**
     *N	String	省名称
     */
    private String provinceName;
    /**
     *N	String	市编码
     */
    private String cityCode;
    /**
     *N	String	市名称
     */
    private String cityName;
    /**
     *Y	String	银行名称对应编码
     */
    private String bankCode;
    /**
     *Y	String	银行名称
     */
    private String bankName;
    /**
     *Y	String	卡号
     */
    private String bankCardNo;
    /**
     *N	String	支行名称
     */
    private String bankBranchName;
    /**
     *N	String	支行联行号
     */
    private String cnapsCode;
    /**
     *Y	String	是否是他行（0：浙商；1：非浙商）
     */
    private String otherFlag;
    /**
     *Y	String	账户渠道（YXD）
     */
    private String accountChannel;
    /**
     *Y	String	第三方存储放款账户的主键
     */
    private String accountThirdId;

    public Long getUserBorrowerId() {
        return userBorrowerId;
    }

    public void setUserBorrowerId(Long userBorrowerId) {
        this.userBorrowerId = userBorrowerId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getBorrowerThirdId() {
        return borrowerThirdId;
    }

    public void setBorrowerThirdId(String borrowerThirdId) {
        this.borrowerThirdId = borrowerThirdId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountIdCard() {
        return accountIdCard;
    }

    public void setAccountIdCard(String accountIdCard) {
        this.accountIdCard = accountIdCard;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getBankBranchName() {
        return bankBranchName;
    }

    public void setBankBranchName(String bankBranchName) {
        this.bankBranchName = bankBranchName;
    }

    public String getCnapsCode() {
        return cnapsCode;
    }

    public void setCnapsCode(String cnapsCode) {
        this.cnapsCode = cnapsCode;
    }

    public String getOtherFlag() {
        return otherFlag;
    }

    public void setOtherFlag(String otherFlag) {
        this.otherFlag = otherFlag;
    }

    public String getAccountChannel() {
        return accountChannel;
    }

    public void setAccountChannel(String accountChannel) {
        this.accountChannel = accountChannel;
    }

    public String getAccountThirdId() {
        return accountThirdId;
    }

    public void setAccountThirdId(String accountThirdId) {
        this.accountThirdId = accountThirdId;
    }

    @Override
    public String toString() {
        return "PFLoanRequest{" +
                "userBorrowerId=" + userBorrowerId +
                ", borrowerThirdId='" + borrowerThirdId + '\'' +
                ", accountType='" + accountType + '\'' +
                ", accountName='" + accountName + '\'' +
                ", accountIdCard='" + accountIdCard + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", cityName='" + cityName + '\'' +
                ", bankCode='" + bankCode + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankCardNo='" + bankCardNo + '\'' +
                ", bankBranchName='" + bankBranchName + '\'' +
                ", cnapsCode='" + cnapsCode + '\'' +
                ", otherFlag='" + otherFlag + '\'' +
                ", accountChannel='" + accountChannel + '\'' +
                ", accountThirdId='" + accountThirdId + '\'' +
                '}';
    }
}
