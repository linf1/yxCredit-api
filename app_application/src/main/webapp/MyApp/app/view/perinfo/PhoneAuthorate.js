/**运营商授权
 * Created by Administrator on 2017/10/26 0026.
 */
Ext.define('MyApp.view.perinfo.PhoneAuthorate',{
    extend:'Ext.Panel',
    xtype:'PhoneAuthorateView',

    requires:[
        'Ext.Panel',
        'Ext.form.Panel',
        'Ext.field.Password',
        'Ext.Img',
        'Ext.field.Hidden'
    ],

    config:{
        id:'PhoneAuthorateView',
        zIndex:5,
        layout:{
            type:'vbox'
        },
        showAnimation : {
            type:'slide', direction : 'left'
        },
        scrollable:{direction: 'vertical',indicators: false},
        items:[
            {
                xtype: 'toolbar',
                title:'手机运营商授权',
                docked:'top',
                style:'background-color:#FEE300;border-radius:0;',
                items:[
                    {
                        html: '<span class="icon-back" style="font-size:26px;color:#000;"></span>',
                        style:'margin-left:10px;color:#8c8c8c;margin-top:5px;',
                        listeners:{
                            tap:function () {
                                hidePage('#PhoneAuthorateView');
                            }
                        }
                    }
                ]
            },
            {
                xtype:'container',
                style:'width:100%;height:260px;background:url(resources/images/mobile_bg.jpg) no-repeat center center;background-size:cover;'
            },
            {
                xtype:'container',
                style:'background:#fff;height:210px;margin:-88px 25px 0;padding-top:30px;border-radius:8px;box-shadow: 0 1px 2px rgba(0,0,0,.3);',
                items:[
                    {
                        xtype:'textfield',
                        id:'authTelnum',
                        name:'authTelnum',
                        placeHolder:'请输入手机号',
                        clearIcon:false,
                        maxLength: 11,
                        html:'<span class="icon-user" style="position: absolute;top:10px;left:10px;font-size: 18px;"></span>',
                        style:'height:40px;height:41px;width:90%;margin:0 auto;border-radius:5px;'
                    },
                    {
                        xtype:'passwordfield',
                        id:'authTelpass',
                        name:'authTelpass',
                        clearIcon:false,
                        placeHolder:'请输入服务密码',
                        html:'<span class="icon-password" style="position: absolute;top:10px;left:10px;font-size: 18px;"></span>',
                        style:'height:40px;height:41px;width:90%;margin:14px auto 8px;border-radius:5px;'
                    },
                    //存储短信验证码
                    {
                        xtype:'hiddenfield',
                        id:'telSmsCodeNum'
                    },
                    {
                        xtype:'button',
                        html:'<span style="color:#000;">授权</span>',
                        style:'background-color:#ffda44;border:none;border-radius:140px;height:40px;margin:10px 30px 0;height:40px;box-shadow: 0px 3px 3px rgba(248,214,201,1);',
                        listeners:{
                            tap:function () {
                                var authTelnum = Ext.ComponentQuery.query('#authTelnum')[0].getValue();
                                var authTelpass = Ext.ComponentQuery.query('#authTelpass')[0].getValue();
                                var orderId = Ext.getCmp('order_id').getValue();
                                var telReg = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1})|(17[0-9]{1}))+\d{8})$/;
                                if(authTelnum == ""){
                                    layer.open({content:'手机号不能为空',skin:'msg',time:2});
                                    return
                                }else if(!telReg.test(authTelnum)){
                                    layer.open({content:'手机号码格式不正确',skin:'msg',time:2});
                                    return
                                }
                                if(authTelpass == ""){
                                    layer.open({content:'服务密码不能为空',skin:'msg',time:2});
                                    return
                                }
                                Ext.ComponentQuery.query('#PhoneAuthorateView')[0].getJxlCode(authTelnum,authTelpass,orderId);
                                //Ext.ComponentQuery.query('#PhoneAuthorateView')[0].getMobileAuthoration(authTelnum,authTelpass);
                            }
                        }
                    }
                ]
            },
            {
                xtype:'container',
                items:[
                    {
                        html:'<div style="margin-top:40px;color:#a7a7a7;margin-left:15px;font-size: 12px;"><p>温馨提示</p><p>1、登录成功后将收到运营商通知短信，无需回复</p><p>2、忘记服务密码? <span style="color:#FE8F60;" onclick="telAuthThis.overlayShow()">找回手机服务密码></span></p></div>'
                    }
                ]
            }
        ]
    },
    initialize:function () {
        telAuthThis=this;
        var customerTel = Ext.getCmp('customerTel').getValue();
        Ext.getCmp('authTelnum').setValue(customerTel);
    },
    overlayShow:function () {
        //实物协议
        var swAgreementHtml = '<div><p style="font-size: 16px;font-weight: 700;padding:5px;">1.移动找回：</p>' +
            '<p style="font-size: 10px;padding:5px;">（1）发送MMCZ到10086；</p>' +
            '<p style="font-size: 10px;padding:5px;">（2）登录网上营业厅,点击"忘记密码",凭手机号码和短信发送的动态密码二次登录后,进行密码重置；</p>' +
            '<p style="font-size: 10px;padding:5px;">（3）本机登录掌上营业厅,进入"掌上客服"－密码重置；</p>' +
            '<p style="font-size: 10px;padding:5px;">（4）拨打10086自助语音服务,进行密码重置。</p>' +
            '<p style="font-size: 16px;font-weight: 700;padding:5px;">2.联通找回：</p>' +
            '<p style="font-size: 10px;padding:5px;">（1）电脑登陆中国联通网上营业厅www.10010.com</p>' +
            '<p style="font-size: 10px;padding:5px;">（2）发送短信“MMCZ#6位新密码”至10010重置服务密码。</p>' +
            '<p style="font-size: 10px;padding:5px;">（3）注意：归属地不同可能会出现不同的情况，有些地区需要发送“MMXC#旧密码#新密码#新密码”至10010重置密码</p>' +
            '<p style="font-size: 16px;font-weight: 700;padding:5px;">3.电信：</p>' +
            '<p style="font-size: 10px;padding:5px;">固话、小灵通、手机用户本机拨打10001再按3获取。（星级手机用户拨打10001-1-3-2获取）手机信息方式：小灵通、手机用户可编辑信息内容551发送到10001获取。</p>' +
            '</div>';

        var html = swAgreementHtml;
        var title = '找回服务密码';
        var width=window.screen.width;
        var overlay = Ext.Viewport.add({
            xtype: 'panel',
            left: (width-width*0.8)/2,
            top: '20%',
            modal: true,
            zIndex:10,
            hideOnMaskTap: true,
            hidden: true,
            width: "80%",
            height: '65%',
            styleHtmlContent: true,
            style:'border-radius:10px',
            scrollable:{
                //注意横向竖向模式的配置，不能配错
                direction: 'vertical',
                //隐藏滚动条样式
                indicators: false
            },
            items: [
                {
                    docked: 'top',
                    xtype: 'titlebar',
                    title: '<p style="color:#000">'+title+'</p>',
                    style:'background:#FEE300;border-top-left-radius:10px;border-top-right-radius:10px',
                    items:[
                        {},{xtype:'spacer'},
                        {
                            ui:'button',
                            text:'<span style="font-size: 30px">&times;</span>',
                            style:'color:#000;position:absolute;top:10px;left:257px;',
                            listeners:{
                                tap:function () {
                                    overlay.hide({type: 'slideOut', direction: 'up'});
                                }
                            }
                        }
                    ]
                },
                {
                    xtype:'panel',
                    items:[
                        {
                            html:html
                        }
                    ]
                }
            ]
        });
        overlay.show({type: 'slideIn', direction: 'up'});
    },
    /********************聚信立调用的方法***********************/
    //首次调用
    getJxlCode:function (authTelnum,authTelpass,orderId) {
        whir.loading.add("", 1);
        Comm.ajaxPost(
            '/authorization/jxlGetSmsCode',
            {
                password:authTelpass,
                tel:authTelnum,
                orderId:orderId
            },
            function (response) {
                whir.loading.remove();
                var msg=eval('('+response.responseText+')');
                console.log(msg);
                var code = msg.retData.state;
                var token = msg.retData.token;
                var website = msg.retData.website;
                var content = msg.retData.msg;
                Ext.ComponentQuery.query('#PhoneAuthorateView')[0].judgeCode(code,content,authTelnum,authTelpass,token,website,orderId);
            }
        )
    },
    //二次调用
    secondToGet:function (codeNum,authTelnum,authTelpass,token,website,orderId) {
        whir.loading.add("", 1);
        Comm.ajaxPost(
            '/authorization/jxlCheckSmsCode',
            {
                password:authTelpass,
                tel:authTelnum,
                token:token,
                website:website,
                smsCode:codeNum,
                orderId:orderId
            },
            function (response) {
                whir.loading.remove();
                var msg=eval('('+response.responseText+')');
                console.log(msg);
                var code = msg.retData.state;
                var content = msg.retData.msg;
                //二次请求后再次比较code码进行下一步操作
                Ext.ComponentQuery.query('#PhoneAuthorateView')[0].judgeCode(code,content,authTelnum,authTelpass,token,website,orderId);
            }
        )
    },
    //处理返回状态码
    judgeCode:function (code,content,authTelnum,authTelpass,token,website,orderId) {
        /***code 为状态码
         * 第一次调用接口获得token,website作参，为了方便在第二次调用验证码接口时使用
         * authTelnum,authTelpass分别为电话号码与服务密码
         * */
        if(code == '10001') {
            mui.prompt('', '请输入手机验证码', '验证码', ['取消', '确定'], function (obj) {
                if (obj.index == 1) {
                    var codeNum = obj.value;
                    Ext.ComponentQuery.query('#PhoneAuthorateView')[0].secondToGet(codeNum,authTelnum,authTelpass,token,website,orderId)
                }
            })
            return
        }else if(code == '10002'){
            mui.prompt('','请输入手机验证码','验证码',['取消','确定'],function (obj) {
                if(obj.index == 1){
                    var codeNum = obj.value;
                    Ext.ComponentQuery.query('#PhoneAuthorateView')[0].secondToGet(codeNum,authTelnum,authTelpass,token,website,orderId)
                }
            })
            return
        }else if(code == '10003'){
              mui.alert('密码错误','','确定',null,'div');
            return
        }else if(code == '10004'){
            mui.alert('短信验证码错误','','确定',null,'div');
            return
        }else if(code == '10006'){
            mui.alert('短信验证码失效系统已自动重新下发','','确定',function () {
                mui.prompt('','请输入手机验证码','验证码',['取消','确定'],function (obj) {
                    if(obj.index == 1){
                        var codeNum = obj.value;
                        Ext.ComponentQuery.query('#PhoneAuthorateView')[0].secondToGet(codeNum,authTelnum,authTelpass,token,website,orderId)
                    }
                })
            },'div');
            return
        }else if(code == '10007'){
            mui.alert('简单密码或初始密码无法登录','','确定',null,'div');
            return
        }else if(code == '10008'){
            layer.open({content:'授权成功',skin:'msg',time:2,end:function () {
                var isMove = document.getElementById('isMove');
                 isMove.innerHTML = '已授权';
                 isMove.setAttribute('class','au-text au-active');
                var authBadge = document.getElementById('auth-badge');
                var faceFlag = Ext.getCmp('faceFlag').getValue(),
                    phoneFlag = Ext.getCmp('phoneFlag');
                phoneFlag.setValue('1');
                faceFlag == ""?authBadge.style.display = 'block':authBadge.style.display='none';
                hidePage('#PhoneAuthorateView');
            }});
            return
        }else if(code == '10017'){
            mui.alert('请用本机发送CXXD至10001获取查询详单的验证码','','确定',function () {
                mui.prompt('','请输入手机验证码','验证码',['取消','确定'],function (obj) {
                    if(obj.index == 1){
                        var codeNum = obj.value;
                        Ext.ComponentQuery.query('#PhoneAuthorateView')[0].secondToGet(codeNum,authTelnum,authTelpass,token,website,orderId)
                    }
                })
            },'div');
            return
        }else if(code == '10018'){
            mui.alert('短信码失效，请用本机发送CXXD至10001获取查询详单的验证码','','确定',function () {
                mui.prompt('','请输入手机验证码','验证码',['取消','确定'],function (obj) {
                    if(obj.index == 1){
                        var codeNum = obj.value;
                        Ext.ComponentQuery.query('#PhoneAuthorateView')[0].secondToGet(codeNum,authTelnum,authTelpass,token,website,orderId)
                    }
                })
            },'div');
            return
        }else if(code == '10022'){
            mui.alert('输入查询密码','','确定',null,'div');
            return
        }else if(code == '10023'){
            mui.alert('查询密码错误','','确定',null,'div');
            return
        }else if(code == '30000'){
            if(content){
                mui.alert(content,'','确定',null,'div');
            }else{
                mui.alert('网络异常、运营商异常或当天无法下发短信验证码所导致的无法登陆,请联系客服.','','确定',null,'div');
            }
           // mui.alert('错误信息','','确定',null,'div');
            return
        }else if(code == '0'){
            mui.alert('运营商网站异常或者服务更新升级导致不可用,请联系客服.','','确定',null,'div');
            return
        }else{
            mui.alert('请求失败','','确定',null,'div');
            return
        }
    }
    //------------------------------铜盾--------------------
   /* //首次调用
    getJxlCode:function (authTelnum,authTelpass) {
        whir.loading.add("", 1);
        var control = this;
        Comm.ajaxPost(
            '/authorization/tongdunGetSmsCode',
            {
                password:authTelpass,
                phone:authTelnum
            },
            function (response) {
                whir.loading.remove();
                var msg=eval('('+response.responseText+')');
                console.log(msg);
                var data = msg.retData;
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
                control.judgeCode(data,responseCode,smsData,fields,taskId,next_stage,authTelnum,authTelpass);
            }
        )
    },
    //输入短信验证码
    showSmscode:function (authTelnum,authTelpass,taskId,next_stage,smsCode,authCode) {
        whir.loading.add("", 1);
        var control = this;
        Comm.ajaxPost(
            '/authorization/tongdunCheckSmsCode',
            {
                password:authTelpass,
                taskStage:next_stage,
                taskId:taskId,
                smsCode:smsCode,
                authCode:authCode,
                phone:authTelnum
            },
            function (response) {
                whir.loading.remove();
                var msg=eval('('+response.responseText+')');
                console.log(msg);
                console.log(msg);
                var data = msg.retData;
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
                control.judgeCode(data,responseCode,smsData,fields,taskId,next_stage,authTelnum,authTelpass);
            }
        )
    },
    //重发接口
    retrySmscode:function (taskId) {
        whir.loading.add("", 1);
        var control = this;
        Comm.ajaxPost(
            '/authorization/tongdunRetrySmsCode',
            {
                taskId:taskId
            },
            function (response) {
                whir.loading.remove();
                var msg=eval('('+response.responseText+')');
                console.log(msg);
                var data = msg.retData;
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
                control.judgeCode(data,responseCode,smsData,fields,taskId,next_stage,authTelnum,authTelpass);
            }
        )
    },

    //处理返回状态码
    judgeCode:function (data,code,smsData,fields,taskId,next_stage,authTelnum,authTelpass) {
        var control = this;
        /!***code 为状态码
         * smsData返回的接口数据,fields返回的要输入的类型
         * authTelnum,authTelpass分别为电话号码与服务密码
         * *!/
        if(code == '101') {
            mui.prompt('<img src="data:image/png;base64,'+smsData.auth_code+'"/>','请输入图形验证码','验证码',['取消','确定'],function (obj) {
                if (obj.index == 1) {
                    var codeNum = obj.value;
                    control.showSmscode(authTelnum,authTelpass,taskId,next_stage,"",codeNum);
                }
            })
            return
        }else if(code == '104' || code=='108' || code=='122' || code=='124'){
            control.retrySmscode(taskId);
            return
        }else if(code == '105'){
            mui.prompt('','请输入短信验证码','验证码',['取消','确定'],function (obj) {
                if(obj.index == 1){
                    var codeNum = obj.value;
                    control.showSmscode(authTelnum,authTelpass,taskId,next_stage,codeNum,"");
                }
            })
            return
        }else if(code == '123'){
            mui.prompt('<img src="data:image/png;base64,'+smsData.auth_code+'"/><input id="authCode" style="width:100%;height:26px;" type="text" placeholder="请输入图形验证码"/>','请再输入短信验证码','验证码',['取消','确定'],function (obj) {
                if(obj.index == 1){
                    var smsCode = obj.value;
                    var authCode = document.getElementById('authCode').getAttribute('value');
                    control.showSmscode(authTelnum,authTelpass,taskId,next_stage,smsCode,authCode);
                }
            })
            return
        }else if(code == '137' || code=='2007'){
            layer.open({content:"授权成功",skin:'msg',time:2,end:function () {
                var operateAuDom = document.getElementById('operateAuDom');
                operateAuDom.innerHTML = '已授权';
                operateAuDom.style.color = '#333';
                Ext.ComponentQuery.query('#telOperator')[0].setValue('1');
                hidePage('#PhoneAuthorateView');
            }})
            return
        }else{
            mui.alert(data.responseMsg,'','确定',null,'div');
            return
        }
    }*/
})
