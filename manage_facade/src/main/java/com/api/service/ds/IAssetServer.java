package com.api.service.ds;

import com.api.model.common.BYXResponse;
import com.api.model.ds.AssetRequest;
import com.api.model.ds.DSMoneyRequest;

import java.util.Map;

/**
 * 第三方资产接口同步
 * @author 韩梅生
 */
public interface IAssetServer {

    String BEAN_KEY = "assetServerImpl";

    /**
     * 第三方资产同步
     * @param request
     * @return
     */
    BYXResponse thirdAssetsReceiver(AssetRequest request) throws Exception;

    /**
     * 有信贷查询资产是否存在
     * @param paramMap
     * @return
     */
    BYXResponse getByBusinessId(Map paramMap) throws Exception;



}
