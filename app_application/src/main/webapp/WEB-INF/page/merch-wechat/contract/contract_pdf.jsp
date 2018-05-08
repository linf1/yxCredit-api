<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <title>合同</title>
    <%@include file ="../../common/input.jsp"%>
    <%@include file ="../../common/taglibs.jsp"%>
    <link rel="stylesheet" href="${ctx}/resources/cash-wechat/css/style.css${version}">
    <link rel="stylesheet" href="${ctx}/resources/cash-wechat/css/myCss.css${version}">
    <script src="${ctx}/resources/merch-wechat/js/contract/contract.js${version}"></script>
    <style>
        #confirmContract{
            position: absolute;
            right: 15px;
        }
        .confirmBtnContainer{
            position: absolute;
            z-index: 10;
            bottom:0;
            width: 100%;
            text-align: center;
            font-size: 14px;
            margin-bottom: 10px;
        }
        .buttonContainer button{
            width: 80%;
            height: 40px;
            line-height: 40px;
            border: none;
            background: #FFDA44;
            border-radius: 30px;
            outline: none;
        }
    </style>
</head>
<body  style="background:#f6f6f6">
    <input type="hidden" id="orderId" value="${param.orderId}">
    <input type="hidden" id="isChecked" value="${param.isChecked}">
    <input type="hidden" id="pdfUrl" value="">
    <%--<div class="toolBar">--%>
        <%--<p><span class="icon-back" onclick="window.location=_ctx+'/wechat/page/toDetailsOrder?orderId='+$('#orderId').val()"></span><span>合同详情</span><span id="confirmContract" style="display: none">${param.word}</span></p>--%>
    <%--</div>--%>
    <div class="confirmBtnContainer">
        <label for="isCheck">
            <input type="checkbox" id="isCheck" checked>已阅读并同意合同
        </label>
        <p class="buttonContainer">
            <button onclick="confirmContract()">确认合同</button>
        </p>
    </div>
    <div class="scroll-wrapper" style="-webkit-overflow-scrolling: touch;overflow-y: scroll;position: fixed;top: 10px;bottom: 0px;left: 0px;right: 0px;">
        <iframe id="exterlinkframe" src="" noresize="noresize" frameborder="0" height="85%" width="100%"/>
    </div>
</body>
</html>