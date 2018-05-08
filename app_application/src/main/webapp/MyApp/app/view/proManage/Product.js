/**
 * 商品管理界面
 * Created by zl on 2017/11/21 0021.
 */
Ext.define('MyApp.view.proManage.Product',{
    extend:'Ext.Panel',
    xtype:'productView',
    requires:[
        'Ext.Panel',
        'MyApp.view.proManage.Search',
        'MyApp.view.proManage.MakeCode'
    ],
    config:{
        id:'productView',
        showAnimation:{type:'slide', direction:'left'},
        scrollable:{direction: 'vertical',indicators: false},
        layout:{type:'vbox'},
        items:[
            {
                xtype:'container',
                docked:'top',
                style:'background:url(resources/images/pro_bg.png) no-repeat center center;width:100%;height:145px;background-size:cover;padding-top:25px;',
                items:[
                    {
                        html:'<div class="pro-header">'+
                            '<div class="lf" onclick="proThis.getMerchanList()"><span class="icon-store"></span></div>'+
                            '<div style="background:#fff;" class="md" onclick="proThis.showSearch()"><span class="icon-search"></span><span>商品名称</span></div>'+
                            '<div class="rt" id="exitApp" onclick="proThis.exitApp()"><span class="icon-exit"></span></div>'+
                            '<div id="backPro" onclick="proThis._backPro()" style="display:none;width:80px;height:30px;line-height:30px;text-align:center;font-size:14px;color:#fff;">取消</div>'+
                            '</div>'+
                            '<div class="pro-desc">'+
                            '<div class="nameImg"><img width="40" src="resources/images/store-logo.png"></div>'+
                            '<div class="name-desc" id="proName"></div>'+
                            '<div class="pro-num"><p id="proNum"></p><p>产品数</p></div>'+
                            '<div class="pro-add" onclick="proThis._toAddGoods()"><p><span class="icon-imageAdd"></span><span>添加</span></p></div>'+
                            '</div>'
                    }
                ]
            },
            {
                xtype:'container',
                items:[
                    {
                        html:'<!--数据列表-->'+
                        '<div id="productWrapper" class="data-wrapper">'+
                            /*'<div class="data-item">'+
                                '<div class="img"><img data-src="https://img.alicdn.com/tps/TB1rPrIKpXXXXbzaXXXXXXXXXXX-520-280.jpg_300x300.jpg" src="resources/images/60x60.gif" alt=""></div>'+
                                '<div class="content">' +
                                    '<div class="title">IPHONE8苹果（APPLE）苹果手机</div>'+
                                    '<div class="desc"><span class="capacity">32G</span><span class="color">金色</span></div>'+
                                    '<p><span class="codeBtn">生成二维码</span></p>'+
                                '</div>'+
                            '</div>'+
                            '<div class="data-item">'+
                                '<div class="img"><img data-src="https://img.alicdn.com/tps/TB1rPrIKpXXXXbzaXXXXXXXXXXX-520-280.jpg_300x300.jpg" src="resources/images/60x60.gif" alt=""></div>'+
                                '<div class="content">' +
                                    '<div class="title">IPHONE8苹果（APPLE）苹果手机</div>'+
                                    '<div class="desc"><span class="capacity">32G</span><span class="color">金色</span></div>'+
                                    '<p><span class="codeBtn">生成二维码</span></p>'+
                                '</div>'+
                            '</div>'+
                            '<div class="data-item">'+
                                '<div class="img"><img data-src="https://img.alicdn.com/tps/TB1rPrIKpXXXXbzaXXXXXXXXXXX-520-280.jpg_300x300.jpg" src="resources/images/60x60.gif" alt=""></div>'+
                                '<div class="content">' +
                                    '<div class="title">IPHONE8苹果（APPLE）苹果手机</div>'+
                                    '<div class="desc"><span class="capacity">32G</span><span class="color">金色</span></div>'+
                                    '<p><span class="codeBtn">生成二维码</span></p>'+
                                '</div>'+
                            '</div>'+*/
                        '</div>'
                    }
                ]
            }
        ],
        listeners:{
            show:function () {
                //Ext.getCmp('productView')._getMerchandiseList()
            }
        }
    },
    initialize:function() {
        proThis = this;
        count = 0;
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
                            html:'<span style="position:absolute;top:0;right:-15px;width:30px;height:30px;line-height:30px;" onclick="proThis._overlayHide()" class="serve-close">&times;</span>'
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
    toMakeCode:function (me) {
        Ext.getCmp('codeProductId').setValue(me.getAttribute('data-productId'));
        if(Ext.getCmp('makeCodeView')){
            Ext.getCmp('makeCodeView').show();
        }else{
            Ext.Viewport.add(Ext.create('MyApp.view.proManage.MakeCode')).show();
        }
    },
    _lazyload:function(){
        Echo.init({
            offset: 0,//离可视区域多少像素的图片可以被加载
            throttle: 0 //图片延时多少毫秒加载
        });
    },
    showSearch:function () {
        if(Ext.getCmp('searchView')){
            Ext.getCmp('searchView').show();
        }else{
            Ext.Viewport.add(Ext.create('MyApp.view.proManage.Search')).show();
        }
    },
    _getMerchandiseList:function () {
        var me = this;
        whir.loading.add("", 1)
        var merchanId = Ext.getCmp('merchanId').getValue(),
            salesmanId = Ext.getCmp('salesmanId').getValue();
        Comm.ajaxPost(
            '/merchandise/getMerchandiseList',
            {merchantId:merchanId},
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                console.log(msg);
                if(msg.retCode == "SUCCESS"){
                    var data = msg.retData;
                    var merchantName = data.merchantName,//商户名称
                        merchandiseNum = data.merchandiseNum;//商品数量
                    var list = data.list;//商品列表
                    if(merchantName == "" || merchantName == undefined){
                        merchantName = '该商家暂无商品'
                    }
                    me._showHeaderInfo(merchantName,merchandiseNum);
                    me._showProList(list);
                }
            }
        )
    },
    //头部信息展示
    _showHeaderInfo:function (businessName,merchandiseNum) {
        var proNameDom = document.getElementById('proName'),//商户名
            proNumDom = document.getElementById('proNum');//产品个数
        proNameDom.innerHTML = businessName;
        proNumDom.innerHTML = merchandiseNum + '件';
    },
    //展示商品
    _showProList:function (list) {
        var data = new Object();
        data.merchanList = list;
        var html = template('merchanList',data);
        document.getElementsByClassName('data-wrapper')[0].innerHTML = html;
        this._lazyload();
    },
    //回到展示所有商品页面
    _backPro:function () {
        document.getElementById('exitApp').style.display = 'block';
        document.getElementById('backPro').style.display = 'none';
        this._getMerchandiseList();
    },
    //添加商品页
    _toAddGoods:function () {
        if(Ext.getCmp('addgoodsView')){
            Ext.getCmp('addgoodsView').show();
        }else{
            Ext.Viewport.add(Ext.create('MyApp.view.proManage.Addgoods')).show();
        }
    },
    exitApp:function () {
        if(Ext.getCmp('home')){
            Ext.getCmp('home').destroy();
        }
        Ext.Viewport.setActiveItem('loginView')
        /*hidePage('#home')*/
    }
});