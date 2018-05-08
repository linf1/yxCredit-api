package com.zw.miaofuspd.user.service;

import com.zw.miaofuspd.facade.user.service.ISessionListenerService;
import com.zw.service.base.AbsServiceBase;
import org.springframework.stereotype.Service;

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
@Service
public class SessionListenerServiceImpl extends AbsServiceBase implements ISessionListenerService {
  /*
    * 用户登录状态设置
    * @param id     用户id
    * @param state  登录状态
    * @return boolean
    * */
    @Override
    public void setUserState(String id, String state) throws Exception {
        String sql="update app_user set state='"+state+"',registration_id = '' where id='"+id+"'";//更改用户状态
        sunbmpDaoSupport.exeSql(sql);
    }
}
