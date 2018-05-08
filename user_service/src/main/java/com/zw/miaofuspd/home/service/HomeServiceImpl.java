package com.zw.miaofuspd.home.service;

import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.home.service.HomeService;
import com.zw.service.base.AbsServiceBase;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author xiahaiyang
 * @Create 2017年12月21日10:29:25
 **/
@Service("homeServiceImpl")
public class HomeServiceImpl extends AbsServiceBase implements HomeService {

    /**
     * 根据userId获取我的页面信息
     * @param userId
     * @return
     */
    @Override
    public Map getMyPageInfo(String userId) {
        String sql = "select head_img as headImg from app_user where id = '"+userId+"'";
        Map userMap = sunbmpDaoSupport.findForMap(sql);
        sql= "select PERSON_NAME as personName from mag_customer where USER_ID = '"+userId+"'";
        Map cusMap = sunbmpDaoSupport.findForMap(sql);
        //查询所有订单数量(显示在我的分期申请下面)
        sql ="select COUNT(DISTINCT id) as orderCount from mag_order where order_type = '2'" +
                "  and USER_ID = '"+userId+"'";
        Map orderMap = sunbmpDaoSupport.findForMap(sql);
        sql = "select COUNT(DISTINCT id) as payCount from mag_order where order_type = '2' and " +
                "state = '5'and commodity_state in ('19','20') and USER_ID = '"+userId+"'";
        Map payMap = sunbmpDaoSupport.findForMap(sql);
        Map map  = new HashMap();
        map.putAll(userMap);
        map.putAll(cusMap);
        map.put("orderCount",orderMap.get("orderCount").toString());
        map.put("payCount",payMap.get("payCount").toString());
        return map;
    }

    @Override
    public Map checkOrder(AppUserInfo appUserInfo) {
        Map map = new HashMap();
        String orderSql = "select id from mag_order where customer_id = '"+appUserInfo.getCustomer_id()+"' " +
                "and order_type='2' and state in('5','9')";
        List orderList = sunbmpDaoSupport.findForList(orderSql);
        if(orderList!=null && orderList.size()>0){
            map.put("flag",true);
            return map;
        }
        map.put("flag",false);
        map.put("msg","当前无审核通过的订单,不能分享好友!");
        return map;
    }
}
