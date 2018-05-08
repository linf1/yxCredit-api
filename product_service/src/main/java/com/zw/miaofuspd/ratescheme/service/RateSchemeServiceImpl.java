package com.zw.miaofuspd.ratescheme.service;

import com.zw.miaofuspd.facade.ratescheme.service.RateSchemeService;
import com.zw.service.base.AbsServiceBase;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/18 0018.
 */
@Service
public class RateSchemeServiceImpl extends AbsServiceBase implements RateSchemeService {
    /**
     * 获取利率方案接口
     * @param
     * @return
     * @throws Exception
     */
    @Override
    public List getRateScheme() throws Exception {
        String sql = "SELECT id AS productId,pro_name AS productName FROM pro_crm_product WHERE STATUS = '1' AND parent_id = " +
                "(SELECT id FROM pro_crm_product WHERE pro_series_type = '1' AND STATUS = '1') ORDER BY productName ASC";
        List ratelist = sunbmpDaoSupport.findForList(sql);
        return ratelist;
    }
    /**
     * 根据产品id获取产品的期数和费率信息
     * @param productId
     * @return
     * @throws Exception
     */
    @Override
    public Map getFeeInfo(String productId) throws Exception {
        String productSql = "SELECT product_periods as periods,year_rate as yearRate,month_rate as monthRate,li_xi as dateRate ,yuqi_fee as yuqiFee" +
                " FROM mag_product_fee WHERE product_id = (SELECT id FROM pro_working_product_detail where status = '1' " +
                "and crm_product_id='"+productId+"' ORDER BY periods +0 asc)";
        Map productMap = sunbmpDaoSupport.findForMap(productSql);
        //查询产品类型和产品名称
        String sql = "SELECT a.pro_name as proTypeName,b.pro_name as proNameName from pro_crm_product a " +
                "LEFT JOIN pro_crm_product b on a.id= b.parent_id where b.id = '"+productId+"' and a.status = '1'";
        Map map = sunbmpDaoSupport.findForMap(sql);
        productMap.put("proTypeName",map.get("proTypeName").toString());
        productMap.put("proNameName",map.get("proNameName").toString());
        return productMap;
    }

    @Override
    public Map getProductInfo(String productId) {
        String proSql="SELECT id ,diy_type as diyType,diy_days as diyDays FROM pro_working_product_detail where status = '1' and crm_product_id='"+productId+"' ORDER BY periods +0 asc";
        return sunbmpDaoSupport.findForMap(proSql);
    }
}
