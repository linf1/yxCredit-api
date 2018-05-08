<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1,  user-scalable=no">
    <title>登录</title>
    <%@include file ="../common/input.jsp"%>
    <%@include file ="../common/taglibs.jsp"%>
    <link rel="stylesheet" href="${ctx}/resources/cash-wechat/css/style.css${version}"/>
    <link rel="stylesheet" href="${ctx}/resources/cash-wechat/css/login.css${version}"/>
    <style>
        #weui-prompt-input{border:1px solid #ddd;height: 30px;line-height: 30px;text-align: center}
    </style>
</head>
<body ontouchstart>
    <div id="loginContainer">
        <div class="toolBar">
            <p><span>登录</span></p>
        </div>
        <div id="logo">
            <img src="${ctx}/resources/cash-wechat/images/login.png" alt="" class="logo"/>
        </div>
        <div class="content-wrapper">
            <div class="weui-cells weui-cells_form">
                    <div class="weui-cell">
                        <div class="weui-cell__hd"><label class="icon-phone"></label></div>
                        <div class="weui-cell__bd">
                            <input value="15755020540" class="weui-input" name="userPhone" id="userPhone" type="text"   maxlength="11"  onkeyup="value=value.replace(/[^\d]/g,'')" placeholder="请输入手机号">
                        </div>
                        <div class="weui-cell__ft"></div>
                    </div>
                </div>
            <div class="weui-cells weui-cells_form">
                    <div class="weui-cell">
                        <div class="weui-cell__hd"><label class="icon-pwd"></label></div>
                        <div class="weui-cell__bd">
                            <input value="111111" class="weui-input" type="password" name="userPwd" id="userPwd"  maxlength="20"  placeholder="请输入密码">
                        </div>
                        <div class="">
                            <span style="font-size: 20px" class="icon-eye" onclick="changeType(this)" isEye="true"></span>
                        </div>
                    </div>
                </div>
        </div>
        <div id="forget">
            <span id="forgetBtn">忘记密码？</span>
        </div>
        <div id="uselogin">
            <span>登录</span>
        </div>
        <div class="userregister">
            <p>没有账号？<span id="registerBtn">去注册</span></p>
        </div>
    </div>
    <script>
        $(function() {
            FastClick.attach(document.body);
        });
    </script>
    <script src="${ctx}/resources/lib/js/gVerify.js${version}"></script>
    <script src="${ctx}/resources/cash-wechat/js/login.js${version}"></script>
    <script>
        $("#forgetBtn").on("click",function (){
            window.location=_ctx+"/wechat/forget"
        })
        $("#registerBtn").on("click",function () {
            window.location=_ctx+"/wechat/register/toRegister"
        })
    </script>
</body>
</html>