package com.zw.fraudapi.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import com.alibaba.fastjson.JSON;

public class FraudApiInvoker {

    //private static final String apiUrl = "https://apitest.tongdun.cn/riskService/v1.1";

    private static HttpsURLConnection conn;

    public static String invoke(Map<String, Object> params,String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            // 组织请求参数
            StringBuilder postBody = new StringBuilder();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (entry.getValue() == null) continue;
                postBody.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue().toString(),
                        "utf-8")).append("&");
            }

            if (!params.isEmpty()) {
                postBody.deleteCharAt(postBody.length() - 1);
            }

            SSLSocketFactory ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();
            conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);
            // 设置长链接
            conn.setRequestProperty("Connection", "Keep-Alive");
            // 设置连接超时
            conn.setConnectTimeout(15000);
            // 设置读取超时，建议设置为500ms。若同时调用了信息核验服务，请与客户经理协商确认具体时间
            conn.setReadTimeout(10000);
            // 提交参数
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.getOutputStream().write(postBody.toString().getBytes());
            conn.getOutputStream().flush();
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                System.out.println("[FraudApiInvoker] invoke failed, response status:" + responseCode);
                return null;
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line).append("\n");
            }
            return result.toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void main(String[] args) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("partner_code", "zzlrz");// 此处值填写您的合作方标识
        params.put("secret_key", "82942f94bd294662b28925d4532cf077");// 此处填写对应app密钥
        params.put("event_id", "Login_ios_20170419");// 此处填写策略集上的事件标识
        params.put("people", "{\"contact_name\":\"张三\",\"info\":{\"contacts_mobile\":\"18719210921\",\"id_number\":\"3212200122112211\"}}");// 此处填写系统对象
        String apiUrl = "https://apitest.tongdun.cn/riskService/v1.1";
       // FraudApiResponse apiResp = new FraudApiInvoker().invoke(params,apiUrl);
        /*System.out.println(apiResp.getSuccess());
        System.out.println(apiResp.getReason_code());
        System.out.println(apiResp.getFinal_decision());*/
    }

    public static void heartbeat(){
        final FraudApiInvoker invoker = new FraudApiInvoker();
        final Map<String, Object> params = new HashMap<>();
        params.put("partner_code","zzlrz");
        params.put("secret_key","heartbeat");
        String apiUrl = "https://apitest.tongdun.cn/riskService/v1.1";
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        invoker.invoke(params,apiUrl);
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        }, "FraudApiInvoker Heartbeat Thread").start();
    }
}

