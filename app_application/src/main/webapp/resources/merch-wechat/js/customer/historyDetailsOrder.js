$(function () {
    initPageInfo();
    $("#showPackage").on("click", function () {
        $(".severPackage").show();
    })
});
function initPageInfo() {
    //判断当期是否已还款；
    // checkNowIsNotPay();
    var orderId = $("#orderId").val();
    // orderId='70bab7de-7180-42e2-8850-6bf65f4b5480';
    Comm.ajaxPost("/order/getFenqiOrderInfo", "orderId=" + orderId, function (data) {
        var retData = data.retData;
        console.log(retData);
        var html = "";
        if (retData) {
            retData.merchantName == "" ? $("#productMerchantName").html("暂无") : $("#productMerchantName").html(retData.merchantName);//商户名称
            retData.merchandiseBrandName == "" ? $("#productMerchandiseBrandName").html("暂无") : $("#productMerchandiseBrandName").html(retData.merchandiseBrandName);//商品名称
            retData.merchandiseModelName == "" ? $("#productMerchandiseModelName").html("暂无") : $("#productMerchandiseModelName").html(retData.merchandiseModelName);//商品类型
            retData.merchandiseVersionName == "" ? $("#productMerchandiseVersionName").html("暂无") : $("#productMerchandiseVersionName").html(retData.merchandiseVersionName);//商品版本
            retData.merchandiseUrl == "" ? $("#productMerchandiseUrl").attr("src", "") : $("#productMerchandiseUrl").attr("src", retData.merchandiseUrl);//商品图片
            retData.monthPay == "" ? $("#productMonthPay").html("暂无") : $("#productMonthPay").html("￥" + retData.monthPay);//每期分期金额
            retData.monthPay == "" ? $("#productMonthPayBottom").val("￥0.00") : $("#productMonthPayBottom").val("￥" + retData.monthPay);//每期分期金额
            retData.periods == "" ? $("#Periods").html("暂无") : $("#Periods").html("x" + retData.periods + "期");//分期期数
            retData.periods == "" ? $("#productPeriods").val("0期") : $("#productPeriods").val(retData.periods + "期");//分期期数
            retData.servicePagNum == "" ? $("#severBag").val("共有0款服务包") : $("#severBag").val("共有" + retData.servicePagNum + "款服务包");//服务包个数
            retData.allMoney == "" ? $("#productAllMoney").val("￥0.00") : $("#productAllMoney").val("￥" + retData.allMoney);//商品总价
            retData.downPayMoney == "" ? $("#productDownPayMoney").val("￥0.00") : $("#productDownPayMoney").val("￥" + retData.downPayMoney);//首付金额
            retData.fenqiMoney == "" ? $("#productFenqiMoney").val("￥0.00") : $("#productFenqiMoney").val("￥" + retData.fenqiMoney);//申请分期金额
        }
        if (retData.serPackageList) {
            var serPackageList = retData.serPackageList;
            for (var i = 0; i < serPackageList.length; i++) {
                html += '<div class="packageInfo"><p class="packageNameInfo"><span>' + serPackageList[i].packageName + '</span><span class="rightWord">￥' + serPackageList[i].collectionAmount + '</span></p><p class="packageNameInfo"><span>' + serPackageList[i].qsRemark + '</span><span class="rightWord">' + serPackageList[i].sqTypeRemark + '</span></p></div>';
            }
            $(".packageBody").html(html);
        }
        var commodityState = retData.commodityState;
        var state = retData.state;
        if (state == "0") {
            if (commodityState == "11") {
                document.getElementById("applyInfo").style.display = "none";
                document.getElementById("imageInfo").style.display = "none";
                document.getElementById("handSign").style.display = "none";
                document.getElementById("commodityEmp").style.display = "block";
                $("#header_img").attr("src", _ctx + "/resources/merch-wechat/images/tjbdy.png");
            } else if (commodityState == "12") {
                document.getElementById("imageInfo").style.display = "none";
                document.getElementById("handSign").style.display = "none";
                document.getElementById("commodityEmp").style.display = "block";
                $("#header_img").attr("src", _ctx + "/resources/merch-wechat/images/tjbdy.png");
            } else if (commodityState == "13") {
                document.getElementById("handSign").style.display = "none";
                document.getElementById("commodityEmp").style.display = "block";
            } else if (commodityState == "14") {
                document.getElementById("commodityEmp").style.display = "block";
                $("#header_img").attr("src", _ctx + "/resources/merch-wechat/images/tjbdy.png");
            } else if (commodityState == "15") {
                document.getElementById("commodity-content-footer").style.display = "block";
                $("#header_img").attr("src", _ctx + "/resources/merch-wechat/images/ddqrxx.png");
            }else if (commodityState == "21" ) { //订单退回
                $("#header_img").attr("src", _ctx + "/resources/merch-wechat/images/tuihui.png");//显示还款中
            }
        } else if (state == "3" || state == "3.5" || state == "6") {
            document.getElementById("unPass").style.display = "block";
            $("#header_img").attr("src", _ctx + "/resources/merch-wechat/images/shwtg.png");
        } else if (state == "1" || state == "2" || state == "4") {
            document.getElementById("passIng").style.display = "block";
            $("#header_img").attr("src", _ctx + "/resources/merch-wechat/images/shz.png");
        } else if (state == "9") {
            $("#header_img").attr("src", _ctx + "/resources/merch-wechat/images/yijieqing.png"); //图片换成已结清
            $('#qbjq').css({"display": "none"});//全部结清按钮隐藏
            $("#already").css({"display":"block"});//显示还款历史明细按钮
            $("#payHistoryNumber").css({"display":"block"});//显示已还期数和已还总额div
            //隐藏（查看申请信息，查看影像资料，查看手签，查看合同）
            $("#lookLookSeeSee").css({"display":"none"});
        }else if (state == "8") {
            $("#header_img").attr("src", _ctx + "/resources/merch-wechat/images/huankuanzhong.png");//显示还款中
            $("#already").css({"display":"block"});//显示还款历史明细按钮
            $("#payHistoryNumber").css({"display":"block"});//显示已还期数和已还总额div
            checkNowIsNotPay();//查询此时当月当期是否已还
            $("#qbjq").css({"display": "inline"});//显示全部结清按钮
            $("#theLast").css("z-index","1")
        } else if (state == "10") {
            $("#header_img").attr("src", _ctx + "/resources/merch-wechat/images/guanbi.png");
        }else if (state == "5") {
            if (commodityState == "16") {//此时审批已通过，等待办单员绑卡
                document.getElementById("waitBindCard").style.display = "block";
                $("#header_img").attr("src", _ctx + "/resources/merch-wechat/images/ddqrxx.png");
            } else if (commodityState == "16.5") {//绑卡成功，等待客户确认合同
                // document.getElementById("pass").style.display = "block";
                $("#header_img").attr("src", _ctx + "/resources/merch-wechat/images/ddqrxx.png");
                $('#sureHetong_btn').css("z-index","1");//设置优先级
                $('#sureHetong_btn').css("display","block");
                $("#theLast").css("z-index","-1")
            } else if (commodityState == "16.7") {//此时合同已确认(查看合同div显示) 判断有无前置服务包
                // $('#pass').css("display","block");;//显示审核已通过,办单员处理订单中
                $('#showHeton').css("display", "block");//显示合同查看div
                $("#isBank").val(retData.isBank);
                $("#header_img").attr("src", _ctx + "/resources/merch-wechat/images/shtg.png");
                if (retData.code == "1") {//有服务包
                    $("#packageName").html(retData.packageName);
                    $("#svcPcgAmount").html("￥" + retData.svcPcgAmount);
                    // document.getElementById("pay").style.display = "block";
                    $("#ljzf_btn").css('display','block');
                    $("#ljzf_btn").css('z-index','1');//设置优先级
                    $("#theLast").css("z-index","-1");
                } else {//无服务包
                    $('#pass').css("display", "block");
                    ;//显示审核已通过,办单员处理订单中
                    $('#showHeton').css("display", "block");//显示合同查看div
                    $("#header_img").attr("src", _ctx + "/resources/merch-wechat/images/shtg.png");
                }
                if (retData.isBank == "1") {
                    $("#bankCard").val(retData.bankCard);
                    $("#bankName").val(retData.bankName);
                }
            } else if (commodityState == "17") {//前置服务包
                $('#pass').css("display", "block");
                ;//显示审核已通过,办单员处理订单中
                $('#showHeton').css("display", "block");//显示合同查看div
                $("#header_img").attr("src", _ctx + "/resources/merch-wechat/images/shtg.png");
            } else if (commodityState == "18") {//预付款已收取
                $('#pass').css("display", "block");
                ;//显示审核已通过,办单员处理订单中
                $('#showHeton').css("display", "block");//显示合同查看div
                $("#header_img").attr("src", _ctx + "/resources/merch-wechat/images/shtg.png");
                //隐藏（查看申请信息，查看影像资料，查看手签，查看合同）
                $("#lookLookSeeSee").css({"display":"none"});
            } else if (commodityState == "19" || commodityState == "20") { //此时已提货，可以去还款了
                $("#header_img").attr("src", _ctx + "/resources/merch-wechat/images/huankuanzhong.png");//显示还款中
                $('#showHeton').css("display", "block");//显示合同查看div
                checkNowIsNotPay();//查询此时当月当期是否已还
                // $("#jqbq").css({"display":"inline"});//显示结清本期按钮
                $("#qbjq").css({"display": "inline"});//显示全部结清按钮
                $("#theLast").css("z-index","1");
                $("#already").css({"display":"block"});//显示还款历史明细按钮
                $("#payHistoryNumber").css({"display":"block"});//显示已还期数和已还总额div
                //隐藏（查看申请信息，查看影像资料，查看手签，查看合同）
                $("#lookLookSeeSee").css({"display":"none"});
            }
        }
        //还款记录
        var alreadyPayList = retData.alreadyPayList;
        if (alreadyPayList) {
            html = '<p style="border-bottom: 1px solid #E1E1E1;">还款历史明细</p>'
            if (alreadyPayList.length > 0) {
                for (var i = 0; i < alreadyPayList.length; i++) {
                    var actualTime = alreadyPayList[i].actualTime;
                    actualTime = actualTime.substring(0, 4) + "-" + actualTime.substring(4, 6) + "-" + actualTime.substring(6, 8);
                    var actualAmount = parseFloat(alreadyPayList[i].actualAmount).toFixed(2);
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
                    html += '<p style="font-size: 13px;"><span style="text-align: left;display: inline-block;">' + moneyTitle +' 还款</span><span style="text-align: right;display: inline-block; ">&nbsp;&nbsp;&nbsp;￥' + actualAmount+ (alreadyPayList[i].redAmount ? '(红包抵扣'+ alreadyPayList[i].redAmount+ ')' : '') + '</span><span style="float: right;">'+ actualTime+'</span></p>';
                }
            } else {
                html += '<p style="font-size: 13px;"><span style="text-align: left;display: inline-block;">-暂无还款记录-</span><span style="text-align: right;display: inline-block; width: 60px;"></span></p>';

            }
            $("#already").html(html);//还款记录生成
            //已还期数
           // $('#yihuanqishu').val(retData.alreadyPayCount);
            retData.alreadyPayCount == "" ? $("#yihuanqishu").val("0期") : $("#yihuanqishu").val(retData.alreadyPayCount + "期");//已还期期数
            //已还总额
            var yihuanzonge = 0;
            for (var m = 0; m < alreadyPayList.length; m++) {
                yihuanzonge += parseFloat(alreadyPayList[m].actualAmount);
            }

            $('#yihuanzonge').val("￥"+ yihuanzonge.toFixed(2));

        }
    })
}
function checkInfomationTure() {
    $.modal({
        title: '',
        text: '若申请信息不正确，请联系您的办单员撤销此笔订单，修改后再次确认',
        buttons: [
            {
                text: "知道了", onClick: function () {
            }
            }
        ]
    });
}

