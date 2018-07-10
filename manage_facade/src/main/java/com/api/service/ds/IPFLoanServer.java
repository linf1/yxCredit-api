package com.api.service.ds;

import com.api.model.common.BYXResponse;
import com.api.model.ds.PFLoanRequest;

/**
 * 保存放款账户接口服务同步
 * @author 韩梅生
 */
public interface IPFLoanServer {

    String BEAN_KEY = "pFLoanServerImpl";

    /**
     * 保存账户放款接口
     * @param request
     * @return
     * @exception
     */
    BYXResponse saveLoanMoney(PFLoanRequest request) throws Exception;

}
