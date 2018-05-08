package com.zw.miaofuspd.util;

/**
 * Created by Administrator on 2018/1/3 0003.
 */
public class TaoBaoConstant {
    /**
     * 根据系统返回码获取相应的信息
     *
     * @param code
     * @return
     */
    public static String  getTaoBaoMsg(String code) {
        switch (code) {
            case "0":
                return "请求成功";
            case "1000":
                return "网络错误";
            case "1001":
                return "用户名或者密码错误";
            case "1002":
                return "验证码错误";
            case "1004":
                return "op参数错误";
            case "1005":
                return "用户不存在";
            case "1006":
                return "参数错误";
            case "2000":
                return "账号安全问题";
            case "4000":
                return "其他未知错误";
            case "4001":
                return "登陆需要绑定手机";
            case "4002":
                return "新注册账号无法登陆";
            case "4003":
                return "用户正在抓取中，为终止状态，避免重复登录和抓取导致正在抓取的任务执行失败";
            case "4004":
                return "很抱歉,您的账户可能被盗,已被监管,请电话申诉开通";
            case "4005":
                return "非常抱歉！您的账户存在严重违规情况已作“冻结”账户处理";
            case "4006":
                return "该账户已被冻结，暂时无法登陆";
            case "4007":
                return "账户已申请挂失，暂时无法登陆";
            case "4008":
                return "该账户存在安全风险，重置登陆密码和支付密码";
            case "4009":
                return "没有绑定支付宝账号";
            case "4010":
                return "密码输入错误达到上限";
            case "4023":
                return "淘宝账号被锁定需登录激活";
            case "4030":
                return "淘宝账号登陆中，请勿重复登陆，为终止状态，避免用户频繁重复登录，\n" +
                        "导致发送手机短信验证码达到上线。";
            case "5010":
                return "短信发送频繁,请稍后重试";
            case "5011":
                return "短信提交超时";
            case "6010":
                return "请使用支付宝app,扫描二维码";
            case "6011":
                return "二维码扫描成功,但用户在支付宝 APP 端,未点击确定允许在 PC 端登陆";
            case "6012":
                return "二维码已经失效 ";
            case "7002":
                return "业务处理中，需要进行轮询查询，为非终止状态码，其他所有状态码均为\n" +
                        "终止状态码，并不需要进行轮询查询。";
            case "7003":
                return "登录成功";
            case "7004":
                return "抓取成功";
        }
        return "未知错误";
    }

}
