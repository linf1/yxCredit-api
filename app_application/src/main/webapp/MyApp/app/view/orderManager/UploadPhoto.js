/**
 * 上传发货照片
 * Created by zl on 2017/12/9 0009.
 */
Ext.define('MyApp.view.orderManager.UploadPhoto',{
   extend:'Ext.Panel',
   xtype:'uploadGoodsView',
   requires:[
       'Ext.Panel'
   ],
   config:{
       id:'uploadGoodsView',
       showAnimation:{type:'slide', direction:'left'},
       layout:{type:'vbox'},
       zIndex:5,
       items:[
           {
               xtype:'toolbar',
               title:'上传发货照片',
               docked : "top",
               style:'background-color:#FEE300;border-radius:0;',
               items:[
                   {
                       html: '<span class="icon-back" style="font-size:26px;color:#000;"></span>',
                       style:'margin-left:10px;color:#8c8c8c;margin-top:8px;',
                       listeners:{
                           tap:function () {
                               hidePage('#uploadGoodsView');
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
               id:'deliverImg',
               items: [
                   {
                       html:'<span style="color:#000;">拍照</span>',
                       style:'height:50px;border:none;border-radius:0;margin-bottom:0;border-bottom:1px solid #f1f1f1;',
                       listeners:{
                           tap:function(){
                               uploadGoodThis.capturePhoto();
                           }
                       }
                   },
                   {
                       html:'<span style="color:#000;">相册中获取</span>',
                       style:'height:50px;border:none;border-radius:0;border-bottom:1px solid #f1f1f1;',
                       listeners:{
                           tap:function(){
                               uploadGoodThis.loadImageLocal();
                           }
                       }
                   },
                   {
                       html:'<span style="color:#000;">取消</span>',
                       style:'height:50px;border:none;border-radius:0;margin-top:5px;',
                       listeners:{
                           tap:function(){
                               Ext.ComponentQuery.query('#deliverImg')[0].hide();
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
                       html:'<div class="img-data-wrapper">' +
                       '<div class="customer-content">' +
                       '<div class="title">发货照片</div>'+
                       '<div class="img-content" id="deliverDiv">' +
                       /*'<div class="img-item">' +
                        '<img class="active" data-preview-src="" data-preview-group="4" src="https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1512790266&di=b38a19fef7f43c57a92a9519de4ee748&src=http://img.wzrb.com.cn/articlefile/20150827/20150827542846.jpg"/>' +
                        '<span class="icon-clear" onclick="uploadGoodThis._deleteImg(this)"></span>'+
                        '</div>'+
                        '<div class="img-item">' +
                        '<img class="active" data-preview-src="" data-preview-group="4" src="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1512800387466&di=f01f7eaf382d1d79eb2296a66c25a4cc&imgtype=0&src=http%3A%2F%2Festimation.cang.com%2F201102%2F201102191505326802.jpg"/>' +
                        '<span class="icon-clear" onclick="uploadGoodThis._deleteImg(this)"></span>'+
                        '</div>'+
                        '<div class="img-item">' +
                        '<img class="active" data-preview-src="" data-preview-group="4" src="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1512800387463&di=72238c4de533aabe21668e91e4026dbd&imgtype=0&src=http%3A%2F%2Fwww.btdongfeng.com%2FUploadImages%2F5548c548a5a84d95b7441597b02be74b%25B6%25F2%25B9%25CF%25B6%25E0%25B6%25FB%25C1%25F2%25BB%25AF%25B9%25DE%25B7%25A2%25BB%25F5-%25CF%25E4%25B9%25F12.jpg"/>' +
                        '<span class="icon-clear" onclick="uploadGoodThis._deleteImg(this)"></span>'+
                        '</div>'+
                        '<div class="img-item">' +
                        '<img class="active" data-preview-src="" data-preview-group="4" src="https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=544882801,871249347&fm=27&gp=0.jpg"/>' +
                        '<span class="icon-clear" onclick="uploadGoodThis._deleteImg(this)"></span>'+
                        '</div>'+*/
                       '<div id="deliverToImg" onclick="uploadGoodThis.acSheetShow(1)" class="img-item">' +
                       '<img src="resources/images/imgAdd.png"/>' +
                       '</div>'+
                       '</div>'+
                       '<div class="footer-desc">1.上传发货相关的照片</div>'+
                       '</div>'+
                       '<div class="hold-card-content">' +
                       '<div class="title">合同照片</div>' +
                       '<div class="img-content" id="contractDiv">' +
                       /*'<div class="img-item">' +
                        '<img class="active" data-preview-src="" data-preview-group="4" src="https://miaofuxianjindai-001.oss-cn-beijing.aliyuncs.com/fintecher_file/image/ef371d3d-987d-49c8-b6d6-24fb9845675b1.jpg"/>'+
                        '<span class="icon-clear" onclick="uploadGoodThis.deleteImg(this)"></span>'+
                        '</div>'+*/
                       '<div id="contractImg" onclick="uploadGoodThis.acSheetShow(2)" class="img-item">' +
                       '<img src="resources/images/imgAdd.png"/>' +
                       '</div>'+
                       '</div>'+
                       '<div class="footer-desc">1.合同文字需清晰可见，每个页面都需上传</div>'+
                       '</div>'+
                       '</div>'
                   },
                   {
                       xtype:'hiddenfield',
                       id:'imgGoodsType'
                   },
                   {
                       xtype:'button',
                       html:'<span style="color:#1a1a1d;">确 定</span>',
                       style:'width:70%;margin:16px auto;border:none;border-radius:50px;background-color:#FEE300;height:40px;box-shadow: 0px 3px 3px rgba(248,214,201,1);',
                       listeners:{
                           tap:function () {
                               uploadGoodThis._updateImg();
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
        uploadGoodThis = this;
        document.addEventListener("deviceready", uploadGoodThis.onDeviceReady, false);
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
    acSheetShow:function (index) {
        Ext.getCmp('imgGoodsType').setValue(index);
        Ext.ComponentQuery.query('#deliverImg')[0].show();
    },
    _updateImg:function () {
        var deliverDiv = document.getElementById('deliverDiv'),
            contractDiv = document.getElementById('contractDiv');
        var deNum = this._getImgNum(deliverDiv),
            conNum = this._getImgNum(contractDiv);
        if(deNum<1){
            layer.open({content:'至少上传1张发货照片',skin:'msg',time:2});
            return
        }
        if(conNum<3){
            layer.open({content:'至少上传2张合同照片',skin:'msg',time:2});
            return
        }
        var deChildren = deliverDiv.children,
            conChild = contractDiv.children;
        var deImgUrl = this._getSrcJson(deChildren),
            conImgUrl = this._getSrcJson(conChild);
        var orderId = Ext.getCmp('order_id').getValue();
        whir.loading.add("", 1);
        Comm.ajaxPost(
            '/image/addCustomerImage',
            {orderId:orderId,urlType:'fhtp',firstUrl:deImgUrl,secondUrl:conImgUrl},
            function (response) {
                var msg = eval('('+response.responseText+')');
                if(msg.retCode == "SUCCESS"){
                    Comm.ajaxPost(
                        '/employeeOrder/updateOrderStateBeforeSubmit',
                        {orderId:orderId,state:'19'},
                        function (response) {
                            whir.loading.remove();
                            var msg = eval('('+response.responseText+')');
                            console.log(msg);
                            if(msg.retCode == "SUCCESS"){
                                if(Ext.getCmp('OrderManaView')){
                                    orderMaThis._getAllOrder();
                                }
                                if(Ext.getCmp('OrderDetailsView')){
                                    orderDetailThis._getOrderDetail();
                                    document.getElementById('toUploadBtn').style.display = 'none';
                                }
                                hidePage('#uploadGoodsView');
                            }
                        }
                    )

                }
            }
        )
    },
    //获取照片的张数
    _getImgNum:function (el) {
        var children = el.children;
        var len = children.length;
        return len;
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
    //-----------------------------------
    //调取摄像头及相册获取方法
    onDeviceReady:function(){
        pictureSource = navigator.camera.PictureSourceType;
        destinationType = navigator.camera.DestinationType;
    },
    capturePhoto:function(){
        Ext.ComponentQuery.query('#deliverImg')[0].hide();//关闭模态框
        var me=this;
        navigator.camera.getPicture(me.onPhotoURISuccess, me.onFail, {
           quality : 90,
            saveToPhotoAlbum : true,
            targetWidth: 1080,
            targetHeight: 1920,
            encondingType : Camera.EncodingType.DATA_URL,
            destinationType : destinationType.FILE_URL //FILE_URI
        });
    },
    onPhotoURISuccess:function(imageURI){
        whir.loading.add("", 1);
        uploadGoodThis.uploadPhoto(imageURI);
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
        div.innerHTML = '<img class="active" data-preview-src="" data-preview-group="4" src="'+src+'"/><span data-url="'+src+'" class="icon-clear" onclick="uploadGoodThis._deleteImg(this)"></span>';
        var imgType = Ext.getCmp('imgGoodsType').getValue()
        if(imgType == 1){
            var ImgWrapper = document.getElementById('deliverDiv');
            ImgWrapper.insertBefore(div,document.getElementById('deliverToImg'));
        }else{
            var ImgWrapper = document.getElementById('contractDiv');
            ImgWrapper.insertBefore(div,document.getElementById('contractImg'));
        }
    },
    loadImageLocal:function(){
        var me=this;
        Ext.ComponentQuery.query('#deliverImg')[0].hide();//关闭模态框
        navigator.camera.getPicture(me.onLoadImageLocalSuccess, me.onLoadImageFail, {
            quality : 99,
            destinationType : Camera.DestinationType.FILE_URL,
            sourceType : 0,
            targetWidth : 4032,
            targetHeight : 3024
        });
    },
    onLoadImageLocalSuccess:function(imageURI){
        // 开始上传图片
        whir.loading.add("", 1);
        uploadGoodThis.uploadPhoto(imageURI);
    },
    onFileUploadFail:function(msg){
        console.log(msg);
        whir.loading.remove();
        layer.open({content:'上传失败！',skin:'msg',time:2});
    },
    //----------------------------
});