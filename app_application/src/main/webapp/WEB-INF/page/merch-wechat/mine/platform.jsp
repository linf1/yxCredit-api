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
        iframe{width: 100%}
    </style>
</head>
<body style="background:#f6f6f6">
<div class="toolBar">
    <p><span class="icon-back" onclick="history.go(-1)"></span><span id="comQuestionTitle">${param.name}</span></p>
</div>
<div style="margin-top: 50px">
    <iframe src="${param.url}" frameborder="0"></iframe>
</div>
<script>
    $("iframe").css("height",screen.height)
</script>
</body>
</html>