package com.api.model.common;

/**
 * 基础settings
 * @author 陈清玉
 */
public class BaseSettings {

    /**
     * 请求url
     */
    private String requestUrl;

    /**
     * 请求类型
     */
    private String requestType;

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
}
