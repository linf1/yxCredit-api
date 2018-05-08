/**
 * 添加新商品
 * Created by zl on 2017/11/27 0027.
 */
Ext.define('MyApp.view.proManage.Addnewgoods',{
    extend:'Ext.Panel',
    xtype:'newgoodsView',
    requires:[
        'Ext.Panel',
        'MyApp.model.proManage.Addnewgoods'
    ],
    config:{
        id:'newgoodsView',
        showAnimation:{type:'slide', direction:'left'},
        layout:{type:'vbox'},
        zIndex:5,
        items:[
            {
                xtype:'toolbar',
                title:'添加商品',
                docked:'top',
                style:'background-color:#FEE300;border-radius:0;',
                items:[
                    {
                        html: '<span class="icon-back" style="font-size:26px;color:#000;"></span>',
                        style:'margin-left:10px;color:#8c8c8c;margin-top:8px;',
                        listeners:{
                            tap:function () {
                                hidePage('#newgoodsView');
                            }
                        }
                    },
                ]
            },
            {
                xtype:'actionsheet',
                hidden:true,
                docked:'bottom',
                height:'155px',
                scrollable:false,
                style:'background-color:#f6f6f6;border-top:none;padding:0;',
                id:'newGoodsPhoto',
                items: [
                    {
                        html:'<span style="color:#000;">拍照</span>',
                        style:'height:50px;border:none;border-radius:0;margin-bottom:0;border-bottom:1px solid #f1f1f1;',
                        listeners:{
                            tap:function(){
                                newAddGoods.capturePhoto();
                            }
                        }
                    },
                    {
                        html:'<span style="color:#000;">相册中获取</span>',
                        style:'height:50px;border:none;border-radius:0;border-bottom:1px solid #f1f1f1;',
                        listeners:{
                            tap:function(){
                                newAddGoods.loadImageLocal();
                            }
                        }
                    },
                    {
                        html:'<span style="color:#000;">取消</span>',
                        style:'height:50px;border:none;border-radius:0;margin-top:5px;',
                        listeners:{
                            tap:function(){
                                Ext.ComponentQuery.query('#newGoodsPhoto')[0].hide();
                            }
                        }
                    }
                ]
            },
            {
                xtype:'formpanel',
                id:'newGoodsForm',
                flex:9,
                scrollable:{direction: 'vertical',indicators: false},
                items:[
                    {
                        xtype: 'textfield',
                        name: 'newType',
                        id:'newType',
                        clearIcon:false,
                        label:'<span style="font-size: 14px;">商品类型</span>',
                        placeHolder:'请选择',
                        html:'<span class="icon-more" style="position:absolute;top:14px;right:15px;font-size: 16px;"></span>',
                        readOnly:true,
                        style:'border-bottom:1px solid #f6f6f6;margin-top:10px;'
                    },
                    {
                        xtype:"hiddenfield",
                        id:'newTypeValue',//商品类型隐藏域
                        name:'newTypeValue',
                        value:""
                    },
                    {
                        xtype: 'textfield',
                        name: 'newBrandName',
                        id:'newBrandName',
                        clearIcon:false,
                        label:'<span style="font-size: 14px;">品牌名称</span>',
                        placeHolder:'请输入品牌名称',
                        style:'border-bottom:1px solid #f6f6f6;margin-top:5px;'
                    },
                    {
                        xtype: 'textfield',
                        name: 'newModel',
                        id:'newModel',
                        clearIcon:false,
                        label:'<span style="font-size: 14px;">具体型号</span>',
                        placeHolder:'请输入具体型号',
                        style:'border-bottom:1px solid #f6f6f6;margin-top:5px;'
                    },
                    {
                        xtype: 'textfield',
                        name: 'newEdition',
                        id:'newEdition',
                        clearIcon:false,
                        label:'<span style="font-size: 14px;">版本</span>',
                        placeHolder:'请输入版本信息',
                        style:'border-bottom:1px solid #f6f6f6;margin-top:5px;'
                    },
                    {
                        xtype:'container',
                        style:'background-color:#fff;',
                        items:[
                            {
                                html:'<div class="goods-img-desc"><span class="large-title">商品图片</span><span class="low-title">(第一张为产品缩略图)</span></div>'
                            },
                            {
                                html:'<div id="newAddImgData" class="img-wrapper">' +
                                '<div id="newGoodsAddImg" class="img-item" onclick="newAddGoods.acSheetShow()"><img src="resources/images/imgAdd.png" width="100%"></div>' +
                                '</div>'
                            },
                            {
                                html:'<div class="tips"><p class="tip-title">至少包含:</p><p class="tip-desc">1.1张车的照片</p><p class="tip-desc">2.一张印有商品型号的吊牌或保修卡照片</p></div>'
                            }
                        ]
                    },
                    {
                        xtype:'button',
                        html:'<span style="color:#1a1a1d;">添加商品</span>',
                        style:'width:258px;margin:24px auto;border:none;border-radius:50px;background-color:#FEE300;height:40px;box-shadow: 0px 3px 3px rgba(248,214,201,1);',
                        listeners:{
                            tap:function () {
                                newAddGoods.addNewGoods();
                            }
                        }
                    }
                ]
            }
        ],
        listeners:{
            show:function () {
                if(Ext.getCmp('addgoodsView')){
                    Ext.getCmp('addgoodsView').destroy();
                }
                Ext.getCmp('newgoodsView').assignText();
                Ext.getCmp('newgoodsView')._toGetProType();
            }
        }
    },
    initialize:function () {
        newAddGoods = this;
        document.addEventListener("deviceready", addGoods.onDeviceReady, false);
    },
    assignText:function () {
        var typeName = Ext.getCmp('typeName').getValue(),
            typeId = Ext.getCmp('typeId').getValue(),
            brand_Name = Ext.getCmp('brand_Name').getValue(),
            model_Name = Ext.getCmp('model_Name').getValue(),
            edition_Name = Ext.getCmp('edition_Name').getValue();
        Ext.getCmp('newType').setValue(typeName);
        Ext.getCmp('newTypeValue').setValue(typeId);
        Ext.getCmp('newBrandName').setValue(brand_Name);
        Ext.getCmp('newModel').setValue(model_Name);
        Ext.getCmp('newEdition').setValue(edition_Name);
    },
    //得到商品类型
    _toGetProType:function () {
        var control = this;
        whir.loading.add("", 1)
        Comm.ajaxPost(
            '/merchandise/getMerchandisTypeList',
            '',
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                console.log(msg);
                if(msg.retCode == "SUCCESS"){
                    var data = msg.retData;
                    var merchandiseTypeList = data.merchandiseTypeList;
                    var _merchandiseTypeList = [];
                    //将后台传过来的商品类型转换为better-picker需要的list类型
                    merchandiseTypeList.forEach(function (item) {
                        _merchandiseTypeList.push({text:item.merchandise_name,value:item.id});
                    })
                    control.typeList = _merchandiseTypeList;
                    console.log(control.typeList);
                    control.showProTypePicker();
                }
            }
        )
    },
    showProTypePicker:function () {
        var control = this;
        var newTypePickerEl = document.getElementById('newType');
        var newTypePicker = new Picker({
            data: [control.typeList]
        });
        var newType = Ext.getCmp('newType'),
            newTypeValue = Ext.getCmp('newTypeValue');
        newTypePicker.on('picker.select', function (selectedVal, selectedIndex) {
            //picker1El.innerText = data1[selectedIndex[0]].text + data1[selectedIndex[0]].value;
            newType.setValue(control.typeList[selectedIndex[0]].text);
            newTypeValue.setValue(control.typeList[selectedIndex[0]].value);
        });
        newTypePickerEl.addEventListener('click', function () {
            newTypePicker.show();
        });
    },
    acSheetShow:function () {
        Ext.ComponentQuery.query('#newGoodsPhoto')[0].show();
    },
    addNewGoods:function () {
        var control = this;
        var formData = Ext.getCmp('newGoodsForm').getValues();
        var model = Ext.create('MyApp.model.proManage.Addnewgoods',formData);
        var errors = model.validate();
        if(errors.isValid()){
            var imgNum = control._getImgNum();
            if(imgNum<2){
                layer.open({content:'至少上传2张产品图片',skin:'msg',time:2});
                return;
            }
            whir.loading.add("", 1);
            var merchantId = Ext.getCmp('merchanId').getValue(),
                parent_id  = Ext.getCmp('newTypeValue').getValue(),
                brandName = Ext.getCmp('newBrandName').getValue(),
                xinghaoName = Ext.getCmp('newModel').getValue(),
                versionName = Ext.getCmp('newEdition').getValue();
            var imgDataDom = document.getElementById('newAddImgData');
            var children = imgDataDom.children;
            var imgUrl = control._getChildren(children);
            var data = {
                merchantId:merchantId,
                parent_id:parent_id,
                brandName:brandName,
                xinghaoName:xinghaoName,
                versionName:versionName,
                imgUrl:imgUrl
            };
            data = JSON.stringify(data);
            Comm.ajaxPost(
                '/merchandise/addNewMerchandise',
                {data:data},
                function (response) {
                    whir.loading.remove();
                    var msg = eval('('+response.responseText+')');
                    console.log(msg);
                    if(msg.retCode == "SUCCESS"){
                        Ext.getCmp('productView')._getMerchandiseList();
                        hidePage('#newgoodsView');
                    }
                }
            )
        }else{
            var message = '';
            Ext.each(errors.items[0],function (rec) {
                message += rec.getMessage()+'<br>';
            });
            layer.open({content: message,skin: 'msg',time: 2});
        }
    },
    _getChildren:function (children) {
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
    _getImgNum:function () {
        var imgDataDom = document.getElementById('newAddImgData');
        var children = imgDataDom.children;
        var len = children.length;
        return len;
    },
    //删除图片
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
    //--------------------
    onDeviceReady:function(){
        pictureSource = navigator.camera.PictureSourceType;
        destinationType = navigator.camera.DestinationType;
    },
    capturePhoto:function(){
        Ext.ComponentQuery.query('#newGoodsPhoto')[0].hide();//关闭模态框
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
        newAddGoods.uploadPhoto(imageURI);
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
        div.innerHTML = '<img class="active" data-preview-src="" data-preview-group="9" src="'+src+'" width="100%"><span onclick="newAddGoods._deleteImg(this)" class="icon-clear"></span>';
        var ImgWrapper = document.getElementById('newAddImgData');
        ImgWrapper.insertBefore(div,document.getElementById('newGoodsAddImg'));
    },
    loadImageLocal:function(){
        var me=this;
        Ext.ComponentQuery.query('#newGoodsPhoto')[0].hide();//关闭模态框
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
        newAddGoods.uploadPhoto(imageURI);
    },
    onFileUploadFail:function(msg){
        console.log(msg);
        whir.loading.remove();
        layer.open({content:'上传失败！',skin:'msg',time:2});
    },
});
