package com.zw.miaofuspd.api.ds;

import com.api.model.BYXSettings;
import com.api.model.common.BYXRequest;
import com.api.model.common.BYXResponse;
import com.api.model.ds.PFLoanRequest;
import com.api.model.ds.PFLoanSettings;
import com.api.service.ds.AbsDSBaseServer;
import com.api.service.ds.IPFLoanServer;
import com.zw.api.HttpClientUtil;
import com.zw.miaofuspd.api.poxy.ApiProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 保存放款账户接口
 * @author 韩梅生
 */
@Service(IPFLoanServer.BEAN_KEY)
public class PFLoanServerImpl extends AbsDSBaseServer implements IPFLoanServer {

    @Autowired
    private PFLoanSettings pFLoanSettings;


    @Autowired
    private BYXSettings byxSettings;

    @Override
    public BYXResponse saveLoanMoney(PFLoanRequest request ) throws Exception {
        if (request != null){
            request.setOtherFlag(getOtherFlag(request.getBankCode()));
            //final String result = HttpClientUtil.post(pFLoanSettings.getRequestUrl(), BYXRequest.getBYXRequest(request, byxSettings), byxSettings.getHeadRequest());
            final String result = new ApiProxy(pFLoanSettings).apiInvoke(BYXRequest.getBYXRequest(request, byxSettings), byxSettings.getHeadRequest());
            return BYXResponse.getBYXResponse(result, byxSettings);
        }
        return  BYXResponse.error();
    }

}
