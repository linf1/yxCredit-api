package com.zw.miaofuspd.myset.service;

import com.base.util.DateUtils;
import com.zw.miaofuspd.facade.myset.service.FeedBackService;
import com.zw.service.base.AbsServiceBase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class FeedBackServiceImpl extends AbsServiceBase implements FeedBackService {


   /************************************************** 碧友信******************************************************/

    /**
     * 添加客户意见反馈
     * @author 仙海峰
     * @param userId
     * @param content
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, String> feedbackAdd(String userId, String content) throws Exception {
        Map outMap=new HashMap();
        try {
            String selectSql="SELECT mc.ID AS customerId , mc.PERSON_NAME AS customerName , au.TEL AS tel FROM app_user au LEFT JOIN mag_customer mc ON au.`id`=mc.`USER_ID` WHERE au.ID='"+userId+"'";
            Map customerMap = sunbmpDaoSupport.findForMap(selectSql);

            //获取客户ID
            String customerId = customerMap.get("customerId").toString();
            //获取客户名称
            String customerName = customerMap.get("customerName").toString();
            //获取客户电话
            String tel = customerMap.get("tel").toString();
            //生成id
            String id = UUID.randomUUID().toString();
            //获得当前时间
            String creatTime = DateUtils.getDateString(new Date());
            String title = "意见反馈";
            String insertSql="INSERT INTO app_suggestion (id,user_id,customer_id,customer_name,tel,title,content,creat_time)  " +
                             "VALUES ('"+id+"','"+userId+"','"+customerId+"','"+customerName+"','"+tel+"','"+title+"','"+content+"','"+creatTime+"')";
            //插入表中
            int count= sunbmpDaoSupport.executeSql(insertSql);
            if (count==1){
                outMap.put("res_code",1);
                outMap.put("res_msg","保存成功");
            }else {
                outMap.put("res_code",2);
                outMap.put("res_msg","保存失败");
            }

        }catch (Exception e){
            e.printStackTrace();
            outMap.put("res_code",2);
            outMap.put("res_msg","保存失败");
        }
        return outMap;
    }
}

