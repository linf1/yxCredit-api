package com.zw.miaofuspd.share.service;

import com.base.util.DateUtils;
import com.zw.miaofuspd.facade.share.service.IShareService;
import com.zw.service.base.AbsServiceBase;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ShareServiceImpl extends AbsServiceBase implements IShareService {
    /**
     * 分享推广
     * @param code
     * @return
     * @throws Exception
     */
    public Map getShare(String code) throws Exception {
        Map detailMap =new HashMap();
        String shareSql = "select code,name from mag_recommend where state='1' and code='"+code+"'";
        Map shareMap = sunbmpDaoSupport.findForMap(shareSql);
        if(shareMap==null){
            detailMap.put("mag","fail");
            return detailMap;
        }
        detailMap.put("mag","success");
        return detailMap;
    }

    @Override
    public Map saveShareInfo(Map map) {
        Map result=new HashMap();
        String id= UUID.randomUUID().toString();
        String creatTime = DateUtils.getDateString(new Date());
        String saveSql="insert into mag_recommend_list(id,recommend_code,create_time,is_register,user_id,user_name)values('"+id+"','"+map.get("code").toString()
                +"','"+creatTime+"','0','','')";
        sunbmpDaoSupport.exeSql(saveSql);
        result.put("id",id);
        return result;
    }

    @Override
    public Map updateInfo(String id,String phone)throws Exception {
        String nowTime = DateUtils.getDateString(new Date());
        String sql="select id from app_user where tel='"+phone+"'";
        Map map=sunbmpDaoSupport.findForMap(sql);
        if(map==null){
            map.put("msg","信息获取失败!");
            return map;
        }
        String userId=map.get("id").toString();
        String updateSql="update mag_recommend_list set is_register='"+1+"',create_time='"+nowTime+"',user_id='"+userId+"'  where id='"+id+"'";
        sunbmpDaoSupport.exeSql(updateSql);
        map.put("msg","推广信息已更新！");
         return map;
    }
}
