package com.zw.miaofuspd.employee.controller;

import com.alibaba.fastjson.JSONObject;
import com.base.util.FileNewName;
import com.zw.miaofuspd.facade.dict.service.ISystemDictService;
import com.zw.miaofuspd.facade.merchant.service.MerchandiseService;
import com.zw.util.UploadFileUtil;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import com.zw.web.base.vo.VOConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.*;

/**
 * Created by Administrator on 2017/11/22 0022.
 */
@Controller
@RequestMapping("/merchandise")
public class MerchandiseController extends AbsBaseController {
    @Autowired
    public MerchandiseService merchandiseService;

    @Autowired
    ISystemDictService iSystemDictService;
    /**
     *获取该商户下所有激活的商品.分页显示
     * @return
     * @throws Exception
     */
//    @RequestMapping("/getMerchandiseList")
//    @ResponseBody
//    public ResultVO getMerchandiseList(String merchantId,String firstIndex,String pageSize) throws Exception{
//        ResultVO resultVO = new ResultVO();
//        Map map = new HashMap();
//        List list = merchandiseService.getMerchandiseList(merchantId,firstIndex,pageSize);
//        if(list!=null && list.size()>0){
//            map.put("merchandiseNum",list.size());//该商户下所有商品的个数
//        }else{
//            map.put("merchandiseNum","0");//如果没有则返回0
//        }
//        map.put("list",list);
//        resultVO.setRetData(map);
//        return resultVO;
//    }
//    /**
//     * 根据商户名称，模糊查询出所有符合的商户
//     * @param merchandiseBrandName
//     * @param merchantId
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping("/getMerchandiseInfo")
//    @ResponseBody
//    public ResultVO getMerchandiseInfo(String merchandiseBrandName, String merchantId) throws Exception{
//        ResultVO resultVO = new ResultVO();
//        List list = merchandiseService.getMerchandiseInfo(merchandiseBrandName,merchantId);
//        resultVO.setRetData(list);
//        return resultVO;
//    }

    /**
     * 获取该商户下所有激活的商品
     * @param merchantId 商户id
     * @return
     * @throws Exception
     */
    @RequestMapping("/getMerchandiseList")
    @ResponseBody
    public ResultVO getMerchandiseList(String merchantId) throws Exception{
        ResultVO resultVO = new ResultVO();
        Map map = new HashMap();
        Map map1 = merchandiseService.findMerchantNameById(merchantId);
        if(map1!=null && map1.size()>0){
            String merName =map1.get("merName").toString();
            map.put("merchantName",merName);
        }
        List list = merchandiseService.getMerchandiseList(merchantId);
        if(list!=null && list.size()>0){
            map.put("merchandiseNum",list.size());//该商户下所有商品的个数
        }else{
            map.put("merchandiseNum","0");//如果没有则返回0
        }
        map.put("list",list);
        resultVO.setRetData(map);
        return resultVO;
    }
    /**
     * 模糊查询商店里商品
     * @param  merchantId 商户id
     * @paran  salesmanId办单员id
     * @param  merchandiseSearch 搜索内容
     * @return
     */
    @RequestMapping("/merchandiseSearch")
    @ResponseBody
    public ResultVO merchandiseSearch(String merchantId,String merchandiseSearch,String salesmanId) throws  Exception{
        ResultVO resultVO = new ResultVO();
        Map map = new HashMap();
        List list = merchandiseService.searchMerchandiseList(merchantId,salesmanId,merchandiseSearch);
        if(list!=null && list.size()>0){
            map.put("merchandiseList",list);
            resultVO.setRetData(map);
        }else{
            resultVO.setRetCode(VOConst.FAIL);
            resultVO.setRetMsg("未找到符合条件的商品");
        }
        return resultVO;
    }
    /**
     *展示搜索历史的前几条数据
     * @param merchantId 商户Id
     * @param salesmanId 办单员Id
     * @return
     * @throws Exception
     */
    @RequestMapping("/showSearchHistory")
    @ResponseBody
    public ResultVO showSerarchHistory(String merchantId,String salesmanId) throws  Exception {
        ResultVO resultVO = new ResultVO();
        Map map = new HashMap();
        List list=merchandiseService.showSearchList(merchantId,salesmanId);
        if(list!=null && list.size()>0){
            map.put("searchHistoryList",list);
            resultVO.setRetData(map);
        }else{
            map.put("searchHistoryList",new ArrayList());
            resultVO.setRetData(map);
        }
        return resultVO;
    }
    /**
     * 清空搜索历史
     * @param merchantId 商户Id
     * @param salesmanId 办单员Id
     * @return
     * @throws Exception
     */
    @RequestMapping("/clearSearchHistory")
    @ResponseBody
    public ResultVO clearSerarchHistory(String merchantId,String salesmanId) throws  Exception{
        ResultVO resultVO = new ResultVO();
        Map map =new HashMap();
        merchandiseService.dropSerarchHistroy(merchantId,salesmanId);
        map.put("searchHistoryList",new ArrayList());
        resultVO.setRetData(map);
        resultVO.setRetMsg("搜索历史已经清空");
        return resultVO;
    }

