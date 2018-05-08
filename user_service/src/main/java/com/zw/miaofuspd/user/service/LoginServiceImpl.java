package com.zw.miaofuspd.user.service;

import com.base.util.DateUtils;
import com.base.util.MD5Utils;
import com.base.util.TokenUtil;
import com.zw.miaofuspd.facade.dict.service.IDictService;
import com.zw.miaofuspd.facade.user.service.ILoginService;
import com.zw.service.base.AbsServiceBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <strong>Title :用户登录 <br>
 * </strong> <strong>Description : </strong>@类注释说明写在此处@<br>
 * <strong>Create on : 2017年02月21日<br>
 * </strong>
 * <p>
 * <strong>Copyright (C) Vbill Co.,Ltd.<br>
 * </strong>
 * <p>
 *
 * @author department:技术开发部 <br>
 *         username:wangmin <br>
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
public class LoginServiceImpl extends AbsServiceBase implements ILoginService {

    @Autowired
    private IDictService dictServiceImpl;

    @Override
    public Map login(String username, String password, String registration_id,String black_box,String type,String ip_address,String deviceCode) throws Exception {
        Map returnMap = new HashMap();
        password = MD5Utils.GetMD5Code(password);
        String issql = "select id,tel,head_img as img_url,error_num,alter_time,state,passwd,registration_id,is_black,only_code_spd " +
                "from app_user where tel='"+username+"'";
        List list = sunbmpDaoSupport.findForList(issql);
        if(list.isEmpty()){
            returnMap.put("success",false);
            returnMap.put("msg","该用户不存在，请注册后再登录");
            return returnMap;
        }
        Map map = (Map) list.get(0);
//        if(!(deviceCode.equals(map.get("only_code_spd")+"")) && !("".equals(map.get("only_code_spd")+""))){
//            returnMap.put("success",false);
//            returnMap.put("msg","该账号已绑定设备，请更换登录设备");
//            return returnMap;
//        }
        int errorNumBase = Integer.parseInt(map.get("error_num").toString());
        int errorNum = Integer.parseInt(dictServiceImpl.getDictInfo("密码错误次数", "0"));//获取数据字典中配置的密码输入错误次数
        //判断密码输入错误是否已经超过规定次数
        Map limitMap = new HashMap();
        String limitime =dictServiceImpl.getDictInfo("限制时间", "0");
        if(errorNum <= errorNumBase){
            if(!(DateUtils.isErrorTime(map.get("alter_time").toString(),"0",limitime))){//判断被锁定的用户是否在规定时间外登录
                int logTime = Integer.parseInt(limitime)/60;
                returnMap.put("success",false);
                returnMap.put("msg","密码超过"+errorNum+"次，请在"+logTime+"分钟后登录");
                return returnMap;
            }
            StringBuffer upsql = new StringBuffer("update app_user set error_num ='0' where tel='"+username+"'");//将解锁的用户重置密码输入错误次数
            sunbmpDaoSupport.exeSql(upsql.toString());
            errorNumBase = 0;
        }
        //获取存储在数据库的密码
        String savePassword=map.get("passwd").toString();
        String alterTime = DateUtils.getDateString(new Date());
        if (!savePassword.equals(password)) {
            errorNumBase = errorNumBase+1;
            StringBuffer unsql = new StringBuffer("update app_user set error_num = '"+errorNumBase+"',alter_time ='"+alterTime+"'where tel ='"+username+"'");
            sunbmpDaoSupport.exeSql(unsql.toString());//更新密码错误次数及密码输入错误时间
            if(errorNumBase >=errorNum){
                int logTime = Integer.parseInt(limitime)/60;
                returnMap.put("success",false);
                returnMap.put("msg","密码错误超过"+errorNum+"次，请在"+logTime+"分钟后登录");
            }else if(errorNumBase == errorNum-1){
                returnMap.put("success",false);
                returnMap.put("imgCode","imgCode");
                returnMap.put("msg","用户名密码输入错误，请重新输入");
            }
            else{
                returnMap.put("success",false);
                returnMap.put("msg","用户名密码输入错误，请重新输入");
            }
            return returnMap;
        }else{
            StringBuffer unsql = new StringBuffer("update app_user set error_num = '0',alter_time ='"+alterTime+"',state='0',registration_id='"+registration_id+"' where tel ='"+username+"'");
            sunbmpDaoSupport.exeSql(unsql.toString());//成功登陆后将密码错误次数重置并更新登录时间
        }
        String id=map.get("id").toString();
        //此次不太需要用外联
        StringBuffer sql = new StringBuffer(
                "select m.person_name,m.card_type_id,m.CARD_TYPE,m.CARD,m.bg_cust_info_id,m.crm_cust_info_id,m.bg_customer_id,m.id as customer_id,m.occupation_type as occupation_type,b.sex,m.sex_name,b.id as person_id"
                        + " from mag_customer m,mag_customer_person b  where  m.ID=b.customer_id and m.user_id = '");
        sql.append(id + "'");
        List ulist = sunbmpDaoSupport.findForList(sql.toString());//校验用户输入的用户名密码是否匹配
        returnMap.put("id",map.get("id"));
        returnMap.put("tel",map.get("tel"));
        returnMap.put("img_url",map.get("img_url"));
        returnMap.put("is_black",map.get("is_black"));
        returnMap.put("registration_id",registration_id);
        returnMap.put("isIdentity",false);
        //判断mag_customer,mag_customer_person表中是否存在数据
        if(ulist != null && ulist.size()>0){
            Map umap = (Map) ulist.get(0);
            returnMap.put("isIdentity",true);
            returnMap.put("person_name",umap.get("person_name"));
            returnMap.put("bg_cust_info_id",umap.get("bg_cust_info_id"));
            returnMap.put("bg_customer_id",umap.get("bg_customer_id"));
            returnMap.put("crm_cust_info_id",umap.get("crm_cust_info_id"));
            returnMap.put("customer_id",umap.get("customer_id"));
            returnMap.put("card_type_id",umap.get("card_type_id"));
            returnMap.put("CARD_TYPE",umap.get("CARD_TYPE"));
            returnMap.put("CARD",umap.get("CARD"));
            returnMap.put("sex",umap.get("sex"));
            returnMap.put("sex_name",umap.get("sex_name"));
            returnMap.put("person_id",umap.get("person_id"));
            returnMap.put("occupation_type",umap.get("occupation_type"));
            //获取token，并将token更新到数据库中
            String token = TokenUtil.getToken(map.get("tel")+"");
            String tokenTime = (new Date()).getTime()+"";
            StringBuffer unsql = new StringBuffer("update app_user set  only_code_spd = '"+deviceCode+"', token = '"+token+"',token_time ='"+tokenTime+"' where tel ='"+username+"'");
            sunbmpDaoSupport.exeSql(unsql.toString());
            returnMap.put("token",token);
            returnMap.put("token_time",tokenTime);
        }
        //更新设备信息表
        String updateSql = "update app_user set black_box='"+black_box+"',type='"+type+"' where id ='"+map.get("id")+"'";
        sunbmpDaoSupport.exeSql(updateSql);
        returnMap.put("success",true);
        return  returnMap;
    }

