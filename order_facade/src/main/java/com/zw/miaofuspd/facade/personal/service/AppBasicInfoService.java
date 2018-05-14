package com.zw.miaofuspd.facade.personal.service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/28.
 */

public interface AppBasicInfoService {
    Map getMiaofuBasicInfo(String customerId) throws Exception;

    /**
     * 获取联系人信息
     * @param userId 用户id
     * @param map1 联系人信息集合
     * @return
     * @throws Exception
     */
    Map updateLinkManInfo(String userId, Map map1) throws Exception;

    /**
     * @author hanmeisheng
     * 添加用户申请基本信息
     * @param paramMap
     * @return
     * @throws Exception
     */
    Map addApplyInfo(Map<String, String> paramMap) throws Exception;

    /**
     * 获取用户基本信息
     * @param orderId 订单id
     * @return
     * @throws Exception
     */
    Map getApplyInfo(String orderId) throws Exception;

    /**
     * @author:韩梅生
     * @Description 保存用户的个人信息
     * @Date 14:10 2018/5/12
     * @param
     * @return
     */
    Map addBasicInfo(Map<String, String> paramMap) throws Exception;
    /**
     * @author:韩梅生
     * @Description 获取用户的个人信息
     * @Date 14:10 2018/5/12
     * @param
     */
    Map getBasicInfo(String customerId) throws Exception;


    /**
     * 获取用户基本信息
     * @param map
     * @return
     * @throws Exception
     */
    void getBasicCustomerInfo(Map<String, String> map) throws Exception;

    Map getLinkMan(String customerId) throws Exception;
    void saveTongXunLu(String customerId,String data) throws Exception;

    /**
     * 判断是否实名认证
     * @author 韩梅生
     * @param id
     * @return
     * @throws Exception
     */
    Map getPersonInfo(String id) throws Exception;

    /**
     * 获取申请信息主页面信息
     * @author 韩梅生
     * @param id
     * @return
     * @throws Exception
     */
    Map getHomeApplyInfo(String id,String productName) throws Exception;

    /**
     * @author:韩梅生
     * @Description 获取省份信息
     * @Date 13:27 2018/5/12
     * @param
     * @return java.util.Map
     */

    List<Map> getProvinceList() throws Exception;


    /**
     * @author:韩梅生
     * @Description 获取市信息
     * @Date 13:27 2018/5/12
     * @param provinceId 省id
     * @return java.util.Map
     */

    List<Map> getCityList(String provinceId) throws Exception;

    /**
     * @author:韩梅生
     * @Description 获取市信息
     * @Date 13:27 2018/5/12
     * @param cityId 市id
     * @return java.util.Map
     */

    List<Map> getDistrictList(String cityId) throws Exception;
    /**
     * @author:韩梅生
     * @Description 一键申请
     * @Date 13:27 2018/5/12
     * @param userId 用户id
     * @return java.util.Map
     */

   Map oneClickApply(String userId) throws Exception;


}
