package com.zw.miaofuspd.facade.log.entity;

import java.io.Serializable;

/**
 * 订单日志表
 * mag_order_log
 */


public class OrderLogBean implements Serializable {
	private String id="";
	private String orderNo="";//订单编号
	private String userId="";//用户ID
	private String periods="";//申请期限/期数
	private String merchantId="";//商户ID
	private String merchantName="";//商户ID
	private String merchandiseId="";//商品ID
	private String merchandiseType="";//商品类型
	private String merchandiseName="";//商品名称
	private String customerId="";//'客户id
	private String customerName="";//客户名
	private String sexName="";//客户性别
	private String tel="";//客户联系电话
	private String cardType="";//证件类型
	private String card="";//证件号码
	private String credit="";//授信额度
	private String precredit="";//预授信额度
	private String amount="";//商品金额
	private String company="";//归属公司
	private String branch="";//归属部门
	private String empId="";//员工ID
	private String manager="";//归属人
	private String creatTime="";//创建时间
	private String alterTime="";//修改时间
	private String state="";//'0未提交;1借款申请;2自动化审批通过;3自动化审批拒绝;4自动化审批异常转人工;5人工审批通过;6人工审批拒绝;7合同确认;8放款成功;9结清',
	private String loanTime="";//放款时间
	private String fee="";//利息
	private String repayType="";//'还款方式'
	private String rate="";//'利率（年%,月%）'
	private String merchandiseBrand="";//商品品牌
	private String merchandiseModel="";//'商品型号'
	private String merchandiseCode="";//''商品代码''
	private String provincesId="";//省的id
	private String cityId="";//市的id
	private String districId="";//区的id
	private String provinces="";//省
	private String city="";//市
	private String distric="";//区
	private String productKind="";//产品的类型（1单期，2多期）
	private String productType="";//'产品系列编号
	private String productTypeName="";//'产品系列编号
	private String productName="";//''产品名称编号'
	private String productNameName="";//'''产品名称名称''
	private String productDetail="";//'产品期数编号'
	private String productDetailName="";//'产品期数名称'
	private String productDetailCode="";//'第三级产品code'
	private String contractAmount="";//合同总价
	private String loanAmount="";//放款金额
	private String sex="";
	private String merchandiseBrandId="";
	private String merchandiseTypeId="";
	private String empNumber="";//'员工工号'
	private String repayMoney="";//'还款金额 '
	private String orderType="";//'现金分期1，商品分期是2 '
	private String applayMoney="";//'申请金额'
	private String predictPrice="";//'首付金额'
	private String receiveId="";//'领取人id'
	private String scoreCard="";//评分卡json字符串'
	private String isRefuse="";//评分卡json字符串'
	private String commodityState="";//商品订单状态(这个状态仅用于商品贷11 生成二维码，12 完善资料，13补充影像，14手签，15 提交)
	private String withdrawState="";//提现状态，(0,未提现，1提现)
	private String orderSubmissionTime="";//进入总部审核时间
	private String source="";//订单来源：(1,微信)，(2app)
	private String contract_no="";//'合同编号'
	private String repayDate="";//还款日期
	private String overdueState="";//0,逾期超2天；1,正常结清放款时间不超过30天；2,正常结清放款时间超过30天；3,上笔订单不存在'
	private String manageFee="";//管理费
	private String quickTrialFee="";//快审费
	private String feeState="";//费用状态(0,代扣，1已扣，2扣款中，3扣款失败)
	private String merchandiseVersion="";//商品版本
	private String requestno="";//宝付费用扣款批次号
	private String isSign="";//(0,未标记，1标记)
	private String weight="";//黄金重量
	private String price="";//黄金单价
	private String identryRuleState="";//身份风控引擎状态281(0,未通过，1通过)
	private String personRuleState="";//个人信息同盾规则状态282(0.未通过，1已通过)
	private String orderId="";//订单id
	private String tache="";//操作环节
	private String changeValue="";//操作具体描述(例如审批通过)
	private String operatorId="";//操作人id 客户id或后台用户id
	private String operatorName="";//操作人
	private String operatorType="";//操作人类型0：客户自己；1：后台用户
	private String creatTimeLog="";//创建时间
	private String messageId = "";//

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPeriods() {
		return periods;
	}

