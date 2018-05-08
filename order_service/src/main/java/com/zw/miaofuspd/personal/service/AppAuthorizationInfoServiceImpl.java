package com.zw.miaofuspd.personal.service;

import com.base.util.DateUtils;
import com.zw.miaofuspd.facade.personal.service.AppAuthorizationInfoService;
import com.zw.service.base.AbsServiceBase;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/20.
 */
@Service
public class AppAuthorizationInfoServiceImpl extends AbsServiceBase implements AppAuthorizationInfoService {
    @Override
    /**
     * 通过订单Id获取客户的授权信息
     * @param orderId 订单Id
     */
    public List AuthorizationInfo(String orderId) throws Exception{
        String sql = "select user_id,customer_id from mag_order where id = '"+orderId+"' and  order_type='2'";
        List list = sunbmpDaoSupport.findForList(sql);
        String user_id = "";
        String customer_id ="";
        if(list!=null && list.size()>0){
            Map map = (Map) list.get(0);
            user_id = map.get("user_id").toString();
            customer_id=map.get("customer_id").toString();
        }
        sql ="select customer_id as customerId,complete,task_id as taskId,create_time as createTime," +
                "platform_type as platformType from " +
                "mag_authorization where customer_id='"+customer_id+"' order by complete desc";
        list =sunbmpDaoSupport.findForList(sql);
        return list;
    }

    /** 依据授权Id 更改授权状态
     * @parm taskId 授权id
     */
    public  void perfectingAuthorizationInfo(String taskId)  throws  Exception{
        String sql="update mag_authorization set complete='1'," +
                    "alter_time='"+ DateUtils.getCurrentTime(DateUtils.STYLE_10) + "'"
                    +" where task_id='"+taskId+"'";
        sunbmpDaoSupport.exeSql(sql);
    }
}
