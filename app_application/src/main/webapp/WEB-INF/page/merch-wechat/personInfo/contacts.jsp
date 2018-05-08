<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>联系人</title>
    <meta name="viewport" content="width=device-width, initial-scale=1,  user-scalable=no">
    <%@include file ="../../common/input.jsp"%>
    <%@include file ="../../common/taglibs.jsp"%>
    <link rel="stylesheet" href="${ctx}/resources/merch-wechat/css/style.css${version}">
    <link rel="stylesheet" href="${ctx}/resources/merch-wechat/css/personInfo.css${version}">
</head>
<body style="background: #F6F6F6" id="contactsPage">
    <div class="toolBar">
        <p><span class="icon-back" onclick="history.go(-1)"></span><span>联系人</span></p>
    </div>
    <div class="contactsContainer">
        <div class="contactsItems">
            <div class="weui-cells weui-cells_form">
                <div class="weui-cells">
                    <div class="weui-cell">
                        <div class="weui-cell__hd"><label class="weui-label">直系亲属</label></div>
                        <div class="weui-cell__bd">
                            <input class="weui-input" type="text"  placeholder="输入直系亲属姓名" name="directName1" maxlength="10">
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd"><label class="weui-label">联系方式</label></div>
                        <div class="weui-cell__bd">
                            <input class="weui-input" type="text"  placeholder="输入联系方式" name="directPhone1" maxlength="11">
                        </div>
                    </div>
                    <div class="weui-cell weui-cell_select weui-cell_select-after">
                        <div class="weui-cell__hd">
                            <label class="weui-label">关系</label>
                        </div>
                        <div class="weui-cell__bd">
                            <select class="weui-select" name="directRel">
                                <option value="1">父亲</option>
                                <option value="2">母亲</option>
                                <option value="3">兄弟姐妹</option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="contactsItems">
            <div class="weui-cells weui-cells_form">
                <div class="weui-cells">
                    <div class="weui-cell">
                        <div class="weui-cell__hd"><label class="weui-label">直系亲属</label></div>
                        <div class="weui-cell__bd">
                            <input class="weui-input" type="text"  placeholder="输入直系亲属姓名" name="directName2" maxlength="10">
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd"><label class="weui-label">联系方式</label></div>
                        <div class="weui-cell__bd">
                            <input class="weui-input" type="text"  placeholder="输入联系方式" name="directPhone2" maxlength="11">
                        </div>
                    </div>
                    <div class="weui-cell weui-cell_select weui-cell_select-after">
                        <div class="weui-cell__hd">
                            <label class="weui-label">关系</label>
                        </div>
                        <div class="weui-cell__bd">
                            <select class="weui-select" name="directRe2">
                                <option value="1">父亲</option>
                                <option value="2">母亲</option>
                                <option value="3">兄弟姐妹</option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="contactsItems">
            <div class="weui-cells weui-cells_form">
                <div class="weui-cells">
                    <div class="weui-cell">
                        <div class="weui-cell__hd"><label class="weui-label">其他联系人</label></div>
                        <div class="weui-cell__bd">
                            <input class="weui-input" type="text"  placeholder="输入其他联系人姓名" name="otherName1" maxlength="10">
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd"><label class="weui-label">联系方式</label></div>
                        <div class="weui-cell__bd">
                            <input class="weui-input" type="text"  placeholder="输入联系方式" name="otherPhone1" maxlength="11">
                        </div>
                    </div>
                    <div class="weui-cell weui-cell_select weui-cell_select-after">
                        <div class="weui-cell__hd">
                            <label class="weui-label">关系</label>
                        </div>
                        <div class="weui-cell__bd">
                            <select class="weui-select" name="otherRe1">
                                <option value="1">同学</option>
                                <option value="2">朋友</option>
                                <option value="3">其他</option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="contactsItems">
            <div class="weui-cells weui-cells_form">
                <div class="weui-cells">
                    <div class="weui-cell">
                        <div class="weui-cell__hd"><label class="weui-label">其他联系人</label></div>
                        <div class="weui-cell__bd">
                            <input class="weui-input" type="text"  placeholder="输入其他联系人姓名" name="otherName2" maxlength="10">
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd"><label class="weui-label">联系方式</label></div>
                        <div class="weui-cell__bd">
                            <input class="weui-input" type="text"  placeholder="输入联系方式" name="otherPhone2" maxlength="11">
                        </div>
                    </div>
                    <div class="weui-cell weui-cell_select weui-cell_select-after">
                        <div class="weui-cell__hd">
                            <label class="weui-label">关系</label>
                        </div>
                        <div class="weui-cell__bd">
                            <select class="weui-select" name="otherRe2">
                                <option value="1">同学</option>
                                <option value="2">朋友</option>
                                <option value="3">其他</option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="commBtn">
        <button class="" style="margin: 30px auto;">完成</button>
    </div>
</body>
</html>