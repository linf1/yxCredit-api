package com.zw.miaofuspd.employee.controller;

import com.zw.app.util.AppConstant;
import com.zw.miaofuspd.facade.dict.service.ISystemDictService;
import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.user.service.ILoginService;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import com.zw.web.base.vo.VOConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by Administrator on 2017/12/12 0012.
 */
@Controller
@RequestMapping("/employeeLogin")
public class EmployeeLoginController extends AbsBaseController {
    @Autowired
    ILoginService loginService;
    @Autowired
    private ISystemDictService systemDictServiceImpl;
    /**
     * 办单员登录
     *
     * @param phone 手机号
     * @param password 密码
     * @return
     * @throws Exception
     */
    @RequestMapping("/login")
    @ResponseBody
    public ResultVO loginEmployee(String phone, String password) throws Exception {
        //获取客户端的Ip地址
        ResultVO resultVO = new ResultVO();
        Map map = loginService.loginEmployee(phone, password);
        boolean flag = (boolean) map.get("success");
        String msg = (String) map.get("msg");
        //此种情况是登录失败的情况
        if(!flag){
            resultVO.setErrorMsg(VOConst.FAIL, msg);
            resultVO.setRetData(map);
        }
        //登录成功返回办单员的信息
        String host = systemDictServiceImpl.getInfo("appPhone.url");
        map.put("appHost",host);
        resultVO.setRetData(map);
        AppUserInfo user = new AppUserInfo();
        user.setId(map.get("id") + "");
        user.setTel(map.get("tel") + "");
        user.setName(map.get("realname") + "");
        user.setCard(map.get("idcard") + "");
        user.setSex(map.get("sex") + "");
        user.setSexName(map.get("sex_name") + "");
        getRequest().getSession().setAttribute(AppConstant.APP_USER_INFO, user);
        return resultVO;
    }
}
