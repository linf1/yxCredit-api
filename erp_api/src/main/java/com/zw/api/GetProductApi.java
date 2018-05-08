package com.zw.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.util.HttpClientUtil;
import org.apache.log4j.Logger;

/**
 * <strong>Title : <br>
 * </strong> <strong>Description : </strong>@类注释说明写在此处@<br>
 * <strong>Create on : 2017年04月25日<br>
 * </strong>
 * <p>
 * <strong>Copyright (C) Vbill Co.,Ltd.<br>
 * </strong>
 * <p>
 *
 * @author department:技术开发部 <br>
 *         username:yaoxuetao <br>
 *         email: <br>
 * @version <strong>zw有限公司-运营平台</strong><br>
 *          <br>
 *          <strong>修改历史:</strong><br>
 *          修改人 修改日期 修改描述<br>
 *          -------------------------------------------<br>
 *          <br>
 *          <br>
 */
public class GetProductApi {

    private static Logger logger = Logger.getLogger("communication");

    /**
     * 产品系列编号查询一二级产品信息
     *
     * @param host
     * @param proNumber 预定义产品编号
     * @return
     * @throws Exception
     */
    public JSONObject getProduct(String host, String proNumber) throws Exception {
        try {
            String url = host + "/product/crm?proNumber=" + proNumber;
            logger.info("接口发送--->" + url);
            String data = HttpClientUtil.getInstance().sendHttpGet(url);
            logger.info("接口返回--->" + data);
            return JSON.parseObject(data);
        } catch (Exception ex) {
            logger.error("异常--->", ex);
            throw ex;
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(new GetProductApi().getProduct(
                "http://172.16.10.8:8082/cf_api", ""));
    }

}


