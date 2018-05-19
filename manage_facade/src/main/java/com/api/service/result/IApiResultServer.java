package com.api.service.result;

import com.api.model.result.ApiResult;

import java.util.List;

/**
 * 远程调用API结果服务
 * @author 陈清玉
 */
public interface IApiResultServer {
    String BEAN_KEY = "apiResultServerImpl";

    /**
     * 保存数据
     * @param result 参数实体
     * @return 影响行数
     */
    int insertApiResult(ApiResult result) throws Exception;

    /**
     * 查询数据
     * @param result 参数实体
     * @return 影响行数
     */
    List<ApiResult> selectApiResult(ApiResult result) throws Exception;
}
