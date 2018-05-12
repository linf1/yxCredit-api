package com.api.model.bankcard;

import com.api.model.common.BaseSettings;

/**
 * 银行卡四要素配置实体
 * @author 陈清玉
 */
public class BankcardSettings extends BaseSettings {
    private String  merchantNumber;

    public String getMerchantNumber() {
        return merchantNumber;
    }

    public void setMerchantNumber(String merchantNumber) {
        this.merchantNumber = merchantNumber;
    }
}
