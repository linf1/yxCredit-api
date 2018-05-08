/**密码显隐**/
$(".pwdBtn").on("click",function () {
    if($(this).hasClass("icon-eye")){
        $(this).removeClass("icon-eye").addClass("icon-eyeopen");
        $("[name=userPwd]").attr("type","text");
    }else{
        $(this).removeClass("icon-eyeopen").addClass("icon-eye");
        $("[name=userPwd]").attr("type","password");
    }
})
/**信息校验**/
function validationInfo(flag) {
    var userPhone=$("#userPhone").val(),//手机号码
        phoneCode=$("#phoneCode").val(),//验证码
        userPwd=$("#userPwd").val();//密码

    var isPhoneReg=/^1[34578]\d{9}$/;
    var isPwdReg=/^[0-9a-zA-Z]{6,20}$/;
    var isNum=/\d/;
    if(!userPhone){
        layer.open({content: '请输入手机号码',skin: 'msg',time: 2 });
        return false;
    }else if(!isPhoneReg.test(userPhone)){
        layer.open({content: '手机号格式不正确',skin: 'msg',time: 2 });
        return false;
    }
    if(!flag){
        if(!phoneCode){
            layer.open({content: '请输入手机验证码',skin: 'msg',time: 2 });
            return false;
        }else if(!isNum.test(phoneCode)){
            layer.open({content: '验证码格式不正确',skin: 'msg',time: 2 });
            return false;
        }
        if(!userPwd){
            layer.open({content: '请输入密码',skin: 'msg',time: 2 });
            return false;
        }else if(!isPwdReg.test(userPwd)){
            layer.open({content: '密码格式不正确',skin: 'msg',time: 2 });
            return false;
        }
    }
    return true;
}
/**获取验证码**/
$(".getCode").on("click",function () {
    if(validationInfo("1")){
        getPhoneCode();
    }
})
/**后台获取验证码**/
function getPhoneCode() {
    console.log("获取验证码");
}
/**注册**/
$(".commBtn button").on("click",function () {
    if(validationInfo()){
        updataInfo();
    }
})
/**注册提交**/
function updateInfo() {
    console.log("注册提交");
}