/**
 * 申请管理
 */
Ext.define('MyApp.view.applyManager.Allorder',{
    extend:'Ext.Panel',
    xtype:'allOrderView',
    requires:[
        'Ext.Panel',
        'MyApp.view.applyManager.ApplySearch',
        'MyApp.view.applyManager.OrderDetails',
        'MyApp.view.perinfo.IdentityAuth',
        'MyApp.view.perinfo.CustomerHome',
        'MyApp.view.applyManager.ImageData'
    ],
    config:{
        id:'allOrderView',
        //zIndex:5,
        layout:{type:'vbox'},
        scrollable:{direction: 'vertical',indicators: false},
        showAnimation : {type:'slide', direction : 'right'},
        items:[
            {
                xtype:'container',
                docked:'top',
                style:'background-color:#fff;padding-bottom:10px;padding-top:25px;',
                items:[
                    {
                        html:'<div class="pro-header">'+
                        '<div class="lf" onclick="allOrderThis.getMerchanList()"><span class="icon-store"></span></div>'+
                        '<div class="md" onclick="allOrderThis.showSearch()"><span class="icon-search"></span><span>申请管理</span></div>'+
                        '<div class="rt" id="exitApp1" onclick="allOrderThis.exitApp()"><span class="icon-exit"></span></div>'+
                        '<div id="backApply" onclick="allOrderThis._backPro()" style="display:none;width:80px;height:30px;line-height:30px;text-align:center;font-size:14px;color:#fff;">取消</div>'+
                        '</div>'
                    }
                ]
            },
            {
                xtype:'container',
                docked:'top',
                items:[
                    {
                        html:'<div style="border-top:1px solid #f6f6f6;" class="title-wrapper">' +
                            '<div onclick="allOrderThis.tabOrderTitle(this,1)" class="all-order-title active"><span>全部</span><span id="listAllNum">(0)</span></div>'+
                            '<div onclick="allOrderThis.tabOrderTitle(this,2)" class="all-order-title"><span>待提交</span><span id="uncomNum">(0)</span></div>'+
                            '<div onclick="allOrderThis.tabOrderTitle(this,3)" class="all-order-title"><span>审核中</span><span id="InreviewNum">(0)</span></div>'+
                            '<div onclick="allOrderThis.tabOrderTitle(this,4)" class="all-order-title"><span>未通过</span><span id="rejectNum">(0)</span></div>'+
                        '</div>'
                    }
                ]
            },
            {
                xtype:'container',
                style:'margin-top:5px;',
                items:[
                    {
                        html:'<div id="applyOrder" class="all-order-wrapper"></div>'
                    },
                    {}
                ]
            }
        ],
        listeners:{
            show:function () {
                Ext.getCmp('allOrderView').toGetApplyOrder();
            }
        }
    },
    initialize:function() {
        allOrderThis = this;
        this.allList = [];//全部订单
        this.inReviewList = [];//审核中
        this.rejectList = [];//内拒单
        this.uncomList = [];//待提交
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
                            html:'<span style="position:absolute;top:0;right:-15px;width:30px;height:30px;line-height:30px;" onclick="allOrderThis._overlayHide()" class="serve-close">&times;</span>'
                        },
                        {
                            html:'<span>   </span>'
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
    _overlayHide:function () {
        this.overlay.destroy();
    },
    artTemplate:function (list) {
        var data = new Object();
        data.list = list;
        var html = template('test',data);
        document.getElementsByClassName('merchans')[0].innerHTML = html;
    },
    //查询所有的订单并展示
    toGetApplyOrder:function () {
        whir.loading.add("", 1);
        var me = this;
        var employeeId = Ext.getCmp('salesmanId').getValue();
        Comm.ajaxPost(
            '/employeeOrder/getAllOrder',
            {employeeId:employeeId,type:'sqgl'},
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                console.log(msg);
                if(msg.retCode == "SUCCESS"){
                    var data = msg.retData;
                    //全部订单
                    var listAll = data.listAll,//所有订单
                        listInReview = data.listInReview,//审核中
                        listReject = data.listReject,//内拒单
                        listUncom = data.listUncom;//待提交
                    me.inReviewList = listInReview;
                    me.rejectList = listReject;
                    me.listUncom = listUncom;
                    me.allList = listAll;
                    me.showAllApply(listAll);
                    //显示顶部标题的数量
                    me.addTitleNum(listAll,listInReview,listReject,listUncom);
                }
            }
        )
    },
    //展示所有申请中订单
    showAllApply:function (listAll) {
        var orderData = {};
        orderData.orderList = listAll;
        allOrderThis.orderTemplate(orderData);
        allOrderThis.addFirstActive();
    },
    toMarkUser:function (e) {
        var flag = e.getAttribute('data-flag');
        var child = e.parentNode.nextSibling.children;
        if(flag === 'true'){
            e.previousSibling.setAttribute('class','active icon-activeMark');
            e.innerHTML = '已标记';
            e.setAttribute('class','mark-text active');
            e.setAttribute('data-flag','false');
            child[0].setAttribute('data-isSign','1');
        }else{
            e.previousSibling.setAttribute('class','icon-activeMark');
            e.innerHTML = '标记用户';
            e.setAttribute('class','mark-text');
            e.setAttribute('data-flag','true')
            child[0].setAttribute('data-isSign','0');
        }
    },
    orderTemplate:function (orderData) {
        var html = template('order',orderData);
        document.getElementById('applyOrder').innerHTML = html;
    },
    //给订单添加数量
    addTitleNum:function (listAll,listInReview,listReject,listUncom) {
        var listAllNum = document.getElementById('listAllNum'),
            InreviewNum = document.getElementById('InreviewNum'),
            uncomNum = document.getElementById('uncomNum'),
            rejectNum = document.getElementById('rejectNum');
        listAllNum.innerHTML = '('+listAll.length+')';
        InreviewNum.innerHTML = '('+listInReview.length+')';
        uncomNum.innerHTML = '('+listUncom.length+')';
        rejectNum.innerHTML = '('+listReject.length+')';
    },
    //切换不同的订单菜单
    tabOrderTitle:function (e,index) {
        var control = this;
        control.addActive(e);
        switch (index)
        {
            case 1:
                var orderData = {};
                orderData.orderList=control.allList;
                control.orderTemplate(orderData);
                break;
            case 2:
                var orderData = {};
                orderData.orderList=control.listUncom;
                control.orderTemplate(orderData);
                break;
            case 3:
                var orderData = {};
                orderData.orderList=control.inReviewList;
                control.orderTemplate(orderData);
                break;
            case 4:
                var orderData = {};
                orderData.orderList=control.rejectList;
                control.orderTemplate(orderData);
                break;
        }
    },
    //标题添加激活状态
    addActive:function (e) {
        var child = e.parentNode.children;
        for(var i=0,l=child.length;i<l;i++){
            var item = child[i];
            if(e != item){
                e.setAttribute('class','active all-order-title');
                item.setAttribute('class','all-order-title');
            }
        }
    },
    //页面重刷后将激活状态给到全部
    addFirstActive:function () {
        var itemDom = document.getElementsByClassName('all-order-title')[0];
        var className = itemDom.className;
        var parent = itemDom.parentNode;
        var children = parent.children;
        if(className.indexOf('active')<0){
            itemDom.setAttribute('class','all-order-title active');
            for(var i=0,len=children.length;i<len;i++){
                var child = children[i];
                if(child != itemDom){
                    child.setAttribute('class','all-order-title')
                }
            }
        }else{
            return
        }
    },
    showSearch:function () {
        if(Ext.getCmp('applySearchView')){
            Ext.getCmp('applySearchView').show();
        }else{
            Ext.Viewport.add(Ext.create('MyApp.view.applyManager.ApplySearch')).show();
        }
    },
    //展示详情页面
    toOrderDeatil:function (me) {
        Ext.getCmp('order_id').setValue(me.getAttribute('data-id'));
       if(Ext.getCmp('OrderDetailsView')){
           Ext.getCmp('OrderDetailsView').show();
       }else{
         Ext.Viewport.add(Ext.create('MyApp.view.applyManager.OrderDetails')).show();
       }
    },
    //完善客户信息
    toCompleteInfo:function (e) {
        event.stopPropagation();
        whir.loading.add("", 1);
        var orderId = e.getAttribute('data-id');
        Ext.getCmp('order_id').setValue(orderId);
        Comm.ajaxPost(
            '/employeeOrder/improveCustomerInformation',
            {orderId:orderId},
            function (response) {
                whir.loading.remove();
                var msg = eval('(' + response.responseText + ')');
                console.log(msg);
                if(msg.retCode == "FAIL"){
                    if(Ext.getCmp('IdentityAuthenticationView')){
                        Ext.getCmp('IdentityAuthenticationView').show();
                    }else{
                        Ext.Viewport.add(Ext.create('MyApp.view.perinfo.IdentityAuth')).show();
                    }
                }else{
                    if(Ext.getCmp('custHomeView')){
                        Ext.getCmp('custHomeView').show();
                    }else{
                        Ext.Viewport.add(Ext.create('MyApp.view.perinfo.CustomerHome')).show();
                    }
                }
            }
        );
    },
    //手签
    toSign:function (me) {
        var orderId = me.getAttribute('data-id');
        var option = [orderId];
        var ua = navigator.userAgent.toLowerCase();
        if (/iphone|ipad|ipod/.test(ua)) {
            device.getWritingSignature(allOrderThis.signSuccess,allOrderThis.signError,option);
        }else if(/android/.test(ua)){
            device.signature(allOrderThis.signSuccess,allOrderThis.signError,option);
        }
    },
    //回到展示所有商品页面
    _backPro:function () {
        document.getElementById('exitApp1').style.display = 'block';
        document.getElementById('backApply').style.display = 'none';
        //this._getMerchandiseList();
    },
    toImageData:function (me) {
        event.stopPropagation();
        Ext.getCmp('order_id').setValue(me.getAttribute('data-id'));
        if(Ext.getCmp('uploadView')){
            Ext.getCmp('uploadView').show();
        }else{
            Ext.Viewport.add(Ext.create('MyApp.view.applyManager.ImageData')).show();
        }
    },
    //提交
    toSubmit:function (me) {
        var orderId = me.getAttribute('data-id'),
            state = '15',
            isSign = me.getAttribute('data-isSign');
        whir.loading.add("", 1);
        Comm.ajaxPost(
            '/employeeOrder/submitUpdateState',
            {orderId:orderId,state:state,isSign:isSign},
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                if(msg.retCode == "SUCCESS"){
                    Ext.getCmp('allOrderView').toGetApplyOrder();
                }
            }
        )
    },
    exitApp:function () {
        if(Ext.getCmp('home')){
            Ext.getCmp('home').destroy();
        }
        Ext.Viewport.setActiveItem('loginView');
    },
    toOrderBack:function (me) {
        var orderId = me.getAttribute('data-id');
        var state = '14';
        Comm.ajaxPost(
            '/employeeOrder/updateOrderStateBeforeSubmit',
            {orderId:orderId,state:state},
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                if(msg.retCode == "SUCCESS"){
                    Ext.getCmp('allOrderView').toGetApplyOrder();
                }
            }
        )
    },
    signSuccess:function (msg) {
        Ext.getCmp('allOrderView').toGetApplyOrder();
    },
    signError:function (msg) {
        // alert('error');
    }
});