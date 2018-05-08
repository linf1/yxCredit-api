package com.zw.erp.rest.crm.customer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.util.HttpClientUtil;
import com.base.util.ImgUtil;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <strong>Title : <br>
 * </strong> <strong>Description : </strong>@@<br>
 * <strong>Create on : 2017年03月06日 13:48<br>
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
 *          修改人 修改日期 修改描述<br>
 *          -------------------------------------------<br>
 *          <br>
 *          <br>
 */

public class UploadFileApi {

    private static Logger logger = Logger.getLogger("communication");

    /**
     * 方法描述
     *
     * @param host        主机地址
     * @param empId       员工编号
     * @param bgCustId
     * @param paperTypeId 资料文件类型：9-身份证
     * @param fileName    文件名
     * @param file        文件对象
     * @return
     * @throws Exception
     */
    public JSONObject uploadFile(String host, String empId, String bgCustId,
                                 String paperTypeId, String fileName, File file) throws Exception {
        try {
            if (ImgUtil.isImage(file)) {
                if (fileName.toLowerCase().indexOf(".jpg") == -1
                        && fileName.toLowerCase().indexOf(".jpeg") == -1
                        && fileName.toLowerCase().indexOf(".png") == -1) {
                    fileName += ".jpg";
                }
            }
            String url = host + "/remote/file";
            Map map = new HashMap<String, String>();
            map.put("filename", fileName);
            map.put("empId", empId);
            map.put("bgCustId", bgCustId);
            map.put("paperTypeId", paperTypeId);
            List<File> list = new ArrayList<File>();
            list.add(file);
            logger.info("接口发送--->" + map.toString());
            String data = HttpClientUtil.getInstance().sendHttpPost(url, map, list);
            logger.info("接口返回--->" + data);
            return JSON.parseObject(data);
        } catch (Exception ex) {
            logger.error("异常--->", ex);
            throw ex;
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(new UploadFileApi().uploadFile("http://116.236.220.210:8081/cf_api", "1a9a3658-8edd-4c2c-a98c-ffc600568142",
                "402897315827c470015827c4e35d0003", "71", "QQ图片2016112814215622.jpg", new File("C:\\Users\\zh-pc\\Desktop\\QQ图片20161128142156.jpg")));
    }

}
