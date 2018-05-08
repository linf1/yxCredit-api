<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="format-detection" content="telephone=no" />
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <%@include file ="../../common/input.jsp"%>
   <%-- <%@include file ="../../common/taglibs.jsp"%>--%>
    <link rel="stylesheet" href="${ctx}/resources/lib/css/reset.css${version}">
    <link rel="stylesheet" href="${ctx}/resources/lib/css/weui.1.1.2.min.css${version}"/>
    <link rel="stylesheet" href="${ctx}/resources/lib/css/jquery.1.0.0-weui.min.css${version}"/>
    <link rel="stylesheet" href="${ctx}/resources/lib/css/style.css${version}">
    <script src="${ctx}/resources/lib/js/jquery-2.1.4.js${version}"></script>
    <script src="${ctx}/resources/lib/js/fastclick.js${version}"></script>
    <script src="${ctx}/resources/lib/layer_mobile/layer.js${version}"></script>
    <script src="${ctx}/resources/lib/ajaxLoading/spin.js${version}"></script>
    <script src="${ctx}/resources/lib/ajaxLoading/loading.js${version}"></script>
    <script src="${ctx}/resources/lib/js/common.js${version}"></script>
    <link rel="stylesheet" href="${ctx}/resources/merch-wechat/css/customer.css${version}">
    <title>还款</title>
    <style>
        .scan-footer .active .icon-hk::before {
            content: "\e900";
            color: #fe9c51;
        }
        .content-item{height:50px;background:#fff;border-bottom:1px solid #ddd;}
        .content-item span:first-child{float:left;margin-top:12.5px;margin-left:10px;}
        .content-item span:last-child{float:right;margin-top:12.5px;}
        .content-item .icon-circle{font-size: 20px;margin-right:10px;}
        .content-item .icon-orhook{font-size: 25px;margin-right:6px;}
    </style>
</head>
<body>
    <div class="attestation-top" style="background-color:#FFC602;">
        <input type="hidden" id="orderId" value="${param.orderId}"/>
        <!--抵扣券总数-->
        <input type="hidden" id="vouMoney">
        <!--抵扣券id串-->
        <input type="hidden" id="idJson">
    </div>
    <div class="refund">
        <img src="${ctx}/resources/merch-wechat/images/header.png" alt="" class="refund_img">
        <div class="refund-content">
            <div class="refund-header">
                <div class="bill" onclick="tiqianjieqing()">
                    <span>提前结清</span>
                    <img src="${ctx}/resources/merch-wechat/images/next1.png" alt="" class="next1">
                </div>
            </div>
            <div class="commodity-title">
                <img src="${ctx}/resources/merch-wechat/images/logo1.png" alt="" style="width:18px;">
                <span class="title-text" id="merchantName"></span>
            </div>
            <div class="RefundContent">
                <img src="${ctx}/resources/merch-wechat/images/phone.png" alt="" class="Apple_img" id="url" style="height: 66px;">
                <div class="RefundContent-text">
                    <p class="content-text-one" id="merchandiseBrand"></p>
                    <p style="font-size: 14px;"><span id="merchandiseModel">32G</span>&nbsp;&nbsp;<span id="merchandiseVersion"></span></p>
                    <p class="repay_text">
                        <span id="divideMoney"></span>
                        <span style="color:#929292;" id="dividePeriods"></span>
                    </p>
                </div>
            </div>
        </div>
        <img src="${ctx}/resources/merch-wechat/images/bottom.png" alt="" class="bottom_img">
    </div>
    <div class="refundText">
        <span>还款方式</span>
        <span class="refundText_bank" id="bank"></span>
        <img src="${ctx}/resources/merch-wechat/images/next2.png" alt="" class="refundText_img">
    </div>
    <div id="voucherDom" style="display:none;" class="refundText">
        <a style="display:inline-block;width:100%;height:100%;" href="javascript:;" class="open-popup" data-target="#pop">
            <span style="color:#000;">抵扣券</span>
            <span class="refundText_bank" id="voucher"></span>
            <img src="${ctx}/resources/merch-wechat/images/next2.png" alt="" class="refundText_img">
        </a>
    </div>
    <div class="refund_choose" id="nowMoney" style="display: none;">

    </div>

    <div class="refund_choose" id="over" style="display: none;">

    </div>
    <div class="refundText_footer">
        <span class="refundText_footer_one">合计还款</span>
        <span class="refundText_footer_two" id="allMoney"></span>
        <span class="refundText_footer_three" onclick="confirmPay()">确认还款</span>
    </div>

    <div id="pop" class="weui-popup__container popup-bottom">
        <div class="weui-popup__overlay"></div>
        <div class="weui-popup__modal">
            <div style="height:2.2rem;line-height:2.2rem;text-align:center;">
                <span>抵扣券</span>
                <span onclick="lala()" style="float:right;margin-right:10px;color:#ff6600;">确定</span>
            </div>
            <%--<div class="toolbar">
                <div class="toolbar-inner">
                    <a href="javascript:;"  onclick="lala()" class="picker-button close-popup">确定</a>
                    <h1 class="title">抵扣券</h1>
                </div>
            </div>--%>
            <div id="content" class="modal-content" style="height:260px;padding-top:0;">
                <div data-money="12" onclick="_addActive(this)" class="content-item">
                    <span class="text"></span>
                    <span class="icon-circle"></span>
                </div>
                <div data-money="12" onclick="_addActive(this)" class="content-item">
                    <span class="text"></span>
                    <span class="icon-circle"></span>
                </div>
                <div data-money="12" onclick="_addActive(this)" class="content-item">
                    <span class="text"></span>
                    <span class="icon-circle"></span>
                </div>
                <div data-money="12" onclick="_addActive(this)" class="content-item">
                    <span class="text"></span>
                    <span class="icon-circle"></span>
                </div>
                <div data-money="12" onclick="_addActive(this)" class="content-item">
                    <span class="text"></span>
                    <span class="icon-circle"></span>
                </div>
            </div>
        </div>
    </div>

    <script type="text/javascript">
        function changeImg(Img) {
            var a = document.getElementById(Img);
            if(a.getAttribute("src",2)=="${ctx}/resources/merch-wechat/images/check1.png"){
                a.src="${ctx}/resources/merch-wechat/images/check2.png"
            }else{
                a.src="${ctx}/resources/merch-wechat/images/check1.png"
            }
        }
    </script>
</body>
<script src="${ctx}/resources/lib/js/jquery.1.2.0-weui.min.js${version}"></script>
<script src="${ctx}/resources/merch-wechat/js/customer/refundTwo.js${version}"></script>
</html>
