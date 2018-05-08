<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/11/28 0028
  Time: 下午 4:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="format-detection" content="telephone=no" />
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <%@include file ="../../common/input.jsp"%>
    <%@include file ="../../common/taglibs.jsp"%>
    <link rel="stylesheet" href="${ctx}/resources/merch-wechat/css/customer.css${version}">
    <title>分期还款</title>
</head>
<body>
    <div class="attestation-top" style="background-image: linear-gradient(to left, #fe8702,#ffae00); ">
        <span class="attestation-text" style="color: #fff;font-size: 17px;">分期还款</span>
    </div>
    <div class="pay">
        <div class="pay_one">
            <img src="${ctx}/resources/merch-wechat/images/pay1.png" alt="">
            <span>逾期还款</span>
        </div>
        <p class="text_one">当前代还总额</p>
        <p class="text_two">0.00</p>
        <div class="pay_two">
            <span>逾期金额0.00</span>
            <span style="padding-left: 25%;">未到期金额0.00</span>
        </div>
    </div>

    <div class="pay_img">
        <img src="${ctx}/resources/merch-wechat/images/banner.png" alt="">
        <%--<p>您还没有需要还款的订单哦~</p>--%>
    </div>



</body>
</html>
