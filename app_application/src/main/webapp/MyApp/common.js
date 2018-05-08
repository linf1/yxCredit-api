var Comm = Comm || {};

Comm.getCtx = function () {
    /*
     ****取localStorage中存储的上下文
     */
    var _ctx="";
    if(localStorage['_ctx']){
        _ctx = localStorage['_ctx']
    }
    return _ctx;
}

Comm.isIE6 = !window.XMLHttpRequest;	//ie6

/**
 * 获取URL参数值
 * @type {Comm.urlParam}
 */
Comm.getRequest = Comm.urlParam = function () {
    var param, url = location.search, theRequest = {};
    if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        strs = str.split("&");
        for (var i = 0, len = strs.length; i < len; i++) {
            param = strs[i].split("=");
            theRequest[param[0]] = decodeURIComponent(param[1]);
        }
    }
    return theRequest;
};

/**
 * 通用post请求，返回json对象
 * @param url 请求地址
 * @param params 请求参数，没有传null
 * @param callback 成功时的回调函数
 * @param timeout 超时时间，参数可以不传
 * @param async 异步标志，true表示异步，false同步，参数可以不传
 * @param errorCallback 错误时的回调函数，参数可以不传
 * @param completeCallback 结束回调函数，参数可以不传
 */
Comm.ajaxPost = function (url, params, callback, errorCallback, completeCallback, timeout) {
    if (url.indexOf('?') != -1) {
        url += "&r=" + new Date().getTime();
    } else {
        url += "?r=" + new Date().getTime();
    }
    if (timeout == null || typeof(timeout) == "undefined") {
        timeout = 240 * 1000;
    }
    url = Comm.getCtx() + url;
    Ext.Ajax.request({
        url: url + "&EXT=1&app_user_id=" + localStorage['user_id'] + "&app_tel=" + localStorage['tel'] + "&app_registration_id=" + localStorage['registration_id'],
        method: 'POST',
        params: params,
        timeout: timeout,
        async: true,
        success: function (response) {
            data = eval('(' + response.responseText + ')');
            if (data.retCode == 'EXCEPTION') {
                Ext.Viewport.unmask();
                whir.loading.remove();
                layer.open({content: data.retMsg, skin: 'msg', time: 3 });
            } else if (data.retCode == 'SESSIONTIMEOUT') {
                layer.open({content:"请先登录哟~!", skin: 'msg', time: 1,end:function () {
                    // var items = Ext.Viewport.items.items;
                    // var ln = items.length;
                    // for (var i = items.length - 1; i > 1; i--) {
                    //     if (items[i]) {
                    //         if (items[i].getId() != "homeview") {
                    //             items[i].destroy();
                    //         }
                    //     }
                    // }
                    localStorage['user_id'] = '';
                    localStorage['tel'] = '';
                    localStorage['registration_id'] = '';
                    localStorage['expirationTime'] = '';
                    Ext.Viewport.setActiveItem('login');
                    Ext.Viewport.unmask();
                    whir.loading.remove();
                } });
            } else {
                callback(response);
            }

        },
        failure: function (response) {
            Ext.Viewport.unmask();
            whir.loading.remove();
            layer.open({content:"请求超时，请检查网络！", skin: 'msg', time: 2 });
            //errorCallback && errorCallback(response);
        },
        complete: function () {
            completeCallback && completeCallback();
        }
    })
};
//确认合同同步请求
Comm.ajaxContract = function (url, params, callback, errorCallback, completeCallback, timeout) {
    if (url.indexOf('?') != -1) {
        url += "&r=" + new Date().getTime();
    } else {
        url += "?r=" + new Date().getTime();
    }
    if (timeout == null || typeof(timeout) == "undefined") {
        timeout = 30 * 1000;
    }
    url = Comm.getCtx() + url;
    Ext.Ajax.request({
        url: url + "&EXT=1&app_user_id=" + localStorage['user_id'] + "&app_tel=" + localStorage['tel'] + "&app_registration_id=" + localStorage['registration_id'],
        method: 'POST',
        params: params,
        timeout: timeout,
        async: false,
        success: function (response) {
            data = eval('(' + response.responseText + ')');
            if (data.retCode == 'EXCEPTION') {
                Ext.Viewport.unmask();
                whir.loading.remove();
                layer.open({content: data.retMsg, skin: 'msg', time: 3 });
            } else if (data.retCode == 'SESSIONTIMEOUT') {
                layer.open({content:"会话失效, 请重新登录!", skin: 'msg', time: 1,end:function () {
                    // var items = Ext.Viewport.items.items;
                    // var ln = items.length;
                    // for (var i = items.length - 1; i > 1; i--) {
                    //     if (items[i]) {
                    //         if (items[i].getId() != "homeview") {
                    //             items[i].destroy();
                    //         }
                    //     }
                    // }
                    localStorage['user_id'] = '';
                    localStorage['tel'] = '';
                    localStorage['registration_id'] = '';
                    localStorage['expirationTime'] = '';
                    Ext.Viewport.setActiveItem('login');
                    Ext.Viewport.unmask();
                    whir.loading.remove();
                } });
            } else {
                callback(response);
            }

        },
        failure: function (response) {
            whir.loading.remove();
            layer.open({content:"请求超时，请检查网络！", skin: 'msg', time: 2 });
            //errorCallback && errorCallback(response);
        },
        complete: function () {
            completeCallback && completeCallback();
        }
    })
};
Comm.ajaxScan = function (url, params, callback, errorCallback, completeCallback, timeout) {
    if (url.indexOf('?') != -1) {
        url += "&r=" + new Date().getTime();
    } else {
        url += "?r=" + new Date().getTime();
    }
    if (timeout == null || typeof(timeout) == "undefined") {
        timeout = 30 * 1000;
    }
    Ext.Ajax.request({
        url: url + "&EXT=1&app_user_id=" + localStorage['user_id'] + "&app_tel=" + localStorage['tel'] + "&app_registration_id=" + localStorage['registration_id'],
        method: 'POST',
        params: params,
        timeout: timeout,
        async: true,
        success: function (response) {
            data = eval('(' + response.responseText + ')');
            if (data.retCode == 'EXCEPTION') {
                Ext.Viewport.unmask();
                whir.loading.remove();
                layer.open({content:data.retMsg, skin: 'msg', time: 3});
            } else if (data.retCode == 'SESSIONTIMEOUT') {
                layer.open({content:"会话失效, 请重新登录!", skin: 'msg', time: 3,end:function () {
                    // var items = Ext.Viewport.items.items;
                    // var ln = items.length;
                    // for (var i = items.length - 1; i > 1; i--) {
                    //     if (items[i]) {
                    //         if (items[i].getId() != "homeview") {
                    //             items[i].destroy();
                    //         }
                    //     }
                    // }
                    localStorage['user_id'] = '';
                    localStorage['tel'] = '';
                    localStorage['registration_id'] = '';
                    localStorage['expirationTime'] = '';
                    Ext.Viewport.setActiveItem('login');
                }});
            } else {
                callback(response);
            }

        },
        failure: function (response) {
            Ext.Viewport.unmask();
            whir.loading.remove();
            layer.open({content:"请求超时，请检查网络！", skin: 'msg', time: 2 });
            //errorCallback && errorCallback(response);
        },
        complete: function () {
            completeCallback && completeCallback();
        }
    })
};
/**
 * 通用get请求，返回json对象
 * @param url 请求地址
 * @param params 请求参数，没有传null
 * @param callback 成功时的回调函数
 * @param timeout 超时时间，参数可以不传
 * @param async 异步标志，true表示异步，false同步，参数可以不传
 * @param errorCallback 错误时的回调函数，参数可以不传
 * @param completeCallback 结束回调函数，参数可以不传
 */
