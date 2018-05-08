package com.zw.erp.rest.crm.customer;

import com.base.util.HttpClientUtil;
import org.apache.log4j.Logger;

/**
 * <strong>Title : <br>
 * </strong> <strong>Description : </strong>@调用远程服务器借口，根据影像标号删除影像信息@<br>
 * <strong>Create on : 2017年03月06日 13:44<br>
 * </strong>
 * <p>
 * <strong>Copyright (C) Vbill Co.,Ltd.<br>
 * </strong>
 * <p>
 *
 * @author department:技术开发部 <br>
 *         username:吴城 <br>
 *         email: <br>
 * @version <strong>zw有限公司-运营平台</strong><br>
 *          <br>
 *          <strong>修改历史:</strong><br>
 *          从一期项目中迁移至此，未进行改动 2017年03月06日 13:44<br>
 *          -------------------------------------------<br>
 *          <br>
 *          <br>
 */

public class DeleteFileApi {

    private static Logger logger = Logger.getLogger("communication");

    /**
     * 删除接口
     *
     * @param host
     * @param id   影像标号
     * @throws Exception
     */
    public String deleteFile(String host, String id) throws Exception {
        try {
            String url = host + "/crm/customer/paper?id=" + id;
            logger.info("接口发送--->" + url);
            String data = HttpClientUtil.getInstance().sendHttpPost(url);
            logger.info("接口返回--->" + data);
            return data;
        } catch (Exception ex) {
            logger.error("异常--->", ex);
            throw ex;
        }
    }

}
