package com.zw.miaofuspd.authentication.controller;

import com.alibaba.fastjson.JSONObject;
import com.zw.app.util.AppConstant;
import com.zw.miaofuspd.facade.dict.service.ISystemDictService;
import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.entity.BranchInfo;
import com.zw.miaofuspd.facade.order.service.AppOrderService;
import com.zw.miaofuspd.facade.personal.service.AppUserService;
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

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Administrator on 2017/11/24 0024.
 */
@Controller
@RequestMapping("/authorization")
public class AuthenticationController extends AbsBaseController {
    @Autowired
    private IAppIdentityService iAppIdentityService;
    @Autowired
    private ISystemDictService systemDictServiceImpl;
    @Autowired
    private AppUserService appUserServiceImpl;
    @Autowired
    private IUserService userService;
    @Autowired
    private AppOrderService appOrderService;
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
        ResultVO resultVO = new ResultVO();
        String type = map.get("type").toString();
        AppUserInfo userInfo = (AppUserInfo) getHttpSession().getAttribute(AppConstant.APP_USER_INFO);
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
        map.put("userId", userInfo.getId());
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
                if("AND".equals(type)){
                    Zcard_src_base64 = Zcard_src_base64.replace(" ","+");
                }
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
                if("AND".equals(type)){
                    Fcard_src_base64 = Fcard_src_base64.replace(" ","+");
                }
                Fcard_src_base64 = UploadFileUtil.uploadFaceToOSS(Fcard_src_base64,imgPath,bucket,endpoint,accessKeyId,accessKeySecret);
                map.put("Fcard_src_base64",Fcard_src_base64);
            }catch (Exception e){
            }
        }
        map.put("SOURCE","0");//区分是哪个app(1:办单员端,0:客户端)调用保存信息接口,如果办单员端,就是在保存的时候做三码验证,如果是客户端就不做
        Map map1 = iAppIdentityService.saveIdentityInfo(map);
        userInfo.setCard(map.get("card").toString());
        userInfo.setSex_name(map.get("sex_name").toString());
        userInfo.setName(map.get("realname").toString());
        getRequest().getSession().setAttribute(AppConstant.APP_USER_INFO, userInfo);//将empId放到session中
        resultVO.setRetData(map1.get("customer_id"));
        return resultVO;
    }

    /**
     * 秒付金服身份认证信息回显
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/getIdentityInfoByCustId")
    @ResponseBody
    public ResultVO getIdentityInfoByCustomerId() throws Exception {
        ResultVO resultVO = new ResultVO();
        AppUserInfo userInfo = (AppUserInfo) getHttpSession().getAttribute(AppConstant.APP_USER_INFO);
        Map map = iAppIdentityService.getIdentityInfoByCustomerId(userInfo.getCustomer_id());
        resultVO.setRetData(map);
        return resultVO;
    }

    /**
     * 秒付金服-商品贷获取信用认证状态
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/getIdentityState")
    @ResponseBody
    public ResultVO getIdentityState() throws Exception {
        ResultVO resultVO = new ResultVO();
        AppUserInfo userInfo = (AppUserInfo) getHttpSession().getAttribute(AppConstant.APP_USER_INFO);
        Map map = iAppIdentityService.getIdentityState(userInfo.getCustomer_id());
        resultVO.setRetData(map);
        return resultVO;
    }

    /**
     * 保存人脸识别完成度
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/saveFaceComplete")
    @ResponseBody
    public ResultVO saveFaceComplete(String faceSrc) throws Exception {
        ResultVO resultVO = new ResultVO();
        AppUserInfo userInfo = (AppUserInfo) this.getHttpSession().getAttribute(AppConstant.APP_USER_INFO);
        String cusId = userInfo.getCustomer_id();
        faceSrc=faceSrc.replaceAll(" ","+");//前端传过来，会把“+”丢失为“”（空格）
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
        iAppIdentityService.saveFaceComplete(cusId, face_state_spd, face_src);
        resultVO.setRetMsg("保存成功");
        return resultVO;
    }

    /**
     * 秒付金服-聚立信短信验证接口
     * @return
     * @throws Exception
     *
     */
    @RequestMapping("/jxlCheckSmsCode")
    @ResponseBody
    public ResultVO jxlCheckSmsCode(String tel, String smsCode, String password, String website, String token,String orderId) throws Exception {
        ResultVO resultVO = new ResultVO();
        AppUserInfo userInfo = userService.getUserByOrderId(orderId);
        Map orderMap = appOrderService.getOrderById(orderId);
        //根据业务员获取对应的部门，递归获取分公司以及总公司
        String empId=orderMap.get("empId").toString();
        Map saleMap=appOrderService.getSaleInfo(empId);
        String branchId=saleMap.get("branch").toString();
        BranchInfo branchInfo=appOrderService.getGongSiName(appOrderService.getBranchInfo(branchId));
        Map<String , Object>  smsMap = new HashMap();
        smsMap.put("password",password);
        smsMap.put("captcha",smsCode);
        smsMap.put("phone",tel);
        smsMap.put("website",website);
        smsMap.put("token",token);
        smsMap.put("companyName",branchInfo.getDeptName());
        smsMap.put("companyId",branchInfo.getId());
        smsMap.put("busType","2");
        smsMap.put("pid", UUID.randomUUID().toString());
        Map map = iAppIdentityService.jxlCheckSmsCode(smsMap,userInfo);
        boolean b = (boolean)map.get("flag");
        if(!b){
            resultVO.setErrorMsg(VOConst.FAIL,map.get("msg").toString());
        }
        resultVO.setRetData(map);
        return resultVO;
    }

    /**
     * 秒付金服-聚立信获取手机运营商短信接口
     * type smsCode 为空的话为获取短信
     * 不为空，则为验证短信
     * @return
     * @throws Exception
     */
    @RequestMapping("/jxlGetSmsCode")
    @ResponseBody
    public ResultVO jxlGetSmsCode(String tel, String password,String orderId) throws Exception {
        ResultVO resultVO = new ResultVO();
        AppUserInfo userInfo = userService.getUserByOrderId(orderId);
        Map orderMap = appOrderService.getOrderById(orderId);
        //根据业务员获取对应的部门，递归获取分公司以及总公司
        String empId=orderMap.get("empId").toString();
        Map saleMap=appOrderService.getSaleInfo(empId);
        String branchId=saleMap.get("branch").toString();
        BranchInfo branchInfo=appOrderService.getGongSiName(appOrderService.getBranchInfo(branchId));
        Map<String , Object>  smsMap = new HashMap();
        smsMap.put("password",password);
        smsMap.put("captcha","");
        smsMap.put("phone",tel);
        smsMap.put("companyName",branchInfo.getDeptName());
        smsMap.put("companyId",branchInfo.getId());
        smsMap.put("busType","2");
        smsMap.put("pid", UUID.randomUUID().toString());
        Map  map  = iAppIdentityService.jxlGetSmsCode(smsMap,userInfo);
        boolean b = (boolean)map.get("flag");
        if(!b){
            resultVO.setErrorMsg(VOConst.FAIL,map.get("msg").toString());
        }
        resultVO.setRetData(map);
        return resultVO;
    }

    /**
     *同盾获取手机运营商短信验证码，图形验证码
     * @param password
     * @return
     * @throws Exception
     */
    @RequestMapping("/tongdunGetSmsCode")
    @ResponseBody
    public ResultVO tongdunGetSmsCode(String password, String phone) throws Exception {
        ResultVO resultVO = new ResultVO();
        AppUserInfo userInfo = (AppUserInfo) getHttpSession().getAttribute(AppConstant.APP_USER_INFO);
        String host = systemDictServiceImpl.getInfo("rule.url");
        Map  map  = iAppIdentityService.tongdunGetSmsCode(password,phone,host,userInfo);
        resultVO.setRetData(map);
        return resultVO;
    }
    /**
     *同盾验证手机运营商短信验证码，图形验证码
     * @param password
     * @return
     * @throws Exception
     */
    @RequestMapping("/tongdunCheckSmsCode")
    @ResponseBody
    public ResultVO tongdunCheckSmsCode(String password, String taskId, String taskStage, String smsCode, String authCode, String phone) throws Exception {
        ResultVO resultVO = new ResultVO();
        AppUserInfo userInfo = (AppUserInfo) getHttpSession().getAttribute(AppConstant.APP_USER_INFO);
        String host = systemDictServiceImpl.getInfo("rule.url");
        Map  map  = iAppIdentityService.tongdunCheckSmsCode(password,taskId, taskStage, smsCode, authCode, phone, host, userInfo);
        resultVO.setRetData(map);
        return resultVO;
    }

    /**
     * 调用同盾获取手机运营运营商短信重发接口
     * @param taskId
     * @return
     * @throws Exception
     */
    @RequestMapping("/tongdunRetrySmsCode")
    @ResponseBody
    public ResultVO tongdunRetrySmsCode(String taskId) throws Exception {
        ResultVO resultVO = new ResultVO();
        AppUserInfo userInfo = (AppUserInfo) getHttpSession().getAttribute(AppConstant.APP_USER_INFO);
        String host = systemDictServiceImpl.getInfo("rule.url");
        String  data  = iAppIdentityService.tongdunRetrySmsCode(taskId, host);
        JSONObject jsonObject = JSONObject.parseObject(data);
        resultVO.setRetData(jsonObject);
        return resultVO;
    }
}


