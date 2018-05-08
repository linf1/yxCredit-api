/**
 * 查询绑卡信息
 */
$(function () {
    Comm.ajaxPost("/wechat/ybCard/getOpenAccount","",function (msg) {
        console.log(msg);
        var data=msg.retData;
        var bank_card=data.bank_card;//银行卡
        var account_bank=data.account_bank;//开户行
        var is_openAccount=data.is_openAccount;//是否开户
        var tel=data.tel;
        if(tel){$("#callTel").attr("href","tel:"+tel)}
        if(is_openAccount == '0'){
            layer.open({content:'您还没有绑定银行卡',skin:'msg',time:2,end:function () {
                history.go(-1);
            }})
        }else{
            if(!bank_card){
                $("#userBankCard").html("暂无")
            }else{
                $("#userBankCard").html(bank_card.substring(0,4)+bank_card.substring(4,bank_card.length-4).replace(/\d/g,"*")+bank_card.substring(bank_card.length-4));
            }
            if(!account_bank){
                $("#bankOpenName").html("暂无");
            }else{
                $("#bankOpenName").html(account_bank);
            }
        }
    })
})
