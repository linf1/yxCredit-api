$(function () {
    initPageInfo();
});
function initPageInfo(){
    Comm.ajaxPost("/order/getAllPersonOrder1",'',function (data) {
        debugger
        var retData=data.retData;
        console.log(retData);
        var orderId = "";
        var html="";
        var allList = retData.allList;
        if(allList.length>0){
            for(var i=0;i<allList.length;i++){
                orderId = allList[i].id;//订单id
                $("#orderId").val(orderId);
                var merchantName = allList[i].merchantName==""?"暂无":allList[i].merchantName;//商户名称
                var merchandiseBrandName = allList[i].merchandiseBrand==""?"暂无":allList[i].merchandiseBrand;//商户名称
                var merchandiseModelName = allList[i].merchandiseModel==""?"暂无":allList[i].merchandiseModel;//商户名称
                var merchandiseVersionName = allList[i].merchandiseVersion==""?"暂无":allList[i].merchandiseVersion;//商户名称
                var merchandiseUrl = allList[i].merchandiseUrl==""?"暂无":allList[i].merchandiseUrl;//商户名称
                var monthPay = allList[i].monthPay==""?"暂无":allList[i].monthPay;//每期分期金额
                var periods = allList[i].periods==""?"暂无":allList[i].periods+"期";//分期期数
                var alterTime =  allList[i].alterTime;
                var commodityState = allList[i].commodityState;
                html+='<div class="commodity-top">' +
                        '<div class="commodity-title">' +
                            '<img src="'+_ctx+'/resources/merch-wechat/images/logo1.png" alt="">'+
                            '<span class="title-text" id="productMerchantName">'+merchantName+'</span>'+
                            '<span style="float: right;font-size:14px;margin-right:5px;" id="creatTime">'+alterTime+'</span>'+
                        '</div>'+
                        '<div data-id="'+orderId+'" class="commodity-content" onclick="toOrderDetils(\''+orderId+'\')"'+
                            '<input type="hidden">'+
                            '<img src="'+merchandiseUrl+'" alt="" id="productMerchandiseUrl">'+
                            '<div class="commodity-content-text">'+
                                '<p class="content-text-one" id="productMerchandiseBrandName">'+merchandiseBrandName+'</p>'+
                                '<p class="content-text-two">'+
                                '<span id="productMerchandiseModelName">'+merchandiseModelName+'</span>'+
                                '<span id="productMerchandiseVersionName">'+merchandiseVersionName+'</span>'+
                                '</p>'+
                                '<p class="content-text-three">'+
                                '<span id="productMonthPay">￥'+monthPay+'</span>'+
                                '<span style="margin-left: -12px;" id="Periods"> x'+periods+'</span>'+
                                '</p>'+
                            '</div>'+
                        '</div>';
                if(allList[i].state == "0"){
                    if(commodityState == "11" || commodityState == "12" || commodityState == "13" ||commodityState == "14"){
                        var stateRemark = "已提交办单员处理";
                        html+= '<div class="text-time">'+
                            '<span class="time-text" style="color: red">'+stateRemark+'</span>'+
                            '</div>';
                    }else if(commodityState == "15"){
                        var stateRemark ="确认信息";
                        html+= '<div class="text-time">'+
                            '<button class="affirm" style="background:#fff;" onclick="confirmInfo(\''+orderId+'\')">'+stateRemark+'</button>'+
                            '</div>';
                    }else if(commodityState == "21"){
                        var stateRemark = "已退回";
                        html+= '<div class="text-time">'+
                            '<button class="affirm" style="background:#fff;right:6%;" onclick="orderReapply(\''+orderId+'\')">重新申请</button>'+
                            '<span class="time-text" style="color: red">'+stateRemark+'</span>'+
                            '</div>';
                    }
                }else if(allList[i].state == "1" || allList[i].state == "2" ||allList[i].state == "4" ){
                    var stateRemark = "审核中";
                    html+= '<div class="text-time">'+
                        '<span class="time-text" style="color: red">'+stateRemark+'</span>'+
                        '</div>';
                }else if(allList[i].state == "3" || allList[i].state == "3.5" || allList[i].state=='6'){
                    var stateRemark = "审核未通过";
                    html+= '<div class="text-time">'+
                        '<span class="time-text" style="color: red">'+stateRemark+'</span>'+
                        '</div>';
                }else if(allList[i].state == "5"){
                    if(commodityState == '16'){//审批通过 等待办单员绑卡
                            var stateRemark = "审核已通过,等待办单员绑卡";
                            html+= '<div class="text-time">'+
                                '<span class="time-text" style="color: red">'+stateRemark+'</span>'+
                                '</div>';
                    } else if(commodityState == '16.5'){//绑卡成功，等待确认合同,跳转确认合同页面
                        var isBank = allList[i].isBank;
                        // var packageName = typeof (allList[i].packageName) ==   'undefined'?'':allList[i].packageName ;
                        // var svcPcgAmount = typeof (allList[i].svcPcgAmount) == 'undefined'?'':'￥'+allList[i].svcPcgAmount;
                        html+= '<div class="text-time">'+
                            // '<span style="float: left;">'+packageName+'</span>'+
                            // '<span style="margin-right: 25px;">'+svcPcgAmount+'</span>'+
                            '<button class="affirm" style="background:#fff;" onclick="gotoHeTon(\''+orderId+'\',\''+isBank+'\')">合同确认</button>'+
                            '</div>';
                    } else if(commodityState == '16.7'){//此时合同已确认，确认合同之后判断前置服务包
                        if (allList[i].code == '1'){
                            var packageName = allList[i].packageName;
                            var svcPcgAmount = '￥'+allList[i].svcPcgAmount;
                            var bankCard = allList[i].bankCard;
                            var bankName = allList[i].bankName;
                            var isBank = allList[i].isBank;
                            // var orderId = allList[i].id;
                            html+= '<div class="text-time">'+
                                '<span style="float: left;">'+packageName+'</span>'+
                                '<span style="margin-right: 25px;">'+svcPcgAmount+'</span>'+
                                '<button class="affirm" style="background:#fff;" onclick="lijizhifu(\''+svcPcgAmount+'\',\''+packageName+'\',\''+orderId+'\',\''+isBank+'\',\''+bankCard+'\',\''+bankName+'\')">立即支付</button>'+
                                '</div>';
                        }else {
                            var stateRemark = "审核已通过,办单员处理订单中";
                            html+= '<div class="text-time">'+
                                '<span class="time-text" style="color: red">'+stateRemark+'</span>'+
                                '</div>';
                        }
                    }else if(commodityState == '17' || commodityState=='18'){//17 18  都是等待办单员处理
                        html+= '<div class="text-time">'+
                            '<span class="time-text" style="color: red">审核已通过,办单员处理订单中</span>'+
                            '</div>';
                    }else{ //19 20 可以去还款了 19(上传发货图片)20(提交串号码)
                        debugger
                        var packageName = typeof (allList[i].packageName) ==   'undefined'?'':allList[i].packageName ;
                        var svcPcgAmount = typeof (allList[i].svcPcgAmount) == 'undefined'?'':'￥'+allList[i].svcPcgAmount;
                        html+= '<div class="text-time">'+
                            // '<span style="float: left;">'+packageName+'</span>'+
                            // '<span style="margin-right: 25px;">'+svcPcgAmount+'</span>'+
                            '<button class="affirm" style="background:#fff;" onclick="quhuankuan(\''+orderId+'\')">去还款</button>'+
                            '</div>';
                    }

                }else if(allList[i].state == "9"){
                    html+= '<div class="text-time">'+
                        '<span class="time-text" style="color: red">已结清</span>'+
                        '</div>';
                }else if(allList[i].state == "8"){//订单大状态放款成功
                    html+= '<div class="text-time">'+
                        '<button class="affirm" style="background:#fff;" onclick="quhuankuan(\''+orderId+'\')">去还款</button>'+
                        '</div>';
                }else if(allList[i].state == "10"){
                    html+= '<div class="text-time">'+
                        '<span class="time-text" style="color: red">已关闭</span>'+
                        '</div>';
                }
                html+='</div>';
            }
        }else{
             html +='<div class="commodity-top" style="text-align: center;font-size: 14px">暂无订单</div>';
        }
        $("#commodity-content").html(html);
    })
}

