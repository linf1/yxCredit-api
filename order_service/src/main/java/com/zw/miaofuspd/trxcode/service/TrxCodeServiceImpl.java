package com.zw.miaofuspd.trxcode.service;

import com.zw.miaofuspd.facade.dict.service.ISystemDictService;
import com.zw.miaofuspd.facade.trxcode.service.TrxCodeService;
import com.zw.miaofuspd.facade.entity.TrxCode;
import com.zw.service.base.AbsServiceBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/5 0005.
 */
@Service
public class TrxCodeServiceImpl extends AbsServiceBase implements TrxCodeService {
    @Autowired
    public ISystemDictService iSystemDictService;

    /**
     * 91回调接口
     * @param card 身份证
     * @return
     * @throws Exception
     */
    @Override
    public List selectTrxcodeByCard(String card) throws Exception {
        List<TrxCode> listx=new ArrayList<TrxCode>();
        String companyCode = iSystemDictService.getInfo("company.code");//公司代码
        String borrowType = iSystemDictService.getInfo("borrow.type");//借款类型
        TrxCode tx=new TrxCode();
        tx.setBorrowType(borrowType);//借款类型
        tx.setCompanyCode(companyCode);//公司代码
        String sql = "select id as orderId,state as state," +
                "apply_money as borrowAmount,creat_time as createTime,periods " +
                "from mag_order where customer_id = (select id from mag_customer where card = '"+card+"') and order_type='2'";
        List<Map> list=sunbmpDaoSupport.findForList(sql);
        if(list!=null && list.size()>0){
            for(Map<String,String> trx:list){
                String orderId=trx.get("orderId");
                tx.setBorrowState("0");
                ////借款状态 0.未知1.拒贷2.批贷已放款3.批贷未放款5.审核中6.待放款
                 if("1".equals(trx.get("state")) || "2".equals(trx.get("state")) || "4".equals(trx.get("state"))){
                    tx.setBorrowState("5");
                }else if("3".equals(trx.get("state"))||"6".equals(trx.get("state"))){
                    tx.setBorrowState("1");
                }else if("5".equals(trx.get("state"))){
                    tx.setBorrowState("6");
                }
                //合同金额
                if("0".equals(tx.getBorrowState())||"2".equals(tx.getBorrowState())||"1".equals(tx.getBorrowState())||"4".equals(tx.getBorrowState())){
                    tx.setBorrowAmount("0");
                }if(trx.get("borrowAmount")!=null&&!"".equals(trx.get("borrowAmount"))) {
                    tx.setBorrowAmount(tod(trx.get("borrowAmount")));
                }else{tx.setBorrowAmount(tod(trx.get("borrowAmount")));}

                //合同日期
                if(trx.get("creatTime")!=null&&!"".equals(trx.get("creatTime"))) {
                    tx.setContractDate(change(trx.get("creatTime")));
                }else{tx.setContractDate(change(trx.get("creatTime")));}

                //批贷期数
                if("0".equals(tx.getBorrowState())||"2".equals(tx.getBorrowState())||"1".equals(tx.getBorrowState())||"2".equals(tx.getBorrowState())){
                    tx.setLoanPeriod("0");
                }else {
                    if (trx.get("periods") != null && !"".equals(trx.get("periods"))) {
                        tx.setLoanPeriod(trx.get("periods"));
                    } else {
                        tx.setLoanPeriod(trx.get("periods"));
                    }
                }
                String stateSql = "SELECT state from mag_repayment WHERE order_id='"+orderId+"' order by pay_day desc";
                List<Map> mp=sunbmpDaoSupport.findForList(stateSql);
                tx.setRepayState("0");
                if(mp!=null && mp.size()>0){//还款状态
                    for(Map<String,String> m:mp) {
//                        if ("0".equals(m.get("state"))) {
//                            tx.setRepayState("1");
//                        }
//                        if ("1".equals(m.get("state"))) {
//                            tx.setRepayState("2");
//                        }
                        tx.setRepayState("0");
                        tx.setArrearsAmount("0");
                    }
//                    String amountSql = "select * from mag_repayment";
//                    //欠款金额
//                    List<Map> li=sunbmpDaoSupport.findForList(amountSql);
//                    if(li!=null && li.size()>0){
//                        double a=todouble(li.get(0).get("total").toString());
//                        int b=toInt(li.get(0).get("qk").toString());
//                        double e=Math.rint(a*b*100000);
//                        tx.setArrearsAmount("0");
//                    }else{tx.setArrearsAmount("0");}
                }else{
                    tx.setRepayState("0");
                    tx.setArrearsAmount("0");

                }

                listx.add(tx);
            }
        }
        return listx;
    }
    //把string转换成int
    public int toInt(String s){
        int i=0;
        if(s!=null&&!"".equals(s)){i=Integer.parseInt(s);}
        return i;
    }
    //把string转换成double
    public double todouble(String s){
        double i=0;
        if(s!=null&&!"".equals(s)){i=Double.parseDouble(s);}
        return i;
    }
    //金额分档
    public String tod(String s) {
        String i = null;
        double a = 0;
        if (s != null && !"".equals(s)) {
            a = Double.parseDouble(s);
            double b = a / 10000;
            int d = (int) b / 2;
            if (b == 0) {
                i = "0";
            }
            if (0.1 > b && b >= 0) {
                i = "-7";
            }
            if (0.2 > b && b >= 0.1) {
                i = "-6";
            }
            if (0.3 > b && b >= 0.2) {
                i = "-5";
            }
            if (0.4 > b && b >= 0.3) {
                i = "-4";
            }
            if (0.6 > b && b >= 0.4) {
                i = "-3";
            }
            if (0.8 > b && b >= 0.6) {
                i = "-2";
            }
            if (1 > b && b >= 0.8) {
                i = "-1";
            }
            if (d >= 0) {
                int t = d + 1;
                i = String.valueOf(t);
            }

        }else{i="0";}
        return i;
    }

    public String change(String s) throws Exception{
        String a=s+"000";
        DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssms");
        Date date1= sdf.parse(a);
        long time1=date1.getTime();
        return String.valueOf(time1);
    }
}
