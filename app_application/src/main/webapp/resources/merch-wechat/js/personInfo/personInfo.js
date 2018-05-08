/**
 * 个人信息 基本信息
 */
$(function(){
    personInfo();
    $('.weui-navbar__item').on('click', function () {
        $(this).addClass('weui-bar__item--on').siblings().removeClass('weui-bar__item--on');
        var tabId = $(this).attr('href');
        $('.weui-tab__bd').find(tabId).addClass('weui-tab__bd-item--active').siblings().removeClass('weui-tab__bd-item--active');
        if(tabId=="#tab1"){
            personInfo();
        }else if(tabId=="#tab2"){
            basicInfo();
        }else if(tabId=="#tab3"){
            authorizationInfo();
        }
    });


});
//查看个人信息接口
function personInfo(){
    Comm.ajaxPost("/apply/getApplyInfo","",function (data) {
        var retData=data.retData;
        var indentityMap = retData.indentityMap;//身份认证信息
        if(indentityMap){
            $("#relName").val(indentityMap.realname);
            $("#sexName").val(indentityMap.sexName);
            $("#birthday").val(indentityMap.birth);
            $("#cardNum").val(indentityMap.cardNo);
            $("#address").val(indentityMap.cardRegissterAddress);
        }
        var linkManMap = retData.linkManMap;//联系人信息

        if(linkManMap.linkmanlist){
            var linkmanlist=linkManMap.linkmanlist;//直系联系人信息
            for(var i=0;i<linkmanlist.length;i++){
                $("#linkName1").val(linkmanlist[i].linkName);
                $("#contact1").val(linkmanlist[i].contact);
                $("#relationship1").val(linkmanlist[i].relationshipname);
                $("#linkName2").val(linkmanlist[i].linkName);
                $("#contact2").val(linkmanlist[i].contact);
                $("#relationship2").val(linkmanlist[i].relationshipname);
            }
        }
        if(linkManMap.olinkmanlist){
            var olinkmanlist = linkManMap.olinkmanlist;//其他联系人
            console.log(olinkmanlist);
            for(var i=0;i<olinkmanlist.length;i++){
                $("#linkName3").val(olinkmanlist[i].linkName);
                $("#contact3").val(olinkmanlist[i].contact);
                $("#relationship3").val(olinkmanlist[i].relationshipname);
                $("#linkName4").val(olinkmanlist[i].linkName);
                $("#contact4").val(olinkmanlist[i].contact);
                $("#relationship4").val(olinkmanlist[i].relationshipname);
            }
        }
    })
}
//获取基本信息接口
function basicInfo(){
    Comm.ajaxPost("/apply/getBasicInfo","",function (data) {
        var retData=data.retData;
        if(retData.liveMap !=null){
            var liveMap = retData.liveMap;
            $("#liveAddr").val(liveMap.provinces+"/"+liveMap.city+"/"+liveMap.distric);
            $("#livePlace").val(liveMap.addressDetail);
        }
        if(retData.jobMap !=null){
            var jobMap = retData.jobMap;
            var type = jobMap.jobType_id;
            if(type=="0"){
                $("#category_work_img").attr("src",_ctx+"/resources/merch-wechat/images/workMan.png");
            }else{
                $("#category_boss_img").attr("src",_ctx+"/resources/merch-wechat/images/bossMan.png");
            }
            $("#companyName").val(jobMap.companyName);//公司名称
            $("#companyPhone").val(jobMap.companyPhone);
            $("#posLevel").val(jobMap.posLevel);//职位
            $("#companyAddr").val(jobMap.provinceName+"/"+jobMap.cityName+"/"+jobMap.districtName);
            $("#companyAddrDetail").val(jobMap.companyAddress);
        }
    })
}
//获取授权信息
function authorizationInfo(){
    Comm.ajaxPost("/apply/getCustomerIdentityState","",function (data) {
        var retData=data.retData;
        var taoBaoState = retData.taoBaoState;
        if(taoBaoState=="1"){
            $("#taobaoSate").html("已授权");
            $("#taobaoSate").css("color: #000");
        }else{
            $("#taobaoSate").html("未授权");
        }
    })
}
/**选择身份类别**/
$(".categoryItems").on("click",function () {
    var isChecked=$(this).find("input").val();
    $("#category_work").val("").next("img").attr("src",$("#category_work").next("img").attr("alt"));
    $("#category_boss").val("").next("img").attr("src",$("#category_boss").next("img").attr("alt"));
    if(!isChecked){
        $(this).find("img").attr("src",$(this).find("img").attr("src_active"));
        $(this).find("input").val(true);
    }
})
