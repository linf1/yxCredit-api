package com.zw.miaofuspd.user.service;

import com.base.util.DateUtils;
import com.base.util.MD5Utils;
import com.zhiwang.zwfinance.app.jiguang.util.JiGuangUtils;
import com.zw.miaofuspd.facade.dict.service.IDictService;
import com.zw.miaofuspd.facade.user.service.IAppResetPwService;
import com.zw.service.base.AbsServiceBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
        * <strong>Title : <br>
        * </strong> <strong>Description : </strong>@类注释说明写在此处@<br>
 * <strong>Create on : 2017年02月21日<br>
 * </strong>
        * <p>
 * <strong>Copyright (C) Vbill Co.,Ltd.<br>
 * </strong>
        * <p>
 *
         * @author department:技术开发部 <br>
 *         username:yaoxuetao <br>
 *         email: <br>
 * @version <strong>zw有限公司-运营平台</strong><br>
 *          <br>
 *          <strong>修改历史:</strong><br>
 *          修改人 修改日期 修改描述<br>
 *          -------------------------------------------<br>
 *          <br>
 *          <br>
 */
@Service
public class AppResetPwServiceImpl extends AbsServiceBase implements IAppResetPwService {
    @Autowired
    private IDictService iDictService;
    @Override
    public Map alterPw(String registration_id, String password, String phone) throws Exception {
        Map map = new HashMap();
        String date = DateUtils.getDateString(new Date());
        if (phone != null) {
            String passwd = MD5Utils.GetMD5Code(password);//将密码加密
            sunbmpDaoSupport.exeSql("update app_user set passwd = '" + passwd + "' where tel = '" + phone + "'");
            sunbmpDaoSupport.exeSql("INSERT INTO app_user_log (id,tel,handle,current,described,creat_time,alter_time) VALUES("
                    + "'','"+phone+"','"+"找回密码"+"','"+passwd+"','"+"找回密码"+"','"+date+"','"+date+"')");
            //注册成功发送一条站内信
            String title = "修改密码";
            String content = "您的密码重置成功，请妥善保管好您的账户和密码，防止信息泄露风险。感谢您使用秒付金服。全国服务热线：400-*****。"; //iDictService.getDictInfo("消息内容","zzmm");
            String push_state = "0";
            if (!JiGuangUtils.alias(title, content, registration_id)) {
                push_state = "1";
            }
            String messageId = UUID.randomUUID().toString();//message表插入id字段
            String messageState = "0";//表示站内信未读

            //获取修改的用户的id
            Map map1 = sunbmpDaoSupport.findForMap("select id from app_user where tel = '" + phone + "'");
            Map insMap = new HashMap();
            insMap.put("messageId", messageId);
            insMap.put("id", map1.get("id"));
            insMap.put("title", title);
            insMap.put("content", content);
            insMap.put("messageState", messageState);
            insMap.put("push_state", push_state);
            String msg_type = "0";//消息类型，系统消息
            sunbmpDaoSupport.exeSql("insert into app_message(id,msg_type,user_id,title,content,state,creat_time,alter_time,push_state,order_type) values('" +
                    insMap.get("messageId") + "','" + msg_type + "','" + insMap.get("id") + "','" + insMap.get("title") + "','" + insMap.get("content") + "','" + insMap.get("messageState") + "','" + date + "','" + date + "','" + insMap.get("push_state") + "','2')");
            map.put("success",true);
        }
        return map;
    }
}
