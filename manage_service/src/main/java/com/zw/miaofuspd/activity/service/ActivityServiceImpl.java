package com.zw.miaofuspd.activity.service;

import com.zw.activity.service.ActivityService;
import com.zw.service.base.AbsServiceBase;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("activityServiceImpl")
public class ActivityServiceImpl extends AbsServiceBase implements ActivityService {


    /**
     * 获取已上架Banner列表
     * @author 仙海峰
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, String> getBannerList() throws Exception {
        Map resMap=new HashMap();
        try {
            String selectSql="SELECT id AS activityId ,activity_title AS activityTitle," +
                    "activity_content AS activityContent,activity_type AS activityType, " +
                    "activity_url AS activityUrl,activity_img_addr AS activityImgAddr," +
                    "activity_img_url AS activityImgUrl,activity_state AS activityState," +
                    "activity_time AS activityTime,create_time AS createTime,alter_time AS alterTime," +
                    "release_time AS releaseTime,platform_type AS platformType,priority AS priority " +
                    "FROM activity_manage WHERE activity_state='1' ";
            List bannerList = sunbmpDaoSupport.findForList(selectSql);
            if (bannerList!=null){
                resMap.put("bannerList",bannerList);
                resMap.put("res_msg","Banner已上线列表获取成功！");
            }else {
                resMap.put("res_msg","Banner已上线列表获取失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            resMap.put("res_msg","Banner已上线列表获取失败！");

        }
        return resMap;
    }
}
