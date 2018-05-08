package com.zw.miaofuspd.facade.user.service;


import com.zw.miaofuspd.facade.entity.AppUserInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/20.
 */
public interface IAppIdentityService {
    /**
     *秒付金服-现金贷三码认证接口
     * */
    Map sanMaIdentity(String host, Map map)throws Exception;
    /**
     * 获取身份信息
     * @param customerId
     * @return
     * @throws Exception
     */
    Map getIdentityInfoByCustomerId(String customerId) throws Exception;
    /**
     * 保存身份信息
     * @param map
     * @return
     */
    Map saveIdentityInfo(Map map) throws Exception;

    /**
     * 客户端获取客户信用认证状态
     * @param customerId
     * @return
     * @throws Exception
     */
    Map getIdentityState(String customerId) throws Exception;
    /**
     * 保存人脸识别状态
     * @param customerId
     * @param face_state_spd
     * @throws Exception
     */
    void saveFaceComplete(String customerId,String face_state_spd,String faceSrc) throws Exception;

    /**
     * 淘宝授权接口
     * @param taobaoAccount 淘宝账号
     * @param password 淘宝账号密码
     * @return
     * @throws Exception
     */
    Map taobaoGetSmsCode(String taobaoAccount,String password,AppUserInfo userInfo) throws Exception;
    Map taobaoCheckSmsCode(String smsCode,String sid,AppUserInfo userInfo) throws Exception;
    Map taobaoRefreshCode(String sid,AppUserInfo userInfo)throws Exception;
    Map taobaoQuery(String taobaoAccount,String sid,AppUserInfo userInfo) throws Exception;
    Map tabaoCheckQrCode(String sid,AppUserInfo userInfo) throws Exception;
    /**
     *聚信立手机运营商认证
     */
    Map jxlGetSmsCode(Map<String, Object> map, AppUserInfo userInfo) throws Exception;
    Map jxlCheckSmsCode(Map<String, Object> map, AppUserInfo userInfo) throws Exception;

    /**
     *同盾手机运营商认证
     */
    Map tongdunGetSmsCode(String password, String phone, String host, AppUserInfo userInfo) throws Exception;
    String tongdunRetrySmsCode(String taskId, String host) throws Exception;
    Map tongdunCheckSmsCode(String password, String taskId, String taskStage, String smsCode, String authCode, String phone, String host, AppUserInfo userInfo) throws Exception;
    /**
     * 魔蝎手机运营商认证
     */
    String mxPrepare(String host, String idNo, String name, String password, String phone, String userId) throws Exception;
    String mxLunxun(String host, String phone, String smsCode) throws Exception;
    String reGetCode(String host, String phone, String type) throws Exception;
}
