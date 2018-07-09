package com.api.service.ds;

/**
 * 数据同步基础服务
 * @author 陈清玉 create by 2018-07-09
 */
public  interface IDSBaseServer {
    /**
     * 浙商 code
     */
     String ZS_CODE = "316";

    /**
     * 根据开户行银行code判断是否是他行（0：浙商；1：非浙商）
     * @param bankCode 银行code
     * @return （0：浙商；1：非浙商）
     */
    static String getOtherFlag(String bankCode){
        return ZS_CODE.equals(bankCode) ? "0" : "1";
    }
}
