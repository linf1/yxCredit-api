package com.zw.miaofuspd.api.bankcard;

import com.api.model.BYXSettings;
import com.api.model.bankcard.BankcardRequest;
import com.api.model.bankcard.BankcardSettings;
import com.api.model.common.BYXRequest;
import com.api.model.common.BYXResponse;
import com.api.service.bankcard.IBankcardServer;
import com.base.util.GeneratePrimaryKeyUtils;
import com.base.util.StringUtils;
import com.zw.api.HttpClientUtil;
import com.zw.service.base.AbsServiceBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 银行卡四要素
 * @author  陈清玉
 */
@Service(IBankcardServer.BEAN_KEY)
public class BankcardServerImpl extends AbsServiceBase implements IBankcardServer{
    private final Logger LOGGER = LoggerFactory.getLogger(BankcardServerImpl.class);
    @Autowired
    private BankcardSettings bankcardSettings;

    @Autowired
    private BYXSettings byxSettings;


    @Override
    public BYXResponse authsms(BankcardRequest bankcardRequest) throws Exception {
        Map<String,String> param  = new HashMap<>(7);
        param.put("merchantNumber",bankcardSettings.getMerchantNumber());
        param.put("merchantOrder",bankcardRequest.getMerchantOrder());
        param.put("merchantNeqNo",bankcardRequest.getMerchantNeqNo());
        param.put("cardId",bankcardRequest.getCardId());
        param.put("realName",bankcardRequest.getRealName());
        param.put("bankCardNo",bankcardRequest.getBankCardNo());
        param.put("reservePhone",bankcardRequest.getReservePhone());
        if (StringUtils.isNotEmpty(bankcardRequest.getUserId())) {
            param.put("userId",bankcardRequest.getUserId());
        }
        final String result = HttpClientUtil.post(bankcardSettings.getRequestUrl(),BYXRequest.getBYXRequest(param, byxSettings), byxSettings.getHeadRequest());
        return BYXResponse.getBYXResponse(result, byxSettings);
    }

    @Override
    public BYXResponse authconfirm(BankcardRequest bankcardRequest) throws Exception {
        Map<String,String> param  = new HashMap<>(3);
        param.put("merchantNumber",bankcardSettings.getMerchantNumber());
        param.put("merchantOrder",bankcardRequest.getMerchantOrder());
        param.put("merchantNeqNo",bankcardRequest.getMerchantNeqNo());
        param.put("smsCode",bankcardRequest.getSmsCode());
        final String byxRequestJson = BYXRequest.getBYXRequest(param, byxSettings);
        //发起请求
        final String result = HttpClientUtil.post(bankcardSettings.getAuthConfirmUrl(),byxRequestJson,byxSettings.getHeadRequest());
        return BYXResponse.getBYXResponse(result, byxSettings);
    }

    @Override
    public BYXResponse getBankList() throws Exception {
        final String result = HttpClientUtil.post(bankcardSettings.getBankListUrl(),BYXRequest.getBYXRequest(null, byxSettings),byxSettings.getHeadRequest());
        return BYXResponse.getBYXResponse(result,byxSettings);
    }

    @Override
    public BYXResponse getSubBankList(String regionId,String bankCode) throws Exception {
        Map<String,Object> paramMap = new HashMap<>(1);
        paramMap.put("regionId",regionId);
        paramMap.put("bankCode",bankCode);
        final String result = HttpClientUtil.post(bankcardSettings.getSubBankListUrl(), BYXRequest.getBYXRequest(paramMap, byxSettings),byxSettings.getHeadRequest());
        return BYXResponse.getBYXResponse(result,byxSettings);
    }

    @Override
    public BYXResponse getCityList(String provinceId) throws Exception {
        Map<String,Object> paramMap = new HashMap<>(1);
        paramMap.put("provinceId",provinceId);
        final String result = HttpClientUtil.post(bankcardSettings.getCityListUrl(),BYXRequest.getBYXRequest(paramMap, byxSettings),byxSettings.getHeadRequest());
        return BYXResponse.getBYXResponse(result,byxSettings);
    }

    @Override
    public BYXResponse getProvinceList() throws Exception {
        final String result = HttpClientUtil.post(bankcardSettings.getProvinceListUrl(),BYXRequest.getBYXRequest(null, byxSettings),byxSettings.getHeadRequest());
        return BYXResponse.getBYXResponse(result,byxSettings);
    }



    @Override
    public Integer saveBankcard(BankcardRequest bankcardRequest) {
        String id = GeneratePrimaryKeyUtils.getUUIDKey();
        StringBuilder  sql  = new StringBuilder("INSERT INTO app_bank_card (id,card_id,bank_card_no,merchant_order,merchant_neq_no) ");
        sql.append(" VALUES ( '");
        sql.append(id).append("','").append(bankcardRequest.getCardId()).append("','")
                .append(bankcardRequest.getBankCardNo()).append("','")
                .append(bankcardRequest.getMerchantOrder()).append("','")
                .append(bankcardRequest.getMerchantNeqNo()).append("')");
       return sunbmpDaoSupport.executeSql(sql.toString());
    }

    @Override
    public List findBankcard(BankcardRequest bankcardRequest) {
        StringBuilder sql   = new StringBuilder("select merchant_order,merchant_neq_no,create_time from app_bank_card where");
        sql.append(" card_id = '").append(bankcardRequest.getCardId())
                .append("' and bank_card_no = '") .append(bankcardRequest.getBankCardNo())
                .append("' and state ='0' ORDER BY create_time DESC LIMIT 1");
        return sunbmpDaoSupport.findForList(sql.toString());
    }

    @Override
    public Integer updateState(BankcardRequest bankcardRequest) {
        StringBuilder sql = new StringBuilder("update app_bank_card set state ='1' where ");
        sql.append(" merchant_neq_no = '").append(bankcardRequest.getMerchantNeqNo())
        .append("' and merchant_order = '").append(bankcardRequest.getMerchantOrder()).append("' ");
        return  sunbmpDaoSupport.executeSql(sql.toString());
    }

    @Override
    public List<Map> findSysBankcardInfoByCustId(String custId) {
        StringBuilder sql = new StringBuilder("SELECT id, bank_name, bank_number, bank_subbranch_id, bank_subbranch,tel,card,card_number, cust_name, prov_id, prov_name, city_id, city_name ,create_time,bank_type ");
        sql.append("FROM sys_bank_card where is_authcard = '1' and cust_id = '").append(custId).append("'");
        return  sunbmpDaoSupport.findForList(sql.toString());
    }

    @Override
    public List<Map> findSysBankCardInfo(BankcardRequest bankcardRequest) {
        StringBuilder sql = new StringBuilder("SELECT id, bank_name, bank_number, bank_subbranch_id, bank_subbranch,tel,card,card_number, cust_name, prov_id, prov_name, city_id, city_name ,create_time,bank_type ");
        sql.append("FROM sys_bank_card where is_authcard = '1' ");
        if(StringUtils.isNotBlank(bankcardRequest.getCardId())){
            sql.append(" and card = '").append(bankcardRequest.getCardId()).append("'");
        }
        return  sunbmpDaoSupport.findForList(sql.toString());
    }
}
