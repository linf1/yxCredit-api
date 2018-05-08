<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <title>开户</title>
    <%@include file ="../../common/input.jsp"%>
    <%@include file ="../../common/taglibs.jsp"%>
    <link rel="stylesheet" href="${ctx}/resources/cash-wechat/css/style.css${version}">
    <link rel="stylesheet" href="${ctx}/resources/cash-wechat/css/myCss.css${version}">
    <style>
        #weui-prompt-input{border:1px solid #ddd;line-height: 30px;height: 30px;}
    </style>
</head>
<body  style="background:#f6f6f6">
<input type="hidden" id="is_openAccount">
    <div class="toolBar">
        <p><span class="icon-back" onclick="history.go(-1)"></span><span>绑定银行卡</span></p>
    </div>
    <div class="cardInfoOne">
        <p><span>账户类型</span><span class="right">易宝账户</span></p>
        <p><span>姓名</span><span class="right" id="person_name"></span></p>
        <p><span>身份证号码</span><span class="right" id="card"></span></p>
    </div>
    <div class="cardInfoMsg">
        <p>绑定银行卡</p>
    </div>
    <div class="cardInfoTwo" style="padding: 0">
        <%--<div class="weui-cell">--%>
            <%--<div class="weui-cell__hd"><label class="weui-label">开户银行</label></div>--%>
            <%--<div class="weui-cell__bd">--%>
                <%--<input readonly class="weui-input" type="text" placeholder="请选择" name="cardBankName" maxlength="20">--%>
            <%--</div>--%>
        <%--</div>--%>
        <div class="weui-cell weui-cell_select weui-cell_select-after">
            <div class="weui-cell__hd">
                <label class="weui-label">开户银行</label>
            </div>
            <div class="weui-cell__bd">
                <select class="weui-select" name="cardBankName">
                    <option value="1">中国银行</option>
                    <option value="2">中国农业银行</option>
                </select>
            </div>
        </div>
        <div class="weui-cell">
            <div class="weui-cell__hd"><label class="weui-label">银行卡号</label></div>
            <div class="weui-cell__bd">
                <input class="weui-input" type="text" placeholder="请输入银行卡号" name="cardNum" maxlength="19" onkeyup="value=value.replace(/[^\d]/g,'')">
            </div>
        </div>
        <div class="weui-cell">
            <div class="weui-cell__hd"><label class="weui-label">银行预留手机号</label></div>
            <div class="weui-cell__bd">
                <input class="weui-input" type="text" placeholder="请输入银行预留手机号" name="phoneNum" maxlength="11" onkeyup="value=value.replace(/[^\d]/g,'')">
            </div>
        </div>
        <div class="weui-cell">
            <div class="weui-cell__hd">
                <label class="weui-label">手机验证码</label>
            </div>
            <div class="weui-cell__bd">
                <input class="weui-input" type="text" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="6" name="phoneCode" placeholder="请输入短信验证码">
            </div>
            <div class="weui-cell__ft">
                <button class="getCodeBtn" style="border:none;background: #fff;outline: none">获取验证码</button>
            </div>
        </div>
        <%--<div class="weui-cell">--%>
            <%--<div class="weui-cell__hd"><label class="weui-label">银行卡开户省市</label></div>--%>
            <%--<div class="weui-cell__bd">--%>
                <%--<input style="text-align: right" readonly class="weui-input" type="text" placeholder="请选择开户省市" name="bankCity" maxlength="30">--%>
            <%--</div>--%>
        <%--</div>--%>
        <%--<div class="weui-cell">--%>
            <%--<div class="weui-cell__hd"><label class="weui-label">银行卡开户支行</label></div>--%>
            <%--<div class="weui-cell__bd">--%>
                <%--<input style="text-align: right" class="weui-input" type="text" placeholder="请输入银行卡开户支行" name="cardSubBankName" maxlength="20">--%>
            <%--</div>--%>
        <%--</div>--%>
    </div>
    <div style="margin: 30px auto;">
        <button class="weui-btn my-btn" id="kaihu-btn">开户</button>
    </div>
    <script src="${ctx}/resources/lib/js/city-picker.js${version}"></script>
    <script src="${ctx}/resources/cash-wechat/js/centerMsg/openAccount.js${version}"></script>
</body>
</html>