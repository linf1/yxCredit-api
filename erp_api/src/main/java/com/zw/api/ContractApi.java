package com.zw.api;

import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.util.HttpClientUtil;
import com.base.util.TraceLoggerUtil;

import org.apache.log4j.Logger;

/**
 * <strong>Title : 一、	合同信息查询接口<br>
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
public class ContractApi {

    /**
     * 合同信息查询接口
     *
     * @param host
     * @param orderId 订单id
     * @return 合同详细信息
     * @throws Exception
     */
    public JSONObject contract(String host, String orderId
    ) throws Exception {
        try {
            String url = host + "/contract?applayId=" + orderId;
            TraceLoggerUtil.info("接口发送--->" + url);
            TraceLoggerUtil.info(orderId+"接口发送--->" + url);
            String data = HttpClientUtil.getInstance().sendHttpGet(url);
            TraceLoggerUtil.info("接收合同详细信息--->" + data);
            TraceLoggerUtil.info(orderId+"接收合同详细信息--->" + data);
            return JSON.parseObject(data);
        } catch (Exception ex) {
            TraceLoggerUtil.error("合同详细信息异常--->", ex);
            throw ex;
        }
    }
}
