package com.zw.miaofuspd.facade.user.service;

/**
 * Created by htshen on 2017/2/27 0027.
 */
public interface ILogoutService {
    void logOut(String user_id) throws Exception;
    void logoutEmployee(String user_id) throws Exception;

}
