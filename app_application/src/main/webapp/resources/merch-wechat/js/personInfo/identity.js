/**
 * 个人信息 身份认证
 */
$(function(){
    //initPage();
    //initWXConfig();
});
function initWXConfig() {
    Comm.ajaxPost("/wechat/config","url="+encodeURIComponent(location.href.split('#')[0]),function (data) {
        data = data.retData;
        wx.config({
            debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
            appId: data.appId, // 必填，公众号的唯一标识
            timestamp: data.timestamp, // 必填，生成签名的时间戳
            nonceStr: data.nonceStr, // 必填，生成签名的随机串
            signature: data.signature,// 必填，签名，见附录1
            jsApiList: ['uploadImage','chooseImage','getLocation'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
        });
        wx.ready(function(){
            // config信息验证后会执行ready方法，
            // 所有接口调用都必须在config接口获得结果之后，
            // config是一个客户端的异步操作，
            // 所以如果需要在页面加载时就调用相关接口，
            // 则须把相关接口放在ready函数中调用来确保正确执行。
            // 对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
            // layer.open({content: '加载成功!',skin: 'msg',time: 2 });

            wx.getLocation({
                type: 'wgs84', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
                success: function (res) {
                    var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
                    var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
                    var speed = res.speed; // 速度，以米/每秒计
                    var accuracy = res.accuracy; // 位置精度
                    var orderId=$("#orderId").val();

                    var params = {
                        latitude:latitude,
                        longitude:longitude,
                        orderId:orderId
                    }
                    Comm.ajaxPost("/wechat/identity/addPosition",params,function (data) {

                    });
                }
            });

            $("#idCardOne").on("click",function () {
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
                                    $("#idCardFrontValue").val("");
                                    return false;
                                }
                                var reqMap = {
                                    "mediaId":"",
                                    "type":"front"
                                }
                                reqMap.mediaId = mediaId;
                                Comm.ajaxPost("/wechat/identity/uploadImg",reqMap,function (data) {
                                    $("#idCardFrontValue").val("true");
                                    $("#idcardfront").attr("src",data.retData.imgHost+_ctx+data.retData.imgUrl);
                                    layer.open({content: "图片上传成功",skin: 'msg',time: 2 });
                                })
                            },
                            fail: function (res) {
                                layer.open({content: "上传图片失败，请重试",skin: 'msg',time: 2 });
                                $("#idCardFrontValue").val("");
                            }
                        });
                    }
                });
            })
            $("#idCardTwo").on("click",function () {
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
                                    $("#idCardBackValue").val("");
                                    return false;
                                }
                                var reqMap = {
                                    "mediaId":"",
                                    "type":"back"
                                }
                                reqMap.mediaId = mediaId;
                                Comm.ajaxPost("/wechat/identity/uploadImg",reqMap,function (data) {
                                    $("#idCardBackValue").val("true");
                                    $("#idcardback").attr("src",data.retData.imgHost+_ctx+data.retData.imgUrl);
                                    layer.open({content: "图片上传成功",skin: 'msg',time: 2 });
                                })

                            },
                            fail: function (res) {
                                layer.open({content: "上传图片失败，请重试",skin: 'msg',time: 2 });
                                $("#idCardBackValue").val("");
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
function initPage() {
    Comm.ajaxPost("/wechat/identity/getIdentityInfoByCustId","",function (data) {
        if(data.retCode=="SUCCESS"){
            if(data.retData.length > 0){
                if(data.retData[0].realname){$("#userRealName").val(data.retData[0].realname)}//真实姓名
                if(data.retData[0].sex_name){ $("#userRealGender").val(data.retData[0].sex_name)}//性别
                if(data.retData[0].birth){$("#userRealBirthday").val(data.retData[0].birth)}//生日
                if(data.retData[0].card_no){$("#userIdCardNum").val(data.retData[0].card_no)}//身份证号
                if(data.retData[0].card_register_address){$("#userAddress").val(data.retData[0].card_register_address)}//户籍地址
                if(data.retData[0].Zcard_src_base64){$('#idcardfront').attr("src",data.retData[0].Zcard_src_base64);$("#idCardFrontValue").val("true")}
                if(data.retData[0].Fcard_src_base64){$('#idcardback').attr("src",data.retData[0].Fcard_src_base64);$("#idCardBackValue").val("true")}
            }
        }else{
            layer.open({content: data.msg,skin: 'msg',time: 2 });
        }
    });
}
$("#identityBtn").on("click",function () {
    var isValidate=checkIdentityInfo();
    if(isValidate){
        updataIdentityInfo();
    }
})
/**信息校验**/
function checkIdentityInfo() {
    var userRealName=$("#userRealName").val(),//真实姓名
        userRealGender=$("#userRealGender").val(),//性别
        userRealBirthday=$("#userRealBirthday").val(),//生日
        userIdCardNum=$("#userIdCardNum").val(),//身份证号
        userAddress=$("#userAddress").val(),//户籍地址
        idCardFrontValue=$("#idCardFrontValue").val(),//身份证正面
        idCardBackValue=$("#idCardBackValue").val();//身份证反面
    /**姓名汉字校验**/
    var userRealNameReg=/^([a-zA-Z0-9\u4e00-\u9fa5\·]{1,10})$/;
    /**性别汉字校验**/
    var userRealGenderReg=/^([\u4e00-\u9fa5\·]{1,2})$/;
    /**身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X*/
    var userIdCardNumReg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
    /**地址汉字数字字母校验**/
    var userRealNameReg=/^([a-zA-Z0-9\u4e00-\u9fa5\·]{1,50})$/;

    var BirthdayReg=/^[\d]{4}[.\ ][\d]{1,2}[.\ ][\d]{1,2}$/;

    if(!idCardFrontValue){
        layer.open({content: '请上传身份证正面照',skin: 'msg',time: 2 });
        return false;
    }
    if(!idCardBackValue){
        layer.open({content: '请上传身份证反面照',skin: 'msg',time: 2 });
        return false;
    }
    if(!userRealName){
        layer.open({content: '真实姓名不能为空',skin: 'msg',time: 2 });
        return false;
    }else if(!userRealNameReg.test(userRealName)){
        layer.open({content: '姓名格式不正确',skin: 'msg',time: 2 });
        return false;
    }
    if(!userRealGender){
        layer.open({content: '性别不能为空',skin: 'msg',time: 2 });
        return false;
    }else if(!userRealGenderReg.test(userRealGender)){
        layer.open({content: '性别格式不正确',skin: 'msg',time: 2 });
        return false;
    }
    if(!userRealBirthday){
        layer.open({content: '出生日期不能为空',skin: 'msg',time: 2 });
        return false;
    }else if(!BirthdayReg.test(userRealBirthday)){
        layer.open({content: '出生日期格式不正确',skin: 'msg',time: 2 });
        return false;
    }

    if(!userIdCardNum){
        layer.open({content: '身份证号不能为空',skin: 'msg',time: 2 });
        return false;
    }else if(!userIdCardNumReg.test(userIdCardNum)){
        layer.open({content: '身份证号格式不正确',skin: 'msg',time: 2 });
        return false;
    }
    if(!userAddress){
        layer.open({content: '户籍地址不能为空',skin: 'msg',time: 2 });
        return false;
    }else if(!userRealNameReg.test(userAddress)){
        layer.open({content: '户籍地址格式不正确',skin: 'msg',time: 2 });
        return false;
    }
    return true;
}
/**信息检验提交**/
function updataIdentityInfo() {
    var userRealName=$("#userRealName").val(),//真实姓名
        userRealGender=$("#userRealGender").val(),//性别
        userRealBirthday=$("#userRealBirthday").val(),//生日
        userIdCardNum=$("#userIdCardNum").val(),//身份证号
        userAddress=$("#userAddress").val();//户籍地址
    var idcardfront = $('#idcardfront').attr('src');
    var idcardback = $('#idcardback').attr('src');
    var orderId=$("#orderId").val();

    var temp=userRealBirthday.split(".");
    var month,day;
    if(temp[1].length<2){
        month="0"+temp[1];
    }else{
        month=temp[1];
    }
    if(temp[2].length<2){
        day="0"+temp[2];
    }else{
        day=temp[2];
    }
    userRealBirthday=temp[0]+"."+month+"."+day;
    var params = {};
    params.realname = userRealName;
    params.sex_name = userRealGender;
    params.birth =userRealBirthday;
    params.card = userIdCardNum;
    params.card_register_address = userAddress;
    params.Zcard_src_base64 = idcardfront;
    params.Fcard_src_base64 = idcardback;
    params.orderId = orderId;
    var data = JSON.stringify(params);
    Comm.ajaxPost("/wechat/identity/saveIdentityInfo",{"data":data},function (data) {
        if(data.retCode=="SUCCESS"){
            window.location=_ctx+"/wechat/identity/personInfoPage?orderId="+orderId;
        }else{
            layer.open({content: data.msg,skin: 'msg',time: 2 });
        }
    });
}
