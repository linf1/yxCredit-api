package com.zw.miaofuspd.activity.service;

import com.zw.activity.service.ActivityService;
import com.zw.miaofuspd.facade.dict.service.ISystemDictService;
import com.zw.service.base.AbsServiceBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("activityServiceImpl")
public class ActivityServiceImpl extends AbsServiceBase implements ActivityService {
    @Autowired
    private ISystemDictService iSystemDictService;

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
            String selectSql="SELECT  id AS activityId ,activity_title AS activityTitle," +
                    "activity_content AS activityContent,activity_type AS activityType, " +
                    "activity_url AS activityUrl,activity_img_addr AS activityImgAddr," +
                    "activity_img_url AS activityImgUrl,activity_state AS activityState," +
                    "activity_time AS activityTime," +
                    "DATE_FORMAT(STR_TO_DATE(create_time,'%Y%m%d%H%i%s'),'%Y-%c-%d %H:%i:%s') AS createTime," +
                    "DATE_FORMAT(STR_TO_DATE(alter_time,'%Y%m%d%H%i%s'),'%Y-%c-%d %H:%i:%s') AS alterTime," +
                    "DATE_FORMAT(STR_TO_DATE(release_time,'%Y%m%d%H%i%s'),'%Y-%c-%d %H:%i:%s') AS releaseTime," +
                    "platform_type AS platformType,priority AS priority " +
                    "FROM activity_manage WHERE activity_state='1' ORDER BY priority LIMIT 3 ";

            List bannerList = sunbmpDaoSupport.findForList(selectSql);
            if (bannerList!=null){
                //String host = iSystemDictService.getInfo("contract.host");
                resMap.put("res_code",1);
                resMap.put("res_msg","Banner已上线列表获取成功！");
                //resMap.put("host",host);
                resMap.put("bannerList",bannerList);

            }else {
                resMap.put("res_code",2);
                resMap.put("res_msg","Banner已上线列表获取失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            resMap.put("res_code",2);
            resMap.put("res_msg","Banner已上线列表获取失败！");

        }
        return resMap;
    }
}
