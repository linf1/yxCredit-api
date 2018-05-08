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
    <title>支付成功</title>
</head>
<body style="background: #FAFAFA">
    <div class="toolBar">
        <p><span class="icon-back" onclick="history.go(-1)"></span><span>支付成功</span></p>
        <%--<p><span class="icon-back" onclick="linked.agreeContract()"></span><span>支付成功</span></p>--%>
    </div>
    <div class="pay_success" style="margin-top: 50px;">
        <img src="${ctx}/resources/merch-wechat/images/demo10.png" class="success_img" alt="">
        <p style="font-size: 25px;">￥${param.amount}元</p>
        <p>支付申请提交成功，支付结果会以消息推送给您</p>
        <p>
            <img src="${ctx}/resources/merch-wechat/images/light.png" alt="">
            <span class="hint">提示：向商家支付商品预付款，即可提货</span>
        </p>
    </div>
    <div class="commBtn" style="margin-bottom: 0">
        <button>知道了</button>
    </div>
</body>
<script src="${ctx}/resources/merch-wechat/js/customer/repaySuccess.js${version}"></script>
</html>