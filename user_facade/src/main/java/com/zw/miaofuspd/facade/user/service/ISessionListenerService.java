package com.zw.miaofuspd.facade.user.service;

/******************************************************
 *Copyrights @ 2017，zhiwang  Co., Ltd.
 *         项目名称 消费金融
 *All rights reserved.
 *
 *Filename：
 *		用户session处理
 *Description：
 *		设置用户的登录状态
 *Author:
 *		沈华陶
 *Finished：
 *		2017年3月13日
 ********************************************************/
public interface ISessionListenerService {
    /*
    * 用户登录状态设置
    * @param id     用户id
    * @param state  登录状态
    * @return boolean
    * */
    public void setUserState(String id, String state) throws Exception;

}
