package com.api.model.common;

import java.util.HashMap;
import java.util.Map;

/**
 * API常量
 * @author  陈清玉
 */
public class ApiConstants {

    public static Map<String, String> statusMap = new HashMap<String, String>();

    public static final String STATUS_INPUT_PHONE_ERROR_MSG = "手机号不合法";
    public static final String STATUS_INPUT_ID_ERROR_MSG = "身份证号不合法";
    public static final String STATUS_INPUT_NAME_ERROR_MSG = "姓名不合法";

    public static final String STATUS_SUCCESS = "0";
    public static final String STATUS_SUCCESS_MSG = "操作成功";

    public static final String STATUS_ERROR = "1";
    public static final String STATUS_ERROR_MSG = "操作失败";

    public static final String STATUS_CREATE_SUCCESS = "2";
    public static final String STATUS_CREATE_SUCCESS_MSG = "阶段一操作成功";

    public static final String STATUS_SYS_ERROR = "100001";
    public static final String STATUS_SYS_ERROR_MSG = "系统错误";

    public static final String STATUS_DATASOURCE_ERROR = "100002";
    public static final String STATUS_DATASOURCE_ERROR_MSG = "网络错误";

    public static final String STATUS_DATASOURCE_TIME_OUT = "100003";
    public static final String STATUS_DATASOURCE_TIME_OUT_MSG = "数据源连接超时";

    public static final String STATUS_DATASOURCE_INPUT_ERROR = "100004";
    public static final String STATUS_DATASOURCE_INPUT_ERROR_MSG = "数据源输入参数验证失败";

    public static final String STATUS_DATASOURCE_INTERNAL_ERROR = "100005";
    public static final String STATUS_DATASOURCE_INTERNAL_ERROR_MSG = "数据源内部错误";

    public static final String STATUS_SIGN_ERROR = "100006";
    public static final String STATUS_SIGN_ERROR_MSG = "验签失败";

    public static final String STATUS_INPUT_ERROR = "100007";
    public static final String STATUS_INPUT_ERROR_MSG = "必填参数缺失或不合法";

    public static final String STATUS_DONE_TIMEOUT="100008";
    public static final String STATUS_DONE_TIMEOUT_MSG="短信验证码输入错误或超时,请重新输入";

    public static final String STATUS_INFO_NOT_FOUND="100009";
    public static final String STATUS_INFO_NOT_FOUND_MSG="未查询到数据";

    /**
     * 同盾API常量
     */
    public static final String REPORT_SUCCESS_KEY="success";
    public static final String REASON_CODE_KEY="reason_code";
    public static final String REASON_DESC_KEY="reason_desc";
    public static final String API_TONGDUN_KEY = "tongdun";
    public static final String API_TONGDUN_TITLE = "同盾";



    //成功状态
    public static final String HUA_STATUS="1";

    static {
        statusMap.put(STATUS_SUCCESS, STATUS_SUCCESS_MSG);
        statusMap.put(STATUS_ERROR, STATUS_ERROR_MSG);
        statusMap.put(STATUS_SYS_ERROR, STATUS_SYS_ERROR_MSG);
        statusMap.put(STATUS_DATASOURCE_ERROR, STATUS_DATASOURCE_ERROR_MSG);
        statusMap.put(STATUS_DATASOURCE_TIME_OUT, STATUS_DATASOURCE_TIME_OUT_MSG);
        statusMap.put(STATUS_DATASOURCE_INPUT_ERROR, STATUS_DATASOURCE_INPUT_ERROR_MSG);
        statusMap.put(STATUS_DATASOURCE_INTERNAL_ERROR, STATUS_DATASOURCE_INTERNAL_ERROR_MSG);
        statusMap.put(STATUS_SIGN_ERROR, STATUS_SIGN_ERROR_MSG);
        statusMap.put(STATUS_INPUT_ERROR, STATUS_INPUT_ERROR_MSG);
        statusMap.put(STATUS_DONE_TIMEOUT,STATUS_DONE_TIMEOUT_MSG);
        statusMap.put(STATUS_INFO_NOT_FOUND,STATUS_INFO_NOT_FOUND_MSG);
    }
}