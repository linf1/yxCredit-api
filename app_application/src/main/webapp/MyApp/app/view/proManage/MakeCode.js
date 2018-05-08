/**
 * 生成二维码
 * Created by zl on 2017/11/27 0027.
 */
Ext.define('MyApp.view.proManage.MakeCode',{
    extend:'Ext.Panel',
    xtype:'makeCodeView',
    requires:[
        'Ext.Panel',
        'MyApp.view.proManage.Code',
        'MyApp.model.proManage.MakeCode'
    ],
    config:{
        id:'makeCodeView',
        showAnimation:{type:'slide', direction:'left'},
        layout:{type:'vbox'},
        zIndex:5,
        items:[
            {
                xtype:'toolbar',
                title:'生成二维码',
                docked:'top',
                style:'background-color:#FEE300;border-radius:0;',
                items:[
                    {
                        html: '<span class="icon-back" style="font-size:26px;color:#000;"></span>',
                        style:'margin-left:10px;color:#8c8c8c;margin-top:8px;',
                        listeners:{
                            tap:function () {
                                hidePage('#makeCodeView');
                            }
                        }
                    }
                ]
            },
            {
                xtype:'formpanel',
                id:'makeCodeForm',
                flex:9,
                scrollable:{direction: 'vertical',indicators: false},
                items:[
                    {
                        xtype: 'textfield',
                        name: 'totalPrice',
                        id:'totalPrice',
                        clearIcon:false,
                        label:'<span style="font-size: 14px;">总价格</span>',
                        placeHolder:'输入金额',
                        maxLength:10,
                        html:'<span style="position:absolute;top:12px;right:15px;font-size:14px;">元</span>',
                        style:'border-bottom:1px solid #f6f6f6;margin-top:10px;'
                    },
                    {
                        xtype: 'textfield',
                        name: 'downAmount',
                        id:'downAmount',
                        clearIcon:false,
                        label:'<span style="font-size: 14px;">首付金额</span>',
                        placeHolder:'输入首付金额',
                        maxLength:10,
                        html:'<span style="position:absolute;top:12px;right:15px;font-size:14px;">元</span>',
                        style:'border-bottom:1px solid #f6f6f6;margin-top:5px;'
                    },
                    {
                        xtype: 'textfield',
                        name: 'offlineOrder',
                        id:'offlineOrder',
                        clearIcon:false,
                        label:'<span style="font-size: 14px;">订单类型</span>',
                        placeHolder:'请选择',
                        html:'<span class="icon-more" style="position:absolute;top:14px;right:15px;font-size: 16px;"></span>',
                        readOnly:true,
                        style:'border-bottom:1px solid #f6f6f6;margin-top:5px;'
                    },
                    {
                        xtype:'hiddenfield',
                        id:'orderType',
                        name:'orderType'
                    },
                    {
                        xtype: 'textfield',
                        name: 'interestRate',
                        id:'interestRate',
                        clearIcon:false,
                        label:'<span style="font-size: 14px;">利率方案</span>',
                        placeHolder:'请选择',
                        html:'<span class="icon-more" style="position:absolute;top:14px;right:15px;font-size: 16px;"></span>',
                        readOnly:true,
                        style:'border-bottom:1px solid #f6f6f6;margin-top:5px;'
                    },

                    {
                        xtype:'hiddenfield',
                        id:'rateValue',
                        name:'rateValue'
                    },
                    {
                        xtype:'button',
                        id:'servicePackage',
                        style:'border:none;border-radius:0;height:44px;',
                        html:'<span style="color:#000;font-size:14px;float:left;padding-left:8px;">服务包</span><span id="packSelect" style="position:absolute;top:12px;right:30px;font-size: 14px;color:#757575;">请选择</span><span class="icon-more" style="position:absolute;top:14px;right:15px;font-size: 16px;color:#000;"></span>',
                        listeners:{
                            tap:function () {
                                makeCodeThis.loadServePack();
                                //makeCodeThis.overlayShow();
                            }
                        }
                    },

                    {
                        xtype:'button',
                        html:'<span style="color:#1a1a1d;">生成二维码</span>',
                        style:'width:258px;margin:24px auto;border:none;border-radius:50px;background-color:#FEE300;height:40px;box-shadow: 0px 3px 3px rgba(248,214,201,1);',
                        listeners:{
                            tap:function () {
                                makeCodeThis.makeCode();
                            }
                        }
                    }
                ]
            }
        ],
        listeners:{
            show:function () {
                Ext.getCmp('makeCodeView').loadCode();
                //Ext.getCmp('makeCodeView').loadServePack();
            }
        }
    },
    initialize:function() {
        makeCodeThis = this;
        this.prelead_id = '';
        this.month_id = '';
        this.other_id = '';
        this.data = {};
    },
    loadCode:function () {
        whir.loading.add("", 1);
        var control = this;
        Comm.ajaxPost(
            '/rate/getRateScheme',
            '',
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                if(msg.retCode == "SUCCESS"){
                    var data = msg.retData;
                    var list = [];
                    data.forEach(function (item) {
                        list.push({text:item.productName,value:item.productId});
                    })
                    control._showRatePicker(list);

                }
            }
        )
        //线上线下订单
        Comm.ajaxPost(
            '/rate/getOfflineOrder',
            '',
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                if(msg.retCode == "SUCCESS"){
                    var data = msg.retData;
                    var list = [];
                    data.forEach(function (item) {
                        list.push({text:item.value,value:item.key});
                    })
                    control._showOrderType(list);

                }
            }
        )
    },
    loadServePack:function () {
        var control = this;
        var interestRate = Ext.getCmp('interestRate').getValue();
        if(interestRate == ""){
            layer.open({content:'请先选择利率方案',skin:'msg',time:2});
            return
        }
        var productId = Ext.getCmp('rateValue').getValue();
        whir.loading.add("", 1);
        Comm.ajaxPost(
            '/serPackage/getSerPackage',
            {productId:productId},
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                if(msg.retCode == "SUCCESS"){
                    var data = msg.retData;
                    control.serveList = data;
                    control.overlayShow();
                }
            }
        )
    },
    makeCode:function () {
        var me = this;
        var  orderTypeValue= Ext.getCmp('orderType').getValue();
        var productId = Ext.getCmp('codeProductId').getValue(),
            merchantId = Ext.getCmp('merchanId').getValue();
        var formData = Ext.getCmp('makeCodeForm').getValues();
      /* if(makeCodeThis.prelead_id == ""&&makeCodeThis.month_id==""){
            layer.open({content:'请选择服务包',skin:'msg',time:2});
            return
        }*/
        var idJson = makeCodeThis.prelead_id+','+makeCodeThis.month_id+','+makeCodeThis.other_id;
        if(Ext.query("#packSelect")[0].innerHTML=="请选择"||Ext.query("#packSelect")[0].innerHTML=="共选择了0款服务包"){idJson="";}
        idJson = idJson.split(',');
        idJson = idJson.filter(makeCodeThis.checkAdult);
        idJson = idJson.join(',');
        var empId = Ext.getCmp('salesmanId').getValue(),
            merchandiseId = productId,
            merchantId = merchantId,
            orderTypeValue=orderTypeValue,
            productId = formData.rateValue,
            allMoney = formData.totalPrice,
            downPayMoney = formData.downAmount;
        var data = {
            empId:empId,
            merchandiseId:merchandiseId,
            merchantId:merchantId,
            orderTypeValue:orderTypeValue,
            productId:productId,
            allMoney:allMoney,
            downPayMoney:downPayMoney
        }
        data = JSON.stringify(data);
        var model = Ext.create('MyApp.model.proManage.MakeCode',formData);
        var errors = model.validate();
        if(errors.isValid()){
            whir.loading.add("", 1);
            Comm.ajaxPost(
                '/qrCode/createQRCode',
                {data:data,idJson:idJson},
                function (response) {
                    whir.loading.remove();
                    var msg = eval('('+response.responseText+')');
                    console.log(msg);
                    if(msg.retCode == "SUCCESS"){
                        me.data = msg.retData;
                        if(Ext.getCmp('codeView')){
                            Ext.getCmp('codeView').show();
                        }else{
                            Ext.Viewport.add(Ext.create('MyApp.view.proManage.Code')).show();
                        }
                    }else{
                        mui.alert(msg.retMsg,'提示','知道了',null,'div');
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
    },
    _showRatePicker:function (list) {
        var control = this;
        var ratePickerEl = document.getElementById('interestRate');
        var ratePicker = new Picker({
            data: [list]
        });
        var interestRate = Ext.getCmp('interestRate'),
            rateValue = Ext.getCmp('rateValue');
        ratePicker.on('picker.select', function (selectedVal, selectedIndex) {
            //picker1El.innerText = data1[selectedIndex[0]].text + data1[selectedIndex[0]].value;
            interestRate.setValue(list[selectedIndex[0]].text);
            rateValue.setValue(list[selectedIndex[0]].value);
        });
        ratePickerEl.addEventListener('click', function () {
            ratePicker.show();
        });
    },
    //线上线下订单展示
    _showOrderType:function (list) {
        var control = this;
        var orderType1 = document.getElementById('offlineOrder');
        var orderTypeShow = new Picker({
            data: [list]
        });
        var offlineOrder = Ext.getCmp('offlineOrder'),
            orderTypeValue = Ext.getCmp('orderType');
        orderTypeShow.on('picker.select', function (selectedVal, selectedIndex) {
            //picker1El.innerText = data1[selectedIndex[0]].text + data1[selectedIndex[0]].value;
            offlineOrder.setValue(list[selectedIndex[0]].text);
            orderTypeValue.setValue(list[selectedIndex[0]].value);
        });
        orderType1.addEventListener('click', function () {
            orderTypeShow.show();
        });
    },


    overlayShow:function () {
        var e = this;
        var box = document.getElementById('servicePackage');
        var offsetY = e._setOverlayPos(box);
        e.overlay = Ext.Viewport.add({
            xtype: 'panel',
            docked:'bottom',
            modal: true,
            zIndex:999,
            hideOnMaskTap: false,
            cls:'overlay',
            hidden: true,
            width: "100%",
            height: offsetY,
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
                    docked:'top',
                    items:[
                        {
                            html:'<div style="width:100%;background:#fff;height:40px;border:1px solid #f6f6f6;border-left:0;border-right:0;"><span onclick="makeCodeThis._overlayHide()" style="display:inline-block;width:40px;height:40px;line-height:40px;text-align:center;line-height:40px;" class="icon-back"></span></div>'
                        }
                    ]
                },
                {
                    xtype:'panel',
                    items:[
                        {
                            html:'<div class="service-content">' +
                            '<div class="prelead-pack pack">' +
                                '<div class="title">' +
                                    '<span class="pack-desc">前置提前还款包</span>' +
                                    '<span class="pack-money" id="preleMoney"></span>'+
                                '</div>' +
                                '<div id="preList" class="service-pack">' +
                                '</div>'+
                            '</div>' +
                            '<div class="month-pack pack">' +
                                '<div class="title">' +
                                    '<span class="pack-desc">月付提前还款包</span>' +
                                    '<span class="pack-money" id="monthMoney"></span>'+
                                '</div>' +
                                '<div id="monthList" class="service-pack">' +

                                '</div>'+
                            '</div>'+
                            '<div class="other-pack pack">' +
                                '<div class="title">' +
                                    '<span class="pack-desc">其他还款包</span>' +
                                    '<span class="pack-money" id="otherMoney"></span>'+
                                '</div>' +
                                '<div id="otherList" class="service-pack">' +

                                '</div>'+
                            '</div>'+
                            '</div>'
                        }
                    ]
                },
                {
                    xtype:'button',
                    docked:'bottom',
                    height:40,
                    html:'<span style="color:#000;">确定</span>',
                    style:'background-color:#FEE300;border-radius:0;border:none;',
                    listeners:{
                        tap:function () {
                            var otherListDom = document.getElementById('otherList');
                            var child = otherListDom.children;
                          /*  if(makeCodeThis.prelead_id=="" && makeCodeThis.month_id==""){
                                layer.open({content:'请至少选择前置或月付服务包一种',skin:'msg',time:2});
                                return
                            }*/
                            for(var i=0;i<child.length;i++){
                                var children = child[i];
                                if(children.className.indexOf('active')>0){
                                    makeCodeThis.other_id += children.getAttribute('data-id')+',';
                                }
                            }
                            var idJson = makeCodeThis.prelead_id+','+makeCodeThis.month_id+','+makeCodeThis.other_id;
                            idJson = idJson.substring(0,idJson.length-1);
                            var idArr = idJson.split(',');
                            idArr = idArr.filter(makeCodeThis.checkAdult);
                            var text = '共选择了'+idArr.length+'款服务包';
                            document.getElementById('packSelect').innerHTML = text;
                            makeCodeThis.overlay.hide({type:'slideOut',direction:'down'});
                        }
                    }
                }
            ],
            listeners:{
                show:function () {
                    makeCodeThis.other_id = '';
                    makeCodeThis.prelead_id = "";
                    makeCodeThis.month_id = "";
                    var serveList = makeCodeThis.serveList;
                    var packData = makeCodeThis._getServeType(serveList);
                    var monthList = packData.monthList,otherList = packData.otherList,preleadList = packData.preleadList;
                    makeCodeThis._showServePack(preleadList,monthList,otherList);
                }
            }
        });
        e.overlay.show({type:'slideIn',direction:'up'});
    },
    //获取服务包类型
    _getServeType:function (serveList) {
        //前置提前还款包
        var preleadList = [],
            monthList = [],//月付提前
            otherList = [];//其他费包
        serveList.forEach(function (item) {
            if(item.packageType == 1){
                preleadList.push(item);
            }else if(item.packageType == 2){
                monthList.push(item)
            }else if(item.packageType == 3){
                otherList.push(item);
            }
        });
        var data = new Object();
        data.preleadList = preleadList;
        data.monthList = monthList;
        data.otherList = otherList;
        return data;
    },
    //展示服务包
    _showServePack:function (preleadList,monthList,otherList) {
        var preListDom = document.getElementById('preList'),
            monthListDom = document.getElementById('monthList'),
            otherListDom = document.getElementById('otherList');
        preListDom.innerHTML = "";
        monthListDom.innerHTML = "";
        otherListDom.innerHTML = "";
        preleadList.forEach(function (item) {
            preListDom.innerHTML += '<span onclick="makeCodeThis.addActivePack(this,1)" data-money="'+item.collectionAmount+'" data-after="'+item.afterMonth+'" data-id="'+item.id+'" class="pack-btn">'+item.packageName+'<span class="pack-tips">'+item.qsRemark+'</span><span class="pack-tips1">'+item.sqTypeRemark+'</span></span>'
        });
        monthList.forEach(function (item) {
            monthListDom.innerHTML += '<span onclick="makeCodeThis.addActivePack(this,2)" data-money="'+item.collectionAmount+'" data-id="'+item.id+'" class="pack-btn">'+item.packageName+'<span class="pack-tips">'+item.qsRemark+'</span><span class="pack-tips1">'+item.sqTypeRemark+'</span></span>'
        });
        otherList.forEach(function (item) {
            if(item.forceCollection == 1){
                otherListDom.innerHTML +='<span data-money="'+item.collectionAmount+'" data-id="'+item.id+'" class="pack-btn active">'+item.packageName+'<span class="pack-tips" style="display:block;">'+item.qsRemark+'</span><span class="pack-tips1" style="display:block;">'+item.sqTypeRemark+'</span></span>';
                document.getElementById('otherMoney').innerHTML = '￥'+item.collectionAmount+'/月';
            }else{
                otherListDom.innerHTML +='<span onclick="makeCodeThis.addActiveOtherPack(this)" data-money="'+item.collectionAmount+'" data-id="'+item.id+'" class="pack-btn">'+item.packageName+'<span class="pack-tips">'+item.qsRemark+'</span><span class="pack-tips1">'+item.sqTypeRemark+'</span></span>'
            }
        })
    },
    //设置服务包弹出层的位置
    _setOverlayPos:function (el) {
        var height = window.screen.height;
        var pos = el.getBoundingClientRect();
        var bottom = pos.bottom;
        var offsetY = height - bottom - 5;
        return offsetY;
    },
    //给服务包添加激活样式(其他服务包除外)
    addActivePack:function (me,index) {
        var control = this;
        if(me.className.indexOf('active')>0){
            me.setAttribute('class','pack-btn');
            var child = me.children;
            me.getElementsByClassName('pack-tips')[0].style.display="none";
            me.getElementsByClassName('pack-tips1')[0].style.display="none";
            switch (index)
            {
                case 1:
                    document.getElementById('preleMoney').innerHTML = "";
                    control.prelead_id="";
                    break;
                case 2:
                    document.getElementById('monthMoney').innerHTML = "";
                    control.month_id="";
                    break
            }
        }else{
            me.setAttribute('class','pack-btn active');
            me.getElementsByClassName('pack-tips')[0].style.display="block";
            me.getElementsByClassName('pack-tips1')[0].style.display="block";
            var money = me.getAttribute('data-money');
            switch (index)
            {
                case 1:
                    document.getElementById('preleMoney').innerHTML='￥'+money;
                    var preleadId = me.getAttribute('data-id');
                    control.prelead_id = preleadId;
                    var rateValue = Ext.getCmp('rateValue').getValue();
                    var afterMonth = me.getAttribute('data-after');
                    control._getMonthPackList(rateValue,afterMonth);
                    break
                case 2:
                    document.getElementById('monthMoney').innerHTML = '￥'+money+'/月';
                    var monthId = me.getAttribute('data-id');
                    control.month_id = monthId;
                    break
            }
        }
        var children = me.parentNode.children;
        for(var i=0,len=children.length;i<len;i++){
            var child = children[i];
            if(child!=me){
                child.setAttribute('class','pack-btn');
                child.getElementsByClassName('pack-tips')[0].style.display="none";
                child.getElementsByClassName('pack-tips1')[0].style.display="none";
            }
        }
    },
    //给其他服务包添加激活样式
    addActiveOtherPack:function (me) {
        var control = this;
        if(me.className.indexOf('active')>0){
            me.setAttribute('class','pack-btn');
            me.getElementsByClassName('pack-tips')[0].style.display="none";
            me.getElementsByClassName('pack-tips1')[0].style.display="none";
            var money = control._totalMoney();
            document.getElementById('otherMoney').innerHTML = '￥'+money+'/月'
        }else{
            me.setAttribute('class','pack-btn active');
            me.getElementsByClassName('pack-tips')[0].style.display="block";
            me.getElementsByClassName('pack-tips1')[0].style.display="block";
            var money = control._totalMoney();
            document.getElementById('otherMoney').innerHTML = '￥'+money+'/月';
        }
    },
    //获得月付List
    _getMonthPackList:function (rateValue,afterMonth) {
        var me =this;
        whir.loading.add("", 1);
        Comm.ajaxPost(
            '/serPackage/getMonthPackage',
            {afterMonth:afterMonth,productId:rateValue},
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                if(msg.retCode == "SUCCESS"){
                    var data = msg.retData;
                    me.month_id = "";
                    document.getElementById('monthMoney').innerHTML = "";
                    me._tabMonthList(data);
                }
            }
        )
    },
    //切换月付模式
    _tabMonthList:function (monthList) {
        var monthListDom = document.getElementById('monthList');
        monthListDom.innerHTML = "";
        monthList.forEach(function (item) {
            monthListDom.innerHTML += '<span onclick="makeCodeThis.addActivePack(this,2)" data-money="'+item.collectionAmount+'" data-id="'+item.id+'" class="pack-btn">'+item.packageName+'<span class="pack-tips">'+item.qsRemark+'</span><span class="pack-tips1">'+item.sqTypeRemark+'</span></span>'
        });
    },
    //计算钱数
    _totalMoney:function () {
        var otherListDom = document.getElementById('otherList');
        var children = otherListDom.children;
        var totalMoney = 0;
        for(var i=0,len=children.length;i<len;i++){
            var child = children[i];
            if(child.className.indexOf('active')>0){
                totalMoney += parseFloat(child.getAttribute('data-money'));
            }
        }
        return totalMoney;
    },
    //验证服务包
    validatePack:function (idJson) {
        var newArr = idJson.split(',');
        return newArr.length;
    },
    //数组去空
    checkAdult:function (id) {
        return id != "";
    },
    _overlayHide:function () {
        document.getElementById('packSelect').innerHTML = '请选择';
        this.overlay.hide({type:'slideOut',direction:'down'});
    }
});
