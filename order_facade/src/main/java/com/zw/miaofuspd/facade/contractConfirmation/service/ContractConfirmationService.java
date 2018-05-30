package com.zw.miaofuspd.facade.contractConfirmation.service;

import java.util.List;
import java.util.Map;

/**
 * <strong>Title : <br>
 * </strong> <strong>Description : </strong>@类注释说明写在此处@<br>
 * <strong>Create on : 2018年03月12日<br>
 * </strong>
 * <p>
 * <strong>Copyright (C) Vbill Co.,Ltd.<br>
 * </strong>
 * <p>
 *
 * @author department:技术开发部 <br>
 *         username:Win7 <br>
 *         email: <br>
 * @version <strong>zw有限公司-运营平台</strong><br>
 *          <br>
 *          <strong>修改历史:</strong><br>
 *          修改人 修改日期 修改描述<br>
 *          -------------------------------------------<br>
 *          <br>
 *          <br>
 */
public interface ContractConfirmationService {

    /**
     * 获取借款基本信息
     * @param userId
     * @return
     */
    Map getContractUserInfo(String userId);

    /**
     * 获取借款协议
     * @param mapInfo
     * @return
     */
    Map getContractAgreement(Map mapInfo);

    /**
     * 设置已签约的借款协议的合同地址
     * @param orderId
     */
    void setAlreadyContractAgreement(String orderId, String nameUrl);

    /**
     * 通过orderId 获取已签约的借款协议
     * @param orderId
     * @return
     */
    String getAlreadyContractAgreement(String orderId);

    Map getOrderInfo(String orderId);//获取订单信息

    /******************************碧有信*****************************/
    /**
     * 合同信息获取
     * @param orderId
     * @return
     */
    Map getContractInfo(String orderId);
    /**
     * 添加合同
     * @param map
     * @return
     */
    int insertContract(Map map);
    /**
     * 获取合同
     * @param orderId
     * @return
     */
    List<Map> getContractByOrderId(String orderId);

    /**
     * 获取合同
     * @param contractId
     * @return
     */
    Map getContractById(String contractId);

    /**
     * 更新订单状态
     * @param params
     */
    void updateOrderStatus(Map params);
    /**
     * 更新资产推送状态
     * @param orderId,assetState
     */
    void updateAssetStatus(String orderId, String assetState);

}
