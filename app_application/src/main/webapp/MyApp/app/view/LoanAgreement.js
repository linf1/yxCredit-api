/**借款协议
 * Created by Administrator on 2017/11/14 0014.
 */
Ext.define('MyApp.view.LoanAgreement',{
    extend:'Ext.Panel',
    xtype:'loanAgreement',

    requires:[
        'Ext.Panel'
    ],

    config:{
        id:'loanAgreement',
        showAnimation:{type:'slide', direction:'left'},
        zIndex:6,
        layout:{type:'vbox'},
        scrollable:{direction: 'vertical',indicators: false},
        items:[
            {
                xtype: 'toolbar',
                title: '借款协议',
                style:'background-color: #fed300;border-radius:0;',
                docked:'top',
                items:[
                    {
                        ui:'back',
                        style:'margin-left:10px;color:#8c8c8c;margin-top:5px;',
                        listeners: {
                            tap:function() {
                                hidePage('#loanAgreement');
                            }
                        }
                    }
                ]
            },
            {
                html:'<div id="loanACon" style="margin:0 15px;"></div>'
            }
        ],
        listeners:{
            show:function () {
                whir.loading.add("", 1);
                Comm.ajaxPost(
                    '/reg/getRegClause',
                    {type:'0'},
                    function (response) {
                        whir.loading.remove();
                        var msg = eval('('+response.responseText+')');
                        console.log(msg);
                        if(msg.retCode == "SUCCESS"){
                            var content = msg.retData.content;
                            var loanACon = Ext.query('#loanACon')[0];
                            loanACon.innerHTML = content;
                        }
                    }
                )
            }
        }
    }
})
