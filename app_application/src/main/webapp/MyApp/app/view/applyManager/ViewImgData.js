/**
 * 查看影像资料
 * Created by zl on 2017/12/7 0007.
 */
Ext.define('MyApp.view.applyManager.ViewImgData',{
   extend:'Ext.Panel',
   xtype:'seeImgView',
   requires:[
       'Ext.Panel'
   ],
   config:{
       id:'seeImgView',
       showAnimation:{type:'slide', direction:'left'},
       layout:{type:'vbox'},
       zIndex:5,
       items:[
           {
               xtype:'toolbar',
               title:'查看影像资料',
               docked : "top",
               style:'background-color:#FEE300;border-radius:0;',
               items:[
                   {
                       html: '<span class="icon-back" style="font-size:26px;color:#000;"></span>',
                       style:'margin-left:10px;color:#8c8c8c;margin-top:8px;',
                       listeners:{
                           tap:function () {
                               hidePage('#seeImgView');
                           }
                       }
                   },
                   {xtype:'spacer'},
                   {
                       html:'<span style="font-size:16px;color:#000;">编辑</span>',
                       listeners:{
                           tap:function () {
                               seeImgThis._editImg();
                               this.disable();
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
               id:'seeImage',
               items: [
                   {
                       html:'<span style="color:#000;">拍照</span>',
                       style:'height:50px;border:none;border-radius:0;margin-bottom:0;border-bottom:1px solid #f1f1f1;',
                       listeners:{
                           tap:function(){
                               seeImgThis.capturePhoto();
                           }
                       }
                   },
                   {
                       html:'<span style="color:#000;">相册中获取</span>',
                       style:'height:50px;border:none;border-radius:0;border-bottom:1px solid #f1f1f1;',
                       listeners:{
                           tap:function(){
                               seeImgThis.loadImageLocal();
                           }
                       }
                   },
                   {
                       html:'<span style="color:#000;">取消</span>',
                       style:'height:50px;border:none;border-radius:0;margin-top:5px;',
                       listeners:{
                           tap:function(){
                               Ext.ComponentQuery.query('#seeImage')[0].hide();
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
                       id:'seeImgType'
                   },
                   {
                       html:'<div style="padding:10px 0 0 15px;font-size:14px;background:#fff;">客户合影</div>'+
                       '<div id="khhyImgs" class="see-img-wrapper">' +
                       /*'<div class="see-img-item">' +
                        '<img data-preview-src="" data-preview-group="2" src="http://img2.imgtn.bdimg.com/it/u=4123260673,1155291077&fm=27&gp=0.jpg" alt="">'+
                        '</div>' +
                        '<div class="see-img-item">' +
                        '<img data-preview-src="" data-preview-group="2" src="http://img0.imgtn.bdimg.com/it/u=2512002061,23547935&fm=27&gp=0.jpg" alt="">'+
                        '</div>' +
                        '<div class="see-img-item">' +
                        '<img data-preview-src="" data-preview-group="2" src="http://img0.imgtn.bdimg.com/it/u=2010275678,105529222&fm=27&gp=0.jpg" alt="">'+
                        '</div>' +
                        '<div class="see-img-item">' +
                        '<img data-preview-src="" data-preview-group="2" src="http://img3.imgtn.bdimg.com/it/u=2170354497,2879859370&fm=27&gp=0.jpg" alt="">'+
                        '</div>' +*/
                       '</div>'+
                       '<div style="padding:10px 0 0 15px;font-size:14px;background:#fff;margin-top:10px;">客户手持身份证</div>'+
                       '<div id="sczpImgs" class="see-img-wrapper">' +
                       /*'<div class="see-img-item">' +
                        '<img data-preview-src="" data-preview-group="2" src="http://img0.imgtn.bdimg.com/it/u=3223509453,1874658961&fm=27&gp=0.jpg" alt="">'+
                        '</div>' +
                        '<div class="see-img-item">' +
                        '<img data-preview-src="" data-preview-group="2" src="http://img4.imgtn.bdimg.com/it/u=1609823102,1944522548&fm=27&gp=0.jpg" alt="">'+
                        '</div>' +
                        '<div class="see-img-item">' +
                        '<img data-preview-src="" data-preview-group="2" src="http://img2.imgtn.bdimg.com/it/u=1671139849,2426656201&fm=27&gp=0.jpg" alt="">'+
                        '</div>' +
                        '<div class="see-img-item">' +
                        '<img data-preview-src="" data-preview-group="2" src="http://img1.imgtn.bdimg.com/it/u=652910139,4029775868&fm=27&gp=0.jpg" alt="">'+
                        '</div>' +*/
                       '</div>'
                   },
                   {
                       xtype:'button',
                       id:'viewBtn',
                       html:'<span style="color:#1a1a1d;">确 定</span>',
                       style:'width:70%;margin:16px auto;border:none;border-radius:50px;background-color:#FEE300;height:40px;box-shadow: 0px 3px 3px rgba(248,214,201,1);',
                       listeners:{
                           tap:function () {
                               seeImgThis._updateImgData();
                           }
                       }
                   }
               ]
           }
       ],
       listeners:{
           show:function () {
               mui.previewImage();
               seeImgThis.getImgData();
           }
       }
   },
    initialize:function() {
        seeImgThis = this;
        Ext.getCmp('viewBtn').hide();
        document.addEventListener("deviceready", seeImgThis.onDeviceReady, false);
    },
    //获取已上传的图片
    getImgData:function () {
        var me = this;
        var orderId = Ext.getCmp('order_id').getValue(),
            type = 'yxzl';
        whir.loading.add("", 1);
        Comm.ajaxPost(
            '/image/getImageData',
            {orderId:orderId,type:type},
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                console.log(msg);
                if(msg.retCode == "SUCCESS"){
                    var data = msg.retData;
                    var khhyList = data.khhyList,//客户合影
                        sczpList = data.sczpList;//手持身份证
                    //展示照片
                    me._showImgData(khhyList,sczpList);
                }
            }
        );
    },
    _showImgData:function (khhyList,sczpList) {
        var khhyImgs = document.getElementById('khhyImgs'),
            sczpImgs = document.getElementById('sczpImgs');
        khhyImgs.innerHTML = "";
        sczpImgs.innerHTML = "";
        khhyList.forEach(function (item) {
            khhyImgs.innerHTML += '<div data-id="'+item.id+'" class="see-img-item">' +
                '<img data-preview-src="" data-preview-group="2" src="'+item.imgUrl+'" alt="">'+
                '</div>'
        });
        sczpList.forEach(function (item) {
            sczpImgs.innerHTML += '<div data-id="'+item.id+'" class="see-img-item">' +
                '<img data-preview-src="" data-preview-group="2" src="'+item.imgUrl+'"" alt="">'+
                '</div>'
        })
    },
    //编辑照片
    _editImg:function () {
        var e=this;
        var khhyImgs = document.getElementById('khhyImgs');
        var sczpImgs = document.getElementById('sczpImgs');
        //1,2用于区分客户合影还是手持身份证
        e._makeDom(khhyImgs,1);
        e._makeDom(sczpImgs,2);
        Ext.getCmp('viewBtn').show();
    },
    _deleteImg:function (e) {
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
    _updateImgData:function () {
        whir.loading.add("", 1);
        var khhyImgs = document.getElementById('khhyImgs'),
            sczpImgs = document.getElementById('sczpImgs');
        var children = khhyImgs.children,
            holdChild = sczpImgs.children;
        // /image/addCustomerImage
        var customerImgUrl = this._getSrcJson(children),
            holdImgUrl = this._getSrcJson(holdChild);
        var orderId = Ext.getCmp('order_id').getValue();
        Comm.ajaxPost(
            '/image/addCustomerImage',
            {orderId:orderId,urlType:'yxzl',firstUrl:customerImgUrl,secondUrl:holdImgUrl},
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                console.log(msg);
                if(msg.retCode == "SUCCESS"){
                    hidePage('#seeImgView');
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
    //操作Dom(因编辑引起)
    _makeDom:function (el,index) {
        var childDom = el.childNodes;
        var div = document.createElement("div");
        div.setAttribute('class','see-img-item');
        if(index == 1){
            div.innerHTML = '<img src="resources/images/imgAdd.png"/>';
            div.setAttribute('id','khhyAdd')
            div.setAttribute('onclick','seeImgThis.acSheetShow('+index+')');
        }else if(index == 2){
            div.innerHTML = '<img src="resources/images/imgAdd.png"/>';
            div.setAttribute('id','scAdd')
            div.setAttribute('onclick','seeImgThis.acSheetShow('+index+')');
        }
        for(var i=0,len=childDom.length;i<len;i++){
            var span=document.createElement("span");
            span.setAttribute('class','icon-clear');
            span.setAttribute('onclick','seeImgThis._deleteImg(this)')
            childDom[i].appendChild(span);
        }
        el.appendChild(div);
    },
    acSheetShow:function (index) {
        Ext.getCmp('seeImgType').setValue(index);
        Ext.ComponentQuery.query('#seeImage')[0].show();
    },
    //-----------------------------------
    //调取摄像头及相册获取方法
    onDeviceReady:function(){
        pictureSource = navigator.camera.PictureSourceType;
        destinationType = navigator.camera.DestinationType;
    },
    capturePhoto:function(){
        Ext.ComponentQuery.query('#seeImage')[0].hide();//关闭模态框
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
        seeImgThis.uploadPhoto(imageURI);
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
        div.setAttribute('class','see-img-item');
        div.innerHTML = '<img data-preview-src="" data-preview-group="2" src="'+src+'"/><span class="icon-clear" onclick="seeImgThis._deleteImg(this)"></span>';
        var seeImgType = Ext.getCmp('seeImgType').getValue()
        if(seeImgType == 1){
            var khhyImgs = document.getElementById('khhyImgs');
            khhyImgs.insertBefore(div,document.getElementById('khhyAdd'));
        }else{
            var sczpImgs = document.getElementById('sczpImgs');
            sczpImgs.insertBefore(div,document.getElementById('scAdd'));
        }
    },
    loadImageLocal:function(){
        var me=this;
        Ext.ComponentQuery.query('#seeImage')[0].hide();//关闭模态框
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
        seeImgThis.uploadPhoto(imageURI);
    },
    onFileUploadFail:function(msg){
        console.log(msg);
        whir.loading.remove();
        layer.open({content:'上传失败！',skin:'msg',time:2});
    },
    //----------------------------
});
