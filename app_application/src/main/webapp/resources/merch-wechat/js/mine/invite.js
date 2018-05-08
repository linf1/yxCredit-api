$(function () {
    getRedMoney();
});
function getRedMoney() {
    Comm.ajaxPost("/red/getRedList",'',function (data) {
        var retData=data.retData;
        var redMoney = retData.redAllMoney;
        $("#redMoney").html("已获得奖励 : "+redMoney+"元")
    })
}
$(".InviteFriendsBtn").on("click",function (data){
    try{
        if(linked.inviteContract&&typeof(linked.inviteContract)=="function"){
            var title="商品分期";
            var content="选你所需，无需等待！";
            var url="/wechat/page/register";
            linked.inviteContract(title+"$"+content+"$"+url);
        }else{
        }
    }catch(e){
        alert(e);
    }
});
$("#rewards").on("click",function (data){
    window.location=_ctx+"/wechat/page/friendDetail";
});