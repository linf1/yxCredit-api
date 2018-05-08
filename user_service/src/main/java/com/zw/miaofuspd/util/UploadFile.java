package com.zw.miaofuspd.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;
import java.util.*;

public class UploadFile {


    /**
     * 保存客户端上传文件
     *
     * @param request
     *            http request
     * @param saveDir
     *            文件存储的路径
     * @return
     * @throws Exception
     */
    public static Map<String, Object> getFile(HttpServletRequest request,
                                              String saveDir, String fileName) throws Exception {

        // 上传配置
        int MEMORY_THRESHOLD = 1024 * 1024 * 3; // 3MB
        int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
        int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB

        // 配置上传参数
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置内存临界值 - 超过后将产生临时文件并存储于临时目录中
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // 设置临时存储目录
        File saveDirFile = new File(saveDir);
        // if (saveDirFile.isDirectory()) {
        if (!saveDirFile.exists()) {
            saveDirFile.mkdirs();
        }
		/*
		 * } else { saveDir = System.getProperty("java.io.tmpdir"); }
		 */
        factory.setRepository(new File(saveDir));

        ServletFileUpload upload = new ServletFileUpload(factory);

        // 设置最大文件上传值
        upload.setFileSizeMax(MAX_FILE_SIZE);

        // 设置最大请求值 (包含文件和表单数据)
        upload.setSizeMax(MAX_REQUEST_SIZE);
        Map<String, Object> map = new HashMap();
        List<FileItem> formItems = upload.parseRequest(request);
        List<Map<String, Object>> fileList = new ArrayList<Map<String, Object>>();
        if (formItems != null && formItems.size() > 0) {
            // 迭代表单数据
            for (int i=0;i<formItems.size();i++){
                FileItem item = formItems.get(i);
                // 处理不在表单中的字段
                if (!item.isFormField()) {
                    Map<String, Object> fileModel = new HashMap<String, Object>();
                    String path = saveDir + File.separator + fileName+i;
                    File file = new File(path);
                    if (file.exists()) {
                        file.delete();
                    }
                    item.write(file);
                    fileModel.put("originalName", item.getName());
                    fileModel.put("file", file);
                    fileModel.put("Name", fileName+i);
                    fileList.add(fileModel);
                } else {
                    String name = item.getFieldName(); // 获取name属性的值
                    String value = item.getString("utf-8"); // 获取value属性的值
                    map.put(name, value);
                }
                item.delete();
            }
        }
        map.put("fileList", fileList);
        return map;
    }

    public static Map<String, Object> getFileNew(HttpServletRequest request,
                                                 String saveDir, String fileName) throws Exception {
        // 上传配置
        int MEMORY_THRESHOLD = 1024 * 1024 * 3; // 3MB
        int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
        int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB

        // 配置上传参数
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置内存临界值 - 超过后将产生临时文件并存储于临时目录中
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // 设置临时存储目录
        File saveDirFile = new File(saveDir);
        if (saveDirFile.isDirectory()) {
            if (!saveDirFile.exists()) {
                saveDirFile.mkdirs();
            }
        } else {
            saveDir = System.getProperty("java.io.tmpdir");
        }
        factory.setRepository(new File(saveDir));

        ServletFileUpload upload = new ServletFileUpload(factory);

        // 设置最大文件上传值
        upload.setFileSizeMax(MAX_FILE_SIZE);

        // 设置最大请求值 (包含文件和表单数据)
        upload.setSizeMax(MAX_REQUEST_SIZE);
        Map<String, Object> map = new HashMap();
        List<FileItem> formItems = upload.parseRequest(request);
        List<Map<String, Object>> fileList = new ArrayList<Map<String, Object>>();
        if (formItems != null && formItems.size() > 0) {
            // 迭代表单数据
            for (FileItem item : formItems) {
                // 处理不在表单中的字段
                if (!item.isFormField()) {
                    String picName = item.getName();
                    picName = picName.substring(picName.lastIndexOf("\\") + 1);
                    String extName = picName
                            .substring(picName.lastIndexOf("."));

                    fileName = fileName.replace("-", "") + extName;

                    String path = saveDir + File.separator + fileName;

                    FileUtils.copyInputStreamToFile(item.getInputStream(),
                            new File(path));

                    Map<String, Object> fileModel = new HashMap<String, Object>();
                    fileModel.put("originalName", item.getName());
                    fileModel.put("Name", fileName);
                    fileList.add(fileModel);
                } else {
                    String name = item.getFieldName(); // 获取name属性的值
                    String value = item.getString("utf-8"); // 获取value属性的值
                    map.put(name, value);
                }
            }
        }
        map.put("fileList", fileList);
        return map;
    }

