package com.api.model.common;

/**
 * 碧友信对接接口返回对象
 * @author  陈清玉
 */
public class BYXResponse {
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


}