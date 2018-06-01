package com.api.model.tongdun;


import com.api.model.common.ApiCommonRequest;

/**
 * 同盾请求类
 * @author 陈清玉
 */
public class TongDunRequest extends ApiCommonRequest {
    private String  orderId;
    private String  custId;
    /**
     * 报告Id
     */
    private String reportId;

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    @Override
    public String toString() {
        return "TongDunRequest{" +
                "orderId='" + orderId + '\'' +
                '}';
    }
}
