package com.zw.miaofuspd.share.controller;

import com.zw.miaofuspd.facade.dict.service.IDictService;
import com.zw.miaofuspd.facade.dict.service.ISystemDictService;
import com.zw.miaofuspd.facade.share.service.IShareService;
import com.zw.miaofuspd.facade.user.service.IRegisteredService;
import com.zw.miaofuspd.facade.user.service.ISmsService;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import com.zw.web.base.vo.VOConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/31 0031.
 */
@Controller
public class ShareController extends AbsBaseController {
    @Autowired
    ISystemDictService systemDictService;
    @Autowired
    IShareService iShareService;
    @Autowired
    private ISmsService iSmsService;
    @Autowired
    ISystemDictService iSystemDictService;
    @Autowired
    private IRegisteredService iRegisteredService;
    @Autowired
    private IDictService iDictService ;
    @RequestMapping("/share")
    @ResponseBody
    public ModelAndView share() throws Exception {
        ModelAndView modelAndView = new ModelAndView("../../download/download");
        return modelAndView;
    }
    @RequestMapping("/url")
    @ResponseBody
    public ResultVO shareUrl() throws Exception {
        String iosUrl =  systemDictService.getInfo("downloadUrl.ios");
        String androidUrl =  systemDictService.getInfo("downloadUrl.android");
        Map map = new HashMap();
        map.put("iosUrl",iosUrl);
        map.put("androidUrl",androidUrl);
        ResultVO resultVO = new ResultVO();
        resultVO.setRetData(map);
        return resultVO;
    }

    @RequestMapping("/sharePromotion")
    @ResponseBody
    public ModelAndView sharePromotion(String code) throws Exception{
        ModelAndView modelAndView=new ModelAndView("../../generalize/generalizeRegister");
        Map map =iShareService.getShare(code);
        if("fail".equals(map.get("mag").toString())){
            modelAndView.addObject("MSG","没有获取到此推广渠道！");
            return modelAndView;
        }
        map.put("code",code);
        Map saveMap=iShareService.saveShareInfo(map);
        String id=saveMap.get("id").toString();
        modelAndView.addObject("id", id);
        return  modelAndView;
    }
    @RequestMapping("/updateShare")
    @ResponseBody
    public ResultVO saveShare(String id, String phone, String password, String smsCode, String referenceId, String blackBox)throws  Exception{
        ResultVO resultVO=new ResultVO();
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
        //获取用户初始额度
        String credit_pre_amount = iDictService.getDictCode("客户初始金额","初始金额");
        //执行插入到用户表操作
        Map inMap = new HashMap();
        inMap.put("tel", phone);
        inMap.put("password", password);
        inMap.put("smsCode", smsCode);
        inMap.put("credit_pre_amount",credit_pre_amount);
        if (!referenceId.equals("")){
            inMap.put("referenceId",referenceId);
        }
        inMap.put("type","WEB");
        inMap.put("blackBox",blackBox);
        inMap.put("shareId",id);
        iRegisteredService.register(inMap);
        resultVO.setRetMsg("注册成功");
        try {
            Map map2 = iShareService.updateInfo(id,phone);
            String msg = map2.get("msg").toString();
            if(map2!=null){
                resultVO.setRetMsg(msg);
            }else{
                resultVO.setErrorMsg(VOConst.FAIL,msg);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultVO;
    }
}
