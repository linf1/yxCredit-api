package com.zw.miaofuspd.order.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.util.DateUtils;
import com.base.util.StringUtils;
import com.base.util.TraceLoggerUtil;
import com.zhiwang.zwfinance.app.jiguang.util.JiGuangUtils;
import com.zw.miaofuspd.facade.dict.service.IDictService;
import com.zw.miaofuspd.facade.dict.service.ISystemDictService;
import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.log.entity.OrderLogBean;
import com.zw.miaofuspd.facade.log.service.IAppOrderLogService;
import com.zw.miaofuspd.facade.user.service.IUserService;
import com.zw.miaofuspd.util.JXMConvertUtil;
import com.zw.miaofuspd.util.MapToXml;
import com.zw.miaofuspd.util.SecurityUtil;
import com.zw.miaofuspd.util.rsa.HttpPayTreUtil;
import com.zw.miaofuspd.util.rsa.RsaCodingUtil;
import com.zw.service.task.abs.AbsTask;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author xiahaiyang
 * @Create 2018年1月18日14:26:34
 **/
public class CloseOrderTask extends AbsTask {

    @Autowired
    private IDictService dictService;
    @Autowired
    private IAppOrderLogService iAppOrderLogService;
    @Autowired
    private IUserService iUserService;

    @Override
    public void doWork() throws Exception {
        logger.info("定时执行成功");
        //获取超时时间
        String over = dictService.getDictInfo("状态超时", "cssj");
        if (null == over || "".equals(over))
        {
            return;
        }
        int overTime = Integer.valueOf(over) * 60 * 60 * 1000;
        Date d = new Date(new Date().getTime() - overTime);
        String time = DateUtils.formatDate(d, "yyyyMMddHHmmss");
        //查询超时的订单
        String orderSql = "select id,state,commodity_state from mag_order where state in ('0','1','2','4','5') and order_type='2' and ALTER_TIME <= '" + time + "'";
        List orderList = sunbmpDaoSupport.findForList(orderSql);
        if (null == orderList || orderList.size() == 0)
        {
            return;
        }
        for (Map<String, Object> order : (List<Map<String, Object>>)orderList)
        {
            if ("5".equals(order.get("state")))
            {
                if (Float.valueOf((String)order.get("commodity_state")) >= 18)
                {//收取了预付款
                    continue;
                }
                else if (Float.valueOf((String)order.get("commodity_state")) == 17)
                {//收取预付款之前
                    //查询付款成功和付款中的服务包
                    String packSql = "select count(1) as num from mag_servicepag_order where (state=1 or state = 3) and order_id='" + order.get("id") + "'";
                    Map pack = sunbmpDaoSupport.findForMap(packSql);
                    if (Integer.valueOf(pack.get("num").toString()) > 0)
                    {//存在已付款或付款中的服务包
                        continue;
                    }
                }
            }

            //订单修改为关闭状态
            String updateOrder = "update mag_order set state='10',alter_time='" + DateUtils.formatDate("yyyyMMddHHmmss") + "' where id='" + order.get("id") + "'";
            sunbmpDaoSupport.exeSql(updateOrder);

            //推送关闭app消息
            AppUserInfo userInfo = iUserService.getUserByOrderId((String)order.get("id"));
            String regId = userInfo.getRegistration_id();
            String push  = "0";
            String content = dictService.getDictInfo("消息内容","gbdd");
            String title = "订单关闭";
            if(StringUtils.isNotEmpty(regId)){
                if(!JiGuangUtils.alias(title, content, regId)){
                    push = "1";
                }
            }
            String sql = "update app_message set update_state = '0' where order_id = '" + order.get("id") + "'";
            sunbmpDaoSupport.exeSql(sql);
            String messageId = UUID.randomUUID().toString();
            String user_id = userInfo.getId();//用户ID
            String date = DateUtils.getDateString(new Date());
            String messageinsertSql = "insert into app_message(id,user_id,title,content,creat_time,alter_time,jpush_state,state,update_state,msg_type,push_state,order_state,order_id,order_type)values('"+messageId+"','"+user_id+"','"+title+"','"+content+"','"+date+"','"+date+"','"+push+"','0','0','1','"+push+"','"+order.get("state")+"','"+order.get("id")+"','2')";
            sunbmpDaoSupport.exeSql(messageinsertSql);

            //记录订单日志
            OrderLogBean orderLog = new OrderLogBean();
            orderLog.setTache("订单关闭");
            orderLog.setOperatorName("系统");
            orderLog.setOperatorType("1");
            orderLog.setOrderId((String)order.get("id"));
            orderLog.setChangeValue("订单关闭");
            orderLog.setState((String)order.get("state"));
            orderLog.setCreatTime(DateUtils.getDateString(new Date()));
            orderLog.setAlterTime(DateUtils.getDateString(new Date()));
            if (iAppOrderLogService.setOrderLog(orderLog) == null) {
                TraceLoggerUtil.error( "记录订单日志失败!");
            }
        }

    }


}