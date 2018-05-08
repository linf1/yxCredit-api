$(".getRepay").on("click",function (e) {
    if($(this).find(".payWayIcon").hasClass("icon-rthook")){//未选择
        $(this).find(".payWayIcon").removeClass("icon-rthook").addClass("icon-circle");
        $(this).attr("isChecked","true");
        $(this).parent(".payWay").siblings().children().find(".payWayIcon").removeClass("icon-circle").addClass("icon-rthook");
        $(this).parent(".payWay").siblings().children(".getRepay").attr("isChecked","false");
    }else{//已选择
        $(this).find(".payWayIcon").removeClass("icon-circle").addClass("icon-rthook");
        $(this).attr("isChecked","false");
    }
})
$(function () {
    Comm.ajaxPost("/wechat/ybCard/getOpenAccount","",function (msg) {
        var data=msg.retData;
        var bank_card=data.bank_card;//银行卡
        var account_bank=data.account_bank;//开户行
        $("#bankName").html(account_bank+"("+bank_card.substring(bank_card.length-4)+")");
        $("#repayBankNum").val(bank_card);
    })
})
/**确认还款**/
$(".footerBtn button").on("click",function () {
    var ischecked=$(".getRepay").attr("ischecked");
    if(ischecked=="false"){
        layer.open({content: '请选择还款方式',skin: 'msg',time: 2 });
        return;
    }
    /**提交还款方式**/
    toRepayMoney();
})
/**还款**/
function toRepayMoney() {
    whir.loading.add("", 1);
    var finalreFee = $('#finalreFee').html().replace('￥','');
    var repayBankNum = $('#repayBankNum').val();
    Comm.ajaxPost('/wechat/uniBindCardPay/bindCardPay',{money:finalreFee,cardNo:repayBankNum},function (response) {
        console.log(msg);
        var data = msg.retData;
        if(data.status=='FAIL' || data.status=='BIND_FAIL' || data.status=='TIME_OUT'){
            $.alert(data.msg,'消息');
            return
        }else if(data.status == 'BIND_ERROR'){
            bindError();
            return
        }else if(data.status == 'TO_VALIDATE'){
            openMessage();
        }
    })
}
function bindError() {
    whir.loading.add("", 1);
    Comm.ajaxPost('/wechat/uniBindCardPay/bindCardPayResend', '', function (response) {
            console.log(msg);
            var data = msg.retData;
            if(data.status=='FAIL' || data.status=='TIME_OUT'){
                $.alert(data.msg,'消息');
                return
            }else if(data.status=='TO_VALIDATE'){
                openMessage();
            }
        }
    )
}
function openMessage() {
    $.prompt({
        title: '验证码',
        text: '请输入手机验证码',
        input: '',
        empty: false,
        onOK: function (input) {
            var cardNo = $('#repayBankNum').val();
            var finalreFee = $('#finalreFee').html().replace('￥','');
            cardConfirm(input,cardNo,finalreFee);
        },
        onCancel: function () {
        }
    });
}
function cardConfirm(smsCode,cardNo,finalreFee) {
    whir.loading.add("", 1);
    Comm.ajaxPost('/wechat/uniBindCardPay/bindCardPayConfirm',
        {
            smsCode:smsCode,
            cardNo:cardNo,
            money:finalreFee
        },
        function (response) {
            var data = response.retData;
            if(data.status == 'FAIL' || 'PAY_FAIL' || 'TIME_OUT'){//还款失败
                $("#repaymentError").show();
                $("#repaymentContainer").hide();
            }else if(data.status == 'TO_VALIDATE' || 'PROCESSING'){//还款成功
                $("#repaymentSuccess").show();
                $("#repaymentContainer").hide();
            }
        }
    )
}
