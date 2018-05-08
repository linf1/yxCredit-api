$(function() {
    $("#codeText").bind("click", getPhoneCode);
    $("#bindingCard").bind("click", bindingCard);
    Comm.ajaxPost('/payTreasure/getOpenAccountInfo',null,function (response) {
        if(response.retCode == "SUCCESS"){
            var data = response.retData;
            var bankNameList = data.bankNameList;
            if (bankNameList)
            {
                var bankHtml = '<option value="">请选择</option>';
                bankNameList.forEach(function (item) {
                    bankHtml += '<option value="'+item.key+'">'+item.value+'</option>';
                });
                $("#banks").html(bankHtml);
            }
            $("#accountTypeDiv").text("宝付账户");
            $("#nameDiv").text(data.userName);
            $("#idNoDiv").text(data.IdNo);
        }
    })
});

function getPhoneCode(){
    var reg=/^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1})|(17[0-9]{1}))+\d{8})$/;
    var bankReg = /^\d+$/;
    var smsCode = $("#phoneCode").val(),
        tel = $("#phone").val(),
        accountBankName = $("#banks").find("option:selected").text(),
        cardNo = $("#bankCard").val();//银行卡号
    var name = $("#nameDiv").text(),
        idNo = $("#idNoDiv").text(),
        bankCode = $("#banks").val();
    if(!accountBankName){
        layer.open({content:'开户银行不能为空',skin:'msg',time:2});
        return
    }
    if(!cardNo){
        layer.open({content:'银行卡号不能为空',skin:'msg',time:2});
        return
    }else if(!bankReg.test(cardNo)){
        layer.open({content:'银行卡号只能输入数字',skin:'msg',time:2});
        return
    }
    if(!tel){
        layer.open({content:'银行预留手机号不能为空',skin:'msg',time:2});
        return
    }else if(!reg.test(tel)){
        layer.open({content:'银行预留手机号格式错误',skin:'msg',time:2});
        return
    }
    var seconds = 120;//msg.msg.substr(7,3);
    var timer = setInterval(function () {
        seconds = seconds - 1;
        $("#codeText").text('验证码'+'('+seconds+')'+'s');
        $("#codeText").unbind("click", getPhoneCode);
        if(seconds == 0){
            clearInterval(timer);
            $("#codeText").text('重新获取');
            $("#codeText").bind("click", getPhoneCode);
        }
    },1000);
    sendCodeMessage(name,idNo,cardNo,accountBankName,bankCode,tel);
}

var transId = null;
function sendCodeMessage(name,idNo,cardNo,accountBankName,bankCode,tel)
{
    Comm.ajaxPost(
        '/payTreasure/bindingCard',
        {name:name,idNo:idNo,cardNo:cardNo,bankName:accountBankName,bankCode:bankCode,tel:tel},
        function (response) {
            var data = response.retData;
            var respCode = data.respCode;
            if(respCode == '0000' || respCode == 'BF00114'){
                transId = data.transId;
                layer.open({content:'短信验证码已下发',skin:'msg',time:2});
                return
            }else if(respCode == 'over'){
                layer.open({content:data.msg,skin:'msg',time:2,end:function () {
                    linked.backMyAction();
                }});
            }else{
                layer.open({content:data.msg,skin:'msg',time:2});
            }
        }
    )
}

function bindingCard(){
    var reg=/^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1})|(17[0-9]{1}))+\d{8})$/;
    var smsCode = $("#phoneCode").val(),
        tel = $("#phone").val(),
        accountBankName = $("#banks").find("option:selected").text(),
        name = $("#nameDiv").text(),
        idNo = $("#idNoDiv").text(),
        bankCode = $("#banks").val(),
        cardNo = $("#bankCard").val();//银行卡号
    if(accountBankName == ""){
        layer.open({content:'开户银行不能为空',skin:'msg',time:2});
        return
    }
    if(cardNo == ""){
        layer.open({content:'银行卡号不能为空',skin:'msg',time:2});
        return
    }
    if(tel == ""){
        layer.open({content:'银行预留手机号不能为空',skin:'msg',time:2});
        return
    }else if(!reg.test(tel)){
        layer.open({content:'银行预留手机号格式错误',skin:'msg',time:2});
        return
    }
    if(smsCode == ""){
        layer.open({content:'短信验证码不能为空',skin:'msg',time:2});
        return
    }
    Comm.ajaxPost(
        '/payTreasure/bindingCardConfirm',
        {
            name:name,
            idNo:idNo,
            cardNo:cardNo,
            bankName:accountBankName,
            bankCode:bankCode,
            tel:tel,
            smsCode:smsCode,
            transId:transId
        },
        function (response) {
            var data = response.retData;
            var respCode = data.respCode;
            if(respCode == '0000' || respCode =='BF00114'){
                layer.open({content:'绑卡成功',skin:'msg',time:2,end:function () {
                    linked.backMyAction();
                }});
                return
            }else {
                layer.open({content:data.msg,skin:'msg',time:2});
            }
        }
    )
}