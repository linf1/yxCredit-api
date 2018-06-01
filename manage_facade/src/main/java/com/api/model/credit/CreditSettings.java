package com.api.model.credit;


import com.api.model.common.BaseSettings;

/**
 * 征信配置信息
 * @author luochaofang
 */
public class CreditSettings extends BaseSettings {
    /**
     * 获取个人征信 的url
     */
    private String creditUrl ;

    /**
     * 个人征信回调 的url
     */
    private String callbackUrl;

    /**
     * 个人征信urlPath
     */
    private String creditUrlPath;

    /**
     * 合作方标识  必填，权限校验用
     */
    private String appKey ;

    /**
     * 合作方标识  必填，权限校验用
     */
    private String ak ;

    /**
     * 合作方标识  必填，权限校验用
     */
    private String sk;

    /**
     *任务类型 必填，权限校验用
     */
    private String taskType;

    private Long effectiveDay;

    public String getRequestUrl() {
        return  creditUrl + creditUrlPath + "?appKey="+ appKey + "&taskType="+ taskType;
    }

    public String getUrl(){
        return creditUrl + creditUrlPath + "?appKey="+ appKey + "&taskType="+ taskType;
    }

    public String getCreditUrlPath() {
        return creditUrlPath;
    }

    public void setCreditUrlPath(String creditUrlPath) {
        this.creditUrlPath = creditUrlPath;
    }

    public String getAk() {
        return ak;
    }

    public void setAk(String ak) {
        this.ak = ak;
    }

    public String getSk() {
        return sk;
    }

    public void setSk(String sk) {
        this.sk = sk;
    }

    public void setCreditUrl(String creditUrl) {
        this.creditUrl = creditUrl;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }
}
