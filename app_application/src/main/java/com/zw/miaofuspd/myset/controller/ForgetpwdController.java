package com.zw.miaofuspd.myset.controller;

import com.zw.miaofuspd.facade.dict.service.ISystemDictService;
import com.zw.miaofuspd.facade.user.service.IAppResetPwService;
import com.zw.miaofuspd.facade.user.service.IRegisteredService;
import com.zw.miaofuspd.facade.user.service.ISmsService;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import com.zw.web.base.vo.VOConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2017/12/5.
 */
@Controller
@RequestMapping("/forget")
public class ForgetpwdController extends AbsBaseController{

    @Autowired
    private IAppResetPwService appResetPwServiceImpl;
    @Autowired
    private IRegisteredService iRegisteredService;
    @Autowired
    private ISystemDictService iSystemDictService;
    @Autowired
    private ISmsService iSmsService;
    /**
     * 找回用户密码
     * @param phone
     * @param registration_id
     * @param password
     * @return
     * @throws Exception
     */
    @RequestMapping("/forgetpwd")
    @ResponseBody
    public ResultVO forgetpwd(String phone,String registration_id, String password ) throws Exception{
        ResultVO resultVO = new ResultVO();
        Map map = appResetPwServiceImpl.alterPw(registration_id,password,phone);
        boolean flag = (boolean) map.get("success");
        if(!flag){
            resultVO.setErrorMsg(VOConst.FAIL,"修改失败");
            return resultVO;
        }
        resultVO.setRetMsg("修改成功");
        return resultVO;
    }

    /**
     * 忘记密码-获取短信验证码
     *
     * @param phone
     * @return
     * @throws Exception
     */
    @RequestMapping("/getSmsCode")
    @ResponseBody
    public Object getCode(String phone) throws Exception {
        ResultVO resultVO = new ResultVO();
        //判断手机号是否已经注册
        if (iRegisteredService.selectByTel(phone)) {
            resultVO.setErrorMsg(VOConst.FAIL, "该手机号码不存在");
            return resultVO;
        }
        Map inMap = new HashMap();
        inMap.put("tel", phone);
        inMap.put("type","1");//注册
        //发送验证码
        if (!iSmsService.insertSms(inMap)) {//调用发送短信验证码的接口服务
            resultVO.setErrorMsg(VOConst.FAIL, "验证码获取失败，请重新获取");
            return resultVO;
        }
        String errortime = iSystemDictService.getInfo("error_time.0");//获取验证码超时时间
        double time = Double.valueOf(errortime)/60;
        String errot_time = String.format("%.0f",time).toString();
        resultVO.setRetMsg("验证码已发送，" + errot_time + "分钟内输入有效");
        resultVO.setRetData(errot_time);
        return resultVO;
    }

    /**校验短信验证码*/
    @RequestMapping("/check")
    @ResponseBody
    public ResultVO check( String phone, String smsCode) throws Exception{
        ResultVO resultVO =new ResultVO();
        String errortime=iSystemDictService.getInfo("error_time.0");//获取验证码超时时间
        //先查询验证码和用户是否正确
        Map inMap=new HashMap();
        inMap.put("tel",phone);
        inMap.put("smsCode",smsCode);
        inMap.put("errortime",errortime);
        Map outMap=iSmsService.checkSms(inMap);
        if(!(Boolean)(outMap.get("flag"))){
            resultVO.setErrorMsg(VOConst.FAIL,(String)(outMap.get("msg")));
            return resultVO;
        }
        return resultVO;
    }
}
