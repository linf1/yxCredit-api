package com.zw.miaofuspd.facade.openaccount.service;


import com.zw.miaofuspd.facade.entity.AppUserInfo;

import java.util.Map;

/**
 * <strong>Title : <br>
 * </strong> <strong>Description : </strong>@类注释说明写在此处@<br>
 * <strong>Create on : 2017年11月06日<br>
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
public interface IAppWithdrawalsService {
    /**
     * 提现接口
     * @param red_money
     * @param repay_money
     * @param userInfo
     * @param idJson
     * @return
     * @throws Exception
     */
    Map ybWithdrawal(String red_money, String repay_money, AppUserInfo userInfo, String idJson)throws Exception;

    /**
     * 获取开户信息
     * @param customer_id
     * @return
     */
    Map getOpenAccountInfo(String customer_id);

    /**
     * 查询提现明细记录
     * @param userInfo
     * @return
     */
    Map getYbWithdrawalList(AppUserInfo userInfo);
}
