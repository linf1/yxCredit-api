<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <title>合作平台</title>
    <%@include file ="../../common/input.jsp"%>
    <%@include file ="../../common/taglibs.jsp"%>
    <link rel="stylesheet" href="${ctx}/resources/cash-wechat/css/style.css${version}">
    <style>
        .platformInfoContainer{overflow: hidden;padding: 10px 15px;font-size: 14px;}
        .platformInfoContainer ul{overflow: hidden;}
        .platformInfoContainer ul li{list-style: none;float: left;width: 33%;text-align: center;margin: 10px auto;}
        .platformInfoContainer ul li .imgContainer{padding: 10px;}
        .platformInfoContainer ul li .imgContainer img{width: 100%;height: 100%;}
        #NoPlatformInfo{text-align: center;display: none}
    </style>
</head>
<body style="background:#f6f6f6">
<div class="toolBar">
    <p><span class="icon-back" onclick="history.go(-1)"></span><span id="comQuestionTitle">合作平台</span></p>
</div>
<div style="margin-top: 50px">
    <div class="platformInfoContainer">
        <ul id="platformInfo"></ul>
        <p id="NoPlatformInfo">暂无数据</p>
    </div>
</div>
</body>
<script src="${ctx}/resources/cash-wechat/js/mine/consociationPlatform.js${version}"></script>
</html>