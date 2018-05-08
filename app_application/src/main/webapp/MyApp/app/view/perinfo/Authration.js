/**
 * 授权
 * Created by zl on 2017/11/23 0023.
 */
Ext.define('MyApp.view.perinfo.Authration',{
    extend:'Ext.Panel',
    xtype:'authView',
    requires:[
        'Ext.Panel',
        'MyApp.view.perinfo.LiveCertificate',
        'MyApp.view.perinfo.PhoneAuthorate',
        'MyApp.view.perinfo.taoBaoAuth'
    ],
    config:{
        id:'authView',
        zIndex:5,
        showAnimation:{type:'slide', direction:'left'},
        layout:{type:'vbox'},
        scrollable:{direction: 'vertical',indicators: false},
        items:[
            {
                xtype:'button',
                id:'faceAuthBtn',
                html:'<div class="face-wrapper"><img width="40" src="resources/images/face.png"><span>人脸识别</span></div><div class="au-text" id="isFace">未授权</div><img class="must-img" width="30" src="resources/images/must.png">',
                style:'margin-top:10px;height:60px;border:none;border-radius:0;',
                listeners:{
                    // tap:function(){
                    //     if(Ext.ComponentQuery.query('#LiveCertificateView')[0]){
                    //         Ext.ComponentQuery.query('#LiveCertificateView')[0].show();
                    //     }else{
                    //         Ext.Viewport.add(Ext.create('MyApp.view.perinfo.LiveCertificate')).show();
                    //     }
                    // }
                }
            },
            {
                xtype:'hiddenfield',
                id:'faceFlag'
            },
            {
                xtype:'button',
                id:'phoneAuthBtn',
                html:'<div class="face-wrapper"><img width="40" src="resources/images/move.png"><span>手机运营商</span></div><div class="au-text" id="isMove">未授权</div><img class="must-img" width="30" src="resources/images/must.png">',
                style:'margin-top:5px;height:60px;border:none;border-radius:0;',
                listeners:{
                    // tap:function(){
                    //     if(Ext.ComponentQuery.query('#PhoneAuthorateView')[0]){
                    //         Ext.ComponentQuery.query('#PhoneAuthorateView')[0].show();
                    //     }else{
                    //         Ext.Viewport.add(Ext.create('MyApp.view.perinfo.PhoneAuthorate')).show();
                    //     }
                    // }
                }
            },
            {
                xtype:'hiddenfield',
                id:'phoneFlag'
            },
            /* {
                xtype:'button',
                html:'<div class="face-wrapper"><img width="40" src="resources/images/ali.png"><span>淘宝</span></div><div class="au-text" id="isAli">未授权</div>',
                style:'margin-top:5px;height:60px;border:none;border-radius:0;',
                listeners:{
                    tap:function () {
                        if(Ext.getCmp('taoBaoAuth')){
                            Ext.getCmp('taoBaoAuth').show();
                        }else{
                            Ext.Viewport.add(Ext.create('MyApp.view.perinfo.taoBaoAuth')).show();
                        }
                    }
                }
            }*/
        ],
        listeners:{
            show:function () {
                var orderState = Ext.getCmp('orderStateFlag').getValue();
                if (!orderState || orderState <= 11)
                {
                    Ext.getCmp('faceAuthBtn').on('tap', Ext.getCmp('authView').toFaceAuthPage);
                    Ext.getCmp('phoneAuthBtn').on('tap', Ext.getCmp('authView').toPhoneAuthPage);
                }
            }
        }
    },
    toFaceAuthPage:function () {
        if(Ext.ComponentQuery.query('#LiveCertificateView')[0]){
            Ext.ComponentQuery.query('#LiveCertificateView')[0].show();
        }else{
            Ext.Viewport.add(Ext.create('MyApp.view.perinfo.LiveCertificate')).show();
        }
    },
    toPhoneAuthPage:function () {
        if(Ext.ComponentQuery.query('#PhoneAuthorateView')[0]){
            Ext.ComponentQuery.query('#PhoneAuthorateView')[0].show();
        }else{
            Ext.Viewport.add(Ext.create('MyApp.view.perinfo.PhoneAuthorate')).show();
        }
    }
});
