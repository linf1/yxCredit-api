/**
 * 订单管理界面
 * Created by zl on 2017/12/7 0007.
 */
Ext.define('MyApp.view.orderManager.OrderManager',{
   extend:'Ext.Panel',
   xtype:'OrderManaView',
   requires:[
       'Ext.Panel',
       'MyApp.view.orderManager.OpenAccount',
       'MyApp.view.orderManager.UploadPhoto',
       'MyApp.view.orderManager.NumberGoods',
       'MyApp.view.orderManager.orderSearch'
   ],
   config:{
       id:'OrderManaView',
       showAnimation:{type:'slide', direction:'left'},
       scrollable:{direction: 'vertical',indicators: false},
       zIndex:5,
       layout:{type:'vbox'},
       items:[
           {
               xtype:'container',
               docked:'top',
               style:'background-color:#fff;padding-bottom:10px;border-bottom:1px solid #f6f6f6;padding-top:25px;',
               items:[
                   {
                       html:'<div class="pro-header">'+
                       '<div class="lf" onclick="orderMaThis.getMerchanList()"><span class="icon-store"></span></div>'+
                       '<div class="md" onclick="orderMaThis.showSearch()"><span class="icon-search"></span><span>订单管理</span></div>'+
                       '<div class="rt" onclick="orderMaThis.exitApp()"><span class="icon-exit"></span></div>'+
                       '</div>'
                   }
               ]
           },
           {
               xtype:'container',
               docked:'top',
               items:[
                   {
                       html:'<div class="mana-title-wrapper">' +
                       '<div onclick="orderMaThis._tabOrderTitle(this,1)" class="mana-all-order-title active"><span>全部</span><span id="allOrderBtn">(0)</span></div>'+
                       /*'<div onclick="orderMaThis._tabOrderTitle(this,2)" class="mana-all-order-title"><span>待收预付</span><span>(2)</span></div>'+*/
                       '<div onclick="orderMaThis._tabOrderTitle(this,2)" class="mana-all-order-title"><span>待发货</span><span id="deliverBtn">(0)</span></div>'+
                       '<div onclick="orderMaThis._tabOrderTitle(this,3)" class="mana-all-order-title"><span>已发货</span><span id="delivedBtn">(0)</span></div>'+
                       '</div>'
                   }
               ]
           },
           {
               xtype:'container',
               style:'margin-top:5px;',
               items:[
                   {
                       html:'<div id="allOrderMana" class="all-order-wrapper">' +
                       /*'<div class="order-item">' +
                            '<div class="title"><span class="time">2017-08-28 08:56</span><div class="id-wrapper"><span>申请ID:</span><span class="order-id">658295478</span></div></div>'+
                            '<div class="content">' +
                                '<div class="person-name">陈飞飞</div>'+
                                '<div class="desc">' +
                                    '<div class="pro-name">IPHONE8苹果(Apple)苹果手机</div>'+
                                    '<div class="pro-capacity"><span>32G</span><span>金色</span></div>'+
                                    '<div class="money-desc"><span>￥480</span><span>×</span><span>18期</span></div>'+
                                '</div>'+
                            '</div>'+
                            //'<div class="complete-order"><button class="mui-btn">绑定银行卡</button></div>'+
                           //'<div class="complete-order"><p class="desc-wrapper"><span class="complete-order-desc">待收预付款</span><span class="complete-order-money">￥1000.00</span></p><button class="mui-btn">收取预付款</button></div>'+
                           '<div class="complete-order spec">' +
                                '<div class="payment-wrapper"><span class="desc">预付款以收取</span><span class="pay-money">￥1000.00</span></div>' +
                                '<div class="btn-wrapper"><button class="mui-btn">上传发货照片</button></div>'+
                           '</div>'+
                       '</div>'+*/
                       '</div>'
                   }
               ]
           }
       ],
       listeners:{
           show:function () {
               orderMaThis._getAllOrder();
               //orderMaThis.refreshOrder();
           }
       }
   },
    initialize:function() {
        orderMaThis = this;
        this.allOrderList = [];//全部
        this.deliverList = [];//待发货
        this.deliveredList = [];//已发货
    },
    //定时刷新页面
    refreshOrder:function () {
        setTimeout(function () {
            orderMaThis._getAllOrder();
            setTimeout(arguments.callee,30000);
        },30000)
    },
    overlayShow:function () {
        var width=window.screen.width;
        this.overlay = Ext.Viewport.add({
            xtype: 'panel',
            left: (width-width*0.8)/2,
            top: '20%',
            modal: true,
            zIndex:7,
            hideOnMaskTap: false,
            hidden: true,
            width: "80%",
            height: '400px',
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
                    xtype:'container',
                    style:'background:#fff;padding:20px 0 0;',
                    docked:'top',
                    cls:'overCon',
                    items:[
                        {
                            xtype:'searchfield',
                            style:'border:none;width:80%;margin:0 auto;',
                            html:'<span style="position:absolute;top:10px;left:20px;font-size:20px;" class="icon-search"></span>',
                            placeHolder:'商户名称',
                            listeners:{
                                change:function (me, newValue, oldValue, eOpts) {
                                    viewLoginThis.searchRes(newValue);
                                },
                                keyup:function (me, e, eOpts) {
                                    if(me._value == ""){
                                        var str = me._value;
                                        viewLoginThis.searchRes(str);
                                    }
                                }
                            }
                        },
                        {
                            html:'<span style="position:absolute;top:0;right:-15px;width:30px;height:30px;line-height:30px;" onclick="orderMaThis._overlayHide()" class="serve-close">&times;</span>'
                        }
                    ]
                },
                {
                    xtype:'panel',
                    items:[
                        {
                            html:'<div class="merchans">' +

                            '</div>'
                        }
                    ]
                }
            ]
        });
        this.overlay.show({type: 'fadeIn'});
    },
    _overlayHide:function () {
        this.overlay.destroy();
    },
    getMerchanList:function () {
        whir.loading.add("", 1);
        var me = this;
        var salesmanId = Ext.getCmp('salesmanId').getValue();
        Comm.ajaxPost(
            '/merchant/getMerchantList',
            {salesmanId:salesmanId},
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                console.log(msg);
                if(msg.retCode == "SUCCESS"){
                    var merchanList = msg.retData;
                    me.overlayShow();
                    me.artTemplate(merchanList);
                }
            }
        )
    },
    artTemplate:function (list) {
        var data = new Object();
        data.list = list;
        var html = template('test',data);
        document.getElementsByClassName('merchans')[0].innerHTML = html;
    },
    //得到全部订单
    _getAllOrder:function () {
        whir.loading.add("", 1);
        var me = this;
        var employeeId = Ext.getCmp('salesmanId').getValue();
        Comm.ajaxPost(
            '/employeeOrder/getAllOrder',
            {employeeId:employeeId,type:'ddgl'},
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                console.log(msg);
                if(msg.retCode == "SUCCESS"){
                    var data = msg.retData;
                    var approvalList = data.approvalList,//所有订单
                        deliverList = data.deliverList,//待发货
                        deliveredList = data.deliveredList;//已发货
                    me.allOrderList = approvalList;//全部
                    me.deliverList = deliverList;//待发货
                    me.deliveredList = deliveredList;//已发货
                    me._showAllOrder(approvalList);
                    me.addTitleNum(approvalList,deliverList,deliveredList)
                }
            }
        )
    },
    _showAllOrder:function (approvalList) {
        var orderData = {};
        orderData.orderMaList = approvalList;
        orderMaThis._orderTemplate(orderData);
        orderMaThis._addFirstActive();
    },
    //切换Tab
    _tabOrderTitle:function (e,index) {
        var control = this;
        control._addActive(e);
        switch (index)
        {
            case 1:
                var orderData = {};
                orderData.orderMaList=control.allOrderList;
                control._orderTemplate(orderData);
                break;
            case 2:
                var orderData = {};
                orderData.orderMaList=control.deliverList;
                control._orderTemplate(orderData);
                break;
            case 3:
                var orderData = {};
                orderData.orderMaList=control.deliveredList;
                control._orderTemplate(orderData);
                break;
        }
    },
    //给订单添加数量
    addTitleNum:function (approvalList,deliverList,deliveredList) {
        var allOrderBtn = document.getElementById('allOrderBtn'),
            deliverBtn = document.getElementById('deliverBtn'),
            delivedBtn = document.getElementById('delivedBtn');
        allOrderBtn.innerHTML = '('+approvalList.length+')';
        deliverBtn.innerHTML = '('+deliverList.length+')';
        delivedBtn.innerHTML = '('+deliveredList.length+')';
    },
    //标题添加激活状态
    _addActive:function (e) {
        var child = e.parentNode.children;
        for(var i=0,l=child.length;i<l;i++){
            var item = child[i];
            if(e != item){
                e.setAttribute('class','active mana-all-order-title');
                item.setAttribute('class','mana-all-order-title');
            }
        }
    },
    _orderTemplate:function (orderData) {
        var html = template('lala',orderData);
        document.getElementById('allOrderMana').innerHTML = html;
    },
    _addFirstActive:function () {
        var itemDom = document.getElementsByClassName('mana-all-order-title')[0];
        var className = itemDom.className;
        var parent = itemDom.parentNode;
        var children = parent.children;
        if(className.indexOf('active')<0){
            itemDom.setAttribute('class','mana-all-order-title active');
            for(var i=0,len=children.length;i<len;i++){
                var child = children[i];
                if(child != itemDom){
                    child.setAttribute('class','mana-all-order-title')
                }
            }
        }else{
            return
        }
    },
    //开户
    toOpenAccount:function (me) {
        Ext.getCmp('order_id').setValue(me.getAttribute('data-id'));
        if(Ext.getCmp('openAccountView')){
            Ext.getCmp('openAccountView').show();
        }else{
            Ext.Viewport.add(Ext.create('MyApp.view.orderManager.OpenAccount')).show();
        }
    },
    //收取预付款
    toCollectMoney:function (me) {
        var money = me.getAttribute('data-money');
        var custName = me.getAttribute('data-name');
        var orderId = me.getAttribute('data-id');
        mui.confirm('确认预付款您已收取?','你的客户'+custName+'需要于门店支付预付款'+money+'元',['已收取','暂未收取'],function (obj) {
            console.log(obj);
            if(obj.index == 0){
                whir.loading.add("", 1);
                Comm.ajaxPost(
                    '/employeeOrder/updateOrderStateBeforeSubmit',
                    {orderId:orderId,state:'18'},
                    function (response) {
                        whir.loading.remove();
                        var msg = eval('(' + response.responseText + ')');
                        console.log(msg);
                        if(msg.retCode == "SUCCESS"){
                            orderMaThis._getAllOrder();
                        }
                    }
                )
            }
        },'div');
    },
    toNumGoods:function (me) {
        Ext.getCmp('order_id').setValue(me.getAttribute('data-id'));
        if(Ext.getCmp('numGoodsView')){
            Ext.getCmp('numGoodsView').show();
        }else{
            Ext.Viewport.add(Ext.create('MyApp.view.orderManager.NumberGoods')).show();
        }
    },
    //上传发货照片
    toDeliveGoods:function (me) {
        Ext.getCmp('order_id').setValue(me.getAttribute('data-id'));
        if(Ext.getCmp('uploadGoodsView')){
            Ext.getCmp('uploadGoodsView').show();
        }else{
            Ext.Viewport.add(Ext.create('MyApp.view.orderManager.UploadPhoto')).show();
        }
    },
    toOrderDetail:function (me) {
        Ext.getCmp('order_id').setValue(me.getAttribute('data-id'));
        if(Ext.getCmp('OrderDetailsView')){
            Ext.getCmp('OrderDetailsView').show();
        }else{
            Ext.Viewport.add(Ext.create('MyApp.view.applyManager.OrderDetails')).show();
        }
    },
    //展示搜索页面
    showSearch:function () {
        if(Ext.getCmp('orderSearch')){
            Ext.getCmp('orderSearch').show();
        }else{
            Ext.Viewport.add(Ext.create('MyApp.view.orderManager.orderSearch')).show();
        }
    },
    exitApp:function () {
        if(Ext.getCmp('home')){
            Ext.getCmp('home').destroy();
        }
        Ext.Viewport.setActiveItem('loginView')
    }
});
