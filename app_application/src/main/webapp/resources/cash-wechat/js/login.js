var tokenId = miaofu + "-" +new Date().getTime() + "-"+ Math.random().toString(16).substr(2);
(function() {
    _fmOpt = {
        partner: 'miaofu',
        appName: 'miaofu_web',
        token:tokenId,
        fpHost: 'https://fptest.fraudmetrix.cn',
        staticHost: 'statictest.fraudmetrix.cn',
        tcpHost: 'fpflashtest.fraudmetrix.cn',
        wsHost: 'fptest.fraudmetrix.cn:9090'
    };
    var cimg = new Image(1,1);
    cimg.onload = function() {
        _fmOpt.imgLoaded = true;
    };
    cimg.src = "https://fptest.fraudmetrix.cn/fp/clear.png?partnerCode=miaofu&appName=miaofu_web&tokenId=" + _fmOpt.token;
    var fm = document.createElement('script'); fm.type = 'text/javascript'; fm.async = true;
    fm.src = ('https:' == document.location.protocol ? 'https://' : 'http://') + 'statictest.fraudmetrix.cn/fm.js?ver=0.1&t=' + (new Date().getTime()/3600000).toFixed(0);
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(fm, s);
})();
/**登录逻辑**/
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
$("#uselogin").on("click",function () {
    var userPhone=$("#userPhone").val();
    var userPwd=$("#userPwd").val();
    var isPhoneReg=/^1[34578]\d{9}$/;
    var isPwdReg=/^[0-9a-zA-Z]{6,20}$/;
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
    var params = {};
    params.phone = userPhone;
    params.password = userPwd;
    Comm.ajaxPost("/wechat/login/login",params,function (data) {
        if(data.retData.imgCode){
            $.prompt({
                title: '验证码',
                text: '<span id="code_container"></span>',
                input: '',
                empty: false, // 是否允许为空
                onOK: function (input) {
                    //点击确认
                    var res = verifyCode.validate(input);
                    if(!res){layer.open({content: '验证码不正确',skin: 'msg',time: 2 });}
                },
                onCancel: function () {
                    //点击取消
                }
            });
            var verifyCode = new GVerify("code_container");
            $("#weui-prompt-input").attr("maxlength","4");
        }else{
            localStorage["isMine"]="";//设置里的返回标志
            window.location=_ctx+"/wechat/login/home"
        }
    });
})
