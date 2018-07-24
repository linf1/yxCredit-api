package com.activemq.entity.respose;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 还款计划接口请求实体
 * @author 陈淸玉 create 2018-07-10
 */
public class RepaymentResponse implements Serializable {
    /**
     * 还款主键
     */
    private String repaymentId;
    /**
     * 还款状态1、还款中、2还款处理中、3已还款
     */
    private String status;
    /**
     * 期数
     */
    private Integer period;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 订单编号（订单编号，唯一性关联）
     */
    private String orderNo;
    /**
     * 起息日
     */
    private Date interestStartTime;
    /**
     * 预计还款时间
     */
    private Date repaymentTime;
    /**
     * 实际还款时间
     */
    private Date repaymentYesTime;
    /**
     * 预还金额
     */
    private BigDecimal repaymentAccount;
    /**
     * 已还金额
     */
    private BigDecimal repaymentYesAccount;
    /**
     * 本金
     */
    private BigDecimal capital;
    /**
     * 已还本金
     */
    private BigDecimal yesCapital;
    /**
     * 利率
     */
    private BigDecimal rate;
    /**
     * 利息
     */
    private BigDecimal interest;
    /**
     * 实际还款利息
     */
    private BigDecimal repaymentYesInterest;
    /**
     * 是否提前还款，0否，1是
     */
    private Integer isRepayment;
    /**
     * 还款类型，0未还款,1 正常还款; 2 提前还款;3 部分提前还款;4逾期还款，5逾期未还;
     */
    private Integer repaymentType;
    /**
     * 还款主键
     */
    private String addIp;
    /**
     * 创建时间
     */
    private Integer repaymentUserId;
    /**
     * 逾期天数
     */
    private Integer lateDays;
    /**
     * 逾期利率
     */
    private BigDecimal lateRate;
    /**
     * 逾期利息
     */
    private BigDecimal lateInterest;
    /**
     * 减免金额
     */
    private BigDecimal derateAmount;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 备注
     */
    private String remark;
    /**
     * 还款来源
     */
    private String channelType;

    public String getRepaymentId() {
        return repaymentId;
    }

    public void setRepaymentId(String repaymentId) {
        this.repaymentId = repaymentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Date getInterestStartTime() {
        return interestStartTime;
    }

    public void setInterestStartTime(Date interestStartTime) {
        this.interestStartTime = interestStartTime;
    }

    public Date getRepaymentTime() {
        return repaymentTime;
    }

    public void setRepaymentTime(Date repaymentTime) {
        this.repaymentTime = repaymentTime;
    }

    public Date getRepaymentYesTime() {
        return repaymentYesTime;
    }

    public void setRepaymentYesTime(Date repaymentYesTime) {
        this.repaymentYesTime = repaymentYesTime;
    }

    public BigDecimal getRepaymentAccount() {
        return repaymentAccount;
    }

    public void setRepaymentAccount(BigDecimal repaymentAccount) {
        this.repaymentAccount = repaymentAccount;
    }

    public BigDecimal getRepaymentYesAccount() {
        return repaymentYesAccount;
    }

    public void setRepaymentYesAccount(BigDecimal repaymentYesAccount) {
        this.repaymentYesAccount = repaymentYesAccount;
    }

    public BigDecimal getCapital() {
        return capital;
    }

    public void setCapital(BigDecimal capital) {
        this.capital = capital;
    }

    public BigDecimal getYesCapital() {
        return yesCapital;
    }

    public void setYesCapital(BigDecimal yesCapital) {
        this.yesCapital = yesCapital;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public BigDecimal getRepaymentYesInterest() {
        return repaymentYesInterest;
    }

    public void setRepaymentYesInterest(BigDecimal repaymentYesInterest) {
        this.repaymentYesInterest = repaymentYesInterest;
    }

    public Integer getIsRepayment() {
        return isRepayment;
    }

    public void setIsRepayment(Integer isRepayment) {
        this.isRepayment = isRepayment;
    }

    public Integer getRepaymentType() {
        return repaymentType;
    }

    public void setRepaymentType(Integer repaymentType) {
        this.repaymentType = repaymentType;
    }

    public String getAddIp() {
        return addIp;
    }

    public void setAddIp(String addIp) {
        this.addIp = addIp;
    }

    public Integer getRepaymentUserId() {
        return repaymentUserId;
    }

    public void setRepaymentUserId(Integer repaymentUserId) {
        this.repaymentUserId = repaymentUserId;
    }

    public Integer getLateDays() {
        return lateDays;
    }

    public void setLateDays(Integer lateDays) {
        this.lateDays = lateDays;
    }

    public BigDecimal getLateRate() {
        return lateRate;
    }

    public void setLateRate(BigDecimal lateRate) {
        this.lateRate = lateRate;
    }

    public BigDecimal getLateInterest() {
        return lateInterest;
    }

    public void setLateInterest(BigDecimal lateInterest) {
        this.lateInterest = lateInterest;
    }

    public BigDecimal getDerateAmount() {
        return derateAmount;
    }

    public void setDerateAmount(BigDecimal derateAmount) {
        this.derateAmount = derateAmount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    @Override
    public String toString() {
        return "DSRepaymentRequest{" +
                "repaymentId='" + repaymentId + '\'' +
                ", status='" + status + '\'' +
                ", period=" + period +
                ", userId=" + userId +
                ", orderNo='" + orderNo + '\'' +
                ", interestStartTime=" + interestStartTime +
                ", repaymentTime=" + repaymentTime +
                ", repaymentYesTime=" + repaymentYesTime +
                ", repaymentAccount=" + repaymentAccount +
                ", repaymentYesAccount=" + repaymentYesAccount +
                ", capital=" + capital +
                ", yesCapital=" + yesCapital +
                ", rate=" + rate +
                ", interest=" + interest +
                ", repaymentYesInterest=" + repaymentYesInterest +
                ", isRepayment=" + isRepayment +
                ", repaymentType=" + repaymentType +
                ", addIp='" + addIp + '\'' +
                ", repaymentUserId=" + repaymentUserId +
                ", lateDays=" + lateDays +
                ", lateRate=" + lateRate +
                ", lateInterest=" + lateInterest +
                ", derateAmount=" + derateAmount +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", remark='" + remark + '\'' +
                ", channelType='" + channelType + '\'' +
                '}';
    }
}