    /**
     * 保存客户端上传文件,默认先保存在临时目录中
     *
     * @param request
     *            http request
     * @return
     * @throws Exception
     */
    public static Map<String, Object> getFile(HttpServletRequest request)
            throws Exception {
        String saveDir = System.getProperty("java.io.tmpdir");
        String fileName = UUID.randomUUID().toString();
        return getFile(request, saveDir, fileName);
    }


    /**
     * 保存客户端上传文件
     *
     * @param request
     *            http request
     * @param saveDir
     *            文件存储的路径
     * @return
     * @throws Exception
     */
    public static Map<String, Object> getFileContract(HttpServletRequest request,
                                                      String saveDir, String fileName) throws Exception {

        // 上传配置
        int MEMORY_THRESHOLD = 1024 * 1024 * 3; // 3MB
        int MAX_FILE_SIZE = 1024 * 1024 * 300; // 40MB
        int MAX_REQUEST_SIZE = 1024 * 1024 * 400; // 50MB

        // 配置上传参数
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置内存临界值 - 超过后将产生临时文件并存储于临时目录中
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // 设置临时存储目录
        File saveDirFile = new File(saveDir);
        // if (saveDirFile.isDirectory()) {
        if (!saveDirFile.exists()) {
            saveDirFile.mkdirs();
        }
		/*
		 * } else { saveDir = System.getProperty("java.io.tmpdir"); }
		 */
        factory.setRepository(new File(saveDir));

        ServletFileUpload upload = new ServletFileUpload(factory);

        // 设置最大文件上传值
        upload.setFileSizeMax(MAX_FILE_SIZE);

        // 设置最大请求值 (包含文件和表单数据)
        upload.setSizeMax(MAX_REQUEST_SIZE);
        Map<String, Object> map = new HashMap();
        List<FileItem> formItems = upload.parseRequest(request);
        List<Map<String, Object>> fileList = new ArrayList<Map<String, Object>>();
        if (formItems != null && formItems.size() > 0) {
            // 迭代表单数据
            for (FileItem item : formItems) {
                // 处理不在表单中的字段
                if (!item.isFormField()) {
                    String suffixName=item.getName();
                    suffixName = suffixName.substring(suffixName.lastIndexOf(".") + 1);
                    fileName=fileName+"."+suffixName;
                    Map<String, Object> fileModel = new HashMap<String, Object>();
                    String path = saveDir + File.separator + fileName;
                    File file = new File(path);
                    if (file.exists()) {
                        file.delete();
                    }
                    item.write(file);
                    fileModel.put("originalName", item.getName());
                    fileModel.put("file", file);
                    fileModel.put("Name", fileName);
                    fileList.add(fileModel);
                } else {
                    String name = item.getFieldName(); // 获取name属性的值
                    String value = item.getString("utf-8"); // 获取value属性的值
                    map.put(name, value);
                }
                item.delete();
            }
        }
        map.put("fileList", fileList);
        return map;
    }

    private static OSSClient ossClient=null;

    //初始化OSSClient
    private static void initializeOSSClient(){
        //aliyun oss  endpoint
        String endpoint="";
        //aliyun oss  accessKeyId
        String accessKeyId="";
        //aliyun oss  accessKeySecret
        String accessKeySecret="";
        ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }


