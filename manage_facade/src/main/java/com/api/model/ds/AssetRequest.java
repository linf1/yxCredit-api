package com.api.model.ds;

import java.io.Serializable;

/**
 * 资产接口
 * @author 韩梅生
 */
public class AssetRequest implements Serializable {
    private static final long serialVersionUID = -6954899091994975632L;


    /**
     *  String 订单id
     */
   // private  String orderId;
    /**
     * Y	string	资产名称
     */
    private String assetName;
    /**
     * Y	String	资产金额（元）
     */
    private String assetAmount;
    /**
     * 居间服务费收取方式（1.融资到期日一次性支付全部
     * 2.融资发放前一次性支付全部
     * 3.按月支付，即起息日在个月的对日
     * 4.按三月支付，即起息日在每三个月末月的对日
     * 5.按六个月支付，即起息日在每六个月末月对日
     * 6.融资发放前支付首月，剩余各月于起息日在各月的对日）
     */
    private String assetServicePayStyle = "2";
    /**
     * Y	String	融资成本（0.00已除以100的结果）   －借款利率+居间服务费
     */
    private String assetFinanceCost;
    /**
     * Y	String	投资人收益率（0.00已除以100的结果）－空
     */
    private String assetInvestRate = "";
    /**
     * Y	String	居间服务费年化利率（0.00已除以100的结果）
     */
    private String assetServiceRate;
    /**
     * Y	String	借款期限
     */
    private String assetBorrowTimeLimit;
    /**
     *Y	String	借款期限单位（0 月 1 天）
     */
    private String assetBorrowTimeType;
    /**
     *Y	String	借款添加时间（使用yyyy-MM-ddHH:mm:ss格式）
     */
    private String assetBorrowCreateTime;
    /**
     *Y	String	借款用途
     */
    private String assetBorrowApplication;
    /**
     *Y	String	借款还款方式（1.一次性还本付息
     * 2.按月付息，到期还本付息
     * 3.按三月付息，到期还本付息
     * 4.按六月付息，到期还本付息
     * 5.等本等息
     * 6等额本息
     * 7.等额本金）
     */
    private String assetRepaymentStyle = "1";
    /**
     *Y	String	是否允许提前还款(0是，1否)
     */
    private String assetPrepayment;
    /**
     *Y	String	计划提前还款期限
     */
    private String assetPrepaymentTimeLimit;
    /**
     *Y	String	计划提前还款期限单位（0 月 1 天）
     */
    private String assetPrepaymentTimeType;
    /**
     *Y	String	提前还款最少计息天数－空
     */
    private String assetPrepaymentMinDays = "";
    /**
     *Y	String	BYX用户中心用户ID
     */
    private String userId;
    /**
     *Y	String	借款客户类型(1-个人，2-企业)
     */
    private String assetCustomerType = "1";
    /**
     *Y	String	借款客户名称
     */
    private String assetCustomerName;
    /**
     *Y	String	所属行业（A-农、林、牧、渔业；
     * B-采掘业；
     * C-制造业；
     * D-电力、燃气及水的生产和供应业；
     * E-建筑业；
     * F-交通运输、仓储和邮政业；
     * G-信息传输、计算机服务和软件业；
     * H-批发和零售业；
     * I-住宿和餐饮业；
     * J-金融业；
     * K-房地产业；
     * L-租赁和商务服务业；
     * M-科学研究、技术服务业和地质勘察业；
     * N-水利、环境和公共设施管理业；
     * O-居民服务和其他服务业；
     * P-教育；
     * Q-卫生、社会保障和社会福利业；
     * R-文化、体育和娱乐业；
     * S-公共管理和社会组织；
     * T-国际组织；
     * Z-其他）
     */
    private String assetCustomerIndustry = "";
    /**
     *Y	String	个人借款人身份证号
     */
    private String assetPersonIdcard;
    /**
     *Y	String	个人借款人手机号
     */
    private String assetPersonMobilePhone;
    /**
     *Y	String	个人借款人工作性质（1工人、2农民、3商人、4学生5其他）
     */
    private String assetPersonJobNature = "1";
    /**
     *Y	String	个人借款人个人地址
     */
    private String assetPersonAddress;
    /**
     *Y	String	放款账号ID
     */
    private String byxBankId;
    /**
     *Y	String	放款账号类型（1.个人 2 企业）
     */
    private String assetBankType = "1";
    /**
     *Y	String	账号名称
     */
    private String assetBankUserName;
    /**
     *Y	String	账号银行名称
     */
    private String assetBankName;
    /**
     *Y	String	账号银行(银行联行号)
     */
    private String assetBankCnapsCode;
    /**
     *Y	String	银行卡号
     */
    private String assetBankNo;
    /**
     *Y	String	开户银行地址
     */
    private String assetBankAddress;
    /**
     *Y	String	放款人身份证号
     */
    private String assetLoanCard;
    /**
     *Y	String	担保方式（1.无限连带
     * 2.动产抵押
     * 3.不动产抵押
     * 4.股权质押
     * 5.商票质押
     * 6.核心企业承付
     * 7.其他）
     */
    private String assetGuarantorStyle = "1";
    /**
     * Y String 担保人姓名
     */
    private String  assetGuarantorName;
    /**
     * Y String 借款合同ID
     */
    private String  contractId;
    /**
     * Y String 居间服务协议ID
     */
    private String  serviceContractId;
    /**
     * Y String 债权协议ID－空
     */
    private String  bondsContractId = "";
    /**
     * Y String 第三方资产订单号
     */
    private  String thirdAssetOrderNum;

