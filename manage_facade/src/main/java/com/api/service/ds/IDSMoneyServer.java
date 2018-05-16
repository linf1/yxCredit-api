package com.api.service.ds;

import com.api.model.common.BYXResponse;
import com.api.model.ds.DSMoneyRequest;

import java.io.IOException;

/**
 * 借款人及放款账户数据同步服务
 * @author 陈清玉
 */
public interface IDSMoneyServer {

    String BEAN_KEY = "dSMoneyServerImpl";

    /**
     * 借款人及放款账户数据同步接口
     * @param request
     * @return
     */
    BYXResponse saveBorrowerAndAccountCard(DSMoneyRequest request) throws Exception;

}
