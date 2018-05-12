package com.api.model;

import com.base.util.MD5Utils;

import javax.print.attribute.HashAttributeSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 碧友信权限配置项
 * @author 陈清玉
 */
public class BYXSettings {

    private String  appKey;
    private Long ts = System.currentTimeMillis();
    private String  signa;
    private String vi;
    private String appSecrect;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public String getSigna() {
        return signa();
    }

    public void setSigna(String signa) {
        this.signa = signa;
    }

    public String getAppSecrect() {
        return appSecrect;
    }

    public void setAppSecrect(String appSecrect) {
        this.appSecrect = appSecrect;
    }

    public String getVi() {
        return vi;
    }

    public void setVi(String vi) {
        this.vi = vi;
    }

    public String key(){
       return MD5Utils.sign (getAppSecrect() ,getTs().toString(), "utf-8");
    }

    public String signa(){
        return MD5Utils.sign (key() ,getAppKey(), "utf-8").toUpperCase();
    }

    /**
     * 获取碧友信请求头
     * @return 请求头map
     */
    public  Map<String,Object> getHeadRequest(){
        Map<String,Object> headRequestMap = new HashMap<>();
        headRequestMap.put("appKey",getAppKey());
        headRequestMap.put("ts",System.currentTimeMillis());
        headRequestMap.put("signa",signa);
        return headRequestMap;
    }
}
