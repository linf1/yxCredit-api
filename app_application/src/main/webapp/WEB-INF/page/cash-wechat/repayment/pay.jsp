<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1,  user-scalable=no">
    <%@include file ="../../common/input.jsp"%>
    <%@include file ="../../common/taglibs.jsp"%>
    <link rel="stylesheet" href="${ctx}/resources/cash-wechat/css/style.css${version}"/>
    <link rel="stylesheet" href="${ctx}/resources/cash-wechat/css/pay.css${version}"/>
    <title>还款</title>
</head>
<body style="background-color:#f6f6f6;">
    <div class="toolBar">
        <p><span class="icon-back" onclick="history.go(-1)"></span><span id="comQuestionTitle">还款</span></p>
    </div>
    <div id="repaymentContainer" style="display: block">
        <div class="content">
            <p class="content-header">金额</p>
            <p class="content-footer" id="finalreFee">￥${param.money}</p>
        </div>
        <div class="content-wrapper">
            <div class="payWay">
                <p class="getRepay" ischecked="true"><span class="icon-bank"><span class="path1"></span><span class="path2"></span><span class="path3"></span><span class="path4"></span><span class="path5"></span><span class="path6"></span><span class="path7"></span></span><span id="bankName"></span><span class="icon-circle payWayIcon"></span></p>
                <input type="hidden" id="repayBankNum">
            </div>
        </div>
        <div class="footerBtn">
            <button>立即还款</button>
        </div>
    </div>
    <div id="repaymentSuccess" style="margin-top: 50px;display: none">
        <div id="middle-content">
            <div class="content-logo">
                <img src="${ctx}/resources/cash-wechat/images/dggreen.jpg" alt="" class="middle-logo"/>
                <p class="content-footer">￥1123.00</p>
            </div>
            <div class="container">
                <p class="container1">还款成功,借款已还请</p>
                <p class="container2">您的信用评分上升,继续借款,更高额度</p>
            </div>
        </div>
        <div class="public-footer">
            <button>去借款</button>
        </div>
    </div>
    <div id="repaymentError" style="margin-top: 50px;display: none">
        <div class="content-logo">
            <img src="${ctx}/resources/cash-wechat/images/hk.png" alt="" class="middle-logo"/>
        </div>
        <div class="container">
            <p class="container1">抱歉,无法完成付款</p>
            <p class="container2">可能是网络系统异常，请稍后再付款</p>
        </div>
        <div class="public-footer">
            <button>重新还款</button>
        </div>
    </div>
    <script src="${ctx}/resources/cash-wechat/js/repayment/pay.js${version}"></script>
</body>
</html>