//查询当前期是否已还款
function checkNowIsNotPay() {
    debugger;
    var orderId = $("#orderId").val();
    // orderId='b407f980-5abe-4558-b11a-a5e5ae16265d';
    Comm.ajaxPost("/order/checkNowMonthPay", "orderId=" + orderId, function (data) {
        var yesOrNo = data.retData;
        if (yesOrNo == "1") {//1代表当期未还
            $("#jqbq").css({"display": "inline"});
            $("#theLast").css("z-index","1")
        } else if (yesOrNo == "") {//空代表当前无还款计划
            // $("#jqbq").css({"display":"inline"});
        }
    });


    //  Comm.ajax("/order/checkNowMonthPay","orderId="+orderId,function (data) {
    //     console.log(data);
    //     var yesOrNo=data.retCode;
    //     if(yesOrNo == 1){
    //         $("#jqbq").css({"display":"block"});
    //     }
    // });
}

function checkOrderState() {
    var orderId = $("#orderId").val();
    Comm.ajaxPost("/order/updateOrderState", "orderId=" + orderId + "&state=1", function (data) {
        location.reload();
    });
}

//查看申请信息-H5
function checkApplyInfo() {
    window.location = _ctx + "/wechat/page/toPersonInfoHome";
}
//查看影像信息-原生
function checkImgInfo() {
    var orderId = $("#orderId").val();
    try {
        if (linked.portraitContract && typeof(linked.portraitContract) == "function") {
            linked.portraitContract(orderId);
        } else {
        }
    } catch (e) {
        alert(e);
    }
}
//查看手签签名-原生
function chcekSignInfo() {
    var orderId = $("#orderId").val();
    try {
        if (linked.signContract && typeof(linked.signContract) == "function") {
            linked.signContract(orderId);
        } else {
        }
    } catch (e) {
        alert(e);
    }
}
//确认信息
function confirmInfo() {
    var orderId = $("#orderId").val();
    Comm.ajaxPost("/miaofuOrder/subOrder", "orderId=" + orderId, function (data) {
        var retCode = data.retCode;
        console.log(retCode);
        if (retCode == "SUCCESS") {
            window.location = _ctx + "/wechat/page/toApplySuccess?orderId=" + orderId;
        } else {
            layer.open({content: "提交失败", skin: 'msg', time: 2});
        }
    });
}
//结清本期
function jieqingbenqi() {
    var orderId = $("#orderId").val();
    //跳转还款页面
    window.location = _ctx + "/wechat/page/toRefundTwo?orderId=" + orderId;
}

