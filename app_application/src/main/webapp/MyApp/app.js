/*
    This file is generated and updated by Sencha Cmd. You can edit this file as
    needed for your application, but these edits will have to be merged by
    Sencha Cmd when it performs code generation tasks such as generating new
    models, controllers or views and when running "sencha app upgrade".

    Ideally changes to this file would be limited and most work would be done
    in other places (such as Controllers). If Sencha Cmd cannot merge your
    changes and its generated code, it will produce a "merge conflict" that you
    will need to resolve manually.
*/

Ext.application({
    name: 'MyApp',

    requires: [
        'Ext.MessageBox',
        'Ext.ux.FieldScroller'
    ],

    views: [
        'Login','Home','proManage.Addgoods'
    ],
    

    icon: {
        '57': 'resources/icons/Icon.png',
        '72': 'resources/icons/Icon~ipad.png',
        '114': 'resources/icons/Icon@2x.png',
        '144': 'resources/icons/Icon~ipad@2x.png'
    },

    isIconPrecomposed: true,

    startupImage: {
        '320x460': 'resources/startup/320x460.jpg',
        '640x920': 'resources/startup/640x920.png',
        '768x1004': 'resources/startup/768x1004.png',
        '748x1024': 'resources/startup/748x1024.png',
        '1536x2008': 'resources/startup/1536x2008.png',
        '1496x2048': 'resources/startup/1496x2048.png'
    },

    launch: function() {
        //fastclick库解决移动端延时300ms问题
        if('addEventListener' in document) {
            document.addEventListener('DOMContentLoaded', function(){
                FastClick.attach(document.body);
            },false);
        }
        FastClick.attach(document.body);
        document.addEventListener("backbutton", onBackKeyDown, false);
        function onBackKeyDown() {
            document.removeEventListener("backbutton", onBackKeyDown, false); // 注销返回
            document.addEventListener("backbutton", onDubbKeyDown, false);
            return
        }
        function onDubbKeyDown() {
            layer.open({ content: '再点击一次退出!',skin: 'msg',time: 2});
            document.removeEventListener("backbutton", onDubbKeyDown, false); // 注销返回
            document.addEventListener("backbutton", exitApp, false);//绑定退出事件
            // 3秒后重新注册
            var intervalID = window.setInterval(function() {
                window.clearInterval(intervalID);
                document.removeEventListener("backbutton", exitApp, false); // 注销返回键
                document.addEventListener("backbutton", onBackKeyDown, false); // 返回键
            }, 3000);
        }
        function exitApp(){
            navigator.app.exitApp();
        }
        // Destroy the #appLoadingIndicator element
        Ext.fly('appLoadingIndicator').destroy();

        // Initialize the main view
        Ext.Viewport.add(Ext.create('MyApp.view.Login'));
    },

    onUpdated: function() {
        /*Ext.Msg.confirm(
            "Application Update",
            "This application has just successfully been updated to the latest version. Reload now?",
            function(buttonId) {
                if (buttonId === 'yes') {
                    window.location.reload();
                }
            }
        );*/
        mui.confirm('服务器端的应用程序已被更新到最新版本.是否立即加载最新版本的应用程序?','信息',['确定','取消'],function (obj) {
            if(obj.index == '0' || obj.index == '1'){
                window.location.reload();
            }
        },'div');
    }
});
