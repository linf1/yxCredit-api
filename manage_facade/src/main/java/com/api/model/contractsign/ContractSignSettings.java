package com.api.model.contractsign;

import com.api.model.common.BaseSettings;

public class ContractSignSettings extends BaseSettings {
    private static final long serialVersionUID = 960900377369027460L;
    /**
     * 合作方标识  必填，权限校验用
     */
    private String appKey;
    /**
     * 合作方标识  必填，权限校验用
     */
    private String appSecret;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
}
