/**
 * Created by Administrator on 2017/10/31 0031.
 */
$(".fixedSysMsg").on("click",function () {
    $(".centerMsg").hide();
    $(".sysMsgContent").show();
    $("#msgTitle").html("系统消息");
    getSysMsg();
})
$(".icon-back").on("click",function () {
    if($("#msgTitle").html()=="系统消息"){
        Comm.ajaxPost("/wechat/msg/setReadyMsg","type=0",function (sms) {
            $(".centerMsg").show();
            $(".sysMsgContent").hide();
            $("#msgTitle").html("消息中心");
        },"","","",false);
    }else{
        Comm.ajaxPost("/wechat/msg/setReadyMsg","type=1",function (sms) {
            history.go(-1);
        },"","","",false);
    }
})
/**获取消息**/
$(function(){
    Comm.ajaxPost("/wechat/msg/getMsgList","type=",function (result) {
        var pageList=result.retData.pageList;
        var completeMap=result.retData.completeMap;
        if(completeMap){
            $("#authorization_complete").val(completeMap.authorization_complete);
            $("#identity_complete").val(completeMap.identity_complete);
            $("#person_info_complete").val(completeMap.person_info_complete)
        }
        if(pageList){
            var msgHtmlFiexd="";
            var msgHtml="";
            for(var i=0;i<pageList.length;i++){
                var time=timeFormat(pageList[i].alter_time);
                var state=pageList[i].state;
                var order_state=pageList[i].order_state;
                var btnText="";
                var title = pageList[i].title;
                var update_state=pageList[i].update_state;
                var orderId=pageList[i].order_id;
                if(i==0){
                    msgHtmlFiexd+= '<div class="sysMsgContainer">' +
                        '<div><span class="unread unreadMsg" msgSate="'+state+'"></span><img src='+_ctx+'"/resources/cash-wechat/images/mecte.png" alt=""></div>' +
                        '<div><p class="sysMsgTitle">系统消息<span style="float: right">'+time+'</span></p>' +
                        '<p class="sysMsg">'+pageList[i].content+'</p>' +
                        '</div></div>';
                }else{
                    if(order_state == '5'){
                        btnText = '鉴权绑卡';
                        if(update_state=="1"){
                            msgHtml+='<div class="orderMsgContent" update_state="'+pageList[i].update_state+'"><p class="orderMsgTitle"><span class="unread" msgSate="'+state+'"></span>'+pageList[i].title+'</p><p class="orderMsgTime">'+time+'</p><p class="orderMsg">'+pageList[i].content+'</p><p class="orderBtn" onclick="orderBtn(\''+order_state+'\',\''+orderId+'\')"><span>'+btnText+'</span><span class="icon-more right"></span></p></div>';
                        }else{
                            msgHtml+='<div class="orderMsgContent" update_state="'+pageList[i].update_state+'"><p class="orderMsgTitle"><span class="unread" msgSate="'+state+'"></span>'+pageList[i].title+'</p><p class="orderMsgTime">'+time+'</p><p class="orderMsg">'+pageList[i].content+'</p></div>';
                        }
                    }else if(order_state == '0'){
                        btnText = '继续申请';
                        if(update_state=="1"){
                            msgHtml+='<div class="orderMsgContent" update_state="'+pageList[i].update_state+'"><p class="orderMsgTitle"><span class="unread" msgSate="'+state+'"></span>'+pageList[i].title+'</p><p class="orderMsgTime">'+time+'</p><p class="orderMsg">'+pageList[i].content+'</p><p class="orderBtn" onclick="orderBtn(\''+order_state+'\',\''+orderId+'\')"><span>'+btnText+'</span><span class="icon-more right"></span></p></div>';
                        }else{
                            msgHtml+='<div class="orderMsgContent" update_state="'+pageList[i].update_state+'"><p class="orderMsgTitle"><span class="unread" msgSate="'+state+'"></span>'+pageList[i].title+'</p><p class="orderMsgTime">'+time+'</p><p class="orderMsg">'+pageList[i].content+'</p></div>';
                        }
                    }else if(order_state == '7'){
                        btnText = '查看合同';
                        msgHtml+='<div class="orderMsgContent" update_state="'+pageList[i].update_state+'"><p class="orderMsgTitle"><span class="unread" msgSate="'+state+'"></span>'+pageList[i].title+'</p><p class="orderMsgTime">'+time+'</p><p class="orderMsg">'+pageList[i].content+'</p><p class="orderBtn" onclick="orderBtn(\''+order_state+'\',\''+orderId+'\')"><span>'+btnText+'</span><span class="icon-more right"></span></p></div>';
                    }else if(title == '绑卡成功'){
                        btnText = '确认合同';
                        if(update_state=="1"){
                            msgHtml+='<div class="orderMsgContent" update_state="'+pageList[i].update_state+'"><p class="orderMsgTitle"><span class="unread" msgSate="'+state+'"></span>'+pageList[i].title+'</p><p class="orderMsgTime">'+time+'</p><p class="orderMsg">'+pageList[i].content+'</p><p class="orderBtn" onclick="orderBtn(\''+title+'\',\''+orderId+'\')"><span>'+btnText+'</span><span class="icon-more right"></span></p></div>';
                        }else{
                            msgHtml+='<div class="orderMsgContent" update_state="'+pageList[i].update_state+'"><p class="orderMsgTitle"><span class="unread" msgSate="'+state+'"></span>'+pageList[i].title+'</p><p class="orderMsgTime">'+time+'</p><p class="orderMsg">'+pageList[i].content+'</p></div>';
                        }
                    }else{
                        msgHtml+='<div class="orderMsgContent" update_state="'+pageList[i].update_state+'"><p class="orderMsgTitle"><span class="unread" msgSate="'+state+'"></span>'+pageList[i].title+'</p><p class="orderMsgTime">'+time+'</p><p class="orderMsg">'+pageList[i].content+'</p></div>';
                    }
                }
            }
            $(".fixedSysMsg").html(msgHtmlFiexd);
            $("#msgOne").html(msgHtml);

            var list=$(".unread");
            for(var i=0;i<list.length;i++){
                if($(list[i]).attr("msgSate")!="0"){
                   $(list[i]).hide()
                }
            }
        }
    });
});
/**获取系统消息**/
function getSysMsg() {
    Comm.ajaxPost("/wechat/msg/getMsgList","type=0",function (result) {
        var pageList=result.retData.system;
        if(pageList){
            var msgHtml="";
            for(var i=0;i<pageList.length;i++){
                var time=timeFormat(pageList[i].alter_time);
                var sysSate=pageList[i].state;
                msgHtml+='<div class="orderMsgContent" update_state="'+pageList[i].update_state+'"><p class="orderMsgTitle"><span class="unread" msgSate="'+sysSate+'"></span>'+pageList[i].title+'</p><p class="orderMsgTime">'+time+'</p><p class="orderMsg">'+pageList[i].content+'</p></div>';
            }
            $("#msgTwo").html(msgHtml);
            var list=$(".unread");
            for(var i=0;i<list.length;i++){
                if($(list[i]).attr("msgSate")!="0"){
                    $(list[i]).hide()
                }
            }
        }
    });
}
function timeFormat(t) {
    return t.substring(0,4)+"-"+t.substring(4,6)+"-"+t.substring(6,8)+" "+t.substring(8,10)+":"+t.substring(10,12)
}
/**消息按钮**/
function orderBtn(title_order_state,orderId) {
    if(title_order_state=="0"){//继续申请
        var authorization_complete=$("#authorization_complete").val();
        var identity_complete=$("#identity_complete").val();
        var person_info_complete=$("#person_info_complete").val();
        if(identity_complete!="100"){window.location=_ctx+"/wechat/home/identityPage?orderId="+orderId}
        if(person_info_complete!="100"){window.location=_ctx+"/wechat/identity/personInfoPage?orderId="+orderId}
        if(authorization_complete!="100"){window.location=_ctx+"/wechat/basicinfo/accredit?orderId="+orderId}
        if(identity_complete=="100"&&person_info_complete=="100"&&authorization_complete=="100"){window.location=_ctx+"/wechat/basicinfo/accredit?orderId="+orderId};
    }
    if(title_order_state=="5"){//鉴权绑卡 开户
        window.location=_ctx+'/wechat/ybCard/openAccount';
    }
    if(title_order_state=="7"){//查看合同
        window.location=_ctx+"/wechat/loanAgreement/goToContract?isChecked=true&&orderId="+orderId;
    }
    if(title_order_state=="绑卡成功"){//签订合同
        window.location=_ctx+"/wechat/loanAgreement/goToContract?isChecked=false&&orderId="+orderId;
    }
}