//全部结清
function quanbujieqing() {
    var orderId = $("#orderId").val();
    Comm.ajaxPost("/repay/getSettleAuth", "orderId=" + orderId, function (data) {
        console.log(data);
        var data = data.retData;
        var state = data.state;
        if (state == "2") {
            window.location = _ctx + "/wechat/page/toRefundOne?orderId=" + orderId;
        } else if (state == "1") {
            $.modal({
                title: "提前结清",
                text: "确认提前结清，立即拨打客服电话为您开启结清通道。",
                buttons: [
                    {
                        text: "暂不结清", onClick: function () {
                        console.log(1)
                    }
                    },
                    {
                        text: "联系客服", onClick: function () {
                        window.location.href = 'tel://' + data.tel
                    }
                    }
                ]
            });
        } else if (state == "0") {
            var servicePackage = data.servicePackage;
            var html = '<p style="text-align:left;font-size:12px;">您未购买提前结清服务包!</p>';
            var month = "";
            if(servicePackage.length!=0){
                html = '<p style="text-align:left;font-size:12px;">您购买的服务包为:</p>';
                servicePackage.forEach(function (item) {
                    if (item.type == '1') {
                        html += '<p style="text-align: left;font-size:14px;"><span>' + item.packageName + '</span><span style="float:right">' + item.amount + '元</span></p>'
                        if (item.month)
                        {
                            var month1 =parseInt(item.month);
                            if (!month) {
                                month=month1;
                            } else if (month < month1) {
                                month=month1;
                            }
                        }
                    } else if(item.type == '2'){
                        html += '<p style="text-align: left;font-size:14px;"><span>' + item.packageName + '</span><span style="float:right">' + item.amount + '元/月</span></p>'
                        if (item.month) {
                            var month2 =parseInt(item.month);
                            if (!month) {
                                month=month2;
                            } else if (month < month2)
                            {
                                month=month2;
                            }
                        }

                    }else{
                        html += '<p style="text-align: left;font-size:14px;"><span>' + item.packageName + '</span><span style="float:right">' + item.amount + '元/月</span></p>'
                    }
                })
                if(month){
                    html += '<p style="text-align: left;font-size:14px;margin-top:6px;">第' + month + '期即可提前结清分期账单。</p>';
                }

            }
          /*  servicePackage.forEach(function (item) {
                if (item.type == '1') {
                    html += '<p style="text-align: left;font-size:14px;"><span>' + item.packageName + '</span><span style="float:right">' + item.amount + '元</span></p>'
                } else {
                    html += '<p style="text-align: left;font-size:14px;"><span>' + item.packageName + '</span><span style="float:right">' + item.amount + '元/月</span></p>'
                }
                month = item.month;
            })
            html += '<p style="text-align: left;font-size:14px;margin-top:6px;">第' + month + '期即可提前结清分期账单。</p>';*/
            $.modal({
                title: "提前结清",
                text: "<div style='font-size:12px;text-align:left;margin-bottom:10px;'>很遗憾，您暂未拥有提前结清的权限。</div>" + html,
                buttons: [
                    {
                        text: "知道了", onClick: function () {
                        console.log(1)
                    }
                    },
                    {
                        text: "联系客服", onClick: function () {
                        window.location.href = 'tel://' + data.tel
                    }
                    }
                ]
            });
        }
    });


}

