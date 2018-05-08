<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="format-detection" content="telephone=no" />
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <%@include file ="../../common/input.jsp"%>
    <%@include file ="../../common/taglibs.jsp"%>
    <link rel="stylesheet" href="${ctx}/resources/merch-wechat/css/customer.css${version}">
    <link rel="stylesheet" href="${ctx}/resources/merch-wechat/css/style.css${version}">
    <title>支付</title>
</head>
<body style="background: #fafafa">
    <div class="toolBar">
        <p><span class="icon-back" onclick="history.go(-1)"></span><span>支付</span></p>
    </div>
    <input type ="hidden" id="orderId" value="${param.orderId}"/>
    <div class="pay_top" style="margin-top: 44px">
        <p id="svcPcgAmount"></p>
        <p>待支付</p>
    </div>
    <div class="pay_top_content">
        <img src="${ctx}/resources/merch-wechat/images/fwb.png" alt="">
        <span>服务包</span>
        <span id="svcPcgName"></span>
    </div>
    <div class="commodity-topic">
        <p class="pay_type">支付方式</p>
    </div>
    <div class="pay_top_type" onclick="choose()">
        <img src="${ctx}/resources/merch-wechat/images/bank.png" alt="">
        <span class="bank_type" id="bank"></span>
        <img id="flag" src="${ctx}/resources/merch-wechat/images/demo9.png" alt="">
        <input id="flagVal" type="hidden" value="0"/>
    </div>
    <div  id="commBtn" class="commBtn">
        <button >立即支付</button>
    </div>
</body>
<script src="${ctx}/resources/merch-wechat/js/customer/pay.js${version}"></script>
</html>