var checked = false;
$(function () {
    FastClick.attach(document.body);
    var orderId =$("#orderId").val();
    Comm.ajaxPost("/repay/getRepayDetailList","orderId="+orderId,function(data){
        var retData = data.retData;
        console.log(retData);
        var merchantName = retData.merchantName;//商户名称
        $("#merchantName").text(merchantName);
        var merchandiseBrand = retData.merchandiseBrand;//商品brand
        $("#merchandiseBrand").text(merchandiseBrand);
        var merchandiseModel = retData.merchandiseModel;//商品model
        $("#merchandiseModel").text(merchandiseModel);
        var merchandiseVersion = retData.merchandiseVersion;//商品version
        $("#merchandiseVersion").text(merchandiseVersion);
        var url = retData.merchandiseUrl;//商品图片
        $("#url").attr("src",url);
        var repaymentAmount = retData.avgAmount;//每期应还
        var periods = retData.periods;//总期数
        var divideMoney = "￥"+repaymentAmount+"x"+periods+"期";
        $("#divideMoney").text(divideMoney);
        var alreadyNum = retData.alreadyNum;//已还期数
        var dividePeriods = "已还"+alreadyNum+"/"+periods+"期"
        $("#dividePeriods").text(dividePeriods);
        var bankName = retData.bankName;//银行
        var bankCard = retData.bankCard;//银行卡
        bankCard = bankCard.substring(bankCard.length-4,bankCard.length);
        var bank = bankName+"("+bankCard+")";
        $("#bank").text(bank);
        var allMoney = retData.allMoney;//还款总额
        $("#allMoney").text("￥0.00");
        var waitPayList = retData.waitPayMap;//本期应还数据
        if(waitPayList && waitPayList.length>0){
            var  waitPayMoney = parseFloat(retData.waitPayMoney);  //当期待还
            var html = '<div class="underline"><span class="refund_choose_one">当期待还</span>' +
                '<span class="refund_choose_two">共计</span><span id="waitMoney">￥'+waitPayMoney.toFixed(2)+'</span></div>';
            for(var i=0;i<waitPayList.length;i++){
                var waitPayMap = waitPayList[i];
                var waitPayTime = waitPayMap.payTime;
                waitPayTime = waitPayTime.substring(0,4)+"."+waitPayTime.substring(4,6)+"."+waitPayTime.substring(6,8);
                html += '<div data-id="'+waitPayMap.waitpayId+'" class="under_line" style="border:transparent;" onclick="selectTheNow(this)">' +
                    '<img src="'+_ctx+'/resources/merch-wechat/images/check1.png" alt="" style="top:33px;" class="check_one">'+
                    '<div class="R_text"><p>第'+waitPayMap.payCount+'期</p>'+
                    '<p class="P_text"><span>应还款</span><span class="time repaymentAmount" style="padding-left: 30%;">￥'+waitPayMap.repaymentAmount+'</span>'+
                    '</p><p class="P_text"><span>服务包</span><span class="time waitPcgAmount" style="padding-left: 30%;">￥'+waitPayMap.waitPcgAmount+'</span>'+
                    '</p><p class="P_text"><span>到期时间</span><span class="time" style="padding-left: 26%;">'+waitPayTime+'</span>'+
                    '</p></div></div>';
            }
            $("#nowMoney").html(html);
            $("#nowMoney").show();
        }
        /*var overMoney = retData.overMoney;//逾期金额
         $("#overMoney").text(overMoney);*/
        var overAllMoney = retData.overAllMoney;
        var overList = retData.overList;//逾期list
        var redList = retData.redList;//抵扣
        if(redList.length != 0){
            $('#voucherDom').show();
            var contentDom = document.getElementById('content');
            contentDom.innerHTML = "";
            redList.forEach(function (item) {
                contentDom.innerHTML+='<div data-money="'+item.money+'" data-id="'+item.id+'" onclick="_addActive(this)" class="content-item">'+
                '<span class="text">抵扣券-'+item.money+'元</span>'+
                '<span class="icon-circle"></span>'+
                    '</div>'
            })
        }
        if(overList && overList.length>0){
            var html = '<div class="underline">'+
                '<span class="refund_choose_one">逾期待还</span>'+
                '<span class="refund_choose_two">共计</span>'+
                '<span id="overMoney">￥'+overAllMoney+'</span>'+
                '</div>';
            console.log(overList);
            for(var i=0;i<overList.length;i++){
                var overPayCount = overList[i].payCount;//期数
                var overPenalty = overList[i].penalty;//逾期利息
                var overRepaymentAmount = overList[i].repaymentAmount;//应还金额
                var overPcgAmount = overList[i].overPcgAmount;//服务包
                var overMoney = overList[i].overMoney;//总金额
                var overId = overList[i].overId;//逾期还款id
                var defaultInterest = overList[i].defaultInterest;//罚息
                var deRateAmount = overList[i].deRateAmount;//减免金额
                html +='<div data-id="'+overId+'" class="under_line" onclick="selectTheOver(this)">'+
                    '<img src="'+_ctx+'/resources/merch-wechat/images/check2.png" alt=""  style="top:33px;" class="check_one">'+
                    '<div class="R_text">'+
                    '<p><span class="periodsDom">第'+overPayCount+'期</span><span class="overMoney">共计 ￥'+overMoney+'</span></p>'+
                    '<p class="P_text">'+
                    '<span>应还款</span>'+
                    '<span class="time" style="padding-left: 30%;">￥'+overRepaymentAmount+'</span>'+
                    '</p>'+
                    '<p class="P_text">'+
                    '<span>服务包</span>'+
                    '<span class="time" style="padding-left: 30%;">￥'+overPcgAmount+'</span>'+
                    '</p>'+
                    '<p class="P_text">'+
                    '<span>逾期利息</span>'+
                    '<span class="time" style="padding-left: 24%;">￥'+overPenalty+'</span>'+
                    '</p>';
                    if(defaultInterest){
                        html +='<p class="P_text">'+
                        '<span>罚息</span>'+
                        '<span class="time" style="padding-left: 24%;">￥'+defaultInterest+'</span>'+
                        '</p>';
                    }

                    if (deRateAmount){
                        html += '<p class="P_text">'+
                        '<span>减免</span>'+
                        '<span class="time" style="padding-left: 24%;">￥'+deRateAmount+'</span>'+
                        '</p>';
                    }
                    html +='</div>'+
                    '</div>';
            }
            $("#over").html(html);
            $("#over").show();
        }
    });


});

