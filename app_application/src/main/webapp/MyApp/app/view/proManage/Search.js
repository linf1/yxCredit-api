/**
 * 搜索页
 * Created by zl on 2017/11/22 0022.
 */
Ext.define('MyApp.view.proManage.Search',{
    extend:'Ext.Panel',
    xtype:'searchView',
    requires:[
        'Ext.Panel',
        'Ext.field.Search'
    ],
    config:{
        id:'searchView',
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
                                Ext.getCmp('searchView').hideSearchView();
                            }
                        }
                    },
                    {
                        xtype: 'searchfield',
                        id:'search',
                        name:'search',
                        placeHolder:'商品名称',
                        style:'width:60%;margin-top:25px;border-radius:30px;margin-left:63px;',
                        listeners:{
                            change:function (me, newValue, oldValue, eOpts) {
                                var value = newValue.trim();
                                if(value == ""){
                                    return
                                }
                                Ext.getCmp('searchView').searchRes(value);
                            }
                        }
                    },
                    {
                        xtype:'button',
                        text:'<span style="color:#333;font-size:15px;">搜索</span>',
                        style:'width:20%;height:38px;border:none;background-color:transparent;position:absolute;top:24px;right:0;'
                    },
                    {
                        html:'<div id="currentSearch" style="margin-top:32px;font-size:13px;color:#666;margin-left:15px;">最近搜索</div>'
                    }
                ]
            },
            {
                xtype:'container',
                id:'currentSearch',
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
                id:'clearSearch',
                html:'<span style="color:#999;font-size:12px;">清除搜索</span>',
                style:'text-align:left;border:none;width:100%;margin:0 auto;border-bottom:1px solid #e1e2e7;border-radius:0;',
                listeners:{
                    tap:function () {
                        Ext.getCmp('searchView')._clearSearch()
                    }
                }
            }
        ],
        listeners:{
            show:function () {
                Ext.getCmp('searchView')._showHistory();
            }
        }
    },
    initialize:function() {
        Ext.getCmp('clearSearch').hide();
    },
    searchRes:function (value) {
        var control = this;
        whir.loading.add("", 1)
        var merchantId = Ext.getCmp('merchanId').getValue(),
            salesmanId = Ext.getCmp('salesmanId').getValue();
        Comm.ajaxPost(
            '/merchandise/merchandiseSearch',
            {merchantId:merchantId,salesmanId:salesmanId,merchandiseSearch:value},
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                console.log(msg);
                if(msg.retCode == "SUCCESS"){
                    var data = msg.retData;
                    if(data){
                    var merchandiseList = data.merchandiseList;
                        control.makeDom(merchandiseList);
                        Ext.getCmp('searchView').hideSearchView();
                        Ext.getCmp('productView')._showProList(merchandiseList);
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
            salesmanId = Ext.getCmp('salesmanId').getValue();
        Comm.ajaxPost(
            '/merchandise/showSearchHistory',
            {merchantId:merchantId,salesmanId:salesmanId},
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                console.log(msg);
                if(msg.retCode == "SUCCESS"){
                   var data = msg.retData;
                   var searchHistoryList = data.searchHistoryList;
                    if(searchHistoryList && searchHistoryList.length != 0){
                        var currentSearch = Ext.getCmp('currentSearch');
                        currentSearch.removeAll();
                        var items = [];
                        searchHistoryList.forEach(function (item) {
                            items.push({
                                xtype:'button',
                                html:'<p class="sear-container"><span class="icon-history"></span><span class="searh-name">'+item.searchContent+'</span></p>',
                                style:'text-align:left;border:none;width:90%;margin:0 auto;border-bottom:1px solid #e1e2e7;border-radius:0;',
                                listeners:{
                                    tap:function () {
                                        var key = this.bodyElement.dom.getElementsByClassName('searh-name')[0].innerHTML;
                                        control.searchRes(key);
                                    }
                                }
                            });
                        });
                        currentSearch.add(items);
                        Ext.getCmp('clearSearch').show();
                    }else{
                        Ext.getCmp('clearSearch').hide();
                    }
                }
            }
        );
    },
    //操作首页Dom,退出App按钮改为取消按钮并且重新对商品数量赋值
    makeDom:function (list) {
        if(Ext.getCmp('productView')){
            var proNum = list.length;
            document.getElementById('proNum').innerHTML = proNum+'件';
            document.getElementById('exitApp').style.display = 'none';
            document.getElementById('backPro').style.display = 'block';
        }
    },
    //清除搜索记录
    _clearSearch:function () {
        whir.loading.add("", 1)
        var merchantId = Ext.getCmp('merchanId').getValue(),
            salesmanId = Ext.getCmp('salesmanId').getValue();
        Comm.ajaxPost(
            '/merchandise/clearSearchHistory',
            {merchantId:merchantId,salesmanId:salesmanId},
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                console.log(msg);
                if(msg.retCode == "SUCCESS"){
                    var currentSearch = Ext.getCmp('currentSearch');
                    currentSearch.removeAll();
                    Ext.getCmp('clearSearch').hide();
                    layer.open({content:msg.retMsg,skin:'msg',time:2});
                }
            }
        )
    },
    hideSearchView:function () {
        Ext.getCmp('searchView').hide({type:'slideOut',direction:'down'});
        Ext.getCmp('searchView').on('hide',function () {
            if(Ext.getCmp('searchView')){
                Ext.getCmp('searchView').destroy();
            }
        })
    }
});
