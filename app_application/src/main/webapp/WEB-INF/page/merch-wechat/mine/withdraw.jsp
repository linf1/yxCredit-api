<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <title>提现</title>
    <%@include file ="../../common/input.jsp"%>
    <%@include file ="../../common/taglibs.jsp"%>
    <link rel="stylesheet" href="${ctx}/resources/cash-wechat/css/style.css${version}">
    <link rel="stylesheet" href="${ctx}/resources/cash-wechat/css/myCss.css${version}">
    <style>
        .cardInfoOne{margin-top: 50px}
        .cardInfoOne p{line-height: 20px;}
        .cardInfoTwo{padding: 0}
        .cardInfoTwo p{padding: 10px 15px;line-height: 24px;border-bottom: 1px solid #F6F6F6;}
        .cardInfoTwo p:last-child{border-bottom: none;}
    </style>
</head>
<body style="background:#f6f6f6">
<input type="hidden" id="redMoney" value="${param.redMoney}">
<input type="hidden" id="repayMoney" value="${param.repayMoney}">
    <div class="toolBar">
        <p><span class="icon-back" onclick="history.go(-1)"></span><span>提现</span></p>
    </div>
    <div class="cardInfoOne">
        <p><span>提现银行卡</span><span class="right" id="bankCard"></span></p>
        <p><span>开户行</span><span class="right" id="accountBank"></span></p>
    </div>
    <div class="cardInfoTwo">
        <p><span>可提现金额</span><span class="right" id="account">${param.amount}元</span></p>
        <p><span>快速审信费</span><span class="right" id="quickTrialFee">0元</span></p>
        <p><span>账户管理费</span><span class="right" id="manageFee">0元</span></p>
    </div>
    <div style="margin: 30px auto;">
        <button class="weui-btn my-btn">提现</button>
    </div>
    <div style="font-size: 12px;margin: 0 15px;">
        <p style="margin: 10px auto;">温馨提示</p>
        <p>1、提现默认银行卡为您绑定的银行卡。</p>
        <p>2、修改绑定卡请联系 <span style="color:#FE8352">联系客服&gt;</span></p>
    </div>
</body>
<script src="${ctx}/resources/cash-wechat/js/mine/withdraw.js${version}"></script>
</html>