<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <title>个人信息</title>
    <%@include file ="../../common/input.jsp"%>
    <%@include file ="../../common/taglibs.jsp"%>
    <link rel="stylesheet" href="${ctx}/resources/cash-wechat/css/style.css${version}">
    <link rel="stylesheet" href="${ctx}/resources/cash-wechat/css/myCss.css${version}">
</head>
<body style="background:#f6f6f6">
<input type="hidden" id="orderId" value="${param.orderId}">
    <div class="toolBar">
        <p><span class="icon-back" onclick="history.go(-1)"></span><span>借款申请</span></p>
    </div>
    <div class="infoTitle">
        <div class="infoContent">
            <p class="infoLine infoLineActive"></p>
            <p class="infoImg infoImgActive"></p>
            <p class="infoWord">身份认证</p>
        </div>
        <div class="infoContent">
            <p class="infoLine"></p>
            <p class="infoImg infoImgActive"></p>
            <p class="infoWord">个人信息</p>
        </div>
        <div class="infoContent">
            <p class="infoLine"></p>
            <p class="infoImg"></p>
            <p class="infoWord">授权</p>
        </div>
    </div>
    <div class="infoContent">
        <div class="weui-cells weui-cells_form">
            <div class="weui-cells">
                <div class="weui-cell weui-cell_select weui-cell_select-after">
                    <div class="weui-cell__hd">
                        <label class="weui-label">婚况</label>
                    </div>
                    <div class="weui-cell__bd">
                        <select class="weui-select" name="marriageSelect">
                            <option value="1">已婚</option>
                            <option value="2">未婚</option>
                        </select>
                    </div>
                </div>
                <div class="weui-cell weui-cell_select weui-cell_select-after">
                    <div class="weui-cell__hd">
                        <label class="weui-label">学历</label>
                    </div>
                    <div class="weui-cell__bd">
                        <select class="weui-select" name="education">
                            <option value="1">硕士及以上</option>
                            <option value="2">本科</option>
                            <option value="3">大专</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="weui-cells">
                <div class="weui-cell">
                    <div class="weui-cell__hd"><label class="weui-label">公司名称</label></div>
                    <div class="weui-cell__bd">
                        <input class="weui-input" type="text" placeholder="输入公司名称" name="companyName" maxlength="50">
                    </div>
                </div>
                <div class="weui-cell">
                    <div class="weui-cell__hd"><label class="weui-label">联系电话</label></div>
                    <div class="weui-cell__bd">
                        <input class="weui-input" type="text" placeholder="输入区号-输入号码" name="companyPhone" maxlength="12">
                    </div>
                </div>
                <div class="weui-cell">
                    <div class="weui-cell__hd"><label class="weui-label">单位地址</label></div>
                    <div class="weui-cell__bd">
                        <input class="weui-input" type="text" readonly placeholder="省份/城市/地区" name="companyAddr">
                    </div>
                </div>
                <div class="weui-cell">
                    <div class="weui-cell__hd"><label class="weui-label">详细地址</label></div>
                    <div class="weui-cell__bd">
                        <input class="weui-input" type="text" placeholder="输入街道门牌号等信息" name="companyPlace" maxlength="50">
                    </div>
                </div>
            </div>
            <div class="weui-cells">
                <div class="weui-cell">
                    <div class="weui-cell__hd"><label class="weui-label">直系亲属</label></div>
                    <div class="weui-cell__bd">
                        <input class="weui-input" type="hidden" placeholder="直系亲属Id" name="linkDirectId" maxlength="10">
                        <input class="weui-input" type="text" placeholder="输入直系亲属姓名" name="immediateName" maxlength="10">
                    </div>
                </div>
                <div class="weui-cell">
                    <div class="weui-cell__hd"><label class="weui-label">联系方式</label></div>
                    <div class="weui-cell__bd">
                        <input class="weui-input" type="text" placeholder="输入直系亲属手机号"  name="immediatePhone" maxlength="11">
                    </div>
                </div>
                <div class="weui-cell weui-cell_select weui-cell_select-after">
                    <div class="weui-cell__hd">
                        <label class="weui-label">关系</label>
                    </div>
                    <div class="weui-cell__bd">
                        <select class="weui-select" name="immediateRel">
                            <option value="1">父亲</option>
                            <option value="2">母亲</option>
                            <option value="3">兄弟姐妹</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="weui-cells">
                <div class="weui-cell">
                    <div class="weui-cell__hd"><label class="weui-label">其他联系人</label></div>
                    <div class="weui-cell__bd">
                        <input class="weui-input" type="hidden" placeholder="其他联系人Id" name="linkOtherId" maxlength="10">
                        <input class="weui-input" type="text" placeholder="输入其他联系人姓名" name="otherName" maxlength="10">
                    </div>
                </div>
                <div class="weui-cell">
                    <div class="weui-cell__hd"><label class="weui-label">联系方式</label></div>
                    <div class="weui-cell__bd">
                        <input class="weui-input" type="text" placeholder="输入其他联系人手机号" name="otherPhone" maxlength="11">
                    </div>
                </div>
                <div class="weui-cell weui-cell_select weui-cell_select-after">
                    <div class="weui-cell__hd">
                        <label class="weui-label">关系</label>
                    </div>
                    <div class="weui-cell__bd">
                        <select class="weui-select" name="otherRel">
                            <option value="1">父亲</option>
                            <option value="2">母亲</option>
                            <option value="3">兄弟姐妹</option>
                        </select>
                    </div>
                </div>
            </div>
        </div>
        <button class="weui-btn my-btn" style="margin: 30px auto;" id="personInfoBtn">下一步</button>
    </div>
    <script src="${ctx}/resources/lib/js/city-picker.js${version}"></script>
    <script src="${ctx}/resources/cash-wechat/js/personInfo/personInfo.js${version}"></script>
</body>
</html>