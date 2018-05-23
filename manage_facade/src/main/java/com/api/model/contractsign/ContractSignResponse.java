package com.api.model.contractsign;

import com.alibaba.fastjson.JSONObject;
import com.api.model.BYXSettings;
import com.api.model.common.BYXResponse;
import com.base.util.StringUtils;
import com.google.gson.Gson;
import com.zhiwang.zwfinance.app.jiguang.util.api.CryptoTools;

import java.io.Serializable;

public class ContractSignResponse implements Serializable {
    private static final long serialVersionUID = 7135534959627078004L;
    /**
     * 1成功，0失败
     */
    private String res_code;

    /**
     * res_code为0才有值
     */
    private String res_msg;

    private String res_data;

    public String getRes_code() {
        return res_code;
    }

    public void setRes_code(String res_code) {
        this.res_code = res_code;
    }
    public String getRes_msg() {
        return res_msg;
    }

    public void setRes_msg(String res_msg) {
        this.res_msg = res_msg;
    }

    public String getRes_data() {
        return res_data;
    }

    public void setRes_data(String res_data) {
        this.res_data = res_data;
    }

    public static ContractSignResponse ok(String res_data){
        ContractSignResponse response = new ContractSignResponse();
        response.setRes_code("1");
        response.setRes_data(res_data);
        return response;
    }
    public static ContractSignResponse ok(){
        ContractSignResponse response = new ContractSignResponse();
        response.setRes_code("1");
        return response;
    }
    public static ContractSignResponse error(String res_msg){
        ContractSignResponse response = new ContractSignResponse();
        response.setRes_code("0");
        response.setRes_msg(res_msg);
        return response;
    }
    public static ContractSignResponse error(){
        ContractSignResponse response = new ContractSignResponse();
        response.setRes_code("0");
        response.setRes_msg("系统异常，请联系系统管理员！");
        return response;
    }

    public  enum  resCode{
        /**
         * 失败
         */
        fail("0"),
        /**
         * 成功
         */
        success("1");
        private String code;
        resCode(String code){
            this.code = code;
        }
        public String getCode() {
            return code;
        }
        public void setCode(String code) {
            this.code = code;
        }
    }

    /**
     * 解析内容返回结果
     * @param byxResponseJson 远程调用接口返回结果字符串
     * @return BYXResponse
     */

    public static ContractSignResponse getBYXResponse(String byxResponseJson,BYXSettings byxSettings) throws Exception {
        if(StringUtils.isEmpty(byxResponseJson)){return error();}
        CryptoTools cryptoTools = new CryptoTools(byxSettings.getDesKey(),byxSettings.getVi());
        Gson gson = new Gson();
        final ContractSignResponse response = gson.fromJson(byxResponseJson, ContractSignResponse.class);
        if(ContractSignResponse.resCode.success.getCode().equals(response.getRes_code())) {
            if(response.getRes_data() != null){
                final String decode = cryptoTools.decode(response.getRes_data().toString());
                response.setRes_data(decode);
            }

        }
        return response;
    }

    /**
     * 解析内容返回结果
     * @param byxResponseJson 远程调用接口返回结果字符串
     * @return BYXResponse
     */

    public static ContractSignResponse getBYXResponse(String byxResponseJson) throws Exception {
        if(StringUtils.isEmpty(byxResponseJson)){return error();}
        Gson gson = new Gson();
        return gson.fromJson(byxResponseJson, ContractSignResponse.class);
    }
}
