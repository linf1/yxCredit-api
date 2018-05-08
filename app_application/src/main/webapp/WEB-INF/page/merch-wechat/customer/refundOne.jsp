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
    <title>还款</title>
</head>
<body>
    <%--<div class="attestation-top" style="background-color:#FFC602;">--%>
        <%--<img src="${ctx}/resources/merch-wechat/images/back.png" alt="" class="back">--%>
        <%--<span class="attestation-text" style="color: #fff;font-size: 17px;">还款</span>--%>
    <%--</div>--%>
    <div class="refund">
        <input type="hidden" id="orderId" value="${param.orderId}"/>
        <img src="${ctx}/resources/merch-wechat/images/header.png" alt="" class="refund_img">
        <div class="refund-content">
            <div class="refund-header" onclick="toRepayDetails()">
                <div class="bill">
                    <span>账单明细</span>
                    <img src="${ctx}/resources/merch-wechat/images/next1.png" alt="" class="next1">
                </div>
            </div>
            <div class="commodity-title">
                <img src="${ctx}/resources/merch-wechat/images/logo1.png" alt="" style="width:18px;">
                <span class="title-text" id="merchantName"></span>
            </div>
            <div class="RefundContent">
                <img src="${ctx}/resources/merch-wechat/images/phone.png" alt="" class="Apple_img"  id="url" style="height:66px;">
                <div class="RefundContent-text">
                    <p class="content-text-one" id="merchandiseBrand"></p>
                    <p style="font-size: 14px;"><span id="merchandiseModel">3</span>&nbsp;&nbsp;<span id="merchandiseVersion"></span></p>
                    <p class="repay_text">
                        <span id="divideMoney"></span>
                        <span style="color:#929292;" id="dividePeriods"></span>
                    </p>
                </div>
            </div>
        </div>
        <img src="${ctx}/resources/merch-wechat/images/bottom.png" alt="" class="bottom_img">
    </div>
    <div class="refundText">
        <span>还款方式</span>
        <span class="refundText_bank" id="bank"></span>
        <img src="${ctx}/resources/merch-wechat/images/next2.png" alt="" class="refundText_img">
    </div>
    <div id="settleAll">
    <div class="refundText_buttom">
        <!--<div class="underline">
            <span>逾期待还</span>
            <span id="overRepay"></span>
        </div>-->
        <div class="underline">
            <span>当期应还</span>
            <span id="nowRepay"></span>
        </div>
        <!--<div class="underline">
            <span>未到期应还</span>
            <span id="unrepay"></span>
        </div>-->
        <div class="underline_text">
            <p>
                <span>未到期本金</span>
                <span class="money" id="unrepayPrin"></span>
            </p>
            <p>
                <span>未到期服务包费用</span>
                <span class="money" id="unrepayPackage"></span>
            </p>
            <%--<p>
                <span>未到期利息</span>
                <span class="money">-￥900.00</span>
            </p>--%>
            <p>
                <span>提前结清费用</span>
                <span style="padding-left: 0px;" class="moneyThree" id="settleFee"></span>
            </p>
        </div>
    </div>
    <div class="refundText">
        <span >实际应还款</span>
        <span id="allMoney"></span>
    </div>
    <div class="refundText_footer" style="margin-top:45px;">
        <span class="refundText_footer_one">合计还款</span>
        <span class="refundText_footer_two" id="allMoney1"></span>
        <span class="refundText_footer_three" onclick="confrimRepay()">确认还款</span>
    </div>
    </div>
</body>
<script src="${ctx}/resources/merch-wechat/js/customer/refundOne.js${version}"></script>
</html>
