package com.zw.miaofuspd.user.service;

import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.user.service.IUserService;
import com.zw.service.base.AbsServiceBase;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <strong>Title : 辅助进件提交接口<br>
 * </strong> <strong>Description : </strong>@类注释说明写在此处@<br>
 * <strong>Create on : 2017年02月16日<br>
 * </strong>
 * <p>
 * <strong>Copyright (C) Vbill Co.,Ltd.<br>
 * </strong>
 * <p>
 *
 * @author department:技术开发部 <br>
 *         username:zh-pc <br>
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
public class UserServiceImpl extends AbsServiceBase implements IUserService {
    @Override
    public int test(String sql) throws Exception {
        return sunbmpDaoSupport.getCount(sql);
    }

    @Override
    public Map getUserByToken(String token) throws Exception {
        Map returnMap = new HashMap();
        String issql = "select id,tel,head_img as img_url,error_num,alter_time,state,passwd,registration_id,is_black " +
                "from app_user where token='"+token+"'";
        Map map = sunbmpDaoSupport.findForMap(issql);
        String id=map.get("id").toString();
        StringBuffer sql = new StringBuffer(
                "select m.person_name,m.card_type_id,m.CARD_TYPE,m.CARD,m.bg_cust_info_id,m.crm_cust_info_id,m.bg_customer_id,m.id as customer_id,m.occupation_type as occupation_type,b.sex,b.sex_name,b.id as person_id"
                        + " from mag_customer m,mag_customer_person b  where  m.ID=b.customer_id and m.user_id = '"+id + "'");
        List ulist = sunbmpDaoSupport.findForList(sql.toString());//校验用户输入的用户名密码是否匹配
        Map umap = (Map) ulist.get(0);
        AppUserInfo user = new AppUserInfo();
        user.setId(map.get("id") + "");
        user.setTel(map.get("tel") + "");
        user.setImg_url(map.get("img_url") + "");
        user.setRegistration_id(map.get("registration_id")+"");
        user.setName(umap.get("person_name") + "");
        user.setBg_cust_info_id(umap.get("bg_cust_info_id") + "");
        user.setBg_customer_id(umap.get("bg_customer_id") + "");
        user.setCrm_cust_info_id(umap.get("crm_cust_info_id") + "");
        user.setCustomer_id(umap.get("customer_id") + "");
        user.setCardTypeId(umap.get("card_type_id") + "");
        user.setCardType(umap.get("CARD_TYPE") + "");
        user.setCard(umap.get("CARD") + "");
        user.setSex(umap.get("sex") + "");
        user.setSexName(umap.get("sex_name") + "");
        user.setEmp_id(umap.get("emp_id") + "");
        user.setPerson_id(umap.get("person_id") + "");
        user.setOccupation_type(umap.get("occupation_type") + "");
        user.setIsBlack(map.get("is_black") + "");
        returnMap.put("userInfo",user);
        return  returnMap;
    }

    /**
     * 通过orderId获取用户信息
     * @param orderId
     * @return
     */
    @Override
    public AppUserInfo getUserByOrderId(String orderId) {
        String sql = " SELECT Contract_amount as budgetContractAmount  from mag_order " +
                "WHERE order_no='"+orderId+"'";

        Map map = sunbmpDaoSupport.findForMap(sql);
        AppUserInfo appUserInfo = new AppUserInfo();
        appUserInfo.setId(map.get("userId").toString());
        appUserInfo.setTel(map.get("tel").toString());
        appUserInfo.setCustomer_id(map.get("customerId").toString());
        appUserInfo.setCard(map.get("card").toString());
        appUserInfo.setName(map.get("CustomerName").toString());
        appUserInfo.setRegistration_id(map.get("registrationId").toString());
        return appUserInfo;
    }

    @Override
    public Map getCustomerInfoByCustomerId(String customerId) {
        String sql = "SELECT magCustomer.ID AS id, magCustomer.CARD AS card,magCustomer.PERSON_NAME AS customerName,magCustomer.TEL as tel " +
                "FROM mag_customer magCustomer where magCustomer.is_identity = 1 AND magCustomer.ID ='"+customerId+"'";
        return sunbmpDaoSupport.findForMap(sql);
    }
}
