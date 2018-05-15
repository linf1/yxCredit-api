package com.zw.miaofuspd.api.ds;

import com.alibaba.fastjson.JSONObject;
import com.api.model.ds.DSMoneyRequest;
import com.api.model.ds.DSMoneySettings;
import com.api.service.ds.IDSMoneyServer;
import com.zw.api.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 借款人及放款账户数据同步服务实现
 * @author 陈清玉
 */
@Service(IDSMoneyServer.BEAN_KEY)
public class DSMoneyServerImpl implements IDSMoneyServer {

    //private DSMoneySettings dsMoneySettings;

    @Override
    public String saveBorrowerAndAccountCard(DSMoneyRequest request) {
//        try {
////          return HttpUtil.doPost(dsMoneySettings.getRequestUrl(), JSONObject.toJSONString(request));
////
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
        return null;
    }
}
