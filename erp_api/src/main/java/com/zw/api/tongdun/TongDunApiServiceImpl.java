package com.zw.api.tongdun;

import com.alibaba.fastjson.JSONObject;
import com.zw.api.HttpUtil;
import com.zw.model.tongdun.ReportAO;
import com.zw.model.tongdun.TongDunRequest;
import com.zw.model.tongdun.TongDunSettings;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 同盾服务
 * @author 陈清玉
 */
@Service(ITongDunApiService.BEAN_ID)
public class TongDunApiServiceImpl implements ITongDunApiService {

    @Autowired
    private TongDunSettings tongDunSettings;

    @Override
    public ReportAO queryReportId(TongDunRequest request) throws IOException {
        Map<String,Object> params = new HashMap<>(3);
        params.put("id_number",request.getIdNo());
        params.put("mobile",request.getPhone());
        params.put("name",request.getName());
        String result = HttpUtil.doPost(tongDunSettings.getReportUrl(),params);
        return  JSONObject.parseObject(result,ReportAO.class);
    }

    @Override
    public String queryReportInfo(ReportAO reportId) throws Exception {
        return HttpUtil.doGet(tongDunSettings.getReportUrl(reportId.getReportId()));
    }

}
