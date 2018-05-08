<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>秒付首页</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <%@include file ="../common/input.jsp"%>
    <%@include file ="../common/taglibs.jsp"%>
    <link rel="stylesheet" href="${ctx}/resources/cash-wechat/css/style.css${version}">
    <link rel="stylesheet" href="${ctx}/resources/cash-wechat/css/myCss.css${version}">
</head>
<body>
<div class="weui-tab">
    <div class="weui-tab__bd">
        <div id="tab1" class="weui-tab__bd-item weui-tab__bd-item--active">
            <%@include file ="currentPage.jsp"%>
        </div>
        <div id="tab2" class="weui-tab__bd-item">
            <%@include file ="paymentPage.jsp"%>
        </div>
        <div id="tab3" class="weui-tab__bd-item">
           <%@include file="minePage.jsp"%>
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
            <p class="weui-tabbar__label">我的</p>
        </a>
    </div>
</div>
<script src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
<script src="${ctx}/resources/lib/js/swiper.min.js"></script>
<script src="${ctx}/resources/cash-wechat/js/home.js${version}"></script>
<script>
//    if(localStorage["isMine"]){//我的页面返回标志
//        $(".weui-tabbar .weui-tabbar__item").removeClass("weui-bar__item--on");
//        $(".weui-tabbar .weui-tabbar__item:last-child").addClass("weui-bar__item--on");
//        $(".weui-tab__bd .weui-tab__bd-item").removeClass("weui-tab__bd-item--active");
//        $("#tab3").addClass("weui-tab__bd-item--active");
//    }
</script>
</body>
</html>