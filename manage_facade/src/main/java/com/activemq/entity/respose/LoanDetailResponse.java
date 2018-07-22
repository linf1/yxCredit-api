package com.activemq.entity.respose;

/**
 * 放款信息返回实体
 * @author 陈淸玉 create by 2018-07-09
 */
public class LoanDetailResponse {

    /**
     * string	友信贷主键ID
     */
    private String businessId;
    /**
     * string	项目虚拟户账号
     */
    private String loanNo;
    /**
     * String	项目虚拟户户名
     */
    private String loanName;
    /**
     * 	String	放款状态 0：失败，1：成功
     */
    private String status;
    /**
     * String	放款描述
     */
    private String statusMsg;

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getLoanNo() {
        return loanNo;
    }

    public void setLoanNo(String loanNo) {
        this.loanNo = loanNo;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public boolean isOk(){
        return "1".equals(this.status);
    }

    @Override
    public String toString() {
        return "LoanDetailResponse{" +
                "businessId='" + businessId + '\'' +
                ", loanNo='" + loanNo + '\'' +
                ", loanName='" + loanName + '\'' +
                ", status='" + status + '\'' +
                ", statusMsg='" + statusMsg + '\'' +
                '}';
    }
}
