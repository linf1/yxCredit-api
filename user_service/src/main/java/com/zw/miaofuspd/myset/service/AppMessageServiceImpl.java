package com.zw.miaofuspd.myset.service;

import com.base.util.DateUtils;
import com.base.util.StringUtils;
import com.zw.miaofuspd.facade.dict.service.IDictService;
import com.zw.miaofuspd.facade.myset.service.IAppMessageService;
import com.zw.service.base.AbsServiceBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AppMessageServiceImpl extends AbsServiceBase implements IAppMessageService {
    @Autowired
    private IDictService dictService;
    public List getPage()throws  Exception{
        List list = dictService.getDictJson("投诉建议类型");
        return list;
    }
    public boolean saveSuggestion(Map map){
        String user_id = (String) map.get("user_id");
        String content = (String) map.get("content");
        String s_type = (String) map.get("s_type");
        String s_type_name = (String) map.get("s_type_name");
        String sql = "INSERT into app_suggestion " +
                "(id,title,user_id,tel,content,person_name,creat_time,s_type) VALUES('"+
                UUID.randomUUID().toString()+"','"+s_type_name+"','"+user_id+"',(SELECT tel from app_user where id = '"+user_id+"')"+
                ",'"+content+"',(SELECT realname from app_user where id = '"+user_id+"')"+",'"+
                DateUtils.getCurrentTime(DateUtils.STYLE_10)
                +"','"+s_type+
                "')";
        try {
            sunbmpDaoSupport.exeSql(sql);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public List getFaqList(){
        String sql = "SELECT problem_name,problem_content,problem_type,(SELECT name from mag_dict_detail where code=problem_type and dict_id=(SELECT id from mag_dict WHERE name = '问题类型')) as problem_type_name from problem_manage where problem_state='1' and platform_type='1' order by problem_type";
        List list =sunbmpDaoSupport.findForList(sql);
        return list;
    }

    /**
     * 获取系统消息和订单消息
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public Map getMessageList(String id) throws Exception {
        Map resultMap = new HashMap();
        //查询出全部的消息
        String sql = "SELECT title,content,state,creat_time,alter_time,order_id,update_state,order_state from app_message where 1=1 and user_id='"+id+"' and order_type='2'  order by creat_time desc ";
        List list = sunbmpDaoSupport.findForList(sql);
        resultMap.put("list",list);
        return resultMap;
    }
    @Override
    public List getPictureInfo(String type) throws Exception {
        String sql = "SELECT activity_img_url from activity_manage where activity_state = '1' and activity_img_addr = '"+type+"'and platform_type='1'";
        List list = sunbmpDaoSupport.findForList(sql);
        return list;
    }
    public Map getUnMessage(String id) throws Exception{
        Map returnMap = new HashMap();
        String sql = "SELECT count(id) as num from app_message where 1=1 and user_id='"+id+"'and state = '0' and order_type='2' ";
        Map map = sunbmpDaoSupport.findForMap(sql);
        returnMap.put("count",map.get("num"));
        return returnMap;
    }

    /**
     * 设置消息为已读
     * @param id
     * @throws Exception
     */
    public void setReadyMsg(String id) throws Exception{
        String sql = "update app_message set state = '2' where user_id = '"+id+"' and state = '0'and order_type='2' ";
        sunbmpDaoSupport.exeSql(sql);
    }
}
