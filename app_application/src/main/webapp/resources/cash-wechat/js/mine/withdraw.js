var withdrawPamams={};
$(function () {
    initPage();
    $(".my-btn").on("click",function () {
        toDrawMoney();
    })
});
/**获取提现页面信息**/
function initPage(){
    Comm.ajaxPost("/wechat/withdrawal/getYbWithdrawal","",function (data) {
        var flag=data.retData.flag;
        if(flag){
            var ybMap=data.retData.ybMap;
            var quickTrialFee=data.retData.quickTrialFee;//快速审信费
            var manageFee=data.retData.manageFee;//账户管理费
            var bank_card=ybMap.bank_card;//开户银行卡
            var account_bank=ybMap.account_bank;//开户行
            $("#bankCard").html(bank_card.substring(0,4)+bank_card.substring(4,bank_card.length-4).replace(/\d/g,"*")+bank_card.substring(bank_card.length-4))
            $("#accountBank").html(account_bank);
            $("#quickTrialFee").html(quickTrialFee+"元");
            $("#manageFee").html(manageFee+"元");
            withdrawPamams.bank_card=ybMap.bank_card;
            withdrawPamams.account_bank=ybMap.account_bank;
            withdrawPamams.quickTrialFee=data.retData.quickTrialFee;
            withdrawPamams.manageFee=data.retData.manageFee;
        }else{
            $.alert(data.retData.msg, "消息",function () {
                history.go(-1);
            });
        }
    });
}
/**提交提现**/
function toDrawMoney() {
    var read_money = $("#redMoney").val(),//红包
        repay_money = $("#repayMoney").val();//待放款
    Comm.ajaxPost('/wechat/withdrawal/ybWithdrawal',{red_money:read_money,repay_money:repay_money}, function (response) {
            var retData=response.retData;
            if(retData.flag){
                layer.open({content: retData.msg,skin: 'msg',time: 2 ,end:function () {
                    history.go(-1);
                }});
            }else{
                layer.open({content: retData.msg,skin: 'msg',time: 2 });
            }
        }
    )
}