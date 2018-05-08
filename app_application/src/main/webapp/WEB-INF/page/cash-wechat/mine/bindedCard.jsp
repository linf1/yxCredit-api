<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <title>绑定的银行卡</title>
    <%@include file ="../../common/input.jsp"%>
    <%@include file ="../../common/taglibs.jsp"%>
    <link rel="stylesheet" href="${ctx}/resources/cash-wechat/css/style.css${version}">
    <style>
        .bindedCarInfo{  background: #fff; font-size: 14px;  }
        .bindedCarInfo p{height: 30px;line-height: 30px;border-bottom: 1px solid #ddd;padding: 10px 15px;}
        .bindedCarInfo p:last-child{border-bottom:none}
        .right{float: right}
        .bindedCarInfoFooter{font-size: 14px;margin: 20px 15px;color:#A1A1A1}
    </style>
</head>
<body style="background:#f6f6f6">
<div class="toolBar">
    <p><span class="icon-back" onclick="history.go(-1)"></span><span id="comQuestionTitle">绑定的银行卡</span></p>
</div>
<div style="margin-top: 50px">
    <div class="bindedCarInfo">
        <p><span>提现银行卡</span><span class="right" id="userBankCard"></span></p>
        <p><span>开户行</span><span class="right" id="bankOpenName"></span></p>
    </div>
    <div class="bindedCarInfoFooter">
        <p>温馨提示:</p>
        <p>1，提现默认银行卡为您绑定的银行卡。</p>
        <p>2，修改绑定卡请<span style="color:#FF8350;margin-left: 4px"><a href="tel:" style="color:#FF8350" id="callTel">联系客服&gt;</a></span></p>
    </div>
</div>
</body>
<script src="${ctx}/resources/cash-wechat/js/mine/bindedCard.js${version}"></script>
<script>
</script>
</html>