package com.zw.miaofuspd.personal.service;

import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.personal.service.AppUserService;
import com.zw.service.base.AbsServiceBase;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guoqing on 2017/3/6.
 */
@Service
public class AppUserServiceImpl extends AbsServiceBase implements AppUserService {

    @Override
    public void uploadImg(AppUserInfo userInfo) {
        String sql = "update app_user set head_img = '"+userInfo.getImg_url()+"' where id = '"+userInfo.getId()+"'";
        sunbmpDaoSupport.exeSql(sql);
    }

    @Override
    public List getImgInfo(AppUserInfo userInfo) {
        String sql = "select head_img,realname,tel from app_user where id ='"+userInfo.getId()+"'";
        return sunbmpDaoSupport.findForList(sql);
    }

    @Override
    public void updateUrl(Map map,AppUserInfo userInfo) {
        List list = (List) map.get("fileList");
        Map map1 = (Map) list.get(0);
        String nameUrl = map1.get("Name").toString();
        String sql = "update app_user set head_img = '"+nameUrl+"' where id = '"+userInfo.getId()+"'";
        sunbmpDaoSupport.exeSql(sql);
    }

    @Override
    public List getCooperation() {
        String sql = "select name,path,link from mag_partners where state = '1' and platform_type='1'";
        return sunbmpDaoSupport.findForList(sql);
    }

    @Override
    public Map getCustomerInfo(String customerId) throws Exception {
        String sql = "SELECT mc.is_identity,mc.identity_complete,mc.TEL as phone,mc.card,mc.person_name,mc.sex_name,mc.birth,mc.card_register_address," +
                "mc.red_money,mc.person_info_complete,mc.Zcard_src_base64,mc.Fcard_src_base64,mc.person_face_complete,mc.authorization_complete," +
                "mo.applay_money as repay_money " + " FROM mag_customer mc LEFT JOIN mag_order mo on " +
                " mc.id = mo.CUSTOMER_ID where mc.id ='"+customerId+"'";
        return sunbmpDaoSupport.findForMap(sql);
    }

    @Override
    public Map findCustomerInfo(String customerId) throws Exception {
        String sql = "SELECT mc.is_identity,mc.identity_complete,mc.TEL as phone,mc.card,mc.person_name,mc.sex_name,mc.birth,mc.card_register_address," +
                "mc.red_money,mc.person_info_complete,mc.Zcard_src_base64,mc.Fcard_src_base64,mc.person_face_complete,mc.authorization_complete" +
                "  FROM mag_customer mc where mc.id ='" + customerId + "'";
        return sunbmpDaoSupport.findForMap(sql);
    }

    @Override
    public Map  getIdentityInfo(String customerId) {
        Map ptoMap =new HashMap();
        String sql="select Zcard_src_spd,Fcard_src_spd from mag_customer where id='"+customerId+"'";
        Map map1 = sunbmpDaoSupport.findForMap(sql);
        if(map1!=null){
            ptoMap.put("Zcard_src_base64",map1.get("Zcard_src_spd").toString());
            ptoMap.put("Fcard_src_base64",map1.get("Fcard_src_spd").toString());
        }else{
            ptoMap.put("Zcard_src_base64","");
            ptoMap.put("Fcard_src_base64","");
        }
        return ptoMap;
    }

    /**
     * 判断该客户是不是已经开户，如果为开户，提示用户前往办单员开户
     * @param userInfo
     * @return
     */
    @Override
    public Map getAccountInfo(AppUserInfo userInfo) {
        Map map = new HashMap();
        String accountSql = "select count_name,user_name,bank_card,account_bank,card_num from mag_customer_account where USER_ID = '"+userInfo.getId()+"' and state = '0' and channel = '1'";
        List list = sunbmpDaoSupport.findForList(accountSql);
        String bankNo = "";
        String bankName = "";
        if(list!=null & list.size()>0){
            bankNo = ((Map) list.get(0)).get("bank_card").toString();
            bankName = ((Map) list.get(0)).get("account_bank").toString();
            map.put("bankNo",bankNo);
            map.put("bankName",bankName);
            map.put("flag",true);
        }else{
            map.put("bankNo","");
            map.put("bankName","");
            map.put("flag",false);
            map.put("msg","您当前未开户,请联系你的业务员帮您开户");
        }
        return map;
    }
}
