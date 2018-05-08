package com.zw.erp.ws.crmorderservice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.util.TraceLoggerUtil;
import com.zw.erp.ws.crmorderservice.proxy.CrmOrderServiceImplService;
import com.zw.erp.ws.crmorderservice.proxy.ICrmOrderService;

import java.net.URL;

/**
 * <strong>Title : 查询还款计划<br>
 * </strong> <strong>Description : </strong>@类注释说明写在此处@<br>
 * <strong>Create on : 2016年11月4日<br>
 * </strong>
 * <p>
 * <strong>Copyright (C) ZW Co.,Ltd.<br>
 * </strong>
 * <p>
 *
 * @author department:技术开发部 <br>
 *         username:yinlei <br>
 *         email:yin_lei@suixingpay.com <br>
 * @version <strong>zw有限公司-运营平台</strong><br>
 *          <br>
 *          <strong>修改历史:</strong><br>
 *          修改人 修改日期 修改描述<br>
 *          -------------------------------------------<br>
 *          <br>
 *          <br>
 */

public class GetCrmpaycontrolsListByCrmOrderIdApi  {
  // private static Logger logger = Logger.getLogger("communication");

    /**
     * 查询还款计划  还需要通过Erpm crm_order_id查询，此时不需要查询数据库
     *
     * @param crmOrderId crm_order_id订单编号
     * @param pageIndex  查询的页码
     * @param pageSize   每页的条数
     * @return
     * @throws Exception
     */
    public JSONObject getCrmpaycontrolsListByErpCrmOrderIdApi(String wsdlUrl,
                                                              String crmOrderId, int pageIndex, int pageSize) throws Exception {
        try {
            URL url = new URL(wsdlUrl);
            CrmOrderServiceImplService crmOrderService = new CrmOrderServiceImplService(url);
            ICrmOrderService iCrmOrderService = crmOrderService.getCrmOrderServiceImplPort();
            JSONObject paraObject = new JSONObject();
            paraObject.put("crmOrderId", crmOrderId);
            JSONObject speJson = new JSONObject();
            speJson.put("page", pageIndex);
            speJson.put("size", pageSize);
            TraceLoggerUtil.info("接口发送--->" + speJson);
            TraceLoggerUtil.info(crmOrderId + ",接口发送--->" + speJson);
            String retData = iCrmOrderService
                    .getCrmpaycontrolsListByCrmOrderId(paraObject.toJSONString(),
                            speJson.toJSONString());
            TraceLoggerUtil.info("接口返回--->" + retData);
            TraceLoggerUtil.info(crmOrderId + ";接口返回--->" + retData);
            return JSON.parseObject(retData);
        } catch (Exception ex) {
            TraceLoggerUtil.error("接口异常--->", ex);
            throw ex;
        }
    }

//
//    @Override
//    public void doControl() throws HsException {
//
//    }
//
//    public static void main(String[] args) throws Exception {
//        System.out.println(new GetCrmpaycontrolsListByCrmOrderIdApi().getCrmpaycontrolsListByErpCrmOrderIdApi("2c908a8859875dad0159876f117900fa", 0, 30));
//    }
}
