var Comm = Comm || {};
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
Comm.ajaxPost = function (url, params, callback, errorCallback, completeCallback, timeout,async,contentType) {
    if (url.indexOf('?') != -1) {
        url += "&r=" + new Date().getTime();
    } else {
        url += "?r=" + new Date().getTime();
    }
    if (timeout == null || typeof(timeout) == "undefined") {
        timeout = 30 * 1000;
    }
    if (async == null || typeof(async) == "undefined") {
        async = true;
    }
    if (contentType == null || typeof(contentType) == "undefined") {
        contentType = "application/x-www-form-urlencoded";
    }
    url = _ctx + url;
    $.ajax({
        type:"POST",
        url: url,
        data:params,
        async:async,
        contentType:contentType,
        timeout: timeout,
        beforeSend:function () {
            whir.loading.add("", 1);
        },
        success: function(response){
            callback(response);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown){
            layer.open({content:"出现错误！", skin: 'msg', time: 2 });
        },
        complete:function (XMLHttpRequest,status) {
            whir.loading.remove();
            if(status=='timeout'){
                ajaxTimeoutTest.abort();
                layer.open({content:"请求超时，请检查网络！", skin: 'msg', time: 2 });
            }
        }
    });
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
Comm.ajaxGet = function (url, params, callback, errorCallback, completeCallback, timeout,async,contentType) {
    if (url.indexOf('?') != -1) {
        url += "&r=" + new Date().getTime();
    } else {
        url += "?r=" + new Date().getTime();
    }
    if (timeout == null || typeof(timeout) == "undefined") {
        timeout = 30 * 1000;
    }
    if (async == null || typeof(async) == "undefined") {
        async = true;
    }
    if (contentType == null || typeof(contentType) == "undefined") {
        contentType = "application/x-www-form-urlencoded";
    }
    url = _ctx + url;
    $.ajax({
        type:"GET",
        url: url,
        data:params,
        async:async,
        contentType:contentType,
        timeout: timeout,
        beforeSend:function () {
            whir.loading.add("", 1);
        },
        success: function(response){
            callback(response);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown){
            layer.open({content:"出现错误！", skin: 'msg', time: 2 });
        },
        complete:function (XMLHttpRequest,status) {
            whir.loading.remove();
            if(status=='timeout'){
                ajaxTimeoutTest.abort();
                layer.open({content:"请求超时，请检查网络！", skin: 'msg', time: 2 });
            }
        }
    });
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

