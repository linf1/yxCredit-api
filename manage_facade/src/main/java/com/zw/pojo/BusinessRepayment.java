package com.zw.pojo;

import com.zw.base.BaseEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BusinessRepayment extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -3844027227737473317L;
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column business_repayment.id
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column business_repayment.order_id
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    private String orderId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column business_repayment.repayment_id
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    private String repaymentId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column business_repayment.status
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    private String status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column business_repayment.period
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    private Integer period;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column business_repayment.interest_start_time
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    private Date interestStartTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column business_repayment.repayment_time
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    private Date repaymentTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column business_repayment.repayment_yes_time
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    private Date repaymentYesTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column business_repayment.repayment_account
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    private BigDecimal repaymentAccount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column business_repayment.repayment_yes_account
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    private BigDecimal repaymentYesAccount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column business_repayment.capital
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    private BigDecimal capital;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column business_repayment.yes_capital
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    private BigDecimal yesCapital;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column business_repayment.rate
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    private BigDecimal rate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column business_repayment.interest
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    private BigDecimal interest;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column business_repayment.repayment_yes_interest
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    private BigDecimal repaymentYesInterest;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column business_repayment.is_repayment
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    private Integer isRepayment;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column business_repayment.repayment_type
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    private Integer repaymentType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column business_repayment.late_days
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    private Integer lateDays;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column business_repayment.late_rate
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    private BigDecimal lateRate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column business_repayment.late_interest
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    private BigDecimal lateInterest;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column business_repayment.derate_amount
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    private BigDecimal derateAmount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column business_repayment.periods
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    private Integer periods;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column business_repayment.channel_type
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    private String channelType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column business_repayment.remark
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    private String remark;



    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column business_repayment.id
     *
     * @return the value of business_repayment.id
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column business_repayment.id
     *
     * @param id the value for business_repayment.id
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column business_repayment.order_id
     *
     * @return the value of business_repayment.order_id
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column business_repayment.order_id
     *
     * @param orderId the value for business_repayment.order_id
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column business_repayment.repayment_id
     *
     * @return the value of business_repayment.repayment_id
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public String getRepaymentId() {
        return repaymentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column business_repayment.repayment_id
     *
     * @param repaymentId the value for business_repayment.repayment_id
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public void setRepaymentId(String repaymentId) {
        this.repaymentId = repaymentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column business_repayment.status
     *
     * @return the value of business_repayment.status
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column business_repayment.status
     *
     * @param status the value for business_repayment.status
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column business_repayment.period
     *
     * @return the value of business_repayment.period
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public Integer getPeriod() {
        return period;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column business_repayment.period
     *
     * @param period the value for business_repayment.period
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public void setPeriod(Integer period) {
        this.period = period;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column business_repayment.interest_start_time
     *
     * @return the value of business_repayment.interest_start_time
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public Date getInterestStartTime() {
        return interestStartTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column business_repayment.interest_start_time
     *
     * @param interestStartTime the value for business_repayment.interest_start_time
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public void setInterestStartTime(Date interestStartTime) {
        this.interestStartTime = interestStartTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column business_repayment.repayment_time
     *
     * @return the value of business_repayment.repayment_time
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public Date getRepaymentTime() {
        return repaymentTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column business_repayment.repayment_time
     *
     * @param repaymentTime the value for business_repayment.repayment_time
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public void setRepaymentTime(Date repaymentTime) {
        this.repaymentTime = repaymentTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column business_repayment.repayment_yes_time
     *
     * @return the value of business_repayment.repayment_yes_time
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public Date getRepaymentYesTime() {
        return repaymentYesTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column business_repayment.repayment_yes_time
     *
     * @param repaymentYesTime the value for business_repayment.repayment_yes_time
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public void setRepaymentYesTime(Date repaymentYesTime) {
        this.repaymentYesTime = repaymentYesTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column business_repayment.repayment_account
     *
     * @return the value of business_repayment.repayment_account
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public BigDecimal getRepaymentAccount() {
        return repaymentAccount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column business_repayment.repayment_account
     *
     * @param repaymentAccount the value for business_repayment.repayment_account
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public void setRepaymentAccount(BigDecimal repaymentAccount) {
        this.repaymentAccount = repaymentAccount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column business_repayment.repayment_yes_account
     *
     * @return the value of business_repayment.repayment_yes_account
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public BigDecimal getRepaymentYesAccount() {
        return repaymentYesAccount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column business_repayment.repayment_yes_account
     *
     * @param repaymentYesAccount the value for business_repayment.repayment_yes_account
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public void setRepaymentYesAccount(BigDecimal repaymentYesAccount) {
        this.repaymentYesAccount = repaymentYesAccount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column business_repayment.capital
     *
     * @return the value of business_repayment.capital
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public BigDecimal getCapital() {
        return capital;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column business_repayment.capital
     *
     * @param capital the value for business_repayment.capital
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public void setCapital(BigDecimal capital) {
        this.capital = capital;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column business_repayment.yes_capital
     *
     * @return the value of business_repayment.yes_capital
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public BigDecimal getYesCapital() {
        return yesCapital;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column business_repayment.yes_capital
     *
     * @param yesCapital the value for business_repayment.yes_capital
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public void setYesCapital(BigDecimal yesCapital) {
        this.yesCapital = yesCapital;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column business_repayment.rate
     *
     * @return the value of business_repayment.rate
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public BigDecimal getRate() {
        return rate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column business_repayment.rate
     *
     * @param rate the value for business_repayment.rate
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column business_repayment.interest
     *
     * @return the value of business_repayment.interest
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public BigDecimal getInterest() {
        return interest;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column business_repayment.interest
     *
     * @param interest the value for business_repayment.interest
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column business_repayment.repayment_yes_interest
     *
     * @return the value of business_repayment.repayment_yes_interest
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public BigDecimal getRepaymentYesInterest() {
        return repaymentYesInterest;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column business_repayment.repayment_yes_interest
     *
     * @param repaymentYesInterest the value for business_repayment.repayment_yes_interest
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public void setRepaymentYesInterest(BigDecimal repaymentYesInterest) {
        this.repaymentYesInterest = repaymentYesInterest;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column business_repayment.is_repayment
     *
     * @return the value of business_repayment.is_repayment
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public Integer getIsRepayment() {
        return isRepayment;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column business_repayment.is_repayment
     *
     * @param isRepayment the value for business_repayment.is_repayment
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public void setIsRepayment(Integer isRepayment) {
        this.isRepayment = isRepayment;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column business_repayment.repayment_type
     *
     * @return the value of business_repayment.repayment_type
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public Integer getRepaymentType() {
        return repaymentType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column business_repayment.repayment_type
     *
     * @param repaymentType the value for business_repayment.repayment_type
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public void setRepaymentType(Integer repaymentType) {
        this.repaymentType = repaymentType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column business_repayment.late_days
     *
     * @return the value of business_repayment.late_days
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public Integer getLateDays() {
        return lateDays;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column business_repayment.late_days
     *
     * @param lateDays the value for business_repayment.late_days
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public void setLateDays(Integer lateDays) {
        this.lateDays = lateDays;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column business_repayment.late_rate
     *
     * @return the value of business_repayment.late_rate
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public BigDecimal getLateRate() {
        return lateRate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column business_repayment.late_rate
     *
     * @param lateRate the value for business_repayment.late_rate
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public void setLateRate(BigDecimal lateRate) {
        this.lateRate = lateRate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column business_repayment.late_interest
     *
     * @return the value of business_repayment.late_interest
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public BigDecimal getLateInterest() {
        return lateInterest;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column business_repayment.late_interest
     *
     * @param lateInterest the value for business_repayment.late_interest
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public void setLateInterest(BigDecimal lateInterest) {
        this.lateInterest = lateInterest;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column business_repayment.derate_amount
     *
     * @return the value of business_repayment.derate_amount
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public BigDecimal getDerateAmount() {
        return derateAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column business_repayment.derate_amount
     *
     * @param derateAmount the value for business_repayment.derate_amount
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public void setDerateAmount(BigDecimal derateAmount) {
        this.derateAmount = derateAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column business_repayment.periods
     *
     * @return the value of business_repayment.periods
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public Integer getPeriods() {
        return periods;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column business_repayment.periods
     *
     * @param periods the value for business_repayment.periods
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public void setPeriods(Integer periods) {
        this.periods = periods;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column business_repayment.channel_type
     *
     * @return the value of business_repayment.channel_type
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public String getChannelType() {
        return channelType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column business_repayment.channel_type
     *
     * @param channelType the value for business_repayment.channel_type
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column business_repayment.remark
     *
     * @return the value of business_repayment.remark
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column business_repayment.remark
     *
     * @param remark the value for business_repayment.remark
     *
     * @mbggenerated Fri Jul 20 14:24:23 CST 2018
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }


}