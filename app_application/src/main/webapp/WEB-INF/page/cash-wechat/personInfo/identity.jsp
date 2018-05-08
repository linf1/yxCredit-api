<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <title>身份认证</title>
    <%@include file ="../../common/input.jsp"%>
    <%@include file ="../../common/taglibs.jsp"%>
    <link rel="stylesheet" href="${ctx}/resources/cash-wechat/css/style.css${version}">
    <link rel="stylesheet" href="${ctx}/resources/cash-wechat/css/myCss.css${version}">
</head>
<body style="background:#f6f6f6">
<input type="hidden" id="orderId" value="${param.orderId}">
    <div class="toolBar">
        <p><span class="icon-back" onclick="history.go(-1)"></span><span>借款申请</span></p>
    </div>
    <div class="infoTitle">
        <div class="infoContent">
            <p class="infoLine"></p>
            <p class="infoImg infoImgActive"></p>
            <p class="infoWord">身份认证</p>
        </div>
        <div class="infoContent">
            <p class="infoLine"></p>
            <p class="infoImg"></p>
            <p class="infoWord">个人信息</p>
        </div>
        <%--<div class="infoContent">--%>
            <%--<p class="infoLine"></p>--%>
            <%--<p class="infoImg"></p>--%>
            <%--<p class="infoWord">人脸识别</p>--%>
        <%--</div>--%>
        <div class="infoContent">
            <p class="infoLine"></p>
            <p class="infoImg"></p>
            <p class="infoWord">授权</p>
        </div>
    </div>
    <div class="infoPhoto" >
        <p>上传身份证照片,进行身份信息认证</p>
        <div class="infoPhotoContent">
            <div class="infoPhotoImg" id="idCardOne">
                <div style="height: 106px;width: 100%"><img id="idcardfront"  onerror="this.src='${ctx}/resources/cash-wechat/images/idcardFront.png'" src="${ctx}/resources/cash-wechat/images/idcardFront.png" alt=""></div>
                <p>身份证正面</p>
                <input type="hidden" id="idCardFrontValue">
            </div>
            <div class="infoPhotoImg" id="idCardTwo">
                <div style="height: 106px;width: 100%"><img id="idcardback"  onerror="this.src='${ctx}/resources/cash-wechat/images/idcardFront.png'" src="${ctx}/resources/cash-wechat/images/idcardFront.png" alt=""></div>
                <p>身份证反面</p>
                <input type="hidden" id="idCardBackValue">
            </div>
        </div>
    </div>
    <div class="infoMsg">
        <p>请输入以下身份认证信息</p>
    </div>
    <div class="infoForm">
        <div class="weui-cells weui-cells_form">
            <div class="weui-cell">
                <div class="weui-cell__hd"><label class="weui-label"><span class="icon-scanName"></span>真实姓名</label></div>
                <div class="weui-cell__bd">
                    <input class="weui-input" type="text" maxlength="10" placeholder="请输入真实姓名" name="userRealName" id="userRealName">
                </div>
            </div>
            <div class="weui-cell">
                <div class="weui-cell__hd"><label class="weui-label"><span class="icon-scanSex"></span>性别</label></div>
                <div class="weui-cell__bd">
                    <input class="weui-input" type="text" maxlength="2" placeholder="请输入性别" name="userRealGender" id="userRealGender">
                </div>
            </div>
            <div class="weui-cell">
                <div class="weui-cell__hd"><label class="weui-label"><span class="icon-scanBirth"></span>出生日期</label></div>
                <div class="weui-cell__bd">
                    <input class="weui-input" type="text" maxlength="" placeholder="请输入出生日期(如:1990.01.01)" name="userRealBirthday" id="userRealBirthday">
                </div>
            </div>
            <div class="weui-cell">
                <div class="weui-cell__hd"><label class="weui-label"><span class="icon-scanCard"></span>身份证号码</label></div>
                <div class="weui-cell__bd">
                    <input class="weui-input" type="text" maxlength="18" placeholder="请输入身份证号码" name="userIdCardNum" id="userIdCardNum">
                </div>
            </div>
            <div class="weui-cell">
                <div class="weui-cell__hd"><label class="weui-label"><span class="icon-scanAddress"></span>户籍地址</label></div>
                <div class="weui-cell__bd">
                    <input class="weui-input" type="text" maxlength="50" placeholder="请输入户籍地址" name="userAddress" id="userAddress">
                </div>
            </div>
        </div>
        <button class="weui-btn my-btn" style="margin: 30px auto;" id="identityBtn">进行验证</button>
    </div>
</body>
<script src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
<script src="${ctx}/resources/cash-wechat/js/personInfo/identity.js${version}"></script>
</html>