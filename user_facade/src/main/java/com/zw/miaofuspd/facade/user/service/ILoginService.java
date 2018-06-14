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
     * 获取token信息
     * @return
     * @throws Exception
     */
    List getAccessToken() throws Exception;


    /**
     * 碧友信
     * @param phone 电话
     * @param type 手机类型
     * @param ipAddress IP地址
     * @param deviceCode 手机唯一标识
     * @return 返回的用户信息
     * @throws Exception
     */
    Map login(String phone, String type, String ipAddress, String deviceCode) throws Exception;

}
