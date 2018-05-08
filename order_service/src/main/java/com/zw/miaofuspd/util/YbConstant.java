package com.zw.miaofuspd.util;



public class YbConstant {

    /**
     * 根据系统返回码获取相应的信息
     *
     * @param retCode
     * @return
     */
    public String getYbSystemMsg(String retCode) {
        switch (retCode) {
            case "1":
                return "请求成功";
            case "2001":
                return "没有证书";
            case "2002":
                return "登录名与证书不匹配";
            case "2003":
                return "证书用户名格式错误";
            case "2004":
                return "证书起始日志格式错误";
            case "2005":
                return "证书截止日志格式错误";
            case "2006":
                return "证书序列号格式错误";
            case "2008":
                return "证书已过期";
            case "2009":
                return "证书验证失败";
            case "0010":
                return "总金额与明细合计有误";
            case "0011":
                return "总比数与明细合计有误";
            case "0012":
                return "商户ip不合法(ip位于黑名单)";
            case "0013":
                return "商户ip不匹配|商户ip不合法";
            case "0014":
                return "打款文件已处理，请勿重复提交";
            case "0015":
                return "存在重复打款记录";
            case "0016":
                return "签名验证失败";
            case "0017":
                return "打款总额超限";
            case "0018":
                return "打款总笔数超限";
            case "0019":
                return "单笔限额超限";
            case "0020":
                return "商户不存在";
            case "0021":
                return "商户状态不正常";
            case "0022":
                return "收款卡号有误（或者卡号在系统黑名单中）";
            case "0023":
                return "打款单笔处理处理失败";
            case "0031":
                return "省市有误";
            case "0032":
                return "未开通操作权限(批量打款,查询)";
            case "0033":
                return "CA证书有误 ";
            case "0034":
                return "批次号不合法或重复";
            case "0035":
                return "订单号不合法或重复";
            case "0036":
                return "不支持该银行编码和银行名称";
            case "0037":
                return "金额不合法";
            case "0038":
                return "非法的账户名称";
            case "0039":
                return "手续费收取方式不合法";
            case "0040":
                return "邮箱格式不正确";
            case "0041":
                return "非法的手机号";
            case "0042":
                return "留言字数超过限制";
            case "0043":
                return "加急参数非法或者不支持该银行的加急出款";
            case "0044":
                return "商户未开启代付、代发功能";
            case "0045":
                return "商户账户已冻结";
            case "0046":
                return "商户可用打款余额不足";
            case "0047":
                return "请求报文 xml 格式有误";
            case "0048":
                return "查询记录不存在";
            case "0049":
                return "输入页码为 0 或未输入";
            case "0051":
                return "必填项不能为空";
            case "0052":
                return "日期转换错误";
            case "0053":
                return "数据签名失败";
            case "0054":
                return "时间段或批次号至少添一项";
            case "0055":
                return "查询日期不能在当前日期之后";
            case "0056":
                return "未开通结算款汇入";
            case "0057":
                return "请求 xml 报文里的 cmd 参数为空";
            case "0058":
                return "参数 必填项,不能为空";
            case "0059":
                return "日期格式不合法";
            case "0060":
                return "is_Repay 参数不合法";
            case "0061":
                return "参数不合法";
            case "0062":
                return "起始日期不能大于结束日期";
            case "0063":
                return "时间间隔不能超过 7 天";
            case "0064":
                return "商户未开通交易限额业务";
            case "0066":
                return "金额超过打款单笔限额";
            case "0067":
                return "日累计限额超过日限额";
            case "0068":
                return "月累计限额超过月限额";
            case "0069":
                return "接口名称不存在";
            case "0070":
                return "该商户关系不存在";
            case "0071":
                return "该机构无下级机构";
            case "0074":
                return "报文非法";
            case "0075":
                return "支行信息和省市信息不能同时为空";
            case "0076":
                return "全部打款记录未通过校验";
            case "0077":
                return "不支持该银行对公打款";
            case "0078":
                return "bank_Code 和 bank_Name 不能同时为空";
            case "0079":
                return "金额不足以支付手续费";
            case "0080":
                return "未开通周转易";
            case "0081":
                return "未开通周转易+";
            case "0082":
                return "未开通对公出款";
            case "0083":
                return "未开通对私出款";
            case "0084":
                return "重复预约";
            case "0085":
                return "非预约期";
            case "0086":
                return "超过预约额度或易宝备款金额，不能实时出款";
            case "0087":
                return "账户非法或余额不足预约缴费失败";
            case "0088":
                return "预约金额超限";
            case "0089":
                return "未预约";
            case "0090":
                return "已缴费不能修改";
            case "0091":
                return "当前时间不可预约";
            case "0092":
                return "账户余额不足";
            case "0093":
                return "此银行额度不足，建议切换其它银行";
            case "0095":
                return "开户行所在省不能为空或不合法";
            case "0096":
                return "开户行所在市不能为空或不合法";
            case "0097":
                return "开户银行不能为空";
            case "0098":
                return "开户银行不合法";
            case "0099":
                return "开户银行全称不合法";
            case "0100":
                return "开户银行全称不能为空";
            case "1001":
                return "未开通日结通";
            case "1002":
                return "出款银行卡号非绑定银行卡";
            case "1003":
                return "代理商额度不足";
            case "1004":
                return "日结通总额度不足";
            case "1005":
                return "日结通对私额度不足";
            case "1006":
                return "收款方银行为非实时到账银行";
            case "1007":
                return "未开通非工作日加急版";
            case "1008":
                return "未开通工作日加急版";
            case "1009":
                return "未开通非工作日普通版";
            case "1010":
                return "未开通工作日普通版";
            case "1011":
                return "打款状态码不合法";
        }
        return "系统异常";
    }

    /**
     * 打款状态码对应的信息
     * @param r1Code
     * @return
     */
    public  String getYbPayMsg(String r1Code) {
        switch (r1Code) {
            case "0025":
                return "已接收";
            case "0026":
                return "没有证书";
            case "0027":
                return "登录名与证书不匹配";
            case "0028":
                return "证书用户名格式错误";
            case "0029":
                return "证书起始日志格式错误";
        }
        return "未知";
    }


    /**
     * 银行状态对应的信息
     * @param bankStatus
     * @return
     */
    public  String getYbBankStateMsg(String bankStatus) {
        switch (bankStatus) {
            case "S":
                return "已成功";
            case "I":
                return "银行处理中";
            case "F":
                return "款失败,原因见打款明细查询 fail_Desc";
            case "W":
                return "未出款";
        }
        return "未知";
    }

}
