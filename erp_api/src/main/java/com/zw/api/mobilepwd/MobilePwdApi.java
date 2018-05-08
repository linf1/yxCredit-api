package com.zw.api.mobilepwd;

import com.alibaba.fastjson.JSONObject;
import com.base.util.HttpClientUtil;
import com.base.util.TraceLoggerUtil;
import com.zw.api.HttpUtil;
import org.apache.http.client.config.RequestConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * <strong>Title : 手机服务密码验证接口<br>
 * </strong> <strong>Description : </strong>@手机服务密码验证接口@<br>
 * <strong>Create on : 2017年04月27日<br>
 * </strong>
 * <p>
 * <strong>Copyright (C) Vbill Co.,Ltd.<br>
 * </strong>
 * <p>
 *
 * @author department:技术开发部 <br>
 *         username:wucheng <br>
 * @version <strong>zw有限公司-运营平台</strong><br>
 *          <br>
 *          <strong>修改历史:</strong><br>
 *          修改人 修改日期 修改描述<br>
 *          -------------------------------------------<br>
 *          <br>
 *          <br>
 */
public class MobilePwdApi {

    /**
     * 魔蝎接口准备阶段
     * @param d 系统当前时间
     * @param idNo 身份证号
     * @param name 用户姓名
     * @param password 服务密码
     * @param phone 手机号
     * @param sign 通过MD5获取的签字
     * @param userId 用户Id  目前跟对接人沟通可以随便填,这里传递的是登录app的用户ID add by wucheng 20170510
     * @return
     */
    public static String mxPrepare(String host, String d, String idNo, String name, String password, String phone, String sign, String userId){

        StringBuffer sendRequest = new StringBuffer();
        sendRequest.append("{");
        sendRequest.append("\"d\": \"");
        sendRequest.append(d);
        sendRequest.append("\",");
        sendRequest.append("\"idNo\": \"");
        sendRequest.append(idNo);
        sendRequest.append("\",");
        sendRequest.append("\"name\": \"");
        sendRequest.append(name);
        sendRequest.append("\",");
        sendRequest.append("\"password\": \"");
        sendRequest.append(password);
        sendRequest.append("\",");
        sendRequest.append("\"phone\": \"");
        sendRequest.append(phone);
        sendRequest.append("\",");
        sendRequest.append("\"sign\": \"");
        sendRequest.append(sign);
        sendRequest.append("\",");
        sendRequest.append("\"userId\": \"");
        sendRequest.append(userId);
        sendRequest.append("\"");
        sendRequest.append("}");

        String resultData = "";
        String requestURL = host+"/carry/queryCarryTaskId";
        TraceLoggerUtil.info("发送运营商准备阶段接口--->" + sendRequest.toString());
        System.out.println("sendRequest==========>"+sendRequest);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(50000).setConnectTimeout(50000).setConnectionRequestTimeout(50000).build();
        HttpClientUtil.setRequestConfig(requestConfig);
        try {
            resultData = HttpClientUtil.getInstance().sendHttpPost(requestURL,sendRequest.toString());
        }catch (Exception e){
            resultData = "";
        }
        TraceLoggerUtil.info("接收运营商准备阶段接口--->" + resultData);
        return resultData;
    }


