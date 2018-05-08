/**
 * 主页
 * Created by zl on 2017/11/21 0021.
 */
Ext.define('MyApp.view.Home',{
    extend:'Ext.tab.Panel',
    xtype:'home',
    requires:[
        'MyApp.view.proManage.Product',
        'MyApp.view.applyManager.Allorder',
        'MyApp.view.orderManager.OrderManager',
        // 'MyApp.view.MiddleSave',
        //'MyApp.view.login',
        // 'MyApp.view.orders.CenterMsg'
    ],
    config:{
        tabBarPosition : 'bottom',
        showAnimation:{type:'slide', direction:'right'},
        id:'home',
        items:[
            {
                title:'商品管理',
                iconCls:'shop',
                xtype:'productView'
            },
            {
                title:'申请管理',
                iconCls:'notice',
                xtype:'allOrderView'
            },
            {
                title:'订单管理',
                iconCls:'myorder',
                xtype:'OrderManaView'
            }
        ],
        listeners:{
            show:function () {
                //proThis._initMui();
                Ext.getCmp('productView')._getMerchandiseList()
            }
        }
    }
});
