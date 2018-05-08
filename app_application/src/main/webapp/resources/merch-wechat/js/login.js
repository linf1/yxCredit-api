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
function validationInfo() {
    var userPhone=$("#userPhone").val(),//手机号码
        userPwd=$("#userPwd").val();//密码

    var isPhoneReg=/^1[34578]\d{9}$/;
    var isPwdReg=/^[0-9a-zA-Z]{6,20}$/;
    if(!userPhone){
        layer.open({content: '请输入手机号码',skin: 'msg',time: 2 });
        return false;
    }else if(!isPhoneReg.test(userPhone)){
        layer.open({content: '手机号格式不正确',skin: 'msg',time: 2 });
        return false;
    }
    if(!userPwd){
        layer.open({content: '请输入密码',skin: 'msg',time: 2 });
        return false;
    }else if(!isPwdReg.test(userPwd)){
        layer.open({content: '密码格式不正确',skin: 'msg',time: 2 });
        return false;
    }
    return true;
}
/**登录**/
$(".commBtn button").on("click",function () {
    if(validationInfo()){
        userLogin();
    }
})
/**后台登录**/
function userLogin() {
    console.log("登录");
}