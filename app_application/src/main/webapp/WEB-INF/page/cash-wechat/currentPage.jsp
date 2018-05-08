<%--tab页第一页 首页--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="stylesheet" href="${ctx}/resources/lib/css/swiper.min.css">
<style>
    .btnDisabled{  background: #f5f5f5!important;  border:none!important;  color:#ddd!important  }
    #homeContentFooter .icon-orhook:before,.icon-grayhook:before{position: relative;top:3px}
</style>
<div id="swiperContainer">
    <div class="msgContainer" onclick="window.location=_ctx+'/wechat/msg/messageCenter'"><span class="icon-msg"><span id="num" style="display: none"></span></span></div>
    <!-- Swiper -->
    <div class="swiper-container">
            <div class="swiper-wrapper">
                <div class="swiper-slide">
                    <img src="${ctx}/resources/cash-wechat/images/banner_01.jpg" alt="">
                </div>
                <div class="swiper-slide">
                    <img src="${ctx}/resources/cash-wechat/images/banner_01.jpg" alt="">
                </div>
            </div>
            <!-- Add Pagination -->
            <div class="swiper-pagination"></div>
    </div>
</div>
<div id="homeContainer">
    <input type="hidden" value="" id="money">
    <div id="homeContentTitle">
        <p><span class="icon-money"></span><span class="fontWeight">现金借款</span><span class="right grayWord">仅需芝麻信用分,额度<span id="CreditPreAmount"></span>元</span></p>
    </div>
    <div id="homeContentProduct">
        <div class="productContent">
                <p class="textRight">产品详情&gt;</p>
                <p class="productMoneyTitle">选择借款金额</p>
                <ul class="btnList" id="productList">
                    <li class="active">--</li>
                    <li>--</li>
                    <li>--</li>
                </ul>
            </div>
        <div class="productBtnList">
                <p>选择借款期限</p>
                <ul class="btnList" id="periodsList">
                    <li class="active">--</li>
                    <li>--</li>
                    <li>--</li>
                </ul>
            </div>
    </div>
    <div id="homeContentDetail">
        <div class="detailContainer">
                <div class="detailContent">
                    <p><span>到期应还</span><span class="right" id="expireMoney">0.00</span></p>
                    <p><span>到期时间</span><span class="right" id="expireTime"></span></p>
                </div>
                <div class="detailContent">
                    <p><span>快速信审费</span><span class="right" id="quickMoney">0.00</span></p>
                    <p><span>账户管理费</span><span class="right" id="mangerMoney">0.00</span></p>
                    <p><span>利息</span><span class="right" id="interest">0.00</span></p>
                </div>
            </div>
    </div>
    <div id="homeContentFooter">
        <p style="margin-bottom: 6px;"><span style="font-size: 20px;" onclick="changeBtn(this)" isAgree="true" class="icon-orhook"><span style="font-size: 12px;color:#7a7a7a">已阅读并同意</span></span><span style="color:#333" onclick="checkRegister('0')">《借款协议》</span>和<span style="color:#333" onclick="checkRegister('4')">《服务协议》</span></p>
        <button class="weui-btn my-btn" id="currentBtn">立即申请</button>
        <p><span style="font-size: 14px;vertical-align: text-bottom;" class="icon-stop"></span>学生禁止申请贷款</p>
    </div>
</div>