//获取还款历史记录
function getPayListHistory() {
    var orderId = $("#orderId").val();
    Comm.ajaxPost("/order/getFenqiOrderInfo", "orderId=" + orderId, function (data) {
        var retData = data.retData;
        console.log(retData);
        var html = "";
        // if(retData){
        //     retData.merchantName==""?$("#productMerchantName").html("暂无"):$("#productMerchantName").html(retData.merchantName);//商户名称
        //     retData.merchandiseBrandName==""?$("#productMerchandiseBrandName").html("暂无"):$("#productMerchandiseBrandName").html(retData.merchandiseBrandName);//商品名称
        //     retData.merchandiseModelName==""?$("#productMerchandiseModelName").html("暂无"):$("#productMerchandiseModelName").html(retData.merchandiseModelName);//商品类型
        //     retData.merchandiseVersionName==""?$("#productMerchandiseVersionName").html("暂无"):$("#productMerchandiseVersionName").html(retData.merchandiseVersionName);//商品版本
        //     retData.merchandiseUrl==""?$("#productMerchandiseUrl").attr("src",""):$("#productMerchandiseUrl").attr("src",retData.merchandiseUrl);//商品图片
        //     retData.monthPay==""?$("#productMonthPay").html("暂无"):$("#productMonthPay").html("￥"+retData.monthPay);//每期分期金额
        //     retData.monthPay==""?$("#productMonthPayBottom").val("￥0.00"):$("#productMonthPayBottom").val("￥"+retData.monthPay);//每期分期金额
        //     retData.periods==""?$("#Periods").html("暂无"):$("#Periods").html("x"+retData.periods+"期");//分期期数
        //     retData.periods==""?$("#productPeriods").val("0期"):$("#productPeriods").val(retData.periods+"期");//分期期数
        //     retData.servicePagNum==""?$("#severBag").val("共有0款服务包"):$("#severBag").val("共有"+retData.servicePagNum+"款服务包");//服务包个数
        //     retData.allMoney==""?$("#productAllMoney").val("￥0.00"):$("#productAllMoney").val("￥"+retData.allMoney);//商品总价
        //     retData.downPayMoney==""?$("#productDownPayMoney").val("￥0.00"):$("#productDownPayMoney").val("￥"+retData.downPayMoney);//首付金额
        //     retData.fenqiMoney==""?$("#productFenqiMoney").val("￥0.00"):$("#productFenqiMoney").val("￥"+retData.fenqiMoney);//申请分期金额
        // }
        // if(retData.serPackageList){
        //     var serPackageList=retData.serPackageList;
        //     for(var i=0;i<serPackageList.length;i++){
        //         html+='<div class="packageInfo"><p class="packageNameInfo"><span>'+serPackageList[i].packageName+'</span><span class="rightWord">￥'+serPackageList[i].collectionAmount+'</span></p><p class="packageNameInfo"><span>'+serPackageList[i].qsRemark+'</span><span class="rightWord">'+serPackageList[i].sqTypeRemark+'</span></p></div>';
        //     }
        //     $(".packageBody").html(html);
        // }
        // var commodityState = retData.commodityState;
        // var state = retData.state;
        // if(state == "0"){
        //     if(commodityState=="12"){
        //         document.getElementById("applyInfo").style.display = "block";
        //     }else if(commodityState=="13"){
        //         document.getElementById("applyInfo").style.display = "block";
        //         document.getElementById("imageInfo").style.display = "block";
        //     }else if(commodityState=="14"){
        //         document.getElementById("applyInfo").style.display = "block";
        //         document.getElementById("imageInfo").style.display = "block";
        //         document.getElementById("handSign").style.display = "block";
        //     }else if(commodityState=="15"){
        //         document.getElementById("applyInfo").style.display = "block";
        //         document.getElementById("imageInfo").style.display = "block";
        //         document.getElementById("handSign").style.display = "block";
        //         document.getElementById("commodity-content-footer").style.display = "block";
        //     }
        // }else if(state == "5"){
        //     document.getElementById("applyInfo").style.display = "block";
        //     document.getElementById("imageInfo").style.display = "block";
        //     document.getElementById("handSign").style.display = "block";
        //     if (retData.isBank == '0'){
        //         document.getElementById("commodity-content-footer").style.display = "block";
        //     }
        // }else{
        //     document.getElementById("applyInfo").style.display = "block";
        //     document.getElementById("imageInfo").style.display = "block";
        //     document.getElementById("handSign").style.display = "block";
        //     document.getElementById("commodity-content-footer").style.display = "block";
        // }
        var alreadyPayList = retData.alreadyPayList;
        if (alreadyPayList) {
            html = '<p>还款历史明细</p>'
            for (var i = 0; i < alreadyPayList.length; i++) {
                var actualTime = alreadyPayList[i].actualTime;
                actualTime = actualTime.substring(0, 4) + "-" + actualTime.substring(4, 6) + "-" + actualTime.substring(6, 8);
                var actualAmount = alreadyPayList[i].actualAmount;
                html += '<p><span style="text-align: left;display: inline-block;">' + actualTime + ' 还款</span><span style="text-align: right;display: inline-block; width: 60px;">￥' + actualAmount + '</span></p>';
            }
            $("#already").html(html);
        }
    })
}


/**新增查看合同**/
function checkContract(type) {
    var orderId = $("#orderId").val();
    window.location = _ctx + "/wechat/page/confirmContract?isChecked=" + type + "&orderId=" + orderId;
}
//支付前置服务包（立即支付）
function zhifuqianhzi() {
    var svcPcgAmount = $("#svcPcgAmount").html();
    var packageName = $("#packageName").html();
    var orderId = $("#orderId").val();
    var isBank = $("#isBank").val();
    var bankCard = $("#bankCard").val();
    var bankName = $("#bankName").val();
    if (isBank == '0') {
        $.modal({
            title: '',
            text: '您当前还未绑卡,请联系您的专属业务员进行邦卡',
            buttons: [
                {
                    text: "知道了", onClick: function () {
                    location.reload();
                }
                }
            ]
        });
    } else {
        window.location = _ctx + "/wechat/page/toPay?svcPcgAmount=" + svcPcgAmount + "&packageName=" + packageName + "&orderId=" + orderId + "&bankCard=" + bankCard + "&bankName=" + bankName;
    }
}