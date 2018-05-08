<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>现金贷详情</title>
    <meta name="viewport" content="width=device-width, initial-scale=1,  user-scalable=no">
    <%@include file ="../../common/input.jsp"%>
    <%@include file ="../../common/taglibs.jsp"%>
    <link rel="stylesheet" href="${ctx}/resources/merch-wechat/css/style.css${version}">
    <link rel="stylesheet" href="${ctx}/resources/merch-wechat/css/cashLoan.css${version}">
</head>
<body>
    <div class="headerBar">
        <span class="icon-back"></span><span>现金分期</span><span class=""></span>
    </div>
    <div class="cashBg">
        <p class="cashMoneyCode">2,000.00</p>
        <p class="cashMoneyWord">申请金额</p>
    </div>
    <div class="cashForm">
        <div class="weui-cells">
            <div class="weui-cell weui-cell_select weui-cell_select-after">
                <div class="weui-cell__hd">
                    <label for="" class="weui-label">分期期数</label>
                </div>
                <div class="weui-cell__bd">
                    <select class="weui-select" name="">
                        <option value="">请选择</option>
                        <option value="1">6</option>
                        <option value="2">12</option>
                        <option value="3">24</option>
                    </select>
                </div>
            </div>
        </div>
        <div class="weui-cell">
            <div class="weui-cell__hd"><label class="weui-label">每期还款金额</label></div>
            <div class="weui-cell__bd">
                <input class="weui-input" type="text"  placeholder="请输入每期还款金额">
            </div>
        </div>
    </div>
    <div class="commBtn">
        <button>确认申请</button>
    </div>
</body>
</html>