function selectTheNow(me) {
    if ($(me).hasClass("active"))
    {
        $(me).find("img").attr('src','../../resources/merch-wechat/images/check1.png');
        $(me).removeClass("active");
    }
    else
    {
        $(me).find("img").attr('src','../../resources/merch-wechat/images/checked.png');
        $(me).addClass("active");

        $('#over .under_line').attr('class', 'active under_line');
        $('#over .under_line img').attr('src','../../resources/merch-wechat/images/checked.png');
    }

    totalMoney();
}

function tiqianjieqing(){
    var orderId = $("#orderId").val();
    Comm.ajaxPost("/repay/getSettleAuth","orderId="+orderId,function(data){
        console.log(data);
        var data = data.retData;
        var state = data.state;
        if (state == "2"){
            window.location = _ctx+"/wechat/page/toRefundOne?orderId="+orderId;
        }else if (state == "1"){
            $.modal({
                title: "提前结清",
                text: "确认提前结清，立即拨打客服电话为您开启结清通道。",
                buttons: [
                    { text: "暂不结清", onClick: function(){ console.log(1)} },
                    { text: "联系客服", onClick: function(){ window.location.href = 'tel://'+data.tel} }
                ]
            });
        }else if(state == "0"){
            debugger;
            var servicePackage = data.servicePackage;
            var html = '<p style="text-align:left;font-size:12px;">您未购买提前结清服务包!</p>';
            var month = "";
            if(servicePackage.length!=0){
                html = '<p style="text-align:left;font-size:12px;">您购买的服务包为:</p>';
                servicePackage.forEach(function (item){
                    if(item.type == '1'){
                        html += '<p style="text-align: left;font-size:14px;"><span>'+item.packageName+'</span><span style="float:right">'+item.amount+'元</span></p>'
                    }else{
                        html += '<p style="text-align: left;font-size:14px;"><span>'+item.packageName+'</span><span style="float:right">'+item.amount+'元/月</span></p>'
                    }
                    if (item.month)
                    {
                        if (!month || parseInt(item.month) > parseInt(month))
                        {
                            month = parseInt(item.month);
                        }
                    }

                })
                if (month)
                {
                    html += '<p style="text-align: left;font-size:14px;margin-top:6px;">第'+month+'期即可提前结清分期账单。</p>';
                }
            }
            $.modal({
                title: "提前结清",
                text: "<div style='font-size:12px;text-align:left;margin-bottom:10px;'>很遗憾，您暂未拥有提前结清的权限。</div>"+html,
                buttons: [
                    { text: "知道了", onClick: function(){ console.log(1)} },
                    { text: "联系客服", onClick: function(){ window.location.href = 'tel://'+data.tel} }
                ]
            });
        }
    });


}

