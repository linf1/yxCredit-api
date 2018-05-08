package com.zw.miaofuspd.myset.service;

import com.base.util.DateUtils;
import com.base.util.StringUtils;
import com.zw.miaofuspd.facade.entity.AppUserInfo;
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
    /**
     * 秒付金服保存投诉信息
     * @param userinfo
     * @param content
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, String> feedbackAdd(AppUserInfo userinfo, String content) throws Exception {
        Map outMap=new HashMap();
        try {
            String id = UUID.randomUUID().toString();//生成id
            String creatTime = DateUtils.getDateString(new Date());//获得当前时间
            String title = "投诉";
            StringBuffer insql = new StringBuffer("INSERT INTO app_suggestion "
                    + "(id,user_id,tel,title,content,person_id,"
                    + "person_name,creat_time,alter_time) VALUES('"+id+"','"+userinfo.getId()+"','"+userinfo.getTel()+"',"
                    + "'"+title+"','"+content+"','"+(StringUtils.isNotEmpty(userinfo.getPerson_id())?userinfo.getPerson_id():"")+"','");
            if(StringUtils.isNotEmpty(userinfo.getName())){
                insql.append(userinfo.getName());
            }else{
                insql.append(userinfo.getTel());
            }
            insql.append("','"+creatTime+"','"+creatTime+"')");
            sunbmpDaoSupport.exeSql(insql.toString());//插入表中
            outMap.put("flag",true);
            outMap.put("msg","保存成功");
        }catch (Exception e){
            outMap.put("flag",false);
            outMap.put("msg","意见反馈失败");
        }
        return outMap;
    }
}

