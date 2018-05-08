package com.zw.miaofuspd.facade.entity;

/**
 * Created by Administrator on 2018/1/5 0005.
 */
public class TrxCode {
    private String borrowType;//借款类型 0.未知1.个人信贷2.个人抵押3.企业信贷4.企业抵押
    private String borrowState ;//借款状态 0.未知1.拒贷2.批贷已放款3.批贷未放款4.借款人放弃申请5.审核中6.待放款
    private String borrowAmount;//借款金额
    private String contractDate;//合同日期
    private String loanPeriod;//批贷期数
    private String repayState;//还款状态 0.未知1.正常2.M1 3.M2 4.M3 5.M4 6.M5 7.M6 8.M6+
    private String arrearsAmount; //欠款金额
    private String companyCode; //公司代码

    public String getBorrowType() {
        return borrowType;
    }

    public void setBorrowType(String borrowType) {
        this.borrowType = borrowType;
    }

    public String getBorrowState() {
        return borrowState;
    }

    public void setBorrowState(String borrowState) {
        this.borrowState = borrowState;
    }

    public String getBorrowAmount() {
        return borrowAmount;
    }

    public void setBorrowAmount(String borrowAmount) {
        this.borrowAmount = borrowAmount;
    }

    public String getContractDate() {
        return contractDate;
    }

    public void setContractDate(String contractDate) {
        this.contractDate = contractDate;
    }

    public String getLoanPeriod() {
        return loanPeriod;
    }

    public void setLoanPeriod(String loanPeriod) {
        this.loanPeriod = loanPeriod;
    }

    public String getRepayState() {
        return repayState;
    }

    public void setRepayState(String repayState) {
        this.repayState = repayState;
    }

    public String getArrearsAmount() {
        return arrearsAmount;
    }

    public void setArrearsAmount(String arrearsAmount) {
        this.arrearsAmount = arrearsAmount;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }
}
