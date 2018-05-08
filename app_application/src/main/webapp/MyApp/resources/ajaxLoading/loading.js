var whir = window.whir || {};  
whir.loading =  
{  
    add: function (title, opacity) {  
        opacity = opacity == undefined ? 0.4 : opacity;  
        var arr = this.getPageSize();  
        var width = parseInt(arr[2]);  
        var height = parseInt(arr[3]);  
  
        //var loadingImage = _basepath + "Admin/Scripts/jquery-easyui-1.4/themes/default/images/loading.gif";  
        var loadingImage = "resources/ajaxLoading/image/Spinner.svg";
  
        //背景遮罩  
        var mask = document.createElement("div");  
        mask.id = 'mask';  
        mask.style.position = 'fixed';  
        mask.style.left = '0';  
        mask.style.top = '0';  
        mask.style.width = '100%';  
        mask.style.height = parseInt(arr[1]) + "px";  
        mask.style.background = "#F2F2F2";
		mask.style.opacity = '0';
        mask.style.filter = "alpha(opacity=" + opacity * 100 + ")";  
        mask.style.zIndex = "10000";  
        mask.addEventListener('touchstart', function (e) { e.preventDefault(); }, false);   //触摸事件  
        mask.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);    //滑动事件  
        mask.addEventListener('touchend', function (e) { e.preventDefault(); }, false);         //离开元素事件  
        document.body.appendChild(mask);  
  
        //提示文本  
        var loading = document.createElement("div");  
        loading.id = 'loading';  
        loading.style.position = 'absolute';  
        loading.style.left = ((width / 2) - 26) + "px";  
        loading.style.top = (height / 2 - 26) + "px";  
        loading.style.width = '0';  
        loading.style.height = "0";  
        loading.style.lineHeight = "28px";  
        loading.style.display = "inline-block";  
        loading.style.padding = "25px";  
        loading.style.fontSize = " 0.5em";  
        loading.style.fontFamily = " initial";  
        loading.style.zIndex = "100001";  
        loading.style.background = "rgba(0,0,0,0.75)";
        loading.style.border = "1px solid transparent";
		loading.style.borderRadius = '5px';
        loading.style.color = "#000";  
        title = (title != undefined && title.length > 0) ? title : "";  
        loading.innerHTML = title;
        //opts 可从网站在线制作
        var opts = {
            lines: 8, // 花瓣数目
            length: 5, // 花瓣长度
            width: 3, // 花瓣宽度
            radius: 6, // 花瓣距中心半径
            corners: 1, // 花瓣圆滑度 (0-1)
            rotate: 0, // 花瓣旋转角度
            direction: 1, // 花瓣旋转方向 1: 顺时针, -1: 逆时针
            color: '#FFF', // 花瓣颜色
            speed: 1, // 花瓣旋转速度
            trail: 60, // 花瓣旋转时的拖影(百分比)
            shadow: false, // 花瓣是否显示阴影
            hwaccel: false, //spinner 是否启用硬件加速及高速旋转
            className: 'spinner', // spinner css 样式名称
            zIndex: 2e9, // spinner的z轴 (默认是2000000000)
            top: '51%', // spinner 相对父容器Top定位 单位 px
            left: '50%'// spinner 相对父容器Left定位 单位 px
        };
        var spinner = new Spinner(opts).spin(loading);
        document.body.appendChild(loading);  
    },  
    remove: function () {  
        var element = document.getElementById("mask");
        if(!element){
            return;
        }else{
            element.parentNode.removeChild(element);
            element = document.getElementById("loading");
            element.parentNode.removeChild(element);
        }
    },  
    getPageSize: function () {  
        var xScroll, yScroll;  
        if (window.innerHeight && window.scrollMaxY) {  
            xScroll = window.innerWidth + window.scrollMaxX;  
            yScroll = window.innerHeight + window.scrollMaxY;  
        } else {  
            if (document.body.scrollHeight > document.body.offsetHeight) { // all but Explorer Mac      
                xScroll = document.body.scrollWidth;  
                yScroll = document.body.scrollHeight;  
            } else { // Explorer Mac...would also work in Explorer 6 Strict, Mozilla and Safari      
                xScroll = document.body.offsetWidth;  
                yScroll = document.body.offsetHeight;  
            }  
        }  
        var windowWidth = 0;  
        var windowHeight = 0;  
        var pageHeight = 0;  
        var pageWidth = 0;  
  
        if (self.innerHeight) { // all except Explorer      
            if (document.documentElement.clientWidth) {  
                windowWidth = document.documentElement.clientWidth;  
            } else {  
                windowWidth = self.innerWidth;  
            }  
            windowHeight = self.innerHeight;  
        } else {  
            if (document.documentElement && document.documentElement.clientHeight) { // Explorer 6 Strict Mode      
                windowWidth = document.documentElement.clientWidth;  
                windowHeight = document.documentElement.clientHeight;  
            } else {  
                if (document.body) { // other Explorers      
                    windowWidth = document.body.clientWidth;  
                    windowHeight = document.body.clientHeight;  
                }  
            }  
        }  
        // for small pages with total height less then height of the viewport      
  
        if (yScroll < windowHeight) {  
            pageHeight = windowHeight;  
        } else {  
            pageHeight = yScroll;  
        }  
        // for small pages with total width less then width of the viewport      
        if (xScroll < windowWidth) {  
            pageWidth = xScroll;  
        } else {  
            pageWidth = windowWidth;  
        }  
        var arrayPageSize = new Array(pageWidth, pageHeight, windowWidth, windowHeight);  
        return arrayPageSize;  
    }  
};  
/*
window.onload = function () {
    whir.loading.remove();  
};  
whir.loading.add("", 1);  */
