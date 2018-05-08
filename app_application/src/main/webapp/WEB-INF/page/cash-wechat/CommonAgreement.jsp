<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>协议</title>
    <meta name="viewport" content="width=device-width, initial-scale=1,  user-scalable=no">
    <%@include file ="../common/input.jsp"%>
    <%@include file ="../common/taglibs.jsp"%>
    <link rel="stylesheet" href="${ctx}/resources/cash-wechat/css/style.css${version}"/>
</head>
<body>
<input type="hidden" id="code" value="${param.code}">
    <div class="toolBar">
        <p><span class="icon-back" onclick="history.go(-1)"></span><span>协议</span></p>
    </div>
    <div id="showMsg" style="margin-top:50px"></div>
</body>
<script>
    $(function () {
        var code=$("#code").val();
        Comm.ajaxPost("/wechat/register/getRegClause","type="+code,function (data) {
            if(data.retData){ $("#showMsg").html(data.retData.content)}
        })
    })
</script>
</html>
