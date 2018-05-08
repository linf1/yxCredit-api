<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>秒付首页</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <%@include file ="../common/input.jsp"%>
    <%@include file ="../common/taglibs.jsp"%>
    <link rel="stylesheet" href="${ctx}/resources/merch-wechat/css/home.css${version}">
</head>
<body>
<div class="weui-tab">
    <div class="weui-tab__bd">
        <div id="tab1" class="weui-tab__bd-item weui-tab__bd-item--active">
            <%@include file="customer/scan.jsp"%>
        </div>
        <div id="tab2" class="weui-tab__bd-item">
            <%@include file="customer/repaymentTwo.jsp"%>
        </div>
        <div id="tab3" class="weui-tab__bd-item">
            <%@include file="centerMsg/messageCenter.jsp"%>
        </div>
        <div id="tab4" class="weui-tab__bd-item">
            <%@include file="mine/mine.jsp"%>
        </div>
    </div>
    <div class="weui-tabbar">
        <a href="#tab1" class="weui-tabbar__item weui-bar__item--on">
            <div class="weui-tabbar__icon"></div>
            <p class="weui-tabbar__label">首页</p>
        </a>
        <a href="#tab2" class="weui-tabbar__item">
            <div class="weui-tabbar__icon"></div>
            <p class="weui-tabbar__label">还款</p>
        </a>
        <a href="#tab3" class="weui-tabbar__item">
            <div class="weui-tabbar__icon"></div>
            <p class="weui-tabbar__label">消息</p>
        </a>
        <a href="#tab4" class="weui-tabbar__item">
            <div class="weui-tabbar__icon"></div>
            <p class="weui-tabbar__label">我的</p>
        </a>
    </div>
</div>
</body>
</html>