function selected() {
    $('#nowMoney img').attr('src','../../resources/merch-wechat/images/checked.png')
    checked = true;
}
function cancel() {
    $('#nowMoney img').attr('src','../../resources/merch-wechat/images/check1.png');
    checked = false;
}

function cancelAll() {
    var nowChildren = $("#nowMoney").find(".active");
    if (nowChildren.length > 0)
    {
        for (var j = 0; j < nowChildren.length; j++)
        {
            nowChildren.eq(j).find("img").attr('src','../../resources/merch-wechat/images/check1.png');
            nowChildren.eq(j).removeClass("active");
        }
    }
}
function selectTheOver(me) {
    var periods = me.getElementsByClassName('periodsDom')[0].innerHTML;
    var className = me.className;
    periods = parseInt(periods.substring(1,2));
    var brothers = ($(me).siblings());
    for(var i = 0,len=brothers.length;i<len;i++){
        if(i!=0){
            var brother = brothers[i];
            var broPeriods = parseInt(brother.getElementsByClassName('periodsDom')[0].innerHTML.substring(1,2));
            if(className.indexOf('active')>=0){
                if (broPeriods >= periods) {
                    me.setAttribute('class', 'under_line');
                    me.getElementsByTagName('img')[0].setAttribute('src', '../../resources/merch-wechat/images/check2.png');
                    brother.setAttribute('class', 'under_line');
                    brother.getElementsByTagName('img')[0].setAttribute('src', '../../resources/merch-wechat/images/check2.png');
                }else{
                    me.setAttribute('class', 'under_line');
                    me.getElementsByTagName('img')[0].setAttribute('src', '../../resources/merch-wechat/images/check2.png');
                }
                cancelAll();
            }else{
                if (broPeriods <= periods) {
                    me.setAttribute('class', 'active under_line');
                    me.getElementsByTagName('img')[0].setAttribute('src', '../../resources/merch-wechat/images/checked.png');
                    brother.setAttribute('class', 'active under_line');
                    brother.getElementsByTagName('img')[0].setAttribute('src', '../../resources/merch-wechat/images/checked.png');
                }else{
                    me.setAttribute('class', 'active under_line');
                    me.getElementsByTagName('img')[0].setAttribute('src', '../../resources/merch-wechat/images/checked.png');
                }
            }
        }else{
            var className = me.className;
            if(me.className.indexOf('active')>=0){
                me.setAttribute('class', 'under_line');
                me.getElementsByTagName('img')[0].setAttribute('src', '../../resources/merch-wechat/images/check2.png');
                cancelAll();
            }else{
                me.setAttribute('class', 'active under_line');
                me.getElementsByTagName('img')[0].setAttribute('src', '../../resources/merch-wechat/images/checked.png');
            }
        }
    }
    totalMoney();
}

var vouFlag = false;
function totalMoney(){
    vouFlag = false;
    var nowMoney = 0;
    var nowChildren = $("#nowMoney").find(".active");
    if (nowChildren.length > 0)
    {
        for (var j = 0; j < nowChildren.length; j++)
        {
            var repaymentAmount = nowChildren.eq(j).find(".repaymentAmount").text().replace("￥", "");
            var waitPcgAmount = nowChildren.eq(j).find(".waitPcgAmount").text().replace("￥", "");
            nowMoney += parseFloat(repaymentAmount) + parseFloat(waitPcgAmount);
        }
    }

    var overdueMoney = 0;
    var children = document.getElementById('over').children;
    var allMoney = 0;
    for(var i=0,len=children.length;i<len;i++){
        if(i!=0){
            var child = children[i];
            if(child.className.indexOf('active')>=0){
                var overMoney = parseFloat(child.getElementsByClassName('overMoney')[0].innerHTML.replace(/[共计￥]+/g,''));
                console.log(overMoney);
                allMoney += overMoney;
            }
        }
    }
    var totalMoney = (allMoney + nowMoney).toFixed(2);
    var vouMoney = $("#vouMoney").val();
    if (vouMoney){
        if ((totalMoney-vouMoney)>=0){
            totalMoney = totalMoney-vouMoney;
        }
        else
        {
            totalMoney = 0.00;
            vouFlag = true;
        }
    }
    $("#allMoney").text("￥"+parseFloat(totalMoney).toFixed(2));
}

