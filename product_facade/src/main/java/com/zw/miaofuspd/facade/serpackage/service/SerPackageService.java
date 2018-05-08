package com.zw.miaofuspd.facade.serpackage.service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/18 0018.
 */
public interface SerPackageService {
    /**
     * 获取服务包数据
     * @return
     * @throws Exception
     */
    List getSerPackage(String periods) throws Exception;

    /**
     * 通过idJson串 将服务包添加到对应的id里面
     * @param idJson
     * @return
     */
    void setSerPackageInfoByIds(String orderId,String idJson);
    /**
     * 通过idJson串 获取所有id所对应的服务包信息
     * @param idJson
     * @return
     */
    List getSerPackageInfoByIds(String idJson);

    /**
     * 根据前置服务包的提前几个月还的月数,查询出对应的月付还款服务包
     * @param afterMonth
     * @return
     */
    List getMonthPackage(String afterMonth,String periods);

    /**
     * 通过orderId获取前置提前还款服务包
     * @param orderId
     * @return
     */
    Map getSerPcgByOderId(String orderId);
}
