package com.zw.miaofuspd.myset.service;

import com.base.util.DateUtils;
import com.base.util.MD5Utils;
import com.zhiwang.zwfinance.app.jiguang.util.JiGuangUtils;
import com.zw.miaofuspd.facade.dict.service.IDictService;
import com.zw.miaofuspd.facade.dict.service.ISystemDictService;
import com.zw.miaofuspd.facade.myset.service.IAppEditPswService;
import com.zw.miaofuspd.util.UserLogToSqlUtils;
import com.zw.service.base.AbsServiceBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
@Service
public class AppEditPswServiceImpl extends AbsServiceBase implements IAppEditPswService {

    @Autowired
    ISystemDictService systemDictService;
    @Autowired
    private IDictService iDictService;

    /**
     * 修改手机登录密码;
     * @param inMap     参数包含：
     * id(必需) 用户ID；
     * oldPassword（必需）  老密码；
     * tel（必需）  手机号；
     * newPassword（必需）    新密码；
     * registration_id（必需）  推送ID；
     * @return Map  参数包含：
     * success（必需） true  成功；false   失败；
     * @throws Exception
     */
    @Override
    public Map updatePw(Map inMap) throws Exception {

        String id=(String)inMap.get("id");
        String oldPassword=(String)inMap.get("oldPassword");
        String tel=(String)inMap.get("tel");
        String newPassword=(String)inMap.get("newPassword");
        String registration_id=(String)inMap.get("registration_id");

        Map outMap=new HashMap();

        //验证原密码是否正确
        oldPassword= MD5Utils.GetMD5Code(oldPassword);
        StringBuffer checksql=new StringBuffer("select u.id,u.tel from app_user u where u.tel='");
        checksql.append(tel+"' and passwd= '");
        checksql.append(oldPassword+"'");
        List ulist=sunbmpDaoSupport.findForList(checksql.toString());
        if(ulist.size()<=0){
            outMap.put("success",false);
            outMap.put("msg","原密码错误，请重新输入");
            return outMap;
        }
        Date date=new Date();
        String alterTime = DateUtils.getDateString(date);
        //执行修改
        newPassword= MD5Utils.GetMD5Code(newPassword);
        StringBuffer updatesql=new StringBuffer("update app_user set passwd='");
        updatesql.append(newPassword+"',alter_time ='");
        updatesql.append(alterTime+"' where tel='");
        updatesql.append(tel+"'");
        sunbmpDaoSupport.exeSql(updatesql.toString());

        //将记录保存在app_user_log表中
        sunbmpDaoSupport.exeSql(UserLogToSqlUtils.userLog(id, tel, "修改密码",oldPassword, newPassword, "修改密码", alterTime, alterTime));

        //修改密码成功发送一条站内信
        String title = "修改密码";
        String content = "您的密码重置成功，请妥善保管好您的账户和密码，防止信息泄露风险。感谢您使用秒付金服。全国服务热线：400-*****。";//iDictService.getDictInfo("消息内容","zzmm");
        String now = DateUtils.getDateString(new Date());  //获取当前时间
        String messageId= UUID.randomUUID().toString();//message表插入id字段
        String messageState = "0";//表示站内信未读
        String push_state = "0";
        String msg_type = "0";//表示消息类型
        if(! JiGuangUtils.alias(title, content, registration_id)){
            push_state = "1";
        }
        StringBuffer messageSql = new StringBuffer("insert into app_message(id,msg_type,user_id,title,content,state,creat_time,alter_time,order_type,push_state) values('");
        messageSql.append(messageId+"','"+msg_type+"','"+id+"','"+title+"','"+content+"','"+messageState+"','"+now+"','"+now+"','2','"+push_state+"')");
        sunbmpDaoSupport.exeSql(messageSql.toString());
        outMap.put("success",true);
        outMap.put("msg","密码修改成功！");

        return outMap;
    }

}
