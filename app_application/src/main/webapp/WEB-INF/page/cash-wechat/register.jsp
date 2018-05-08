<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1,  user-scalable=no">
    <title>注册</title>
    <%@include file ="../common/input.jsp"%>
    <%@include file ="../common/taglibs.jsp"%>
    <link rel="stylesheet" href="${ctx}/resources/cash-wechat/css/style.css${version}"/>
    <link rel="stylesheet" href="${ctx}/resources/cash-wechat/css/register.css${version}"/>
</head>
<body ontouchstart>
<div id="registerContainer">
    <div class="toolBar">
        <p><span class="icon-back" onclick="history.go(-1)"></span><span>注册</span></p>
    </div>
    <div id="logo">
        <img src="${ctx}/resources/cash-wechat/images/login.png" alt="" class="logo"/>
    </div>
    <div class="content-wrapper">
        <div class="weui-cells weui-cells_form">
                <div class="weui-cell">
                    <div class="weui-cell__hd"><label class="icon-phone"></label></div>
                    <div class="weui-cell__bd">
                        <input class="weui-input" value="" name="userPhone" id="userPhone" type="number" pattern="[0-9]*"  maxlength="11"  onkeyup="value=value.replace(/[^\d]/g,'')" placeholder="请输入手机号">
                    </div>
                    <div class="weui-cell__ft"></div>
                </div>
            </div>
        <div class="weui-cells weui-cells_form">
                <div class="weui-cell">
                    <div class="weui-cell__hd"><label class="icon-pwd"></label></div>
                    <div class="weui-cell__bd">
                        <input class="weui-input" value="" type="password" name="userPwd" id="userPwd"  maxlength="20"  placeholder="请输入密码">
                    </div>
                    <div class="">
                        <span style="font-size: 20px" class="icon-eye" onclick="changeType(this)" isEye="true"></span>
                    </div>
                </div>
            </div>
        <div class="weui-cells weui-cells_form">
                <div class="weui-cell">
                    <div class="weui-cell__hd"><label class="icon-code"></label></div>
                    <div class="weui-cell__bd">
                        <input class="weui-input" type="number" name="usering" id="usering" pattern="[0-9]*"  maxlength="6"  placeholder="请输入验证码">
                    </div>
                    <div class="">
                        <button id="btn" onclick="getSmsCode(this)">获取验证码</button>
                    </div>
                </div>
            </div>
    </div>
    <p style="margin: 0 10%"><span onclick="changeBtn(this)" isAgree="true" class="icon-orhook" style="font-size: 20px" ><span style="font-size: 12px;color:#A7A7A7">已阅读并同意</span></span><span style="color:#333;font-size: 12px" onclick="checkRegister('2')">《注册协议》</span></p>
    <div class="registContainer">
        <button class="registBtn">注册</button>
    </div>
    <div class="ulogin">
        <p>已有秒付全部账号？去<span  onclick="history.go(-1)">登录</span></p>
    </div>
</div>
<script src="${ctx}/resources/cash-wechat/js/register.js${version}"></script>
<script>
    function checkRegister(code) {
        window.location=_ctx+"/wechat/register/goToCheckRegister?code="+code
    }
</script>
</body>
</html>