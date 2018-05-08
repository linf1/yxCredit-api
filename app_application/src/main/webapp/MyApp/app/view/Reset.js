/**
 * 修改密码
 * Created by zl on 2017/12/1 0001.
 */
Ext.define('MyApp.view.Reset',{
    extend:'Ext.Panel',
    xtype:'resetView',
    requires:[
        'Ext.Panel',
        'MyApp.model.Reset'
    ],
    config:{
        id:'resetView',
        zIndex:5,
        showAnimation:{type:'slide', direction:'left'},
        layout:{type:'vbox'},
        items:[
            {
                xtype:'toolbar',
                title:'修改密码',
                docked:'top',
                style:'background-color:#FEE300;border-radius:0;',
                items:[
                    {
                        html: '<span class="icon-back" style="font-size:26px;color:#000;"></span>',
                        style:'margin-left:10px;color:#8c8c8c;margin-top:5px;',
                        listeners:{
                            tap:function () {
                                hidePage('#resetView');
                            }
                        }
                    }
                ]
            },
            {
                xtype:'formpanel',
                id:'resetForm',
                flex:9,
                scrollable:{direction: 'vertical',indicators: false},
                items:[
                    {
                        xtype:'textfield',
                        label:'<span style="font-size:14px;">手机号</span>',
                        id:'resetTel',
                        name:'resetTel',
                        placeHolder:'输入手机号',
                        maxLength:11,
                        clearIcon:false,
                        style:'border-bottom:1px solid #f6f6f6;'
                    },
                    {
                        xtype:'passwordfield',
                        label:'<span style="font-size:14px;">原密码</span>',
                        id:'resetPass',
                        name:'resetPass',
                        placeHolder:'输入原密码',
                        maxLength:20,
                        clearIcon:false,
                        style:'border-bottom:1px solid #f6f6f6;'
                    },
                    {
                        xtype:'passwordfield',
                        label:'<span style="font-size:14px;">新密码</span>',
                        id:'resetNewPass',
                        name:'resetNewPass',
                        placeHolder:'输入新密码',
                        maxLength:20,
                        clearIcon:false,
                        style:'margin-top:10px;border-bottom:1px solid #f6f6f6;'
                    },
                    {
                        xtype:'passwordfield',
                        label:'<span style="font-size:14px;">确认密码</span>',
                        id:'comNewPass',
                        name:'comNewPass',
                        placeHolder:'确认新密码',
                        maxLength:20,
                        clearIcon:false,
                        style:'border-bottom:1px solid #f6f6f6;'
                    },
                    {
                        xtype:'button',
                        html:'<span style="color:#1a1a1d;">确 定</span>',
                        style:'width:80%;margin:24px auto 16px;border:none;border-radius:50px;background-color:#FEE300;height:40px;box-shadow: 0px 3px 3px rgba(248,214,201,1);',
                        listeners:{
                            tap:function () {
                                Ext.getCmp('resetView').editPass();
                            }
                        }
                    }
                ]
            }
        ]
    },
    //修改密码
    editPass:function () {
        var formData = Ext.getCmp('resetForm').getValues();
        var model = Ext.create('MyApp.model.Reset',formData);
        var errors = model.validate();
        if(errors.isValid()){
            whir.loading.add("", 1)
            Comm.ajaxPost(
                '/modify/modifyPwd',
                {phone:formData.resetTel,oldPassword:formData.resetPass,newPassword:formData.resetNewPass},
                function (response) {
                    whir.loading.remove();
                    var msg = eval('('+response.responseText+')');
                    console.log(msg);
                    if(msg.retCode == "SUCCESS"){
                        hidePage('#resetView');
                    }else{
                        layer.open({content:msg.retMsg,skin:'msg',time:2});
                    }
                }
            )
        }else{
            var message = '';
            Ext.each(errors.items[0],function (rec) {
                message += rec.getMessage()+'<br>';
            });
            layer.open({content: message,skin: 'msg',time: 2});
        }
    }
});
