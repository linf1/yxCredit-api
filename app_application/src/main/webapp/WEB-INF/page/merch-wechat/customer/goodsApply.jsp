<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta name="format-detection" content="telephone=no" />
    <%@include file ="../../common/input.jsp"%>
    <%--<%@include file ="../../common/taglibs.jsp"%>--%>
    <link rel="stylesheet" href="${ctx}/resources/lib/css/reset.css${version}">
    <link rel="stylesheet" href="${ctx}/resources/lib/css/weui.1.1.2.min.css${version}"/>
    <link rel="stylesheet" href="${ctx}/resources/lib/css/jquery.1.0.0-weui.min.css${version}"/>
    <script src="${ctx}/resources/lib/js/jquery-2.1.4.js${version}"></script>
    <script src="${ctx}/resources/lib/js/fastclick.js${version}"></script>
    <script src="${ctx}/resources/lib/layer_mobile/layer.js${version}"></script>
    <script src="${ctx}/resources/lib/ajaxLoading/spin.js${version}"></script>
    <script src="${ctx}/resources/lib/ajaxLoading/loading.js${version}"></script>
    <script src="${ctx}/resources/lib/js/common.js${version}"></script>
    <link rel="stylesheet" href="${ctx}/resources/merch-wechat/css/customer.css${version}">
    <link rel="stylesheet" href="${ctx}/resources/merch-wechat/css/style.css${version}">
    <title>我的分期申请</title>
    <style>
        .text_three{
            position: relative;
            top: 23px;
        }
        .content-text-two{
            position: relative;
            top: 9px;
        }
        .content-text-two {
            position: relative;
            top: 13px;
        }
        .weui-pull-to-refresh__layer{
            margin-top: -44px;
        }
    </style>
</head>
<body style="background: #fafafa">
    <div class="toolBar">
        <p><span class="icon-back" style="line-height: 12px;" onclick="history.go(-1)"></span><span>我的分期申请1</span></p>
    </div>
    <div class="apply_tab" id="commodity" style="margin-top: 44px;">
        <div class="weui-pull-to-refresh__layer">
            <div class='weui-pull-to-refresh__arrow'></div>
            <div class='weui-pull-to-refresh__preloader'></div>
            <div class="down">下拉刷新</div>
            <div class="up">释放刷新</div>
            <div class="refresh">正在刷新</div>
        </div>
        <div id="commodity-content">
        </div>
    </div>
    <script>
        $(function(){
            $(".js_nav li").on("click",function(){
                $(".js_nav li").removeClass("active");
                $(this).addClass("active");
                var $id=$(this).attr("data-id");
                $(".apply_tab").hide();
                $("#"+$id).show();
            });
        });
    </script>
    <script src="${ctx}/resources/lib/js/jquery.1.2.0-weui.min.js${version}"></script>
    <script src="${ctx}/resources/merch-wechat/js/customer/goodsApply.js${version}"></script>
</body>
</html>