	public void setPeriods(String periods) {
		this.periods = periods;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getMerchandiseId() {
		return merchandiseId;
	}

	public void setMerchandiseId(String merchandiseId) {
		this.merchandiseId = merchandiseId;
	}

	public String getMerchandiseType() {
		return merchandiseType;
	}

	public void setMerchandiseType(String merchandiseType) {
		this.merchandiseType = merchandiseType;
	}

	public String getMerchandiseName() {
		return merchandiseName;
	}

	public void setMerchandiseName(String merchandiseName) {
		this.merchandiseName = merchandiseName;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getSexName() {
		return sexName;
	}

	public void setSexName(String sexName) {
		this.sexName = sexName;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public String getPrecredit() {
		return precredit;
	}

	public void setPrecredit(String precredit) {
		this.precredit = precredit;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}

	public String getAlterTime() {
		return alterTime;
	}

	public void setAlterTime(String alterTime) {
		this.alterTime = alterTime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getLoanTime() {
		return loanTime;
	}

	public void setLoanTime(String loanTime) {
		this.loanTime = loanTime;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getRepayType() {
		return repayType;
	}

	public void setRepayType(String repayType) {
		this.repayType = repayType;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getMerchandiseBrand() {
		return merchandiseBrand;
	}

	public void setMerchandiseBrand(String merchandiseBrand) {
		this.merchandiseBrand = merchandiseBrand;
	}

	public String getMerchandiseModel() {
		return merchandiseModel;
	}

	public void setMerchandiseModel(String merchandiseModel) {
		this.merchandiseModel = merchandiseModel;
	}

	public String getMerchandiseCode() {
		return merchandiseCode;
	}

	public void setMerchandiseCode(String merchandiseCode) {
		this.merchandiseCode = merchandiseCode;
	}

	public String getProvincesId() {
		return provincesId;
	}

	public void setProvincesId(String provincesId) {
		this.provincesId = provincesId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getDistricId() {
		return districId;
	}

	public void setDistricId(String districId) {
		this.districId = districId;
	}

	public String getProvinces() {
		return provinces;
	}

	public void setProvinces(String provinces) {
		this.provinces = provinces;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistric() {
		return distric;
	}

	public void setDistric(String distric) {
		this.distric = distric;
	}

	public String getProductKind() {
		return productKind;
	}

	public void setProductKind(String productKind) {
		this.productKind = productKind;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getProductTypeName() {
		return productTypeName;
	}

	public void setProductTypeName(String productTypeName) {
		this.productTypeName = productTypeName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductNameName() {
		return productNameName;
	}

	public void setProductNameName(String productNameName) {
		this.productNameName = productNameName;
	}

	public String getProductDetail() {
		return productDetail;
	}

	public void setProductDetail(String productDetail) {
		this.productDetail = productDetail;
	}

	public String getProductDetailName() {
		return productDetailName;
	}

	public void setProductDetailName(String productDetailName) {
		this.productDetailName = productDetailName;
	}

	public String getProductDetailCode() {
		return productDetailCode;
	}

	public void setProductDetailCode(String productDetailCode) {
		this.productDetailCode = productDetailCode;
	}

	public String getContractAmount() {
		return contractAmount;
	}

	public void setContractAmount(String contractAmount) {
		this.contractAmount = contractAmount;
	}

	public String getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(String loanAmount) {
		this.loanAmount = loanAmount;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getMerchandiseBrandId() {
		return merchandiseBrandId;
	}

	public void setMerchandiseBrandId(String merchandiseBrandId) {
		this.merchandiseBrandId = merchandiseBrandId;
	}

	public String getMerchandiseTypeId() {
		return merchandiseTypeId;
	}

	public void setMerchandiseTypeId(String merchandiseTypeId) {
		this.merchandiseTypeId = merchandiseTypeId;
	}

	public String getEmpNumber() {
		return empNumber;
	}

	public void setEmpNumber(String empNumber) {
		this.empNumber = empNumber;
	}

	public String getRepayMoney() {
		return repayMoney;
	}

	public void setRepayMoney(String repayMoney) {
		this.repayMoney = repayMoney;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getApplayMoney() {
		return applayMoney;
	}

	public void setApplayMoney(String applayMoney) {
		this.applayMoney = applayMoney;
	}

	public String getPredictPrice() {
		return predictPrice;
	}

	public void setPredictPrice(String predictPrice) {
		this.predictPrice = predictPrice;
	}

	public String getReceiveId() {
		return receiveId;
	}

	public void setReceiveId(String receiveId) {
		this.receiveId = receiveId;
	}

	public String getScoreCard() {
		return scoreCard;
	}

	public void setScoreCard(String scoreCard) {
		this.scoreCard = scoreCard;
	}

	public String getIsRefuse() {
		return isRefuse;
	}

	public void setIsRefuse(String isRefuse) {
		this.isRefuse = isRefuse;
	}

	public String getCommodityState() {
		return commodityState;
	}

	public void setCommodityState(String commodityState) {
		this.commodityState = commodityState;
	}

	public String getWithdrawState() {
		return withdrawState;
	}

	public void setWithdrawState(String withdrawState) {
		this.withdrawState = withdrawState;
	}

	public String getOrderSubmissionTime() {
		return orderSubmissionTime;
	}

	public void setOrderSubmissionTime(String orderSubmissionTime) {
		this.orderSubmissionTime = orderSubmissionTime;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getContract_no() {
		return contract_no;
	}

	public void setContract_no(String contract_no) {
		this.contract_no = contract_no;
	}

	public String getRepayDate() {
		return repayDate;
	}

	public void setRepayDate(String repayDate) {
		this.repayDate = repayDate;
	}

	public String getOverdueState() {
		return overdueState;
	}

	public void setOverdueState(String overdueState) {
		this.overdueState = overdueState;
	}

	public String getManageFee() {
		return manageFee;
	}

	public void setManageFee(String manageFee) {
		this.manageFee = manageFee;
	}

	public String getQuickTrialFee() {
		return quickTrialFee;
	}

	public void setQuickTrialFee(String quickTrialFee) {
		this.quickTrialFee = quickTrialFee;
	}

	public String getFeeState() {
		return feeState;
	}

	public void setFeeState(String feeState) {
		this.feeState = feeState;
	}

	public String getMerchandiseVersion() {
		return merchandiseVersion;
	}

	public void setMerchandiseVersion(String merchandiseVersion) {
		this.merchandiseVersion = merchandiseVersion;
	}

	public String getRequestno() {
		return requestno;
	}

	public void setRequestno(String requestno) {
		this.requestno = requestno;
	}

	public String getIsSign() {
		return isSign;
	}

	public void setIsSign(String isSign) {
		this.isSign = isSign;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getIdentryRuleState() {
		return identryRuleState;
	}

	public void setIdentryRuleState(String identryRuleState) {
		this.identryRuleState = identryRuleState;
	}

	public String getPersonRuleState() {
		return personRuleState;
	}

	public void setPersonRuleState(String personRuleState) {
		this.personRuleState = personRuleState;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTache() {
		return tache;
	}

	public void setTache(String tache) {
		this.tache = tache;
	}

	public String getChangeValue() {
		return changeValue;
	}

	public void setChangeValue(String changeValue) {
		this.changeValue = changeValue;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}

	public String getCreatTimeLog() {
		return creatTimeLog;
	}

	public void setCreatTimeLog(String creatTimeLog) {
		this.creatTimeLog = creatTimeLog;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
}
