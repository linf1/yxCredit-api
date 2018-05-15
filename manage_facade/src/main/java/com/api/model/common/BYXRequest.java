package com.api.model.common;

import com.alibaba.fastjson.JSONObject;
import com.api.model.BYXSettings;
import com.zhiwang.zwfinance.app.jiguang.util.api.CryptoTools;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 * 碧友信请求实体
 * @author 陈清玉
 */
public class BYXRequest {
    private Long requestTime;
    private String data;

    public Long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Long requestTime) {
        this.requestTime = requestTime;
    }

    public String getData() {

        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    /**
     * 返回json对象参数
     * @param param 返回参数
     * @param byxSettings 碧友信配置
     * @return 请求参数json字符串
     * @throws Exception 加密异常
     */
    public static String getBYXRequest(Object param,BYXSettings byxSettings) throws Exception {
       CryptoTools cryptoTools = new CryptoTools(byxSettings.getDesKey(),byxSettings.getVi());
        //参数转化JSON
        final String paramJson = JSONObject.toJSONString(param);
        //碧友信参数加密
        final String encodesStr = cryptoTools.encode(paramJson);
        BYXRequest byxRequest  = new BYXRequest();
        byxRequest.setData(encodesStr);
        byxRequest.setRequestTime(System.currentTimeMillis());
        return JSONObject.toJSONString(byxRequest);
    }



}
