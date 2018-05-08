<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="format-detection" content="telephone=no" />
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <%@include file ="../../common/input.jsp"%>
    <%@include file ="../../common/taglibs.jsp"%>
    <title>还款</title>
    <style>
        body{
            margin: 0;
            padding: 0;
            background-color: #f6f6f6;
        }
        .top{
            height: 45px;
            line-height: 45px;
            text-align: center;
            background-color: #fff;
        }
        .back{
            width: 20px;
            float: left;
            position: relative;
            top: 14px;
            left: 15px;
        }
        .Container{
            background-color: #fff;
            margin-top: 10px;
        }
        .ContainerTop{
            display: flex;
            height: 45px;
            line-height: 45px;
            margin: 0 5%;
        }
        .ContainerTopOne{
            flex: 1;
        }
        .ContainerTopTwo{
            flex: .4;
        }
        .ContainerTopThree{
            flex: .4;
            text-align: right;
        }
        .ContainerBottom{
            display: flex;
            margin: 0 5%;
            font-size: 14px;
            padding-bottom: 10px;
            color: #797879;
        }
        .ContainerBottomOne,.ContainerBottomTwo {
            flex: .4;
        }
        .ContainerBottom p{
            margin: 0;
            padding: 0;
        }
        .Btn{
            text-align: center;
            margin: 10%;
            background-color: red;
            color: #fff;
            width: 80%;
            padding: 10px 0;
            border-radius: 50px;
        }
    </style>
</head>
<body>
    <input type="hidden" value="${param.orderId}" id="orderId"/>
    <!--<div class="top">
        <img src="${ctx}/resources/merch-wechat/images/back.png" alt="" class="back">
        <span>还款</span>
    </div>-->
    <div class="Container" id="container">
        <div class="ContainerTop">
            <div class="ContainerTopOne">
                <span>第1期</span>
            </div>
            <div class="ContainerTopTwo">
                <span>已还款</span>
            </div>
            <div class="ContainerTopThree">
                <span>￥1127.00</span>
            </div>
        </div>
        <div class="ContainerBottom">
            <div class="ContainerBottomOne">
                <p>应还款</p>
                <p>到期时间</p>
                <p>逾期费</p>
                <p>逾期利息</p>
            </div>
            <div class="ContainerBottomTwo">
                <p>￥1121.00</p>
                <p>2018.01.04</p>
                <p>￥3.00</p>
                <p>￥3.00</p>
            </div>
        </div>
        <div style="border-bottom: 5px solid #f6f6f6;"></div>
        <div class="ContainerTop">
            <div class="ContainerTopOne">
                <span>第2期</span>
            </div>
            <div class="ContainerTopThree" style="">
                <span>未到期</span>
            </div>
        </div>
        <div class="ContainerBottom">
            <div class="ContainerBottomOne">
                <p>应还款</p>
                <p>到期时间</p>
            </div>
            <div class="ContainerBottomTwo">
                <p>￥1121.00</p>
                <p>2018.01.04</p>
            </div>
        </div>
    </div>
    <%--<div class="Btn" onclick="confrimRepay();">全部结清</div>--%>
</body>
<script src="${ctx}/resources/merch-wechat/js/customer/billingDetails.js${version}"></script>
</html>