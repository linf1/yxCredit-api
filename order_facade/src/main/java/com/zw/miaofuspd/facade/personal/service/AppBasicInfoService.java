package com.zw.miaofuspd.facade.personal.service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/28.
 */

public interface AppBasicInfoService {

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
    Map addApplyInfo(Map<String, Object> paramMap) throws Exception;

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
    Map addBasicCustomerInfo(Map<String, String> map) throws Exception;

    Map getLinkMan(String customerId) throws Exception;

    /**
     * 判断是否填写过草稿信息
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

   /**
    * @author:韩梅生
    * @Description  用户信息强规则
    * @Date 20:14 2018/5/14
    * @param
    */
   Map checkCustomerInfo(String userId,String card) throws Exception;

   /**
    * @author:韩梅生
    * @Description 获取实名认证信息
    * @Date 18:17 2018/5/16
    * @param
    */

   Map getRealName(String userId) throws  Exception;

   /**
    * @author:韩梅生
    * @Description
    * @Date 19:35 2018/5/16
    * @param
    */
   Map saveRealName(Map map) throws  Exception;

    /**
     *  按id获取用户信息 create by 陈清玉 2018-05-16
     * @param orderId 用户ID
     * @return 订单信息
     */
    Map findByOrderId(String orderId);

    /**
     * 数据同步完成后更新个人信息 create by 陈清玉 2018-05-16
     * @param userID 同步接口返回userId
     * @param accountId 同步接口返回accountId
     * @param custId 个人信息Id
     * @return 影响行数
     */
    int updateSynById(String userID,String accountId,String custId);

    /**
     * @author:韩梅生
     * @Description 取消订单
     * @Date 17:02 2018/5/19
     * @param
     */
    int cancelOrder(String orderId);

    /**
     * @author:韩梅生
     * @Description 获取授权状态
     * @Date 17:02 2018/5/19
     * @param
     */
    Map getEmpowerStatus(String userId);

    /**
     * @author 韩梅生
     * @date 16:13 2018/5/22
     * 根据订单id获取订单详情
     */
    Map getOrderDetailById(String orderId,String customerId);

    /**
     * @author 韩梅生
     * @date 18:49 2018/5/23
     * 获取实名认证状态
     */
    Map getAuthorStatus(String userId);



    /**
     * 根据登录用户ID获取是否实名认证create by 陈清玉
     * @param userId 登录用户ID
     * @return TRUE 已认证 FALSE 未认证
     */
    Boolean isAuthentication(String userId);

    /**
     * @author 韩梅生
     * @date 9:33 2018/5/25
     * 更新资产推送状态
     */
    void updateAssetStatus(String orderId,boolean flag);

    /**
     * @author 韩梅生
     * @date 11:19 2018/6/6.
     * 获取产品期限
     */
    List getPeriods(String productName);


    /**
     * @author 韩梅生
     * @date 14:24 2018/6/19
     * 获取影像资料
     */
    List getImageInfos(String custoemrId);
    /**
     * @author 韩梅生
     * @date 14:24 2018/6/19
     * 上传影像资料
     */
    Map uploadImageInfos(String customerId,List<String> MultipartFileList);

    /**
     * @author 韩梅生
     * @date 13:40 2018/6/20
     * 获取站内信
     */

    List getInstationMsg(String userId);

    boolean getInstationStatus(String userId);

    void  updateInstationMsg(String userId);

    void deleteImageInfos(String id,String customerId);

    /**
     * 根据code获取获取时间
     * @param code
     * @return
     */
    Map getExpireDays(String code);

    /**
     * 查询个人风控信息
     * @param sourceCode 风控来源类别
     * @param customerId 个人id
     * @return 一条风控信息
     */
    List<Map> findEmpowerStatus(String sourceCode,String customerId);

    /**
     * 新增银行卡信息
     * @param map
     */
    Map addBankInfo(Map map);

    /**
     * 获取银行卡信息
     * @param customerId 个人id
     * @return 银行卡列表
     */
    List<Map> getBankInfo(String customerId);

    /**
     * 获取借款人主键
     * @param userId
     * @return 借款人主键
     */
    String getUserBorrowerId(String userId);

    List<Map> getCustomerIdByid(String userId);

    /**
     * 判断是否填写个人信息
     */
    boolean checkPersonalInfo(String userId);



}