//获取所有选中还款的id
function getAllSeletedRepayId(){
    var repaymentId = "";
    var nowChildren = $("#nowMoney").find(".active");
    if (nowChildren.length > 0)
    {
        for (var j = 0; j < nowChildren.length; j++)
        {
            if (repaymentId)
            {
                repaymentId += "," + nowChildren.eq(j).attr("data-id");
            }
            else
            {
                repaymentId = nowChildren.eq(j).attr("data-id");
            }
        }
    }
    var repayIds = "";
    var children = document.getElementById('over').children;
    for(var i=0,len=children.length;i<len;i++){
        if(i!=0){
            var child = children[i];
            if(child.className.indexOf('active')>=0){
                repayIds += child.getAttribute("data-id")+","
            }
        }
    }

    if (repaymentId){
        repayIds +=repaymentId;
    }else {
        repayIds = repayIds.substring(0,repayIds.length-1);
    }
    return repayIds;
}

//确认还款
function confirmPay(){
    if (vouFlag)
    {
        $.confirm("抵扣券金额大于支付金额，是否继续支付?", function() {
            confirmPayMoney();
        }, function(){});
    }else {
        confirmPayMoney();
    }
}

function confirmPayMoney()
{
    var repayIds = getAllSeletedRepayId();
    var redIds = $("#idJson").val();
    console.log(repayIds);
    $.confirm("确认立即支付吗?", function() {
        //点击确认后的回调函数
        if (repayIds){
            Comm.ajaxPost("/repay/confirmPayByIds","repayIds="+repayIds+"&&redIds="+redIds,function(data){
                var state = data.retData.respCode;
                var msg = data.retData.msg;
                if (state == "0000" || state=="BF00114"){
                    var amount = $("#allMoney").text();
                    if(amount){
                        amount=amount.replace("￥","");
                    }
                    var orderId = $("#orderId").val();
                    layer.open({content: '支付成功',skin: 'msg',time: 2 ,end:function () {
                        window.location=_ctx+"/wechat/page/toRepaySuccess?orderId="+orderId+"&amount="+amount;
                    }});
                }else if (state == "BF00100" || state == "BF00112" || state == "BF00113"
                    || state == "BF00115" || state == "BF00144" || state == "BF00202"){
                    layer.open({content: '支付确认中,请等待.',skin: 'msg',time: 2 });
                }else {
                    layer.open({content: (msg ? msg : "支付失败"),skin: 'msg',time: 2 });
                }
            });
            linked.agreeContract();
        }else{
            layer.open({content: '请勾先勾选还款计划',skin: 'msg',time: 2 });
            return ;
        }
    }, function() {
        //点击取消后的回调函数
    });
}

//抵扣券
function lala(){
    var contentDom = document.getElementById('content');
    var voucher = document.getElementById('voucher'),
        vouMoney = document.getElementById('vouMoney'),
        idJson = document.getElementById('idJson');
    var children = contentDom.children;
    var allMoney = 0;
    var idJsonStr = "";
    for(var i=0,len=children.length;i<len;i++){
        var child = children[i];
        if(child.className.indexOf('active')>=0){
            allMoney += parseFloat(child.getAttribute('data-money'));
            idJsonStr += child.getAttribute('data-id')+',';
        }
    }
    idJsonStr==""?idJsonStr = idJsonStr:idJsonStr=idJsonStr.substr(0,idJsonStr.length-1);
    voucher.innerText = '-￥'+allMoney;
    vouMoney.value = allMoney;
    idJson.value = idJsonStr;
    $.closePopup()
    totalMoney();
}
function _addActive(me) {
    var child = me.getElementsByTagName('span')[1];
    if(me.className.indexOf('active')>=0){
        me.setAttribute('class','content-item');
        child.setAttribute('class','icon-circle');
    }else{
        me.setAttribute('class','content-item active');
        child.setAttribute('class','icon-orhook');
    }
}




