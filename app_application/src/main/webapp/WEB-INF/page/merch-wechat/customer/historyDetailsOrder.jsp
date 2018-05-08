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
        #theLast{
            position:fixed;
            bottom:0;
        }
    </style>
</head>
<body>
<%--<div class="attestation-top">--%>
    <%--<span class="icon-back"></span>--%>
    <%--<span class="attestation-text">分期订单详情</span>--%>
<%--</div>--%>
<div class="header">
    <img src="${ctx}/resources/merch-wechat/images/ddqrxx.png" style="width:100%;" alt="" id="header_img"/>
</div>
<div class="commodity-top">
    <input type="hidden" id="orderId" value="${param.orderId}">
    <input type="hidden" id="bankCard">
    <input type="hidden" id="bankName">
    <input type="hidden" id="isBank">
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
        <span>服务包</span>
        <input type="text" class="user-input" id="severBag" readonly placeholder="">
    </div>
</div>

<div class="commodity-content-one" id="payHistoryNumber" style="display: none">
    <div class="content-one">
        <span>已还期数</span>
        <input type="text" class="user-input" id="yihuanqishu" readonly placeholder="0期">
    </div>
    <div class="content-one">
        <span>已还总额</span>
        <input type="text" class="user-input" id="yihuanzonge" readonly placeholder="￥0.00">
    </div>
</div>

<div class="commodity-content-one" id="lookLookSeeSee">
    <div class="content-one" id="applyInfo" onclick = "checkApplyInfo()">
        <span>查看申请信息</span>
        <input type="text" class="user-input" readonly placeholder=">">
    </div>
    <div class="content-one" id="imageInfo" onclick = "checkImgInfo()">
        <span>查看影像资料</span>
        <input type="text" class="user-input" readonly placeholder=">">
    </div>
    <div class="content-one" id="handSign"  style="border-bottom: transparent;" onclick = "chcekSignInfo()">
        <span>查看手签图片</span>
        <input type="text" class="user-input" readonly placeholder=">">
    </div>
    <div class="content-one" id="showHeton"  style="display:none;border-bottom: transparent;border-top:1px solid #e1e1e1;" onclick = "checkContract('true')">
        <span>查看合同</span>
        <input type="text" class="user-input" readonly placeholder=">">
    </div>
</div>
<div id="already" style="display: none;line-height: 45px;margin: 0 15px;">
    <p style="border-bottom: 1px solid #E1E1E1;">还款历史明细</p>
</div>
<br>

<div class="commodity-content-footer" id="commodity-content-footer" style="display: none">
    <span class="footer-one" onclick="checkInfomationTure()">信息有误</span>
    <img src="${ctx}/resources/merch-wechat/images/wh.png" onclick="checkInfomationTure()" alt="" style="left: 8px;top:16px;float: left;"/>
    <span class="footer-two" onclick="confirmInfo()">确认信息</span>
</div>
<div class="commodity-content-footer" id="commodityEmp" style="display: none">
    <span class="time-text" style="color: red;font-size: 15px;">已提交办单员处理</span>
</div>
<div class="commodity-content-footer" id="unPass" style="display: none">
    <span class="time-text" style="color: red;font-size: 15px;">审核未通过</span>
</div>
<div class="commodity-content-footer" id="passIng" style="display: none">
    <span class="time-text" style="color: red;font-size: 15px;">审核中</span>
</div>
<div class="commodity-content-footer" id="pass" style="display: none">
    <span class="time-text" style="color: red;font-size: 15px;">审核已通过,办单员处理订单中</span>
</div>
<div class="commodity-content-footer" id="waitBindCard" style="display: none">
    <span class="time-text" style="color: red;font-size: 15px;">审核已通过,等待办单员绑卡</span>
</div>
<div class="commodity-content-footer">
    <button class="affirm" style="display:none;color: red;background:#fff;font-size: 15px;margin-left:auto;" id='ljzf_btn' onclick="zhifuqianhzi()" >立即支付</button> <%--立即支付前置服务包--%>
    <button class="affirm" style="display:none;color: red;background:#fff;font-size: 15px;margin-left:auto;" id='sureHetong_btn' onclick="checkContract('false')" >确认合同</button><%--新增确认合同按钮--%>
</div>
<%--<div class="commodity-content-footer" id='sureHetong' style="display: none;">--%>
    <%--<button class="affirm" style="color: red;background:#fff;font-size: 15px;" id='sureHetong_btn' onclick="checkContract('false')" >确认合同</button>&lt;%&ndash;新增确认合同按钮&ndash;%&gt;--%>
<%--</div>--%>
<div class="commodity-content-footer" id="theLast" style="width: 100%;">
    <button class="affirm" style="color: red;background:#fff; width: 46%;  display: none " onclick="jieqingbenqi()"  id="jqbq">结清本期</button>
    <button class="affirm" style="color: red;background:#fff; width: 46%;  display: none " onclick="quanbujieqing()" id="qbjq">全部结清</button>
</div>
<%@include file ="../package.jsp"%>
<script src="${ctx}/resources/merch-wechat/js/customer/historyDetailsOrder.js${version}"></script>
</body>
</html>