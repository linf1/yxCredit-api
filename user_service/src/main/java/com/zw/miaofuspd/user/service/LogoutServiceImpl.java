package com.zw.miaofuspd.user.service;

import com.zw.miaofuspd.facade.user.service.ILogoutService;
import com.zw.service.base.AbsServiceBase;
import org.springframework.stereotype.Service;

@Service
public class LogoutServiceImpl extends AbsServiceBase implements ILogoutService {
    @Override
    public void logOut(String user_id) throws Exception {
        String sql = "update app_user set token = '',token_time = '',state = '1',registration_id = '' where id = '"+user_id+"'";
        sunbmpDaoSupport.exeSql(sql);
    }

    @Override
    public void logoutEmployee(String user_id) throws Exception {
        String sql = "update mag_salesman set state = '1' where id = '"+user_id+"'";
        sunbmpDaoSupport.exeSql(sql);
    }
}
