<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1,  user-scalable=no">
    <%@include file ="../../common/input.jsp"%>
    <%@include file ="../../common/taglibs.jsp"%>
    <link rel="stylesheet" href="${ctx}/resources/cash-wechat/css/style.css${version}"/>
    <link rel="stylesheet" href="${ctx}/resources/cash-wechat/css/message.css${version}"/>
    <title>消息中心</title>
</head>
<body style="background:#f6f6f6">
    <div class="toolBar">
        <p><span class="icon-back"></span><span id="msgTitle">消息中心</span></p>
    </div>
    <div class="centerMsg" style="display: block">
        <div class="fixedSysMsg">
        </div>
        <div class="orderMsgContainer" id="msgOne">
        </div>
    </div>
    <div class="sysMsgContent" style="display: none">
        <div class="orderMsgContainer" style="margin-top: 50px" id="msgTwo">
        </div>
    </div>
    <input type="hidden" id="authorization_complete">
    <input type="hidden" id="identity_complete">
    <input type="hidden" id="person_info_complete">
</body>
<script src="${ctx}/resources/cash-wechat/js/centerMsg/centerMsg.js${version}"></script>
</html>





