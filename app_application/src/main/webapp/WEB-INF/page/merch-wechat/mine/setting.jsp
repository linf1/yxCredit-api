<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <title>设置</title>
    <%@include file ="../../common/input.jsp"%>
    <%@include file ="../../common/taglibs.jsp"%>
    <link rel="stylesheet" href="${ctx}/resources/cash-wechat/css/style.css${version}">
    <link rel="stylesheet" href="${ctx}/resources/cash-wechat/css/myCss.css${version}">
    <style>
        #setting .weui-cell:before{border-top: 1px solid #F0F0F0;}
    </style>
</head>
<body style="background:#f6f6f6">
    <div class="toolBar">
        <p><span class="icon-back" onclick="GoBack()"></span><span id="settingTitle">设置</span></p>
    </div>
    <div id="setting" style="display: block">
        <div style="margin-top: 50px">
            <div class="weui-cells">
                <a class="weui-cell weui-cell_access" id="showUpLoadImg">
                    <div class="weui-cell__bd">
                        <p>头像</p>
                    </div>
                    <div class="weui-cell__ft">
                        <div class="imgContent" style="width: 30px;height: 30px"><img id="wechat-headimg" src="${ctx}/resources/cash-wechat/images/headImgDefault.png" onerror="this.src='${ctx}/resources/cash-wechat/images/headImgDefault.png'" alt=""></div>
                    </div>
                </a>
                <a style="height: 30px" class="weui-cell weui-cell_access" onclick="showUpdatePwd()">
                    <div class="weui-cell__bd">
                        <p>重置密码</p>
                    </div>
                    <div class="weui-cell__ft">
                    </div>
                </a>
                <a style="height: 30px" class="weui-cell weui-cell_access" onclick="goBindedCard()">
                    <div class="weui-cell__bd">
                        <p>绑定的银行卡</p>
                    </div>
                    <div class="weui-cell__ft">
                    </div>
                </a>
            </div>
        </div>
        <div style="margin: 30px auto;">
            <button class="weui-btn my-btn" id="wechat-logout">退出登录</button>
        </div>
    </div>
    <div id="updatePwd" style="display: none">
        <div class="" style="margin-top: 50px;font-size: 14px;background: #fff">
            <div class="weui-cell">
                <div class="weui-cell__hd"><label class="weui-label">原密码</label></div>
                <div class="weui-cell__bd">
                    <input class="weui-input" type="password" placeholder="请输入原密码" name="oldPassWord">
                </div>
            </div>
            <div class="weui-cell">
                <div class="weui-cell__hd"><label class="weui-label">新密码</label></div>
                <div class="weui-cell__bd">
                    <input class="weui-input" type="password" placeholder="请输入新密码" name="newPassWord">
                </div>
            </div>
            <div class="weui-cell">
                <div class="weui-cell__hd"><label class="weui-label">确认密码</label></div>
                <div class="weui-cell__bd">
                    <input class="weui-input" type="password" placeholder="请再次输入新密码" name="repeatPassWord">
                </div>
            </div>
        </div>
        <button class="weui-btn my-btn" style="margin: 30px auto;" id="updatePwdBtn">确认修改</button>
    </div>
</body>
<script src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
<script src="${ctx}/resources/cash-wechat/js/mine/setting.js${version}"></script>
</html>