    @Override
    /** app端办单员登录
     * @param tel 手机号 办单员工号
     * @param password 密码
     */
    public Map loginEmployee(String tel, String password) throws Exception {
        Map returnMap = new HashMap();
        //  password = MD5Utils.GetMD5Code(password);
        String issql = "SELECT id,tel,employee_num AS employeeNum ,passwd,idcard,realname," +
                "sex,sex_name AS sexName,activation_state AS activationState FROM mag_salesman\n" +
                "WHERE tel='"+tel+"' OR employee_num='"+tel+"'";
        List list = sunbmpDaoSupport.findForList(issql);
        if(list.isEmpty()){
            returnMap.put("success",false);
            returnMap.put("msg","该用户不存在");
            return returnMap;
        }
        Map map = (Map) list.get(0);
        //判断用户是否已经激活
        // activation_state
        // 0 未激活  1激活
        if("0".equals(map.get("activationState").toString())){
            returnMap.put("success",false);
            returnMap.put("msg","该用户未激活");
            return returnMap;
        }
        //获取存储在数据库的密码
        String savePassword=map.get("passwd").toString();
        if (!savePassword.equals(MD5Utils.GetMD5Code(password))) {
            returnMap.put("success",false);
            returnMap.put("msg","密码输入错误，请重新输入");
            return returnMap;
        }
        //返回当前的日期 yyyyMMddHHmmss,作为最后的修改
        String alterTime = DateUtils.getDateString(new Date());
        StringBuffer unsql = new StringBuffer("UPDATE mag_salesman SET alter_time ='"+alterTime+"',state='0' WHERE tel ='"+tel+"'");
        sunbmpDaoSupport.exeSql(unsql.toString());
        String id=map.get("id").toString();
        String employee_num = map.get("employeeNum").toString();
        String idcard = map.get("idcard").toString();
        String realname = map.get("realname").toString();
        String sex = map.get("sex").toString();
        String sex_name = map.get("sexName").toString();
        String activation_state = map.get("activationState").toString();
        returnMap.put("id",id);
        returnMap.put("tel",map.get("tel"));
        returnMap.put("employee_num",employee_num);
        returnMap.put("idcard",idcard);
        returnMap.put("realname",realname);
        returnMap.put("sex",sex);
        returnMap.put("sex_name",sex_name);
        returnMap.put("activation_state",activation_state);
        //对于登录成功的情况也需要将登录成功的状态记录下来
        returnMap.put("success",true);
        return  returnMap;
    }

