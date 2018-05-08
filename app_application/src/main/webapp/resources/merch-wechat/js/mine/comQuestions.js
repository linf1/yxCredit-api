/**
 * Created by Administrator on 2017/10/30 0030.
 */

//记录返回的问题类型
var wechat_comQuestionDetail_idKey ="";
$(".weui-cell").on("click",function () {
    if($(this).attr("data-isShow")=="false"){
        $(this).next(".questionsDetail").show();
        $(this).attr("data-isShow","true");
        $(this).find(".weui-cell__ft").css("transform","rotate(180deg)");
    }else{
        $(this).next(".questionsDetail").hide();
        $(this).attr("data-isShow","false");
        $(this).find(".weui-cell__ft").css("transform","rotate(0deg)");
    }
});
/**更多问题**/
$(".getMore").on("click",function () {
    wechat_comQuestionDetail_idKey = "#comQuestionDetail_"+$(this).prev().text();
    $(wechat_comQuestionDetail_idKey).show();
    $("#comQuestion").hide();
    $("#comQuestionTitle").html(wechat_comQuestionDetail_idKey.split("_")[1]);
});
/**返回*/
$(".icon-back").on("click",function () {
    if($("#comQuestionTitle").html()!="常见问题"){
        $(wechat_comQuestionDetail_idKey).hide();
        wechat_comQuestionDetail_idKey = "";
        $("#comQuestion").show();
        $("#comQuestionTitle").html("常见问题");
    }else{
        //localStorage["isMine"] = true;
        history.go(-1);
    }
});