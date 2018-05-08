package com.zw.miaofuspd.filter;

import com.base.util.TokenUtil;
import com.zw.app.util.AppConstant;
import com.zw.miaofuspd.facade.dict.service.ISystemDictService;
import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.user.service.IUserService;
import com.zw.web.base.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/4 0004.
 */
public class TokenFilter implements Filter{
    @Autowired
    ISystemDictService systemDictService;
    @Autowired
    IUserService userService;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext sc = filterConfig.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);

        if(applicationContext != null && applicationContext.getBean("userServiceImpl") != null && userService == null)
            userService = (IUserService) applicationContext.getBean("userServiceImpl");
        if(applicationContext != null && applicationContext.getBean("systemDictServiceImpl") != null && systemDictService == null)
            systemDictService = (ISystemDictService) applicationContext.getBean("systemDictServiceImpl");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException{
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        String token = request.getHeader("token");
        /**上传头像处后跟的的参数token**/
        String strToken=request.getParameter("token");
        if (token==null){
            if(strToken!=null){
                token=strToken;
            }
        }
        ServletContext context = request.getSession().getServletContext();
        Map map = (Map)context.getAttribute("token");//从ServletContext中拿到所有用户的token的集合
        Map tokenMap = (Map)map.get(token);//通过key获取用户的token
        ResultVO vo = new ResultVO();
        if(tokenMap == null){//判断token是否存在
            response.setHeader("tokenstatus", "timeout");
            return;
        }
        try {
            String timeOut= systemDictService.getInfo("token.timeOut");
            if(TokenUtil.isTimeOut(tokenMap,timeOut)){//判断token是否过期
                map.remove(token);
                response.setHeader("tokenstatus", "timeout");
                return;
            }
            AppUserInfo userInfo = (AppUserInfo)request.getSession().getAttribute(AppConstant.APP_USER_INFO);
            if(userInfo == null){
                userInfo = (AppUserInfo)tokenMap.get(AppConstant.APP_USER_INFO);
                request.getSession().setAttribute(AppConstant.APP_USER_INFO,userInfo);
            }
            if(userInfo == null){//token不过期session过期的情况下重新获取session
                Map map1 = userService.getUserByToken(token);
                userInfo = (AppUserInfo) map1.get("userInfo");
                request.getSession().setAttribute(AppConstant.APP_USER_INFO,userInfo);
                tokenMap.put(AppConstant.APP_USER_INFO,userInfo);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
