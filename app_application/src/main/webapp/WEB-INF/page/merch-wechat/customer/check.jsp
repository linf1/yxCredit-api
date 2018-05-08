<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <%@include file ="../../common/input.jsp"%>
    <%@include file ="../../common/taglibs.jsp"%>
    <link rel="stylesheet" href="${ctx}/resources/merch-wechat/css/customer.css${version}">
    <link rel="stylesheet" href="${ctx}/resources/merch-wechat/css/style.css${version}">
    <title>影像资料</title>
</head>
<body style="background: #fafafa">
    <div class="toolBar">
        <p><span class="icon-back" onclick="history.go(-1)"></span><span>影像资料</span></p>
    </div>

    <div class="box" style="margin-top: 44px;">
        <p style="padding: 0 0 5px 6px;color: #3F3F3F;">客户合影</p>
        <div class="check">
        </div>
        <div class="check">
        </div>
        <div class="check">
        </div>
        <div class="check">
        </div>
    </div>
    <div class="box" style="margin-top: 20px;">
        <p style="padding: 0 0 5px 6px;color: #3F3F3F;">客户手持身份证照片</p>
        <div class="check">
        </div>
        <div class="check">
        </div>
        <div class="check">
        </div>
        <div class="check">
        </div>
    </div>
</body>
</html>