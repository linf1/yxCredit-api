<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <title>我的分期申请</title>
    <%@include file ="../../common/input.jsp"%>
    <%@include file ="../../common/taglibs.jsp"%>
    <link rel="stylesheet" href="${ctx}/resources/merch-wechat/css/style.css${version}">
    <link rel="stylesheet" href="${ctx}/resources/merch-wechat/css/mine.css${version}">
</head>
<body style="background:#FAFAFA" id="mineBystages">
    <div class="toolBar">
        <p><span class="icon-back" onclick="history.go(-1)"></span><span id="comQuestionTitle">我的分期申请</span></p>
    </div>
    <div style="margin-top: 45px">
        <div class="btnList">
            <ul>
                <li class="activeBtn"><p>全部</p><p></p></li>
                <li><p>商品分期</p><p></p></li>
                <li><p>现金分期</p><p></p></li>
            </ul>
        </div>
</div>
</body>
<script src="${ctx}/resources/merch-wechat/js/mine/mineBystages.js${version}"></script>
</html>