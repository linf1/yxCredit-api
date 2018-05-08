/**
 * 忘记密码.
 */
var verifyCode = new GVerify("v_container");
/**下一步**/
function getNext() {
    var userPhone=$("#userPhone").val();
    var userCode=$("#userCode").val();
    var isPhoneReg=/^1[34578]\d{9}$/;
    var phoneNum=$("[name=phoneNum]").val();
    var res = verifyCode.validate(userCode);
    if(!userPhone){
        layer.open({content: '手机号不能为空',skin: 'msg',time: 2 });
        return;
    }else if(!isPhoneReg.test(userPhone)){
        layer.open({content: '手机号格式不正确',skin: 'msg',time: 2 });
        return;
    }
    if(!userCode){
        layer.open({content: '图形验证码不能为空',skin: 'msg',time: 2 });
        return;
    }else if(!res){
        layer.open({content: '图形验证码不正确',skin: 'msg',time: 2 });
        return;
    }
    if(!phoneNum){
        layer.open({content: '输入手机验证码',skin: 'msg',time: 2 });
        return;
    }
    //验证短信验证码
    var params = {};
    params.phone = userPhone;
    params.smsCode = phoneNum;
    console.log(params)
    Comm.ajaxPost("check",params,function (sms) {
        console.log(sms);
        var data=JSON.parse(sms);
        if(data.retCode == "FAIL"){
            layer.open({content:data.retMsg,skin:'msg',time:2});
        }
    });
}
function getPhoneCode(me) {
    var userPhone=$("#userPhone").val();
    var isPhoneReg=/^1[34578]\d{9}$/;
    if(!userPhone){
        layer.open({content: '手机号不能为空',skin: 'msg',time: 2 });
        return;
    }else if(!isPhoneReg.test(userPhone)){
        layer.open({content: '手机号格式不正确',skin: 'msg',time: 2 });
        return;
    }
    //获取短信验证码
    var params = {};
    var msg = "";
    params.tel = userPhone;
    Comm.ajaxPost("getSmsCode",params,function (sms) {
        console.log(sms);
         var data=JSON.parse(sms);
        msg = data.retMsg;
        if(data.retCode == "FAIL"){
            layer.open({content:msg,skin:'msg',time:2})
            return ;
        }
    });


    //layer.open({content: '验证码已发送，请注意查收!',skin: 'msg',time: 2 ,end:function () {
    layer.open({content: msg,time: 2 ,end:function () {
        $(me).attr("disabled",true);
        var times=1;
        var registerTimer=setInterval(function () {
            if(times<=0){
                clearInterval(registerTimer);
                $(me).removeAttr("disabled");
                $(me).html("获取验证码");
            }else{
                times--;
                $(me).html("("+times+")后重新发送");
            }
        },1000)
    }});
}

$("#btn-updatepwd").on("click",function(){
    var userPhone=$("#userPhone").val();
    var newpwd = $("#wechat-newpwd").val();
    var confirmpwd = $("#wechat-confirmpwd").val();
    //正则验证
    var isPwdReg=/^[0-9a-zA-Z]{6,20}$/;
    if(!newpwd || !confirmpwd){
        layer.open({content: '密码不能为空',skin: 'msg',time: 2 });
        return;
    }else if(!isPwdReg.test(newpwd) || !isPwdReg.test(confirmpwd)){
        layer.open({content: '密码格式不正确',skin: 'msg',time: 2 });
        return;
    }
    if(newpwd != confirmpwd){
        layer.open({content:"两次密码输入不一致，请重新输入",skin:"msg",time:2});
        return ;
    }
    var params = {};
    params.phone = userPhone;
    params.registration_id = "";
    params.password = confirmpwd;
    Comm.ajaxPost("forgetpwd",params,function (sms) {
        console.log(sms);
        var data=JSON.parse(sms);
        if(data.retCode == "FAIL"){
            layer.open({content:data.retMsg,skin:"msg",time:2});
            return;
        }
        window.location = "/wechat/login/tologin";
    });
})

