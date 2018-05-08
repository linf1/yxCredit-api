<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <title>借款申请成功</title>
    <%@include file ="../../common/input.jsp"%>
    <%@include file ="../../common/taglibs.jsp"%>
    <style>
        .titleImgContainer{text-align: center;margin-top: 44px;padding: 20px;border-bottom: 1px dashed #D3D3D3;}
        .titleImgContainer img{width: 35%}
        .titleImgContainer p{font-size: 16px}
        .orderApplyMsgContainer{margin: 20px 15px;font-size: 14px;}
        .orderApplyMsgContainer p:first-child{color:#7C7C7C;font-weight: bold;margin: 4px auto;}
        .btnContainer{text-align: center}
        .bingCard{font-weight: bold;width: 80%;height: 40px;line-height: 40px;border: none;outline: none;margin: 10px auto;border-radius: 30px;background: #FFDA44}
        .checkOrder{font-weight: bold;width: 80%;height: 40px;line-height: 40px;border: 1px solid #FEE888; background: transparent;outline: none;margin: 10px auto;border-radius: 30px;}
    </style>
</head>
<body style="background:#fff">
<input type="hidden" value="${param.orderId}">
    <div class="toolBar">
        <p><span>借款申请成功</span></p>
    </div>
    <div class="titleImgContainer">
        <img src="${ctx}/resources/cash-wechat/images/moneyc.png" alt="">
        <p>借款申请提交成功！</p>
    </div>
    <div class="orderApplyMsgContainer">
        <p>等候审核结果的过程中,您可以：</p>
        <p>绑定银行卡开通账户(放款成功后用于提现),审核通过,即可直接提现。</p>
    </div>
    <div class="btnContainer">
        <p><button class="checkOrder">查看待放款订单</button></p>
        <p><button class="bingCard backHome">返回首页</button></p>
    </div>
</body>
<script>
    $(".backHome").on("click",function () {
        window.location=_ctx+"/wechat/login/home?orderId=${param.orderId}"
    })
    $(".checkOrder").on("click",function () {
        window.location=_ctx+"/wechat/order/toForLendingView?orderId=${param.orderId}"
    })
</script>
</html>