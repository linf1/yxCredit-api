package com.zw.miaofuspd.api.ds;

import com.alibaba.fastjson.JSONObject;
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
import com.zhiwang.zwfinance.app.jiguang.util.api.CryptoTools;
import com.zw.api.HttpClientUtil;
import com.zw.service.base.AbsServiceBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 第三方资产数据同步服务实现
 * @author 韩梅生
 */
@Service(IAssetServer.BEAN_KEY)
public class AssetServerImpl extends AbsServiceBase implements IAssetServer {

    @Autowired
    private AssetSettings assetSettings;

    @Autowired
    private BusinessSettings businessSettings;

    @Autowired
    private BYXSettings byxSettings;

    @Override
    public BYXResponse thirdAssetsReceiver(AssetRequest request ) throws Exception {
        if (request != null){
            final String updateSql = "update mag_order set asset_state = '1' where order_no = '"+request.getThirdAssetOrderNum()+"'";
            sunbmpDaoSupport.exeSql(updateSql);
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

    @Override
    public void updateAssetStatus(BYXResponse byxResponse) {
        String businessId;
        if(byxResponse != null){
            Map  paramMap = JSONObject.parseObject(byxResponse.getRes_data().toString());
            if(!StringUtils.isEmpty(paramMap.get("businessId"))){
                businessId = paramMap.get("businessId").toString();
                StringBuilder sb = new StringBuilder("update mag_order set asset_state = '");
                if(BYXResponse.resCode.success.getCode().equals(byxResponse.getRes_code())){
                    sb.append("3' where order_no = '").append(businessId).append("'");
                }else {
                    sb.append("4' where order_no = '").append(businessId).append("'");
                }
                sunbmpDaoSupport.exeSql(sb.toString());
            }
        }
    }

    /**
     * 加密解密字符串测试
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String str = "MEfCTqYuwYjHwoiCkRZf/k3M7tE8WB+vkjwqaUpUMqcCY91QPkg3+y14SuNYVoIqRveXptKuWKdJ\n" +
                "WzfBhRHaC2BCH8KYdYZSre+2+2QP02xGOGXYreM6uxzA/dw/Qs5uV8QxdHhC4WyZtAwLd2Ph4w==";

        CryptoTools cryptoTools = new CryptoTools("QEI1LQBXXAA1TWX72FUI7L5H","CVPKUJTP");
        final String decodesStr = cryptoTools.decode(str);
        System.out.println(decodesStr);

    }
}
