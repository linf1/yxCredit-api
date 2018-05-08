package com.zw.web.base.interceptor;

import com.zw.app.util.AppConstant;
import com.zw.web.base.vo.ResultVO;
import com.zw.web.base.vo.VOConst;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class
LoginInterceptor extends HandlerInterceptorAdapter {
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        Object object = request.getSession().getAttribute(AppConstant.APP_USER_INFO);
        if (null == object) {
            if (!((request
                    .getHeader("requestType") != null && request
                    .getHeader("requestType").indexOf("app") > -1) ||request.getHeader("accept").indexOf("application/json") > -1 || (request
                    .getHeader("X-Requested-With") != null && request
                    .getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1))) {
                request.setAttribute("TIME_OUT_MSG","会话失效, 请重新登录!");
                request.getRequestDispatcher("/WEB-INF/page/app/login.jsp").forward(request, response);
            } else {
                response.setHeader("sessionstatus", "timeout");//在响应头设置session状态Re
                try (PrintWriter out = response.getWriter()) {
                    ResultVO resultVO = new ResultVO();
                    resultVO.setErrorMsg(VOConst.SESSION_TIMEOUT, "会话失效, 请重新登录!");
                    out.write(resultVO.toString());
                    out.flush();
                }
            }
            return false;
        }
        return true;
    }

}
