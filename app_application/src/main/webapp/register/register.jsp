<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="textml; charset=utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,Chrome=1">
    <%@include file ="../WEB-INF/page/common/input.jsp"%>
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <title>秒付注册</title>
    <link rel="stylesheet" href="${ctx}/resources/lib/css/reset.css${version}">
    <link rel="stylesheet" href="${ctx}/resources/register/css/style.css${version}">
    <link rel="stylesheet" href="${ctx}/resources/register/css/register.css${version}">
    <style>
        .header{background: url(${ctx}/resources/register/images/banner.png${version}) no-repeat center center;background-size: cover}
    </style>
</head>
<body>
    <input type="hidden" value="${param.referenceId}" id="referenceId">
    <div class="header">
        <p>秒付消费金融平台</p>
    </div>
    <div class="bodyInfo">
        <div class="infoItemsOne">
            <img src="${ctx}/resources/register/images/title.png${version}" alt="">
        </div>
        <div class="infoItems">
            <label for="userPhoneNum">
                <span class="icon-phone"></span>
                <input type="text" id="userPhoneNum" name="userPhoneNum" placeholder="请输入手机号" maxlength="11">
            </label>
        </div>
        <div class="infoItems">
            <label for="userPhonePwd">
                <span class="icon-pwd"></span>
                <input type="password" id="userPhonePwd" name="userPhonePwd" placeholder="请输入6位数字或字母组成的密码" maxlength="20">
                <span class="icon-eye" id="changeEye"></span>
            </label>
        </div>
        <div class="infoItems">
            <label for="userPhoneCode">
                <span class="icon-code"></span>
                <input type="text" id="userPhoneCode" name="userPhoneCode" placeholder="请输入手机验证码" maxlength="6">
                <button id="getCode">获取验证码</button>
            </label>
        </div>
        <div class="btnContainer">
            <button id="registerBtn">注册领注册额度</button>
        </div>
    </div>
</body>
<script src="${ctx}/resources/lib/js/jquery-2.1.4.js"></script>
<script src="${ctx}/resources/lib/js/common.js${version}"></script>
<script src="${ctx}/resources/lib/layer_mobile/layer.js"></script>
<script src="${ctx}/resources/lib/ajaxLoading/spin.js"></script>
<script src="${ctx}/resources/lib/ajaxLoading/loading.js"></script>
<script src="${ctx}/resources/register/js/register.js${version}"></script>
</html>
