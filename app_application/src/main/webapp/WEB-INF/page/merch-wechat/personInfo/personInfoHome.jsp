<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>申请信息</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <%@include file ="../../common/input.jsp"%>
    <%@include file ="../../common/taglibs.jsp"%>
    <link rel="stylesheet" href="${ctx}/resources/merch-wechat/css/style.css${version}">
    <link rel="stylesheet" href="${ctx}/resources/merch-wechat/css/personInfo.css${version}">
</head>
<body style="background: #F6F6F6">
    <%--<div class="toolBar">--%>
        <%--<p><span class="icon-back" onclick="history.go(-1)"></span><span>申请信息</span></p>--%>
    <%--</div>--%>
    <div class="personInfoContainer">
        <div class="weui-tab">
            <div class="weui-navbar">
                <a class="weui-navbar__item weui-bar__item--on" href="#tab1"><p>个人信息</p> <b></b></a>
                <a class="weui-navbar__item" href="#tab2"><p>基本信息</p><b></b></a>
                <a class="weui-navbar__item" href="#tab3"><p>授权信息</p><b></b></a>
            </div>
            <div class="weui-tab__bd">
                <div id="tab1" class="weui-tab__bd-item weui-tab__bd-item--active">
                    <%@include file ="personInfo.jsp"%>
                </div>
                <div id="tab2" class="weui-tab__bd-item">
                    <%@include file="basicInfo.jsp"%>
                </div>
                <div id="tab3" class="weui-tab__bd-item">
                    <%@include file="accreditInfo.jsp"%>
                </div>
            </div>
        </div>
    </div>
    <script src="${ctx}/resources/merch-wechat/js/personInfo/personInfo.js${version}"></script>

</body>
</html>