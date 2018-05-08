package com.zw.cmpp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.util.HttpClientUtil;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <strong>Title : 短信接口<br>
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
public class CmppApi{
   /* private static Logger logger = Logger.getLogger("communication");*/

    public String send(String host, String mobNo, String chanel, String number,
                       String verifiCode) throws Exception {
        try {
            String url = host;
            JSONObject params = new JSONObject();
            params.put("verification_code", verifiCode);
            params.put("channel", chanel);// channel 2:融资；3：开呗，6贷我走
            Map<String, Object> reqMap = new LinkedHashMap<String, Object>();
            reqMap.put("mobile", mobNo);
            reqMap.put("number", number);
            reqMap.put("params", params);
            reqMap.put("sysNumber", "fls_business");
           //logger.info("发送sms报文---->" + reqMap.toString());
            //info(mobNo, "发送sms报文---->" + reqMap.toString());
            String returnString = HttpClientUtil.getInstance().sendHttpPost(url,
                    JSON.toJSONString(reqMap));
            //logger.info("接收sms报文---->" + returnString);
            //info(mobNo, "接收sms报文---->" + returnString);
            return returnString;
        } catch (Exception ex) {
            //logger.error("sms通讯出现异常---->", ex);
            throw ex;
        }

    }

    public static void main(String[] args) throws Exception {
        JSONObject jsonObject = new JSONObject();

        System.out.println(new CmppApi().send("http://192.168.102.124:8082/sms/sendMsg.html", "18055111241", "5", "401",
                "123456"));
    }
}
