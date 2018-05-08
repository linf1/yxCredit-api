package com.zw.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


import com.base.util.HttpClientUtil;
import com.base.util.TraceLoggerUtil;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <strong>Title : 辅助进件提交接口<br>
 * </strong> <strong>Description : </strong>@类注释说明写在此处@<br>
 * <strong>Create on : 2016年12月22日<br>
 * </strong>
 * <p>
 * <strong>Copyright (C) Vbill Co.,Ltd.<br>
 * </strong>
 * <p>
 *
 * @author department:技术开发部 <br>
 *         username:weiming <br>
 *         email: <br>
 * @version <strong>zw有限公司-运营平台</strong><br>
 *          <br>
 *          <strong>修改历史:</strong><br>
 *          修改人 修改日期 修改描述<br>
 *          -------------------------------------------<br>
 *          <br>
 *          <br>
 */
public class AssitApplyApi {
    private static Logger logger = Logger.getLogger("communication");

    /**
     * 进件提交
     *
     * @param host
     * @param crmApplyId
     * * @param money
     * @return
     * @throws Exception
     */
    public JSONObject applyApi(String host,String crmApplyId, String money)
            throws Exception {
        try {
            String url = host + "/crm/apply/submit";
            Map map = new HashMap();
            map.put("crmApplyId", crmApplyId);
            map.put("money", money);
            logger.info("辅助进件接口发送--->" + map.toString());
            TraceLoggerUtil.info(crmApplyId+"辅助进件接口发送--->" + map.toString());
            String data =  HttpClientUtil.getInstance().sendHttpPost(url, map);
            logger.info("接口返回--->" + data);
            TraceLoggerUtil.info(crmApplyId+"接口返回--->" + data);
            return JSON.parseObject(data);
        } catch (Exception ex) {
            logger.error("异常--->", ex);
            throw ex;
        }
    }
}
