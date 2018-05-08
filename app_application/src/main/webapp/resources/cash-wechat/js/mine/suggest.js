/**
 * Created by Administrator on 2017/10/30 0030.
 */
$(".weui-textarea").on("keyup",function () {
    if($(this).val().length<=400){
        $("#wordCounter").html($(this).val().length);
    }
})
$(".my-btn").on("click",function () {
    var Reg=/^([a-zA-Z0-9\u4e00-\u9fa5\·]{1,400})$/;
    var suggestContent=$(".weui-textarea").val();
    if(!suggestContent){
        layer.open({content: '反馈内容不能为空',skin: 'msg',time: 2 });
        return;
    }else if(!Reg.test(suggestContent)){
        layer.open({content: '反馈内容不能包含特殊字符',skin: 'msg',time: 2 });
        return;
    }
    Comm.ajaxPost("saveFeedback","",function (sms) {
        layer.open({content: sms.retMsg,skin: 'msg',time: 2 ,end:function () {
            //localStorage["isMine"] = true;
            history.go(-1);
        }});
    })
})