Comm.ajaxGet = function (url, params, callback, timeout, errorCallback, completeCallback) {
    if (url.indexOf('?') != -1) {
        url += "&r=" + new Date().getTime();
    } else {
        url += "?r=" + new Date().getTime();
    }
    if (timeout == null || typeof(timeout) == "undefined") {
        timeout = 30 * 1000;
    }
    Ext.Ajax.request({
        url: url + "&EXT=1",
        method: 'GET',
        params: params,
        timeout: timeout,
        success: function (response) {
            isTimeOut(response);
            callback(response);
        },
        failure: function (response) {
            errorCallback && errorCallback(response);
        },
        complete: function () {
            completeCallback && completeCallback();
        }
    })
};
/**
 * 设置未来(全局)的AJAX请求默认选项
 * 主要设置了AJAX请求遇到Session过期的情况
 */

function isTimeOut(response) {
    var sessionStatus = response.getResponseHeader('sessionstatus');
    if (sessionStatus == 'timeout') {
        //清除定时器
        // clearInterval(timer);
        // timer=null;
        Ext.Viewport.unmask();
        return false;
    }
    return true;
}
//我的消息函数
//签订合同
function checkContract(order_id) {
    Ext.ComponentQuery.query('#middleSaveview #order_id')[0].setValue(order_id);
    if(Ext.ComponentQuery.query('#confirmordercontractView')[0]){
        Ext.ComponentQuery.query('#confirmordercontractView')[0].show();
    }else{
        var confimcontractview = Ext.create('MyApp.view.orders.confirmordercontract');//新建页面
        Ext.Viewport.add(confimcontractview);
        confimcontractview.show();
    }
};
/**
 * ****我的消息对应的方法
 */

