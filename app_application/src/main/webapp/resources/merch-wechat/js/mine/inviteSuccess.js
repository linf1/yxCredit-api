$(function () {
    getRedMoney();
});
function getRedMoney() {
    Comm.ajaxPost("/red/getRedList",'',function (data) {
        var retData=data.retData;
        console.log(retData);
        var redMoney = retData.redAllMoney;//所有金额
        $("#allMoney").html(redMoney);
        var redAvailableAmount = retData.redAvailableAmount;//可用金额
        $("#redAvailableAmount").html("剩余可使用: ￥"+redAvailableAmount)
        var redList=retData.redList;
        var html1="";
        var html2="";
        var html3="";
        var html4="";
        if(redList){
            for(var i=0;i<redList.length;i++){
                var create_time = redList[i].create_time;
                create_time = create_time.substring(0,4)+"."+create_time.substring(4,6)+"."+create_time.substring(6,8);
                html1+='<p><img src="../../resources/merch-wechat/images/picture.png" alt="" style="width:40px;"></p>'
                html2+='<p>'+redList[i].tel+'</p>';
                html3+='<p>'+create_time+'</p>';
                html4+='<p>成功申购商品</p>';
            }
            $(".FriendsRecordContainerOne").html(html1);
            $(".FriendsRecordContainerTwo").html(html2);
            $(".FriendsRecordContainerThree").html(html3);
            $(".FriendsRecordContainerFour").html(html4);
        }
    })
}