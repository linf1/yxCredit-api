package com.api.model.ds;

import java.io.Serializable;

/**
 * 借款人及放款账户数据同步实体
 * @author 陈青玉
 */
public class DSMoneyRequest implements Serializable {
    private static final long serialVersionUID = -2021848220657721076L;
    /**
     * Y	string	借款人用户名
     */
    private String borrowerUserName;
    /**
     * Y	Integer	借款人类型（0：个人；1：企业）
     */
    private Integer borrowerType = 0;
    /**
     *Y	String	借款人证件类型0（1、身份证，2、户口簿，3、军人身份证件，4、武装警察身份证件，5、台湾居民往来内地通行证，6、外国人永久居留证，7、外国护照，9、港澳居民往来大陆通行证，a、中国护照，b、边民出入境通行证，c、其他个人证件）
     *1（1、统一社会信用码；2、营业执照）
     */
    private String borrowerCardType = "1";
    /**
     * Y	String	借款人名称
     */
    private String borrowerName;
    /**
     * Y	String	借款人证件号
     */
    private String borrowerCardNo;
    /**
     * Y	String	借款人地址
     */
    private String address;
    /**
     * Y	String	借款人手机号
     */
    private String borrowerMobilePhone;
    /**
     *Y	String	借款人渠道（YXD）
     */
    private String borrowerChannel;
    /**
     *Y	String	第三方存储借款人的主键
     */
    private String borrowerThirdId;
    /**
     *Y	String	0：个人；1：企业
     */
    private String accountType  = "0";
    /**
     *Y	String	放款人户名
     */
    private String accountName;
    /**
     *Y	String	放款人证件号（当借款人与放款户名不一致时，必填项）
     */
    private String accountIdCard;
    /**
     *Y	String	省编码
     */
    private String provinceCode;
    /**
     *Y	String	省名称
     */
    private String provinceName;
    /**
     *Y	String	市编码
     */
    private String cityCode;
    /**
     *Y	String	市名称
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
     *Y	String	支行名称
     */
    private String bankBranchName;
    /**
     *Y	String	支行联行号
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

    public String getBorrowerUserName() {
        return borrowerUserName;
    }

    public void setBorrowerUserName(String borrowerUserName) {
        this.borrowerUserName = borrowerUserName;
    }

    public Integer getBorrowerType() {
        return borrowerType;
    }

    public void setBorrowerType(Integer borrowerType) {
        this.borrowerType = borrowerType;
    }

    public String getBorrowerCardType() {
        return borrowerCardType;
    }

    public void setBorrowerCardType(String borrowerCardType) {
        this.borrowerCardType = borrowerCardType;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public String getBorrowerCardNo() {
        return borrowerCardNo;
    }

    public void setBorrowerCardNo(String borrowerCardNo) {
        this.borrowerCardNo = borrowerCardNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBorrowerMobilePhone() {
        return borrowerMobilePhone;
    }

    public void setBorrowerMobilePhone(String borrowerMobilePhone) {
        this.borrowerMobilePhone = borrowerMobilePhone;
    }

    public String getBorrowerChannel() {
        return borrowerChannel;
    }

    public void setBorrowerChannel(String borrowerChannel) {
        this.borrowerChannel = borrowerChannel;
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
}
