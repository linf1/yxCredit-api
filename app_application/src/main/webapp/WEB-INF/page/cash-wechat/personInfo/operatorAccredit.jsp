<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<head>
    <title>运营商授信</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <%@include file ="../../common/input.jsp"%>
    <%@include file ="../../common/taglibs.jsp"%>
    <link rel="stylesheet" href="${ctx}/resources/cash-wechat/css/style.css${version}">
    <link rel="stylesheet" href="${ctx}/resources/cash-wechat/css/myCss.css${version}">
    <style>
        .toolBar{background: #fff;}
        .operatorBgImg{background: url(${ctx}/resources/cash-wechat/images/operatorBg.jpg) no-repeat center center;height: 40%;background-size: cover;}
        .operatorInfoFooter{text-align: center;margin-top: 16px; margin-bottom: 10px;}
        .operatorInfoFooter button{width: 80%;height: 40px;line-height: 40px;border: none;border-radius: 30px;background: #FFDA44;outline: none}
        .promptInfo{font-size: 12px;color: #666;margin: 40px 15px;}
        .promptInfo p{line-height: 20px}
        #weui-prompt-input{border:1px solid #ddd;height: 30px;line-height: 30px}
        #operatorInfo{margin: 10px;display: none}
    </style>
</head>
<body style="background:#f6f6f6">
    <div class="toolBar">
        <p><span class="icon-back" onclick="history.go(-1)"></span><span>运营商授信</span></p>
    </div>
    <div class="operatorBgImg"></div>
    <div class="operatorContainer">
        <div class="operatorInfo">
            <div class="operatorInfoBody">
                <div class="weui-cell">
                    <div class="weui-cell__hd"><span class="icon-phone"></span></div>
                    <div class="weui-cell__bd">
                        <input class="weui-input"  name="authTelnum" type="text" placeholder="请输入手机号" maxlength="11">
                    </div>
                </div>
                <div class="weui-cell">
                    <div class="weui-cell__hd"><span class="icon-pwd1" style="color: #F4EA29;"></span></div>
                    <div class="weui-cell__bd">
                        <input class="weui-input"  name="authTelpass" type="password" placeholder="请输入手机服务密码" maxlength="20">
                    </div>
                </div>
                <div class="operatorInfoFooter">
                    <button>授权</button>
                </div>
            </div>
        </div>
    </div>
    <div class="promptInfo">
        <p>温馨提示:</p>
        <p>1、登录成功后将收到运营商通知短信,无需回复</p>
        <p>2、忘记服务密码?<span style="color:#FF8254;margin-left: 4px" id="getLostPwd">找回手机服务密码&gt;</span></p>
        <div id="operatorInfo">
            <p>联通：找回方式XXXXXXXXXXX</p>
            <p>移动：找回方式XXXXXXXXXXX</p>
            <p>电信：找回方式XXXXXXXXXXX</p>
        </div>
    </div>
</body>
<script src="${ctx}/resources/cash-wechat/js/personInfo/operatorAccredit.js${version}"></script>
</html>
