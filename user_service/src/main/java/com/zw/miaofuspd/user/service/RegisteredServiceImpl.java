package com.zw.miaofuspd.user.service;

import com.base.util.DateUtils;
import com.base.util.GeneratePrimaryKeyUtils;
import com.base.util.MD5Utils;
import com.zw.miaofuspd.facade.dict.service.IDictService;
import com.zw.miaofuspd.facade.user.service.IMsgService;
import com.zw.miaofuspd.facade.user.service.IRegisteredService;
import com.zw.miaofuspd.util.UserLogToSqlUtils;
import com.zw.service.base.AbsServiceBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 秒付金服注册接口
 */
@Service("registeredServiceImpl")
public class RegisteredServiceImpl extends AbsServiceBase implements IRegisteredService {
    @Autowired
    private IMsgService iMsgService;
    @Autowired
    private IDictService iDictService;

    /**
     * 查询是否已经注册
     * @param tel
     * @return
     * @throws Exception
     */
    @Override
    public boolean selectByTel(String tel) throws Exception{
        int count=sunbmpDaoSupport.getCount("select count(0) from app_user  where tel='" + tel + "'");
        if(count>0){
            return false;
        }
        return true;
    }

    /**
     * 秒付金服注册接口
     * @param map
     * @throws Exception
     */
    @Override
    public String register(Map map) throws Exception {
        String id = UUID.randomUUID().toString();
        String customerId = UUID.randomUUID().toString();
        String personId = UUID.randomUUID().toString();
        String password = MD5Utils.GetMD5Code((String) map.get("password"));
        String tel = (String) map.get("tel");//手机号
        String registration_id = (String) map.get("registration_id");
        String creatTime = DateUtils.getDateString(new Date());//将时间格式化
        String only_code_spd = (String) map.get("onlyCode")==null?"":map.get("onlyCode").toString();
        String referenceId = (String)map.get("referenceId")==null?"":map.get("referenceId").toString();
        String type = (String) map.get("type")==null?"":map.get("type").toString();//注册渠道
        String black_box = (String) map.get("black_box")==null?"":map.get("black_box").toString();//同盾唯一识别码
        //插入app_user表
        StringBuffer insql = new StringBuffer("INSERT INTO app_user (id,tel,black_box,type,passwd,state,creat_time,alter_time,registration_id,only_code_spd) VALUES('");
        insql.append(id + "','" + tel + "','" + black_box + "','" + type + "','" + password + "','" + 1 + "','" + creatTime + "','" + creatTime + "','"+registration_id+"','"+only_code_spd+"')");
        //插入mag_customer_person表
        String mcpsql = "INSERT INTO mag_customer_person(ID,customer_id,CREAT_TIME,state) VALUES('" + personId + "','" + customerId + "','" + DateUtils.getDateString(new Date()) + "','0')";
        //插入mag_customer表
        String mcsql = "INSERT INTO mag_customer(ID,USER_ID,PERSON_ID,CREAT_TIME,state,TEL,CARD_TYPE,invitation_id) VALUES('" + customerId + "','" + id + "','" + personId + "','" + DateUtils.getDateString(new Date()) + "','0'" +
                ",'"+tel+"','身份证','"+referenceId+"')";
        List list = new ArrayList();
        list.add(insql.toString());
        list.add(mcpsql);
        list.add(mcsql);
        sunbmpDaoSupport.exeSql(list);
        //将用户注册事件插入用户日志表中
        sunbmpDaoSupport.exeSql(UserLogToSqlUtils.userLog(id, tel, "用户注册", tel, "用户注册", creatTime, creatTime));
        //注册成功发送一条站内信
        String title = "注册成功";
        String content = "恭喜您，您已经成功注册。";//iDictService.getDictInfo("消息内容","zccg");
        Map msgMap = new HashMap();
        msgMap.put("user_id", id);
        msgMap.put("title", title);
        msgMap.put("content", content);
        msgMap.put("registration_id", registration_id);
        msgMap.put("msg_type","0");//消息类型。0系统消息
        iMsgService.insertMsg(msgMap);//调用发送消息接口'
        return id;
    }

    /**
     * 查询唯一识别绑定
     * @param onlyCode
     * @return
     */
    @Override
    public boolean selectByOnlyCode(String onlyCode) {
        int count=sunbmpDaoSupport.getCount("select count(id) from app_user where only_code_spd='" + onlyCode + "'");
        if(count>0){
            return false;
        }
        return true;
    }

    /**
     * 获取注册条款
     * @return
     * @throws Exception
     */
    @Override
    public Map getRegClause(String type) throws Exception {
        return sunbmpDaoSupport.findForMap("SELECT content,content_no_bq FROM mag_template where template_type = '"+type+"' and platform_type = '1'");
    }

    /**
     * 查询手机错误次数
     * @param tel
     * @return
     * @throws Exception
     */
    @Override
    public boolean selectByTelError(String tel) throws Exception {
        Map map = sunbmpDaoSupport.findForMap("select error_count AS errorCount from reg_error_phone where phone='" + tel + "'");
        if(map != null){
            if((Integer)map.get("errorCount") >= 3){
                return true;
            }
        }
        return false;
    }

    /**
     * 更新手机密码错误次数
     * @param phone
     */
    @Override
    public void updateTelError(String phone) {
        Map map = sunbmpDaoSupport.findForMap("select error_count from reg_error_phone where phone='" + phone + "'");
        if(map == null){
            //插入数据
            sunbmpDaoSupport.exeSql("INSERT INTO reg_error_phone(phone,error_count) VALUES('"+phone+"',1) ");
        }else{
            sunbmpDaoSupport.exeSql("UPDATE reg_error_phone SET error_count = (error_count + 1) WHERE phone = '"+phone+"'");
        }
    }

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
