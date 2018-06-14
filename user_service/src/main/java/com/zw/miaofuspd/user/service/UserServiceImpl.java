package com.zw.miaofuspd.user.service;

import com.zw.miaofuspd.facade.user.service.IUserService;
import com.zw.service.base.AbsServiceBase;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <strong>Title : 辅助进件提交接口<br>
 * </strong> <strong>Description : </strong>@类注释说明写在此处@<br>
 * <strong>Create on : 2017年02月16日<br>
 * </strong>
 * <p>
 * <strong>Copyright (C) Vbill Co.,Ltd.<br>
 * </strong>
 * <p>
 *
 * @author department:技术开发部 <br>
 *         username:zh-pc <br>
 *         email: <br>
 * @version <strong>zw有限公司-运营平台</strong><br>
 *          <br>
 *          <strong>修改历史:</strong><br>
 *          修改人 修改日期 修改描述<br>
 *          -------------------------------------------<br>
 *          <br>
 *          <br>
 */
@Service
public class UserServiceImpl extends AbsServiceBase implements IUserService {
    @Override
    public Map getCustomerInfoByCustomerId(String customerId) {
        String sql = "SELECT magCustomer.ID AS id, magCustomer.CARD AS card,magCustomer.PERSON_NAME AS customerName,magCustomer.TEL as tel " +
                "FROM mag_customer magCustomer where magCustomer.is_identity = 1 AND magCustomer.ID ='"+customerId+"'";
        return sunbmpDaoSupport.findForMap(sql);
    }
}
