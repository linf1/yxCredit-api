package com.zw.miaofuspd.employee.controller;

import com.zw.miaofuspd.facade.myset.service.EmployeeModifyPswService;
import com.zw.web.base.vo.ResultVO;
import com.zw.web.base.vo.VOConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/13.
 */
@Controller
@RequestMapping("/modify")
@ResponseBody
public class EmployeeUpdatePswController {
    /**
     * 修改办单员密码
     * @param phone 手机号
     * @param oldPassword  旧密码
     * @param newPassword  新密码
     * @throws Exception
     */
    @Autowired
    EmployeeModifyPswService salesManModifyPswServiceImpl;
    @RequestMapping("/modifyPwd")
    public ResultVO appUpdatePsw(String phone ,String oldPassword, String newPassword)throws Exception{
        ResultVO resultVO = new ResultVO();
        Map map = new HashMap();
        map.put("phone",phone);
        map.put("oldPassword",oldPassword);
        map.put("newPassword",newPassword);
        Map outMap=salesManModifyPswServiceImpl.updatePw(map);
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
