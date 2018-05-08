package com.zw.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.util.HttpClientUtil;
import com.base.util.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <strong>Title : 聚信立手机密码查询接口<br>
 * </strong> <strong>Description : </strong>@类注释说明写在此处@<br>
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
public class JxlCollectApi {
    private static Logger logger = Logger.getLogger("communication");

    /**
     * 手机服务密码查询接口以及手机短信验证码查询接口
     * @param host 接口服务器地址
     * @param mobilePwd 手机服务密码
     * @param tel 手机号
     * @param real_name 真实姓名
     * @param id_card 身份份证号
     * @param code 手机短信验证码
     * @param address
     * @return
     */
    public static String jxlCollect(String host, String mobilePwd, String tel, String real_name, String id_card, String code, List<Map> list, String address) {
        try {
            StringBuffer sendRequest = new StringBuffer();
            sendRequest.append("{");
            sendRequest.append("\"basicInfo\": {");
            sendRequest.append("\"cellPhoneNum\": \"");
            sendRequest.append(tel);
            sendRequest.append("\",");
            sendRequest.append("\"homeAddr\": \"");
            sendRequest.append(address);
            sendRequest.append("\",");
            sendRequest.append("\"idCardNum\": \"");
            sendRequest.append(id_card);
            sendRequest.append("\",");
            sendRequest.append("\"name\": \"");
            sendRequest.append(real_name);
            sendRequest.append("\"},");
            sendRequest.append("\"contactList\":");
            sendRequest.append(JSON.toJSONString(list));
            sendRequest.append(",");
            sendRequest.append("\"captcha\": \"");
            if(StringUtils.isNotEmpty(code)){
                sendRequest.append(code);
            }
            sendRequest.append("\",");
            if(StringUtils.isNotEmpty(code)){
                sendRequest.append("\"type\": \"SUBMIT_CAPTCHA\",");
            }
            sendRequest.append("\"mobileAccount\": \"");
            sendRequest.append(tel);
            sendRequest.append("\",");
            sendRequest.append("\"mobilePwd\": \"");
            sendRequest.append(mobilePwd);
            sendRequest.append("\"");
            sendRequest.append("}");

            String requestURL = host+"/jxl/mobile/collect";
            //String requestURL = "http://192.168.102.103:8080/tteesstt";
            //System.out.print("发送JXL验证接口");
            logger.info("发送JXL验证接口--->" + sendRequest.toString());
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(600000).setConnectTimeout(600000).setConnectionRequestTimeout(600000).build();
            HttpClientUtil.setRequestConfig(requestConfig);
            String resultData = HttpClientUtil.getInstance().sendHttpPost(requestURL,sendRequest.toString(),true);
            //System.out.print("接收JXL验证接口");
            logger.info("接收JXL验证接口--->" + resultData);
            return resultData;
        } catch (Exception ex) {
            logger.error("JXL验证接口--->", ex);
            throw ex;
        }
    }

    public static void main(String[] args) throws Exception{
        System.out.print(new JxlCollectApi().jxlCollect("http://datacenter.zzlgroup.com/datacenter/", "927919", "18255196561", "4", "5", "6", null, "7"));
    }
}
