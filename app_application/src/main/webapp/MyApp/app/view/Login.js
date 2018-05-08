/**登录
 * Created by zl on 2017/11/21 0021.
 */
Ext.define('MyApp.view.Login',{
    extend: 'Ext.Panel',
    xtype:'loginView',
    
    requires:[
        'Ext.Panel',
        'Ext.form.Panel',
        'Ext.field.Password',
        'Ext.Img',
        'Ext.field.Search',
        'MyApp.view.Reset',
        'MyApp.model.Login',
        'MyApp.view.MiddleSave'
    ],
    config:{
        id:'loginView',
        showAnimation:{type:'slide', direction:'left'},
        layout:{type:'vbox'},
        items:[
            {
                xtype:'container',
                src:'resources/images/logo_back.jpg',
                style:'width:100%;height:250px;background:url(resources/images/logo_back.jpg) no-repeat;background-size:cover;background-size:100% 100%;',
                items:[
                    {
                        html:'<img src="resources/images/logo.png" style="display:block;margin:48px auto 0;width:120px;"/>'
                    }
                ]
            },
            {
                xtype: 'formpanel',
                id: 'loginFormPanel',
                style:'margin:0 30px 0;',
                scrollable:{direction: 'vertical',indicators: false},
                flex:9,
                items:[
                    {
                        xtype: 'textfield',
                        id: 'txt_username',
                        name: 'txt_username',
                        maxLength:11,
                        //clearIcon:false,
                        placeHolder:'您在商户平台录入的手机号',
                        //value:'15155162511',
                        html:'<span class="icon-user"></span>',
                        style:'position:relative;background-color:transparent;border-bottom:1px solid #C3C2B2;margin-top:40px;',
                    },
                    {
                        xtype:'passwordfield',
                        id:'txt_password',
                        name:'txt_password',
                        maxLength:20,
                        clearIcon:false,
                        //value:'123456',
                        placeHolder:'您在商户平台录入的密码',
                        html: '<span class="icon-password" style="font-size:20px;"></span><span id="changeEye" class="icon-close-eye" onclick="viewLoginThis.toggleEye()" style="position: absolute;top:15px;right:8px;font-size: 20px;"></span>',
                        style: 'margin-top:15px;position:relative;background-color:transparent;border-bottom:1px solid #C3C2B2;'
                    },
                    {
                        xtype:'button',
                        html:'<span style="color:#1a1a1d;">登 录</span>',
                        id:'loginBtn',
                        style:'width:98%;margin-top:48px;margin-bottom:16px;border:none;border-radius:50px;background-color:#FEE300;height:40px;box-shadow: 0px 3px 3px rgba(248,214,201,1);',
                        listeners:{
                            tap:function () {
                                viewLoginThis.salesLogin();
                                //viewLoginThis.overlayShow();
                                //viewLoginThis.artTemplate();
                            }
                        }
                    },
                    {
                        html:'<div style="text-align:center;"><span onclick="viewLoginThis.toResetView()" class="changePass">修改密码</span></div>'
                    }
                ]
            }
        ]
    },
    initialize:function() {
        viewLoginThis = this;
        this.closeEye = true;
    },
    toggleEye:function () {
        var txt_password = Ext.ComponentQuery.query('#txt_password')[0];
        if(viewLoginThis.closeEye) {
            Ext.query('#changeEye')[0].setAttribute('class','icon-open-eye');
            txt_password.bodyElement.dom.firstElementChild.firstElementChild.setAttribute('type','text');
        }else {
            Ext.query('#changeEye')[0].setAttribute('class','icon-close-eye');
            txt_password.bodyElement.dom.firstElementChild.firstElementChild.setAttribute('type','password');
        }
        this.closeEye = !this.closeEye;
    },
    overlayShow:function () {
        var width=window.screen.width;
        this.overlay = Ext.Viewport.add({
            xtype: 'panel',
            left: (width-width*0.8)/2,
            top: '20%',
            modal: true,
            zIndex:7,
            hideOnMaskTap: true,
            hidden: true,
            width: "80%",
            height: '400px',
            styleHtmlContent: true,
            style:'border-radius:10px',
            scrollable:{
                //注意横向竖向模式的配置，不能配错
                direction: 'vertical',
                //隐藏滚动条样式
                indicators: false
            },
            items: [
                {
                    xtype:'container',
                    style:'background:#fff;padding:20px 0 0;',
                    docked:'top',
                    cls:'overCon',
                    items:[
                        {
                            xtype:'searchfield',
                            style:'border:none;width:80%;margin:0 auto;',
                            html:'<span style="position:absolute;top:10px;left:20px;font-size:20px;" class="icon-search"></span>',
                            placeHolder:'商户名称',
                            listeners:{
                                change:function (me, newValue, oldValue, eOpts) {
                                    viewLoginThis.searchRes(newValue);
                                 },
                                keyup:function (me, e, eOpts) {
                                    if(me._value == ""){
                                        var str = me._value;
                                        viewLoginThis.searchRes(str);
                                    }
                                }
                            }
                        }
                    ]
                },
                {
                    xtype:'panel',
                    items:[
                        {
                            html:'<div class="merchans">' +
                            
                            '</div>'
                        }
                    ]
                }
            ]
        });
        this.overlay.show({type: 'fadeIn'});
    },
    artTemplate:function (list) {
        var data = new Object();
        data.list = list;
        var html = template('test',data);
        document.getElementsByClassName('merchans')[0].innerHTML = html;
    },
    toResetView:function () {
        if(Ext.ComponentQuery.query('#resetView')[0]){
            Ext.ComponentQuery.query('#resetView')[0].show();
        }else{
            Ext.Viewport.add(Ext.create('MyApp.view.Reset')).show();
        }
    },
    //办单员登录
    salesLogin:function () {
        var me = this;
        var formData = Ext.getCmp('loginFormPanel').getValues();
        var Phone = formData.txt_username,
            password = formData.txt_password;
        var model = Ext.create('MyApp.model.Login',formData);
        var errors = model.validate();
        if(errors.isValid()){
            whir.loading.add("", 1)
            Comm.ajaxPost(
                '/employeeLogin/login',
                {phone:Phone,password:password},
                function (response) {
                    var msg = eval('('+response.responseText+')');
                    console.log(msg);
                    if(msg.retCode == "SUCCESS"){
                        var data = msg.retData;
                        var sex_name = data.sex_name,//性别
                            employee_num = data.employee_num,
                            idcard = data.idcard,//身份证
                            sex = data.sex,//性别
                            id = data.id;//办单员id
                        var appHost = data.appHost;
                        //登录成功建立中间缓存页
                        if(Ext.getCmp('middleSaveview')){
                            Ext.getCmp('middleSaveview').show()
                        }else{
                            Ext.Viewport.add(Ext.create('MyApp.view.MiddleSave')).show();
                        }
                        localStorage['userPhone'] = Phone;
                        Ext.getCmp('salesmanId').setValue(id);
                        Ext.getCmp('appHost').setValue(appHost);
                        me.getMerchanList(id)
                    }else{
                        whir.loading.remove();
                        layer.open({content:msg.retMsg,skin:'msg',time:2});
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
    //获取办单员绑定的商户
    getMerchanList:function (id) {
        var me = this;
        Comm.ajaxPost(
            '/merchant/getMerchantList',
            {salesmanId:id},
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                console.log(msg);
                if(msg.retCode == "SUCCESS"){
                    var merchanList = msg.retData;
                    me.overlayShow();
                    me.artTemplate(merchanList);
                }
            }
        )
    },
    //模糊查询
    searchRes:function (value) {
        var me = this;
        whir.loading.add("", 1)
        var salesmanId =  Ext.getCmp('salesmanId').getValue();
        Comm.ajaxPost(
            '/merchant/getMerchantInfo',
            {merchantName:value,salesmanId:salesmanId},
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                console.log(msg);
                if(msg.retCode == "SUCCESS"){
                    var merchanList = msg.retData;
                    me.artTemplate(merchanList);
                }
            }
        )
    },
    toHomeView:function (e) {
        Ext.getCmp('loginFormPanel').reset();
        var merchanId = e.getAttribute('data-id');
        Ext.getCmp('merchanId').setValue(merchanId);
        if(Ext.getCmp('loginView')){
            viewLoginThis.overlay.destroy();
        }
        if(Ext.getCmp('home')){
           var id = Ext.getCmp('home').getActiveItem()._itemId;
            if(id == "OrderManaView"){
                orderMaThis.overlay.destroy();
            }else if(id=="productView"){
                proThis.overlay.destroy();
            }else if(id == "allOrderView"){
                allOrderThis.overlay.destroy();
            }
        }
        //this.overlay.hide({type:'fadeOut'});
        if(Ext.getCmp('home')){
            Ext.getCmp('home').show();
            //继续执行获得商户列表方法
            Ext.getCmp('productView')._getMerchandiseList()

        }else{
            Ext.Viewport.add(Ext.create('MyApp.view.Home')).show()
        }
        //回到首页
        Ext.Viewport.setActiveItem(Ext.getCmp('home').setActiveItem(0));
    },
    signSuccess:function (msg) {
        alert(msg);
    },
    signError:function (msg) {
        alert(msg);
    },
    imgSuccess:function () {
        
    },
    imgError:function () {
        
    }
});
