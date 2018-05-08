<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html manifest="" lang="en-US">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="textml; charset=utf-8">
    <%@include file ="../WEB-INF/page/common/input.jsp"%>
    <%@include file ="../WEB-INF/page/common/taglibs.jsp"%>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,Chrome=1">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <title>秒付APP下载</title>
    <link rel="stylesheet" href="${ctx}/resources/lib/css/reset.css${version}">
    <link rel="stylesheet" href="${ctx}/resources/download/css/myCss.css${version}">
</head>
<body>
    <div class="logoContainer">
        <h1>秒付</h1>
        <img src="${ctx}/resources/download/images/logo-word.png${version}">
    </div>
    <div class="bodyContainer">
        <img src="${ctx}/resources/download/images/download.png${version}">
    </div>
    <div class="btnContainer">
        <p>下载秒付APP,领取您的借款额度</p>
        <button onclick="downloadApp()">立即下载</button>
    </div>
</body>
<script>
    var androidUrl;
    var iosUrl;
    $(function () {
        Comm.ajaxPost("/url","",function (data) {
            var retData=data.retData;
            androidUrl=retData.androidUrl;
            iosUrl=retData.iosUrl;
        })
    })
    function downloadApp() {
        var ua = navigator.userAgent.toLowerCase();
        if(/iphone|ipad|ipod/.test(ua)){//ios
            if (ua.match(/MicroMessenger/i) == "micromessenger") {
                layer.open({content: '请点击微信右上角按钮，然后在弹出的菜单中，点击在Safar中打开，即可下载APP',btn: '确定'});
            } else {
                window.location = iosUrl;
            }
        }else{
            if (ua.match(/MicroMessenger/i) == "micromessenger") {
                layer.open({content: '请点击微信右上角按钮，然后在弹出的菜单中，点击在浏览器中打开，即可下载APP',btn: '确定'});
            } else {
                window.location = androidUrl;
            }
        }
    }
</script>
</html>
