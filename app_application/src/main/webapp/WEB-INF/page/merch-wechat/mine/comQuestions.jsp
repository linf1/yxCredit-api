<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <title>常见问题</title>
    <%@include file ="../../common/input.jsp"%>
    <%@include file ="../../common/taglibs.jsp"%>
    <link rel="stylesheet" href="${ctx}/resources/merch-wechat/css/style.css${version}">
    <link rel="stylesheet" href="${ctx}/resources/merch-wechat/css/mine.css${version}">
    <style>
        .weui-cell_access .weui-cell__ft:after{transform: rotate(135deg);}
        .moreIcon{display: inline-block;width: 20px; height: 20px;background: #F8931F;border-radius: 50%;vertical-align: bottom;margin-right: 4px;text-align: center;color: #fff;}
    </style>
</head>
<body style="background:#f6f6f6">
<div class="toolBar">
    <p><span class="icon-back"></span><span id="comQuestionTitle">常见问题</span></p>
</div>
<div style="margin-top: 0px" id="comQuestion">
    <c:forEach items="${data}" var="item" >
        <div class="weui-cells" style="font-size: 16px;margin-top: 2px">
            <p class="questions"><span class="moreIcon">&quest;</span><span ><c:out value="${item.key}"/></span><span class="right getMore" style="float: right;">更多</span></p>
            <c:forEach items="${item.value}" var="item" begin="0" varStatus="itemStatss" end="2">
                <a class="weui-cell weui-cell_access" data-isShow="false"  href="javascript:;">
                    <div class="weui-cell__bd">
                        <p><c:out value="${item.problem_name}"/></p>
                    </div>
                    <div class="weui-cell__ft">
                    </div>
                </a>
                <div class="questionsDetail">
                    <c:out value="${item.problem_content}" />
                </div>
            </c:forEach>
        </div>
    </c:forEach>
</div>
<c:forEach items="${data}" var="item" >
    <div style="margin-top: 0px;display: none" id="comQuestionDetail_${item.key}">
        <div class="weui-cells" style="margin-top: 0px;font-size: 16px;">
            <p class="questions"><span class="moreIcon">&quest;</span><span><c:out value="${item.key}"/></span></p>
            <c:forEach items="${item.value}" var="item" begin="0" varStatus="itemStatss" end="4">
                <a class="weui-cell weui-cell_access" data-isShow="false"  href="javascript:;">
                    <div class="weui-cell__bd">
                        <p><c:out value="${item.problem_name}"/></p>
                    </div>
                    <div class="weui-cell__ft">
                    </div>
                </a>
                <div class="questionsDetail">
                    <c:out value="${item.problem_content}" />
                </div>
            </c:forEach>
        </div>
    </div>
</c:forEach>
</body>
<script src="${ctx}/resources/cash-wechat/js/mine/comQuestions.js${version}"></script>
</html>