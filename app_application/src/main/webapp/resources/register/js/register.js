$("#changeEye").on("click",function () {
    if($(this).hasClass("icon-eye")){
        $(this).removeClass("icon-eye").addClass("icon-eyeopen");
        $("#userPhonePwd").attr("type","text");
    }else{
        $(this).removeClass("icon-eyeopen").addClass("icon-eye");
        $("#userPhonePwd").attr("type","password");
    }
})
/**获取验证码**/
$("#getCode").on("click",function () {
    var isChecked=checkInfo("1");
    if(isChecked){
        getCode();
    }
})
/**请求验证码**/
function getCode() {
    var username=$("#userPhoneNum").val();//手机号
    Comm.ajaxPost('/reg/getSmsCode', {phone:username,type:'reg'},
        function (response){
            layer.open({content: response.retMsg, skin: 'msg', time: 2 });
            var seconds = "120";//msg.msg.substr(7,3);
            var timer = setInterval(function () {
                seconds = seconds - 1;
                $("#getCode").html('验证码'+'('+seconds+')'+'s');
                $("#getCode").attr("disabled",true);
                if(seconds == 0){
                    clearInterval(timer);
                    $("#getCode").html('重新获取');
                    $("#getCode").removeAttr("disabled");
                }
            },1000);
        }
    )
}
/**提交注册**/
$("#registerBtn").on("click",function () {
    var isChecked=checkInfo();
    if(isChecked){
        confirm();
    }
})
/**确认提交**/
function confirm() {
    var userPhone=$("#userPhoneNum").val();
    var userPwd=$("#userPhonePwd").val();
    var phoneNum=$("#userPhoneCode").val();
    var referenceId=$("#referenceId").val();
    Comm.ajaxPost("/reg/shareReg","referenceId="+referenceId+"&phone="+userPhone+"&black_box="+"&type="+"&password="+userPwd+"&smsCode="+phoneNum+"&registration_id="+"&onlycode=",function (data) {
        var retCode = data.retCode;
        if(retCode == "SUCCESS"){
            layer.open({content: "注册成功" ,skin: 'msg',time: 2});
            window.location=_ctx+"/share"
        }else{
            layer.open({content: data.retMsg ,skin: 'msg',time: 2});
        }
    })
}
/**验证信息**/
function checkInfo(msg) {
    var userPhoneNum=$("#userPhoneNum").val();//手机号
    var userPhonePwd=$("#userPhonePwd").val();//密码
    var userPhoneCode=$("#userPhoneCode").val();//验证码
    var isPhoneReg=/^1[34578]\d{9}$/;
    var isPwdReg=/^[0-9a-zA-Z]{6,20}$/;
    var isNum=/\d/;
    if(!userPhoneNum){
        layer.open({content: '手机号不能为空',skin: 'msg',time: 2 });
        return false;
    }else if(!isPhoneReg.test(userPhoneNum)){
        layer.open({content: '手机号格式不正确',skin: 'msg',time: 2 });
        return false;
    }
    if(!msg){
        if(!userPhonePwd){
            layer.open({content: '密码不能为空',skin: 'msg',time: 2 });
            return false;
        }else if(!isPwdReg.test(userPhonePwd)){
            layer.open({content: '密码格式不正确',skin: 'msg',time: 2 });
            return false;
        }
        if(!userPhoneCode){
            layer.open({content: '请输入手机验证码',skin: 'msg',time: 2 });
            return false;
        }else if(!isNum.test(userPhoneCode)){
            layer.open({content: '验证码格式不正确',skin: 'msg',time: 2 });
            return false;
        }
    }
    return true;
}