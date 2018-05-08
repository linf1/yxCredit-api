<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1,  user-scalable=no">
    <title>忘记密码</title>
    <%@include file ="../common/input.jsp"%>
    <%@include file ="../common/taglibs.jsp"%>
    <link rel="stylesheet" href="${ctx}/resources/cash-wechat/css/style.css${version}"/>
    <link rel="stylesheet" href="${ctx}/resources/cash-wechat/css/forget.css${version}"/>
</head>
<body ontouchstart style="background: #F6F6F6">
    <div class="toolBar">
        <p><span class="icon-back" onclick="history.go(-1)"></span><span>忘记密码</span></p>
    </div>
    <div id="forgetContainer" style="display: block">
        <div id="forgetContainerOne">
            <div class="weui-cell">
                <div class="weui-cell__hd">
                    <label class="weui-label">手机号</label>
                </div>
                <div class="weui-cell__bd">
                    <input class="weui-input" id="userPhone" name="userPhone" type="text"  onkeyup="value=value.replace(/[^\d]/g,'')"  maxlength="11" placeholder="请输入注册手机号">
                </div>
            </div>
            <div class="weui-cell">
                <div class="weui-cell__hd">
                    <label class="weui-label">图形验证码</label>
                </div>
                <div class="weui-cell__bd">
                    <input class="weui-input" id="userCode" name="userCode"  maxlength="4" type="text" placeholder="请输入图形验证码">
                </div>
                <div class="weui-cell__ft">
                    <div id="v_container" style="width: 100px;height: 30px;"></div>
                </div>
            </div>
        </div>
        <div id="forgetContainerTwo">
            <div class="weui-cell">
                <div class="weui-cell__hd">
                    <label class="weui-label">短信验证码</label>
                </div>
                <div class="weui-cell__bd">
                    <input class="weui-input" type="text" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="6" name="phoneNum" placeholder="请输入短信验证码">
                </div>
                <div class="weui-cell__ft">
                    <button class="getCodeBtn" onclick="getPhoneCode(this)">获取验证码</button>
                </div>
            </div>
        </div>
        <div class="btnContainer">
            <button onclick="getNext()">下一步</button>
        </div>
    </div>
    <div id="forgetContainerNext" style="display: none">
        <div style="background: #fff">
            <div class="weui-cell">
                <div class="weui-cell__hd">
                    <label class="weui-label">新密码</label>
                </div>
                <div class="weui-cell__bd">
                    <input class="weui-input" id="wechat-newpwd" type="tel" placeholder="请输入新密码">
                </div>
            </div>
            <div class="weui-cell">
                <div class="weui-cell__hd">
                    <label class="weui-label" id="wechat-confirmpwd">确认密码</label>
                </div>
                <div class="weui-cell__bd">
                    <input class="weui-input" type="tel" placeholder="确认新密码">
                </div>
            </div>
        </div>
        <div class="btnContainer">
            <button  id="btn-updatepwd">确定</button>
        </div>
    </div>
    <script src="${ctx}/resources/lib/js/gVerify.js${version}"></script>
    <script src="${ctx}/resources/cash-wechat/js/forget.js${version}"></script>
</body>
</html>