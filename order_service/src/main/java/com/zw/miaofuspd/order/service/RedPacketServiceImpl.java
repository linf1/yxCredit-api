package com.zw.miaofuspd.order.service;

import com.base.util.DateUtils;
import com.zw.miaofuspd.facade.order.service.RedPacketService;
import com.zw.service.base.AbsServiceBase;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("redPacketServiceImpl")
public class RedPacketServiceImpl extends AbsServiceBase implements RedPacketService {


    @Override
    public Map getRedPacket(String userId,String orderId) {
        Map map = new HashMap();
        //获取推荐人id
        String sql = "SELECT ID as customerId,TEL as tel,PERSON_NAME as customerName,CARD as IdCard,invitation_id as invitationId from mag_customer where USER_ID = '"+userId+"'";
        Map referenceMap = sunbmpDaoSupport.findForMap(sql);
        String orderSql  = "select id from mag_order where USER_ID='"+userId+"' and state != '0' and state != '3'and state != '6' ORDER BY CREAT_TIME desc limit 1";
        List orderList = sunbmpDaoSupport.findForList(orderSql );
        if (orderList.isEmpty()) {
            map.put("success","1");
            map.put("msg", "您当前未完成进件申请,无法进行提现操作");
            return map;
        }
        if (!"".equals(referenceMap.get("invitationId").toString())) {
            String invitationId = referenceMap.get("invitationId").toString();
            //获取红包状态及金钱值
            sql = "SELECT id,red_money as redMoney from mag_red where status = '1'and red_type='1'";
            Map redPacketMap = sunbmpDaoSupport.findForMap(sql);
            if (redPacketMap ==null){
                map.put("flag",false);
                map.put("msg","当前没有可用红包!");
                return map;
            }
            redPacketMap.get("redMoney");

            //获取推荐人红包金钱值
            sql = "SELECT TEL as tel,PERSON_NAME as customerName,red_money as redMoney from mag_customer where USER_ID ='" + invitationId + "'";
            Map userMap = sunbmpDaoSupport.findForMap(sql);
//            //查询当前是否有一笔审核通过的单子，如果有，就不需要添加红包记录
//            String checkOrderSql = "select * from mag_order where user_id = '"+userId+"' and state in(5,7,8,9)";
//            List list = sunbmpDaoSupport.findForList(checkOrderSql);

            //有无推荐给当前人的红包
            String checkRedSql = "select * from mag_red_info where customer_id='" + referenceMap.get("customerId").toString() + "'";
            List list = sunbmpDaoSupport.findForList(checkRedSql);

            if(list!=null && list.size()>0){
            }else {
                //添加红包明细信息
                String redId = redPacketMap.get("id").toString();
                String cusName = referenceMap.get("customerName").toString();
                String idCard = referenceMap.get("IdCard").toString();
                String tel = referenceMap.get("tel").toString();
                String cusId = referenceMap.get("customerId").toString();
                String money = redPacketMap.get("redMoney").toString();
                String createTime = DateUtils.getDateString(new Date());
                String inviteCode = invitationId;
                String inviteName = userMap.get("customerName").toString();
                String inviteTel = userMap.get("tel").toString();
                sql = "insert into mag_red_info (id,red_id,customer_name,customer_id,tel,id_card,money,create_time,invite_code,invite_name,invite_tel,order_id,red_type) " +
                        "values('" + UUID.randomUUID().toString() + "','" + redId + "','" + cusName + "','" + cusId + "','" + tel + "','" + idCard + "','" + money + "','" + createTime + "','" + inviteCode + "','" + inviteName + "','" + inviteTel + "','" + orderId + "','1')";
                sunbmpDaoSupport.exeSql(sql);
            }
//            //红包获取过清空推荐人
//            sql = "update mag_customer set invitation_id ='' where USER_ID = '"+userId+"'";
//            sunbmpDaoSupport.exeSql(sql);
            map.put("flag",true);
            return map;

        }else {
            map.put("flag",false);
            map.put("msg","没有推荐人!");
            return map;
        }
    }
}
