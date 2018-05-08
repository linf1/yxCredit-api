<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <title>待放款订单</title>
    <%@include file ="../../common/input.jsp"%>
    <%@include file ="../../common/taglibs.jsp"%>
    <link rel="stylesheet" href="${ctx}/resources/cash-wechat/css/style.css${version}">
    <link rel="stylesheet" href="${ctx}/resources/cash-wechat/css/myCss.css${version}">
</head>
<body style="background:#f6f6f6">
<input type="hidden" id="orderId" value="${param.orderId}">
    <div class="toolBar">
        <p><span class="icon-back" onclick="history.go(-1)"></span><span>待放款订单</span></p>
    </div>
    <div class="orderInfoTitle">
        <p style="height: 30px;line-height: 20px;"><span class="icon-borrowMoney"><span class="path1"></span><span class="path2"></span></span><span style="font-weight: bold;margin-left: 4px">现金借款</span></p>
        <div class="titleContainer">
            <div class="titleContent">
                <p>申请金额</p>
                <p ><span id="wechat-forlending-amount"></span></p>
            </div>
            <div class="titleContent">
                <p>借款期限</p>
                <p ><span id="wechat-forlending-periods"></span></p>
            </div>
            <div class="titleContent">
                <p>借款单状态</p>
                <p ><span id="wechat-forlending-tache"></span></p>
            </div>
        </div>
    </div>
    <div class="orderInfoBody">
        <div class="orderInfoBodyTitleContainer">
            <div class="orderInfoBodyTitleContent"><span class="orderLine"></span></div>
            <div class="orderInfoBodyTitleContent">订单进度</div>
            <div class="orderInfoBodyTitleContent"><span class="orderLine"></span></div>
        </div>
         <div class="orderStateContainer">
            <!--<div class="orderStateContent orderStateNull">
               <p style="font-weight: bold;"><span class="current_up"></span><span>放款成功</span><span class="right"></span></p>
               <div class="orderStateLine">
                   <p>您的借款申请初审已审批通过,进入总部审核流程,请耐心等到审核结果。</p>
               </div>
            </div>
            <div class="orderStateContent orderStateNull">
                <p style="font-weight: bold;"><span class="current_up"></span><span>开户授权</span><span class="right"></span></p>
                <div class="orderStateLine">
                    <p>您的借款申请初审已审批通过,进入总部审核流程,请耐心等到审核结果。</p>
                </div>
            </div>
            <div class="orderStateContent orderStateNull">
                <p style="font-weight: bold;"><span class="current_up"></span><span>总部审核通过</span><span class="right"></span></p>
                <div class="orderStateLine">
                    <p>您的借款申请初审已审批通过,进入总部审核流程,请耐心等到审核结果。</p>
                </div>
            </div>
            <div class="orderStateContent">
                <p style="font-weight: bold;"><span class="current"></span><span class="currentTitle">初审通过</span><span class="right">2107-10-10 10:10</span></p>
                <div class="orderStateLine">
                    <p>您的借款申请初审已审批通过,进入总部审核流程,请耐心等到审核结果。</p>
                </div>
            </div>
            <div class="orderStateContent">
                <p style="font-weight: bold;"><span class="current_down"></span><span>申请完成</span><span class="right">2107-10-10 10:10</span></p>
                <div class="orderStateLine">
                    <p>您的借款申请初审已审批通过,进入总部审核流程,请耐心等到审核结果。</p>
                </div>
            </div>-->
        </div>
    </div>
</body>
<script src="${ctx}/resources/cash-wechat/js/mine/waitOrder.js${version}"></script>
</html>