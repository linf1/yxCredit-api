package com.zw.miaofuspd.myset.service;

import com.base.util.MD5Utils;
import com.zw.miaofuspd.facade.myset.service.EmployeeModifyPswService;
import com.zw.service.base.AbsServiceBase;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/13.
 */
@Service
public class EmployeeModifyPswServiceImpl extends AbsServiceBase implements EmployeeModifyPswService {
    @Override
    /**
     * @param  map 封装手机号,新密码,旧密码
     *
     */
    public Map updatePw(Map map) throws Exception{
        Map outMap =new HashMap();
        String phone =map.get("phone").toString();
        String oldPassword=map.get("oldPassword").toString();
        String newPassword=map.get("newPassword").toString();
        String sql ="select passwd from mag_salesman where tel ='"+phone+"'";
        List ulist=sunbmpDaoSupport.findForList(sql);
        if(ulist.size()<=0){
            //如果不为空证明
            outMap.put("success",false);
            outMap.put("msg","输入的手机号错误，请重新输入");
            return outMap;
        }
        if(!((Map) ulist.get(0)).get("passwd").equals(MD5Utils.GetMD5Code(oldPassword))){
            outMap.put("success",false);
            outMap.put("msg","输入的原密码错误，请重新输入");
            return outMap;
        }
        newPassword= MD5Utils.GetMD5Code(newPassword);
        StringBuffer updatesql=new StringBuffer("update mag_salesman set passwd='");
        updatesql.append(newPassword);
        updatesql.append("' where tel='");
        updatesql.append(phone+"'");
        sunbmpDaoSupport.exeSql(updatesql.toString());
        outMap.put("success",true);
        outMap.put("msg","密码修改成功！");
        return outMap;
    }
}
