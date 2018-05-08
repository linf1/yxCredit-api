package com.zw.miaofuspd.facade.merchant.service;

import java.util.Map;

/**
 * Created by Administrator on 2017/11/22 0022.
 */
public interface QrCodeService {
    /**
     * 生成二维码接口
     * @param inMap
     * @return
     * @throws Exception
     */
    Map createQRcode(Map inMap,String idJson) throws Exception;
}