    /**
     * 上传文件OSS
     *
     * @param request
     *            http request
     * @param saveDir
     *            文件临时存储的根目录
     * @param saveDirOSS
     *            OSS根目录名
     * @param fileName
     *            文件名称(包括目录/文件名)
     * @param endpoint
     *            oss访问域名和数据中心
     * @param accessKeyId
     *            OSS:accessKeyId
     * @param accessKeySecret
     *            OSS:acessKeySecretc
     * @param catalog
     *            OSS:二级目录
     * @return
     * @throws Exception
     */
    public static Map<String, Object> getFileOSS(HttpServletRequest request,String saveDir, String fileName,String catalog,String saveDirOSS,String endpoint,String accessKeyId,String accessKeySecret) throws Exception {

        // 上传配置
        int MEMORY_THRESHOLD = 1024 * 1024 * 3; // 3MB
        int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
        int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB

        // 配置上传参数
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置内存临界值 - 超过后将产生临时文件并存储于临时目录中
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // 设置临时存储目录
        File saveDirFile = new File(saveDir);
        // if (saveDirFile.isDirectory()) {
        if (!saveDirFile.exists()) {
            saveDirFile.mkdirs();
        }
		/*
		 * } else { saveDir = System.getProperty("java.io.tmpdir"); }
		 */
        factory.setRepository(new File(saveDir));

        ServletFileUpload upload = new ServletFileUpload(factory);

        // 设置最大文件上传值
        upload.setFileSizeMax(MAX_FILE_SIZE);

        // 设置最大请求值 (包含文件和表单数据)
        upload.setSizeMax(MAX_REQUEST_SIZE);
        Map<String, Object> map = new HashMap();
        List<FileItem> formItems = upload.parseRequest(request);
        List<Map<String, Object>> fileList = new ArrayList<Map<String, Object>>();
        ossClient = new OSSClient("https://"+endpoint, accessKeyId, accessKeySecret);
        if (formItems != null && formItems.size() > 0) {
            // 迭代表单数据
            for (int i=0;i<formItems.size();i++){
                FileItem item = formItems.get(i);
                // 处理不在表单中的字段
                if (!item.isFormField()) {
                    //获取后缀名
                    String suffixName=item.getName();
                    suffixName = suffixName.substring(suffixName.lastIndexOf(".") + 1);
                    fileName=fileName+i+"."+suffixName;
                    Map<String, Object> fileModel = new HashMap<String, Object>();
                    String path = saveDir + File.separator + fileName;
                    File file = new File(path);
                    if (file.exists()) {
                        file.delete();
                    }
                    //写到本地
                    item.write(file);
                    //上传的OSS
                    ossClient.putObject(saveDirOSS,catalog+fileName,file);
                    fileModel.put("originalName", item.getName());
                    fileModel.put("file", file);
                    fileModel.put("Name", "https://"+saveDirOSS+"."+endpoint+"/"+catalog+fileName);
                    fileList.add(fileModel);
                    //删除本地文件
                    file.delete();
                } else {
                    String name = item.getFieldName(); // 获取name属性的值
                    String value = item.getString("utf-8"); // 获取value属性的值
                    map.put(name, value);
                }
                item.delete();
            }
        }
        // 关闭client
        ossClient.shutdown();
        map.put("fileList", fileList);
        return map;
    }

    /**
     * 上传网络流
     * @param inputStream 网络流
     * @param catalog
     * @param bucketName
     * @param endpoint
     * @param accessKeyId
     * @param accessKeySecret
     * @return
     * @throws Exception
     */
    public static Map<String, Object> getFileOSS(InputStream inputStream, String catalog, String bucketName, String endpoint, String accessKeyId, String accessKeySecret)throws Exception{
        Map<String, Object> result = new HashMap<>();
        //创建ossClient对象
        ossClient = new OSSClient("https://"+endpoint, accessKeyId, accessKeySecret);
        PutObjectResult putObjectResult = ossClient.putObject(bucketName,catalog,inputStream);
        result.put("src", "https://"+bucketName+"."+endpoint+"/"+catalog);
        // 关闭client
        ossClient.shutdown();
        return result;
    }
    /**
     * 删除OSS文件
     * @param saveDirOSS
     *            OSS根目录名
     * @param fileName
     *            文件名称(包括目录/文件名)
     * @param endpoint
     *            oss访问域名和数据中心
     * @param accessKeyId
     *            OSS:accessKeyId
     * @param accessKeySecret
     *            OSS:acessKeySecretc
     * @return
     * @throws Exception
     */
    public static boolean deleteFilePathOSS(String fileName,String saveDirOSS,String endpoint,String accessKeyId,String accessKeySecret){
        try {
            ossClient = new OSSClient("https://"+endpoint, accessKeyId, accessKeySecret);
            // 删除Object
            ossClient.deleteObject(saveDirOSS, fileName);
            // 关闭client
            ossClient.shutdown();
            return true;
        }catch (Exception e){
            // 关闭client
            ossClient.shutdown();
            return false;
        }
        finally {
            return false;
        }
    }
}
