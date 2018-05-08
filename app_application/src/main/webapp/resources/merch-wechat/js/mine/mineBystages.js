$(".btnList ul li").on("click",function () {
    $(this).siblings("li.activeBtn").removeClass("activeBtn");
    $(this).addClass("activeBtn");
})