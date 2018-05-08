/**服务协议
 * Created by Administrator on 2017/11/14 0014.
 */
Ext.define('MyApp.view.ServeAgreement',{
    extend:'Ext.Panel',
    xtype:'serveAgreement',

    requires:[
        'Ext.Panel'
    ],

    config:{
        id:'serveAgreement',
        showAnimation:{type:'slide', direction:'left'},
        zIndex:6,
        layout:{type:'vbox'},
        scrollable:{direction: 'vertical',indicators: false},
        items:[
            {
                xtype: 'toolbar',
                title: '服务协议',
                style:'background-color: #fed300;border-radius:0;',
                docked:'top',
                items:[
                    {
                        ui:'back',
                        style:'margin-left:10px;color:#8c8c8c;margin-top:5px;',
                        listeners: {
                            tap:function() {
                                hidePage('#serveAgreement');
                            }
                        }
                    }
                ]
            },
            {
                html:'<div id="serACon" style="margin:0 15px;"></div>'
            }
        ],
        listeners:{
            show:function () {
                whir.loading.add("", 1);
                Comm.ajaxPost(
                    '/reg/getRegClause',
                    {type:'4'},
                    function (response) {
                        whir.loading.remove();
                        var msg = eval('('+response.responseText+')');
                        console.log(msg);
                        if(msg.retCode == "SUCCESS"){
                            var content = msg.retData.content;
                            var serACon = Ext.query('#serACon')[0];
                            serACon.innerHTML = content;
                        }
                    }
                )
            }
        }
    }
})