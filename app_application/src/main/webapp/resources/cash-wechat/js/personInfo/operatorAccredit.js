$(".operatorInfoFooter button").on("click",function () {
    if(checkInfo()){
        getJxlCode();
    }
})
/**信息验证*/
function checkInfo() {
    var authTelnum=$("input[name=authTelnum]").val();
    var authTelpass=$("input[name=authTelpass]").val();
    var isPhoneReg=/^1[34578]\d{9}$/;
    var isPwdReg=/^[0-9a-zA-Z]{6,20}$/;
    if(!authTelnum){
        layer.open({content: '手机号不能为空',skin: 'msg',time: 2 });
        return false;
    }else if(!isPhoneReg.test(authTelnum)){
        layer.open({content: '手机号格式不正确',skin: 'msg',time: 2 });
        return false;
    }
    if(!authTelpass){
        layer.open({content: '密码不能为空',skin: 'msg',time: 2 });
        return false;
    }else if(!isPwdReg.test(authTelpass)){
        layer.open({content: '密码格式不正确',skin: 'msg',time: 2 });
        return false;
    }
    return true;
}
/**首次调用**/
function getJxlCode() {
    whir.loading.add("", 1);
    var authTelnum=$("input[name=authTelnum]").val();
    var authTelpass=$("input[name=authTelpass]").val();
    Comm.ajaxPost('/wechat/operator/tongdunGetSmsCode', {password:authTelpass, phone:authTelnum}, function (response) {
            var data = response.retData;
            var responseCode = data.responseCode;//code码
            var smsData = data.smsData;
            var taskId = data.taskId;
            if(smsData){
                var fields = smsData.fields,
                    next_stage = smsData.next_stage;
            }else{
                var fields = "",
                    next_stage = "";
            }
            judgeCode(data,responseCode,smsData,fields,taskId,next_stage,authTelnum,authTelpass);
        }
    )
}
function judgeCode(data,code,smsData,fields,taskId,next_stage,authTelnum,authTelpass) {
    // if(code == '10001') {
    //     $.prompt({
    //         title: '验证码',
    //         text: '请输入手机验证码',
    //         input: '',
    //         empty: false,
    //         onOK: function (input) {
    //             secondToGet(input,authTelnum,authTelpass,token,website)
    //         },
    //         onCancel: function () {
    //         }
    //     });
    //     return
    // }else if(code == '10002'){
    //     $.prompt({
    //         title: '验证码',
    //         text: '请输入手机验证码',
    //         input: '',
    //         empty: false,
    //         onOK: function (input) {
    //             secondToGet(input,authTelnum,authTelpass,token,website)
    //         },
    //         onCancel: function () {
    //         }
    //     });
    //     return
    // }else if(code == '10003'){
    //     $.alert("密码错误", "消息");
    //     return
    // }else if(code == '10004'){
    //     $.alert("短信验证码错误", "消息");
    //     return
    // }else if(code == '10006'){
    //     $.alert({
    //         title: '消息',
    //         text: '短信验证码失效系统已自动重新下发',
    //         onOK: function () {
    //             $.prompt({
    //                 title: '验证码',
    //                 text: '请输入手机验证码',
    //                 input: '',
    //                 empty: false,
    //                 onOK: function (input) {
    //                     secondToGet(input,authTelnum,authTelpass,token,website)
    //                 },
    //                 onCancel: function () {
    //                 }
    //             });
    //         }
    //     });
    //     return
    // }else if(code == '10007'){
    //     $.alert("简单密码或初始密码无法登录", "消息");
    //     return
    // }else if(code == '10008'){
    //     layer.open({content:'开始采集行为数据',skin:'msg',time:2,end:function () {
    //         history.go(-1);
    //     }});
    //     return
    // }else if(code == '10017'){
    //     $.alert({
    //         title: '消息',
    //         text: '请用本机发送CXXD至10001获取查询详单的验证码',
    //         onOK: function () {
    //             $.prompt({
    //                 title: '验证码',
    //                 text: '请输入手机验证码',
    //                 input: '',
    //                 empty: false,
    //                 onOK: function (input) {
    //                     secondToGet(input,authTelnum,authTelpass,token,website)
    //                 },
    //                 onCancel: function () {
    //                 }
    //             });
    //         }
    //     });
    //     return
    // }else if(code == '10018'){
    //     $.alert({
    //         title: '消息',
    //         text: '短信码失效，请用本机发送CXXD至10001获取查询详单的验证码',
    //         onOK: function () {
    //             $.prompt({
    //                 title: '验证码',
    //                 text: '请输入手机验证码',
    //                 input: '',
    //                 empty: false,
    //                 onOK: function (input) {
    //                     secondToGet(input,authTelnum,authTelpass,token,website)
    //                 },
    //                 onCancel: function () {
    //                 }
    //             });
    //         }
    //     });
    //     return
    // }else if(code == '10022'){
    //     $.alert("输入查询密码", "消息");
    //     return
    // }else if(code == '10023'){
    //     $.alert("查询密码错误", "消息");
    //     return
    // }else if(code == '30000'){
    //     $.alert("错误信息", "消息");
    //     return
    // }else if(code == '0'){
    //     $.alert("采集请求超时", "消息");
    //     return
    // }else{
    //     $.alert("请求失败", "消息");
    //     return
    // }


    if(code == '101') {
        $.prompt({
            title: '验证码',
            text: '<img src="data:image/png;base64,'+smsData.auth_code+'"/>',
            input: '',
            empty: false,
            onOK: function (input) {
                showSmscode(authTelnum,authTelpass,taskId,next_stage,"",input);
            },
            onCancel: function () {
            }
        });
        return
    }else if(code == '104' || code=='108' || code=='122' || code=='124'){
        retrySmscode(taskId);
        return
    }else if(code == '105'){
        $.prompt({
            title: '验证码',
            text: '请输入手机验证码',
            input: '',
            empty: false,
            onOK: function (input) {
                showSmscode(authTelnum,authTelpass,taskId,next_stage,input,"");
            },
            onCancel: function () {
            }
        });
        return
    }else if(code == '123'){
        $.prompt({
            title: '验证码',
            text: '<img src="data:image/png;base64,'+smsData.auth_code+'"/><input id="authCode" style="width:100%;height:26px;" type="text" placeholder="请输入图形验证码"/>',
            input: '',
            empty: false,
            onOK: function (input) {
               var authCode = $("#authCode").val();
                showSmscode(authTelnum,authTelpass,taskId,next_stage,input,authCode);
            },
            onCancel: function () {
            }
        });
        return
    }else if(code == '137' || code=='2007'){
        layer.open({content:"授权成功!",skin:'msg',time:2,end:function () {
            history.go(-1);
        }})
        return
    }else{
        $.alert(data.responseMsg, "消息");
        return
    }

}
function showSmscode(authTelnum,authTelpass,taskId,next_stage,smsCode,authCode) {
    whir.loading.add("", 1);
    Comm.ajaxPost('/wechat/operator/tongdunCheckSmsCode',
        {
            password:authTelpass,
            taskStage:next_stage,
            taskId:taskId,
            smsCode:smsCode,
            authCode:authCode,
            phone:authTelnum
        },
        function (response) {
            var data = response.retData;
            var responseCode = data.responseCode;//code码
            var smsData = data.smsData;
            var taskId = data.taskId;
            if(smsData){
                var fields = smsData.fields,
                    next_stage = smsData.next_stage;
            }else{
                var fields = "",
                    next_stage = "";
            }
            judgeCode(data,responseCode,smsData,fields,taskId,next_stage,authTelnum,authTelpass);
        }
    )
}
/**重发接口*/
function retrySmscode(taskId) {
    whir.loading.add("", 1);
    Comm.ajaxPost('/wechat/operator/tongdunRetrySmsCode',
        {
            taskId:taskId
        },
        function (response) {
            var data = response.retData;
            var responseCode = data.responseCode;//code码
            var smsData = data.smsData;
            var taskId = data.taskId;
            if(smsData){
                var fields = smsData.fields,
                    next_stage = smsData.next_stage;
            }else{
                var fields = "",
                    next_stage = "";
            }
            judgeCode(data,responseCode,smsData,fields,taskId,next_stage,authTelnum,authTelpass);
        }
    )
}
/**第二次调用**/
function secondToGet(codeNum,authTelnum,authTelpass,token,website) {
    whir.loading.add("", 1);
    Comm.ajaxPost('/wechat/identity/jxlCheckSmsCode',
        {
            password:authTelpass,
            tel:authTelnum,
            token:token,
            website:website,
            smsCode:codeNum
        },
        function (response) {
            var code = response.retData.state;
            //二次请求后再次比较code码进行下一步操作
            judgeCode(code,authTelnum,authTelpass,token,website);
        }
    )
}
/**找回密码效果**/
$("#getLostPwd").on("click",function () {
   $("#operatorInfo").slideToggle()
})
