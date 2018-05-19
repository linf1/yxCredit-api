package com.api.service.credit;


import com.api.model.credit.CreditRequest;
import com.api.model.credit.CreditResultAO;
import com.api.model.credit.CreditResultRequest;
import com.api.model.tongdun.ReportAO;
import com.api.model.tongdun.TongDunRequest;

import java.io.IOException;
import java.util.Map;

/**
 * 征信API服务接口
 * @author luochaofang
 */
public interface ICreditApiService {
    String BEAN_ID = "creditApiServiceImpl";
    /**
     * 征信账号验证
     * @param request 请求对象 {@link CreditRequest}
     * @throws  IOException 调用异常
     * @return 返回对象
     */
    CreditResultAO validateAccount(CreditRequest request) throws IOException;

    /**
     * 保存征信数据
     * @param map {@link Map}
     * @throws  Exception 调用异常
     * @return 返回接口数据
     */
    void saveCreditInfo(Map map) throws Exception;

}
