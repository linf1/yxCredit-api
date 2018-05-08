package com.zw.miaofuspd.employee.controller;

import com.zw.app.util.AppConstant;
import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.user.service.ILogoutService;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2017/12/12 0012.
 */
@Controller
@RequestMapping("/employeeLogout")
public class EmployeeLogoutController extends AbsBaseController {
    @Autowired
    ILogoutService logoutService;
    /**
     * 办单员登出功能;
     * @return resultVO
     * @throws Exception
     */
    @RequestMapping("/logout")
    @ResponseBody
    public ResultVO logoutEmployee() throws Exception{
        AppUserInfo userInfo = (AppUserInfo)getRequest().getSession().getAttribute(AppConstant.APP_USER_INFO);
        logoutService.logoutEmployee(userInfo.getId());//将数据库状态置为退出状态
        ResultVO vo = new ResultVO();
        vo.setRetMsg("退出成功");
        return vo;
    }
}
