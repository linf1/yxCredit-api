package com.zw.miaofuspd.logout.controller;


import com.base.util.TraceLoggerUtil;
import com.zw.app.util.AppConstant;
import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.user.service.ILogoutService;
import com.zw.web.base.AbsBaseController;
import com.zw.web.base.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import java.util.Map;

/******************************************************
 *Copyrights @ 2016，zhiwang  Co., Ltd.
 *         项目名称 秒付金服
 *All rights reserved.
 *
 *Filename：
 *		文件名称  app退出登录
 *Finished：
 *		2017年10月20日
 ********************************************************/
@Controller
@RequestMapping("/logout")
public class LogutController extends AbsBaseController {
    @Autowired
    ILogoutService logoutService;
    /**
     * 用户登出功能;
     * @return resultVO
     * @throws Exception
     */
    @RequestMapping(value="/logout")
    @ResponseBody
    public ResultVO logOut() throws Exception{
        AppUserInfo userInfo = (AppUserInfo)getRequest().getSession().getAttribute(AppConstant.APP_USER_INFO);
        String token = getRequest().getHeader("token");//得到要退出用户的token
        logoutService.logOut(userInfo.getId());//将数据库状态置为退出状态
        ServletContext servletContext = getRequest().getSession().getServletContext();
        Map map = (Map)servletContext.getAttribute("token");//从内存中拿到所用具有token的用户集合
        map.remove(token);//去除内存中的token
        getRequest().getSession().removeAttribute(AppConstant.APP_USER_INFO);
        ResultVO vo = new ResultVO();
        vo.setRetMsg("退出成功");
        return vo;
    }
}
