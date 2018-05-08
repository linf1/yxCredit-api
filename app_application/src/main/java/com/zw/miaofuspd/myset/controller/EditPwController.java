package com.zw.miaofuspd.myset.controller;

import com.zw.app.util.AppConstant;
import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.myset.service.IAppEditPswService;
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
 * Created by Administrator on 2017/10/20 0020.
 */
@Controller
@RequestMapping("/editPw")
public class EditPwController extends AbsBaseController {
    @Autowired
    IAppEditPswService appEditPswServiceImpl;
    /**
     * 修改用户密码
     * @throws Exception
     */
    @RequestMapping("/set")
    @ResponseBody
    public ResultVO appUpdatePsw(String oldPassword, String newPassword)throws Exception{
        AppUserInfo userInfo = (AppUserInfo)this.getRequest().getSession().getAttribute(AppConstant.APP_USER_INFO);
        Map map = new HashMap();
        map.put("id",userInfo.getId());
        map.put("tel",userInfo.getTel());
        map.put("oldPassword",oldPassword);
        map.put("newPassword",newPassword);
        map.put("registration_id",userInfo.getRegistration_id());
        ResultVO resultVO = new ResultVO();
        Map outMap = appEditPswServiceImpl.updatePw(map);
        boolean flag = (boolean) outMap.get("success");
        String msg = outMap.get("msg").toString();
        if(flag){
            resultVO.setRetMsg(msg);
        }else{
            resultVO.setErrorMsg(VOConst.FAIL,msg);
        }
        return  resultVO;
    }
}
