<%--tab第二个页面 还款首页--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style>
    .repayTime{
        display: inline-block;
        border: 1px solid #FF9D65;
        border-radius: 30px;
        margin: 0 4px;
        padding: 0 6px;
        color:#FF9D65
    }
</style>
<div class="repaymentContainer">
    <div class="repaymentContentTitle">
        <div class="toolBar" style="background: #fff;">
            <p style="position: relative"><span>还款</span><span class="icon-msg" id="repayMsg" onclick="window.location=_ctx+'/wechat/msg/messageCenter'"><i class="msg" style="color:#fff;display: none">0</i></span></p>
        </div>
        <div class="repaymentContentCard">
            <p class="repaymentContentCardOne">
                <label id="checkTips">
                    <span class="icon-question" style="font-size: 24px;vertical-align: middle;"></span>
                    <span>逾期还款</span>
                </label>
            </p>
            <p class="repaymentContentCardTwo">
                <span>应还款</span>
            </p>
            <div class="repaymentContentCardThree">
                <p id="mustRepayMoney">0.00</p>
                <p>最后还款日<span id="repayTime">-月-日</span></p>
                <button onclick="goToRepayMent()">立即还款</button>
            </div>
        </div>
    </div>
    <div class="repaymentContentBody">
        <p style="border-bottom: 1px solid #d4d4d4; height: 30px;line-height: 30px;"><span class="icon-clock" style="padding-left: 15px;font-size: 20px;vertical-align: text-bottom;"></span>待还款账单详情</p>
        <div style="padding: 10px 15px 4px 15px;font-size: 14px">
            <p><span>借款期限</span><span class="right" id="redetailTerm">--</span></p>
            <p><span>借款日期</span><span class="right" id="redetailDate">--</span></p>
            <p><span>到期应还</span><span class="right" id="finalMoney">0.00</span></p>
        </div>
        <div style="padding:0 15px 10px 25px;color:#666;font-size: 12px">
            <p><span>借款本金</span><span class="right" id="borrowMoney">0.00</span></p>
            <p style="display: none"><span>快速信审费</span><span class="right" id="quickMoney">0.00</span></p>
            <p><span>利息</span><span class="right" id="addMoney">0.00</span></p>
            <p><span id="overdue_days">逾期费用(已逾期3天)</span><span class="right" id="overdueMoney">0.00</span></p>
            <p style="display: none"><span>账户管理费</span><span class="right" id="adminMoney">0.00</span></p>
        </div>
        <div style="padding:0 15px 4px 15px;font-size: 14px;display: none">
            <p><span>逾期费用</span><span class="right" id="overFeeMoney">0.00</span></p>
        </div>
        <div style="padding:0 15px 4px 15px;font-size: 14px;display: block" id="deratingMoneyParent">
            <p><span>减免金额</span><span class="repayTime">0:00前还款有效</span><span class="right" id="deratingMoney" style="color:#f00">0.00</span></p>
        </div>
        <div style="padding:0 15px 4px 15px;font-size: 14px;display: block" id="derateAllMoneyParent">
            <p><span>减免后应还金额</span><span class="right" id="derateAllMoney">0.00</span></p>
        </div>
    </div>
    <div class="repaymentContentFooter">
        <p style="border-bottom: 1px solid #d4d4d4; height: 40px;line-height: 40px;"><span class="icon-duihao" style="padding-left: 15px;font-size: 20px;vertical-align: text-bottom;"></span>待还款账单详情</p>
        <div class="repaymentContentFooterInfo">
            <div class="repaymentContentFooterInfoContent">
                <p>-----</p>
                <p>还款日期</p>
            </div>
            <div class="repaymentContentFooterInfoContent">
                <p>0.00</p>
                <p>还款金额</p>
            </div>
            <div class="repaymentContentFooterInfoContent">
                <p>----</p>
                <p>借款期限</p>
            </div>
        </div>
    </div>
</div>