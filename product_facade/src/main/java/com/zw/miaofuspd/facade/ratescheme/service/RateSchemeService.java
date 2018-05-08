package com.zw.miaofuspd.facade.ratescheme.service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/18 0018.
 */
public interface RateSchemeService {
    /**
     *商品贷获取利率方案
     * @return Map
     */
    List getRateScheme() throws Exception;

    /**
     * 根据产品id获取产品的期数和费率信息
     * @param productId
     * @return
     * @throws Exception
     */
    Map getFeeInfo(String productId) throws Exception;

    Map getProductInfo(String productId);
}
