package com.zw.miaofuspd.api.ds;

import com.api.model.BYXSettings;
import com.api.model.common.BYXRequest;
import com.api.model.common.BYXResponse;
import com.api.model.ds.AssetRequest;
import com.api.model.ds.AssetSettings;
import com.api.model.ds.DSMoneyRequest;
import com.api.model.ds.DSMoneySettings;
import com.api.model.repayment.BusinessSettings;
import com.api.service.ds.IAssetServer;
import com.api.service.ds.IDSMoneyServer;
import com.zw.api.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 第三方资产数据同步服务实现
 * @author 韩梅生
 */
@Service(IAssetServer.BEAN_KEY)
public class AssetServerImpl implements IAssetServer {

    @Autowired
    private AssetSettings assetSettings;

    @Autowired
    private BusinessSettings businessSettings;


    @Autowired
    private BYXSettings byxSettings;

    @Override
    public BYXResponse thirdAssetsReceiver(AssetRequest request ) throws Exception {
        if (request != null){
            final String result = HttpClientUtil.post(assetSettings.getRequestUrl(), BYXRequest.getBYXRequest(request, byxSettings), byxSettings.getHeadRequest());
            return BYXResponse.getBYXResponse(result, byxSettings);
        }
        return  BYXResponse.error();
    }
    @Override
    public BYXResponse getByBusinessId(Map paramMap ) throws Exception {
        if (paramMap != null){
            final String result = HttpClientUtil.post(businessSettings.getRequestUrl(), BYXRequest.getBYXRequest(paramMap, byxSettings), byxSettings.getHeadRequest());
            return BYXResponse.getBYXResponse(result, byxSettings);
        }
        return  BYXResponse.error();
    }

}
