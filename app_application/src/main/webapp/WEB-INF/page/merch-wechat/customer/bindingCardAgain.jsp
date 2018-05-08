<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <%@include file ="../../common/input.jsp"%>
    <%@include file ="../../common/taglibs.jsp"%>
    <title>绑定银行卡</title>
    <style>
        #bindingCard{
            color:#1a1a1d;
            border:none;
            margin:30px 30px;
            border-radius:50px;
            background-color:#ffda44;
            box-shadow: 0px 3px 3px rgba(248,214,201,1);
        }
        #codeText{
            color:#fff;
            font-size:13px;
            background-color:#FF6600;
            text-align:center;
            padding:3px 5px;
            display:inline-block;
            width:100%;
        }
    </style>
</head>
<body style="background: #f6f6f6;">
<div class="weui-cells weui-cells_form" style="margin-top: 0.5em;">
    <div class="weui-cell">
        <div class="weui-cell__hd"><label class="weui-label" style="width: 150px;">账户类型</label></div>
        <div class="weui-cell__bd" style="text-align: right;" id="accountTypeDiv">

        </div>
    </div>
    <div class="weui-cell">
        <div class="weui-cell__hd"><label class="weui-label" style="width: 150px;">姓名</label></div>
        <div class="weui-cell__bd" style="text-align: right;" id="nameDiv">

        </div>
    </div>
    <div class="weui-cell">
        <div class="weui-cell__hd"><label class="weui-label" style="width: 150px;">身份证号码</label></div>
        <div class="weui-cell__bd" style="text-align: right;" id="idNoDiv">

        </div>
    </div>

    <div class="weui-cells__title" style="background: #f6f6f6; margin-top: 0px;margin-bottom: 0px;padding-top: 0.5em;padding-bottom: 0.5em;font-size: 1em;">绑定银行卡</div>
    <div class="weui-cell weui-cell_select weui-cell_select-after">
        <div class="weui-cell__hd">
            <label class="weui-label" style="width: 150px;">开户银行</label>
        </div>
        <div class="weui-cell__bd">
            <select class="weui-select" name="select2" id="banks">
            </select>
        </div>
    </div>
    <div class="weui-cell">
        <div class="weui-cell__hd">
            <label class="weui-label" style="width: 150px;">银行卡号</label>
        </div>
        <div class="weui-cell__bd">
            <input class="weui-input" style="font-size: 15px;" type="tel" placeholder="请输入" id="bankCard">
        </div>
    </div>
    <div class="weui-cell">
        <div class="weui-cell__hd">
            <label class="weui-label" style="width: 150px;">银行预留手机号</label>
        </div>
        <div class="weui-cell__bd">
            <input class="weui-input" type="tel" placeholder="请输入" id="phone">
        </div>
    </div>
    <div class="weui-cell">
        <div class="weui-cell__hd" style="width: 150px;"><label class="weui-label">手机验证码</label></div>
        <div class="weui-cell__bd">
            <input class="weui-input" type="number" placeholder="请输入" id="phoneCode">
        </div>
        <div class="weui-cell__ft">
            <span id="codeText">获取验证码</span>
        </div>
    </div>

</div>
<span class="weui-btn" id="bindingCard">绑定银行卡</span>
<script src="${ctx}/resources/merch-wechat/js/customer/bindingCardAgain.js${version}"></script>
</body>
</html>
