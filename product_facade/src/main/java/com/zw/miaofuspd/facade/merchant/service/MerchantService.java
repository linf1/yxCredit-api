package com.zw.miaofuspd.facade.merchant.service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/22 0022.
 */
public interface MerchantService {
    /**
     * 获取该业务下所有的商户列表
     * @param salesmanId
     * @return
     * @throws Exception
     */
    List getMerchantList(String salesmanId) throws Exception;
    /**
     * 根据商户的名称，进行模糊查询
     * @param merchantSearchInfo 模糊搜索商店名称的内容
     * @param  salesmanId 办单员id
     * @return
     * @throws Exception
     */
    List selectMerchantByInfo(String merchantSearchInfo,String salesmanId) throws Exception;


    /**
     * @param merchantId 商户Id
     * 通过商户id获取商户的名字
     */
    Map getMerchantById(String merchantId);

    /**
     * 查询改商户下是否超过单笔限额以及每日进件笔数
     * @param merchantId
     * @return
     * @throws Exception
     */
    Map checkMerchantQuota(String merchantId,String allMoney,String downMoney) throws Exception;
}