    @Override
    public Map wechatLogin(String username, String password) throws Exception {
        Map returnMap = new HashMap();
        password = MD5Utils.GetMD5Code(password);
        String issql = "select id,tel,head_img as img_url,error_num,alter_time,state,passwd,registration_id,is_black,only_code_spd " +
                "from app_user where tel='"+username+"'";
        List list = sunbmpDaoSupport.findForList(issql);
        if(list.isEmpty()){
            returnMap.put("success",false);
            returnMap.put("msg","该用户不存在，请注册后再登录");
            return returnMap;
        }

        Map map = (Map) list.get(0);

        int errorNumBase = Integer.parseInt(map.get("error_num").toString());
        int errorNum = Integer.parseInt(dictServiceImpl.getDictInfo("密码错误次数 ", "0"));//获取数据字典中配置的密码输入错误次数
        //判断密码输入错误是否已经超过规定次数
        Map limitMap = new HashMap();
        String limitime =dictServiceImpl.getDictInfo("限制时间", "0");
        if(errorNum <= errorNumBase){
            if(!(DateUtils.isErrorTime(map.get("alter_time").toString(),"0",limitime))){//判断被锁定的用户是否在规定时间外登录
                int logTime = Integer.parseInt(limitime)/60;
                returnMap.put("success",false);
                returnMap.put("msg","密码超过"+errorNum+"次，请在"+logTime+"分钟后登录");
                return returnMap;
            }
            StringBuffer upsql = new StringBuffer("update app_user set error_num ='0' where tel='"+username+"'");//将解锁的用户重置密码输入错误次数
            sunbmpDaoSupport.exeSql(upsql.toString());
            errorNumBase = 0;
        }
        //获取存储在数据库的密码
        String savePassword=map.get("passwd").toString();
        String alterTime = DateUtils.getDateString(new Date());
        if (!savePassword.equals(password)) {
            StringBuffer unsql = new StringBuffer("update app_user set error_num = '"+(errorNumBase+1)+"',alter_time ='"+alterTime+"'where tel ='"+username+"'");
            sunbmpDaoSupport.exeSql(unsql.toString());//更新密码错误次数及密码输入错误时间
            if(errorNumBase >=errorNum){
                int logTime = Integer.parseInt(limitime)/60;
                returnMap.put("success",false);
                returnMap.put("msg","密码错误超过"+errorNum+"次，请在"+logTime+"分钟后登录");
            }else if(errorNumBase == errorNum-1){
                returnMap.put("success",false);
                returnMap.put("imgCode","imgCode");
                returnMap.put("msg","用户名密码输入错误，请重新输入");
            }
            else{
                returnMap.put("success",false);
                returnMap.put("msg","用户名密码输入错误，请重新输入");
            }

            return returnMap;
        }else{
            StringBuffer unsql = new StringBuffer("update app_user set error_num = '0',alter_time ='"+alterTime+"',state='0' where tel ='"+username+"'");
            sunbmpDaoSupport.exeSql(unsql.toString());//成功登陆后将密码错误次数重置并更新登录时间
        }
        String id=map.get("id").toString();
        //此次不太需要用外联
        StringBuffer sql = new StringBuffer(
                "select m.person_name,m.card_type_id,m.CARD_TYPE,m.CARD,m.bg_cust_info_id,m.crm_cust_info_id,m.bg_customer_id,m.id as customer_id,m.occupation_type as occupation_type,b.sex,b.sex_name,b.id as person_id"
                        + " from mag_customer m,mag_customer_person b  where  m.ID=b.customer_id and m.user_id = '");
        sql.append(id + "'");
        List ulist = sunbmpDaoSupport.findForList(sql.toString());//校验用户输入的用户名密码是否匹配
        returnMap.put("id",map.get("id"));
        returnMap.put("tel",map.get("tel"));
        returnMap.put("img_url",map.get("img_url"));
        returnMap.put("is_black",map.get("is_black"));
        returnMap.put("isIdentity",false);
        //判断mag_customer,mag_customer_person表中是否存在数据
        if(ulist != null && ulist.size()>0){
            Map umap = (Map) ulist.get(0);
            returnMap.put("isIdentity",true);
            returnMap.put("person_name",umap.get("person_name"));
            returnMap.put("bg_cust_info_id",umap.get("bg_cust_info_id"));
            returnMap.put("bg_customer_id",umap.get("bg_customer_id"));
            returnMap.put("crm_cust_info_id",umap.get("crm_cust_info_id"));
            returnMap.put("customer_id",umap.get("customer_id"));
            returnMap.put("card_type_id",umap.get("card_type_id"));
            returnMap.put("CARD_TYPE",umap.get("CARD_TYPE"));
            returnMap.put("CARD",umap.get("CARD"));
            returnMap.put("sex",umap.get("sex"));
            returnMap.put("sex_name",umap.get("sex_name"));
            returnMap.put("person_id",umap.get("person_id"));
            returnMap.put("occupation_type",umap.get("occupation_type"));
            //获取token，并将token更新到数据库中
            String token = TokenUtil.getToken(map.get("tel")+"");
            String tokenTime = (new Date()).getTime()+"";
            StringBuffer unsql = new StringBuffer("update app_user set token = '"+token+"',token_time ='"+tokenTime+"' where tel ='"+username+"'");
            sunbmpDaoSupport.exeSql(unsql.toString());
            returnMap.put("token",token);
            returnMap.put("token_time",tokenTime);
        }

        returnMap.put("success",true);
        return  returnMap;
    }


    @Override
    public List getAccessToken() throws Exception{
        String sql = "select tel,token,token_time from app_user";
        List list = sunbmpDaoSupport.findForList(sql);
        return list;
    }

    @Override
    public void logOut(String user_id) throws Exception {
        String sql = "update app_user set token = '',token_time = '',state = '1',registration_id = '' where id = '"+user_id+"'";
        sunbmpDaoSupport.exeSql(sql);
    }
//    public Map validationLogin(String type,String account_login,String account_mobile ,String state ,String account_password ,String black_box,String ip_address,Map umap) throws Exception{
//        Map map =  new HashMap();
//        map.put("account_login",account_login);
//        map.put("account_mobile",account_mobile);
//        map.put("state",state);
//        map.put("account_password",account_password);
//        map.put("ip_address",ip_address);
//        map.put("black_box",black_box);
//        Map map1 = new HashMap();
//        map1.put("tel",account_mobile);
//        map1.put("name",umap.get("person_name"));
//        map1.put("user_id",umap.get("id"));
//        map1.put("customer_id",umap.get("customer_id"));
//        map1.put("idcard",umap.get("CARD"));
//        return fraudApiServiceImpl.validationLogin(type,map,map1);
//    }
}