    /**
     * 运营商轮询任务状态
     * @param host 接口服务器
     * @param d 时间戳
     * @param phone 账号
     * @param sign 签字
     * @param smsCode 验证码
     * @return
     */
    public static String mxLunxun(String host, String d, String phone, String sign, String smsCode){

        StringBuffer sendRequest = new StringBuffer();
        sendRequest.append("{");
        sendRequest.append("\"d\": \"");
        sendRequest.append(d);
        sendRequest.append("\",");
        sendRequest.append("\"phone\": \"");
        sendRequest.append(phone);
        sendRequest.append("\",");
        sendRequest.append("\"sign\": \"");
        sendRequest.append(sign);
        sendRequest.append("\",");
        sendRequest.append("\"smsCode\": \"");
        sendRequest.append(smsCode);
        sendRequest.append("\"");
        sendRequest.append("}");

        String resultData = "";
        String requestURL = host+"/carry/updateTaskStatus";
        TraceLoggerUtil.info("发送运营商轮询接口--->" + sendRequest.toString());
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).setConnectionRequestTimeout(30000).build();
        HttpClientUtil.setRequestConfig(requestConfig);
        try {
            resultData = HttpClientUtil.getInstance().sendHttpPost(requestURL,sendRequest.toString());
        }catch (Exception e){
            resultData = "";
        }
        TraceLoggerUtil.info("接收运营商轮询接口--->" + resultData);
        return resultData;
    }

    /**
     * 再次获取验证码
     * @param host 接口服务器
     * @param d 时间戳
     * @param phone 账号
     * @param sign 签字
     * @param type 验证码类型  img:图片验证码  sms:短信验证码
     * @return
     */
    public static String reGetCode(String host, String d, String phone, String sign, String type){

        StringBuffer sendRequest = new StringBuffer();
        sendRequest.append("{");
        sendRequest.append("\"d\": \"");
        sendRequest.append(d);
        sendRequest.append("\",");
        sendRequest.append("\"phone\": \"");
        sendRequest.append(phone);
        sendRequest.append("\",");
        sendRequest.append("\"sign\": \"");
        sendRequest.append(sign);
        sendRequest.append("\",");
        sendRequest.append("\"type\": \"");
        sendRequest.append(type);
        sendRequest.append("\"");
        sendRequest.append("}");

        String resultData = "";
        String requestURL = host+"/carry/verifyCode";
        TraceLoggerUtil.info("发送运营商再次获取验证码接口--->" + sendRequest.toString());
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).setConnectionRequestTimeout(30000).build();
        HttpClientUtil.setRequestConfig(requestConfig);
        try {
            resultData = HttpClientUtil.getInstance().sendHttpPost(requestURL,sendRequest.toString());
        }catch (Exception e){
            resultData = "";
        }
        TraceLoggerUtil.info("接收运营商再次获取验证码接口--->" + resultData);
        return resultData;
    }

    /**
     *  学信网准备阶段接口
     * @param d 系统当前时间
     * @param idNo 身份证号
     * @param ip 用户ip
     * @param password 服务密码
     * @param phone 手机号
     * @param sign 通过MD5获取的签字
     * @param userId 用户Id  目前跟对接人沟通可以随便填,这里传递的是登录app的用户ID add by wucheng 20170510
     * @return
     */
    public static String xxPrepare(String host, String d, String idNo, String password, String phone, String sign, String ip, String userId){

        StringBuffer sendRequest = new StringBuffer();
        sendRequest.append("{");
        sendRequest.append("\"d\": \"");
        sendRequest.append(d);
        sendRequest.append("\",");
        sendRequest.append("\"idNo\": \"");
        sendRequest.append(idNo.toUpperCase());
        sendRequest.append("\",");
        sendRequest.append("\"ip\": \"");
        sendRequest.append(ip);
        sendRequest.append("\",");
        sendRequest.append("\"password\": \"");
        sendRequest.append(password);
        sendRequest.append("\",");
        sendRequest.append("\"phone\": \"");
        sendRequest.append(phone);
        sendRequest.append("\",");
        sendRequest.append("\"sign\": \"");
        sendRequest.append(sign);
        sendRequest.append("\",");
        sendRequest.append("\"userId\": \"");
        sendRequest.append(userId);
        sendRequest.append("\"");
        sendRequest.append("}");

        String resultData = "";
        String requestURL = host+"/chsi/queryChsiTaskId";
        TraceLoggerUtil.info("发送学信网准备阶段接口--->" + sendRequest.toString());
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(50000).setConnectTimeout(50000).setConnectionRequestTimeout(50000).build();
        HttpClientUtil.setRequestConfig(requestConfig);
        try {
            resultData = HttpClientUtil.getInstance().sendHttpPost(requestURL,sendRequest.toString());
        }catch (Exception e){
            resultData = "";
        }
        TraceLoggerUtil.info("接收学信网准备阶段接口--->" + resultData);
        return resultData;
    }


    /**
     * 运营商轮询任务状态
     * @param host 接口服务器
     * @param d 时间戳
     * @param phone 账号
     * @param sign 签字
     * @param smsCode 验证码
     * @return
     */
    public static String xxLunxun(String host, String d, String phone, String sign, String smsCode){

        StringBuffer sendRequest = new StringBuffer();
        sendRequest.append("{");
        sendRequest.append("\"d\": \"");
        sendRequest.append(d);
        sendRequest.append("\",");
        sendRequest.append("\"phone\": \"");
        sendRequest.append(phone);
        sendRequest.append("\",");
        sendRequest.append("\"sign\": \"");
        sendRequest.append(sign);
        sendRequest.append("\",");
        sendRequest.append("\"smsCode\": \"");
        sendRequest.append(smsCode);
        sendRequest.append("\"");
        sendRequest.append("}");

        String resultData = "";
        String requestURL = host+"/chsi/updateTaskStatus";
        TraceLoggerUtil.info("发送学信网轮询接口--->" + sendRequest.toString());
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).setConnectionRequestTimeout(30000).build();
        HttpClientUtil.setRequestConfig(requestConfig);
        try {
            resultData = HttpClientUtil.getInstance().sendHttpPost(requestURL,sendRequest.toString());
        }catch (Exception e){
            resultData = "";
        }
        TraceLoggerUtil.info("接收学信网轮询接口--->" + resultData);
        return resultData;
    }
    /**
     * 调用聚信立获取token接口
     *
     */
    public static String jxlApplyForm(String host, Map map) throws Exception {
        try {
            String url = host + "/szt/jxl/applyForm";
            TraceLoggerUtil.info("聚信立接口发送参数--->" + map);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).setConnectionRequestTimeout(60000).build();
            HttpClientUtil.setRequestConfig(requestConfig);
            String data = HttpUtil.doPost(url, JSONObject.toJSON(map).toString());
            TraceLoggerUtil.info("聚信立接口接收参数--->" + data);
            return data;
        } catch (Exception ex) {
            throw ex;
        }
    }
    /**
     * 调用聚信立获取短信接口
     *
     */
    public static String jxlGetSmsCode(String host, Map map) throws Exception {
        try {
            String url = host + "/szt/jxl/submitRequest";
            TraceLoggerUtil.info("聚信立获取短信接口发送参数--->" + map);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).setConnectionRequestTimeout(60000).build();
            HttpClientUtil.setRequestConfig(requestConfig);
            String data = HttpUtil.doGet(url, map);
            TraceLoggerUtil.info("聚信立获取短信接口接收参数--->" + data);
            return data;
        } catch (Exception ex) {
            throw ex;
        }
    }
    /**
     * 调用聚信立报告接口
     *
     */
    public static String jxlGetMobileOnline(String host, Map map) throws Exception {
        try {
            String url = host + "/szt/jxl/creditQuery";
            TraceLoggerUtil.info("聚信立获取报告接口发送参数--->" + map);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).setConnectionRequestTimeout(60000).build();
            HttpClientUtil.setRequestConfig(requestConfig);
            String data = HttpUtil.doGet(url, map);
            TraceLoggerUtil.info("聚信立获取报告接口接收参数--->" + data);
            return data;
        } catch (Exception ex) {
            throw ex;
        }
    }
    /**
     * 调用同盾手机运营商获取任务Id接口
     *
     */
    public static String tongdunGetTaskId(String host, Map map) throws Exception {
        map.put("channelType","YYS");
        map.put("channelCode", "100000");
        try {
            String url = host + "/szt/tongdun/create";
            TraceLoggerUtil.info("调用同盾手机运营商获取任务Id接口发送参数--->" + map);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).setConnectionRequestTimeout(60000).build();
            HttpClientUtil.setRequestConfig(requestConfig);
            String data = HttpUtil.doGet(url, map);
            TraceLoggerUtil.info("调用同盾手机运营商获取任务Id接口接收参数--->" + data);
            return data;
        } catch (Exception ex) {
            throw ex;
        }
    }
    /**
     * 1-调用同盾手机运营商获取短信验证码接口(submit,query);
     *  a.第1次调用获取短信验证码接口，taskStage-INIT；requestType-submit;
     *  b.第2次调用获取短信验证码接口，taskStage-INIT；requestType-query;
     * 2-验证手机运营商短信验证码接口(submit,query)
     *  c.第1次调用验证接口，taskStage-QUERY；requestType-submit;
     *  d.第2次调用验证接口，taskStage-QUERY；requestType-query;
     */
    public static String tongdunSubmitQuery(String host, Map map) throws Exception {
        try {
            String url = host + "/szt/tongdun/acquire";
            TraceLoggerUtil.info("调用同盾手机运营商获取任务Id接口发送参数--->" + map);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).setConnectionRequestTimeout(60000).build();
            HttpClientUtil.setRequestConfig(requestConfig);
            String data = HttpUtil.doGet(url, map);
            TraceLoggerUtil.info("调用同盾手机运营商获取任务Id接口接收参数--->" + data);
            return data;
        } catch (Exception ex) {
            throw ex;
        }
    }
    public static String tongdunSubmitRetry(String host, Map map) throws Exception {
        try {
            String url = host + "/szt/tongdun/retry";
            TraceLoggerUtil.info("调用同盾手机运营商获取任务Id接口发送参数--->" + map);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).setConnectionRequestTimeout(60000).build();
            HttpClientUtil.setRequestConfig(requestConfig);
            String data = HttpUtil.doGet(url, map);
            TraceLoggerUtil.info("调用同盾手机运营商获取任务Id接口接收参数--->" + data);
            return data;
        } catch (Exception ex) {
            throw ex;
        }
    }
    public static void main(String[] args) throws Exception {
        Map<String , Object> param = new HashMap<String , Object>();
//        param.put("channelType","YYS");
//        param.put("channelCode", "100000");
//        param.put("name", "张涛");
//        param.put("idNo", "342422199202218077");
//        param.put("phone", "17718145026");
        param.put("taskId","TASKYYS100000201711101554590720985066");
        param.put("taskStage", "INIT");
        param.put("phone", "17718145020");//手机号码
        param.put("userPass", "152564");//运营商服务密码
        param.put("requestType", "query");
        param.put("smsCode", "");
        param.put("authCode", "");
        String url = "http://192.168.102.103:9999";
//        String data = new MobilePwdApi().tongdunGetTaskId(url,param);
        String data = new MobilePwdApi().tongdunSubmitQuery(url,param);
        JSONObject jsonObject = JSONObject.parseObject(data);
//        JSONObject jsonObject1 = (JSONObject) jsonObject.get("obj");
        System.out.println("------------------------"+data);
        System.out.println("------------------------"+jsonObject);
        JSONObject jsonObject1 = (JSONObject) jsonObject.get("data");
        System.out.println("------------------------"+jsonObject.get("data"));
        System.out.println("------------------------"+jsonObject.getString("next_stage"));
    }
}
