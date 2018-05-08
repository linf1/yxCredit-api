/**
 * 个人信息 基本信息
 */
$(function(){
    //initPage();
});
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
