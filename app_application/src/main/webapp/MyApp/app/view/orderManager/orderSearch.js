/**
 * 订单搜索
 * Created by zl on 2018/1/3 0003.
 */
Ext.define('MyApp.view.orderManager.orderSearch',{
    extend:'Ext.Panel',
    xtype:'orderSearch',
    requires:[
        'Ext.Panel',
        'Ext.field.Search'
    ],
    config:{
        id:'orderSearch',
        showAnimation:{type:'slide', direction:'up'},
        layout:{type:'vbox'},
        zIndex:5,
        items:[
            {
                xtype:'container',
                style:'width:100%;height:120px;position:relative;border-bottom:1px solid #e1e2e7;',
                items:[
                    {
                        xtype:'button',
                        text:'<span class="icon-back" style="color:#000;"></span>',
                        style:'width:15%;height:38px;border:none;background-color:transparent;position:absolute;top:24px;left:0;',
                        listeners:{
                            tap:function () {
                                if(Ext.getCmp('OrderManaView')){
                                    Ext.getCmp('OrderManaView')._getAllOrder();
                                }
                                Ext.getCmp('orderSearch').hideSearchView();
                            }
                        }
                    },
                    {
                        xtype: 'searchfield',
                        id:'searchOrText',
                        name:'searchOrText',
                        placeHolder:'搜索申请中订单',
                        style:'width:60%;margin-top:25px;border-radius:30px;margin-left:63px;',
                        listeners:{
                            change:function (me, newValue, oldValue, eOpts) {
                                var value = newValue.trim();
                                if(value == ""){
                                    return
                                }
                                Ext.getCmp('orderSearch').searchRes(value);
                            }
                        }
                    },
                    {
                        xtype:'button',
                        text:'<span style="color:#333;font-size:15px;">搜索</span>',
                        style:'width:20%;height:38px;border:none;background-color:transparent;position:absolute;top:24px;right:0;'
                    },
                    {
                        html:'<div id="currentSearch1" style="margin-top:32px;font-size:13px;color:#666;margin-left:15px;">最近搜索</div>'
                    }
                ]
            },
            {
                xtype:'container',
                id:'orSearch',
                style:'background-color:#fff;',
                items:[
                    /*{
                     xtype:'button',
                     html:'<p class="sear-container"><span class="icon-history"></span><span class="searh-name">iphone8</span></p>',
                     style:'text-align:left;border:none;width:90%;margin:0 auto;border-bottom:1px solid #e1e2e7;border-radius:0;'
                     },
                     {
                     xtype:'button',
                     html:'<p class="sear-container"><span class="icon-history"></span><span class="searh-name">金色</span></p>',
                     style:'text-align:left;border:none;width:90%;margin:0 auto;border-bottom:1px solid #e1e2e7;border-radius:0;'
                     },
                     {
                     xtype:'button',
                     html:'<p class="sear-container"><span class="icon-history"></span><span class="searh-name">32G</span></p>',
                     style:'text-align:left;border:none;width:90%;margin:0 auto;border-bottom:1px solid #e1e2e7;border-radius:0;'
                     },
                     {
                     xtype:'button',
                     html:'<p class="sear-container"><span class="icon-history"></span><span class="searh-name">粉色</span></p>',
                     style:'text-align:left;border:none;width:90%;margin:0 auto;border-bottom:1px solid #e1e2e7;border-radius:0;'
                     },
                     {
                     xtype:'button',
                     html:'<p class="sear-container"><span class="icon-history"></span><span class="searh-name">黑色</span></p>',
                     style:'text-align:left;border:none;width:90%;margin:0 auto;border-bottom:1px solid #e1e2e7;border-radius:0;'
                     }*/
                ]
            },
            {
                xtype:'button',
                id:'orClearSearch',
                html:'<span style="color:#999;font-size:12px;">清除搜索</span>',
                style:'text-align:left;border:none;width:100%;margin:0 auto;border-bottom:1px solid #e1e2e7;border-radius:0;',
                listeners:{
                    tap:function () {
                        Ext.getCmp('orderSearch')._clearSearch()
                    }
                }
            }
        ],
        listeners:{
            show:function () {
                Ext.getCmp('orderSearch')._showHistory();
            }
        }
    },
    initialize:function() {
        Ext.getCmp('orClearSearch').hide();
    },
    searchRes:function (value) {
        var control = this;
        whir.loading.add("", 1)
        var employeeId = Ext.getCmp('salesmanId').getValue();
        var merchantId = Ext.getCmp('merchanId').getValue();
        Comm.ajaxPost(
            '/employeeOrder/searchOrder',
            {employeeId:employeeId,merchantId:merchantId,searchContent:value,state:'5',type:'2'},
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                console.log(msg);
                if(msg.retCode == "SUCCESS"){
                    var data = msg.retData;
                    if(data){
                        var allPassOrder = data.allPassOrder;
                        var orderData = {};
                        orderData.orderMaList = allPassOrder;
                        Ext.getCmp('orderSearch').hideSearchView();
                        Ext.getCmp('OrderManaView')._orderTemplate(orderData);
                    }else{
                        layer.open({content:msg.retMsg,skin:'msg',time:2});
                    }
                }else{
                    layer.open({content:msg.retMsg,skin:'msg',time:2});
                }
            }
        )
    },
    //展示历史
    _showHistory:function () {
        whir.loading.add("", 1);
        var control = this;
        var merchantId = Ext.getCmp('merchanId').getValue(),
            employeeId = Ext.getCmp('salesmanId').getValue();
        Comm.ajaxPost(
            '/employeeOrder/showOrderSearchHistory',
            {merchantId:merchantId,employeeId:employeeId,type:'2'},
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                console.log(msg);
                if(msg.retCode == "SUCCESS"){
                    var data = msg.retData;
                    var searchHistoryList = data.searchHistoryList;
                    if(searchHistoryList && searchHistoryList.length != 0){
                        var orSearch = Ext.getCmp('orSearch');
                        orSearch.removeAll();
                        var items = [];
                        searchHistoryList.forEach(function (item) {
                            items.push({
                                xtype:'button',
                                html:'<p class="sear-container"><span class="icon-history" style="vertical-align:middle;"></span><span style="color:#999;vertical-align:middle;padding-left:6px;font-size:14px;" class="searh-name">'+item.searchContent+'</span></p>',
                                style:'text-align:left;border:none;width:90%;margin:0 auto;border-bottom:1px solid #e1e2e7;border-radius:0;',
                                listeners:{
                                    tap:function () {
                                        var key = this.bodyElement.dom.getElementsByClassName('searh-name')[0].innerHTML;
                                        control.searchRes(key);
                                    }
                                }
                            });
                        });
                        orSearch.add(items);
                        Ext.getCmp('orClearSearch').show();
                    }else{
                        Ext.getCmp('orClearSearch').hide();
                    }
                }
            }
        );
    },
    //清除搜索记录
    _clearSearch:function () {
        whir.loading.add("", 1)
        var merchantId = Ext.getCmp('merchanId').getValue(),
            employeeId = Ext.getCmp('salesmanId').getValue();
        Comm.ajaxPost(
            '/employeeOrder/dropOrderSearchHistory',
            {merchantId:merchantId,employeeId:employeeId,type:'2'},
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                console.log(msg);
                if(msg.retCode == "SUCCESS"){
                    var orSearch = Ext.getCmp('orSearch');
                    orSearch.removeAll();
                    Ext.getCmp('orClearSearch').hide();
                    layer.open({content:msg.retMsg,skin:'msg',time:2});
                }
            }
        )
    },
    hideSearchView:function () {
        Ext.getCmp('orderSearch').hide({type:'slideOut',direction:'down'});
        Ext.getCmp('orderSearch').on('hide',function () {
            if(Ext.getCmp('orderSearch')){
                Ext.getCmp('orderSearch').destroy();
            }
        })
    }
});
