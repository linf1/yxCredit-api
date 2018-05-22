package com.api.model.tongdun;


import com.api.model.common.ApiCommonRequest;

/**
 * 同盾请求类
 * @author 陈清玉
 */
public class TongDunRequest extends ApiCommonRequest {
    private String  orderId;
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "TongDunRequest{" +
                "orderId='" + orderId + '\'' +
                '}';
    }
}
