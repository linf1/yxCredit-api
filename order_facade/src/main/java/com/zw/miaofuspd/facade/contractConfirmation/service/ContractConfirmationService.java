package com.zw.miaofuspd.facade.contractConfirmation.service;

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
}
