package com.zw.miaofuspd.facade.myset.service;

import java.util.Map;

/******************************************************
 *Copyrights @ 2017，zhiwang  Co., Ltd.
 *         项目名称 秒付金服
 *All rights reserved.
 *
 *Filename：
 *		文件名称  修改手机登录密码
 *Description：
 *		文件描述  修改手机登录密码
 ********************************************************/
public interface IAppEditPswService {

    /**
     * 修改手机登录密码;
     * @param map     参数包含：
     * id(必需) 用户ID；
     * oldPassword（必需）  老密码；
     * tel（必需）  手机号；
     * newPassword（必需）    新密码；
     * registration_id（必需）  推送ID；
     * @return Map  参数包含：
     * success（必需） true  成功；false   失败；
     * @throws Exception
     */
    Map updatePw(Map map) throws Exception;

}
