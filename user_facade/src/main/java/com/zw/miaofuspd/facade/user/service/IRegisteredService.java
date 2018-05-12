package com.zw.miaofuspd.facade.user.service;

import java.util.Map;

/**
 * <strong>Title : <br>
 * </strong> <strong>Description : </strong>@类注释说明写在此处@<br>
 * <strong>Create on : 2017年02月28日<br>
 * </strong>
 * <p>
 * <strong>Copyright (C) Vbill Co.,Ltd.<br>
 * </strong>
 * <p>
 *
 * @author department:技术开发部 <br>
 *         username:吕彬 <br>
 *         email: <br>
 * @version <strong>zw有限公司-运营平台</strong><br>
 *          <br>
 *          <strong>修改历史:</strong><br>
 *          修改人 修改日期 修改描述<br>
 *          -------------------------------------------<br>
 *          <br>
 *          <br>
 */
public interface IRegisteredService {
    boolean selectByTel(String tel) throws Exception;//判断手机是否已经注册
    String register(Map map) throws Exception;//秒付注册用户
    boolean selectByOnlyCode(String onlyCode);
    Map getRegClause(String type) throws Exception;//注册条款
    boolean selectByTelError(String tel) throws Exception;//判断是否是三次注册失败手机号码
    void updateTelError(String phone);

    /**
     * 新增用戶
     * @param map 用戶map
     * @return
     */
    String insertUser(Map<String,Object> map);
}
