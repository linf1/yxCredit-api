package com.zw.miaofuspd.red.service;

import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.red.service.RedService;
import com.zw.service.base.AbsServiceBase;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/10 0010.
 */
@Service
public class RedServiceImpl extends AbsServiceBase implements RedService{
    /**
     * 获取红包列表,红包总金额,可用金额
     * @param appUserInfo
     * @return
     * @throws Exception
     */
    @Override
    public Map getRedList(AppUserInfo appUserInfo) throws Exception {
        Map returnMap = new HashMap();
        String userId = appUserInfo.getId();//用户id
        String redSql = "select  id,customer_name,tel,invite_tel, money,create_time,is_withdraw from mag_red_info  where invite_code='"+userId+"'and is_withdraw = '0' and red_type='1'ORDER BY create_time DESC ";
        List redList = sunbmpDaoSupport.findForList(redSql);
        double redAllMoney = 0.0;
        double redAvailableAmount = 0.0;
        if(redList!=null && redList.size()>0){
            for(int i=0;i<redList.size();i++){
                Map redMap = new HashMap();
                redMap = (Map) redList.get(i);
                redAllMoney += Double.valueOf(redMap.get("money").toString());
                if("0".equals(redMap.get("is_withdraw"))){
                    redAvailableAmount+=Double.valueOf(redMap.get("money").toString());
                }
            }
        }
        returnMap.put("redAllMoney",redAllMoney);
        returnMap.put("redAvailableAmount",redAvailableAmount);
        returnMap.put("redList",redList);
        return returnMap;
    }

    @Override
    public Map getUnUserList(AppUserInfo appUserInfo) throws Exception {
        Map returnMap = new HashMap();
        String userId = appUserInfo.getId();//用户id
        String withdrawaSql = "select id,customer_name,tel,invite_tel, money,create_time,is_withdraw from mag_red_info  where invite_code='"+userId+"' and red_type='1' and is_withdraw='0' ORDER BY create_time DESC ";
        List redList = sunbmpDaoSupport.findForList(withdrawaSql);
        if(redList!=null && redList.size()>0){
            returnMap.put("withdrawaList",redList);
            return returnMap;
        }

        returnMap.put("msg","您当前没有折扣红包金额！");
        return returnMap;
    }
}
