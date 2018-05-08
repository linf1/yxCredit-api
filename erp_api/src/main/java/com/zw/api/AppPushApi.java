package com.zw.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.util.HttpClientUtil;
import org.apache.log4j.Logger;

/**
 * <strong>Title : <br>
 * </strong> <strong>Description : </strong>@类注释说明写在此处@<br>
 * <strong>Create on : 2016年10月25日<br>
 * </strong>
 * <p>
 * <strong>Copyright (C) Vbill Co.,Ltd.<br>
 * </strong>
 * <p>
 *
 * @author department:技术开发部 <br>
 *         username:zhoujun <br>
 *         email: <br>
 * @version <strong>zw有限公司-运营平台</strong><br>
 *          <br>
 *          <strong>修改历史:</strong><br>
 *          修改人 修改日期 修改描述<br>
 *          -------------------------------------------<br>
 *          <br>
 *          <br>
 */
public class AppPushApi {
    private static Logger logger = Logger.getLogger("communication");

    /**
     * 指云推送
     *
     * @param host    主机地址
     * @param empid   员工id，多个id用,分割
     * @param pushNum 推送码，必填
     * @param content 推送内容
     * @return
     * @throws Exception
     */
    public JSONObject appPush(String host, String empid, String pushNum, String content) throws Exception {
        try {
            String url = host + "?empIds=" + empid + "&pushNum=" + pushNum + "&content=" + content;
            String data = HttpClientUtil.getInstance().sendHttpGet(url);
            logger.info("推送指云消息--->" + url);
            return JSON.parseObject(data);
        } catch (Exception ex) {
            logger.error("推送指云消息出现异常--->", ex);
            throw ex;
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(new AppPushApi().appPush("http://116.236.220.210:9659/app_v3/sys/info/appPush.html", "zzl463381", "80001", "推动内容"));
    }
}
