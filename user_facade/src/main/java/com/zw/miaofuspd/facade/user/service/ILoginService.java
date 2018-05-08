package com.zw.miaofuspd.facade.user.service;

import java.util.List;
import java.util.Map;

/**
 * Created by wangmin on 2017/2/20.
 */
public interface ILoginService {
    /**
     * 登录
     * @param username,password,registration_id
     * @return
     */
    Map login(String username, String password, String registration_id, String black_box, String type, String ip_address, String deviceCode) throws Exception;

    /**
     * @param phone 手机号,办单员工号
     * @param password  密码
     * @return
     * @throws Exception
     */
    Map loginEmployee(String phone, String password) throws Exception;
    /**
     * 登录
     * @param username,password,registration_id
     * @return
     */
    Map wechatLogin(String username, String password) throws Exception;

    /**
     * 获取token信息
     * @return
     * @throws Exception
     */
    List getAccessToken() throws Exception;

    /**
     * 秒付退出
     * @param user_id
     * @throws Exception
     */
    void logOut(String user_id) throws Exception;
}
