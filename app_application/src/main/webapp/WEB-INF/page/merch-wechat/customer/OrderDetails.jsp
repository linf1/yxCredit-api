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
    <title>分期订单详情</title>
</head>
<body>
<div class="attestation-top">
    <span class="icon-back"></span>
    <span class="attestation-text">分期订单详情</span>
</div>
<div class="header">
    <img src="${ctx}/resources/merch-wechat/images/demo6.png" style="width:100%;" alt=""/>
</div>
<div class="commodity-top">
    <div class="commodity-title">
        <img src="${ctx}/resources/merch-wechat/images/demo4.png" alt="">
        <span class="title-text"></span>
    </div>
    <div class="commodity-content">
        <img src="${ctx}/resources/merch-wechat/images/demo5.png" alt="">
        <div class="commodity-content-text">
            <p class="content-text-one"></p>
            <p class="content-text-two">
                <span></span>
                <span></span>
            </p>
            <p class="content-text-three">
                <span></span>
                <span style="color: #9F9B98;"></span>
            </p>
        </div>
    </div>
</div>
<div class="commodity-content-one">
    <div class="content-one">
        <span>首付</span>
        <input type="text" class="user-input" placeholder="￥1000.00">
    </div>
    <div class="content-one">
        <span>申请分期金额</span>
        <input type="text" class="user-input" placeholder="￥5780.00">
    </div>
    <div class="content-one">
        <span>申请期数</span>
        <input type="text" class="user-input" placeholder="18期">
    </div>
    <div class="content-one">
        <span>每期还款</span>
        <input type="text" class="user-input" placeholder="￥321.11">
    </div>
    <div class="content-one" style="border-bottom: transparent;">
        <span>前置服务包</span>
        <input type="text" class="user-input" placeholder="300.00">
    </div>
</div>


</body>
</html>