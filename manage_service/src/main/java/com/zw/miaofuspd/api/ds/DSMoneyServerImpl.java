package com.zw.miaofuspd.api.ds;

import com.api.model.BYXSettings;
import com.api.model.common.BYXRequest;
import com.api.model.common.BYXResponse;
import com.api.model.ds.DSMoneyRequest;
import com.api.model.ds.DSMoneySettings;
import com.api.service.ds.AbsDSBaseServer;
import com.api.service.ds.IDSMoneyServer;
import com.zw.api.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 借款人及放款账户数据同步服务实现
 * @author 陈清玉
 */
@Service(IDSMoneyServer.BEAN_KEY)
public class DSMoneyServerImpl extends AbsDSBaseServer implements IDSMoneyServer {

    @Autowired
    private DSMoneySettings dsMoneySettings;


    @Autowired
    private BYXSettings byxSettings;

    @Override
    public BYXResponse saveBorrowerAndAccountCard(DSMoneyRequest request ) throws Exception {
        if (request != null){
            //设置浙商
            request.setOtherFlag(getOtherFlag(request.getBankCode()));
            request.setBorrowerChannel(ACCOUNT_CHANNEL);
            request.setAccountChannel(ACCOUNT_CHANNEL);
            final String result = HttpClientUtil.post(dsMoneySettings.getRequestUrl(), BYXRequest.getBYXRequest(request, byxSettings), byxSettings.getHeadRequest());
            return BYXResponse.getBYXResponse(result, byxSettings);
        }
        return  BYXResponse.error("参数缺失");
    }

}
