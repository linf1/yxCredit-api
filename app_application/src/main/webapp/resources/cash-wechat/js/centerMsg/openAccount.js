$(function () {
    $("[name=bankCity]").cityPicker({
        title: "请选择省市",
        showDistrict: false
    });
    $("#kaihu-btn").on('click',function () {
        var isValidate=checkPersonInfo();
        if(isValidate){
            cardConfirm();
        }
    });
    $(".getCodeBtn").on("click",function () {
        var isValidate=checkPersonInfo("1");
        if(isValidate){
            updataPersonInfo();
        }
    })
    getUserOpenInfo();
})
/**获取开户用户信息**/
function getUserOpenInfo() {
    Comm.ajaxPost("/wechat/ybCard/getOpenAccount","",function (sms) {
        var retData=sms.retData;
        console.log(retData);
        if(retData){
            $("#person_name").html(retData.person_name);
            $("#card").html(retData.card);
            $("input[name=cardNum]").html(retData.bank_card);
            $("#is_openAccount").val(retData.is_openAccount);//1是开过户 0未开户
            $("#userTel").val(retData.tel);
            var str='<option value="">请选择</option>';
            for (var i = 0;i < retData.bankNameList.length;i++){
                str +='<option value="'+retData.bankNameList[i].value+'">'+retData.bankNameList[i].text+'</option>';
            }
            $("[name=cardBankName]").html(str);
        };
    })
}
function checkPersonInfo(msg) {
    var bankName=$("[name='cardBankName']").val();//银行名称

    var cardNum=$("[name='cardNum']").val();//银行卡号

    var tel=$("[name='phoneNum']").val();//银行预留手机号

    var phoneCode=$("[name='phoneCode']").val();//手机验证码

    //var bankCity=$("[name='bankCity']").val();//银行省市
    //var cardSubBankName=$("[name='cardSubBankName']").val();//支行
    //var phoneName=$("[name=phoneName]").val();
    //var passlogin=$("[name=passlogin]").val();
    //var passtixian=$("[name=passtixian]").val();

    var isPhoneReg=/^1[34578]\d{9}$/;
    //var isPwdReg=/^[0-9a-zA-Z]{6,20}$/;
    var isCardReg=/\d{15}|\d{19}/;
    var wordReg=/^([a-zA-Z0-9\u4e00-\u9fa5\·]{1,50})$/;


    if(!bankName){
        layer.open({content: '请选择开户银行',skin: 'msg',time: 2 });
        return false;
    }

    if(!cardNum){
        layer.open({content: '银行卡号不能为空',skin: 'msg',time: 2 });
        return false;
    }else if(!isCardReg.test(cardNum)){
        layer.open({content: '银行卡号格式不正确',skin: 'msg',time: 2 });
        return false;
    }

    if(!tel){
        layer.open({content: '银行预留手机号码不能为空',skin: 'msg',time: 2 });
        return false;
    }else if(!isPhoneReg.test(tel)){
        layer.open({content: '手机号码格式不正确',skin: 'msg',time: 2 });
        return false;
    }
    if(msg!="1"){
        if(!phoneCode){
            layer.open({content: '手机验证码不能为空',skin: 'msg',time: 2 });
            return false;
        }
    }
    // if(!bankCity){
    //     layer.open({content: '开户省市不能为空',skin: 'msg',time: 2 });
    //     return false;
    // }
    // if(!cardSubBankName){
    //     layer.open({content: '开户支行名称不能为空',skin: 'msg',time: 2 });
    //     return false;
    // }else if(!wordReg.test(cardSubBankName)){
    //     layer.open({content: '开户支行名称格式不正确',skin: 'msg',time: 2 });
    //     return false;
    // }
    /*if(!bankName){
        layer.open({content: '开户行名称不能为空',skin: 'msg',time: 2 });
        return false;
    }

    if(!phoneName){
        layer.open({content: '手机号码不能为空',skin: 'msg',time: 2 });
        return false;
    }else if(!isPhoneReg.test(phoneName)){
        layer.open({content: '手机号码格式不正确',skin: 'msg',time: 2 });
        return false;
    }
    if(!passlogin){
        layer.open({content: '登录密码不能为空',skin: 'msg',time: 2 });
        return false;
    }else if(!isPwdReg.test(passlogin)){
        layer.open({content: '登录密码格式不正确',skin: 'msg',time: 2 });
        return false;
    }
    if(!passtixian){
        layer.open({content: '提现密码不能为空',skin: 'msg',time: 2 });
        return false;
    }else if(!isPwdReg.test(passtixian)){
        layer.open({content: '提现密码格式不正确',skin: 'msg',time: 2 });
        return false;
    }*/
    return true;
}
/**开户提交**/
function updataPersonInfo() {
    var is_openAccount=$("#is_openAccount").val();
    if(is_openAccount=="1"){//1是开过户 0未开户
        layer.open({content:'已经开过户,请跳过次项操作!',skin:'msg',time:2});
    }else{
        firstMethod();
    }
}
/**第一次调开户接口**/
function firstMethod() {
    whir.loading.add("", 1);
    var cardNo =  $("[name='cardNum']").val();
    var tel=$("[name='phoneNum']").val();
    Comm.ajaxPost('/wechat/ybCard/wechat/bindingCard', {tel:tel,cardno:cardNo}, function (response) {
            var data = response.retData;
            if(data.status=='FAIL' || data.status=='BIND_FAIL' || data.status=='TIME_OUT' || data.status=='0'){
                $.alert(data.msg, "消息");
                return
            }else if(data.status == 'BIND_ERROR'){
                bindError();
                return
            }else if(data.status == 'TO_VALIDATE'){
                layer.open({content:'短信验证码已下发,请输入',skin:'msg',time:2,end:function () {
                    $(".getCodeBtn").attr("disabled",true);
                    var times=120;
                    var registerTimer=setInterval(function () {
                        if(times<=0){
                            clearInterval(registerTimer);
                            $(".getCodeBtn").removeAttr("disabled");
                            $(".getCodeBtn").html("获取验证码");
                        }else{
                            times--;
                            $(".getCodeBtn").html("("+times+")后重新发送");
                        }
                    },1000)
                }});
            }
        }
    )
}
function bindError() {
    whir.loading.add("", 1);
    Comm.ajaxPost('/wechat/ybCard/bindingCardResend', '', function (response) {
            var data = response.retData;
            if(data.status=='FAIL' || data.status=='TIME_OUT'){
                $.alert(data.msg, "消息");
                return
            }else if(data.status=='TO_VALIDATE'){
                layer.open({content:'短信验证码已下发,请输入',skin:'msg',time:2})
            }
        }
    )
}
function cardConfirm() {
    var params = {};
    params.accountPcaddr=$("[name='cardBankName']").val();//银行名称
    params.cardNo=$("[name='cardNum']").val();//银行卡号
    params.tel=$("[name='phoneNum']").val();//银行预留手机号
    params.smsCode=$("[name='phoneCode']").val();//手机验证码
    Comm.ajaxPost("/wechat/ybCard/bindingCardConfirm",params,function (sms) {
        var data = sms.retData;
        if(data.status == 'FAIL'){
            CardCheck(params.cardNo);
            return
        }else if(data.status == 'BIND_ERROR'){
            firstMethod();
            return
        }else if(data.status == 'BIND_FAIL'){
            $.alert(data.msg,'消息');
            return
        }else if(data.status == 'BIND_SUCCESS'){
            layer.open({content:'绑卡成功',skin:'msg',time:2,end:function () {
                history.go(-1);
            }});
        }
    });
}
function CardCheck(cardNo) {
    whir.loading.add("", 1);
    Comm.ajaxPost('/wechat/ybCard/bindingCardCheck', "cardNo="+cardNo, function (response) {
            var data = response.retData;
            if(data.retCode == "SUCCESS"){
                layer.open({content:'已开户',skin:'msg',time:2});
            }else{
                layer.open({content:'未开户',skin:'msg',time:2});
            }
        }
    )
}