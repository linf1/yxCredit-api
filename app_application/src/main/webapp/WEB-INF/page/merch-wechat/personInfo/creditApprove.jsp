<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>认证中心</title>
    <meta name="viewport" content="width=device-width, initial-scale=1,  user-scalable=no">
    <%@include file ="../../common/input.jsp"%>
    <%@include file ="../../common/taglibs.jsp"%>
    <link rel="stylesheet" href="${ctx}/resources/merch-wechat/css/style.css${version}">
    <link rel="stylesheet" href="${ctx}/resources/merch-wechat/css/personInfo.css${version}">
    <style>
        .must{position: absolute;width: 30px;height: 30px;background: url(${ctx}/resources/merch-wechat/images/must.png) no-repeat center center;background-size: cover;top:0;right: 0}
    </style>
</head>
<body style="background: #F6F6F6">
    <div class="toolBar">
        <p><span class="icon-back" onclick="history.go(-1)"></span><span>认证中心</span></p>
    </div>
    <div class="creditContainer">
       <div class="creditState">
           <p>为确保您的交易安全准确,请完成个<span>人信息认证</span></p>
       </div>
       <div class="creditItems">
           <div class="items">
               <span class="icon-renzheng"></span>
           </div>
           <div class="items">
               <p class="itemsTitle">实名认证</p>
               <p class="itemsContent"><span>身份证信息扫描认证</span><label style="float: right"><span style="color:#3A9BFB;margin-right: 4px;">去认证</span><span class="icon-getMore"></span></label></p>
           </div>
       </div>
       <div class="creditItems noBorderBottom">
            <div class="items">
                <span class="icon-face"></span>
            </div>
            <div class="items">
                <p class="itemsTitle">人脸识别</p>
                <p class="itemsContent"><span>验证本人真实操作</span><label style="float: right"><span style="color:#CDCDCD;margin-right: 4px;">需完成实名认证</span></label></p>
            </div>
        </div>
       <div class="creditItems marginTop">
            <div class="items">
                <span class="icon-lianxiren"></span>
            </div>
            <div class="items">
                <span class="must"></span>
                <p class="itemsTitle">联系人</p>
                <p class="itemsContent"><span>绑定亲属、朋友联系人方式</span><label style="float: right"><span style="color:#3A9BFB;margin-right: 4px;">去认证</span><span class="icon-getMore"></span></label></p>
            </div>
        </div>
    </div>
    <div class="commBtn">
        <button>完&nbsp;&nbsp;成</button>
    </div>
</body>
</html>