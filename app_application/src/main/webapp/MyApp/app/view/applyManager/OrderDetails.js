/**
 * 订单详情
 */
Ext.define('MyApp.view.applyManager.OrderDetails',{
    extend: 'Ext.Panel',
    xtype: 'OrderDetailsView',
    requires:[
        'Ext.Panel',
        'Ext.Img',
        'MyApp.view.applyManager.ViewImgData',
        'MyApp.view.applyManager.HandSign',
        'MyApp.view.orderManager.SupplyPhoto'
    ],
    config: {
        id: 'OrderDetailsView',
        showAnimation:{type:'slide', direction:'right'},
        layout:{type:'vbox'},
        zIndex:5,
        style:'background:#F6F6F6',
        items: [
            {
                xtype: 'toolbar',
                title: '<span>订单详情</span>',
                style: 'background-color: #fff;',
                items:[
                    {
                        html: '<span class="icon-back" style="font-size:26px;color:#000;"></span>',
                        style:'margin-left:10px;color:#8c8c8c;margin-top:5px;',
                        listeners:{
                            tap: function() {
                                if(Ext.getCmp('allOrderView')){
                                    Ext.getCmp('allOrderView').toGetApplyOrder();
                                }
                                if(Ext.getCmp('OrderManaView')){
                                    orderMaThis._getAllOrder();
                                }
                                hidePage('#OrderDetailsView');
                            }
                        }
                    }
                ]
            },
            {
                xtype:'img',
                id:'orderStateImg',
                src:'',
                style:'background-size:cover;background-size:100% 100%;height:90px;'
            },
            {
                xtype:"panel",
                scrollable:{direction: 'vertical',indicators: false},
                flex:1,
                style:'margin-top:5px;',
                items:[
                    {
                        html:'<div class="orderDetailsInfoContainer">' +
                        '<div class="orderDetailsInfo">' +
                            '<p><img class="user-avatar" src="resources/images/user_avatar.png" width="16"/><span id="custNameD" class="userName"></span><span id="alertTime" class="rightWord"></span></p>' +
                            '<p><span id="orderNo"></span></p>' +
                        '</div>' +
                        '<div class="orderDetailsInfo">' +
                            '<div class="orderInfo" style="padding: 10px 15px">' +
                                '<div class="orderInfoItems"><img id="proImg" src="" width="70"/></div>' +
                                '<div class="orderInfoItems">' +
                                    '<p id="proBrandName"></p>' +
                                    '<p style="margin:5px 0"><span id="merchandiseModelName" class="info Details"></span><span id="merchandiseVersionName" class="info Details"></span></p>' +
                                    '<p><span id="proAmount" class="moneyInfo"></span></p>' +
                                '</div>' +
                            '</div>'+
                        '</div>' +
                        '<div class="orderDetailsInfo">' +
                            '<p><span>首付</span><span id="downPayMoney" class="rightWord"></span></p>'+
                            '<p><span>申请分期金额</span><span id="fenqiMoney" class="rightWord"></span></p>'+
                            '<p><span>申请期数</span><span id="payPeriods" class="rightWord"></span></p>'+
                            '<p><span>每期还款</span><span id="monthPay" class="rightWord"></span></p>'+
                            '<p onclick="orderDetailThis.overlayShow()"><span>服务包</span><span style="float:right;margin-top:6px;" class="icon-more"></span><span id="serverPackNum" style="color:#ccc;" class="rightWord">共1款服务包</span></p>'+
                        '</div>'+
                        '<div id="customerInfo" onclick="orderDetailThis.toViewCustInfo()" class="order-footer-btn"><span class="text-desc">查看客户信息</span><span class="icon-more"></span></div>'+
                        '<div id="imgInfo" onclick="orderDetailThis.toViewImgData()" class="order-footer-btn"><span class="text-desc">查看影像资料</span><span class="icon-more"></span></div>'+
                        '<div id="signInfo" onclick="orderDetailThis.seeHandSign()" class="order-footer-btn"><span class="text-desc">查看手签图片</span><span class="icon-more"></span></div>'+
                        '<div id="alreadyMoney" class="order-footer-btn"><span class="text-desc">预付款已收取</span><span id="alreadyCharge" style="float:right;margin-right:15px;height:50px;line-height:50px;color:#ff6600;">￥1000</span></div>'+
                        '<div id="goodsCodeNum" class="order-footer-btn"><span class="text-desc">商品串号码</span><span id="detailNum" style="float:right;margin-right:15px;height:50px;line-height:50px;color:#ff6600;">￥1000</span></div>'+
                        '<div id="serverPackInfo" class="order-footer-btn"><span style="float:right;margin-right:15px;height:50px;line-height:50px;color:#ff6600;">等待客户支付前置服务包</span></div>'+
                        '<div id="deliverImg1" onclick="orderDetailThis.supplyImg()" class="order-footer-btn"><span class="text-desc">补充发货照片</span><span class="icon-more"></span></div>'+
                        '<div id="sureHeTon" class="order-footer-wrapper"><span style="float:right;margin-right:15px;height:50px;line-height:50px;color:#ff6600;">等待客户确认合同</span></div>'+
                        '<div id="collectMoney" class="order-footer-btn"><span class="text-desc">待收取预付款</span><span id="advanCharge" style="float:left;height:50px;line-height:50px;color:#ff6600;"></span><button id="getChargeBtn" onclick="orderDetailThis.collectCharge(this)" data-name data-money style="float:right;margin-right:15px;margin-top:8px;border:1px solid #ff6600;color:#ff6600;border-radius:15px;" class="mui-btn">待收取预付款</button></div>'+
                        '<div id="custmoerBtn" class="order-footer-wrapper"><button onclick="orderDetailThis.completeInfo()" class="mui-btn" style="margin:8px 15px 5px 0;float:right;border-radius:15px;border:1px solid #ff6600;color:#ff6600;">完善客户信息</button></div>'+
                        '<div id="imgBtn" class="order-footer-wrapper"><button onclick="orderDetailThis.toImgData()" class="mui-btn" style="margin:8px 5px 5px 0;float:right;border-radius:15px;border:1px solid #ff6600;color:#ff6600;">补充影像资料</button></div>'+
                        '<div id="signBtn" class="order-footer-wrapper"><button onclick="orderDetailThis.toSign()" class="mui-btn" style="margin:8px 5px 5px 0;float:right;border-radius:15px;border:1px solid #ff6600;color:#ff6600;">手签</button></div>'+
                        '<div id="openAccountBtn" class="order-footer-wrapper"><button onclick="orderDetailThis.toOpenAccount()" class="mui-btn" style="margin:8px 5px 5px 0;float:right;border-radius:15px;border:1px solid #ff6600;color:#ff6600;">绑定银行卡</button></div>'+
                        '<div id="toUploadBtn" class="order-footer-wrapper"><button onclick="orderDetailThis.toDeliver()" class="mui-btn" style="margin:8px 5px 5px 0;float:right;border-radius:15px;border:1px solid #ff6600;color:#ff6600;">上传发货照片</button></div>'+
                        '<div id="toSubmerCode" class="order-footer-wrapper"><button onclick="orderDetailThis.toSubCodeBtn()" class="mui-btn" style="margin:8px 5px 5px 0;float:right;border-radius:15px;border:1px solid #ff6600;color:#ff6600;">提交串号码</button></div>'+
                        '</div>'
                    },
                    {
                        xtype:'container',
                        docked:'bottom',
                        hidden:true,
                        id:'submitCon',
                        height:50,
                        style:'background:#fff;box-shadow: 0 1px 2px rgba(0,0,0,1);',
                        items:[
                            {
                                html:'<div class="bottom-wrapper"><p class="order-mark-content" data-flag="true" onclick="orderDetailThis._signUser(this)"><span class="icon-activeMark"></span><span class="mark-text">标记用户</span></p><button onclick="orderDetailThis._submitOrder()" class="subBtn">提交</button></div>'
                            }
                        ]
                    },
                    {
                        xtype:'container',
                        docked:'bottom',
                        hidden:true,
                        id:'aleadyCon',
                        height:50,
                        style:'background:#fff;box-shadow: 0 1px 2px rgba(0,0,0,1);',
                        items:[
                            {
                                html:'<div class="bottom-wrapper"><button style="border:1px solid #333;" onclick="orderDetailThis.toOrderBack()" class="withdraw mui-btn">撤回</button><span style="float:right;margin-top:15px;font-size:14px;margin-right:10px;">已提交,等待客户确认订单</span></div>'
                            }
                        ]
                    },
                    {
                        xtype:'container',
                        docked:'bottom',
                        hidden:true,
                        id:'refuseCon',
                        height:50,
                        style:'background:#fff;box-shadow: 0 1px 2px rgba(0,0,0,1);',
                        items:[
                            {
                                html:'<div class="bottom-wrapper" style="text-align: right;font-size: 14px;line-height: 50px;margin-right: 15px;height: 50px;"><p style="margin-top:0;" class="order-mark-content"><span class="active icon-activeMark"></span><span class="active mark-text">已标记</span></p><button onclick="orderDetailThis.toOrderBack()" style="border:1px solid #333;margin-right:0;" class="withdraw mui-btn">撤回</button><span style="float:right;font-size:12px;margin-right:10px;">已提交,等待客户确认订单</span></div>'
                            }
                        ]
                    },
                    {
                        xtype:'hiddenfield',
                        id:'signFlag'//是否标记用户
                    }
                ]
            }
        ],
        listeners:{
            show:function () {
                orderDetailThis._getOrderDetail();
            }
        }
    },
    initialize:function() {
        orderDetailThis = this;
        this.serPackageList = [];
    },
    //封装按钮显示函数
    _elShow:function (id) {
        return  document.getElementById(id).style.display = 'block';
    },
    overlayShow:function () {
        var width=window.screen.width;
        this.overlay = Ext.Viewport.add({
            xtype: 'panel',
            left: (width-width*0.8)/2,
            top: '20%',
            modal: true,
            zIndex:999,
            hideOnMaskTap: true,
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
                    style:'background:#fff;',
                    docked:'top',
                    cls:'overCon',
                    items:[
                        {
                            html:'<div style="height:44px;background:#fff;text-align:right;"><span onclick="orderDetailThis._overlayHide()" class="serve-close">&times;</span></div>'
                        }
                    ]
                },
                {
                    xtype:'panel',
                    items:[
                        {
                            html:'<div class="serverpack-wrapper">' +
                                '<div class="serverpack-tittle"><span class="lf"></span><span class="md">提前还款包</span><span class="rt"></span></div>' +
                                '<div class="preleadPack">' +
                                '</div>'+
                                '<div class="serverpack-tittle" style="margin-top:40px;"><span class="lf"></span><span class="md">其他服务包</span><span class="rt"></span></div>' +
                                '<div class="otherPack">' +
                                '</div>'+
                            '</div>'
                        }
                    ]
                }
            ],
            listeners:{
                show:function () {
                    var serPackageList = orderDetailThis.serPackageList;
                    var preleadPack = document.getElementsByClassName('preleadPack')[0];
                    var otherPack = document.getElementsByClassName('otherPack')[0];
                    serPackageList.forEach(function (item) {
                        if(item.packageType == "1"||item.packageType == '2'){
                            preleadPack.innerHTML += '<div class="pack-item">' +
                                '<div><span class="packageName">'+item.packageName+'</span><span class="collectionAmount">￥'+item.collectionAmount+'</span></div>'+
                                '<div><span class="qsRemark">'+item.qsRemark+'</span><span class="sqTypeRemark">'+item.sqTypeRemark+'</span></div>'+
                                '</div>'
                        }else if(item.packageType == '3'){
                            otherPack.innerHTML += '<div class="pack-item">' +
                                '<div><span class="packageName">'+item.packageName+'</span><span class="collectionAmount">￥'+item.collectionAmount+'/月</span></div>'+
                                '<div class="qsSqmark"><span>'+item.qsRemark+','+item.sqTypeRemark+'</span></div>'+
                                '</div>'
                        }
                    })
                }
            }
        });
        this.overlay.show({type: 'fadeIn', direction: 'up'});
    },
    _overlayHide:function () {
        this.overlay.destroy();
    },
    _getOrderDetail:function () {
        whir.loading.add("", 1);
        var me = this;
        var orderId = Ext.getCmp('order_id').getValue();
        var orderStateImg = Ext.getCmp('orderStateImg');
        Comm.ajaxPost(
            '/order/getFenqiOrderInfo',
            {orderId:orderId},
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                console.log(msg);
                if(msg.retCode == "SUCCESS"){
                    var data = msg.retData;
                    me._controlBtnShow(data,orderStateImg);
                    me._assignmentDetail(data);
                }
            }
        )
    },
    //赋值自上而下-----
    _assignmentDetail:function (data) {
        if(data){
            var custNameD = document.getElementById('custNameD'),//客户姓名
                alertTime = document.getElementById('alertTime'),//订单时间
                orderNo = document.getElementById('orderNo'),//申请ID
                proImg = document.getElementById('proImg'),//产品图片
                proBrandName = document.getElementById('proBrandName'),//产品名称
                merchandiseModelName = document.getElementById('merchandiseModelName'),//版本
                merchandiseVersionName = document.getElementById('merchandiseVersionName'),//型号
                proAmount = document.getElementById('proAmount'),//商品价格
                downPayMoney = document.getElementById('downPayMoney'),//首付
                fenqiMoney = document.getElementById('fenqiMoney'),//分期金额
                payPeriods = document.getElementById('payPeriods'),//申请期数
                monthPay = document.getElementById('monthPay'),//每月还款
                serverPackNum = document.getElementById('serverPackNum');//服务包数量
            var customerId = Ext.getCmp('customerId');
            customerId.setValue(data.customerId);
            custNameD.innerHTML = data.customerName;
            alertTime.innerHTML = data.alterTime;
            orderNo.innerHTML = '申请ID:'+data.orderNo;
            proImg.setAttribute('src',data.merchandiseUrl);
            proBrandName.innerHTML = data.merchandiseBrandName;
            merchandiseModelName.innerHTML = data.merchandiseModelName;
            merchandiseVersionName.innerHTML = data.merchandiseVersionName;
            proAmount.innerHTML = '商品价格: ￥'+data.amount;
            downPayMoney.innerHTML = '￥'+data.downPayMoney;
            fenqiMoney.innerHTML = '￥'+data.fenqiMoney;
            payPeriods.innerHTML = data.periods+'期';
            monthPay.innerHTML = '￥'+data.monthPay;
            serverPackNum.innerHTML = '共'+data.servicePagNum+'款服务包';
            var serPackageList = data.serPackageList;
            this.serPackageList = serPackageList;
        }
    },
    //标记用户
    _signUser:function (e) {
        var flag = e.getAttribute('data-flag');
        var children = e.children;
        if(flag === 'true'){
            children[0].setAttribute('class','active icon-activeMark');
            children[1].innerHTML = '已标记';
            children[1].setAttribute('class','mark-text active');
            e.setAttribute('data-flag','false')
            Ext.getCmp('signFlag').setValue('1');
        }else{
            children[0].setAttribute('class','icon-activeMark');
            children[1].innerHTML = '标记用户';
            children[1].setAttribute('class','mark-text');
            e.setAttribute('data-flag','true');
            Ext.getCmp('signFlag').setValue('0');
        }
    },
    //控制操作按钮显示
    _controlBtnShow:function (data,component) {
        if(data.state == '0'){
            if(data.commodityState == '11'){
                this._elShow('custmoerBtn');
                component.setSrc('resources/images/xxdws.png');
                return
            }else if(data.commodityState == '12'){
                this._elShow('customerInfo');
                this._elShow('imgBtn');
                component.setSrc('resources/images/xxyws.png');
                return
            }else if(data.commodityState == '13'){
                this._elShow('customerInfo');
                this._elShow('imgInfo');
                this._elShow('signBtn');
                component.setSrc('resources/images/xxyws.png');
                return
            }else if(data.commodityState == '14'){
                this._elShow('customerInfo');
                this._elShow('imgInfo');
                this._elShow('signInfo');
                Ext.getCmp('submitCon').show();
                component.setSrc('resources/images/dtj.png');
                return
            }else if(data.commodityState == '15'){
                this._elShow('customerInfo');
                this._elShow('imgInfo');
                this._elShow('signInfo');
                if(data.isSign == '0' || data.isSign == ''){
                    Ext.getCmp('aleadyCon').show();
                    component.setSrc('resources/images/ytj.png');
                }else if(data.isSign == '1'){
                    Ext.getCmp('refuseCon').show();
                    component.setSrc('resources/images/shwtg.png');
                }
            }else if(data.commodityState == '21')
            {
                component.setSrc('resources/images/tuihui.png'); //订单关闭
                return;
            }
        }
        if(data.state == "5"){
            if(data.commodityState == '16'){
                this._elShow('openAccountBtn');
                component.setSrc('resources/images/dsyfk.png');
                return
            }if(data.commodityState == "16.5"){
                this._elShow('sureHeTon');
                component.setSrc('resources/images/dsyfk.png'); //确认合同
                return
            }else if(data.commodityState == '16.7'){
                if(data.code=='1'){
                    this._elShow('serverPackInfo');
                    component.setSrc('resources/images/dsyfk.png');
                }else{
                    this._elShow('collectMoney');
                    document.getElementById('advanCharge').innerHTML = '￥'+data.downPayMoney;
                    document.getElementById('getChargeBtn').setAttribute('data-name',data.customerName);
                    document.getElementById('getChargeBtn').setAttribute('data-money',data.downPayMoney);
                    component.setSrc('resources/images/dsyfk.png');
                    return
                }
            }else if(data.commodityState == '17'){
                this._elShow('collectMoney');
                document.getElementById('advanCharge').innerHTML = '￥'+data.downPayMoney;
                document.getElementById('getChargeBtn').setAttribute('data-name',data.customerName);
                document.getElementById('getChargeBtn').setAttribute('data-money',data.downPayMoney);
                component.setSrc('resources/images/dsyfk.png');
                return
            }else if(data.commodityState == '18'){
                this._elShow('alreadyMoney');
                this._elShow('toUploadBtn');
                document.getElementById('alreadyCharge').innerHTML = '￥'+data.downPayMoney;
                component.setSrc('resources/images/dth.png');
                return
            }else if(data.commodityState == '19'){
                this._elShow('alreadyMoney');
                this._elShow('deliverImg1');
                this._elShow('toSubmerCode');
                document.getElementById('alreadyCharge').innerHTML = '￥'+data.downPayMoney;
                component.setSrc('resources/images/yth.png');
                return
            }else if(data.commodityState == '20'){
                this._elShow('alreadyMoney');
                this._elShow('deliverImg1');
                this._elShow('goodsCodeNum');
                document.getElementById('alreadyCharge').innerHTML = '￥'+data.downPayMoney;
                document.getElementById('detailNum').innerHTML = data.merchandiseCode;
                component.setSrc('resources/images/yth.png');
                return
            }
        }
        if(data.state == "1" || data.state == "2" || data.state == "4"){
            component.setSrc('resources/images/shz.png');
            return
        }
        if(data.state == "3"||data.state == "6"||data.state == "3.5"){
            component.setSrc('resources/images/shwtg.png');
            return
        }
        if (data.state == "10")
        {
            component.setSrc('resources/images/guanbi.png'); //订单关闭
            return;
        }
    },
    //查看客户信息
    toViewCustInfo:function () {
        if(Ext.getCmp('custHomeView')){
            Ext.getCmp('custHomeView').show();
        }else{
            Ext.Viewport.add(Ext.create('MyApp.view.perinfo.CustomerHome')).show();
        }
    },
    //完善客户信息
    completeInfo:function () {
        whir.loading.add("", 1);
        var orderId = Ext.getCmp('order_id').getValue();
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

/*

    //等待客户确认合同
    sureHeTon:function () {
        whir.loading.add("", 1);
        var orderId = Ext.getCmp('order_id').getValue();
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
*/



    //查看影像资料
    toViewImgData:function () {
        if(Ext.getCmp('seeImgView')){
            Ext.getCmp('seeImgView').show()
        }else{
            Ext.Viewport.add(Ext.create('MyApp.view.applyManager.ViewImgData')).show();
        }
    },
    //补充影像资料
    toImgData:function () {
        if(Ext.getCmp('uploadView')){
            Ext.getCmp('uploadView').show();
        }else{
            Ext.Viewport.add(Ext.create('MyApp.view.applyManager.ImageData')).show();
        }
    },
    //手签
    toSign:function () {
        var orderId = Ext.getCmp('order_id').getValue();
        var option = [orderId];
        var ua = navigator.userAgent.toLowerCase();
        if (/iphone|ipad|ipod/.test(ua)) {
            device.getWritingSignature(orderDetailThis.signSuccess,orderDetailThis.signError,option);
        }else if(/android/.test(ua)){
            device.signature(orderDetailThis.signSuccess,orderDetailThis.signError,option);
        }
    },
    //查看手签
    seeHandSign:function () {
        if(Ext.getCmp('handSianView')){
            Ext.getCmp('handSianView').show();
        }else {
            Ext.Viewport.add(Ext.create('MyApp.view.applyManager.HandSign')).show();
        }
    },
    //提交
    _submitOrder:function () {
        var orderId = Ext.getCmp('order_id').getValue(),
            state = '15',
            isSign = Ext.getCmp('signFlag').getValue();
        whir.loading.add("", 1);
        Comm.ajaxPost(
            '/employeeOrder/submitUpdateState',
            {orderId:orderId,state:state,isSign:isSign},
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                if(msg.retCode == "SUCCESS"){
                    Ext.getCmp('submitCon').hide();
                    Ext.getCmp('OrderDetailsView')._getOrderDetail();
                }
            }
        )
    },
    //撤回
    toOrderBack:function () {
        whir.loading.add("", 1);
        var orderId = Ext.getCmp('order_id').getValue();
        var state = '14';
        Comm.ajaxPost(
            '/employeeOrder/updateOrderStateBeforeSubmit',
            {orderId:orderId,state:state},
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                if(msg.retCode == "SUCCESS"){
                    Ext.getCmp('refuseCon').hide();
                    Ext.getCmp('aleadyCon').hide();
                    Ext.getCmp('OrderDetailsView')._getOrderDetail();
                }
            }
        )
    },
    //开户
    toOpenAccount:function () {
        if(Ext.getCmp('openAccountView')){
            Ext.getCmp('openAccountView').show();
        }else{
            Ext.Viewport.add(Ext.create('MyApp.view.orderManager.OpenAccount')).show();
        }
    },
    //收取预付款
    collectCharge:function (me) {
        var money = me.getAttribute('data-money');
        var custName = me.getAttribute('data-name');
        var orderId = Ext.getCmp('order_id').getValue();
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
                            document.getElementById('collectMoney').style.display = 'none';
                            orderDetailThis._getOrderDetail();
                        }
                    }
                )
            }
        },'div');
    },
    //上传发货照片
    toDeliver:function () {
        if(Ext.getCmp('uploadGoodsView')){
            Ext.getCmp('uploadGoodsView').show();
        }else{
            Ext.Viewport.add(Ext.create('MyApp.view.orderManager.UploadPhoto')).show();
        }
    },
    //补充发货照片
    supplyImg:function () {
        if(Ext.getCmp('supplyImgView')){
            Ext.getCmp('supplyImgView').show();
        }else{
            Ext.Viewport.add(Ext.create('MyApp.view.orderManager.SupplyPhoto')).show();
        }
    },
    //提交串号码
    toSubCodeBtn:function () {
        if(Ext.getCmp('numGoodsView')){
            Ext.getCmp('numGoodsView').show();
        }else{
            Ext.Viewport.add(Ext.create('MyApp.view.orderManager.NumberGoods')).show();
        }
    },
    signSuccess:function (msg) {
        Ext.getCmp('OrderDetailsView')._getOrderDetail();
        document.getElementById('signBtn').style.display = 'none';
    },
    signError:function (msg) {
        //alert('error');
    }
});