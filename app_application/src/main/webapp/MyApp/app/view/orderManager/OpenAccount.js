/**开户
 * Created by Administrator on 2017/10/30 0030.
 */
Ext.define('MyApp.view.orderManager.OpenAccount',{
    extend:'Ext.Panel',
    xtype:'openAccountView',

    requires:[
        'Ext.Panel',
        'Ext.Button',
        'Ext.form.Panel',
        'Ext.field.Password',
        'Ext.field.Hidden'
    ],

    config:{
        id:'openAccountView',
        zIndex:6,
        layout:{
            type:'vbox'
        },
        showAnimation : {
            type:'slide', direction : 'right'
        },
        items:[
            {
                xtype:'toolbar',
                title:'绑定银行卡',
                style:'background-color:#FEE300;border-radius:0;',
                items:[
                    {
                        html: '<span class="icon-back" style="font-size:26px;color:#000;"></span>',
                        style:'margin-left:10px;color:#8c8c8c;margin-top:8px;',
                        listeners:{
                            tap:function () {
                                hidePage('#openAccountView');
                            }
                        }
                    }
                ]
            },
            {
                xtype:'formpanel',
                id:'openAccountForm',
                flex:9,
                scrollable:{direction: 'vertical',indicators: false},
                items:[
                    {
                        xtype:'container',
                        id:'presonDescCon',
                        style:'margin-top:10px',
                        items:[
                            {
                                xtype: 'textfield',
                                name: 'accountType',
                                id:'accountType',
                                clearIcon:false,
                                label:'<span style="font-size: 14px;">账户类型</span>',
                                value:'宝付账户',
                                readOnly:true,
                                style:'border-bottom:1px solid #f6f6f6;'
                            },
                            {
                                xtype: 'textfield',
                                name: 'accountName',
                                id:'accountName',
                                clearIcon:false,
                                label:'<span style="font-size: 14px;">姓名</span>',
                                readOnly:true,
                                style:'border-bottom:1px solid #f6f6f6;'
                            },
                            {
                                xtype: 'textfield',
                                name: 'accountIdcard',
                                id:'accountIdcard',
                                clearIcon:false,
                                label:'<span style="font-size: 14px;">身份证号码</span>',
                                readOnly:true
                            }
                        ]
                    },
                    {
                        html:'<div style="background-color:#F6F6F6;height:40px;font-size: 14px;color:#767676;line-height: 40px;padding-left: 1em;">绑定银行卡</div>'
                    },
                    {
                        xtype: 'textfield',
                        name: 'accountBankName',
                        id:'accountBankName',
                        clearIcon:false,
                        label:'<span style="font-size: 14px;">开户银行</span>',
                        placeHolder:'请选择',
                        html:'<span class="icon-more" style="position:absolute;top:14px;right:15px;font-size: 16px;"></span>',
                        readOnly:true,
                        style:'border-bottom:1px solid #f6f6f6;'
                    },
                    {
                        xtype:'hiddenfield',
                        id:'bankIdHide',
                        name:'bankIdHide'
                    },
                    {
                        xtype:'hiddenfield',
                        id:'transId',//tansId
                        name:'transId'
                    },
                    {
                        xtype: 'textfield',
                        name: 'accountBankcard',
                        id:'accountBankcard',
                        clearIcon:false,
                        //value:'6228480661557110110',
                        label:'<span style="font-size: 14px;">银行卡号</span>',
                        placeHolder:'请输入',
                        maxLength:19,
                        style:'border-bottom:1px solid #f6f6f6;',
                    },
                    {
                        xtype: 'textfield',
                        name: 'accountTel',
                        id:'accountTel',
                        maxLength:11,
                        clearIcon:false,
                        html:'<span style="font-size: 14px;position:absolute;top:12px;left:1em;">银行预留手机号</span>',
                        placeHolder:'请输入',
                        style:'margin-top:10px;border-bottom:1px solid #f6f6f6;'
                    },
                    {
                        xtype:'container',
                        style:'position:relative;',
                        items:[
                            {
                                xtype: 'textfield',
                                name: 'openTelCode',
                                id:'openTelCode',
                                clearIcon:false,
                                label:'<span style="font-size: 14px;">手机验证码</span>',
                                placeHolder:'请输入',
                                maxLength:6,
                                style:'border-bottom:1px solid #f6f6f6;'
                            },
                            {
                                xtype:'button',
                                text:'<span style="color:#fff;font-size:13px;background-color:#FF6600;text-align:center;padding:2px 0;display:inline-block;width:100%;" id="codeText">获取验证码</span>',
                                style:'width:30%;position:absolute;top:7px;right:0;border-radius:5px;border:none;background-color:#fff;font-size:14px;color:#9c9c9c;',
                                listeners:{
                                    tap:function () {
                                        var e = this,
                                            codeText = document.getElementById('codeText');
                                        var reg=/^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1})|(17[0-9]{1}))+\d{8})$/;
                                        var bankReg = /^\d+$/;
                                        var smsCode = Ext.ComponentQuery.query('#openTelCode')[0].getValue(),
                                            tel = Ext.ComponentQuery.query('#accountTel')[0].getValue(),
                                            accountBankName = Ext.ComponentQuery.query('#accountBankName')[0].getValue(),
                                            cardNo = Ext.ComponentQuery.query('#accountBankcard')[0].getValue();//银行卡号
                                        var name = Ext.getCmp('accountName').getValue(),
                                            idNo = Ext.getCmp('accountIdcard').getValue(),
                                            bankCode = Ext.getCmp('bankIdHide').getValue();
                                        if(accountBankName == ""){
                                            layer.open({content:'开户银行不能为空',skin:'msg',time:2});
                                            return
                                        }
                                        if(cardNo == ""){
                                            layer.open({content:'银行卡号不能为空',skin:'msg',time:2});
                                            return
                                        }else if(!bankReg.test(cardNo)){
                                            layer.open({content:'银行卡号只能输入数字',skin:'msg',time:2});
                                            return
                                        }
                                        if(tel == ""){
                                            layer.open({content:'银行预留手机号不能为空',skin:'msg',time:2});
                                            return
                                        }else if(!reg.test(tel)){
                                            layer.open({content:'银行预留手机号格式错误',skin:'msg',time:2});
                                            return
                                        }
                                        var seconds = 120;//msg.msg.substr(7,3);
                                        var timer = setInterval(function () {
                                            seconds = seconds - 1;
                                            codeText.innerHTML = '验证码'+'('+seconds+')'+'s';
                                            e.disable();
                                            if(seconds == 0){
                                                clearInterval(timer);
                                                codeText.innerHTML = '重新获取';
                                                e.enable();
                                            }
                                        },1000);
                                        Ext.ComponentQuery.query('#openAccountView')[0].firstMethod(name,idNo,cardNo,accountBankName,bankCode,tel);
                                    }
                                }
                            }
                        ]
                    },
                    {
                        xtype:'button',
                        html:'<span style="color:#1a1a1d;">绑定银行卡</span>',
                        style:'border:none;margin:30px 30px;border-radius:50px;background-color:#ffda44;height:40px;box-shadow: 0px 3px 3px rgba(248,214,201,1);',
                        listeners:{
                            tap:function () {
                                Ext.ComponentQuery.query('#openAccountView')[0].cardConfirm()
                            }
                        }
                    },
                    //禁止mui重复创建picker
                    {
                        xtype:'hiddenfield',
                        id:'isOpenInit',
                        value:""
                    }
                ]
            }
        ],
        listeners:{
            show:function () {
                Ext.ComponentQuery.query('#openAccountView')[0].getOpenInfo();
            }
        }
    },
    //开户银行picker
    getPcPicker:function (bankNameList) {
        var isOpenInit=Ext.ComponentQuery.query('#isOpenInit')[0].getValue();
        if(!isOpenInit){
            var control = this;
            var bankList = [];
            bankNameList.forEach(function (item) {
                bankList.push({text:item.value,value:item.key});
            });
            var accountBankEl = document.getElementById('accountBankName');
            var bankPicker = new Picker({
                data: [bankList]
            });
            var accountBankName = Ext.getCmp('accountBankName'),
                bankIdHide = Ext.getCmp('bankIdHide');
            bankPicker.on('picker.select', function (selectedVal, selectedIndex) {
                //picker1El.innerText = data1[selectedIndex[0]].text + data1[selectedIndex[0]].value;
                accountBankName.setValue(bankList[selectedIndex[0]].text);
                bankIdHide.setValue(bankList[selectedIndex[0]].value);
            });
            accountBankEl.addEventListener('click', function () {
                bankPicker.show();
            });
            Ext.ComponentQuery.query('#isOpenInit')[0].setValue(1);
        }
    },
    getOpenInfo:function () {
        whir.loading.add("", 1);
        var openview = this;
        var orderId = Ext.getCmp('order_id').getValue();
       Comm.ajaxPost('/payTreasure/getOpenAccountInfo',{orderId:orderId},function (response) {
           whir.loading.remove();
           var msg = eval('('+response.responseText+')');
           console.log(msg);
           if(msg.retCode == "SUCCESS"){
               var accountName = Ext.ComponentQuery.query('#accountName')[0],
                   accountIdcard = Ext.ComponentQuery.query('#accountIdcard')[0];
               var data = msg.retData;
               var bankNameList = data.bankNameList;
               openview.getPcPicker(bankNameList);
               accountName.setValue(data.userName);
               accountIdcard.setValue(data.IdNo);
           }
       })
    },
    /****************宝付开户*******************/
    //String name, String idNo, String cardNo, String bankName,String bankCode,String tel
    firstMethod:function (name,idNo,cardNo,accountBankName,bankCode,tel) {
        whir.loading.add("", 1);
        var orderId = Ext.getCmp('order_id').getValue();
        Comm.ajaxPost(
            '/payTreasure/bindingCard',
            {name:name,idNo:idNo,cardNo:cardNo,bankName:accountBankName,bankCode:bankCode,tel:tel,orderId:orderId},
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                console.log(msg);
                var data = msg.retData;
                var transId = data.transId,
                    respCode = data.respCode;
                if(respCode == '0000' || respCode == 'BF00114'){
                    Ext.getCmp('transId').setValue(transId);
                    layer.open({content:'短信验证码已下发',skin:'msg',time:2});
                    return
                }else if(respCode == 'over'){
                    mui.alert(data.msg,'提示','确定',function () {
                        hidePage('#openAccountView');
                    },'div');
                }else{
                    mui.alert(data.msg,'提示','确定',null,'div');
                }
            }
        )
    },
    //短信验证码调用方法
    cardConfirm:function () {
        var reg=/^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1})|(17[0-9]{1}))+\d{8})$/;
        var smsCode = Ext.ComponentQuery.query('#openTelCode')[0].getValue(),
            tel = Ext.ComponentQuery.query('#accountTel')[0].getValue(),
            accountBankName = Ext.ComponentQuery.query('#accountBankName')[0].getValue(),
            name = Ext.getCmp('accountName').getValue(),
            idNo = Ext.getCmp('accountIdcard').getValue(),
            bankCode = Ext.getCmp('bankIdHide').getValue(),
            transId = Ext.getCmp('transId').getValue(),
            cardNo = Ext.ComponentQuery.query('#accountBankcard')[0].getValue();//银行卡号
        if(accountBankName == ""){
            layer.open({content:'开户银行不能为空',skin:'msg',time:2});
            return
        }
        if(cardNo == ""){
            layer.open({content:'银行卡号不能为空',skin:'msg',time:2});
            return
        }
        if(tel == ""){
            layer.open({content:'银行预留手机号不能为空',skin:'msg',time:2});
            return
        }else if(!reg.test(tel)){
            layer.open({content:'银行预留手机号格式错误',skin:'msg',time:2});
            return
        }
        if(smsCode == ""){
            layer.open({content:'短信验证码不能为空',skin:'msg',time:2});
            return
        }
        whir.loading.add("", 1);
        var orderId = Ext.getCmp('order_id').getValue();
        //String name, String idNo, String cardNo,String bankName,String bankCode,String tel,String smsCode,String transId
        Comm.ajaxPost(
            '/payTreasure/bindingCardConfirm',
            {
                name:name,
                idNo:idNo,
                cardNo:cardNo,
                bankName:accountBankName,
                bankCode:bankCode,
                tel:tel,
                smsCode:smsCode,
                transId:transId,
                orderId:orderId
            },
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                console.log(msg);
                var data = msg.retData;
                var respCode = data.respCode;
                if(respCode == '0000' || respCode =='BF00114'){
                    layer.open({content:'绑卡成功',skin:'msg',time:2,end:function () {
                        if(Ext.getCmp('OrderManaView')){
                            orderMaThis._getAllOrder();
                        }
                        if(Ext.getCmp('OrderDetailsView')){
                            document.getElementById('openAccountBtn').style.display = 'none';
                            orderDetailThis._getOrderDetail();
                        }
                        hidePage('#openAccountView');
                    }});
                    return
                }else {
                    mui.alert(data.msg,'','确定',null,'div');
                }
            }
        )
    },
    /*****************易宝开户****************/
   /* //首次调用的接口
    firstMethod:function (cardNo,tel) {
        whir.loading.add("", 1);
        Comm.ajaxPost(
            '/ybCard/bindingCard',
            {cardno:cardNo,tel:tel},
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                console.log(msg);
                var data = msg.retData;
                if(data.status=='FAIL' || data.status=='BIND_FAIL' || data.status=='TIME_OUT' || data.status=='0'){
                    mui.alert(data.msg,'','确定',null,'div');
                    return
                }else if(data.status == 'BIND_ERROR'){
                    Ext.ComponentQuery.query('#openAccountView')[0].bindError();
                    return
                }else if(data.status == 'TO_VALIDATE'){
                    layer.open({content:'短信验证码已下发,请输入',skin:'msg',time:2});
                }
            }
        )
    },
    //出现BIND_ERROR字段调用的方法
    bindError:function () {
        whir.loading.add("", 1);
        Comm.ajaxPost(
            '/ybCard/bindingCardResend',
            '',
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                console.log(msg);
                var data = msg.retData;
                if(data.status=='FAIL' || data.status=='TIME_OUT'){
                    mui.alert(data.msg,'','确定',null,'div');
                    return
                }else if(data.status=='TO_VALIDATE'){
                    layer.open({content:'短信验证码已下发,请输入',skin:'msg',time:2})
                }
            }
        )
    },
    //短信验证码调用方法
    cardConfirm:function () {
        var reg=/^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1})|(17[0-9]{1}))+\d{8})$/;
        var smsCode = Ext.ComponentQuery.query('#openTelCode')[0].getValue(),
            tel = Ext.ComponentQuery.query('#accountTel')[0].getValue(),
            accountBankName = Ext.ComponentQuery.query('#accountBankName')[0].getValue(),
            cardNo = Ext.ComponentQuery.query('#accountBankcard')[0].getValue();//银行卡号
        if(accountBankName == ""){
            layer.open({content:'开户银行不能为空',skin:'msg',time:2});
            return
        }
        if(cardNo == ""){
            layer.open({content:'银行卡号不能为空',skin:'msg',time:2});
            return
        }
        if(tel == ""){
            layer.open({content:'银行预留手机号不能为空',skin:'msg',time:2});
            return
        }else if(!reg.test(tel)){
            layer.open({content:'银行预留手机号格式错误',skin:'msg',time:2});
            return
        }
        whir.loading.add("", 1);
        Comm.ajaxPost(
            '/ybCard/bindingCardConfirm',
            {
                smsCode:smsCode,
                cardNo:cardNo,
                tel:tel,
                accountBankName:accountBankName
            },
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                console.log(msg);
                var data = msg.retData;
                if(data.status == 'FAIL'){
                    Ext.ComponentQuery.query('#openAccountView')[0].CardCheck(cardNo);
                    return
                }else if(data.status == 'BIND_ERROR' || data.status == 'TO_VALIDATE'){
                    mui.alert(data.msg,'','确定',null,'div');
                    // Ext.ComponentQuery.query('#openAccountView')[0].firstMethod();
                    return
                }else if(data.status == 'BIND_FAIL'){
                    mui.alert(data.msg,'','确定',null,'div');
                    return
                }else if(data.status == 'BIND_SUCCESS'){
                    layer.open({content:'绑卡成功',skin:'msg',time:2,end:function () {
                        if( Ext.ComponentQuery.query('#CenterMsgView')[0]){
                            Ext.ComponentQuery.query('#CenterMsgView')[0].loadMessage();
                        }
                        hidePage('#openAccountView');
                    }});
                }
            }
        )
    },
    //Cardcheck方法
    CardCheck:function (cardNo) {
        whir.loading.add("", 1);
        Comm.ajaxPost(
            '/ybCard/bindingCardCheck',
            {cardNo:cardNo},
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                console.log(msg);
                var data = msg.retData;
                if(msg.retCode == "SUCCESS"){
                    layer.open({content:'已开户',skin:'msg',time:2});
                }else{
                    layer.open({content:'未开户',skin:'msg',time:2});
                }
            }
        )
    }*/
});
