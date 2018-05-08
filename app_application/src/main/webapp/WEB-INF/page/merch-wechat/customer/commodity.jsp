<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="format-detection" content="telephone=no" />
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <%@include file ="../../common/input.jsp"%>
    <%@include file ="../../common/taglibs.jsp"%>
    <link rel="stylesheet" href="${ctx}/resources/merch-wechat/css/style.css${version}">
    <link rel="stylesheet" href="${ctx}/resources/merch-wechat/css/customer.css${version}">
    <title>商品分期</title>
</head>
<body style="background: #FAFAFA">
    <div class="toolBar">
        <p><span class="icon-back" onclick="history.go(-1)"></span><span>商品分期</span></p>
    </div>
    <div class="commodity-top" style="margin-top: 44px;">
        <input type="hidden" id="empId" value="${param.empId}">
        <input type="hidden" id="orderTypeValue" value="${param.orderTypeValue}">
        <input type="hidden" id="allMoney" value="${param.allMoney}">
        <input type="hidden" id="downPayMoney" value="${param.downPayMoney}">
        <input type="hidden" id="merchantId" value="${param.merchantId}">
        <input type="hidden" id="productId" value="${param.productId}">
        <input type="hidden" id="idJson" value="${param.idJson}">
        <input type="hidden" id="merchandiseId" value="${param.merchandiseId}">
        <input type="hidden" id="createTime" value="${param.createTime}">
        <div class="commodity-title">
            <img src="${ctx}/resources/merch-wechat/images/demo4.png" alt="">
            <span class="title-text" id="productMerchantName"></span>
        </div>
        <div class="commodity-content">
            <img style="width: 90px;height: 90px" src="" alt="" id="productMerchandiseUrl">
            <div class="commodity-content-text">
                <p class="content-text-one" id="productMerchandiseBrandName"></p>
                <p class="content-text-two">
                    <span id="productMerchandiseVersionName"></span>
                    <span id="productMerchandiseModelName"></span>
                </p>
                <p class="content-text-three">
                    <span id="productMonthPay"></span>
                    <span id="Periods"></span>
                </p>
            </div>
        </div>
    </div>
    <div class="commodity-topic">
        <p>分期信息</p>
    </div>
    <div class="commodity-content-one">
        <div class="content-one">
            <span>商品总价</span>
            <input type="text" class="user-input" id="productAllMoney" readonly placeholder="￥0.00">
        </div>
        <div class="content-one">
            <span>首付</span>
            <input type="text" class="user-input" id="productDownPayMoney" readonly placeholder="￥0.00">
        </div>
        <div class="content-one">
            <span>申请分期金额</span>
            <input type="text" class="user-input" id="productFenqiMoney" readonly placeholder="￥0.00">
        </div>
        <div class="content-one" style="border-bottom: transparent;">
            <span>申请期数</span>
            <input type="text" class="user-input" id="productPeriods" readonly placeholder="0期">
        </div>
    </div>
    <div class="content-two">
        <div class="content-two-one" id="showPackage">
            <span>服务包</span>
            <input type="text" class="user-input" id="severBag" readonly placeholder="共有0款服务包">
        </div>
    </div>
    <div class="content-two">
        <div class="content-two-one">
            <span>每期还款</span>
            <img src="${ctx}/resources/merch-wechat/images/wh.png" alt="" id="wh">
            <input type="text" class="user-input" readonly id="productMonthPayBottom" placeholder="￥0.00">
        </div>
    </div>
    <div class="content-two">
        <div class="content-two-one">
            <span>订单类型</span>
            <input type="text" class="user-input" readonly id="orderType" placeholder="线上订单">
        </div>
    </div>
    <div class="commBtn" style="margin-bottom: 2px;">
        <button>确认信息</button>
    </div>
    <%@include file ="../package.jsp"%>
    <script src="${ctx}/resources/merch-wechat/js/customer/commodity.js${version}"></script>
</body>
</html>