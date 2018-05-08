$(function () {
    var orderId = $("#orderId").val();
    Comm.ajaxPost("/repay/getBillingDetails","orderId="+orderId,function(data){
        var retData = data.retData;
        console.log(retData);
        var lenth = retData.length;
        var html = '';
        var num = 0;
        var pay = '';
        for(var i=0;i<lenth;i++){
            var payCount = retData[i].payCount;//期数
            var state = retData[i].state;//状态
            var repaymentAmount = retData[i].repaymentAmount;//应还款
            var actualAmount = retData[i].monthPay;//实还
            var payTime = retData[i].payTime;//到期时间
            if (payTime)
            {
                payTime = payTime.substring(0,4)+"."+payTime.substring(4,6)+"."+payTime.substring(6,8)
            }
            var penalty = retData[i].penalty;//逾期利息
            var pcgAmount = retData[i].pcgAmount;//服务包
            var defaultInterest = retData[i].defaultInterest;//罚息
            var derateAmount = retData[i].derateAmount;//减免
            var redAmount = retData[i].redAmount;//红包
            var settleAmount = retData[i].settleAmount;//本金
            var settleFee = retData[i].settleFee;//正常结清费用
            var now =  retData[i].now;
            if(defaultInterest==''){
                defaultInterest = 0.00;
            }
            var payTitle = '第' + payCount + '期';
            if (payCount == "0")
            {
                payTitle = '非正常结清';
            }
            else if (payCount == "-1")
            {
                payTitle = '提前结清';
            }
            html += '<div class="ContainerTop">'+
                '<div class="ContainerTopOne" style="flex:0.5;">'+
                '<span>'+payTitle+'</span>'+
                '</div>';
            if(state=='1' || state=='2' || state =='3' || state =='4'){
            //     html += '<div class="ContainerTopTwo">'+
            //         '<span>当期待还</span>'+
            //         '</div>'+
            //         '<div class="ContainerTopThree">'+
            //         '<span>￥'+actualAmount+'</span>'+
            //         '</div>'+
            //         '</div>';
            //     html += '<div class="ContainerBottom">'+
            //         '<div class="ContainerBottomOne">'+
            //         '<p>应还款</p>'+
            //         '<p>到期时间</p>'+
            //         ((pcgAmount && 0 != pcgAmount) ? '<p>服务包</p>' : '')+
            //         '</div>'+
            //         '<div class="ContainerBottomTwo">'+
            //         '<p>￥'+repaymentAmount+'</p>'+
            //         '<p>'+payTime+'</p>'+
            //         ((pcgAmount && 0 != pcgAmount) ? ('<p>￥'+pcgAmount+'</p>') : '')+
            //         '</div>'+
            //         '</div>'+
            //         '<div style="border-bottom: 5px solid #f6f6f6;"></div>';
            // }else if(state=='2' || state =='3' || state =='4'){
                var timeTitle = "还款时间";
                if(state=='1')
                {
                    timeTitle = "到期时间";
                    html += '<div class="ContainerTopTwo" style="flex:0.3;text-align:right;">'+
                        '<span>当期待还</span>'+
                        '</div>'+
                        '<div class="ContainerTopThree" style="flex:0.2;">'+
                        '<span>￥'+actualAmount+'</span>'+
                        '</div>'+
                        '</div>';
                }
                if(state=='2'){
                    html += '<div class="ContainerTopTwo" style="flex:0.3;text-align:right;">'+
                        '<span>已还款</span>'+
                        '</div>'+
                        '<div class="ContainerTopThree" style="flex:0.2;">'+
                        '<span>￥'+actualAmount+'</span>'+
                        '</div>'+
                        '</div>';
                }else if(state == '3'){
                    timeTitle = "到期时间";
                    // var overMoney = (Number(repaymentAmount)+Number(pcgAmount)+Number(penalty)+Number(defaultInterest)).toFixed(2);
                    html += '<div class="ContainerTopTwo" style="flex:0.3;text-align:right;">'+
                        '<span>逾期待还</span>'+
                        '</div>'+
                        '<div class="ContainerTopThree" style="flex:0.2;">'+
                        '<span>￥'+actualAmount+'</span>'+
                        '</div>'+
                        '</div>';
                }else if(state == '4'){
                    // var overMoney = (Number(repaymentAmount)+Number(pcgAmount)+Number(penalty)+Number(defaultInterest)).toFixed(2);
                    html += '<div class="ContainerTopTwo" style="flex:0.3;text-align:right;">'+
                        '<span>待确认还款</span>'+
                        '</div>'+
                        '<div class="ContainerTopThree" style="flex:0.2;">'+
                        '<span>￥'+actualAmount+'</span>'+
                        '</div>'+
                        '</div>';
                }
                html += '<div class="ContainerBottom">'+
                    '<div class="ContainerBottomOne">'+
                    ((repaymentAmount && 0 != repaymentAmount) ? '<p>应还款</p>': '')+
                    ((pcgAmount && 0 != pcgAmount) ? '<p>服务包</p>' : '')+
                    ((penalty && 0 != penalty) ? '<p>逾期利息</p>' : '')+
                    ((defaultInterest && 0 != defaultInterest) ? '<p>罚息</p>' : '')+
                    ((derateAmount && 0 != derateAmount) ? '<p>减免</p>' : '')+
                    ((redAmount && 0 != redAmount) ? '<p>红包抵扣</p>' : '')+
                    ((settleAmount && 0 != settleAmount) ? '<p>应还本金</p>' : '')+
                    ((settleFee && 0 != settleFee) ? '<p>提前结清费用</p>' : '')+
                    ((payTime && 0 != payTime) ? ('<p>'+timeTitle+'</p>'): '')+
                    '</div>'+
                    '<div class="ContainerBottomTwo">'+
                    ((repaymentAmount && 0 != repaymentAmount) ? ('<p>￥'+repaymentAmount+'</p>'): '')+
                    ((pcgAmount && 0 != pcgAmount) ? ('<p>￥'+pcgAmount+'</p>') : '')+
                    ((penalty && 0 != penalty) ? ('<p>￥'+penalty+'</p>') : '')+
                    ((defaultInterest && 0 != defaultInterest) ? ('<p>￥'+defaultInterest+'</p>') : '')+
                    ((derateAmount && 0 != derateAmount) ? ('<p>￥'+derateAmount+'</p>') : '')+
                    ((redAmount && 0 != redAmount) ? ('<p>￥'+redAmount+'</p>') : '')+
                    ((settleAmount && 0 != settleAmount) ? ('<p>￥'+settleAmount+'</p>') : '')+
                    ((settleFee && 0 != settleFee) ? ('<p>￥'+settleFee+'</p>') : '')+
                    ((payTime && 0 != payTime) ? ('<p>'+payTime+'</p>'): '')+
                    '</div>'+
                    '</div>'+
                    '<div style="border-bottom: 5px solid #f6f6f6;"></div>';
            }
        }
        $("#container").html(html);
    })
})

//全部结清
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
    }, function() {
        //点击取消后的回调函数
    });
}