$('#commodity').pullToRefresh(function () {
    setTimeout(function () {
        initPageInfo();
        $('#commodity').pullToRefreshDone();
    },1000)
});

function toOrderDetils(orderId) {
     // window.location=_ctx+"/wechat/page/toDetailsOrder?orderId="+orderId;
     window.location=_ctx+"/wechat/page/toHistoryDetailsOrder?orderId="+orderId;
}

//确认信息
function confirmInfo(orderId) {
    Comm.ajaxPost("/miaofuOrder/subOrder","orderId="+orderId,function (data) {
        var retCode=data.retCode;
        console.log(retCode);
        if(retCode == "SUCCESS"){
            location.reload();
        }else{
            layer.open({content: "提交失败" ,skin: 'msg',time: 2});
        }
    });
}
//重新申请
function orderReapply(orderId) {
    try{
        if(linked.certifyContract&&typeof(linked.certifyContract)=="function"){
            linked.certifyContract(orderId);
        }else{
        }
    }catch(e){
        alert(e);
    }
}

function lijizhifu(svcPcgAmount,packageName,orderId,isBank,bankCard,bankName) {
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
//跳转合同页面
function  gotoHeTon(orderId,isBank) {
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
        var type='false';
        window.location =_ctx+"/wechat/page/confirmContract?isChecked="+type+"&orderId="+orderId;
    }
}
//去还款方法
function quhuankuan(orderId){
    //判断是否有有效的提前结清
    Comm.ajaxPost("/repay/getSettleAuth","orderId="+orderId,function(data){
        var data = data.retData;
        var state = data.state;
        if (state == "2"){
            window.location = _ctx+"/wechat/page/toRefundOne?orderId="+orderId;
        }else{
            window.location =_ctx+"/wechat/page/toRefundTwo?orderId="+orderId;
        }
    });
}