    /**
     * 获取后台配置的所有的商品类型
     */
    @RequestMapping("/getMerchandisTypeList")
    @ResponseBody
    public ResultVO getMerchandisTypeList() throws Exception{
        ResultVO resultVO = new ResultVO();
        Map map =new HashMap();
        List list=merchandiseService.getAllMerchantdiseType();
        if(list!=null && list.size()>0){
            map.put("merchandiseTypeList",list);
            resultVO.setRetData(map);
        }else{
            map.put("merchandiseTypeList",new ArrayList());
            resultVO.setRetData(map);
            resultVO.setRetMsg("没有商品类型");
        }
        return resultVO;
    }
    /**
     * 获取对应商品类型下的所有品牌类型，品牌类型下的所有的型号
     * 对应型号下的所有的版本
     * @param  parentId 父节点
     */
    @RequestMapping("/getChildrenTypeList")
    @ResponseBody
    public ResultVO getMerchandisTypeList(String parentId) throws Exception{
        ResultVO resultVO = new ResultVO();
        Map map =new HashMap();
        List list=merchandiseService.getAllType(parentId);
        if(list!=null && list.size()>0){
            map.put("childrenTypeList",list);
            resultVO.setRetData(map);
        }else{
            map.put("childrenTypeList",new ArrayList());
            resultVO.setRetData(map);
        }
        return resultVO;
    }
    /**
     * 上传头像到阿里云
     * @return
     * @throws Exception
     */
    @ResponseBody
    @PostMapping("/upload")
    public ResultVO upload() throws Exception {
        //获取图片路劲
        String imgPath = getHttpSession().getServletContext().getRealPath("/fintecher_file");
        //生成新的文件名
        String id = UUID.randomUUID().toString();
        String url = "image";
        //获得头像路径
        String catalog="fintecher_file"+"/"+url+"/";
        String bucket = iSystemDictService.getInfo("oss.bucket");
        String endpoint = iSystemDictService.getInfo("oss.endpoint");
        String accessKeyId = iSystemDictService.getInfo("oss.accessKeyId");
        String accessKeySecret = iSystemDictService.getInfo("oss.accessKeySecret");
        Map<String, Object> map = UploadFileUtil.getFileOSS(getRequest(), imgPath+ File.separator + url, id,catalog,bucket,endpoint,accessKeyId,accessKeySecret);
        Map map1 = new HashMap();
        map1.put("fileList",map.get("fileList"));
        return this.createResultVO(map1);
    }
    /**
     * 添加商品
     */
    @RequestMapping("/addMerchandise")
    @ResponseBody
    public ResultVO addMerchandise(String data) throws Exception {
        ResultVO resultVO = new ResultVO();
        Map merMap  = JSONObject.parseObject(data);
        String merchantId = merMap.get("merchantId").toString();
        String versionId = merMap.get("versionId").toString();
        String imgUrl =merMap.get("imgUrl").toString();
        //商品对应的多张图片地址
        String [] merchandiseImages=imgUrl.split(",");
        imgUrl = merchandiseImages[0];
        Map resultMap = merchandiseService.addMerchandiseRelMerchant(versionId,merchantId,imgUrl);
//        if(!(boolean)resultMap.get("flag")){
//            resultVO.setErrorMsg(VOConst.FAIL,resultMap.get("msg").toString());
//        }
        //添加商品图片
        for(int i=0;i<merchandiseImages.length;i++){
            merchandiseService.addMerchandiseImagesToMerchandise(merchandiseImages[i],versionId);
        }
        resultVO.setRetData(resultMap);
        return resultVO;
    }
    /**
     * 添加新商品
     * @param data
     */
    @RequestMapping("/addNewMerchandise")
    @ResponseBody
    public ResultVO addNewMerchandise(String data) throws  Exception{
        ResultVO resultVO = new ResultVO();
        JSONObject json = JSONObject.parseObject(data);
        Map merMap  = JSONObject.parseObject(data);
        String merchantId=(String)merMap.get("merchantId");
        String imgUrl =(String)merMap.get("imgUrl");
        //商品对应的多张图片地址
        String [] merchandiseImages=imgUrl.split(",");
        //商品的第一张图片
        imgUrl = merchandiseImages[0];
        merMap.put("imgUrl",imgUrl);
        //增加商品
        Map resultMap = merchandiseService.addMerchandiseType(merMap);
        Boolean flag=(Boolean)resultMap.get("flag");
        //表示添加新商品成功
        if(flag){
             //获取商品的版本,商品的版本关联商品的id
            String versionId=(String)resultMap.get("versionId");
            //将商品的信息保存在商品商户信息表中
            merchandiseService.addMerchandiseToMerchant(versionId,merchantId);
            //同时需要将商品图片和商品id关联保存在mag_customer_image表中
            for(int i=0;i<merchandiseImages.length;i++){
                  merchandiseService.addMerchandiseImagesToMerchandise(merchandiseImages[i],versionId);
            }
        }
        resultVO.setRetData(resultMap);
        return resultVO;
    }
}
