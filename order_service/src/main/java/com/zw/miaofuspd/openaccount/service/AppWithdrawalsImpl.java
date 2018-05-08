package com.zw.miaofuspd.openaccount.service;

import com.base.util.DateUtils;
import com.zw.miaofuspd.facade.entity.AppUserInfo;
import com.zw.miaofuspd.facade.openaccount.service.IAppWithdrawalsService;
import com.zw.service.base.AbsServiceBase;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("appWithdrawalsServiceImpl")
public class AppWithdrawalsImpl extends AbsServiceBase implements IAppWithdrawalsService {
    @Override
    public Map getOpenAccountInfo(String customer_id) {
        Map damap=new HashMap();
//        String orderSql  = "select id from mag_order where customer_id='"+customer_id+"' and state != '0'and state != '4' and state != '9' and state != '3'and state != '6' ORDER BY CREAT_TIME desc limit 1";
//        List orderList = sunbmpDaoSupport.findForList(orderSql );
//        if (orderList.isEmpty()) {
//            damap.put("status","isNoOrder");
//            damap.put("flag", false);
//            damap.put("msg", "您当前未完成进件申请,无法进行提现操作");
//            return damap;
//        }
        //客户是否开户

        String isAccountSql  = "select user_id,count_name,bank_card,account_bank,account_branch_bank,account_bank_id,tel,account_province,account_city from mag_customer_account where customer_id='"+customer_id+"' and state='0'";
        List list1 = sunbmpDaoSupport.findForList(isAccountSql );
        if (list1.isEmpty()) {
            damap.put("status","isNoOpenAccount");
            damap.put("flag", false);
            damap.put("msg", "您未开户,请前往开户页面进行开户后在进行操作");
            return damap;
        }
        Map ybMap=(Map)list1.get(0);
        //获取订单号
        Map orderIdMap = sunbmpDaoSupport.findForMap("select id,manageFee,quickTrialFee from mag_order where customer_id = '"+customer_id+"' and fee_state = '0' and order_type ='1' ORDER BY CREAT_TIME desc limit 1 ");
        String manageFee=(String)orderIdMap.get("manageFee");
        String quickTrialFee=(String)orderIdMap.get("quickTrialFee");
        damap.put("ybMap",ybMap);
        damap.put("manageFee",manageFee);
        damap.put("quickTrialFee",quickTrialFee);
        damap.put("flag", true);
        damap.put("msg", "信息获取成功");
        return damap;
    }


    /**
     * 提现即发送报文
     * @return
     * @throws Exception
     */
    @Override
    public Map ybWithdrawal(String red_money, String repay_money, AppUserInfo userInfo, String idJson) throws Exception {
        Map resultMap=new HashMap();
        //获取订单号
        Map orderIdMap = sunbmpDaoSupport.findForMap("select id,applay_money from mag_order where USER_ID = '"+userInfo.getId()+"' and order_type='1' ORDER BY CREAT_TIME desc limit 1");
        String order_id=(String)orderIdMap.get("id");
        String redId = UUID.randomUUID().toString();//红包id
        String nowtime = DateUtils.getDateString(new Date());
        List list = new ArrayList();
        String selectSql = "";
        if("".equals(idJson)){
            selectSql = "select id,money from mag_red_info where id in('') and is_withdraw='0'";
        }else{
            selectSql = "select id,money from mag_red_info where id in("+idJson+") and is_withdraw='0'";
        }
        List redList = sunbmpDaoSupport.findForList(selectSql);
        Map redMap = new HashMap();
        double redMoney1 = 0.0;
        if(redList!=null && redList.size()>0){
            for(int i=0;i<redList.size();i++){
                redMap = (Map) redList.get(0);
                redMoney1 += Double.valueOf(redMap.get("money").toString());
            }
        }
        if(redMoney1 != Double.valueOf(red_money)){
            resultMap.put("flag",false);
            resultMap.put("msg","数据错误,请刷新页面重新提交");
            return resultMap;
        }
        String type = "";
        Double withdrawal_amount = 0.0;
        if(!"0".equals(red_money) && "0".equals(repay_money)){//说明只有红包
            type = "0";
            withdrawal_amount = Double.valueOf(red_money);
        }else if("0".equals(red_money) && !"0".equals(repay_money)){//说明只有借款金额
            type = "1";
            withdrawal_amount = Double.valueOf(repay_money);
        }else if(!"0".equals(red_money) && !"0".equals(repay_money)){//红包加借款金额
            type = "2";
            withdrawal_amount = Double.valueOf(red_money) + Double.valueOf(repay_money);
        }
        String withdrawalAmount = String.format("%.2f",withdrawal_amount);
        String redSql = "insert into mag_withdrawal (id,withdrawal_amount,type,user_id,order_id,state,CREATE_TIME,ALTER_TIME,redAmount,principalAmount) values" +
                "('" + redId + "','" + withdrawalAmount + "','"+type+"','" + userInfo.getId() + "','" + order_id + "','0','"
                + nowtime + "','" + nowtime + "','" + red_money + "','" + repay_money + "')";
        sunbmpDaoSupport.exeSql(redSql);
        //将订单表的申请金额数目更新为空
        String updateOrderSql = "update mag_order set applay_money = '0',loan_amount='"+withdrawalAmount+"' where id = '"+order_id+"'";
        list.add(updateOrderSql);
        sunbmpDaoSupport.exeSql(list);
        String updateSql = "";
        if("".equals(idJson)){
            updateSql = "update mag_red_info set is_withdraw ='1' where id in('')";
        }else{
            updateSql = "update mag_red_info set is_withdraw ='1' where id in("+idJson+")";
        }
        sunbmpDaoSupport.exeSql(updateSql);
        resultMap.put("flag",true);
        resultMap.put("loanAmount",withdrawalAmount);
        resultMap.put("msg","您的提现申请已提交成功");
        return resultMap;
    }

    /**
     * 获取提现明细
     * @param appUserInfo
     * @return
     */
   @Override
    public Map getYbWithdrawalList(AppUserInfo appUserInfo) {
        Map returnMap = new HashMap();
        String userId = appUserInfo.getId();//用户id
        String withdrawaSql = "SELECT * FROM (select money, '1' as type,create_time from mag_red_info  where invite_code='"+userId+"'\n" +
                "union all \n" +
                "select withdrawal_amount as money, '3' as type, CREATE_TIME as create_time from mag_withdrawal  where user_id='"+userId+"'\n" +
                "union all   SELECT  amount as money, '2' as type, CREAT_TIME as create_time from mag_order  where user_id='"+userId+"'\n" +
                ")  t1  \n" +
                "\n" +
                "\n" +
                "  ORDER BY create_time DESC";
        List withdrawaList = sunbmpDaoSupport.findForList(withdrawaSql);
       returnMap.put("withdrawaList",withdrawaList);
       return returnMap;
    }
}



