<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="stylesheet" href="${ctx}/resources/merch-wechat/css/style.css${version}">
<link rel="stylesheet" href="${ctx}/resources/merch-wechat/css/customer.css${version}">
<style>
        .imgs{
            float: right;
            position: relative;
            top: 12px;
            left: 10px;
        }
        .content-one img{
            width:18px;
            position: relative;
            top: 5px;
            padding-right: 6px;
        }
</style>
<div class="minePage" style="background: #FAFAFA;">
    <div class="attestation-top" style="background-color:#FDB856; ">
    <span class="attestation-text" style="color: #fff;font-size: 17px;">我的</span>
</div>
    <div class="mine-header">
        <img src="${ctx}/resources/merch-wechat/images/demo11.png" alt="" class="img_one">
        <span>张衡</span>
        <img src="${ctx}/resources/merch-wechat/images/next.png" alt="" class="img_two">
    </div>
    <div class="mine-content">
        <ul class="mine-left">
            <li>
                <img src="${ctx}/resources/merch-wechat/images/mine8.png" alt="" class="imgs_one">
                <span>我的分期申请</span>
            </li>
            <li>
                <img src="${ctx}/resources/merch-wechat/images/mine9.png" alt="" class="imgs_two">
                <span>我的还款</span>
            </li>
        </ul>
        <div class="mine-middle">
            <span>1</span>
            <span class="string"></span>
            <span>0</span>
        </div>
        <ul class="mine-bottom">
            <li>申请中订单(笔)</li>
            <li>未结清订单(笔)</li>
        </ul>
    </div>
    <div class="commodity-content-one" style="clear: both;">
        <div class="content-one">
            <img src="${ctx}/resources/merch-wechat/images/mine1.png" alt="">
            <span>可提现金额</span>
            <label class="rightWord"><span>2000</span><span class="icon-getMore"></span></label>
        </div>
        <div class="content-one" style="border:transparent;">
            <img src="${ctx}/resources/merch-wechat/images/mine2.png" alt="">
            <span>邀请好友</span>
            <label class="rightWord"><span>分享领现金红包</span><span class="icon-getMore"></span></label>
            <img src="${ctx}/resources/merch-wechat/images/mine7.png" alt="" class="imgs" style="top:12px;margin-right: 10px;">
        </div>
    </div>
    <div class="commodity-content-one">
        <div class="content-one" onclick="window.location='${ctx}/wechat/problem/getFaqList'">
            <img src="${ctx}/resources/merch-wechat/images/mine3.png" alt="">
            <span>常见问题</span>
            <label class="rightWord"><span class="icon-getMore"></span></label>
        </div>
        <div class="content-one">
            <img src="${ctx}/resources/merch-wechat/images/mine4.png" alt="">
            <span>投诉建议</span>
            <label class="rightWord"><span class="icon-getMore"></span></label>
        </div>
        <div class="content-one">
            <img src="${ctx}/resources/merch-wechat/images/mine5.png" alt="">
            <span>合作平台</span>
            <label class="rightWord"><span class="icon-getMore"></span></label>
        </div>
        <div class="content-one" style="border:transparent;" onclick="checkRegister('10','关于我们')">
            <img src="${ctx}/resources/merch-wechat/images/mine6.png" alt="">
            <span>关于我们</span>
            <label class="rightWord"><span class="icon-getMore"></span></label>
        </div>
    </div>
</div>
<script>
    function checkRegister(code,name) {
        window.location=_ctx+"/wechat/register/goToCheckRegister?code="+code+"&&name="+name;
    }
</script>

