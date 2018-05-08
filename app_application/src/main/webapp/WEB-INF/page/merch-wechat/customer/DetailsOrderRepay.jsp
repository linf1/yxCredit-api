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
    <style>
        .commodity-content-text{
            position: relative;
            top: 11px;
        }
        .content-text-two{
            position: relative;
            top: 12px;
        }
        .content-text-three{
            position: relative;
            top: 30px;
        }
        .commodity-content-footer{
            text-align: right;
        }
        .footer-two{
            display: inline-block;
            height: 35px;
            line-height: 35px;
            text-align: center;
            margin-right: 15px;
            border-radius: 5px;
        }
        .footer-one{
            float: left;
        }
    </style>
</head>
<body>
<%--<div class="attestation-top">--%>
    <%--<span class="icon-back"></span>--%>
    <%--<span class="attestation-text">分期订单详情</span>--%>
<%--</div>--%>
<div class="header">
    <img src="${ctx}/resources/merch-wechat/images/ddqrxx.png" style="width:100%;" alt=""/>
</div>
<div class="commodity-top">
    <input type="hidden" id="orderId" value="${param.orderId}">
    <div class="commodity-title">
        <img src="${ctx}/resources/merch-wechat/images/demo4.png" alt="">
        <span class="title-text" id="productMerchantName"></span>
    </div>
    <div class="commodity-content" style="height: 115px;">
        <img style="width: 90px;height: 90px" src="${ctx}/resources/merch-wechat/images/demo5.png" alt="" id="productMerchandiseUrl">
        <div class="commodity-content-text">
            <p class="content-text-one" id="productMerchandiseBrandName"></p>
            <p class="content-text-two">
                <span id="productMerchandiseVersionName"></span>
                <span id="productMerchandiseModelName"></span>
            </p>
            <p class="content-text-three">
                <span id="productMonthPay"></span>
                <span style="margin-left: -12px;" id="Periods"></span>
            </p>
        </div>
    </div>
</div>
<div class="commodity-content-one">
    <div class="content-one">
        <span>首付</span>
        <input type="text" class="user-input" id="productDownPayMoney" readonly placeholder="￥0.00">
    </div>
    <div class="content-one">
        <span>申请分期金额</span>
        <input type="text" class="user-input" id="productFenqiMoney" readonly placeholder="￥0.00">
    </div>
    <div class="content-one">
        <span>申请期数</span>
        <input type="text" class="user-input" id="productPeriods" readonly placeholder="0期">
    </div>
    <div class="content-one">
        <span>每期还款</span>
        <input type="text" class="user-input" readonly id="productMonthPayBottom" placeholder="￥0.00">
    </div>
    <div class="content-one" style="border-bottom: transparent;" id="showPackage">
        <span>前置服务包</span>
        <input type="text" class="user-input" id="severBag" readonly placeholder="">
    </div>
</div>
<div class="commodity-content-one" >
    <div class="content-one" id="applyInfo"   onclick = "checkApplyInfo()">
        <span>查看申请信息</span>
        <input type="text" class="user-input" readonly placeholder=">">
    </div>
    <div class="content-one" id="imageInfo"    onclick = "checkImgInfo()">
        <span>查看影像资料</span>
        <input type="text" class="user-input" readonly placeholder=">">
    </div>
    <div class="content-one" id="handSign"  style="border-bottom: transparent;" onclick = "chcekSignInfo()">
        <span>查看手签图片</span>
        <input type="text" class="user-input" readonly placeholder=">">
    </div>
</div>
<div class="commodity-content-footer" id="commodity-content-footer" style="display: none">
    <span class="footer-one" onclick="checkInfomationTure()">信息有误</span>
    <img src="${ctx}/resources/merch-wechat/images/wh.png" alt="" style="left: 8px;top:16px;float: left;"/>
    <span class="footer-two" onclick="checkOrderState()">确认信息</span>
</div>
<div class="content-one" id="already">
    <p>还款历史明细</p>
    <p><span style="text-align: left;display: inline-block;">2014-09-23 还款</span><span style="text-align: right;display: inline-block; width: 60px;">￥322.00</span></p>
</div>

<%@include file ="../package.jsp"%>
<script src="${ctx}/resources/merch-wechat/js/customer/detailsOrderRepay.js${version}"></script>
</body>
</html>