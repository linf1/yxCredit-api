/**
 * 淘宝授权
 * Created by zl on 2018/1/4 0004.
 */
Ext.define('MyApp.view.perinfo.taoBaoAuth',{
    extend:'Ext.Panel',
    xtype:'taoBaoAuth',
    requires:[
        'Ext.Panel'
    ],
    config:{
        id:'taoBaoAuth',
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
                title:'淘宝授权',
                docked:'top',
                style:'background-color:#Fff;border-radius:0;',
                items:[
                    {
                        html: '<span class="icon-back" style="font-size:26px;color:#000;"></span>',
                        style:'margin-left:10px;color:#8c8c8c;margin-top:5px;',
                        listeners:{
                            tap:function () {
                                hidePage('#taoBaoAuth');
                            }
                        }
                    }
                ]
            },
            {
                xtype:'container',
                style:'width:100%;height:260px;background:url(resources/images/taobao_bg.jpg) no-repeat center center;background-size:cover;'
            },
            {
                xtype:'container',
                style:'background:#fff;height:328px;margin:-130px 25px 0;padding-top:30px;border-radius:8px;box-shadow: 0 1px 2px rgba(0,0,0,.3);',
                items:[
                    {
                        html:'<div><img width="50" style="display:block;margin:15px auto 8px;" src="resources/images/tobao-logo.png" alt=""><p style="font-size:14px;color:#666;text-align:center;">淘宝授权</p></div>'
                    },
                    {
                        xtype:'formpanel',
                        scrollable:false,
                        id:'taobaoForm',
                        height:200,
                        items:[
                            {
                                xtype:'textfield',
                                id:'aliNumber',
                                name:'aliNumber',
                                placeHolder:'淘宝会员名/手机号/邮箱',
                                clearIcon:false,
                                maxLength:20,
                                style:'height:40px;height:41px;width:90%;margin:10px auto 0;border:1px solid #d7d8d9;'
                            },
                            {
                                xtype:'passwordfield',
                                id:'aliPass',
                                name:'aliPass',
                                clearIcon:false,
                                placeHolder:'请输入淘宝密码',
                                maxLength:20,
                                style:'height:40px;height:41px;width:90%;margin:0 auto 8px;border:1px solid #d7d8d9;border-top:0;'
                            },
                            {
                                xtype:'button',
                                html:'<span style="color:#000;">授权</span>',
                                style:'background-color:#ffda44;border:none;border-radius:140px;height:40px;margin:25px 30px 0;height:40px;box-shadow: 0px 3px 3px rgba(248,214,201,1);',
                                listeners:{
                                    tap:function () {
                                        var aliNumber = Ext.getCmp('aliNumber').getValue(),
                                            aliPass = Ext.getCmp('aliPass').getValue();
                                        if(aliNumber == ""){
                                            layer.open({content:'淘宝账号不能为空',skin:'msg',time:2});
                                            return
                                        }
                                        if(aliPass == ""){
                                            layer.open({content:'淘宝密码不能为空',skin:'msg',time:2});
                                            return
                                        }
                                        var orderId = Ext.getCmp('order_id').getValue();
                                        taoBaoThis.toAuthration(aliNumber,aliPass,orderId);
                                    }
                                }
                            }
                        ]
                    },
                ]
            },
        ]
    },
    initialize:function() {
        taoBaoThis = this;
    },
    //短信验证码接口
    toAuthration:function (aliNumber,aliPass,orderId) {
        whir.loading.add("", 1);
        var me = this;
        Comm.ajaxPost(
            '/employeeAuthorization/taobaoGetSmsCode',
            {taobaoAccount:aliNumber,password:aliPass,orderId:orderId},
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                console.log(msg);
                var data = msg.retData;
                var base64String = data.base64String,
                    code = data.code,
                    sid = data.sid,
                    type = data.type;
                var msg = msg.retMsg;
                me.backCode(code,base64String,sid,type,msg,orderId)
            }
        );
    },
    //重发接口
    refreshCode:function (sid,orderId) {
        whir.loading.add("", 1);
        var me = this;
        Comm.ajaxPost(
            '/employeeAuthorization/taobaoRefreshCode',
            {sid:sid,orderId:orderId},
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                console.log(msg);
                var data = msg.retData;
                var base64String = data.base64String,
                    code = data.code,
                    sid = data.sid,
                    type = data.type;
                var msg = msg.retMsg;
                me.backCode(code,base64String,sid,type,msg,orderId);
            }
        );
    },
    //短信验证码接口
    smsCode:function (smsCode,sid,orderId) {
        whir.loading.add("", 1);
        var me = this;
        Comm.ajaxPost(
            '/employeeAuthorization/taobaoCheckSmsCode',
            {smsCode:smsCode,sid:sid,orderId:orderId},
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                console.log(msg);
                var data = msg.retData;
                var base64String = data.base64String,
                    code = data.code,
                    sid = data.sid,
                    type = data.type;
                var msg = msg.retMsg;
                me.backCode(code,base64String,sid,type,msg,orderId);
            }
        );
    },
    //扫描二维码接口
    saoCode:function (sid,orderId) {
        whir.loading.add("", 1);
        var taobaoAccount = Ext.getCmp('aliNumber').getValue();
        var me = this;
        Comm.ajaxPost(
            '/employeeAuthorization/qrCode',
            {sid:sid,orderId:orderId},
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                console.log(msg);
                var data = msg.retData;
                var base64String = data.base64String,
                    code = data.code,
                    sid = data.sid,
                    type = data.type;
                var msg = msg.retMsg;
                me.backCode(code,base64String,sid,type,msg,orderId);
            }
        );
    },
    //拉取报告接口
    taobaoQuery:function (sid,orderId) {
        whir.loading.add("", 1);
        var taobaoAccount = Ext.getCmp('aliNumber').getValue();
        var me = this;
        Comm.ajaxPost(
            '/employeeAuthorization/taobaoQuery',
            {taobaoAccount:taobaoAccount,sid:sid,orderId:orderId},
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                console.log(msg);
                var data = msg.retData;
                var base64String = data.base64String,
                    code = data.code,
                    sid = data.sid,
                    type = data.type;
                var msg = msg.retMsg;
                me.backCode(code,base64String,sid,type,msg,orderId);
            }
        );
    },
    //校验返回码
    backCode:function (code,base64String,sid,type,msg,orderId) {
        var control = this;
        if(code =='7004'){
            var isAli = document.getElementById('isAli');
            isAli.setAttribute('class','au-text au-active');
            isAli.innerHTML = '已授权';
            hidePage('#taoBaoAuth');
        }else if(code =='7003'){//调拉取报告的接口
            control.taobaoQuery(sid,orderId);
        }else if(code == "5011"){
            //调重发接口
            this.refreshCode(sid,orderId)
        }else if(code == '0'){
            //调短信验证码接口
            if(type == '0'){
                mui.prompt('<img src="data:image/png;base64,'+base64String+'"/>','请输入图形验证码','验证码',['取消','确定'],function (obj) {
                    if (obj.index == 1) {
                        var smsCode = obj.value;
                        control.smsCode(smsCode,sid,orderId);
                    }
                })
            }else if(type == '1'){
                mui.prompt('','请输入短信验证码','验证码',['取消','确定'],function (obj) {
                    if(obj.index == 1){
                        var smsCode = obj.value;
                        control.smsCode(smsCode,sid,orderId);
                    }
                })
            }else if(type == '3'){
                mui.confirm('<img src="data:image/png;base64,'+base64String+'"/>','请用支付宝App扫描二维码',['取消','确定'],function (obj) {
                    if(obj.index == 1){
                        //调扫描二维码后的接口
                        control.saoCode(sid,orderId);
                    }
                })
            }
        }else{
            mui.alert(msg,'提示','确定',null,'div');
            return
        }
    }
});
