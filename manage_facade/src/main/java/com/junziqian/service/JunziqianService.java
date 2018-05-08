package com.junziqian.service;

import java.io.IOException;
import java.util.Map;

public interface JunziqianService {

    /**
     * 提交合同打印电子签章
     * @param map 电子签章所需信息
     * @return
     */
    public String printJunziqian(Map map) throws Exception;

    /**
     * 电子签章下载
     * @param map 合同编号,姓名,身份证号
     * @return
     */
    public String getJunziqian(Map map) throws Exception;

}
