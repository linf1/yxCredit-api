$(function () {
    var orderId = $("#orderId").val();
    $("#commBtn").bind("click", lijizhifu);
    Comm.ajaxPost("/payTreasure/payServicePackageResult","orderId="+orderId,function (data) {
        var state = data.retData.state;
        //state : I 确认中, S 已付款 ,F 付款失败
        if (state == 'I'){
            layer.open({content: '支付确认中.',skin: 'msg',time: 2 });
            //前置支付按钮置灰
            $("#commBtn").unbind("click", lijizhifu);
            $("#commBtn button").css("background", 'url("../../merch-wechat/images/commBtn_w.png") no-repeat center center');
        }else if (state == 'S'){
            var svcPcgAmount = $("#svcPcgAmount").text();
            layer.open({content: '您的服务包已支付成功.',skin: 'msg',time: 2 ,end:function () {
                window.location=_ctx+"/wechat/page/toPaySuccess?orderId="+orderId+"&svcPcgAmount="+svcPcgAmount;
            }});

        }else if (state == 'F'){
            layer.open({content: '支付失败,请重新支付.',skin: 'msg',time: 2,end:function () {
                getPayInfo(orderId);
            } });
        }else{
            getPayInfo(orderId);  //未支付前置服务包
        }
    });
});


/**
 * 获取支付前置提前还款服务包信息
 */
function getPayInfo(){
    var orderId = $("#orderId").val();
    Comm.ajaxPost("/payTreasure/getPaySerPcgInfo","orderId="+orderId,function (data) {
        var data = data.retData;
        var bankNo = data.bankNo.substring(data.bankNo.length-4,data.bankNo);
        $("#svcPcgAmount").text("￥"+data.amount+"元");
        $("#svcPcgName").text(data.packageName);
        $("#bank").text(data.bankName+'('+bankNo+')');
    });
}

//选中银行卡
function choose(){
    var flag = $("#flagVal").val();
    if (flag == '0'){
        $("#flag").attr('src',_ctx+"/resources/merch-wechat/images/demo10.png");
        $("#flagVal").val('1');
    }else {
        $("#flag").attr('src',_ctx+"/resources/merch-wechat/images/demo9.png");
        $("#flagVal").val('0');
    }
}

function lijizhifu(){
    //置灰
    $("#commBtn").unbind("click", lijizhifu);
    $("#commBtn button").css("background", 'url("../../merch-wechat/images/commBtn_w.png") no-repeat center center');
    var orderId = $("#orderId").val();
    var flagVal = $("#flagVal").val();
    if (flagVal == '0'){
        layer.open({content: '请勾先勾选银行卡',skin: 'msg',time: 2 });
        //显示支付按钮
        $("#commBtn").bind("click", lijizhifu);
        $("#commBtn button").css("background", 'url("../../merch-wechat/images/commBtn.png") no-repeat center center');
        return ;
    }
    Comm.ajaxPost("/payTreasure/payServicePackageCardPay",'orderId='+orderId,function (data) {
        var state = data.retData.respCode;
        var msg = data.retData.msg;
        if (state == "0000" || state=="BF00114"){
            var svcPcgAmount = $("#svcPcgAmount").text();
            var orderId = $("#orderId").val();
            layer.open({content: '支付成功',skin: 'msg',time: 2 ,end:function () {
                window.location=_ctx+"/wechat/page/toPaySuccess?orderId="+orderId+"&svcPcgAmount="+svcPcgAmount;
            }});
        }else if (state == "BF00100" || state == "BF00112" || state == "BF00113"
            || state == "BF00115" || state == "BF00144" || state == "BF00202"){
            layer.open({content: '支付确认中,请等待.',skin: 'msg',time: 2 });

        }else {
            layer.open({content: msg,skin: 'msg',time: 2 });
           //显示支付按钮
            $("#commBtn").bind("click", lijizhifu);
            $("#commBtn button").css("background", 'url("../../merch-wechat/images/commBtn.png") no-repeat center center');
        }
        linked.agreeContract();
    });

}