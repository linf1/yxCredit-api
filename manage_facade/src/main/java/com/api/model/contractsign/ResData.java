package com.api.model.contractsign;

public class ResData {
    private int status;
    private String msg;
    private String orderNo;
    private byte[] signedStream;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public byte[] getSignedStream() {
        return signedStream;
    }

    public void setSignedStream(byte[] signedStream) {
        this.signedStream = signedStream;
    }
}
