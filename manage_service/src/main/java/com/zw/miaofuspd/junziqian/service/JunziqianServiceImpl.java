package com.zw.miaofuspd.junziqian.service;

import com.junziqian.service.JunziqianService;
import com.zw.miaofuspd.facade.dict.service.ISystemDictService;
import com.zw.miaofuspd.junziqian.samples.pres.PresFileLinkSample;
import com.zw.miaofuspd.junziqian.samples.sign.ApplySignFileSample;
import com.zw.service.base.AbsServiceBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author xiahaiyang
 * @Create 2017年11月16日11:24:31
 **/
@Service("junziqianServiceImpl")
public class JunziqianServiceImpl extends AbsServiceBase implements JunziqianService {

    @Autowired
    private ISystemDictService iSystemDictService;

    @Override
    public String printJunziqian(Map map) throws Exception {
        //获取当前订单id
        String sql = "select id,applay_money from mag_order where USER_ID = '"+map.get("userId").toString()+"' and CREAT_TIME =(SELECT MAX(creat_time) from mag_order where USER_ID = '"+map.get("userId").toString()+"') and order_type='2'";
        Map orderMap = sunbmpDaoSupport.findForMap(sql);
        String orderId = orderMap.get("id").toString();
        String contractAmount = orderMap.get("applay_money").toString();
        map.put("contractAmount",contractAmount);
        map.put("services_url",iSystemDictService.getInfo("junziqian.services_url"));
        map.put("appSecrete",iSystemDictService.getInfo("junziqian.appSecrete"));
        map.put("appKey",iSystemDictService.getInfo("junziqian.appKey"));
        String contractNo = ApplySignFileSample.getJunziqian(map);
        if (contractNo != null){
            sql = "UPDATE mag_order set contract_no ='"+contractNo+"' where id = '"+orderId+"';";
            sunbmpDaoSupport.exeSql(sql);
        }
        return contractNo;
    }

    @Override
    public String getJunziqian(Map map)throws Exception {
        map.put("services_url",iSystemDictService.getInfo("junziqian.services_url"));
        map.put("appSecrete",iSystemDictService.getInfo("junziqian.appSecrete"));
        map.put("appKey",iSystemDictService.getInfo("junziqian.appKey"));
        String pdfUrl =PresFileLinkSample.getJunziqian(map);
        return pdfUrl;
    }
}
