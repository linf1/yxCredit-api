<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>注册</title>
    <%@include file ="../common/input.jsp"%>
    <%@include file ="../common/taglibs.jsp"%>
    <meta name="viewport" content="width=device-width, initial-scale=1,  user-scalable=no">
    <link rel="stylesheet" href="${ctx}/resources/merch-wechat/css/style.css${version}">
    <link rel="stylesheet" href="${ctx}/resources/merch-wechat/css/base.css${version}">
</head>
<body id="registerContainer">
    <div class="toolBar" style="border-bottom: none;box-shadow: none;">
        <p><span class="icon-close toolBarRightBtn" onclick="history.go(-1)"></span></p>
    </div>
    <div class="logoTitle">
        <p>欢迎注册秒付金服!</p>
    </div>
    <div class="loginForm">
        <div class="inputItems">
            <label for="userPhone">
                <span class="icon-phone"></span>
                <input type="text" name="userPhone" id="userPhone" placeholder="请输入手机号码" maxlength="11">
            </label>
            <button class="getCode">获取验证码</button>
        </div>
        <div class="inputItems">
            <label for="phoneCode">
                <span class="icon-code" style="font-size: 14px;"></span>
                <input type="text" name="phoneCode" id="phoneCode" placeholder="请输入验证码" maxlength="6">
            </label>
        </div>
        <div class="inputItems">
            <label for="userPwd">
                <span class="icon-pwd"></span>
                <input type="password" name="userPwd" id="userPwd" placeholder="请输入密码" maxlength="20">
            </label>
            <span class="icon-eye pwdBtn"></span>
        </div>
    </div>
    <div class="commBtn" style="margin-bottom: 0">
        <button>注&nbsp;&nbsp;册</button>
    </div>
    <div class="registerFooter">
        <p>注册表示已同意<span class="registerPageInfo">《秒付金服服务协议》</span></p>
    </div>
</body>
<script src="${ctx}/resources/merch-wechat/js/register.js${version}"></script>
</html>