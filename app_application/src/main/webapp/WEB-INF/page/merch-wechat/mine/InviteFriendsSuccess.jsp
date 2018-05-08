
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <%@include file ="../../common/input.jsp"%>
    <%@include file ="../../common/taglibs.jsp"%>
    <link rel="stylesheet" href="${ctx}/resources/merch-wechat/css/style.css${version}">
    <link rel="stylesheet" href="${ctx}/resources/merch-wechat/css/customer.css${version}">
    <title>邀请成功的好友</title>
</head>
<body style="background-color: #fff;">
    <!--<div class="InviteFriendsTitle">
        <span class="icon-back"></span>
        <span>邀请成功的好友</span>
    </div>-->
    <div>
        <img src="${ctx}/resources/merch-wechat/images/top.png" alt="" style="width:100%;">
    </div>
    <div class="SuccessQuan">
        <img src="${ctx}/resources/merch-wechat/images/quan.png" alt="" style="width:75%;">
    </div>
    <div class="InviteFriendsSuccessMoney">
        <span style="font-size: 20px;">￥</span>
        <span style="font-size: 40px;" id="allMoney">0</span>
    </div>
    <div class="surplus">
        <span id="redAvailableAmount">剩余可使用：￥50</span>
    </div>
    <div class="FriendsRecord">
        <span>邀请好友记录</span>
    </div>
    <div class="FriendsRecordContainer">
        <div class="FriendsRecordContainerOne">
        </div>
        <div class="FriendsRecordContainerTwo" style="color: #9E9E9E;">
        </div>
        <div class="FriendsRecordContainerThree">
        </div>
        <div class="FriendsRecordContainerFour">
        </div>
    </div>
    <script src="${ctx}/resources/merch-wechat/js/mine/inviteSuccess.js${version}"></script>
</body>
</html>
