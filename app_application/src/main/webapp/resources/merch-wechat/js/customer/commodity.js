$(function () {
    initPageInfo();
    $("#showPackage").on("click",function () {
        $(".severPackage").show();
    })

    if ($("#orderTypeValue").val() == "1")
    {
        $("#orderType").val("线下订单");
    }
    else
    {
        $("#orderType").val("线上订单");
    }
})
function initPageInfo(){
    var empId=$("#empId").val();
    var allMoney=$("#allMoney").val();
    var downPayMoney=$("#downPayMoney").val();
    var merchantId=$("#merchantId").val();
    var productId=$("#productId").val();
    var idJson=$("#idJson").val();
    var merchandiseId=$("#merchandiseId").val();
    var createTime = $("#createTime").val();
    Comm.ajaxPostOrder("/order/getMerdiseInfoStage","empId="+empId+"&allMoney="+allMoney+"&downPayMoney="+downPayMoney+"&merchantId="+merchantId+"&productId="+productId+"&idJson="+idJson+"&merchandiseId="+merchandiseId+"&createTime="+createTime,function (data) {
        var flag = data.retCode;
        if(flag=="SUCCESS"){
            var retData=data.retData;
            var html="";
            if(retData){
                retData.merchantName==""?$("#productMerchantName").html("暂无"):$("#productMerchantName").html(retData.merchantName);//商户名称
                retData.merchandiseBrandName==""?$("#productMerchandiseBrandName").html("暂无"):$("#productMerchandiseBrandName").html(retData.merchandiseBrandName);//商品名称
                retData.merchandiseModelName==""?$("#productMerchandiseModelName").html("暂无"):$("#productMerchandiseModelName").html(retData.merchandiseModelName);//商品类型
                retData.merchandiseVersionName==""?$("#productMerchandiseVersionName").html("暂无"):$("#productMerchandiseVersionName").html(retData.merchandiseVersionName);//商品版本
                retData.merchandiseUrl==""?$("#productMerchandiseUrl").attr("src",""):$("#productMerchandiseUrl").attr("src",retData.merchandiseUrl);//商品图片
                retData.monthPay==""?$("#productMonthPay").html("暂无"):$("#productMonthPay").html("￥"+retData.monthPay);//每期分期金额
                retData.monthPay==""?$("#productMonthPayBottom").val("￥0.00"):$("#productMonthPayBottom").val("￥"+retData.monthPay);//每期分期金额
                retData.periods==""?$("#Periods").html("暂无"):$("#Periods").html("X"+retData.periods+"期");//分期期数
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
        }else{
            $.modal({
                title: '',
                text: "当前二维码已失效,请重新生成",
                buttons: [
                    { text: "知道了", onClick: function(){
                        try{
                            if(linked.backHomeAction&&typeof(linked.backHomeAction)=="function"){
                                linked.backHomeAction();
                            }else{
                            }
                        }catch(e){
                            alert(e);
                        }
                    } }
                ]
            });
        }
    })
}
$(".commBtn button").on("click",function (data) {
    var data={
        empId:$("#empId").val(),
        allMoney:$("#allMoney").val(),
        downPayMoney:$("#downPayMoney").val(),
        merchantId:$("#merchantId").val(),
        productId:$("#productId").val(),
        idJson:$("#idJson").val(),
        merchandiseId:$("#merchandiseId").val(),
        orderTypeValue:$("#orderTypeValue").val()
    };
    Comm.ajaxPost("/order/addOrder","data="+JSON.stringify(data),function (data) {
        var retData=data.retData;
        if(retData){
            var retCode = data.retCode;
            if(retCode == "SUCCESS"){
                layer.open({content: retData.msg ,skin: 'msg',time: 2 ,end:function () {
                    try{
                        if(linked.certifyContract&&typeof(linked.certifyContract)=="function"){
                            linked.certifyContract(retData.orderId);
                        }else{
                        }
                    }catch(e){
                        alert(e);
                    }
                }});
            }else{
                layer.open({content: data.retMsg ,skin: 'msg',time: 2 ,end:function () {
                try{
                    if(linked.backHomeAction&&typeof(linked.backHomeAction)=="function"){
                        linked.backHomeAction();
                    }else{
                    }
                }catch(e){
                    alert(e);
                }
            }});
            }
        }
    })
});
$("#wh").on("click",function () {
    var money = $("#productMonthPayBottom").val();
    $.modal({
        title: '',
        text: '每期还款金额为'+money+'元（不包含服务包费用）请做好个人财务规划,避免产生逾期额外费用',
        buttons: [
            { text: "知道了", onClick: function(){ } }
        ]
    });

});