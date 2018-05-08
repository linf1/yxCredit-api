/**添加商品
 * Created by zl on 2017/11/22 0022.
 */
Ext.define('MyApp.view.proManage.Addgoods',{
    extend:'Ext.Panel',
    xtype:'addgoodsView',
    requires:[
        'Ext.Panel',
        'Ext.ActionSheet',
        'MyApp.view.proManage.Addnewgoods'
    ],
    config:{
        id:'addgoodsView',
        showAnimation:{type:'slide', direction:'left'},
        layout:{type:'vbox'},
        zIndex:5,
        items:[
            {
                xtype: 'toolbar',
                title:'添加商品',
                docked:'top',
                style:'background-color:#FEE300;border-radius:0;',
                items:[
                    {
                        html: '<span class="icon-back" style="font-size:26px;color:#000;"></span>',
                        style:'margin-left:10px;color:#8c8c8c;margin-top:8px;',
                        listeners:{
                            tap:function () {
                                hidePage('#addgoodsView');
                            }
                        }
                    },
                    {
                        xtype:'spacer'
                    },
                    {
                        html:'<span class="icon-add" style="color:#000;"></span>',
                        listeners:{
                            tap:function () {
                                Ext.Viewport.mask();
                                Ext.Viewport.setMasked({
                                    zIndex:999,
                                    html:'<div class="pop-picker-view"><div id="btn1" class="pop-item"><span class="icon-add-brand"></span>添加商品名称<span></span></div><div class="pop-item" id="btn2"><span class="icon-add-edition"></span>添加具体型号<span></span></div><div class="pop-item" id="btn3" style="border-bottom:none;"><span class="icon-add-model"></span>添加版本<span></span></div><div class="flow-angel"></div></div>',
                                    listeners:{
                                        tap:function () {
                                            Ext.Viewport.unmask();
                                            var btn1 = document.getElementById('btn1'),
                                                btn2 = document.getElementById('btn2'),
                                                btn3 = document.getElementById('btn3');
                                            btn1.removeEventListener('tap',function () {
                                                addGoods.addNewGoods(1)
                                            })
                                            btn2.removeEventListener('tap',function () {
                                                addGoods.addNewGoods(2)
                                            })
                                            btn3.removeEventListener('tap',function () {
                                                addGoods.addNewGoods(3)
                                            })
                                        }
                                    }
                                });
                                setTimeout(function () {
                                    var btn1 = document.getElementById('btn1'),
                                        btn2 = document.getElementById('btn2'),
                                        btn3 = document.getElementById('btn3');
                                    btn1.addEventListener('tap',function () {
                                        addGoods.addNewGoods(1)
                                    })
                                    btn2.addEventListener('tap',function () {
                                        addGoods.addNewGoods(2)
                                    })
                                    btn3.addEventListener('tap',function () {
                                        addGoods.addNewGoods(3)
                                    })
                                },20)
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
                id:'addGoodsPhoto',
                items: [
                    {
                        html:'<span style="color:#000;">拍照</span>',
                        style:'height:50px;border:none;border-radius:0;margin-bottom:0;border-bottom:1px solid #f1f1f1;',
                        listeners:{
                            tap:function(){
                                addGoods.capturePhoto();
                            }
                        }
                    },
                    {
                        html:'<span style="color:#000;">相册中获取</span>',
                        style:'height:50px;border:none;border-radius:0;border-bottom:1px solid #f1f1f1;',
                        listeners:{
                            tap:function(){
                                addGoods.loadImageLocal();
                            }
                        }
                    },
                    {
                        html:'<span style="color:#000;">取消</span>',
                        style:'height:50px;border:none;border-radius:0;margin-top:5px;',
                        listeners:{
                            tap:function(){
                                Ext.ComponentQuery.query('#addGoodsPhoto')[0].hide();
                            }
                        }
                    }
                ]
            },
            {
                xtype:'formpanel',
                id:'goodsForm',
                flex:9,
                scrollable:{direction: 'vertical',indicators: false},
                items:[
                    {
                        xtype: 'textfield',
                        name: 'goodsType',
                        id:'goodsType',
                        clearIcon:false,
                        label:'<span style="font-size: 14px;">商品类型</span>',
                        placeHolder:'请选择',
                        html:'<span class="icon-more" style="position:absolute;top:14px;right:15px;font-size: 16px;"></span>',
                        readOnly:true,
                        style:'border-bottom:1px solid #f6f6f6;margin-top:10px;',
                        listeners:{
                            change:function (me, newValue, oldValue, eOpts) {
                                var brandName = Ext.getCmp('brandName'),
                                    brandValue = Ext.getCmp('brandValue'),
                                    specificModel = Ext.getCmp('specificModel'),
                                    modelValue = Ext.getCmp('modelValue'),
                                    edition = Ext.getCmp('edition'),
                                    editionValue = Ext.getCmp('editionValue');
                                if(newValue != oldValue){
                                    brandName.setValue('');
                                    brandValue.setValue('');
                                    specificModel.setValue('');
                                    modelValue.setValue('');
                                    edition.setValue('');
                                    editionValue.setValue('');
                                }
                            }
                        }
                    },
                    {
                        xtype:"hiddenfield",
                        id:'typeValue',//商品类型隐藏域
                        name:'typeValue',
                        value:""
                    },
                    {
                        xtype: 'textfield',
                        name: 'brandName',
                        id:'brandName',
                        clearIcon:false,
                        label:'<span style="font-size: 14px;">品牌名称</span>',
                        placeHolder:'请选择',
                        html:'<span class="icon-more" style="position:absolute;top:14px;right:15px;font-size: 16px;"></span>',
                        readOnly:true,
                        style:'border-bottom:1px solid #f6f6f6;margin-top:5px;',
                        listeners:{
                            focus:function () {
                                addGoods._toGetBrand();
                            },
                            change:function (me, newValue, oldValue, eOpts) {
                                var specificModel = Ext.getCmp('specificModel'),
                                    modelValue = Ext.getCmp('modelValue'),
                                    edition = Ext.getCmp('edition'),
                                    editionValue = Ext.getCmp('editionValue');
                                if(newValue != oldValue){
                                    specificModel.setValue('');
                                    modelValue.setValue('');
                                    edition.setValue('');
                                    editionValue.setValue('');
                                }
                            }

                        }
                    },
                    {
                        xtype:"hiddenfield",
                        id:'brandValue',//品牌隐藏域
                        name:'brandValue',
                        value:""
                    },
                    {
                        xtype: 'textfield',
                        name: 'specificModel',
                        id:'specificModel',
                        clearIcon:false,
                        label:'<span style="font-size: 14px;">具体型号</span>',
                        placeHolder:'请选择',
                        html:'<span class="icon-more" style="position:absolute;top:14px;right:15px;font-size: 16px;"></span>',
                        readOnly:true,
                        style:'border-bottom:1px solid #f6f6f6;margin-top:5px;',
                        listeners:{
                            focus:function () {
                                addGoods._toGetModel()
                            },
                            change:function (me, newValue, oldValue, eOpts) {
                                var edition = Ext.getCmp('edition'),
                                    editionValue = Ext.getCmp('editionValue');
                                if(newValue != oldValue){
                                    edition.setValue('');
                                    editionValue.setValue('');
                                }
                            }

                        }

                    },
                    {
                        xtype:"hiddenfield",
                        id:'modelValue',//具体型号隐藏域
                        name:'modelValue',
                        value:""
                    },
                    {
                        xtype: 'textfield',
                        name: 'edition',
                        id:'edition',
                        clearIcon:false,
                        label:'<span style="font-size: 14px;">版本</span>',
                        placeHolder:'请选择',
                        html:'<span class="icon-more" style="position:absolute;top:14px;right:15px;font-size: 16px;"></span>',
                        readOnly:true,
                        style:'border-bottom:1px solid #f6f6f6;margin-top:5px;',
                        listeners:{
                            focus:function () {
                                addGoods._toGetEdition()
                            }
                        }
                    },
                    {
                        xtype:"hiddenfield",
                        id:'editionValue',//具体版本隐藏域
                        name:'editionValue',
                        value:""
                    },
                    {
                        xtype:'container',
                        style:'background-color:#fff;',
                        items:[
                            {
                                html:'<div class="goods-img-desc"><span class="large-title">商品图片</span><span class="low-title">(第一张为产品缩略图)</span></div>'
                            },
                            {
                                html:'<div id="addImgData" class="img-wrapper">' +
                                '<div class="img-item" id="goodsAddImg" onclick="addGoods.acSheetShow()"><img src="resources/images/imgAdd.png" width="100%"></div>' +
                                '</div>'
                            },
                            {
                                html:'<div class="tips"><p class="tip-title">至少包含:</p><p class="tip-desc">1.1张商品的照片</p><p class="tip-desc">2.一张印有商品型号的吊牌或保修卡照片</p></div>'
                            }
                        ]
                    },
                    {
                        xtype:'button',
                        html:'<span style="color:#1a1a1d;">添加商品</span>',
                        style:'width:258px;margin:24px auto;border:none;border-radius:50px;background-color:#FEE300;height:40px;box-shadow: 0px 3px 3px rgba(248,214,201,1);',
                        listeners:{
                            tap:function () {
                                addGoods.addGoods();
                            }
                        }
                    }
                ]
            }
        ],
        listeners:{
            show:function () {
                mui.previewImage();
                console.log(document.getElementById('btn1'));
                Ext.getCmp('addgoodsView')._toGetProType();
            }
        }
    },
    initialize:function() {
        addGoods = this;
        this.typeList = [];
        this.brandList = [];
        this.modelList = [];
        this.editionList = [];
        document.addEventListener("deviceready", addGoods.onDeviceReady, false);
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
    //得到品牌名称
    _toGetBrand:function () {
        var goodsType = Ext.getCmp('goodsType').getValue();
        if(goodsType == ""){
            layer.open({content:'请先选择商品类型',skin:'msg',time:2});
            return
        }
        var me = this;
        whir.loading.add("", 1);
        var parentId = Ext.getCmp('typeValue').getValue();
        Comm.ajaxPost(
            '/merchandise/getChildrenTypeList',
            {parentId:parentId},
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                console.log(msg);
                if(msg.retCode == "SUCCESS"){
                    var data = msg.retData;
                    var childrenTypeList = data.childrenTypeList;
                    me.brandList = me._changeList(childrenTypeList);
                    //console.log(me.brandList );
                    me.showBrandPicker()
                }
            }
        )
    },
    //得到具体型号
    _toGetModel:function () {
        var goodsType = Ext.getCmp('goodsType').getValue();
        var brandName = Ext.getCmp('brandName').getValue();
        if(goodsType == ""){
            layer.open({content:'请先选择商品类型',skin:'msg',time:2});
            return
        }
        if(brandName == ""){
            layer.open({content:'请先选择商品品牌',skin:'msg',time:2});
            return
        }
        var me = this;
        whir.loading.add("", 1);
        var parentId = Ext.getCmp('brandValue').getValue();
        Comm.ajaxPost(
            '/merchandise/getChildrenTypeList',
            {parentId:parentId},
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                console.log(msg);
                if(msg.retCode == "SUCCESS"){
                    var data = msg.retData;
                    var childrenTypeList = data.childrenTypeList;
                    me.modelList = me._changeList(childrenTypeList);
                    //console.log(me.brandList );
                    me.showModelPicker()
                }
            }
        )
    },
    //得到版本
    _toGetEdition:function () {
        var goodsType = Ext.getCmp('goodsType').getValue();
        var brandName = Ext.getCmp('brandName').getValue();
        var specificModel = Ext.getCmp('specificModel').getValue();
        if(goodsType == ""){
            layer.open({content:'请先选择商品类型',skin:'msg',time:2});
            return
        }
        if(brandName == ""){
            layer.open({content:'请先选择商品品牌',skin:'msg',time:2});
            return
        }
        if(specificModel == ""){
            layer.open({content:'请先选择具体型号',skin:'msg',time:2});
            return
        }
        var me = this;
        whir.loading.add("", 1);
        var parentId = Ext.getCmp('modelValue').getValue();
        Comm.ajaxPost(
            '/merchandise/getChildrenTypeList',
            {parentId:parentId},
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                console.log(msg);
                if(msg.retCode == "SUCCESS"){
                    var data = msg.retData;
                    var childrenTypeList = data.childrenTypeList;
                    me.editionList = me._changeList(childrenTypeList);
                    //console.log(me.brandList );
                    me.showEditionPicker()
                }
            }
        )
    },
    showProTypePicker:function () {
        var control = this;
        var typePickerEl = document.getElementById('goodsType');
        var typePicker = new Picker({
            data: [control.typeList]
        });
        var goodsType = Ext.getCmp('goodsType'),
            typeValue = Ext.getCmp('typeValue');
        typePicker.on('picker.select', function (selectedVal, selectedIndex) {
            //picker1El.innerText = data1[selectedIndex[0]].text + data1[selectedIndex[0]].value;
            goodsType.setValue(control.typeList[selectedIndex[0]].text);
            typeValue.setValue(control.typeList[selectedIndex[0]].value);
        });
        typePicker.on('picker.valuechange', function (selectedVal, selectedIndex) {
            console.log(selectedVal);
        });
        typePickerEl.addEventListener('click', function () {
            typePicker.show();
        });
    },
    showBrandPicker:function () {
        var e = this;
        if(e.brandPicker){
            e.brandPicker.pickerEl.parentNode.removeChild(e.brandPicker.pickerEl)
            e.brandPicker = null;
        }
        e.brandPicker = new Picker({
            data: [e.brandList]
        });
        var brandName = Ext.getCmp('brandName'),
            brandValue = Ext.getCmp('brandValue');
        var brandPickerEl = document.getElementById('brandName');
        e.brandPicker.on('picker.select', function (selectedVal, selectedIndex) {
            brandName.setValue(e.brandList[selectedIndex[0]].text);
            brandValue.setValue(e.brandList[selectedIndex[0]].value);
        });
        e.brandPicker.show();

    },
    showModelPicker:function () {
        var e = this;
        if(e.modelPicker){
            e.modelPicker.pickerEl.parentNode.removeChild(e.modelPicker.pickerEl)
            e.modelPicker = null;
        }
        e.modelPicker = new Picker({
            data: [e.modelList]
        });
        var specificModel = Ext.getCmp('specificModel'),
            modelValue = Ext.getCmp('modelValue');
        e.modelPicker.on('picker.select', function (selectedVal, selectedIndex) {
            specificModel.setValue(e.modelList[selectedIndex[0]].text);
            modelValue.setValue(e.modelList[selectedIndex[0]].value);
        });
        e.modelPicker.show();
    },
    showEditionPicker:function () {
        var e = this;
        if(e.editionPicker){
            e.editionPicker.pickerEl.parentNode.removeChild(e.editionPicker.pickerEl)
            e.editionPicker = null;
        }
        e.editionPicker = new Picker({
            data: [e.editionList]
        });
        var edition = Ext.getCmp('edition'),
            editionValue = Ext.getCmp('editionValue');
        e.editionPicker.on('picker.select', function (selectedVal, selectedIndex) {
            edition.setValue(e.editionList[selectedIndex[0]].text);
            editionValue.setValue(e.editionList[selectedIndex[0]].value);
        });
        e.editionPicker.show();
    },
    //添加商品
    addGoods:function () {
        var me = this;
        var formData = Ext.getCmp('goodsForm').getValues();
        var valid = me.validate(formData);
        if(!valid)return
        var imgDataDom = document.getElementById('addImgData');
        var children = imgDataDom.children;
        var imgUrl = me._getChildren(children);
        var merchantId = Ext.getCmp('merchanId').getValue();
        var data = {
            parent_id:formData.typeValue,
            brandName:formData.brandName,
            xinghaoName:formData.specificModel,
            versionName:formData.edition,
            imgUrl:imgUrl,
            merchantId:merchantId,
            versionId:formData.editionValue
        };
        data = JSON.stringify(data);
        console.log(data);
        whir.loading.add("", 1)
        Comm.ajaxPost(
            '/merchandise/addMerchandise',
            {data:data},
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                console.log(msg);
                if(msg.retCode == "SUCCESS"){
                    var data = msg.retData;
                    if(!data.flag){
                        layer.open({content:data.msg,skin:'msg',time:2});
                        return
                    }else{
                        Ext.getCmp('productView')._getMerchandiseList();
                        hidePage('#addgoodsView');
                    }
                }
            }
        )
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
    validate:function (formData) {
        var me = this;
        if(formData.goodsType == ""){
            layer.open({content:'请选择商品类型',skin:'msg',time:2});
            return false;
        }else if(formData.brandName == ""){
            layer.open({content:'请选择品牌名称',skin:'msg',time:2});
            return false;
        }else if(formData.specificModel == ""){
            layer.open({content:'请选择具体型号',skin:'msg',time:2});
            return false;
        }else if(formData.edition == ""){
            layer.open({content:'请选择版本',skin:'msg',time:2});
            return false;
        }
        var imgNum = me._getImgNum();
        if(imgNum<2){
            layer.open({content:'至少上传2张产品图片',skin:'msg',time:2});
            return false;
        }
        return true;
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
    //获取照片的张数
    _getImgNum:function () {
        var imgDataDom = document.getElementById('addImgData');
        var children = imgDataDom.children;
        var len = children.length;
        return len;
    },
    //改变list
    _changeList:function (list) {
        var newList = [];
        list.forEach(function (item) {
            newList.push({text:item.merchandise_name,value:item.id})
        });
        return newList;
    },
    acSheetShow:function () {
        Ext.ComponentQuery.query('#addGoodsPhoto')[0].show();
    },
    //-----------------------------------
    //调取摄像头及相册获取方法
    onDeviceReady:function(){
        pictureSource = navigator.camera.PictureSourceType;
        destinationType = navigator.camera.DestinationType;
    },
    capturePhoto:function(){
        Ext.ComponentQuery.query('#addGoodsPhoto')[0].hide();//关闭模态框
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
        addGoods.uploadPhoto(imageURI);
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
        div.innerHTML = '<img class="active" data-preview-src="" data-preview-group="9" src="'+src+'" width="100%"><span onclick="addGoods._deleteImg(this)" class="icon-clear"></span>';
        var ImgWrapper = document.getElementsByClassName('img-wrapper')[0]
        ImgWrapper.insertBefore(div,document.getElementById('goodsAddImg'));
    },
    loadImageLocal:function(){
        var me=this;
        Ext.ComponentQuery.query('#addGoodsPhoto')[0].hide();//关闭模态框
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
        addGoods.uploadPhoto(imageURI);
    },
    onFileUploadFail:function(msg){
        console.log(msg);
        whir.loading.remove();
        layer.open({content:'上传失败！',skin:'msg',time:2});
    },
    //----------------------------
    //添加新商品
    addNewGoods:function (index) {
        var me = this;
        switch (index)
        {
            case 1:
                if(Ext.getCmp('goodsType').getValue() == ""){
                    Ext.Viewport.unmask();
                    layer.open({content:'请先选择商品类型',skin:'msg',time:2});
                    return
                }
                this.toAddNewGoods();
                break;
            case 2:
                var goodsType = Ext.getCmp('goodsType').getValue(),
                    brandName = Ext.getCmp('brandName').getValue();
                if(goodsType == ""){
                    Ext.Viewport.unmask();
                    layer.open({content:'请先选择商品类型',skin:'msg',time:2});
                    return
                }else if(brandName == ""){
                    Ext.Viewport.unmask();
                    layer.open({content:'请先选择品牌名称',skin:'msg',time:2});
                    return
                }
                this.toAddNewGoods();
                break;
            case 3:
                var goodsType = Ext.getCmp('goodsType').getValue(),
                    brandName = Ext.getCmp('brandName').getValue(),
                    specificModel = Ext.getCmp('specificModel').getValue();
                if(goodsType == ""){
                    Ext.Viewport.unmask();
                    layer.open({content:'请先选择商品类型',skin:'msg',time:2});
                    return
                }else if(brandName == ""){
                    Ext.Viewport.unmask();
                    layer.open({content:'请先选择品牌名称',skin:'msg',time:2});
                    return
                }else if(specificModel == ""){
                    Ext.Viewport.unmask();
                    layer.open({content:'请先选择具体型号',skin:'msg',time:2});
                    return
                }
                this.toAddNewGoods();
                break;
        }
    },
    toAddNewGoods:function () {
        Ext.Viewport.unmask();
        var formData = Ext.getCmp('goodsForm').getValues();
        var typeName = Ext.getCmp('typeName'),
            typeId = Ext.getCmp('typeId'),
            brand_Name = Ext.getCmp('brand_Name'),
            model_Name = Ext.getCmp('model_Name'),
            edition_Name = Ext.getCmp('edition_Name');
        typeName.setValue(formData.goodsType);
        typeId.setValue(formData.typeValue);
        brand_Name.setValue(formData.brandName);
        model_Name.setValue(formData.specificModel);
        edition_Name.setValue(formData.edition);
        if(Ext.getCmp('newgoodsView')){
            Ext.getCmp('newgoodsView').show();
        }else{
            Ext.Viewport.add(Ext.create('MyApp.view.proManage.Addnewgoods')).show();
        }
    }
});
