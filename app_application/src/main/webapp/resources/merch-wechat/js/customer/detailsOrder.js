$(function () {
    initPageInfo();
    $("#showPackage").on("click",function () {
        $(".severPackage").show();
    })
});
function initPageInfo(){
    var orderId=$("#orderId").val();
    Comm.ajaxPost("/order/getFenqiOrderInfo","orderId="+orderId,function (data) {       var retData=data.retData;
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
            if(commodityState=="11"){
                document.getElementById("applyInfo").style.display = "none";
                document.getElementById("imageInfo").style.display = "none";
                document.getElementById("handSign").style.display = "none";
                document.getElementById("commodityEmp").style.display = "block";
                $("#header_img").attr("src",_ctx+"/resources/merch-wechat/images/tjbdy.png");
            }else if(commodityState=="12"){
                document.getElementById("imageInfo").style.display = "none";
                document.getElementById("handSign").style.display = "none";
                document.getElementById("commodityEmp").style.display = "block";
                $("#header_img").attr("src",_ctx+"/resources/merch-wechat/images/tjbdy.png");
            }else if(commodityState=="13"){
                document.getElementById("handSign").style.display = "none";
                document.getElementById("commodityEmp").style.display = "block";
            }else if(commodityState=="14"){
                document.getElementById("commodityEmp").style.display = "block";
                $("#header_img").attr("src",_ctx+"/resources/merch-wechat/images/tjbdy.png");
            }else if(commodityState=="15"){
                document.getElementById("commodity-content-footer").style.display = "block";
                $("#header_img").attr("src",_ctx+"/resources/merch-wechat/images/ddqrxx.png");
            }else if(commodityState=="16.7"){
                $("#pay").show();
                $("#checkContract").show();
                $("#checkContractFooterBtn").hide();
                $("#header_img").attr("src",_ctx+"/resources/merch-wechat/images/sqtg.png");  //已确认合同
            }
        }else if(state == "5"){
            debugger;
             if(commodityState=='16'){
                 document.getElementById("bindCard").style.display = "block";
             }
             if(commodityState=='16.5'){
                 document.getElementById("checkContractFooterBtn").style.display = "block";
             }
            if (commodityState=='16.7'){
                $("#packageName").html(retData.packageName);
                $("#svcPcgAmount").html("￥"+retData.svcPcgAmount);
                document.getElementById("checkContract").style.display = "block";
                document.getElementById("pay").style.display = "block";
            }
            if(commodityState == '17' || commodityState=='18' ){
                document.getElementById("checkContract").style.display = "block";
                document.getElementById("diss").style.display = "block";
            }
          /*  if (retData.code == "1"){
                $("#packageName").html(retData.packageName);
                $("#svcPcgAmount").html("￥"+retData.svcPcgAmount);
                document.getElementById("pay").style.display = "block";
            }
            if(retData.isBank == "1"){
                $("#bankCard").val(retData.bankCard);
                $("#bankName").val(retData.bankName);
            }*/
            $("#isBank").val(retData.isBank);
            $("#header_img").attr("src",_ctx+"/resources/merch-wechat/images/shtg.png");
        }else if(state == "3" || state == "3.5" || state == "6"){
            document.getElementById("unPass").style.display = "block";
            $("#header_img").attr("src",_ctx+"/resources/merch-wechat/images/shwtg.png");
        }else if(state == "1" || state == "2" || state == "4"){
            document.getElementById("passIng").style.display = "block";
            $("#header_img").attr("src",_ctx+"/resources/merch-wechat/images/shz.png");
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

function checkOrderState() {
    var orderId = $("#orderId").val();
    Comm.ajaxPost("/order/updateOrderState","orderId="+orderId+"&state=1",function (data) {
        location.reload();
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
//确认信息
function confirmInfo() {
    var orderId=$("#orderId").val();
    Comm.ajaxPost("/miaofuOrder/subOrder","orderId="+orderId,function (data) {
        var retCode=data.retCode;
        console.log(retCode);
        if(retCode == "SUCCESS"){
            window.location=_ctx+"/wechat/page/toApplySuccess?orderId="+orderId;
        }else{
            layer.open({content: "提交失败" ,skin: 'msg',time: 2});
        }
    });
}

//立即支付
function confrim() {
        window.location =_ctx+"/contractConfirmation/goToContract";
}



//立即支付
function lijizhifu() {
    var svcPcgAmount = $("#svcPcgAmount").html();
    var packageName = $("#packageName").html();
    var orderId = $("#orderId").val();
    var isBank = $("#isBank").val();
    var bankCard = $("#bankCard").val();
    var bankName = $("#bankName").val();
    if (isBank == '0'){
        $.modal({
            title: '',
            text: '您当前还未绑卡,请联系您的专属业务员进行邦卡',
            buttons: [
                { text: "知道了", onClick: function(){
                    location.reload();
                }}
            ]
        });
    }else{
        window.location =_ctx+"/wechat/page/toPay?svcPcgAmount="+svcPcgAmount+"&packageName="+packageName+"&orderId="+orderId+"&bankCard="+bankCard+"&bankName="+bankName;
    }
}
/**新增查看合同**/
function checkContract(type) {
    if (isBank == '0'){
        $.modal({
            title: '',
            text: '您当前还未绑卡,请联系您的专属业务员进行邦卡',
            buttons: [
                { text: "知道了", onClick: function(){
                    location.reload();
                }}
            ]
        });
    }else{
        var orderId=$("#orderId").val();
        window.location =_ctx+"/wechat/page/confirmContract?isChecked="+type+"&orderId="+orderId;
    }
}