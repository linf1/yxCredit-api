package com.zw.miaofuspd.facade.personal.service;



import com.zw.miaofuspd.facade.entity.AppUserInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by guoqing on 2017/3/6.
 * App用户服务接口
 */
public interface AppUserService {
    /**
     * 上传用户头像接口
     * @param appUserInfo -用户信息（head_img:头像路劲，id：用户id）
     * */
    void uploadImg(AppUserInfo appUserInfo);

    List getImgInfo(AppUserInfo userInfo);

    void updateUrl(Map map, AppUserInfo userInfo);
    List getCooperation();
    /**
     * 获取用户基本信息，姓名，是否实名认证
     *
     */
    Map getCustomerInfo(String customerId) throws Exception;

    /**
     * 获取客户信息
     * @param customerId 客户id
     * @return 客户信息
     * @throws Exception 异常
     */
    Map findCustomerInfo(String customerId) throws Exception;

    /**获取身份证正反面的url
     * @param customerId
     * @return
     */
    Map getIdentityInfo(String customerId);

    /**
     * 获取开户信息
     * @param userInfo
     * @return
     */
    Map getAccountInfo(AppUserInfo userInfo);

    String getPhoneById(String id);
}
