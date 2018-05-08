/**注册逻辑**/
/**改变input类型**/
function changeType(me) {
    var isEye=$(me).attr("isEye");
    if(isEye=="true"){
        $(me).removeClass("icon-eye").addClass("icon-eyeopen");
        $(me).attr("isEye","false");
        $(me).parent("div").prev(".weui-cell__bd").children("input").attr("type","text")
    }else{
        $(me).removeClass("icon-eyeopen").addClass("icon-eye");
        $(me).attr("isEye","true");
        $(me).parent("div").prev(".weui-cell__bd").children("input").attr("type","password")
    }
}
$(".registBtn").on("click",function () {
    var userPhone=$("#userPhone").val();
    var userPwd=$("#userPwd").val();
    var phoneNum=$("#usering").val();
    var isPhoneReg=/^1[34578]\d{9}$/;
    var isPwdReg=/^[0-9a-zA-Z]{6,20}$/;
    var isNum=/\d/;
    if(!userPhone){
        layer.open({content: '手机号不能为空',skin: 'msg',time: 2 });
        return;
    }else if(!isPhoneReg.test(userPhone)){
        layer.open({content: '手机号格式不正确',skin: 'msg',time: 2 });
        return;
    }
    if(!userPwd){
        layer.open({content: '密码不能为空',skin: 'msg',time: 2 });
        return;
    }else if(!isPwdReg.test(userPwd)){
        layer.open({content: '密码格式不正确',skin: 'msg',time: 2 });
        return;
    }
    if(!phoneNum){
        layer.open({content: '请输入手机验证码',skin: 'msg',time: 2 });
        return;
    }else if(!isNum.test(phoneNum)){
        layer.open({content: '验证码格式不正确',skin: 'msg',time: 2 });
        return;
    }

    var params = {};
    params.phone = userPhone;
    params.password = userPwd;
    params.smsCode =phoneNum;
    params.referenceId="";
    Comm.ajaxPost("/wechat/register/register",params,function (data) {
        if(data.retCode=="SUCCESS"){//注册成功
            layer.open({content: data.retMsg,skin: 'msg',time: 2 });
            delete params.smsCode;
            delete params.referenceId;
            //注册成功进行登录
            Comm.ajaxPost("/wechat/login/login",params,function (data) {
                // var data=JSON.parse(sms);
                if(data.code==1){//异常
                    layer.open({content: data.msg,skin: 'msg',time: 2 });
                }else{
                    window.location="/wechat/login/home"
                }
            });
        }else{//异常
            layer.open({content: data.retMsg,skin: 'msg',time: 2 });
        }
    })
})

/**获取验证码**/
function getSmsCode(me){
    var userPhone=$("#userPhone").val();
    var isPhoneReg=/^1[34578]\d{9}$/;
    if(!userPhone){
        layer.open({content: '手机号不能为空',skin: 'msg',time: 2 });
        return;
    }else if(!isPhoneReg.test(userPhone)){
        layer.open({content: '手机号格式不正确',skin: 'msg',time: 2 });
        return;
    }
    var params = {};
    params.phone = userPhone;
    Comm.ajaxPost("/wechat/register/getSmsCode",params,function (data) {
        if(data.retCode=="SUCCESS"){//发送成功!
            layer.open({content: '验证码已发送，请注意查收!',skin: 'msg',time: 2 ,end:function () {
                $(me).attr("disabled",true);
                var times=120;
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
            layer.open({content: data.retMsg,skin: 'msg',time: 2 });
        }else{//异常
            layer.open({content: data.msg,skin: 'msg',time: 2 });
        }
    });

}
/**注册按钮启用停用**/
function changeBtn(me) {
    var isAgreeType=$(me).attr("isAgree");
    if(isAgreeType=="true"){
        $(me).removeClass("icon-orhook").addClass("icon-grayhook");
        $(me).attr("isAgree","false");
        $(".registBtn").addClass("btnDisabled").attr("disabled",true)
    }else{
        $(me).removeClass("icon-grayhook").addClass("icon-orhook");
        $(me).attr("isAgree","true");
        $(".registBtn").removeClass("btnDisabled").attr("disabled",false)
    }
}

