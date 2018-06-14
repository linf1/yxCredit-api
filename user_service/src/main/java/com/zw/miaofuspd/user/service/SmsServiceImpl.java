package com.zw.miaofuspd.user.service;

import com.api.model.sortmsg.MsgRequest;
import com.base.util.DateUtils;
import com.base.util.GeneratePrimaryKeyUtils;
import com.zw.api.SendSmsApi;
import com.zw.miaofuspd.facade.dict.service.IDictService;
import com.zw.miaofuspd.facade.dict.service.ISystemDictService;
import com.zw.miaofuspd.facade.user.service.ISmsService;
import com.zw.service.base.AbsServiceBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SmsServiceImpl extends AbsServiceBase implements ISmsService {
    @Autowired
    private ISystemDictService iSystemDictService;
    @Autowired
    private IDictService iDictService;

    @Override
    public boolean saveSms(MsgRequest msgRequest) {
        String phone =  msgRequest.getPhone();
        String varCode = msgRequest.getSmsCode();
        String date = DateUtils.getDateString(new Date());
        StringBuffer sql = new StringBuffer("INSERT INTO app_sms (id,type,tel,var_code,creat_time,alter_time) VALUES('");
        sql.append(GeneratePrimaryKeyUtils.getUUIDKey() + "','" + msgRequest.getType() + "','" + phone + "','" + varCode + "','" + date + "','" + date + "')");
        sunbmpDaoSupport.exeSql(sql.toString());
        return true;
    }

    /**
     * 检查验证码是否正确
     */
    @Override
    public Map checkSms(Map inMap) throws Exception {
        //先查询验证码和用户是否正确
        Map outMap=new HashMap();
        String tel = inMap.get("tel") == null ? "" : (String)inMap.get("tel");
        String smsCode=(String)inMap.get("smsCode");
        String errortime=(String)inMap.get("errortime");
        String type = inMap.get("type") == null ? "0" : (String)inMap.get("type");
        List smslist=sunbmpDaoSupport.findForList("select var_code,tel,creat_time from app_sms where tel = '" + tel +"' and state ='0' and type = '"+type+"' ORDER BY creat_time DESC LIMIT 1");
        if (smslist.size() <= 0) {//判断验证码和用户名是否同时存在
            outMap.put("flag",false);
            outMap.put("msg","验证码输入有误，请重新输入");
            return outMap;
        }
        Map smsMap=(Map)smslist.get(0);
        if (smsCode != null && ! smsCode.equals(smsMap.get("var_code"))) {//判断使用的验证码是否为最新获取的
            outMap.put("flag",false);
            outMap.put("msg","验证码输入有误，请重新输入");
            return outMap;
        }
        if (!(DateUtils.isDateBig((String) smsMap.get("creat_time"), errortime))) { //判断验证码是否失效
            outMap.put("flag",false);
            outMap.put("msg","验证码已失效，请重新获取");
            return outMap;
        }
        //验证完成修改验证码的状态
        String upsql = "update app_sms set state ='1' where var_code='" + smsCode + "' and tel ='" + tel + "'";
        sunbmpDaoSupport.exeSql(upsql);
        outMap.put("flag",true);
        return outMap;
    }
    public String getSmsContent(String type){
        String sql = "select sms_content from sms_manage where sms_rule = '"+type+"' and sms_state='1' and platform_type='1'";
        Map map = sunbmpDaoSupport.findForMap(sql);
        return map.get("sms_content").toString();
    }


    @Override
    public boolean updateSms(MsgRequest request) {
        String phone = request.getPhone();
        String varCode = request.getSmsCode();
        StringBuffer sql = new StringBuffer("update app_sms  set state = 0 , var_code = '");
        sql.append(varCode).append("'").append(" where 1=1 ");
        sql.append("and tel = '").append(phone);
        sql.append("' and type = '").append(request.getType()).append("'");
        return sunbmpDaoSupport.executeSql(sql.toString()) > 0;
    }
}
