/**
 * 补充发货照片
 * Created by zl on 2017/12/11 0011.
 */
Ext.define('MyApp.view.orderManager.SupplyPhoto',{
   extend:'Ext.Panel',
   xtype:'supplyImgView',
   requires:[
       'Ext.Panel'
   ],
   config:{
       id:'supplyImgView',
       showAnimation:{type:'slide', direction:'left'},
       layout:{type:'vbox'},
       zIndex:5,
       items:[
           {
               xtype:'toolbar',
               title:'补充发货照片',
               docked : "top",
               style:'background-color:#FEE300;border-radius:0;',
               items:[
                   {
                       html: '<span class="icon-back" style="font-size:26px;color:#000;"></span>',
                       style:'margin-left:10px;color:#8c8c8c;margin-top:8px;',
                       listeners:{
                           tap:function () {
                               hidePage('#supplyImgView');
                           }
                       }
                   }
               ]
           },
           {
               xtype:'actionsheet',
               hidden:true,
               docked:'bottom',
               height:'155px',
               scrollable:false,
               style:'background-color:#f6f6f6;border-top:none;padding:0;',
               id:'supplyImg',
               items: [
                   {
                       html:'<span style="color:#000;">拍照</span>',
                       style:'height:50px;border:none;border-radius:0;margin-bottom:0;border-bottom:1px solid #f1f1f1;',
                       listeners:{
                           tap:function(){
                               supUploadThis.capturePhoto();
                           }
                       }
                   },
                   {
                       html:'<span style="color:#000;">相册中获取</span>',
                       style:'height:50px;border:none;border-radius:0;border-bottom:1px solid #f1f1f1;',
                       listeners:{
                           tap:function(){
                               supUploadThis.loadImageLocal();
                           }
                       }
                   },
                   {
                       html:'<span style="color:#000;">取消</span>',
                       style:'height:50px;border:none;border-radius:0;margin-top:5px;',
                       listeners:{
                           tap:function(){
                               Ext.ComponentQuery.query('#supplyImg')[0].hide();
                           }
                       }
                   }
               ]
           },
           {
               xtype:'formpanel',
               flex:9,
               scrollable:{direction: 'vertical',indicators: false},
               items:[
                   {
                       html:'<div style="background:#fff;" class="customer-content">' +
                       '<div class="title">发货照片</div>'+
                       '<div class="img-content" id="seeDeliveImg">' +
                       /*'<div class="img-item">' +
                        '<img class="active" data-preview-src="" data-preview-group="4" src="https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1512790266&di=b38a19fef7f43c57a92a9519de4ee748&src=http://img.wzrb.com.cn/articlefile/20150827/20150827542846.jpg"/>' +
                        '</div>'+
                        '<div class="img-item">' +
                        '<img class="active" data-preview-src="" data-preview-group="4" src="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1512800387466&di=f01f7eaf382d1d79eb2296a66c25a4cc&imgtype=0&src=http%3A%2F%2Festimation.cang.com%2F201102%2F201102191505326802.jpg"/>' +
                        '</div>'+
                        '<div class="img-item">' +
                        '<img class="active" data-preview-src="" data-preview-group="4" src="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1512800387463&di=72238c4de533aabe21668e91e4026dbd&imgtype=0&src=http%3A%2F%2Fwww.btdongfeng.com%2FUploadImages%2F5548c548a5a84d95b7441597b02be74b%25B6%25F2%25B9%25CF%25B6%25E0%25B6%25FB%25C1%25F2%25BB%25AF%25B9%25DE%25B7%25A2%25BB%25F5-%25CF%25E4%25B9%25F12.jpg"/>' +
                        '</div>'+*/
                       '</div>'+
                       '</div>'
                   },
                   {
                       html:'<div style="background:#fff;" class="customer-content">' +
                       '<div class="title">合同照片</div>'+
                       '<div class="img-content" id="seeContractImg">' +
                       /*'<div class="img-item">' +
                        '<img class="active" data-preview-src="" data-preview-group="4" src="https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1512790266&di=b38a19fef7f43c57a92a9519de4ee748&src=http://img.wzrb.com.cn/articlefile/20150827/20150827542846.jpg"/>' +
                        '</div>'+
                        '<div class="img-item">' +
                        '<img class="active" data-preview-src="" data-preview-group="4" src="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1512800387466&di=f01f7eaf382d1d79eb2296a66c25a4cc&imgtype=0&src=http%3A%2F%2Festimation.cang.com%2F201102%2F201102191505326802.jpg"/>' +
                        '</div>'+
                        '<div class="img-item">' +
                        '<img class="active" data-preview-src="" data-preview-group="4" src="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1512800387463&di=72238c4de533aabe21668e91e4026dbd&imgtype=0&src=http%3A%2F%2Fwww.btdongfeng.com%2FUploadImages%2F5548c548a5a84d95b7441597b02be74b%25B6%25F2%25B9%25CF%25B6%25E0%25B6%25FB%25C1%25F2%25BB%25AF%25B9%25DE%25B7%25A2%25BB%25F5-%25CF%25E4%25B9%25F12.jpg"/>' +
                        '</div>'+*/
                       '</div>'+
                       '</div>'
                   },
                   {
                       html:'<div style="height:10px;bakcground:#f6f6f6;"></div>'
                   },
                   {
                       html:'<div style="background:#fff;" class="customer-content">' +
                       '<div class="title">补充发货照片</div>'+
                       '<div class="img-content" id="supDeliverDiv">' +
                       '<div id="supDeliverImg" onclick="supUploadThis.acSheetShow(1)" class="img-item">' +
                       '<img src="resources/images/imgAdd.png"/>' +
                       '</div>'+
                       '</div>'+
                       '</div>'
                   },
                   {
                       html:'<div style="background:#fff;" class="customer-content">' +
                       '<div class="title">补充合同照片</div>'+
                       '<div class="img-content" id="supContractDiv">' +
                       '<div id="supContractImg" onclick="supUploadThis.acSheetShow(2)" class="img-item">' +
                       '<img src="resources/images/imgAdd.png"/>' +
                       '</div>'+
                       '</div>'+
                       '</div>'
                   },
                   {
                       xtype:'hiddenfield',
                       id:'supImgType'
                   },
                   {
                       xtype:'button',
                       html:'<span style="color:#1a1a1d;">确 定</span>',
                       style:'width:70%;margin:16px auto;border:none;border-radius:50px;background-color:#FEE300;height:40px;box-shadow: 0px 3px 3px rgba(248,214,201,1);',
                       listeners:{
                           tap:function () {
                               supUploadThis.updateSupImg();
                           }
                       }
                   }
               ]
           },
       ],
       listeners:{
           show:function () {
               mui.previewImage();
               supUploadThis.getDeliverImg();
           }
       }
   },
    initialize:function() {
        supUploadThis = this;
        document.addEventListener("deviceready", supUploadThis.onDeviceReady, false);
    },
    getDeliverImg:function () {
        var orderId = Ext.getCmp('order_id').getValue();
        var me = this;
        whir.loading.add("", 1);
        Comm.ajaxPost(
            '/image/getImageData',
            {orderId:orderId,type:'fhzp'},
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                console.log(msg);
                if(msg.retCode == "SUCCESS"){
                    var data = msg.retData;
                    var fhzpList = data.fhzpList,
                        htzpList = data.htzpList;
                    me.assignImg(fhzpList,htzpList);
                }
            }
        )
    },
    assignImg:function (fhzpList,htzpList) {
        var seeDeliveImg = document.getElementById('seeDeliveImg'),
            seeContractImg = document.getElementById('seeContractImg');
        seeDeliveImg.innerHTML = "";
        seeContractImg.innerHTML = "";
        fhzpList.forEach(function (item) {
            seeDeliveImg.innerHTML += '<div class="img-item">' +
                '<img class="active" data-preview-src="" data-preview-group="5" src="'+item.imgUrl+'"/>' +
                '</div>'
        });
        htzpList.forEach(function (item) {
            seeContractImg.innerHTML += '<div class="img-item">' +
                '<img class="active" data-preview-src="" data-preview-group="5" src="'+item.imgUrl+'"/>' +
                '</div>'
        });
    },
    updateSupImg:function () {
        var me = this;
        var seeDeliveImg = document.getElementById('seeDeliveImg'),
            seeContractImg = document.getElementById('seeContractImg'),
            supDeliverDiv = document.getElementById('supDeliverDiv'),
            supContractDiv = document.getElementById('supContractDiv');
        var seeDeliveChild = seeDeliveImg.children,
            seeContractChild = seeContractImg.children,
            supDeliverChild = supDeliverDiv.children,
            supContractChild = supContractDiv.children;
        var seeDeliveUrl = this._getSrcJson(seeDeliveChild),
            seeContractUrl = this._getSrcJson(seeContractChild),
            supDeliverUrl = this._getSrcJson(supDeliverChild),
            supContractUrl = this._getSrcJson(supContractChild);
        var firstUrl = seeDeliveUrl+supDeliverUrl,
            secondUrl = seeContractUrl + supContractUrl;
        var orderId = Ext.getCmp('order_id').getValue();
        whir.loading.add("", 1);
        Comm.ajaxPost(
            '/image/addCustomerImage',
            {orderId:orderId,urlType:'fhtp',firstUrl:firstUrl,secondUrl:secondUrl},
            function (response) {
                var msg = eval('('+response.responseText+')');
                console.log(msg);
                if(msg.retCode == "SUCCESS"){
                    Comm.ajaxPost(
                        '/employeeOrder/updateOrderStateBeforeSubmit',
                        {orderId:orderId,state:'19'},
                        function (response) {
                            whir.loading.remove();
                            var msg = eval('('+response.responseText+')');
                            console.log(msg);
                            if(msg.retCode == "SUCCESS"){
                                hidePage('#supplyImgView');
                            }
                        }
                    )

                }
            }
        )
    },
    _getSrcJson:function (children) {
        if(!children){
            return
        }
        var str = '';
        for(var i=0;i<children.length;i++){
            var src = children[i].getElementsByTagName('img')[0].getAttribute('src') + ',';
            if(src.indexOf('resources/images/imgAdd.png')<0){
                str += src;
            }
        }
        return str;
    },
    acSheetShow:function (index) {
        Ext.getCmp('supImgType').setValue(index);
        Ext.ComponentQuery.query('#supplyImg')[0].show();
    },
    _deleteImg:function (e) {
        whir.loading.add("", 1);
        var url = e.getAttribute('data-url');
        Comm.ajaxPost(
            '/order/deleteImage',
            {url:url},
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                console.log(msg);
                if(msg.retCode == "SUCCESS"){
                    var parent = e.parentNode.parentNode;
                    var child = e.parentNode;
                    parent.removeChild(child);
                }
            }
        );
    },
    //-----------------------------------
    //调取摄像头及相册获取方法
    onDeviceReady:function(){
        pictureSource = navigator.camera.PictureSourceType;
        destinationType = navigator.camera.DestinationType;
    },
    capturePhoto:function(){
        Ext.ComponentQuery.query('#supplyImg')[0].hide();//关闭模态框
        var me=this;
        navigator.camera.getPicture(me.onPhotoURISuccess, me.onFail, {
            quality : 90,
            saveToPhotoAlbum : true,
            targetWidth : 960,
            targetHeight : 720,
            encondingType : Camera.EncodingType.DATA_URL,
            destinationType : destinationType.FILE_URL //FILE_URI
        });
    },
    onPhotoURISuccess:function(imageURI){
        whir.loading.add("", 1);
        supUploadThis.uploadPhoto(imageURI);
    },
    uploadPhoto:function(imageURI){
        imageURI.indexOf('?')>0?imageURI = imageURI.substr(0,imageURI.indexOf('?')):imageURI=imageURI;
        var me=this;
        var appHost = Ext.ComponentQuery.query('#middleSaveview #appHost')[0].getValue();
        var options = new FileUploadOptions();
        options.fileKey = "fileAddPic";
        options.fileName = imageURI.substr(imageURI.lastIndexOf('/') + 1);
        options.mimeType = "image/jpeg";
        var uri = encodeURI(appHost+'/merchandise/upload');
        // var uri = encodeURI('http://192.168.102.29:8080/merchandise/upload');
        options.chunkedMode = false;
        var params = new Object();
        params.fileAddPic = imageURI;
        options.params = params;
        var ft = new FileTransfer();
        ft.upload(imageURI, uri, me.onFileUploadSuccess, me.onFileUploadFail, options);
    },
    onFileUploadSuccess:function(msg){
        var e =this;
        whir.loading.remove();
        var response = msg.response;
        response = JSON.parse(response);
        var data = response.retData;
        console.log(data);
        var fileList = data.fileList;
        var src = fileList[0].Name;
        var div = document.createElement('div');
        div.setAttribute('class','img-item');
        div.innerHTML = '<img class="active" data-preview-src="" data-preview-group="4" src="'+src+'"/><span data-url="'+src+'" class="icon-clear" onclick="supUploadThis._deleteImg(this)"></span>';
        var imgType = Ext.getCmp('supImgType').getValue()
        if(imgType == 1){
            var ImgWrapper = document.getElementById('supDeliverDiv');
            ImgWrapper.insertBefore(div,document.getElementById('supDeliverImg'));
        }else{
            var ImgWrapper = document.getElementById('supContractDiv');
            ImgWrapper.insertBefore(div,document.getElementById('supContractImg'));
        }
    },
    loadImageLocal:function(){
        var me=this;
        Ext.ComponentQuery.query('#supplyImg')[0].hide();//关闭模态框
        navigator.camera.getPicture(me.onLoadImageLocalSuccess, me.onLoadImageFail, {
            quality : 90,
            destinationType : Camera.DestinationType.FILE_URL,
            sourceType : 0,
            targetWidth : 960,
            targetHeight : 720
        });
    },
    onLoadImageLocalSuccess:function(imageURI){
        // 开始上传图片
        whir.loading.add("", 1);
        supUploadThis.uploadPhoto(imageURI);
    },
    onFileUploadFail:function(msg){
        console.log(msg);
        whir.loading.remove();
        layer.open({content:'上传失败！',skin:'msg',time:2});
    },
    //----------------------------
});
