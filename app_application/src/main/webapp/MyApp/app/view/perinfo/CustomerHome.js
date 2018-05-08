/**
 * 客户信息
 * Created by zl on 2017/11/23 0023.
 */
Ext.define("MyApp.view.perinfo.CustomerHome",{
    extend:'Ext.tab.Panel',
    xtype:'custHomeView',
    requires:[
        'Ext.Panel',
        'MyApp.view.perinfo.PersonalInfo',
        'MyApp.view.perinfo.BasicInfo',
        'MyApp.view.perinfo.Authration'
    ],
    config:{
        id:'custHomeView',
        zIndex:5,
        tabBarPosition : 'top',
        showAnimation : {
            type:'slide', direction : 'left'
        },
        items:[
            {
                xtype:'toolbar',
                title:'客户信息',
                docked : "top",
                style:'background-color:#FEE300;border-radius:0;',
                items:[
                    {
                        html: '<span class="icon-back" style="font-size:26px;color:#000;"></span>',
                        style:'margin-left:10px;color:#8c8c8c;margin-top:8px;',
                        listeners:{
                            tap:function () {
                                hidePage('#custHomeView');
                            }
                        }
                    },
                    {
                        xtype:'spacer'
                    },
                    {
                        html:'<span style="font-size:16px;color:#000;">提交</span>',
                        id:'custHomeBtn',
                        listeners:{
                            tap:function () {
                                var basicBadge = document.getElementById('basic-badge'),
                                    authBadge = document.getElementById('auth-badge');
                                 var  phoneFlag = Ext.getCmp('phoneFlag').getValue();
                                if(basicBadge.style.display==='block'){
                                    layer.open({content:'请完善基本信息',skin:'msg',time:2});
                                    return
                                }
                                if(authBadge.style.display==='block' || phoneFlag!='1'){
                                    layer.open({content:'请完善授权信息',skin:'msg',time:2});
                                    return;
                                }
                                var orderId = Ext.getCmp('order_id').getValue();
                                whir.loading.add("", 1);
                                Comm.ajaxPost(
                                    '/employeeOrder/updateOrderStateBeforeSubmit',
                                    {orderId:orderId,state:'12'},
                                    function (response) {
                                        whir.loading.remove();
                                        var msg = eval('('+response.responseText+')');
                                        console.log(msg);
                                        if(msg.retCode == "SUCCESS"){
                                            hidePage('#custHomeView');
                                            if(Ext.getCmp('allOrderView')){
                                                Ext.getCmp('allOrderView').toGetApplyOrder();
                                            }
                                            if(Ext.getCmp('OrderDetailsView')){
                                                document.getElementById('custmoerBtn').style.display="none";
                                                orderDetailThis._getOrderDetail();
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                ]
            },
            {
                title:'<span class="custHomeTitle">个人信息</span>',
                itemId:'1',
                xtype:'personalView'
            },
            {
                title:'<span class="custHomeTitle">基本信息</span><span id="basic-badge" class="mui-badge mui-badge-danger">待完善</span>',
                itemId:'2',
                xtype:'basicView'
            },
            {
                title:'<span class="custHomeTitle">授权信息</span><span id="auth-badge" class="mui-badge mui-badge-danger">待完善</span>',
                itemId:'3',
                xtype:"authView"
            }
        ],
        listeners:{
            initialize:function () {
                this.getTabBar().setHeight(36);
            }
        }
    },
})
