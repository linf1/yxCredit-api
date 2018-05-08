/**
 * 基本信息
 * Created by zl on 2017/11/23 0023.
 */
Ext.define('MyApp.view.perinfo.BasicInfo',{
    extend:'Ext.Panel',
    xtype:'basicView',
    requires:[
        'Ext.Panel',
        'Ext.field.Hidden',
        'MyApp.model.perinfo.BasicInfo'
    ],
    config:{
        id:'basicView',
        zIndex:5,
        showAnimation:{type:'slide', direction:'left'},
        layout:{type:'vbox'},
        items:[
            {
                xtype:'formpanel',
                id:'basicForm',
                flex:9,
                scrollable:{direction: 'vertical',indicators: false},
                items:[
                    {
                        html:'<div class="link-man-content">身份类型</div>'
                    },
                    {
                        xtype:'container',
                        style:{
                            backgroundColor:'#fff',
                            padding:'20px 0'
                        },
                        items:[
                            {
                                html:'<div class="select-type">' +
                                '<div class="se-lf" onclick="basicView.tabJobType(this,1)"><div class="img"><img id="salaryImg" width="60" height="60" src="resources/images/salary_font.png"></div><div class="basi-title">工薪</div></div>' +
                                '<div class="se-rt" onclick="basicView.tabJobType(this,2)"><div class="img"><img id="personImg" width="60" height="60" src="resources/images/person_back.png"></div><div class="basi-title">个体</div></div>' +
                                '</div>'
                            },
                            {
                                xtype:"hiddenfield",
                                id:'idenType',//身份类型
                                name:'idenType',
                                value:"0"
                            }
                        ]
                    },
                    {
                        xtype:'textfield',
                        id:'liveAddress',
                        name:'liveAddress',
                        clearIcon:false,
                        readOnly:true,
                        html:'<span class="icon-more" style="position:absolute;top:14px;right:15px;font-size: 16px;"></span>',
                        label:'<span style="font-size: 14px;">居住地址</span>',
                        placeHolder:'省份/城市/地区',
                        style:'margin-top:10px;border-bottom:1px solid #f1f1f1;'
                    },
                    {
                        xtype:'hiddenfield',
                        id:'liveAddrId',
                        name:'liveAddrId'
                    },
                    {
                        xtype:'textfield',
                        id:'detailAddr',
                        name:'detailAddr',
                        clearIcon:false,
                        label:'<span style="font-size: 14px;">详细地址</span>',
                        placeHolder:'输入街道门牌号等信息',
                        style:'border-bottom:1px solid #f1f1f1;'
                    },
                    {
                        xtype:'textfield',
                        id:'companyName',
                        name:'companyName',
                        clearIcon:false,
                        maxLength:20,
                        label:'<span style="font-size: 14px;">单位名称</span>',
                        placeHolder:'输入单位名称',
                        style:'margin-top:10px;border-bottom:1px solid #f1f1f1;'
                    },
                    {
                        xtype:'textfield',
                        id:'linkNumber',
                        name:'linkNumber',
                        clearIcon:false,
                        maxLength:13,
                        label:'<span style="font-size: 14px;">联系电话</span>',
                        placeHolder:'输入联系电话',
                        style:'margin-top:10px;border-bottom:1px solid #f1f1f1;'
                    },
                    /*{
                        xtype:'container',
                        style:'position:relative;overflow:hidden;background:#fff;border-bottom:1px solid #f1f1f1;',
                        items:[
                            {
                                html:'<div style="padding-left:18px;height:44px;line-height:44px;font-size:14px;">联系电话</div>',
                                style:'float:left;width:30%;'
                            },
                            {
                                xtype:'textfield',
                                id:'companyCode',
                                name:'companyCode',
                                clearIcon:false,
                                maxLength:4,
                                placeHolder:'区号',
                                style:'float:left;width:40px;'
                            },
                            {
                                html:'<span>-</span>',
                                style:'float:left;height:44px;line-height:44px;'
                            },
                            {
                                xtype:'textfield',
                                id:'linkNumber',
                                name:'linkNumber',
                                clearIcon:false,
                                maxLength:8,
                                placeHolder:'输入号码',
                                style:'float:left;width:30%;'
                            }
                        ]
                    },*/
                    {
                        xtype:'textfield',
                        id:'positionName',
                        name:'positionName',
                        clearIcon:false,
                        maxLength:20,
                        label:'<span style="font-size: 14px;">职位名称</span>',
                        placeHolder:'输入职位名称',
                        style:'border-bottom:1px solid #f1f1f1;'
                    },
                    {
                        xtype:'textfield',
                        id:'companyAddr',
                        name:'companyAddr',
                        clearIcon:false,
                        readOnly:true,
                        html:'<span class="icon-more" style="position:absolute;top:14px;right:15px;font-size: 16px;"></span>',
                        label:'<span style="font-size: 14px;">单位地址</span>',
                        placeHolder:'省份/城市/地区',
                        style:'border-bottom:1px solid #f1f1f1;'
                    },
                    {
                        xtype:'hiddenfield',
                        id:'comAddrId',
                        name:'comAddrId'
                    },
                    {
                        xtype:'textfield',
                        id:'comdeAddr',
                        name:'comdeAddr',
                        clearIcon:false,
                        label:'<span style="font-size: 14px;">详细地址</span>',
                        placeHolder:'输入街道门牌号等信息',
                        style:'border-bottom:1px solid #f1f1f1;'
                    },
                    {
                        xtype:'button',
                        html:'<span style="color:#1a1a1d;">保 存</span>',
                        id:'saveBasicInfoBtn',
                        style:'width:258px;margin:24px auto;border:none;border-radius:50px;background-color:#FEE300;height:40px;box-shadow: 0px 3px 3px rgba(248,214,201,1);',
                        listeners:{
                            tap:function () {
                                Ext.getCmp('basicView').updateBasicInfo();
                            }
                        }
                    },
                    {
                        xtype:'hiddenfield',
                        id:'isInit'
                    }
                ]
            }
        ],
        listeners:{
            show:function () {
                Ext.getCmp('basicView').getBetterPicker();
                Ext.getCmp('basicView').getBasicInfo();
            }
        }
    },
    initialize:function() {
        basicView = this;
    },
    getBetterPicker:function () {
        /*
         var isInit = Ext.getCmp('isInit').getValue();
        if(!isInit){
             var nameEl1 = document.getElementById('liveAddress'),
             nameEl2 = document.getElementById('companyAddr');
             var name1 = 'liveAddress',
             name2 = 'companyAddr',
             id1 = 'liveAddrId',
             id2 = 'comAddrId';
            layerThreePicker(nameEl1,name1,id1);
            layerThreePicker(nameEl2,name2,id2);
            Ext.getCmp('isInit').setValue('1');
        }*/
        var initVlaue=Ext.ComponentQuery.query('#isInit')[0].getValue();
        if(!initVlaue){
            (function($, doc) {
                $.init();
                $.ready(function() {
                    //					//级联示例
                    var cityPicker3 = new $.PopPicker({
                        layer: 3
                    });
                    cityPicker3.setData(cityData3);
                    var showCityPickerButton = doc.getElementById('liveAddress');
                    var liveAddress = Ext.ComponentQuery.query('#liveAddress')[0];
                    var liveAddrId = Ext.ComponentQuery.query('#liveAddrId')[0];
                    showCityPickerButton.addEventListener('tap', function(event) {
                        cityPicker3.show(function(items) {
                            var itemText=(items[2] || {}).text;
                            if(!itemText){
                                itemText="";
                            }else {
                                itemText="/"+itemText;
                            }
                            var itemId=(items[2] || {}).value;
                            if(!itemText){
                                itemId="";
                            }else {
                                itemId="/"+itemId;
                            }
                            liveAddress.setValue((items[0] || {}).text + "/" + (items[1] || {}).text + itemText);
                            var addressId1 = (items[0] || {}).value + "/" + (items[1] || {}).value + itemId;
                            liveAddrId.setValue(addressId1);
                        });
                    }, false);
                    //----------------------------------------------------
                    var cityPicker4 = new $.PopPicker({
                        layer: 3
                    });
                    cityPicker4.setData(cityData3);
                    var showCityPickerButton1 = doc.getElementById('companyAddr');
                    var companyAddr = Ext.ComponentQuery.query('#companyAddr')[0];
                    var comAddrId = Ext.ComponentQuery.query('#comAddrId')[0];
                    showCityPickerButton1.addEventListener('tap', function(event) {
                        cityPicker4.show(function(items) {
                            var itemText=(items[2] || {}).text;
                            if(!itemText){
                                itemText="";
                            }else {
                                itemText="/"+itemText;
                            }
                            var itemId=(items[2] || {}).value;
                            if(!itemText){
                                itemId="";
                            }else {
                                itemId="/"+itemId;
                            }
                            companyAddr.setValue((items[0] || {}).text + "/" + (items[1] || {}).text + itemText);
                            var addressId1 = (items[0] || {}).value + "/" + (items[1] || {}).value + itemId;
                            comAddrId.setValue(addressId1);
                        });
                    }, false);
                });
            })(mui, document);
            Ext.ComponentQuery.query('#isInit')[0].setValue(1);
        }
    },
    getBasicInfo:function () {
        whir.loading.add("", 1);
        var me = this;
        var orderId = Ext.getCmp('order_id').getValue();
        Comm.ajaxPost(
            '/basic/getBasicInfo',
            {orderId:orderId},
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                console.log(msg);
                if(msg.retCode == "SUCCESS"){
                    var data = msg.retData;
                    var jobMap = data.jobMap,
                        liveMap = data.liveMap;
                    me.assignBasicInfo(jobMap,liveMap);
                }
            }
        )
    },
    assignBasicInfo:function (jobMap,liveMap) {
        var idenType = Ext.getCmp('idenType'),
            liveAddress = Ext.getCmp('liveAddress'),
            liveAddrId = Ext.getCmp('liveAddrId'),
            detailAddr = Ext.getCmp('detailAddr'),
            companyName = Ext.getCmp('companyName'),
            linkNumber = Ext.getCmp('linkNumber'),
            positionName = Ext.getCmp('positionName'),
            companyAddr = Ext.getCmp('companyAddr'),
            comAddrId = Ext.getCmp('comAddrId'),
            comdeAddr = Ext.getCmp('comdeAddr');
        var salaryImg = document.getElementById('salaryImg'),
            personImg = document.getElementById('personImg');
        if(jobMap){
            idenType.setValue(jobMap.jobTypeId);
            //控制职业类型激活状态
            if(jobMap.jobTypeId == '0'){
                salaryImg.setAttribute('src','resources/images/salary_font.png');
                personImg.setAttribute('src','resources/images/person_back.png');
            }else if(jobMap.jobTypeId == '1'){
                salaryImg.setAttribute('src','resources/images/salary_back.png');
                personImg.setAttribute('src','resources/images/person_font.png');
            }
            companyName.setValue(jobMap.companyName);
            linkNumber.setValue(jobMap.companyPhone);
            positionName.setValue(jobMap.posLevel);
            var comAddressName = '',
                comAddressId = "";
            comAddressName = jobMap.districtName?jobMap.provinceName+'/'+jobMap.cityName+'/'+jobMap.districtName:jobMap.provinceName+'/'+jobMap.cityName;
            comAddressId = jobMap.districtId?jobMap.provinceId+'/'+jobMap.cityId+'/'+jobMap.districtId:jobMap.provinceId+'/'+jobMap.cityId;
            companyAddr.setValue(comAddressName);
            comAddrId.setValue(comAddressId);
            comdeAddr.setValue(jobMap.detailsAddress);
        }
        if(liveMap){
            var addressName = "",
                addressId = '';
            addressName = liveMap.distric?liveMap.provinces+'/'+liveMap.city+'/'+liveMap.distric:liveMap.provinces+'/'+liveMap.city;
            addressId = liveMap.districId?liveMap.provincesId+'/'+liveMap.cityId+'/'+liveMap.districId:liveMap.provincesId+'/'+liveMap.cityId;
            liveAddress.setValue(addressName);
            liveAddrId.setValue(addressId);
            detailAddr.setValue(liveMap.addressDetail);
        }
    },
    updateBasicInfo:function () {
        var basicForm = Ext.getCmp('basicForm').getValues();
        var model = Ext.create('MyApp.model.perinfo.BasicInfo',basicForm);
        var orderId = Ext.getCmp('order_id').getValue();
        var errors = model.validate();
        var dataJson = {
            liveAddress:basicForm.liveAddress,
            liveAddressId:basicForm.liveAddrId,
            addressDetail:basicForm.detailAddr,
            companyName:basicForm.companyName,
            linkNumber:basicForm.linkNumber,
            detailAddress:basicForm.comdeAddr,
            companyAddress:basicForm.companyAddr,
            companyAddressId:basicForm.comAddrId,
            posLevel:basicForm.positionName,
            jobTypeId:basicForm.idenType,
            orderId:orderId
        }
        var data = JSON.stringify(dataJson);
        if(errors.isValid()){
            whir.loading.add("", 1);
            Comm.ajaxPost(
                '/basic/addBasicInfo',
                {data:data},
                function (response) {
                    whir.loading.remove();
                    var msg = eval('('+response.responseText+')');
                    console.log(msg);
                    if(msg.retCode == "SUCCESS"){
                        var basicBadge = document.getElementById('basic-badge');
                        basicBadge.style.display = 'none';
                        Ext.getCmp('custHomeView').setActiveItem(2);
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
    tabJobType:function (me,index) {
        var salaryImg = document.getElementById('salaryImg'),
            personImg = document.getElementById('personImg');
        var idenType = Ext.getCmp('idenType');
        if(index == 1){
            salaryImg.setAttribute('src','resources/images/salary_font.png');
            personImg.setAttribute('src','resources/images/person_back.png');
            idenType.setValue('0');
        }else{
            salaryImg.setAttribute('src','resources/images/salary_back.png');
            personImg.setAttribute('src','resources/images/person_font.png');
            idenType.setValue('1');
        }
    }
});
