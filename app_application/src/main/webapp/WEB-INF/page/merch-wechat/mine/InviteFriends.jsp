
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <%@include file ="../../common/input.jsp"%>
    <%@include file ="../../common/taglibs.jsp"%>
    <link rel="stylesheet" href="${ctx}/resources/merch-wechat/css/style.css${version}">
    <link rel="stylesheet" href="${ctx}/resources/merch-wechat/css/customer.css${version}">
    <title>邀请好友</title>
</head>
<body>
    <div class="InviteFriendsContent">
        <img src="${ctx}/resources/merch-wechat/images/topic.jpg" alt="" class="InviteFriendsImg">
    </div>
    <div class="ImgContainer">
        <img src="${ctx}/resources/merch-wechat/images/anniu.png" alt="" class="InviteFriendsBtn">
    </div>
    <div class="rewards" id="rewards">
        <span id="redMoney"></span>
        <img src="${ctx}/resources/merch-wechat/images/next.png" alt="" style="width:6px;padding-left: 10px;">
    </div>
</body>
<script src="${ctx}/resources/merch-wechat/js/mine/invite.js${version}"></script>
</html>
