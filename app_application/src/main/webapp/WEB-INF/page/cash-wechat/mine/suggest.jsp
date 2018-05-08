<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <title>投诉建议</title>
    <%@include file ="../../common/input.jsp"%>
    <%@include file ="../../common/taglibs.jsp"%>
    <link rel="stylesheet" href="${ctx}/resources/cash-wechat/css/style.css${version}">
    <link rel="stylesheet" href="${ctx}/resources/cash-wechat/css/myCss.css${version}">
</head>
<body style="background:#f6f6f6">
    <div class="toolBar">
        <p><span class="icon-back" onclick="history.go(-1)"></span><span>投诉建议</span></p>
    </div>
    <div style="margin-top: 50px">
        <div class="weui-cells weui-cells_form">
            <div class="weui-cell">
                <div class="weui-cell__bd">
                    <textarea class="weui-textarea" maxlength="400" placeholder="输入您的反馈意见(字数400以内)" rows="5"></textarea>
                    <div class="weui-textarea-counter"><span id="wordCounter">0</span>/400</div>
                </div>
            </div>
        </div>
    </div>
    <div style="margin: 30px auto;">
        <button class="weui-btn my-btn">提交</button>
    </div>
</body>
<script src="${ctx}/resources/cash-wechat/js/mine/suggest.js${version}"></script>
</html>