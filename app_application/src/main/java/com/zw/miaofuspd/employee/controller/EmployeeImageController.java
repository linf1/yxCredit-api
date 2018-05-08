package com.zw.miaofuspd.employee.controller;

import com.base.util.FileNewName;
import com.zw.miaofuspd.facade.dict.service.ISystemDictService;
import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.order.service.AppImageService;
import com.zw.miaofuspd.facade.order.service.AppOrderService;
import com.zw.miaofuspd.facade.user.service.IUserService;
import com.zw.util.UploadFileUtil;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import com.zw.web.base.vo.VOConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Administrator on 2017/12/27 0027.
 */
@Controller
@RequestMapping("/image")
public class EmployeeImageController extends AbsBaseController {
    @Autowired
    private AppImageService appImageService;
    @Autowired
    private IUserService userService;
    @Autowired
    private ISystemDictService iSystemDictService;
    @Autowired
    private AppOrderService appOrderService;
    /**
     *
     *上传手签
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/uploadHand")
    public ResultVO upload() throws Exception {
        ResultVO resultVO =  new ResultVO();
        //获取图片路劲
        String imgPath = getHttpSession().getServletContext().getRealPath("/fintecher_file");
        //生成新的文件名
        String fileName = FileNewName.gatFileName();
        String id = UUID.randomUUID().toString();
        String url = "image";
        //获得头像路劲
        String catalog="fintecher_file"+"/"+url+"/";
        String bucket = iSystemDictService.getInfo("oss.bucket");
        String endpoint = iSystemDictService.getInfo("oss.endpoint");
        String accessKeyId = iSystemDictService.getInfo("oss.accessKeyId");
        String accessKeySecret = iSystemDictService.getInfo("oss.accessKeySecret");
        Map<String, Object> map = UploadFileUtil.getFileOSS(getRequest(), imgPath+ File.separator + url, id,catalog,bucket,endpoint,accessKeyId,accessKeySecret);
        List list = (List) map.get("fileList");
        Map map1 = (Map) list.get(0);
        String nameUrl = map1.get("Name").toString();
        String orderId = map.get("orderId").toString();
        //获取用户的信息
        AppUserInfo userInfo = userService.getUserByOrderId(orderId);
        String customerId = userInfo.getCustomer_id();
        Map map2= appImageService.addImageByType(customerId,orderId,"5",nameUrl);
        //修改未提交订单状态
        appOrderService.updateOrderStateBeforeSubmit(orderId,"14");
        if(!(boolean)map2.get("flag")){
            resultVO.setErrorMsg(VOConst.FAIL,map2.get("msg").toString());
        }
        resultVO.setRetData(map2);
        return resultVO;
    }
    /**
     * 保存客户图片资料
     * @param orderId
     * @param firstUrl
     * @param secondUrl
     * @param  urlType yyzl 影像资料  fhtp 发货图片
     * @return
     * @throws Exception
     */
    @RequestMapping("/addCustomerImage")
    @ResponseBody
    public ResultVO addCustomerImage(String orderId,String urlType,String firstUrl, String secondUrl) throws  Exception{
        ResultVO resultVO = new ResultVO();
        AppUserInfo userInfo = userService.getUserByOrderId(orderId);
        String customerId = userInfo.getCustomer_id();
        Map map = appImageService.addCustomerImage(customerId,urlType,orderId,firstUrl,secondUrl);
        if(!(boolean)map.get("flag")){
            resultVO.setErrorMsg(VOConst.FAIL,map.get("msg").toString());
            return resultVO;
        }
        map.remove("flag");
        resultVO.setRetData(map);
        return resultVO;
    }
    /**
     * 查看影像资料，手签资料，发货照片
     * @param orderId
     * @type  类型 包括影像资料，手签
     */
    @RequestMapping("/getImageData")
    @ResponseBody
    public ResultVO getImageData (String orderId,String type) throws Exception{
        ResultVO resultVO =new ResultVO();
        Map map =appImageService.showImageData(orderId,type);
        if(map.size()>0){
            resultVO.setRetData(map);
        }
        return resultVO;
    }
}