    /**
     * 总包商
     * @return
     */
   // private  String contractorName;

    /**
     * 本地用户id
     * @return
     */
    //private  String customerId;

//    public String getOrderId() {
//        return orderId;
//    }
//
//    public void setOrderId(String orderId) {
//        this.orderId = orderId;
//    }
//
//    public String getCustomerId() {
//        return customerId;
//    }
//
//    public void setCustomerId(String customerId) {
//        this.customerId = customerId;
//    }
//
//    public String getContractorName() {
//        return contractorName;
//    }
//
//    public void setContractorName(String contractorName) {
//        this.contractorName = contractorName;
//    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getAssetAmount() {
        return assetAmount;
    }

    public void setAssetAmount(String assetAmount) {
        this.assetAmount = assetAmount;
    }

    public String getAssetServicePayStyle() {
        return assetServicePayStyle;
    }

    public void setAssetServicePayStyle(String assetServicePayStyle) {
        this.assetServicePayStyle = assetServicePayStyle;
    }

    public String getAssetFinanceCost() {
        return assetFinanceCost;
    }

    public void setAssetFinanceCost(String assetFinanceCost) {
        this.assetFinanceCost = assetFinanceCost;
    }

    public String getAssetInvestRate() {
        return assetInvestRate;
    }

    public void setAssetInvestRate(String assetInvestRate) {
        this.assetInvestRate = assetInvestRate;
    }

    public String getAssetServiceRate() {
        return assetServiceRate;
    }

    public void setAssetServiceRate(String assetServiceRate) {
        this.assetServiceRate = assetServiceRate;
    }

    public String getAssetBorrowTimeLimit() {
        return assetBorrowTimeLimit;
    }

    public void setAssetBorrowTimeLimit(String assetBorrowTimeLimit) {
        this.assetBorrowTimeLimit = assetBorrowTimeLimit;
    }

    public String getAssetBorrowTimeType() {
        return assetBorrowTimeType;
    }

    public void setAssetBorrowTimeType(String assetBorrowTimeType) {
        this.assetBorrowTimeType = assetBorrowTimeType;
    }

    public String getAssetBorrowCreateTime() {
        return assetBorrowCreateTime;
    }

    public void setAssetBorrowCreateTime(String assetBorrowCreateTime) {
        this.assetBorrowCreateTime = assetBorrowCreateTime;
    }

    public String getAssetBorrowApplication() {
        return assetBorrowApplication;
    }

    public void setAssetBorrowApplication(String assetBorrowApplication) {
        this.assetBorrowApplication = assetBorrowApplication;
    }

    public String getAssetRepaymentStyle() {
        return assetRepaymentStyle;
    }

    public void setAssetRepaymentStyle(String assetRepaymentStyle) {
        this.assetRepaymentStyle = assetRepaymentStyle;
    }

    public String getAssetPrepayment() {
        return assetPrepayment;
    }

    public void setAssetPrepayment(String assetPrepayment) {
        this.assetPrepayment = assetPrepayment;
    }

    public String getAssetPrepaymentTimeLimit() {
        return assetPrepaymentTimeLimit;
    }

    public void setAssetPrepaymentTimeLimit(String assetPrepaymentTimeLimit) {
        this.assetPrepaymentTimeLimit = assetPrepaymentTimeLimit;
    }

    public String getAssetPrepaymentTimeType() {
        return assetPrepaymentTimeType;
    }

    public void setAssetPrepaymentTimeType(String assetPrepaymentTimeType) {
        this.assetPrepaymentTimeType = assetPrepaymentTimeType;
    }

    public String getAssetPrepaymentMinDays() {
        return assetPrepaymentMinDays;
    }

    public void setAssetPrepaymentMinDays(String assetPrepaymentMinDays) {
        this.assetPrepaymentMinDays = assetPrepaymentMinDays;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAssetCustomerType() {
        return assetCustomerType;
    }

    public void setAssetCustomerType(String assetCustomerType) {
        this.assetCustomerType = assetCustomerType;
    }

    public String getAssetCustomerName() {
        return assetCustomerName;
    }

    public void setAssetCustomerName(String assetCustomerName) {
        this.assetCustomerName = assetCustomerName;
    }

    public String getAssetCustomerIndustry() {
        return assetCustomerIndustry;
    }

    public void setAssetCustomerIndustry(String assetCustomerIndustry) {
        this.assetCustomerIndustry = assetCustomerIndustry;
    }

    public String getAssetPersonIdcard() {
        return assetPersonIdcard;
    }

    public void setAssetPersonIdcard(String assetPersonIdcard) {
        this.assetPersonIdcard = assetPersonIdcard;
    }

    public String getAssetPersonMobilePhone() {
        return assetPersonMobilePhone;
    }

    public void setAssetPersonMobilePhone(String assetPersonMobilePhone) {
        this.assetPersonMobilePhone = assetPersonMobilePhone;
    }

    public String getAssetPersonJobNature() {
        return assetPersonJobNature;
    }

    public void setAssetPersonJobNature(String assetPersonJobNature) {
        this.assetPersonJobNature = assetPersonJobNature;
    }

    public String getAssetPersonAddress() {
        return assetPersonAddress;
    }

    public void setAssetPersonAddress(String assetPersonAddress) {
        this.assetPersonAddress = assetPersonAddress;
    }

    public String getByxBankId() {
        return byxBankId;
    }

    public void setByxBankId(String byxBankId) {
        this.byxBankId = byxBankId;
    }

    public String getAssetBankName() {
        return assetBankName;
    }

    public void setAssetBankName(String assetBankName) {
        this.assetBankName = assetBankName;
    }

    public String getAssetBankCnapsCode() {
        return assetBankCnapsCode;
    }

    public void setAssetBankCnapsCode(String assetBankCnapsCode) {
        this.assetBankCnapsCode = assetBankCnapsCode;
    }

    public String getAssetBankNo() {
        return assetBankNo;
    }

    public void setAssetBankNo(String assetBankNo) {
        this.assetBankNo = assetBankNo;
    }

    public String getAssetBankAddress() {
        return assetBankAddress;
    }

    public void setAssetBankAddress(String assetBankAddress) {
        this.assetBankAddress = assetBankAddress;
    }

    public String getAssetLoanCard() {
        return assetLoanCard;
    }

    public void setAssetLoanCard(String assetLoanCard) {
        this.assetLoanCard = assetLoanCard;
    }

    public String getAssetGuarantorStyle() {
        return assetGuarantorStyle;
    }

    public void setAssetGuarantorStyle(String assetGuarantorStyle) {
        this.assetGuarantorStyle = assetGuarantorStyle;
    }

    public String getAssetGuarantorName() {
        return assetGuarantorName;
    }

    public void setAssetGuarantorName(String assetGuarantorName) {
        this.assetGuarantorName = assetGuarantorName;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getServiceContractId() {
        return serviceContractId;
    }

    public void setServiceContractId(String serviceContractId) {
        this.serviceContractId = serviceContractId;
    }

    public String getBondsContractId() {
        return bondsContractId;
    }

    public void setBondsContractId(String bondsContractId) {
        this.bondsContractId = bondsContractId;
    }

    public String getThirdAssetOrderNum() {
        return thirdAssetOrderNum;
    }

    public void setThirdAssetOrderNum(String thirdAssetOrderNum) {
        this.thirdAssetOrderNum = thirdAssetOrderNum;
    }

    public String getAssetBankType() {
        return assetBankType;
    }

    public void setAssetBankType(String assetBankType) {
        this.assetBankType = assetBankType;
    }

    public String getAssetBankUserName() {
        return assetBankUserName;
    }

    public void setAssetBankUserName(String assetBankUserName) {
        this.assetBankUserName = assetBankUserName;
    }
}
