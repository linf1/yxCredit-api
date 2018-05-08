package com.zw.miaofuspd.regist.controller;


import com.zw.miaofuspd.facade.dict.service.ISystemDictService;
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

@Controller
@RequestMapping("/reg")
public class RegistController extends AbsBaseController {
    @Autowired
    private IRegisteredService iRegisteredService;
    @Autowired
    private ISystemDictService iSystemDictService;
    @Autowired
    private ISmsService iSmsService;

    /******************************************************
     * 秒付商品贷注册
     * phone -手机号码
     * password -密码
     * registration_id -设备号
     * idcard -身份证号码
     * realname -真实姓名
     * black_box - 同盾唯一识别号
     * type - 注册类型
     * referenceId - 推荐人id
     ********************************************************/
    @RequestMapping("/register")
    @ResponseBody
    public ResultVO register(String phone, String black_box, String type, String password, String registration_id, String smsCode, String onlycode, String referenceId) throws Exception {
        ResultVO resultVO = new ResultVO();
        //判断手机号是否已经注册
        if (!iRegisteredService.selectByTel(phone)) {
            resultVO.setErrorMsg(VOConst.FAIL, "该号码已注册");
            return resultVO;
        }
//        //判断设备号是否被绑定过
//        if (!iRegisteredService.selectByOnlyCode(onlycode)) {
//            resultVO.setErrorMsg(VOConst.FAIL, "该设备已经绑定用户");
//            return resultVO;
//        }
        //检验验证码是否正确
        String errortime = iSystemDictService.getInfo("error_time.0");//获取验证码超时时间
        Map map = new HashMap();
        map.put("tel", phone);
        map.put("smsCode", smsCode);
        map.put("errortime", errortime);
        map.put("type", "0");
        Map outMap = iSmsService.checkSms(map);
        if (!(Boolean) (outMap.get("flag"))) {
            resultVO.setErrorMsg(VOConst.FAIL, (String) (outMap.get("msg")));
            return resultVO;
        }
        //执行插入到用户表操作
        Map inMap = new HashMap();
        inMap.put("tel", phone);
        inMap.put("password", password);
        inMap.put("registration_id", registration_id);
        inMap.put("onlyCode", onlycode);
        inMap.put("smsCode", smsCode);
        inMap.put("black_box", black_box);
        inMap.put("type", type);
        if (!referenceId.equals("")){
            inMap.put("referenceId",referenceId);
        }
        iRegisteredService.register(inMap);
        resultVO.setRetMsg("注册成功");
        return resultVO;
    }
    @RequestMapping("/shareReg")
    @ResponseBody
    public ResultVO shareReg(String phone, String black_box, String type, String password, String registration_id, String smsCode, String onlycode, String referenceId) throws Exception {
        ResultVO resultVO = new ResultVO();
        //判断手机号是否已经注册
        if (!iRegisteredService.selectByTel(phone)) {
            resultVO.setErrorMsg(VOConst.FAIL, "该号码已注册");
            return resultVO;
        }
        //检验验证码是否正确
        String errortime = iSystemDictService.getInfo("error_time.0");//获取验证码超时时间
        Map map = new HashMap();
        map.put("tel", phone);
        map.put("smsCode", smsCode);
        map.put("errortime", errortime);
        map.put("type", "0");
        Map outMap = iSmsService.checkSms(map);
        if (!(Boolean) (outMap.get("flag"))) {
            resultVO.setErrorMsg(VOConst.FAIL, (String) (outMap.get("msg")));
            return resultVO;
        }
        //执行插入到用户表操作
        Map inMap = new HashMap();
        inMap.put("tel", phone);
        inMap.put("password", password);
        inMap.put("registration_id", registration_id);
        inMap.put("onlyCode", onlycode);
        inMap.put("smsCode", smsCode);
        inMap.put("black_box", black_box);
        inMap.put("type", type);
        if (!referenceId.equals("")){
            inMap.put("referenceId",referenceId);
        }
        iRegisteredService.register(inMap);
        resultVO.setRetMsg("注册成功");
        return resultVO;
    }
    /**
     * 获取短信验证码
     *
     * @param phone
     * @return
     * @throws Exception
     */
    @RequestMapping("/getSmsCode")
    @ResponseBody
    public Object getCode(String phone) throws Exception {
        ResultVO resultVO = new ResultVO();
        String tempId = null;
        //判断手机号是否已经注册
        if (!iRegisteredService.selectByTel(phone)) {
            resultVO.setErrorMsg(VOConst.FAIL, "该号码已注册");
            return resultVO;
        }
        Map inMap = new HashMap();
        inMap.put("tel", phone);
        inMap.put("type","0");//注册
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
    public ResultVO check(String phone, String smsCode) throws Exception{
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

    /**获取注册条款*/
    @RequestMapping("/getRegClause")
    @ResponseBody
    public ResultVO getRegClause(String type) throws Exception{
        Map map = iRegisteredService.getRegClause(type);
        return this.createResultVO(map);
    }
    /**获取关于我们*/
    @RequestMapping("/getAbountUs")
    @ResponseBody
    public ResultVO getAbountUs(String type) throws Exception{
        Map map = iRegisteredService.getRegClause(type);
        return this.createResultVO(map);
    }
}
