/**首页信息**/
getCurrentPage();
/**重写tab切换**/
$(function(){
    $('.weui-tabbar__item').on('click', function () {
        $(this).addClass('weui-bar__item--on').siblings().removeClass('weui-bar__item--on');
        var tabId = $(this).attr('href');
        $('.weui-tab__bd').find(tabId).addClass('weui-tab__bd-item--active').siblings().removeClass('weui-tab__bd-item--active');
        if(tabId=="#tab1"){
            getCurrentPage();
        }else if(tabId=="#tab2"){
            getPaymentPage();
        }else if(tabId=="#tab3"){
            getMinePage();
        }
    });
});
/**首页页面接口**/
var productParams={periodsId:"",periods:"",money:"",moneyArry:[]};
/**periodsId第一个产品对应的第一个期数id**/
/***periods第一个产品对应的第一个期数*/
/**money第一个产品对应的第一个钱**/
function getCurrentPage() {
    getCurrentPageInfo();
}
/**获取首页信息轮播图等**/
function getCurrentPageInfo() {
    Comm.ajaxPost("/wechat/home/getProductInfo","",function (data) {
        if(data.retCode=="SUCCESS"){
            var CreditPreAmount=data.retData.credit_pre_amount;//额度
            var host=data.retData.host;
            var num=data.retData.num;
            msgCount=num;
            var Picture=data.retData.list;
            var periodsList=data.retData.periodsList;//期数列表
            var sortProducts=data.retData.productList;//产品列表
            //对产品金额进行冒泡排序，以避免提额导致的顺序混乱
            var _proMoneyList = bubbleSort(sortProducts);
            //产品金额去重
            var ProCrmProducts=uniqueList(_proMoneyList);
            if(num<=0){
                $("#num").hide();
            }else{
                $("#num").html(num);
                $("#num").show();
            }
            var PictureHtml="";
            for(var i=0;i<Picture.length;i++){
                PictureHtml+='<div class="swiper-slide"><img onerror="this.src=\'/resources/cash-wechat/images/banner_01.jpg\'" src="'+host+Picture[i].activity_img_url+'"></div>';
            }
            $(".swiper-wrapper").html(PictureHtml);
            var swiper = new Swiper('.swiper-container', {
                pagination: '.swiper-pagination',
                paginationClickable: true,
                loop : true,
                autoplay : 3000
            });
            var ProductsHtml="";
            for(var j=0;j<ProCrmProducts.length;j++){
                if(j==0){
                    productParams.money=ProCrmProducts[j].productAmount;
                    productParams.moneyArry.push(Number(ProCrmProducts[j].productAmount));
                    ProductsHtml+='<li onclick="getProductsPeriods(this)" productAmount="'+ProCrmProducts[j].productAmount+'"  productId="'+ProCrmProducts[j].productId+'"  productName="'+ProCrmProducts[j].productName+'" class="active">'+ProCrmProducts[j].productAmount+'</li>';
                }else{
                    productParams.moneyArry.push(Number(ProCrmProducts[j].productAmount));
                    ProductsHtml+='<li onclick="getProductsPeriods(this)" productAmount="'+ProCrmProducts[j].productAmount+'"  productId="'+ProCrmProducts[j].productId+'"  productName="'+ProCrmProducts[j].productName+'"  class="">'+ProCrmProducts[j].productAmount+'</li>';
                }
            }
            $("#productList").html(ProductsHtml);
            $("#CreditPreAmount").html(Math.max(productParams.moneyArry[0],productParams.moneyArry[1],productParams.moneyArry[2]));
            var periodsListHtml="";
            for(var t=0;t<periodsList.length;t++){
                if(t==0){
                    productParams.periodsId=periodsList[t].periodsId;
                    productParams.periods=periodsList[t].periods;
                    periodsListHtml+='<li onclick="getProducts(this)"  periodsId="'+periodsList[t].periodsId+'" periods="'+periodsList[t].periods+'"  class="active">'+periodsList[t].periods+'天</li>';
                }else{
                    periodsListHtml+='<li onclick="getProducts(this)"  periodsId="'+periodsList[t].periodsId+'" periods="'+periodsList[t].periods+'" class="">'+periodsList[t].periods+'天</li>';
                }
            }
            $("#periodsList").html(periodsListHtml);
            getCurrentPageFree(productParams.periodsId,productParams.periods,productParams.money);
        }else{
            layer.open({content: data.msg,skin: 'msg',time: 2 });
        }
    });
}
function getCurrentPageFree(periodsId,periods,CreditPreAmount) {
    var params = {};
    params.periodsId = periodsId;
    params.money = CreditPreAmount;
    params.periods =periods;
    Comm.ajaxPost("/wechat/home/getProductFee",params,function (sms) {
        if(sms.retCode=="SUCCESS"){
            $("#expireMoney").html(sms.retData.principal);//到期应还
            $("#expireTime").html(sms.retData.dueDate);//还款日期
            $("#quickMoney").html(sms.retData.quickTrialFee);//快速审信费
            $("#mangerMoney").html(sms.retData.manageFee);//账户管理
            $("#interest").html(sms.retData.interest);//利息
        }
    });
}
/**对产品金额进行冒泡排序*/
function bubbleSort(list) {
    if(!list){
        return;
    }
    for(var i=0,len=list.length - 1;i<len;i++){
        for(var j=0;j<list.length-i-1;j++){
            if(Number(list[j].productAmount) > Number(list[j+1].productAmount)){
                var t = list[j];
                list[j] = list[j+1];
                list[j+1] = t;
            }
        }
    }
    return list;
}
/**提额后数组去重**/
function uniqueList(array) {
    if(!array||array.length==0){
        return
    }
    var r = [];
    for(var i = 0, l = array.length; i < l; i++) {
        for(var j = i + 1; j < l; j++)
            //关键在这里
            if (array[i].productAmount == array[j].productAmount) j = ++i;
        r.push(array[i]);
    }
    return r;
}
/**选择产品期数等***/
function getProducts(me) {
    $("#periodsList li").removeClass("active");
    $(me).addClass("active");
    getCurrentPageFree($(me).attr("periodsId"),$(me).attr("periods"),productParams.money)
}
/**选择产品切换对应期数**/
function getProductsPeriods(me) {
    $("#productList li").removeClass("active");
    $(me).addClass("active");
    getPeriods($(me).attr("productid"),$(me).attr("productamount"));
}
function getPeriods(productId,currentMoney) {
    Comm.ajaxPost("/wechat/home/getPeriods","productId="+productId,function (sms) {
        if(sms.retCode=="SUCCESS"){
            var currentPeriodsList=sms.retData.periodsList;
            var currentProductPeriodsListHtml="";
            for(var k=0;k<currentPeriodsList.length;k++){
                if(k==0){
                    productParams.periodsId=currentPeriodsList[k].periodsId;
                    productParams.periods=currentPeriodsList[k].periods;
                    currentProductPeriodsListHtml+='<li onclick="getProducts(this)"  periodsId="'+currentPeriodsList[k].periodsId+'" periods="'+currentPeriodsList[k].periods+'"  class="active">'+currentPeriodsList[k].periods+'天</li>';
                }else{
                    currentProductPeriodsListHtml+='<li onclick="getProducts(this)"  periodsId="'+currentPeriodsList[k].periodsId+'" periods="'+currentPeriodsList[k].periods+'" class="">'+currentPeriodsList[k].periods+'天</li>';
                }
            }
            $("#periodsList").html(currentProductPeriodsListHtml);
            productParams.money=currentMoney;
            getCurrentPageFree(productParams.periodsId,productParams.periods,productParams.money);
        }
    });
}
/**首页立即申请**/
$("#currentBtn").on("click",function () {
    //window.location=_ctx+"/wechat/basicinfo/accredit";
    //window.location=_ctx+"/wechat/repay/toRepayPage";
    getInit();
    //webTest();
})
/**网页测试**/
function webTest() {
    var periods_id=$("#periodsList li.active").attr("periodsid");
    var product_id=$("#productList li.active").attr("productid");
    var dueDate=$("#expireTime").html();
    var fee=$("#interest").html();
    var manageFee=$("#mangerMoney").html();
    var quickTrialFee=$("#quickMoney").html();
    Comm.ajaxPost("/wechat/order/addOrder",{
        periods_id:periods_id,
        product_id:product_id,
        money:productParams.money,
        dueDate:dueDate,
        fee:fee,
        manageFee:manageFee,
        quickTrialFee:quickTrialFee
    },function (sms) {
        window.location=_ctx+"/wechat/home/identityPage?orderId="+sms.retData.order_id;
    });
}
/**微信测试**/
function getInit() {
    Comm.ajaxPost("/wechat/config","url="+encodeURIComponent(location.href.split('#')[0]),function (data) {
        data = data.retData;
        wx.config({
            debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
            appId: data.appId, // 必填，公众号的唯一标识
            timestamp: data.timestamp, // 必填，生成签名的时间戳
            nonceStr: data.nonceStr, // 必填，生成签名的随机串
            signature: data.signature,// 必填，签名，见附录1
            jsApiList: ['getLocation','onMenuShareAppMessage','onMenuShareTimeline','onMenuShareQQ'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
        });
        wx.ready(function(){
            wx.getLocation({
                type: 'wgs84', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
                success: function (res) {
                    var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
                    var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
                    var speed = res.speed; // 速度，以米/每秒计
                    var accuracy = res.accuracy; // 位置精度

                    var periods_id=$("#periodsList li.active").attr("periodsid");
                    var product_id=$("#productList li.active").attr("productid");
                    var dueDate=$("#expireTime").html();
                    var fee=$("#interest").html();
                    var manageFee=$("#mangerMoney").html();
                    var quickTrialFee=$("#quickMoney").html();
                    Comm.ajaxPost("/wechat/order/addOrder",{
                        periods_id:periods_id,
                        product_id:product_id,
                        money:productParams.money,
                        dueDate:dueDate,
                        fee:fee,
                        manageFee:manageFee,
                        quickTrialFee:quickTrialFee
                    },function (sms) {
                        window.location=_ctx+"/wechat/home/identityPage?orderId="+sms.retData.order_id;
                    });
                }
            });
        });
    });
}
/**还款页面接口**/
var openMsg="";
function getPaymentPage() {
    Comm.ajaxPost("/wechat/repay/getRepayList","",function (sms) {
        var data = sms.retData;
        if(msgCount<=0){
            $("#repayMsg i").hide();
        }else{
            $("#repayMsg i").show();
            $("#repayMsg i").html(msgCount);
        }
        if(data.repaidMap){
                var derateAllMoney=data.repaidMap.allMoney;//减免应还
                var allMoney=data.repaidMap.allMoney;//待还款
                var amount=data.repaidMap.amount;//借款金额
                var fee=data.repaidMap.fee;//利息
                var overdue_days=data.repaidMap.overdue_days;//逾期天数
                var overdue_fee=data.repaidMap.overdue_fee;//逾期费用
                var derate_amount=data.repaidMap.derate_amount;//减免金额
                var periods=data.repaidMap.periods;//期数
                var loanTime=data.repaidMap.loanTime.slice(0,4)+'-'+data.repaidMap.loanTime.slice(4,6)+"-"+data.repaidMap.loanTime.slice(6,8);//借款日
                var repayment_time=data.repaidMap.repayment_time.slice(4,6)+"-"+data.repaidMap.repayment_time.slice(6,8);//最后还款日
                $("#redetailTerm").empty().html(periods+'天');//借款期限
                $("#repayTime").empty().html(repayment_time);
                $("#redetailDate").empty().html(loanTime);
                $("#finalMoney").empty().html(allMoney);
                $("#mustRepayMoney").empty().html(derateAllMoney);
                $("#borrowMoney").empty().html(amount);
                $("#addMoney").empty().html(fee);
                $("#overdue_days").empty().html("逾期费用(已逾期"+overdue_days+"天)");
                $("#overdueMoney").empty().html(overdue_fee);
                if(derate_amount){$("#deratingMoney").empty().html("-"+derate_amount+"元")}else{$("#deratingMoneyParent").hide()}
                if(derate_amount){$("#derateAllMoney").empty().html(derateAllMoney+"元")}else{$("#derateAllMoneyParent").hide()}

            }
        if(data.alreadyRepidList){
            var alreadyRepidList=data.alreadyRepidList;
            var html="";
            for(var i=0;i<alreadyRepidList.length;i++){
                var getTime=alreadyRepidList[i].repayment_time.slice(0,4)+'-'+alreadyRepidList[i].repayment_time.slice(4,6)+"-"+alreadyRepidList[i].repayment_time.slice(6,8);
                html+='<div class="repaymentContentFooterInfo"><div class="repaymentContentFooterInfoContent">' +
                    '<p>'+getTime+'</p><p>还款日期</p></div>' +
                    '<div class="repaymentContentFooterInfoContent">' +
                    '<p>'+alreadyRepidList[i].allMoney+'</p><p>还款金额</p></div>' +
                    '<div class="repaymentContentFooterInfoContent">' +
                    '<p>'+alreadyRepidList[i].periods+'天</p><p>借款期限</p></div></div>';
            }
            $(".repaymentContentFooter").append(html);
        }else{$(".repaymentContentFooter").hide();}
    });
    Comm.ajaxPost("/wechat/repay/getRepayInfo","",function (sms) {
        openMsg=sms.retData;
    })
}
$("#checkTips").on("click",function () {
    $.modal({
        title: "逾期还款",
        text: openMsg,
        buttons: [
            { text: "我知道了"}
        ]
    });
})
/**立即还款**/
function goToRepayMent() {
    var mustRepayMoney=$("#mustRepayMoney").html();
    if(Number(mustRepayMoney)<=0){
        layer.open({content: '没有待还款账单',skin: 'msg',time: 2 });
        return;
    }else{
        window.location=_ctx+"/wechat/repay/toRepayPage?money="+mustRepayMoney;
    }
}
/**我的页面接口**/
function getMinePage() {
    Comm.ajaxPost("/wechat/customer/getCustomer","",function (sms) {
            var retData=sms.retData;
            if(retData){
                var customerMap=retData.customerMap;
                var messageMap=retData.messageMap;
                if(customerMap){
                    var is_identity=customerMap.is_identity;//是否实名认证
                    var person_name=customerMap.person_name;//真实姓名
                    var userPhone=customerMap.phone;//手机号
                    var red_money=customerMap.red_money;//红包金额
                    var repay_money=customerMap.repay_money;//待放款金额
                    is_identity=="1"?$(".relName").show():$(".relName").hide();
                    person_name!=""?$("#relName").html(person_name):$("#relName").html(userPhone);
                    red_money==""?red_money=0:red_money=Number(red_money);//红包
                    repay_money==""?repay_money=0:repay_money=Number(repay_money);//应付款
                    $("#redMoney").val(red_money);
                    $("#repayMoney").val(repay_money);
                    red_money+repay_money==0?$(".withdrawMoney").html("0.00"): $(".withdrawMoney").html((Number(red_money)+Number(repay_money)).toFixed(2));
                    repay_money==0?$("#wechat-forlending").html("0.00"):$("#wechat-forlending").html(repay_money);
                }
                if(messageMap){
                    var count=messageMap.count;
                    count<=0?$("#msgCount").hide():$("#msgCount").show().html(count);
                }
            }
    });
    Comm.ajaxPost('/wechat/oss/getPage','',function (data) {
        var retData=data.retData;
        var host=retData.host;
        if(retData.list){
            var head_img=retData.list[0].head_img;
            $("#wechat-headimg").attr("src",host+_ctx+head_img);
        }
    });
    $("#inviteFriend").on("click",function () {
        wx.onMenuShareAppMessage({
            title: '秒付金服', // 分享标题
            desc: '秒付金服,随意贷款,想怎么贷就怎么贷,没利息,不用还.当然这肯定是骗人的.要还利息贼高', // 分享描述
            link: 'xiahaiyang.nat300.top', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
            imgUrl: 'xiahaiyang.nat300.top/wechat_file/customer_order_img/0cd80ac4-f019-4c33-9c31-596f96216520.jpg', // 分享图标
            type: '', // 分享类型,music、video或link，不填默认为link
            dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
            success: function () {
                // 用户确认分享后执行的回调函数
            },
            cancel: function () {
                // 用户取消分享后执行的回调函数
            }
        });
        wx.onMenuShareTimeline({
            title: '12312', // 分享标题
            link: 'xiahaiyang.nat300.top', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
            imgUrl: 'xiahaiyang.nat300.top/wechat_file/customer_order_img/0cd80ac4-f019-4c33-9c31-596f96216520.jpg', // 分享图标
            success: function () {
                // 用户确认分享后执行的回调函数
            },
            cancel: function () {
                // 用户取消分享后执行的回调函数
            }
        });
        wx.onMenuShareQQ({
            title: '1231', // 分享标题
            desc: '13123', // 分享描述
            link: 'xiahaiyang.nat300.top', // 分享链接
            imgUrl: 'xiahaiyang.nat300.top/wechat_file/customer_order_img/0cd80ac4-f019-4c33-9c31-596f96216520.jpg', // 分享图标
            success: function () {
                // 用户确认分享后执行的回调函数
            },
            cancel: function () {
                // 用户取消分享后执行的回调函数
            }
        });
    });
}
function withdraw(){
    var amount = $(".withdrawMoney").html();
    var redMoney=$("#redMoney").val();
    var repayMoney=$("#repayMoney").val();
    window.location=_ctx+'/wechat/withdrawal/toWithDrawal?amount='+amount+"&&redMoney="+redMoney+"&&repayMoney="+repayMoney;
}

/**立即申请按钮启用停用**/
function changeBtn(me) {
    var isAgreeType=$(me).attr("isAgree");
    if(isAgreeType=="true"){
        $(me).removeClass("icon-orhook").addClass("icon-grayhook");
        $(me).attr("isAgree","false");
        $("#currentBtn").addClass("btnDisabled").attr("disabled",true)
    }else{
        $(me).removeClass("icon-grayhook").addClass("icon-orhook");
        $(me).attr("isAgree","true");
        $("#currentBtn").removeClass("btnDisabled").attr("disabled",false)
    }
}
/**查看协议**/
function checkRegister(code) {
    window.location=_ctx+"/wechat/register/goToCheckRegister?code="+code
}
