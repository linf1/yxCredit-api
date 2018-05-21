package com.api.service.contractsign;

import com.api.model.common.BYXResponse;
import com.api.model.contractsign.ContractSignRequest;


/**
 * 合同签章API服务接口
 * @author hanwannan
 */
public interface IContractSignService {

    String BEAN_ID = "contractSignServiceImpl";

    /**
     * 合同签章
     * @param request
     * @return
     * @throws Exception
     */
    BYXResponse signContract(ContractSignRequest request) throws Exception;
}
