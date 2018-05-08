/**
 * 商品串号码
 * Created by zl on 2017/12/8 0008.
 */
Ext.define('MyApp.view.orderManager.NumberGoods',{
    extend:'Ext.Panel',
    xtype:'numGoodsView',
    requires:[
        'Ext.Panel'
    ],
    config:{
        id:'numGoodsView',
        showAnimation:{type:'slide', direction:'left'},
        layout:{type:'vbox'},
        zIndex:5,
        items:[
            {
                xtype:'toolbar',
                title:'商品串号码',
                docked : "top",
                style:'background-color:#FEE300;border-radius:0;',
                items:[
                    {
                        html: '<span class="icon-back" style="font-size:26px;color:#000;"></span>',
                        style:'margin-left:10px;color:#8c8c8c;margin-top:8px;',
                        listeners:{
                            tap:function () {
                                hidePage('#numGoodsView');
                            }
                        }
                    },
                    {xtype:'spacer'},
                    {
                        html:'<span style="font-size:16px;color:#000;">提交</span>',
                        listeners:{
                            tap:function () {
                                Ext.getCmp('numGoodsView').submitGoodsNum();
                            }
                        }
                    }
                ]
            },
            {
               xtype:'formpanel',
               id:'numGoodsForm',
                scrollable:{direction: 'vertical',indicators: false},
                flex:9,
                items:[
                    {
                        xtype:'textfield',
                        id:'numGoods',
                        name:'numGoods',
                        placeHolder:'输入商品串号码',
                        maxLength:30,
                        style:'margin-top:5px;',
                    }
                ]
            }
        ]
    },
    submitGoodsNum:function () {
        var numValue = Ext.getCmp('numGoods').getValue();
        var reg = /^[0-9a-zA-Z\_\-]+$/;
        if(numValue == ""){
            layer.open({content:'商品串号码不能为空',skin:'msg',time:2});
            return
        }else if(!reg.test(numValue)){
            layer.open({content:'商品串号码不能输入特殊字符',skin:'msg',time:2});
            return
        }
        var orderId = Ext.getCmp('order_id').getValue();
        whir.loading.add("", 1);
        Comm.ajaxPost(
            '/employeeOrder/submitMerchandiseCode',
            {orderId:orderId,merCode:numValue,state:'20'},
            function (response) {
                whir.loading.remove();
                var msg = eval('(' + response.responseText + ')');
                console.log(msg);
                if(msg.retCode == "SUCCESS"){
                    if(Ext.getCmp('OrderManaView')){
                        orderMaThis._getAllOrder();
                    }
                    if(Ext.getCmp('OrderDetailsView')){
                        document.getElementById('toSubmerCode').style.display = 'none';
                    }
                    hidePage('#numGoodsView');
                }
            }
        )
    }
});
