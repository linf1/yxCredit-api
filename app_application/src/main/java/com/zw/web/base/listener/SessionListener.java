package com.zw.web.base.listener;

import com.zw.miaofuspd.facade.user.service.ISessionListenerService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * <strong>Title : <br>
 * </strong> <strong>Description : </strong>@类注释说明写在此处@<br>
 * <strong>Create on : 2017年03月10日<br>
 * </strong>
 * <p>
 * <strong>Copyright (C) zw.<br>
 * </strong>
 * <p>
 *
 * @author department:技术开发部 <br>
 *         username:zh-pc <br>
 *         email: <br>
 * @version <strong>zw有限公司-运营平台</strong><br>
 *          <br>
 *          <strong>修改历史:</strong><br>
 *          修改人 修改日期 修改描述<br>
 *          -------------------------------------------<br>
 *          <br>
 *          <br>
 */
public class SessionListener implements HttpSessionListener {
    @Autowired
    ISessionListenerService sessionListenerService;

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        //System.out.println("");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent){} /*{
        /*HttpSession httpSession = httpSessionEvent.getSession();
        String appId = (String) httpSession.getAttribute(AppConstant.APP_SYS_ID_KEY);
        if (appId != null && AppConstant.APP_SYS_ID_VALUE.equals(appId)) {
            //得到session被销毁的用户信息
            AppUserInfo appUserInfo = (AppUserInfo) httpSession.getAttribute(AppConstant.APP_USER_INFO);
            if (appUserInfo != null) {
                try {
                    sessionListenerService.setUserState(appUserInfo.getId(), "1");//用户退出时将用户的状态改为未登录
                } catch (Exception e) {
                    TraceLoggerUtil.info(e.getMessage());
                }
            }
        }
    }*/
}
