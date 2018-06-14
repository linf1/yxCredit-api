package com.zw.miaofuspd.user.service;

import com.base.util.DateUtils;
import com.base.util.GeneratePrimaryKeyUtils;
import com.zw.miaofuspd.facade.user.service.IRegisteredService;
import com.zw.service.base.AbsServiceBase;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * 秒付金服注册接口
 */
@Service("registeredServiceImpl")
public class RegisteredServiceImpl extends AbsServiceBase implements IRegisteredService {

    @Override
    public String insertUser(Map<String, Object> map) {
        String id = GeneratePrimaryKeyUtils.getUUIDKey();
        //手机号
        String tel = (String) map.get("tel");
        String registrationId = (String) map.get("registrationId");
        //将时间格式化
        String ceateTime =map.get("createTime") == null ? DateUtils.getDateString(new Date()) : (String)map.get("createTime");
        String onlyCodeSpd = map.get("onlyCode") == null ? "" : map.get("onlyCode").toString();
        String blackBox = "";
        String password = "";
        //注册渠道
        String type = map.get("type") == null ? "" : map.get("type").toString();
        //插入app_user表
        StringBuffer insql = new StringBuffer("INSERT INTO app_user (id,tel,black_box,type,passwd,state,creat_time,alter_time,registration_id,only_code_spd) VALUES('");
        insql.append(id + "','" + tel + "','" + blackBox + "','" + type + "','" + password + "','" + 1 + "','" + ceateTime + "','" + ceateTime + "','"+registrationId+"','"+onlyCodeSpd+"')");
        //插入mag_customer_person表
        sunbmpDaoSupport.exeSql(insql.toString());
        return id;
    }
}
