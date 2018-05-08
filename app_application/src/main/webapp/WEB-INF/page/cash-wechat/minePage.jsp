<%--tab页第三页 我的--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style>
    .minContainer .weui-cell:before{border-top: 1px solid #F0F0F0;}
</style>
<div class="mineTitleContainer">
    <input type="hidden" id="redMoney">
    <input type="hidden" id="repayMoney">
    <%--<span class="user"></span>--%>
    <p class="mineTitleP"><span style="display: inline-block;width: 30px;height: 30px;"><img id="wechat-headimg" style="width: 100%;height:100%;vertical-align: middle;margin-right: 4px;vertical-align: bottom;border-radius: 50%" src="${ctx}/resources/cash-wechat/images/headImgDefault.png" onerror="this.src='${ctx}/resources/cash-wechat/images/headImgDefault.png'"></span><span id="relName"></span><span class="icon-relName relName">实名认证</span><span id="myMsg" class="right icon-msg" onclick="window.location=_ctx+'/wechat/msg/messageCenter'" style="font-size: 30px;position: relative;"><i class="msg" id="msgCount" style="display: none"></i></span></p>
    <p style="padding: 0 15px">可提现(元)</p>
    <p class="withdraw"><span class="withdrawMoney">0.00</span><span class="withdrawBtn" onclick="withdraw()">提现</span></p>
    <p class="lending"><span >待放款 <span id="wechat-forlending">0.00</span></span><span class="lendingBtn" onclick="window.location=_ctx+'/wechat/order/toForLendingView'">待放款订单&gt;</span></p>
</div>
<div class="minContainer">
    <div class="weui-cells">
        <a class="weui-cell weui-cell_access" href="${ctx}/wechat/editPw/toConPlatform">
            <div class="weui-cell__bd">
                <p><span class="icon-platform" style="font-size: 18px;vertical-align: sub"></span>合作平台</p>
            </div>
            <div class="weui-cell__ft">
            </div>
        </a>
    </div>
    <div class="weui-cells" id="inviteFriend">
        <a class="weui-cell weui-cell_access" href="javascript:;">
            <div class="weui-cell__bd" >
                <p><span class="icon-invite" style="font-size: 18px;vertical-align: sub"></span>邀请好友</p>
            </div>
            <div class="weui-cell__ft">
                <span class="hongBao">分享领现金红包</span>
            </div>
        </a>
    </div>
    <div class="weui-cells">
        <a class="weui-cell weui-cell_access" href="${ctx}/wechat/problem/getFaqList">
            <div class="weui-cell__bd">
                <p><span class="icon-comquestion" style="font-size: 24px;vertical-align: sub;margin-right: 4px;"></span>常见问题</p>
            </div>
            <div class="weui-cell__ft">
            </div>
        </a>
        <a class="weui-cell weui-cell_access" href="${ctx}/wechat/feedback/index">
            <div class="weui-cell__bd">
                <p><span class="icon-advice" style="font-size: 18px;vertical-align: sub;"></span>投诉建议</p>
            </div>
            <div class="weui-cell__ft">
            </div>
        </a>
        <a class="weui-cell weui-cell_access" href="${ctx}/wechat/editPw/toSetPage">
            <div class="weui-cell__bd">
                <p><span class="icon-setting" style="font-size: 18px;vertical-align: sub;"></span>设置</p>
            </div>
            <div class="weui-cell__ft">
            </div>
        </a>
        <a class="weui-cell weui-cell_access" href="">
            <div class="weui-cell__bd">
                <p><span class="icon-about" style="font-size: 20px;vertical-align: sub;margin-right: 8px;"></span>关于我们</p>
            </div>
            <div class="weui-cell__ft">
            </div>
        </a>
    </div>
</div>