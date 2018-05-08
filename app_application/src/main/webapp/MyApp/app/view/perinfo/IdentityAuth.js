Ext.define('MyApp.view.perinfo.IdentityAuth',{
    extend: 'Ext.Panel',
    xtype:'IdentityAuthenticationView',

    requires: [
        'Ext.Panel',
        'Ext.form.Panel',
        'Ext.Img',
        'Ext.field.Hidden',
        'Ext.field.DatePicker'
    ],

    config: {
        id:'IdentityAuthenticationView',
        zIndex:5,
        layout:{
            type:'vbox'
        },
        showAnimation : {
            type:'slide', direction : 'right'
        },
        items:[
            {
                xtype: 'toolbar',
                title:'<span>实名认证</span>',
                docked:'top',
                style:'background-color:#FEE300;border-radius:0;',
                items:[
                    {
                        html: '<span class="icon-back" style="font-size:26px;color:#000;"></span>',
                        style:'margin-left:10px;color:#8c8c8c;margin-top:5px;',
                        listeners:{
                            tap:function () {
                                hidePage('#IdentityAuthenticationView');
                            }
                        }
                    }
                ]
            },
            {
                html:'<p style="background: #F6F6F6;height:10px;"></p>',
            },
            {
                xtype: 'formpanel',
                flex:9,
                id:'idCardForm',
                scrollable:{direction: 'vertical',indicators: false},
                style:'background-color:#fff;margin-top:10px',
                items:[
                    {
                        html:'<p style="background:#fff;font-size: 16px;text-align: center;padding:17px 0 15px 0;color:rgb(51,51,51);font-weight:600;">扫描身份证，进行身份信息认证</p>'
                    },
                    {
                        xtype:'container',
                        id:'idcardCon',
                        layout:{
                            type:'hbox'
                        },
                        style:'width:100%;background:#fff;',
                        items:[
                            {
                                xtype:'img',
                                id:'idcardFront',
                                flex:1,
                                style:'position:relative;width:109px;height:89px;background-size:contain;margin:10px 0 10px 10px;',
                                src:'resources/images/idcardFront.png',
                                html:'<p style="position:absolute;width:100%;top:100px;text-align: center;font-size: 14px;">身份证正面</p>',
                                listeners: {
                                    tap:function () {
                                        var ua = navigator.userAgent.toLowerCase();
                                        //option 1、//0:人像面  1:国徽面
                                        //option 2、//0:竖屏  1:横屏
                                        var option=["0","1"];
                                        if (/iphone|ipad|ipod/.test(ua)) {
                                            device.getMegIDCardQuality(Ext.ComponentQuery.query('#IdentityAuthenticationView')[0].getMegIDCardQualitySuccess,Ext.ComponentQuery.query('#IdentityAuthenticationView')[0].getMegIDCardQualityError,option);
                                        }else if(/android/.test(ua)){
                                            device.idCardAuth(Ext.ComponentQuery.query('#IdentityAuthenticationView')[0].getMegIDCardQualitySuccess,Ext.ComponentQuery.query('#IdentityAuthenticationView')[0].getMegIDCardQualityError,option);
                                        }
                                    }
                                }
                            },
                            {
                                xtype:'hiddenfield',
                                id:'idcardFrontId',
                                name:'idcardFrontId'
                            },
                            {
                                xtype:'img',
                                id:'idcardBack',
                                flex:1,
                                html:'<p style="position:absolute;width:100%;top:100px;text-align: center;font-size: 14px;">身份证反面</p>',
                                style:'width:109px;height:89px;background-size:contain;margin:10px;',
                                src:'resources/images/idcardBack.png',
                                listeners:{
                                    tap:function () {
                                        var ua = navigator.userAgent.toLowerCase();
                                        //option 1、//0:人像面  1:国徽面
                                        //option 2、//0:竖屏  1:横屏
                                        var option=["1","1"];
                                        if (/iphone|ipad|ipod/.test(ua)) {
                                            device.getMegIDCardQuality(Ext.ComponentQuery.query('#IdentityAuthenticationView')[0].getMegIDCardQualitySuccess,Ext.ComponentQuery.query('#IdentityAuthenticationView')[0].getMegIDCardQualityError,option);
                                        }else if(/android/.test(ua)){
                                            device.idCardAuth(Ext.ComponentQuery.query('#IdentityAuthenticationView')[0].getMegIDCardQualitySuccess,Ext.ComponentQuery.query('#IdentityAuthenticationView')[0].getMegIDCardQualityError,option);
                                        }
                                    }
                                }
                            },
                            {
                                xtype:'hiddenfield',
                                id:'idcardBackId',
                                name:'idcardBackId'
                            }
                        ]
                    },
                    {
                        html:'<p style="position:relative;margin: 30px 0 0px 0;text-align: left;background: #F6F6F6;color:#686868;padding:11px 0 10px 37px;font-size: 14px;"><span class="icon-confirm" style="position: absolute;top:11px;left:12px;font-size: 19px;"></span>请确认以下身份证信息是否正确</p>',
                    },
                    {
                        xtype:'textfield',
                        id:'scanName',
                        name:'scanName',
                        clearIcon:false,
                        maxLength:20,
                        label:'<p style="position: relative;padding-left:40px;text-align:left;"><i class="icon-scanName" style="font-size: 18px;top:1px;"></i><span style="font-size: 14px;color: #686868;">真实姓名</span></p>',
                        style:'height:40px;text-align:right;border-bottom:1px dashed #f1f1f1;'
                    },
                    {
                        xtype:'textfield',
                        id:'scanSex',
                        name:'scanSex',
                        clearIcon:false,
                        maxLength:2,
                        readOnly:true,
                        label:'<p style="position: relative;padding-left:40px;text-align:left;"><i class="icon-scanSex" style="font-size: 20px;top:2px"></i><span style="font-size: 14px;color: #686868;">性别</span></p>',
                        style:'height:40px;text-align:right;border-bottom:1px dashed #f1f1f1;'
                    },
                    {
                        xtype:'datepickerfield',
                        id:'scanBirth',
                        name:'scanBirth',
                        clearIcon:false,
                        dateFormat:'Y年m月d日',
                        label:'<p style="position: relative;padding-left:40px;text-align:left;"><i class="icon-scanBirth" style="font-size: 17px;color:red;top:3px;"></i><span style="font-size: 14px;color: #686868;">出生日期</span></p>',
                        style:'height:40px;text-align:right;border-bottom:1px dashed #f1f1f1;',
                        picker:{
                            yearFrom:1940,
                            yearTo: new Date().getFullYear(),
                            dayText:'日',
                            monthText:'月',
                            yearText:'年',
                            slotOrder:['year','month','day'],
                            cancelButton:'取消',
                            doneButton:'确定'
                        }
                    },
                    {
                        xtype:'textfield',
                        id:'scanCard',
                        name:'scanCard',
                        clearIcon:false,
                        maxLength:18,
                        label:'<p style="position: relative;padding-left:40px;text-align:left;"><i class="icon-scanCard" style="font-size: 20px;left:12px;"></i><span style="font-size: 14px;color: #686868;">身份证号码</span></p>',
                        style:'height:38px;text-align:right;border-bottom:1px dashed #f1f1f1;'
                    },
                    {
                        xtype:'textareafield',
                        id:'scanAddress',
                        name:'scanAddress',
                        clearIcon:false,
                        maxLength:28,
                        label:'<p style="position: relative;padding-left:40px;text-align:left;"><i class="icon-scanAddress" style="font-size: 18px;top:3px;"></i><span style="font-size: 14px;color: #686868;">户籍地址</span></p>',
                        style:'text-align:right;border-bottom:1px dashed #f1f1f1;'
                    },
                    {
                        html: '<div id="identityDesc" style="text-align:center;margin-top:20px;"><span style="vertical-align:middle;" class="active icon-activeMark"></span><span style="padding-left:5px;color:#d81e06;font-size:13px;vertical-align:middle;">请确认身份认证信息正确无误</span></div>'
                    },
                    {
                        xtype:'button',
                        html:'<span style="color:#000;">进行验证</span>',
                        style:'background-color:#ffda44;border:none;border-radius:140px;width:80%;height:40px;margin:8px auto 30px;height:40px;box-shadow: 0px 3px 3px rgba(248,214,201,1);',
                        listeners:{
                            tap:function () {
                                Ext.ComponentQuery.query('#IdentityAuthenticationView')[0].saveIdentityAuth();
                                /*var formData = Ext.ComponentQuery.query('#idCardForm')[0].getValues();
                                Ext.ComponentQuery.query('#middleSaveview #customerName')[0].setValue(formData.scanName);
                                Ext.ComponentQuery.query('#middleSaveview #customerCardNum')[0].setValue(formData.scanName);
                                if(Ext.ComponentQuery.query('#personalInfoView')[0]){
                                    Ext.ComponentQuery.query('#personalInfoView')[0].show();
                                }else{
                                    var personalInfoView = Ext.create('MyApp.view.perinfo.PersonalInfo');
                                    Ext.Viewport.add(personalInfoView);
                                    personalInfoView.show();
                                }*/
                            }
                        }
                    },
                    {
                        xtype:'hiddenfield',
                        id:'isIdentityInit',
                        value:""
                    }
                ] 
            }
        ],
        listeners:{
            show:function () {
                Ext.ComponentQuery.query('#IdentityAuthenticationView')[0].getMuiPicker();
                //Ext.ComponentQuery.query('#IdentityAuthenticationView')[0].loadIdentityMsg();
            }
        }
    },
    initialize:function() {
       this.sexList = [{text:'男',value:0},{text:'女',value:1}];
    },
    loadIdentityMsg:function () {
        whir.loading.add("", 1);
        var idcardFront = Ext.ComponentQuery.query('#idcardFront')[0],
            idcardFrontId = Ext.ComponentQuery.query('#idcardFrontId')[0],
            idcardBack = Ext.ComponentQuery.query('#idcardBack')[0],
            idcardBackId = Ext.ComponentQuery.query('#idcardBackId')[0],
            scanName = Ext.ComponentQuery.query('#scanName')[0],
            scanSex = Ext.ComponentQuery.query('#scanSex')[0],
            scanBirth = Ext.ComponentQuery.query('#scanBirth')[0],
            scanCard = Ext.ComponentQuery.query('#scanCard')[0],
            scanAddress = Ext.ComponentQuery.query('#scanAddress')[0];
        Comm.ajaxPost(
            '/authorization/getIdentityInfoByCustId',
            '',
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                console.log(msg);
                var data = msg.retData;
                var personInfo = data[0];
                if(personInfo.Zcard_src_base64){
                    idcardFront.setSrc(personInfo.Zcard_src_base64);
                    idcardFrontId.setValue(personInfo.Zcard_src_base64);
                }else{
                    idcardFront.setSrc('resources/images/idcardFront.png');
                }
                if(personInfo.Fcard_src_base64){
                    idcardBack.setSrc(personInfo.Fcard_src_base64);
                    idcardBackId.setValue(personInfo.Fcard_src_base64);
                }else{
                    idcardBack.setSrc('resources/images/idcardBack.png');
                }
                scanName.setValue(personInfo.realname);
                scanSex.setValue(personInfo.sex_name);
                scanBirth.setValue(Ext.ComponentQuery.query('#IdentityAuthenticationView')[0].forMatDate(personInfo.birth));
                scanCard.setValue(personInfo.card_no);
                scanAddress.setValue(personInfo.card_register_address);
            }
        )
    },
    saveIdentityAuth:function () {
        var formData = Ext.ComponentQuery.query('#idCardForm')[0].getValues();
        var model = Ext.create('MyApp.model.perinfo.IdentityAuth',formData);
        var errors = model.validate();
        var frontSrc = Ext.ComponentQuery.query('#idcardFront')[0].getSrc();
        var backSrc = Ext.ComponentQuery.query('#idcardBack')[0].getSrc();
        var scanBirthDate = Ext.ComponentQuery.query('#scanBirth')[0].getValue();
        var order_id = Ext.ComponentQuery.query('#middleSaveview #order_id')[0].getValue();
        //将时间对象转化为字符串
        var timeDate = Ext.ComponentQuery.query('#IdentityAuthenticationView')[0].formatDateObj(scanBirthDate);
        var jsonData = {
            realname:formData.scanName,
            sex_name:formData.scanSex,
            card_register_address:formData.scanAddress,
            Zcard_src_base64:frontSrc,
            Fcard_src_base64:backSrc,
            card:formData.scanCard,
            birth:timeDate,
            identity_complete:'100',
            orderId:order_id
        };
        var data=JSON.stringify(jsonData);
        if(errors.isValid()){
            whir.loading.add("", 1);
            Comm.ajaxPost(
                '/employeeAuthorization/saveIdentityInfo',
                {data:data},
                function (response) {
                    whir.loading.remove();
                    var msg = eval('('+response.responseText+')');
                    if(msg.retCode == 'SUCCESS'){
                        //缓存客户姓名、身份证号
                        Ext.ComponentQuery.query('#middleSaveview #customerName')[0].setValue(formData.scanName);
                        Ext.ComponentQuery.query('#middleSaveview #customerCardNum')[0].setValue(formData.scanCard);
                        if(Ext.ComponentQuery.query('#custHomeView')[0]){
                            Ext.ComponentQuery.query('#custHomeView')[0].show();
                        }else{
                            var custHomeView = Ext.create('MyApp.view.perinfo.CustomerHome');
                            Ext.Viewport.add(custHomeView);
                            custHomeView.show();
                        }
                    }else{
                        if(msg.retData.threeCode == "refuse"){
                            layer.open({content: msg.retMsg,skin: 'msg',time: 2,end:function () {
                                hidePage('#IdentityAuthenticationView');
                                return;
                            }});
                        }
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
    getMegIDCardQualitySuccess:function (msg) {//身份证识别成功
        var html='';
        if(typeof(msg)=="string"){
            msg=eval('('+msg+')');
        }
        var data=msg.data;
        if(typeof(data)=="string"){
            data=eval('('+data+')');
        }
        if(data.xh_side=="front"){//人面
            Ext.ComponentQuery.query('#idcardFront')[0].setSrc("data:image/png;base64,"+msg.image);
            Ext.ComponentQuery.query('#idcardFrontId')[0].setValue("data:image/png;base64,"+msg.image);
            Ext.ComponentQuery.query('#scanName')[0].setValue(data.xh_name);//姓名
            Ext.ComponentQuery.query('#scanSex')[0].setValue(data.xh_gender);//性别
            Ext.ComponentQuery.query('#scanCard')[0].setValue(data.xh_id_card_number);//身份证卡号
            Ext.ComponentQuery.query('#scanBirth')[0].setValue(Ext.ComponentQuery.query('#IdentityAuthenticationView')[0].forMatDate(data.xh_birthday));//出生日期
            Ext.ComponentQuery.query('#scanAddress')[0].setValue(data.xh_address);//户籍地址
        }else{
            Ext.ComponentQuery.query('#idcardBack')[0].setSrc("data:image/png;base64,"+msg.image);
            Ext.ComponentQuery.query('#idcardBackId')[0].setValue("data:image/png;base64,"+msg.image);
        }
    },
    getMegIDCardQualityError:function (msg) {//身份证识别失败
        //alert(msg);
    },
    getMuiPicker:function () {
        var me = this;
        var scanSexEl = document.getElementById('scanSex');
        var sexPicker = new Picker({
            data: [me.sexList]
        });
        var scanSex = Ext.getCmp('scanSex');
        sexPicker.on('picker.select', function (selectedVal, selectedIndex) {
            scanSex.setValue(me.sexList[selectedIndex[0]].text);
        });
        scanSexEl.addEventListener('click', function () {
            sexPicker.show();
        });
    },
    //将日期转为标准时间对象
    forMatDate:function (date){
        if(!date){
            return;
        }
        var arr1 = date.split('年');
        var arr2 = arr1[1].split('月');
        var arr3 = arr2[1].split('日');
        return new Date(arr1[0],arr2[0]-1,arr3[0]);
    },
    //时间对象转化为字符串
    formatDateObj:function (time) {
        if(!time){
            return;
        }
        var year = time.getFullYear();
        var month = (time.getMonth()+1).toString();
        var day = +time.getDate().toString();
        month.length == 1 ? month = '0'+month: month=month;
        day.length == 1 ? day = '0'+day: day=day;
        return timeStr = year+'年'+month+'月'+day+'日';
    }
});