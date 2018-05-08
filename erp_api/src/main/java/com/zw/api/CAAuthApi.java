package com.zw.api;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.util.HttpClientUtil;
import com.base.util.TraceLoggerUtil;

import org.apache.log4j.Logger;

/**
 * <strong>Title : CA认证<br>
 * </strong> <strong>Description : </strong>@类注释说明写在此处@<br>
 * <strong>Create on : 2016年11月7日<br>
 * </strong>
 * <p>
 * <strong>Copyright (C) Vbill Co.,Ltd.<br>
 * </strong>
 * <p>
 *
 * @author department:技术开发部 <br>
 *         username:yinlei <br>
 *         email:yin_lei@suixingpay.com <br>
 * @version <strong>zw有限公司-运营平台</strong><br>
 *          <br>
 *          <strong>修改历史:</strong><br>
 *          修改人 修改日期 修改描述<br>
 *          -------------------------------------------<br>
 *          <br>
 *          <br>
 */
public class CAAuthApi{


    /**
     * CA认证接口
     * 方法描述
     *
     * @param host
     * @param orderId 订单号
     * @return 认证结果
     */
    public JSONObject acAuth(String host, String orderId) {
        try {
            String url = host + "/contract";
            Map map = new HashMap();
            map.put("crmApplyId", orderId);
            TraceLoggerUtil.info("发送CA认证--->" + map.toString());
            TraceLoggerUtil.info(orderId+"发送CA认证--->" + map.toString());
            String data = HttpClientUtil.getInstance().sendHttpPost(url, map);
            TraceLoggerUtil.info("接收CA认证--->" + data);
            TraceLoggerUtil.info(orderId+"接收CA认证--->" + data);
            return JSON.parseObject(data);
        } catch (Exception ex) {
            TraceLoggerUtil.error("CA认证异常--->", ex);
            throw ex;
        }
    }

}
