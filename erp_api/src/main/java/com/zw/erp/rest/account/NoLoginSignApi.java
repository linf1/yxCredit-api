package com.zw.erp.rest.account;

import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.util.HttpClientUtil;

/**
 * <strong>Title : APP免登签约状态查询接口<br>
 * </strong> <strong>Description : </strong>@类注释说明写在此处@<br>
 * <strong>Create on : 2017年3月13日<br>
 * </strong>
 * <p>
 * <strong>Copyright (C) Vbill Co.,Ltd.<br>
 * </strong>
 * <p>
 *
 * @author department:技术开发部 <br>
 *         username:zhangtao <br>
 *         email:<br>
 * @version <strong>zw有限公司-运营平台</strong><br>
 *          <br>
 *          <strong>修改历史:</strong><br>
 *          修改人 修改日期 修改描述<br>
 *          -------------------------------------------<br>
 *          <br>
 *          <br>
 */
public class NoLoginSignApi {
    /**
     * APP免登签约状态查询接口
     *
     * @param host
     * @param tel 手机号
     * @return APP免登签约状态信息
     * @throws Exception
     */
    public JSONObject checkNoLoginSignByTel(String host, String tel
                               ) throws Exception {
        String url = host + "/account/noLoginSign?mobile=" + tel;
        String data = HttpClientUtil.getInstance().sendHttpGet(url);
        return JSON.parseObject(data);
    }

}
