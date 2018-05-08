<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <title>授权</title>
    <%@include file ="../../common/input.jsp"%>
    <%@include file ="../../common/taglibs.jsp"%>
    <link rel="stylesheet" href=${ctx}/resources/cash-wechat/css/style.css${version}">
    <link rel="stylesheet" href="${ctx}/resources/cash-wechat/css/myCss.css${version}">
    <style>
        .accreditInfo{    background: #FBEDE2;font-size: 14px;padding-left: 15px;margin: 10px auto;height: 40px;line-height: 40px;}
        .accreditInfo .accreditInfoImg{display: inline-block;width: 16px;height: 18px;background: url(${ctx}/resources/cash-wechat/images/up.png) no-repeat center center;background-size: cover;margin-right: 10px;}
        .accreditItemsContainer{display: flex;font-size: 14px;background: #fff;padding: 10px 15px;text-align: center; margin-bottom: 5px;}
        .accreditItemsContainer:last-child{margin-top: 5px}
        .accreditItemsContainer div:first-child{flex:.2;text-align: left}
        .accreditItemsContainer div:first-child img{width: 70%;vertical-align: bottom;}
        .accreditItemsContainer div:nth-child(2){flex:.6;text-align: left;}
        .accreditItemsContainer div:nth-child(2) p:last-child{color:#999;font-size: 12px;margin-top: 6px;}
        .accreditItemsContainer div:last-child{flex:.2;position: relative}
        .accreditItemsContainer div:last-child span{position: relative;color:#333}
        .accreditState{position: absolute;top:-10px;right: -15px;width: 40px;height: 40px;background: url(${ctx}/resources/cash-wechat/images/must.png) no-repeat center center;background-size: cover;}
    </style>
</head>
<body style="background:#f6f6f6">
<input type="hidden" id="orderId" value="${param.orderId}">
    <div class="toolBar">
        <p><span class="icon-back" onclick="history.go(-1)"></span><span>现金借款</span><span class="toolBarRightBtn" id="submitOrderBtn">完成</span></p>
    </div>
    <div class="infoTitle">
        <div class="infoContent">
            <p class="infoLine infoLineActive"></p>
            <p class="infoImg infoImgActive"></p>
            <p class="infoWord">身份认证</p>
        </div>
        <div class="infoContent">
            <p class="infoLine infoLineActive"></p>
            <p class="infoImg infoImgActive"></p>
            <p class="infoWord">个人信息</p>
        </div>
        <div class="infoContent">
            <p class="infoLine"></p>
            <p class="infoImg infoImgActive"></p>
            <p class="infoWord">授权</p>
        </div>
    </div>
    <p class="accreditInfo">
        <span class="accreditInfoImg"></span><span>完成更多授权，提高借款通过率</span>
    </p>
    <div class="accreditItemsContainer" onclick="window.location=_ctx+'/wechat/basicinfo/operatorAccredit'">
        <div>
            <img src="${ctx}/resources/cash-wechat/images/operator.png" alt="">
        </div>
        <div>
            <p>手机运营商授权</p>
            <p>授权信息仅用于信用评估</p>
        </div>
        <div>
            <span id="phone_host_state">未授权</span>
            <input type="hidden" id="phone_host_state_value" >
            <i class="accreditState"></i>
        </div>
    </div>
    <div class="accreditItemsContainer" onclick="window.location=_ctx+'/wechat/basicinfo/zhiMaAccredit'" style="display: none">
        <div>
            <img src="${ctx}/resources/cash-wechat/images/zhima.png" alt="">
        </div>
        <div>
            <p>芝麻信用授权</p>
            <p>授权信息仅用于信用评估</p>
        </div>
        <div>
            <span class="already" id="sesame_state">未授权</span>
            <input type="hidden" id="sesame_state_value">
        </div>
    </div>
</body>
<script>
    $(function () {
        getAccreditInfo();
        $("#submitOrderBtn").on("click",function () {
            var phoneValue=$("#phone_host_state_value").val();
            if(!phoneValue){
                layer.open({content: "请先授权手机运营商",skin: 'msg',time: 2 });
                return;
            }
            submitOrder();
        })
    })
    function getAccreditInfo() {
        whir.loading.add("", 1);
        Comm.ajaxPost('/wechat/identity/getPageInfoForSesameSQ', "", function (response) {
            var retData = response.retData;
            if(retData.phone_host_state==""){
                $("#phone_host_state").html("未授权")
            }else{
                $("#phone_host_state").html("已授权");
                $("#phone_host_state_value").val("100")
            }
            if(retData.sesame_state==""){
                $("#sesame_state").html("未授权")
            }else{
                $("#sesame_state").html("已授权");
                $("#sesame_state_value").val("100")
            }
        })
    }
    function submitOrder() {
        var orderId = $("#orderId").val();
        Comm.ajaxPost('/miaofuOrder/subOrder', {orderId:orderId}, function (response) {
            window.location=_ctx+"/wechat/identity/applySuccess?orderId=${param.orderId}"
        });
    }
</script>
</html>