//查看合同
function lookContract(order_id) {
    Ext.ComponentQuery.query('#middleSaveview #order_id')[0].setValue(order_id);
    if(Ext.ComponentQuery.query('#contractView')[0]){
        Ext.ComponentQuery.query('#contractView')[0].show();
    }else{
        var contractview = Ext.create('MyApp.view.orders.Contract');//新建页面
        Ext.Viewport.add(contractview);
        contractview.show();
    }
};
//免登陆签约
function checkNoLoginSign(order_id) {//签订协议
    Ext.ComponentQuery.query('#middleSaveview #order_id')[0].setValue(order_id);
    if(Ext.ComponentQuery.query('#SurfacesignedView')[0]){
        Ext.ComponentQuery.query('#SurfacesignedView')[0].show();
    }else{
        var surfacesignedview = Ext.create('MyApp.view.openaccount.surfacesigned');//新建页面
        Ext.Viewport.add(surfacesignedview);
        surfacesignedview.show();
    }
};
//开户
function openAccount(channel, order_id) {
    //确定是否已经开过户了
    if (channel == "1") {
        Ext.ComponentQuery.query('#middleSaveview #order_id')[0].setValue(order_id);
        var fy = Ext.ComponentQuery.query('#fyopenaccountview')[0];
        if (fy) {
            Ext.ComponentQuery.query('#fyopenaccountview')[0].show();
        } else {
            var fyview = Ext.create('MyApp.view.openaccount.Fyopenaccount');//新建页面
            Ext.Viewport.add(fyview);
            fyview.show();
        }
    } else if (channel == "2") {
        Ext.ComponentQuery.query('#middleSaveview #order_id')[0].setValue(order_id);
        var jx = Ext.ComponentQuery.query('#Jxopenaccountview')[0];
        if (jx) {
            Ext.ComponentQuery.query('#Jxopenaccountview')[0].show();
        } else {
            var jxview = Ext.create('MyApp.view.openaccount.Jxopenaccount');//新建页面
            Ext.Viewport.add(jxview);
            jxview.show();
        }
    }
};
//提现页面
function takeCash(order_id) {//
    if(Ext.ComponentQuery.query('#myAccountview')[0]){
        Ext.ComponentQuery.query('#myAccountview')[0].show();
    }else{
        var myAccountview=Ext.create('MyApp.view.setting.Myaccount');//新建页面
        Ext.Viewport.add(myAccountview);
        myAccountview.show();
    }
};

/*hide动画*/
function hidePage(pageId) {
    if(!Ext.ComponentQuery.query(pageId)[0]){
        return;
    }
    Ext.ComponentQuery.query(pageId)[0].hide({type:'slideOut',direction:'right'});
    Ext.ComponentQuery.query(pageId)[0].on("hide",function () {
        if(Ext.ComponentQuery.query(pageId)[0]){
            Ext.ComponentQuery.query(pageId)[0].destroy();
        }
    });
}

/*******数组排序********/
function sortNumber(a,b) {
    return a - b
}