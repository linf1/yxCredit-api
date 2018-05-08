package com.zw.miaofuspd.facade.merchant.service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/22 0022.
 */
public interface MerchandiseService {
//    /**
//     * 根据商户id查询出改商户下所有激活的商品
//     * @param merchantId
//     * @return
//     * @throws Exception
//     */
//    List getMerchandiseList(String merchantId,String firstIndex,String pageSize) throws Exception;

    /**
     * 获取所有商品类型
     * @return
     */
    List getMerchantdiseType(String id,String type);



    /**
     * 根据商品名称查询，商户id,查询该商户下的品牌
     * @param merchandiseBrandName
     * @param merchantId
     * @return
     * @throws Exception
     */
    List getMerchandiseInfo(String merchandiseBrandName,String merchantId) throws Exception;

    /**
     * 添加商品类型
     * @param map
     * @return
     */
    Map addMerchandiseType(Map map);

    /**
     * 添加商品与商户关联
     * @return
     */
     void addMerchandiseToMerchant(String merchandiseId,String merchantId);
    Map addMerchandiseRelMerchant(String merchandiseId,String merchantId,String imgUrl);
    /**
     * 查询商户的所有的商品
     * @param merchantId 商户id
     * @return
     */
    List getMerchandiseList(String merchantId) throws Exception;
    /**
     * 根据商户id查找商户
     * @param merchantId 商户Id
     * @return
     */
    Map findMerchantNameById(String merchantId) throws Exception;
    /**
     * 模糊搜索商户的商品
     * @param  merchartId 商户id
     * @param merchandiseSearch 搜索内容
     * @return
     */
    public List searchMerchandiseList(String merchartId,String salesmanId,String merchandiseSearch) throws Exception;
    /**
     * 展示搜索历史的前几条数据
     * @param merchantId 商户Id
     * @param salesmanId 办单员Id
     * @return
     */

    public  List showSearchList(String merchantId,String salesmanId) throws Exception;


    /**
     * @param employeeId  员工Id
     * @param searchContent 搜索内容
     * 插入搜索记录
     * 在订单模糊搜索的时候使用
     */

    /**
     * 查找搜索历史表中的搜索历史条数
     * @param merchantId 商户Id
     * @param salesmanId 办单员Id
     * @return
     */
    public Map  getSearchHistoryNum(String merchantId,String salesmanId)throws Exception;


    /**
     * 根据搜索历史Id删除搜索历史
     * @param  id
     * @merchantId 商户id
     * @salesmanId 办单员id
     */
    public void deleteSearchHistoryById(String id,String merchantId,String salesmanId) throws Exception;
    /**
     * 清空搜索历史
     * @return
     * @throws Exception
     */
   public void  dropSerarchHistroy (String merchantId,String salesmanId) throws  Exception;

    /**
     * 插入一条搜索记录
     * @return
     * @throws Exception
     */
    public void insertSearchRecord(Map map) throws  Exception;

    /**
     * 获取所有的商品类型
     * @return
     * @throws Exception
     */
    public List getAllMerchantdiseType() throws  Exception;

    public List getAllType(String parentId) throws  Exception;

    /**
     * 商品图片和商品id关联保存在mag_customer_image表中
     * @param src 商品图片的阿里云地址
     * @param merchandiseId 商户Id
     * @return
     * @throws Exception
     */
    public void addMerchandiseImagesToMerchandise(String src,String merchandiseId) throws Exception;

    /**
     * 根据商品的id获取到商品的信息
     * @param id
     * @return
     */
    Map getMerchandiseInfoById(String id);
}
