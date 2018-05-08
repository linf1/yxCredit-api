<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>登录</title>
    <meta name="viewport" content="width=device-width, initial-scale=1,  user-scalable=no">
    <%@include file ="../common/input.jsp"%>
    <%@include file ="../common/taglibs.jsp"%>
    <link rel="stylesheet" href="${ctx}/resources/merch-wechat/css/style.css${version}">
    <link rel="stylesheet" href="${ctx}/resources/merch-wechat/css/base.css${version}">
</head>
<body>
    <div class="logoBg"></div>
    <div class="loginForm">
        <div class="inputItems">
            <label for="userPhone">
                <span class="icon-phone"></span>
                <input type="text" name="userPhone" id="userPhone" placeholder="请输入手机号码" maxlength="11">
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
    <div class="commBtn">
        <button>登&nbsp;&nbsp;录</button>
    </div>
    <div class="loginFooter">
        <p><span>忘记密码？</span><span>立即注册</span></p>
    </div>
</body>
<script src="${ctx}/resources/merch-wechat/js/login.js${version}"></script>
</html>