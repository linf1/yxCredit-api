<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<head>
    <title>芝麻授权</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <%@include file ="../../common/input.jsp"%>
    <%@include file ="../../common/taglibs.jsp"%>
    <link rel="stylesheet" href="${ctx}/resources/cash-wechat/css/style.css${version}">
    <link rel="stylesheet" href="${ctx}/resources/cash-wechat/css/myCss.css${version}">
    <style>
        iframe{width: 100%}
    </style>
</head>
<body>
    <div class="toolBar">
        <p><span class="icon-back" onclick="history.go(-1)"></span><span>芝麻授权</span></p>
    </div>
    <div style="margin-top: 50px">
        <iframe src="" frameborder="0"></iframe>
    </div>
    <input type="hidden" id="customer_id">
    <input type="hidden" id="customerName">
    <input type="hidden" id="customerIdentCard">
    <input type="hidden" id="uesrphonenum">
    <input type="hidden" id="appHost">
</body>
<script src="${ctx}/resources/cash-wechat/js/personInfo/zhiMaAccredit.js${version}"></script>
</html>
