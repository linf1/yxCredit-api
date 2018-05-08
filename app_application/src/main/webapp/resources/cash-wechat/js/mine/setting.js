/**
 * Created by Administrator on 2017/10/30 0030.
 */
$(function () {
    initWXConfig();
    getPageInfo();
})
function getPageInfo() {
    Comm.ajaxPost("/wechat/oss/getPage","",function (data) {
        var retData=data.retData;
        var host=retData.host;
        if(retData.list){
            var head_img=retData.list[0].head_img;
            $("#wechat-headimg").attr("src",host+_ctx+head_img);
        }
    })
}
/**初始化微信**/
function initWXConfig() {
    Comm.ajaxPost("/wechat/config","url="+encodeURIComponent(location.href.split('#')[0]),function (data) {
        data = data.retData;
        wx.config({
            debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
            appId: data.appId, // 必填，公众号的唯一标识
            timestamp: data.timestamp, // 必填，生成签名的时间戳
            nonceStr: data.nonceStr, // 必填，生成签名的随机串
            signature: data.signature,// 必填，签名，见附录1
            jsApiList: ['uploadImage','chooseImage'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
        });
        wx.ready(function(){
            // config信息验证后会执行ready方法，
            // 所有接口调用都必须在config接口获得结果之后，
            // config是一个客户端的异步操作，
            // 所以如果需要在页面加载时就调用相关接口，
            // 则须把相关接口放在ready函数中调用来确保正确执行。
            // 对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
            // layer.open({content: '加载成功!',skin: 'msg',time: 2 });
            $("#showUpLoadImg").on("click",function () {
                wx.chooseImage({
                    count: 1, // 一次性选择图片的数量，默认9
                    sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有
                    sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
                    success: function (res) {
                        var localIds = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
                        wx.uploadImage({
                            localId: localIds.toString(), // 需要上传的图片的本地ID，由chooseImage接口获得
                            isShowProgressTips: 1, // 默认为1，显示进度提示
                            success: function (res) {
                                var mediaId = res.serverId; // 返回图片的服务器端ID，即mediaId
                                if (res.serverId == ""){
                                    layer.open({content: "图片上传失败",skin: 'msg',time: 2 });
                                    return false;
                                }
                                var reqMap = {
                                    "mediaId":""
                                }
                                reqMap.mediaId = mediaId;
                                Comm.ajaxPost("/wechat/oss/upload",reqMap,function (data) {
                                    alert(data.retData.imgHost);
                                    alert(data.retData.imgUrl);
                                    $("#wechat-headimg").attr("src",data.retData.imgHost+_ctx+data.retData.imgUrl);
                                    layer.open({content: "图片上传成功",skin: 'msg',time: 2 });
                                })
                            },
                            fail: function (res) {
                                layer.open({content: "上传图片失败，请重试",skin: 'msg',time: 2 });
                            }
                        });
                    }
                });
            })
        });
        wx.error(function(res){
            // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
        });
    })
}
/**忘记密码展示**/
function showUpdatePwd() {
    $("#setting").hide();
    $("#settingTitle").html("修改密码");
    $("#updatePwd").show();
}
/**返回**/
function GoBack() {
    if($("#settingTitle").html()=="修改密码"){
        $("#setting").show();
        $("#settingTitle").html("设置");
        $("#updatePwd").hide();
    }else{
        //localStorage["isMine"] = true;
        history.go(-1);
    }
}
/**updatePwd**/
$("#updatePwdBtn").on("click",function () {
    if(checkPwd()){
        updatePwd();
    }
})
/**
 * 退出登录
 */
$("#wechat-logout").on("click",function () {
    Comm.ajaxPost("/wechat/logout/logout","",function (sms) {
        layer.open({content:sms.retMsg,skin:"msg",time:1,end:function () {
            window.location=_ctx+"/wechat/login/tologin";
        }})
    })
})
/**校验密码**/
function checkPwd() {
    var oldPassWord=$("[name=oldPassWord]").val(),
        newPassWord=$("[name=newPassWord]").val(),
        repeatPassWord=$("[name=repeatPassWord]").val();
    var isPwdReg=/^[0-9a-zA-Z]{6,20}$/;
    if(!oldPassWord){
        layer.open({content: '原密码不能为空',skin: 'msg',time: 2 });
        return false;
    }else if(!isPwdReg.test(oldPassWord)){
        layer.open({content: '原密码格式不正确',skin: 'msg',time: 2 });
        return false;
    }
    if(!newPassWord){
        layer.open({content: '新密码不能为空',skin: 'msg',time: 2 });
        return false;
    }else if(!isPwdReg.test(newPassWord)){
        layer.open({content: '新密码格式不正确',skin: 'msg',time: 2 });
        return false;
    }
    if(oldPassWord==newPassWord){
        layer.open({content: '原密码和新密码不可一致',skin: 'msg',time: 2 });
        return false;
    }
    if(!repeatPassWord){
        layer.open({content: '重复密码不能为空',skin: 'msg',time: 2 });
        return false;
    }else if(!isPwdReg.test(repeatPassWord)){
        layer.open({content: '重复密码格式不正确',skin: 'msg',time: 2 });
        return false;
    }
    if(newPassWord!=repeatPassWord){
        layer.open({content: '新密码和重复密码不一致',skin: 'msg',time: 2 });
        return false;
    }
    return true;
}
/**修改密码**/
function updatePwd() {
    var oldPassWord=$("[name=oldPassWord]").val(),
        newPassWord=$("[name=newPassWord]").val(),
        repeatPassWord=$("[name=repeatPassWord]").val();
    var param = {};
    param.newPassword = newPassWord;
    param.oldPassword = oldPassWord;
    Comm.ajaxPost("set",param,function (sms) {
        var data = JSON.parse(sms);
        layer.open({content:data.retMsg,skin:"msg",time:2,end:function () {
            if(data.retCode === "SUCCESS"){
                GoBack();
            }
        }})
    })
}
/**进入绑定的银行卡**/
function goBindedCard() {
    window.location=_ctx+"/wechat/editPw/toBindedCard"
}

