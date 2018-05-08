<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="stylesheet" href="${ctx}/resources/lib/css/swiper.min.css">
<style>
    /*包裹自定义分页器的div的位置等CSS样式*/
    .swiper-pagination-custom {
        bottom: 10px;
        left: 0;
        width: 100%;
        text-align: right;
    }
    /*自定义分页器的样式，这个你自己想要什么样子自己写*/
    .swiper-pagination-customs {
        width: 30px;
        height: 4px;
        display: inline-block;
        background: #000;
        opacity: .3;
        margin: 0 5px;
    }
    /*自定义分页器激活时的样式表现*/
    .swiper-pagination-customs-active {
        opacity: 1;
        background-color: #F78E00;
    }
</style>
<div class="toolBar">
    <p><span>秒付金服</span></p>
</div>
<div class="productContainer">
    <div class="productTitle">
        <ul>
            <li id="liOne" class="productActive"><p><i></i><span>商品分期</span></p><p></p></li>
            <li id="liTwo"><p><i></i><span>现金分期</span></p><p></p></li>
        </ul>
    </div>
    <div class="productContent">
        <!-- Swiper -->
        <div class="swiper-container" id="swiperOne">
            <div class="swiper-wrapper">
                <div class="swiper-slide">
                    <img src="${ctx}/resources/merch-wechat/images/demo1.png${version}" alt="">
                    <p><img src="${ctx}/resources/merch-wechat/images/saomaBackground.png" alt=""></p>
                </div>
                <div class="swiper-slide">
                    <img src="${ctx}/resources/merch-wechat/images/demo.png${version}" alt="">
                    <p class="money"><span>10000.00</span></p>
                    <p><img src="${ctx}/resources/merch-wechat/images/applicationBackground.png" alt=""></p>
                </div>
            </div>
            <!-- Add Pagination -->
            <div class="swiper-pagination"></div>
        </div>
    </div>
    <div class="productFooter">
        <!-- Swiper -->
        <div class="swiper-container" id="swiperTwo">
            <div class="swiper-wrapper">
                <div class="swiper-slide"><img src="${ctx}/resources/merch-wechat/images/demo2.png" alt=""></div>
                <div class="swiper-slide"><img src="${ctx}/resources/merch-wechat/images/demo2.png" alt=""></div>
                <div class="swiper-slide"><img src="${ctx}/resources/merch-wechat/images/demo2.png" alt=""></div>
            </div>
            <!-- Add Pagination -->
            <div class="swiper-pagination"></div>
        </div>
    </div>
</div>
<script src="${ctx}/resources/lib/js/swiper.min.js"></script>
<script>
    new Swiper('#swiperOne', {
        direction: 'horizontal',
        // 如果需要分页器
        pagination: '.swiper-pagination',
        paginationType: 'custom',//这里分页器类型必须设置为custom,即采用用户自定义配置
        onSlideChangeEnd:function(swiper){
            if(swiper.activeIndex==0){
                $("#liOne").addClass("productActive");$("#liTwo").removeClass("productActive")
            }else{
                $("#liTwo").addClass("productActive");$("#liOne").removeClass("productActive")
            }
        }
    });
    new Swiper('#swiperTwo', {
        direction: 'horizontal',
        loop: true,
        speed: 600,
        autoplay:3000,
        // 如果需要分页器
        pagination: '.swiper-pagination',
        paginationType: 'custom',//这里分页器类型必须设置为custom,即采用用户自定义配置
        //下面方法可以生成我们自定义的分页器到页面上
        paginationCustomRender: function(swiper, current, total) {
            var customPaginationHtml = "";
            for(var i = 0; i < total; i++) {
                //判断哪个分页器此刻应该被激活
                if(i == (current - 1)) {
                    customPaginationHtml += '<span class="swiper-pagination-customs swiper-pagination-customs-active"></span>';
                } else {
                    customPaginationHtml += '<span class="swiper-pagination-customs"></span>';
                }
            }
            return customPaginationHtml;
        }
    });
//    $("#liOne").on("click",function () {
//        $(this).addClass("productActive");
//        $("#liTwo").removeClass("productActive");
//    })
//    $("#liTwo").on("click",function () {
//        $(this).addClass("productActive");
//        $("#liOne").removeClass("productActive");
//    })
</script>