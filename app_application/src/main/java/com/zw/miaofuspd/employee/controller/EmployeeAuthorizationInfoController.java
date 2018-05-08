package com.zw.miaofuspd.employee.controller;

import com.alibaba.fastjson.JSONObject;
import com.zw.miaofuspd.facade.dict.service.ISystemDictService;
import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.user.service.IAppIdentityService;
import com.zw.miaofuspd.facade.user.service.IUserService;
import com.zw.util.UploadFileUtil;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import com.zw.web.base.vo.VOConst;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/20.
 */
@Controller
@RequestMapping("/employeeAuthorization")
public class EmployeeAuthorizationInfoController extends AbsBaseController{
    @Autowired
    private ISystemDictService systemDictServiceImpl;
    @Autowired
    private IAppIdentityService iAppIdentityService;
    @Autowired
    private IUserService userService;
    /**
     * 秒付金服保存身份认证信息
     *
     * @param data Zcard_src_base64 身份证正面base64
     *             Fcard_src_base64 身份证反面base64
     *             realname 姓名
     *             sex_name 性别
     *             card 身份证
     *             card_register_address 身份证地址
     *             orderId 订单id
     * @return
     * @throws Exception
     */
    @RequestMapping("/saveIdentityInfo")
    @ResponseBody
    public ResultVO saveIdentityInfo(String data) throws Exception {
        Map map = (Map) JSONObject.parse(data);
        String orderId = map.get("orderId").toString();
        AppUserInfo userInfo = userService.getUserByOrderId(orderId);
        ResultVO resultVO = new ResultVO();
        String host = systemDictServiceImpl.getInfo("rule.url");//三码地址
        if (StringUtils.isEmpty(host)) {
            host = "http://192.168.102.103:9999";
        }
        map.put("customer_id", userInfo.getCustomer_id());
        map.put("tel", userInfo.getTel());
        Map sanMaMap = iAppIdentityService.sanMaIdentity(host,map);//三码验证接口
        if (! (boolean)sanMaMap.get("flag")) {
            resultVO.setErrorMsg(VOConst.FAIL, sanMaMap.get("msg").toString());
            resultVO.setRetData(sanMaMap);
            return resultVO;
        }
        map.put("userId", userInfo.getCustomer_id());
        String Zcard_src_base64 = (String) map.get("Zcard_src_base64");
        String Fcard_src_base64 = (String) map.get("Fcard_src_base64");
        if(!".jpg".equals(Zcard_src_base64.substring(Zcard_src_base64.length()-4,Zcard_src_base64.length()))){
            try{
                String imgPath = getHttpSession().getServletContext().getRealPath("/");
                //生成新的文件名
                //获得头像路劲
                String bucket = systemDictServiceImpl.getInfo("oss.bucket");
                String endpoint = systemDictServiceImpl.getInfo("oss.endpoint");
                String accessKeyId = systemDictServiceImpl.getInfo("oss.accessKeyId");
                String accessKeySecret = systemDictServiceImpl.getInfo("oss.accessKeySecret");
                //返回的base64身份证对应在阿里云的地址，是jpg类型的文件
                Zcard_src_base64= UploadFileUtil.uploadFaceToOSS(Zcard_src_base64,imgPath,bucket,endpoint,accessKeyId,accessKeySecret);
                map.put("Zcard_src_base64",Zcard_src_base64);
            }catch (Exception e){
            }
        }
        if(!".jpg".equals(Fcard_src_base64.substring(Fcard_src_base64.length()-4,Fcard_src_base64.length()))){
            try{
                Map srcMap = new HashMap();
                srcMap.put("Fcard_src_base64",Fcard_src_base64);
                String imgPath = getHttpSession().getServletContext().getRealPath("/");
                //生成新的文件名
                //获得头像路劲
                String bucket = systemDictServiceImpl.getInfo("oss.bucket");
                String endpoint = systemDictServiceImpl.getInfo("oss.endpoint");
                String accessKeyId = systemDictServiceImpl.getInfo("oss.accessKeyId");
                String accessKeySecret = systemDictServiceImpl.getInfo("oss.accessKeySecret");
                Fcard_src_base64 = UploadFileUtil.uploadFaceToOSS(Fcard_src_base64,imgPath,bucket,endpoint,accessKeyId,accessKeySecret);
                map.put("Fcard_src_base64",Fcard_src_base64);
            }catch (Exception e){
            }
        }
        map.put("SOURCE","1");//区分是哪个app(1:办单员端,0:客户端)调用保存信息接口,如果办单员端,就是在保存的时候做三码验证,如果是客户端就不做
        Map map1 = iAppIdentityService.saveIdentityInfo(map);
        resultVO.setRetData(map1.get("customer_id"));
        return resultVO;
    }
    /**
     * 保存人脸识别完成度
     *@param faceSrc 人脸图片
     * @param orderId 订单iD
     * @return
     * @throws Exception
     */
    @RequestMapping("/saveFaceComplete")
    @ResponseBody
    public ResultVO saveFaceComplete(String faceSrc,String orderId) throws Exception {
        ResultVO resultVO = new ResultVO();
        AppUserInfo userInfo = userService.getUserByOrderId(orderId);
        String face_src = "";
        String face_state_spd = "1";
        try {
            String imgPath = getHttpSession().getServletContext().getRealPath("");
            //生成新的文件名
            //获得头像路劲
            imgPath = imgPath + "/";
            String bucket = systemDictServiceImpl.getInfo("oss.bucket");
            String endpoint = systemDictServiceImpl.getInfo("oss.endpoint");
            String accessKeyId = systemDictServiceImpl.getInfo("oss.accessKeyId");
            String accessKeySecret = systemDictServiceImpl.getInfo("oss.accessKeySecret");
            faceSrc = "data:image/png;base64," + faceSrc;
            face_src = UploadFileUtil.uploadFaceToOSS(faceSrc, imgPath, bucket, endpoint, accessKeyId, accessKeySecret);
        } catch (Exception e) {
        }
        iAppIdentityService.saveFaceComplete(userInfo.getCustomer_id(), face_state_spd, face_src);
        resultVO.setRetMsg("保存成功");
        return resultVO;
    }

