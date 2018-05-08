$(function () {
    Comm.ajaxPost("/wechat/platform/getCooperation","",function (msg) {
        var retData=msg.retData;
        var host=retData.host;
        var list=retData.list;
        var html="";
        if(list){
            for(var i=0;i<list.length;i++){
                html+='<li onclick="goPlatform(\''+list[i].link+'\',\''+list[i].name+'\')"><div class="imgContainer"><img onerror="src='+_ctx+'\'/resources/cash-wechat/images/login.png\'" src="'+host+list[i].path+'"></div><p>'+list[i].name+'</p></li>';
            }
            $("#platformInfo").html(html).show();
            $("#NoPlatformInfo").hide();
        }else{
            $("#NoPlatformInfo").show();
            $("#platformInfo").hide();
        }
    })
})
function goPlatform(url,name) {
    window.location=_ctx+"/wechat/platform/goToPlatform?url="+url+"&name="+name
}