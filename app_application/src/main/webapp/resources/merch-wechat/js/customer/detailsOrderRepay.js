$(function () {
    initPageInfo();
    $("#showPackage").on("click",function () {
        $(".severPackage").show();
    })
});
function initPageInfo(){
    var orderId=$("#orderId").val();
    Comm.ajaxPost("/order/getFenqiOrderInfo","orderId="+orderId,function (data) {
        var retData=data.retData;
        console.log(retData);
        var html="";
        if(retData){
            retData.merchantName==""?$("#productMerchantName").html("暂无"):$("#productMerchantName").html(retData.merchantName);//商户名称
            retData.merchandiseBrandName==""?$("#productMerchandiseBrandName").html("暂无"):$("#productMerchandiseBrandName").html(retData.merchandiseBrandName);//商品名称
            retData.merchandiseModelName==""?$("#productMerchandiseModelName").html("暂无"):$("#productMerchandiseModelName").html(retData.merchandiseModelName);//商品类型
            retData.merchandiseVersionName==""?$("#productMerchandiseVersionName").html("暂无"):$("#productMerchandiseVersionName").html(retData.merchandiseVersionName);//商品版本
            retData.merchandiseUrl==""?$("#productMerchandiseUrl").attr("src",""):$("#productMerchandiseUrl").attr("src",retData.merchandiseUrl);//商品图片
            retData.monthPay==""?$("#productMonthPay").html("暂无"):$("#productMonthPay").html("￥"+retData.monthPay);//每期分期金额
            retData.monthPay==""?$("#productMonthPayBottom").val("￥0.00"):$("#productMonthPayBottom").val("￥"+retData.monthPay);//每期分期金额
            retData.periods==""?$("#Periods").html("暂无"):$("#Periods").html("x"+retData.periods+"期");//分期期数
            retData.periods==""?$("#productPeriods").val("0期"):$("#productPeriods").val(retData.periods+"期");//分期期数
            retData.servicePagNum==""?$("#severBag").val("共有0款服务包"):$("#severBag").val("共有"+retData.servicePagNum+"款服务包");//服务包个数
            retData.allMoney==""?$("#productAllMoney").val("￥0.00"):$("#productAllMoney").val("￥"+retData.allMoney);//商品总价
            retData.downPayMoney==""?$("#productDownPayMoney").val("￥0.00"):$("#productDownPayMoney").val("￥"+retData.downPayMoney);//首付金额
            retData.fenqiMoney==""?$("#productFenqiMoney").val("￥0.00"):$("#productFenqiMoney").val("￥"+retData.fenqiMoney);//申请分期金额
        }
        if(retData.serPackageList){
            var serPackageList=retData.serPackageList;
            for(var i=0;i<serPackageList.length;i++){
                html+='<div class="packageInfo"><p class="packageNameInfo"><span>'+serPackageList[i].packageName+'</span><span class="rightWord">￥'+serPackageList[i].collectionAmount+'</span></p><p class="packageNameInfo"><span>'+serPackageList[i].qsRemark+'</span><span class="rightWord">'+serPackageList[i].sqTypeRemark+'</span></p></div>';
            }
            $(".packageBody").html(html);
        }
        var commodityState = retData.commodityState;
        var state = retData.state;
        if(state == "0"){
            if(commodityState=="12"){
                document.getElementById("applyInfo").style.display = "block";
            }else if(commodityState=="13"){
                document.getElementById("applyInfo").style.display = "block";
                document.getElementById("imageInfo").style.display = "block";
            }else if(commodityState=="14"){
                document.getElementById("applyInfo").style.display = "block";
                document.getElementById("imageInfo").style.display = "block";
                document.getElementById("handSign").style.display = "block";
            }else if(commodityState=="15"){
                document.getElementById("applyInfo").style.display = "block";
                document.getElementById("imageInfo").style.display = "block";
                document.getElementById("handSign").style.display = "block";
                document.getElementById("commodity-content-footer").style.display = "block";
            }
        }else if(state == "5"){
            document.getElementById("applyInfo").style.display = "block";
            document.getElementById("imageInfo").style.display = "block";
            document.getElementById("handSign").style.display = "block";
            if (retData.isBank == '0'){
                document.getElementById("commodity-content-footer").style.display = "block";
            }
        }else{
            document.getElementById("applyInfo").style.display = "block";
            document.getElementById("imageInfo").style.display = "block";
            document.getElementById("handSign").style.display = "block";
            document.getElementById("commodity-content-footer").style.display = "block";
        }
        var alreadyPayList = retData.alreadyPayList;
        if(alreadyPayList){
            html = '<p>还款历史明细</p>'
            for(var i=0;i<alreadyPayList.length;i++){
                var actualTime = alreadyPayList[i].actualTime;
                actualTime = actualTime.substring(0,4)+"-"+actualTime.substring(4,6)+"-"+actualTime.substring(6,8);
                var actualAmount = alreadyPayList[i].actualAmount;
                var moneyTitle = "";
                if (alreadyPayList[i].payCount == "-1")
                {
                    moneyTitle = "提前结清";
                }
                else if (alreadyPayList[i].payCount == "-2")
                {
                    moneyTitle = "非正常结清";
                }
                else
                {
                    moneyTitle = "第" + alreadyPayList[i].payCount + "期";
                }
                html+='<p><span style="text-align: left;display: inline-block;">'+moneyTitle+' 还款</span><span style="text-align: right;display: inline-block; ">&nbsp;&nbsp;&nbsp;￥'+actualAmount+'</span><span style="float: right;">'+ actualTime+'</span></p>';
            }
            $("#already").html(html);
        }
    })
}
function checkInfomationTure() {
    $.modal({
        title: '',
        text: '若申请信息不正确，请联系您的办单员撤销此笔订单，修改后再次确认',
        buttons: [
            { text: "知道了", onClick: function(){ } }
        ]
    });
}
//查看申请信息-H5
function checkApplyInfo() {
    window.location=_ctx+"/wechat/page/toPersonInfoHome";
}
//查看影像信息-原生
function checkImgInfo() {
    var orderId=$("#orderId").val();
    try{
        if(linked.portraitContract&&typeof(linked.portraitContract)=="function"){
            linked.portraitContract(orderId);
        }else{
        }
    }catch(e){
        alert(e);
    }
}
//查看手签签名-原生
function chcekSignInfo() {
    var orderId=$("#orderId").val();
    try{
        if(linked.signContract&&typeof(linked.signContract)=="function"){
            linked.signContract(orderId);
        }else{
        }
    }catch(e){
        alert(e);
    }
}