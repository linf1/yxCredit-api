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

    private Object res_data;

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

    public Object getRes_data() {
        return res_data;
    }

    public void setRes_data(Object res_data) {
        this.res_data = res_data;
    }

    public static BYXResponse ok(Object res_data){
        BYXResponse response = new BYXResponse();
        response.setRes_code("1");
        response.setRes_data(res_data);
        return response;
    }
    public static BYXResponse ok(){
        BYXResponse response = new BYXResponse();
        response.setRes_code("1");
        return response;
    }
    public static BYXResponse error(String res_msg){
        BYXResponse response = new BYXResponse();
        response.setRes_code("0");
        response.setRes_msg(res_msg);
        return response;
    }
    public static BYXResponse error(){
        BYXResponse response = new BYXResponse();
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

}