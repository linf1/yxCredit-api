package com.api.service.tongdun;

import com.alibaba.fastjson.JSONObject;
import com.api.model.common.ApiCommonResponse;
import com.constants.ApiConstants;
import com.api.model.tongdun.ReportAO;
import com.api.model.tongdun.TongDunRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 同盾代理层
 * @author 陈清玉
 */
public class TongDunProxy implements ITongDunApiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TongDunProxy.class);

    private ITongDunApiService tongDunApiService;

    //private CrMagicDataResultServiceImpl crMagicDataResultService;

    public TongDunProxy(ITongDunApiService tongDunApiService) {
        this.tongDunApiService = tongDunApiService;
       // this.crMagicDataResultService = crMagicDataResultService;
    }

    @Override
    public ReportAO queryReportId(TongDunRequest request)  throws IOException {
        return tongDunApiService.queryReportId(request);
    }

    @Override
    public String queryReportInfo(ReportAO reportId) throws Exception {
        return tongDunApiService.queryReportInfo(reportId);
    }

    public ApiCommonResponse invokeTongDunApi(TongDunRequest request) {
        LOGGER.info("同盾--获取报告信息 API调用开始.");
        long startTime = System.currentTimeMillis();
        long costTime = 0;
        ApiCommonResponse response;
      //TODO 根据  去数据库查询 如果有数据就不进行API调用（可加上时间期限）
        //response = crMagicDataResultService.getByDataResult(new CrMagicDataResult(ApiConstants.API_TONGDUN_KEY,request.getIdNo()));
        response = null;
        if (response == null) {
            response = new ApiCommonResponse();
            try {
                //获取报告ID
                final ReportAO reportAO = queryReportId(request);
                if (!reportAO.isSuccess()) {
                    LOGGER.info("同盾-查询获取reportId失败或参数验证不通过");
                    response.setResponseCode(ApiConstants.STATUS_INPUT_ERROR);
                    response.setResponseMsg(ApiConstants.STATUS_INPUT_ERROR_MSG);
                    return response;
                }
                Thread.sleep(3000);
                LOGGER.info("同盾--获取报告信息 API调用参数：{}", request.toString());
                final String result = queryReportInfo(reportAO);
                costTime = System.currentTimeMillis() - startTime;
                LOGGER.info("同盾--获取报告信息{}", result);
                final JSONObject jsonObject = JSONObject.parseObject(result);
                //查到数据
                if (jsonObject.getBoolean(ApiConstants.REPORT_SUCCESS_KEY)) {
                    response.setResponseCode(ApiConstants.STATUS_SUCCESS);
                    response.setResponseMsg(ApiConstants.STATUS_SUCCESS_MSG);
                    response.setOriginalData(jsonObject);
                    //保存数据到数据库
//                    final int rowNum = saveResultData(request, result);
//                    if(!(rowNum > 0)){
//                        LOGGER.info("同盾-报告信息----持久化失败");
//                    }
                } else {
                    response.setResponseCode(jsonObject.getString(ApiConstants.REASON_CODE_KEY));
                    response.setResponseMsg(jsonObject.getString(ApiConstants.REASON_DESC_KEY));
                }
            } catch (Exception e) {
                LOGGER.info("同盾-返回数据解析出错" + e);
                response.setResponseCode(ApiConstants.STATUS_DATASOURCE_INTERNAL_ERROR);
                response.setResponseMsg(ApiConstants.STATUS_DATASOURCE_INTERNAL_ERROR_MSG);
            }
        }else{
            LOGGER.info("同盾-命中数据库记录");
       }
        LOGGER.info("同盾-获取报告信息.[耗时:{}毫秒]", costTime);
        return response;
    }

    /**
     * 持久化调用API的数据
     * @param request 请求参数 {@link TongDunRequest}
     * @param jsonObject API请求到的数据
     * @return 影响行数
     */
//    private int saveResultData(TongDunRequest request, String jsonObject) {
//        CrMagicDataResultWithBLOBs record = new CrMagicDataResultWithBLOBs();
//        record.setId(GeneratePrimaryKeyUtils.getUUIDKey());
//        record.setCode(ApiConstants.STATUS_SUCCESS);
//        record.setMessage(ApiConstants.STATUS_SUCCESS_MSG);
//        record.setTaskId("");
//        record.setChannelType(ApiConstants.API_TONGDUN_KEY);
//        record.setChannelCode("");
//        record.setChannelAttr(ApiConstants.API_TONGDUN_TITLE);
//        record.setChannelSrc(ApiConstants.API_TONGDUN_TITLE);
//        record.setRealName(request.getName());
//        record.setIdentityCode(request.getIdNo());
//        record.setUserMobile(request.getPhone());
//        record.setUserName(request.getName());
//        record.setCreatedTime(DateUtils.formatDate(DateUtils.STYLE_1));
//        record.setTaskData(jsonObject);
//        record.setLostData("");
//        return crMagicDataResultService.saveDataResult(record);
//    }
}
