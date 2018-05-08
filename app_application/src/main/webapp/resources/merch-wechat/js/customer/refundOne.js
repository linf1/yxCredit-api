$(function(){
    var orderId = $("#orderId").val();
    // var orderId = "7222c31c-82d5-40za4-81c4-b21f69422245";
    Comm.ajaxPost("/repay/settleAll","orderId="+orderId,function(data){
        var retData = data.retData;
        console.log(retData);
        var mercahntName = retData.merchantName;//商户名称
        $("#merchantName").text(mercahntName);
        var merchandiseBrand = retData.merchandiseBrand;//商品brand
        var merchandiseModel = retData.merchandiseModel;//商品model
        var merchandiseVersion = retData.merchandiseVersion;//商品version
        $("#merchandiseBrand").text(merchandiseBrand);
        $("#merchandiseModel").text(merchandiseModel);
        $("#merchandiseVersion").text(merchandiseVersion);
        var everyRepay = retData.everyRepay;//每期应还
        var periods = retData.periods;//期数
        $("#divideMoney").text("￥"+everyRepay+"x"+periods+"期");
        var repayNum = retData.repayNum;//已还总期数
        var dividePeriods = "已还"+repayNum+"/"+periods+"期";
        $("#dividePeriods").text(dividePeriods);
        var bankName = retData.bankName;//银行
        var bankCard = retData.bankCard;//银行卡
        bankCard = bankCard.substring(bankCard.length-4,bankCard.length);
        var bank = bankName+"("+bankCard+")";
        $("#bank").text(bank);
        var merchandiseUrl = retData.merchandiseUrl;//商品图片
        $("#url").attr('src',merchandiseUrl);
        if (retData.repayed && "1" == retData.repayed)
        {
            var html = '  <div class="refundText">' +
                '        <span >实际应还款</span>' +
                '        <span id="allMoney">￥'+retData.amount+'</span>' +
                '    </div>'+
                '<div class="refundText_footer" style="margin-top:45px;">' +
                '        <span class="refundText_footer_one">合计还款</span>' +
                '        <span class="refundText_footer_two" id="allMoney1">￥'+retData.amount+'</span>' +
                '        <span class="refundText_footer_three" style="background-color:darkgrey;">确认还款</span>' +
                '    </div>'

            $("#settleAll").html(html);
            layer.open({content: '无还款信息',skin: 'msg',time: 2 });
            return;
        }
       if(retData.settleType == "0"){
           // var overdueMoney = retData.overdueMoney;//逾期待还
           // $("#overRepay").text(overdueMoney);
           var allPrincipalMoney = retData.allPrincipalMoney;//未到期应还
           // var nowRepay = retData.nowRepay;//本期待还
           $("#nowRepay").text(allPrincipalMoney);
           // $("#unrepay").text(allPrincipalMoney);
           var unrepayPrin = retData.allPrincipalSum;//未到期本金
           unrepayPrin = "+￥"+unrepayPrin;
           $("#unrepayPrin").text(unrepayPrin);
           var allPackSum = retData.allPackSum;//未到期服务包费用
           allPackSum = "+￥"+allPackSum;
           $("#unrepayPackage").text(allPackSum);
           var allPackageRepay = retData.settleFee;//提前结清费用
           allPackageRepay = "+￥"+allPackageRepay;
           $("#settleFee").text(allPackageRepay);
           var allMoney = retData.allMoney;//实际应还款
           allMoney = "￥"+allMoney
           $("#allMoney").text(allMoney);
           $("#allMoney1").text(allMoney);

       }else{
           var html = '  <div class="refundText">' +
               '        <span >实际应还款</span>' +
               '        <span id="allMoney">￥'+retData.amount+'</span>' +
               '    </div>'+
               '<div class="refundText_footer" style="margin-top:45px;">' +
               '        <span class="refundText_footer_one">合计还款</span>' +
               '        <span class="refundText_footer_two" id="allMoney1">￥'+retData.amount+'</span>' +
               '        <span class="refundText_footer_three" onclick="confrimRepay()">确认还款</span>' +
               '    </div>'

          $("#settleAll").html(html);
       }
    })
})

function toRepayDetails(){
    var orderId=$("#orderId").val();
    window.location=_ctx+"/wechat/page/toBillingDetails?orderId="+orderId;
}

function confrimRepay() {
    var orderId=$("#orderId").val();
    $.confirm("确认立即支付吗?", function() {
        Comm.ajaxPost("/repay/confirmPaySettle","orderId="+orderId,function(data){
            var state = data.retData.respCode;
            var msg = data.retData.msg;
            if (state == "0000" || state=="BF00114"){
                var amount = $("#allMoney").text();
                if(amount){
                    amount=amount.replace("￥","");
                }
                var orderId = $("#orderId").val();
                layer.open({content: '支付成功',skin: 'msg',time: 2 ,end:function () {
                    window.location=_ctx+"/wechat/page/toRepaySuccess?orderId="+orderId+"&amount="+amount;
                }});
            }else if (state == "BF00100" || state == "BF00112" || state == "BF00113"
                || state == "BF00115" || state == "BF00144" || state == "BF00202"){
                layer.open({content: '支付确认中,请等待.',skin: 'msg',time: 2 });
            }else {
                layer.open({content: msg,skin: 'msg',time: 2 });
            }
        });
        linked.agreeContract();
    }, function() {
        //点击取消后的回调函数
    });
}