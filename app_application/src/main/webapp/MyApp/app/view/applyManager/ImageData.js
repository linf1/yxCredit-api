/**
 * 补充影像资料
 * Created by zl on 2017/12/6 0006.
 */
Ext.define('MyApp.view.applyManager.ImageData',{
   extend:'Ext.Panel',
   xtype:'uploadView',
   requires:[
       'Ext.Panel'
   ],
   config:{
       id:'uploadView',
       showAnimation:{type:'slide', direction:'left'},
       layout:{type:'vbox'},
       zIndex:5,
       items:[
           {
               xtype:'toolbar',
               title:'影像资料',
               docked : "top",
               style:'background-color:#FEE300;border-radius:0;',
               items:[
                   {
                       html: '<span class="icon-back" style="font-size:26px;color:#000;"></span>',
                       style:'margin-left:10px;color:#8c8c8c;margin-top:8px;',
                       listeners:{
                           tap:function () {
                               hidePage('#uploadView');
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
               id:'uploadImage',
               items: [
                   {
                       html:'<span style="color:#000;">拍照</span>',
                       style:'height:50px;border:none;border-radius:0;margin-bottom:0;border-bottom:1px solid #f1f1f1;',
                       listeners:{
                           tap:function(){
                               imgDataThis.capturePhoto();
                           }
                       }
                   },
                   {
                       html:'<span style="color:#000;">相册中获取</span>',
                       style:'height:50px;border:none;border-radius:0;border-bottom:1px solid #f1f1f1;',
                       listeners:{
                           tap:function(){
                               imgDataThis.loadImageLocal();
                           }
                       }
                   },
                   {
                       html:'<span style="color:#000;">取消</span>',
                       style:'height:50px;border:none;border-radius:0;margin-top:5px;',
                       listeners:{
                           tap:function(){
                               Ext.ComponentQuery.query('#uploadImage')[0].hide();
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
                       xtype:'hiddenfield',
                       id:'imgType'
                   },
                   {
                       html:'<div class="img-data-wrapper">' +
                       '<div class="customer-content">' +
                       '<div class="title">客户合影</div>'+
                       '<div id="uploadDiv" class="img-content">' +
                        /*'<div class="img-item">' +
                        '<img class="active" data-preview-src="" data-preview-group="1" src="https://p3.pstatp.com/list/472900029b8521f207eb"/>' +
                        '<span class="icon-clear" onclick="imgDataThis.deleteImg(this)"></span>'+
                        '</div>'+
                        '<div class="img-item">' +
                        '<img class="active" data-preview-src="" data-preview-group="1" src="https://p3.pstatp.com/list/47220004b61d499f4a93"/>' +
                        '<span class="icon-clear" onclick="imgDataThis.deleteImg(this)"></span>'+
                        '</div>'+
                        '<div class="img-item">' +
                        '<img class="active" data-preview-src="" data-preview-group="1" src="https://p3.pstatp.com/list/470a000170439c146806"/>' +
                        '<span class="icon-clear" onclick="imgDataThis.deleteImg(this)"></span>'+
                        '</div>'+*/
                       '<div id="uploadImg" onclick="imgDataThis.acSheetShow(1)" class="img-item">' +
                       '<img onclick="" src="resources/images/imgAdd.png"/>' +
                       '</div>'+
                       '</div>'+
                       '<div class="footer-desc">提示：请至少上传一张与客户的合影照片（您需要身着制服，带工牌，背景为门店全景）</div>'+
                       '</div>'+
                       '<div class="hold-card-content">' +
                       '<div class="title">客户手持身份证</div>' +
                       '<div id="holdUpDiv" class="img-content">' +
                       /*'<div class="img-item">' +
                        '<img data-preview-src="" data-preview-group="1" src="http://img5.imgtn.bdimg.com/it/u=201435541,4221933134&fm=27&gp=0.jpg"/>'+
                        '<span class="icon-clear" onclick="imgDataThis.deleteImg(this)"></span>'+
                        '</div>'+*/
                       '<div id="holdUpImg" onclick="imgDataThis.acSheetShow(2)" class="img-item">' +
                       '<img src="resources/images/imgAdd.png"/>' +
                       '</div>'+
                       '</div>'+
                       '</div>'+
                       '</div>'
                   },
                   {
                       xtype:'button',
                       html:'<span style="color:#1a1a1d;">提 交</span>',
                       style:'width:70%;margin:16px auto;border:none;border-radius:50px;background-color:#FEE300;height:40px;box-shadow: 0px 3px 3px rgba(248,214,201,1);',
                       listeners:{
                           tap:function () {
                               imgDataThis.upDateImgData();
                           }
                       }
                   }
               ]
           },

       ],
       listeners:{
           show:function () {
               mui.previewImage();
           }
       }
   },
    initialize:function() {
        imgDataThis = this;
        document.addEventListener("deviceready", imgDataThis.onDeviceReady, false);
    },
    deleteImg:function (e) {
        whir.loading.add("", 1);
        var url = e.parentNode.getElementsByTagName('img')[0].getAttribute('src');
        console.log(url);
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
    acSheetShow:function (index) {
        Ext.getCmp('imgType').setValue(index);
        Ext.ComponentQuery.query('#uploadImage')[0].show();
    },
    //保存照片方法
    upDateImgData:function () {
        var uploadDiv = document.getElementById('uploadDiv'),
            holdUpDiv = document.getElementById('holdUpDiv');
        var customerNum = this._getImgNum(uploadDiv),
            holdNum = this._getImgNum(holdUpDiv);
        if(customerNum<2){
            layer.open({content:'至少上传一张客户合影',skin:'msg',time:2});
            return
        }
        if(holdNum<2){
            layer.open({content:'至少上传一张手持身份证',skin:'msg',time:2});
            return
        }
        var children = uploadDiv.children,
            holdChild = holdUpDiv.children;
        // /image/addCustomerImage
        var customerImgUrl = this._getSrcJson(children),
            holdImgUrl = this._getSrcJson(holdChild);
        var orderId = Ext.getCmp('order_id').getValue();
        whir.loading.add("", 1)
        Comm.ajaxPost(
            '/image/addCustomerImage',
            {orderId:orderId,urlType:'yxzl',firstUrl:customerImgUrl,secondUrl:holdImgUrl},
            function (response) {
                var msg = eval('('+response.responseText+')');
                console.log(msg);
                if(msg.retCode == "SUCCESS"){
                    Comm.ajaxPost(
                        '/employeeOrder/updateOrderStateBeforeSubmit',
                        {orderId:orderId,state:'13'},
                        function (response) {
                            whir.loading.remove();
                            var msg = eval('('+response.responseText+')');
                            console.log(msg);
                            if(msg.retCode == "SUCCESS"){
                                if(Ext.getCmp('allOrderView')){
                                    Ext.getCmp('allOrderView').toGetApplyOrder();
                                }
                                if(Ext.getCmp('OrderDetailsView')){
                                    orderDetailThis._getOrderDetail();
                                    document.getElementById('imgBtn').style.display = 'none';
                                }
                                hidePage('#uploadView');
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
    //获取照片的张数
    _getImgNum:function (el) {
        var children = el.children;
        var len = children.length;
        return len;
    },
    //-----------------------------------
    //调取摄像头及相册获取方法
    onDeviceReady:function(){
        pictureSource = navigator.camera.PictureSourceType;
        destinationType = navigator.camera.DestinationType;
    },
    capturePhoto:function(){
        Ext.ComponentQuery.query('#uploadImage')[0].hide();//关闭模态框
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
        imgDataThis.uploadPhoto(imageURI);
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
        div.innerHTML = '<img class="active" data-preview-src="" data-preview-group="1" src="'+src+'"/><span class="icon-clear" onclick="imgDataThis.deleteImg(this)"></span>';
        var imgType = Ext.getCmp('imgType').getValue()
        if(imgType == 1){
            var ImgWrapper = document.getElementById('uploadDiv');
            ImgWrapper.insertBefore(div,document.getElementById('uploadImg'));
        }else{
            var ImgWrapper = document.getElementById('holdUpDiv');
            ImgWrapper.insertBefore(div,document.getElementById('holdUpImg'));
        }
    },
    loadImageLocal:function(){
        var me=this;
        Ext.ComponentQuery.query('#uploadImage')[0].hide();//关闭模态框
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
        imgDataThis.uploadPhoto(imageURI);
    },
    onFileUploadFail:function(msg){
        console.log(msg);
        whir.loading.remove();
        layer.open({content:'上传失败！',skin:'msg',time:2});
    },
    //----------------------------
});
