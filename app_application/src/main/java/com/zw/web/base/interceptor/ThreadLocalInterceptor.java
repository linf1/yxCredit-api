package com.zw.web.base.interceptor;

import com.base.util.ThreadLocalHelper;
import com.zw.app.util.AppConstant;
import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.service.dto.UserInfoDTO;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ThreadLocalInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        HttpSession httpSession = request.getSession();
        String appId = (String) httpSession.getAttribute(AppConstant.APP_SYS_ID_KEY);
        if (appId == null) {
            httpSession.setAttribute(AppConstant.APP_SYS_ID_KEY, AppConstant.APP_SYS_ID_VALUE);
        }
        AppUserInfo user = (AppUserInfo) httpSession.getAttribute(AppConstant.APP_USER_INFO);
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setHttpReqId(httpSession.getId() + ":" + System.currentTimeMillis());
        if (user != null) {
            userInfoDTO.setUserId(user.getTel());
        }
        ThreadLocalHelper.getMap().put(ThreadLocalHelper.USER_INFO_DTO, userInfoDTO);
        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        ThreadLocalHelper.getMap().clear();
    }

}