    /**
     * 淘宝登录授权获取短信验证码接口
     * @param taobaoAccount 淘宝账号
     * @param password 验证码
     * @param orderId 订单id
     * @return
     * @throws Exception
     */
    @RequestMapping("/taobaoGetSmsCode")
    @ResponseBody
    public ResultVO taobaoGetSmsCode(String taobaoAccount,String password,String orderId) throws Exception{
        ResultVO resultVO = new ResultVO();
        AppUserInfo userInfo = userService.getUserByOrderId(orderId);
        Map map = iAppIdentityService.taobaoGetSmsCode(taobaoAccount,password,userInfo);
        if(!(boolean)map.get("flag")){
            resultVO.setErrorMsg(VOConst.FAIL,map.get("msg").toString());
        }
        resultVO.setRetMsg(map.get("msg").toString());
        resultVO.setRetData(map);
        return resultVO;
    }
    /**
     * 淘宝登录授权检查验证码接口
     * @param sid 上一次任务爬取id
     * @param orderId
     * @return
     * @throws Exception
     */
    @RequestMapping("/taobaoRefreshCode")
    @ResponseBody
    public ResultVO taobaoRefreshCode(String sid,String orderId) throws Exception{
        ResultVO resultVO = new ResultVO();
        AppUserInfo userInfo = userService.getUserByOrderId(orderId);
        Map map = iAppIdentityService.taobaoRefreshCode(sid,userInfo);
        if(!(boolean)map.get("flag")){
            resultVO.setErrorMsg(VOConst.FAIL,map.get("msg").toString());
        }
        resultVO.setRetMsg(map.get("msg").toString());
        resultVO.setRetData(map);
        return resultVO;
    }
    /**
     * 淘宝登录授权检查验证码接口
     * @param smsCode
     * @param sid
     * @param orderId
     * @return
     * @throws Exception
     */
    @RequestMapping("/taobaoCheckSmsCode")
    @ResponseBody
    public ResultVO taobaoCheckSmsCode(String smsCode,String sid,String orderId) throws Exception{
        ResultVO resultVO = new ResultVO();
        AppUserInfo userInfo = userService.getUserByOrderId(orderId);
        Map map = iAppIdentityService.taobaoCheckSmsCode(smsCode,sid,userInfo);
        if(!(boolean)map.get("flag")){
            resultVO.setErrorMsg(VOConst.FAIL,map.get("msg").toString());
        }
        resultVO.setRetMsg(map.get("msg").toString());
        resultVO.setRetData(map);
        return resultVO;
    }
    /**
     * 淘宝登录授权查询授权状态接口,拉取报告
     * @param taobaoAccount
     * @param sid
     * @param orderId
     * @return
     * @throws Exception
     */
    @RequestMapping("/taobaoQuery")
    @ResponseBody
    public ResultVO taobaoQuery(String taobaoAccount,String sid,String orderId) throws Exception{
        ResultVO resultVO = new ResultVO();
        AppUserInfo userInfo = userService.getUserByOrderId(orderId);
        Map map = iAppIdentityService.taobaoQuery(taobaoAccount,sid,userInfo);
        if(!(boolean)map.get("flag")){
            resultVO.setErrorMsg(VOConst.FAIL,map.get("msg").toString());
        }
        resultVO.setRetMsg(map.get("msg").toString());
        resultVO.setRetData(map);
        return resultVO;
    }
    /**
     * 淘宝登录授权检查二维码状态接口
     * @param sid
     * @param orderId
     * @return
     * @throws Exception
     */
    @RequestMapping("/qrCode")
    @ResponseBody
    public ResultVO tabaoCheckQrCode(String sid,String orderId) throws Exception{
        ResultVO resultVO = new ResultVO();
        AppUserInfo userInfo = userService.getUserByOrderId(orderId);
        Map map = iAppIdentityService.tabaoCheckQrCode(sid,userInfo);
        if(!(boolean)map.get("flag")){
            resultVO.setErrorMsg(VOConst.FAIL,map.get("msg").toString());
        }
        resultVO.setRetMsg(map.get("msg").toString());
        resultVO.setRetData(map);
        return resultVO;
    }
}
