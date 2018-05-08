package com.zw.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.util.HttpClientUtil;
import com.base.util.TraceLoggerUtil;

/**
 * <strong>Title :<br>
 * </strong> <strong>Description : </strong>@类注释说明写在此处@<br>
 * <strong>Create on : 2017年04月12日<br>
 * </strong>
 * <p>
 * <strong>Copyright (C) ZW Co.,Ltd.<br>
 * </strong>
 * <p>
 *
 * @author department:技术开发部 <br>
 *         username:何浩 <br>
 *         email: <br>
 * @version <strong>zw有限公司-运营平台</strong><br>
 *          <br>
 *          <strong>修改历史:</strong><br>
 *          修改人 修改日期 修改描述<br>
 *          -------------------------------------------<br>
 *          <br>
 *          <br>
 */
public class BalanceApi {
    /**
     * 富有查询余额接口
     *
     * @param host 富有余额查询接口地址
     * @param cust_no 金账户名称
     * @return 余额信息
     * @throws Exception
     */
    public JSONObject balance(String host, String cust_no
    ) throws Exception {
        try {
            String url = host + "?cust_no=" + cust_no;
            TraceLoggerUtil.info("接口发送--->" + url);
            TraceLoggerUtil.info(cust_no+"接口发送--->" + url);
            String data = HttpClientUtil.getInstance().sendHttpGet(url);
            TraceLoggerUtil.info("接收富有查询余额信息--->" + data);
            TraceLoggerUtil.info(cust_no+"接收富有查询余额信息--->" + data);
            return JSON.parseObject(data);
        } catch (Exception ex) {
            TraceLoggerUtil.error("富有查询余额信息异常--->", ex);
            throw ex;
        }
    }
}

