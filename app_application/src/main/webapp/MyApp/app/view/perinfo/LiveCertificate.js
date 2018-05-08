/**
 * Created by zl on 2017/8/2 0002.
 * 活体认证
 */
Ext.define('MyApp.view.perinfo.LiveCertificate',{
    extend:'Ext.Panel',
    xtype:'LiveCertificateView',
    requires:[
        'Ext.Panel',
        'Ext.Button',
        'Ext.form.Panel',
        'Ext.Img',
        'Ext.field.Hidden'
    ],
    config:{
        id:'LiveCertificateView',
        zIndex:5,
        layout:{
            type:'vbox',
        },
        showAnimation : {
            type:'slide', direction : 'left'
        },
        items:[
            {
                xtype:'toolbar',
                docked : "top",
                title:'人脸验证',
                style:'background-color: #fed300;border-radius:0;',
                items:[
                    {
                        html: '<span class="icon-back" style="font-size:26px;color:#000;"></span>',
                        style:'margin-left:10px;color:#fff;margin-top:5px;',
                        listeners:{
                            tap:function () {
                                hidePage('#LiveCertificateView');
                            }
                        }
                    }
                ]
            },
            {
                xtype:'panel',
                scrollable:{direction: 'vertical',indicators: false},
                id:'liveCertiformpanel',
                flex:9,
                items:[
                    {
                        html:'<div style="margin:64px auto 20px;"></div><div style="text-align:center;"><span class="icon-photo" style="color:#000;font-size:25px;vertical-align:middle;padding-right:10px;"></span><span style="font-size:20px;color:#000;vertical-align: middle;">即将开始人脸验证</span></div><div style="text-align:center;"><span style="font-size:12px;color:#ccc;">"人脸验证"步骤摄像将用于身份核实</span></div></div>'
                    },
                    //是否人脸验证
                    {
                        xtype:"hiddenfield",
                        id:'isFace',
                        value:""
                    },
                    {
                        xtype:'img',
                        width:'280px',
                        height:'166px',
                        id:'liveCertificatePhoto',
                        src:'resources/images/face-phone.png',
                        style:'margin:49px auto 65px;',
                        listeners:{
                            tap:function () {
                                liveCertificateViewThis.getLiveCertificate();
                            }
                        }
                    },
                    {
                        xtype:'button',
                        html:'<span style="color:#000;">开始认证</span>',
                        style:'background-color:#ffda44;border:none;border-radius:140px;width:280px;height:40px;margin:30px auto;box-shadow: 0px 3px 3px rgba(248,214,201,1);',
                        listeners:{
                            tap:function(){
                                liveCertificateViewThis.getLiveCertificate();
                            }
                        }
                    }
                ]
            }
        ]
    },
    initialize:function (){
        liveCertificateViewThis=this;
    },
    /**活体认证**/
    getLiveCertificate: function () {
        var ua = navigator.userAgent.toLowerCase();
        var certificateName = Ext.ComponentQuery.query('#middleSaveview #customerName')[0].getValue();
        var certificateCard = Ext.ComponentQuery.query('#middleSaveview #customerCardNum')[0].getValue();
        var option = [certificateName,certificateCard];
        if (/iphone|ipad|ipod/.test(ua)) {
            device.getMegLive(liveCertificateViewThis.getMegLiveSuccess,liveCertificateViewThis.getMegLiveError,option);
        }else if(/android/.test(ua)){
            device.megLive(liveCertificateViewThis.getMegLiveSuccess,liveCertificateViewThis.getMegLiveError,option);
        }
    },
    /***人脸认证成功***/
    getMegLiveSuccess:function (msg) {
        whir.loading.add("", 1);
        if(typeof(msg)=="string"){//安卓
            msg=eval('('+msg+')');
            var data=msg.data;
            var image=msg.image;
            data=eval('('+data+')');
            var xh_result_faceid=data.xh_result_faceid;
            var confidence = parseInt(xh_result_faceid.confidence);
            var thOfFour = parseInt(xh_result_faceid.thOfFour);
            var orderId = Ext.getCmp('order_id').getValue();
            if(confidence >= thOfFour){
               Comm.ajaxPost(
                   '/employeeAuthorization/saveFaceComplete',
                   {faceSrc:image,orderId:orderId},
                   function (response) {
                       whir.loading.remove();
                       var msg = eval('('+response.responseText+')');
                       console.log(msg);
                       if(msg.retCode == "SUCCESS"){
                           var isFace = document.getElementById('isFace');
                           isFace.setAttribute('class','au-text au-active');
                           isFace.innerHTML = '已授权';
                           //判断上个页面必填项是否全部完成
                           var authBadge = document.getElementById('auth-badge');
                           var faceFlag = Ext.getCmp('faceFlag'),
                               phoneFlag = Ext.getCmp('phoneFlag').getValue();
                           faceFlag.setValue('1');
                           phoneFlag == ""?authBadge.style.display = 'block':authBadge.style.display='none';
                           hidePage('#LiveCertificateView');
                       }
                   }
               );
            }else{
                whir.loading.remove();
                mui.alert('识别失败,请重试!','消息','确定',null,'div');
            }
        }else {//ios
            var image = msg.xh_image_best;
            var confidence = parseInt(msg.confidence);
            var thOfFour = parseInt(msg.thOfFour);
            var orderId = Ext.getCmp('order_id').getValue();
            if(confidence >= thOfFour){
                Comm.ajaxPost(
                    '/employeeAuthorization/saveFaceComplete',
                    {faceSrc:image,orderId:orderId},
                    function (response) {
                        whir.loading.remove();
                        var msg = eval('('+response.responseText+')');
                        console.log(msg);
                        if(msg.retCode == "SUCCESS"){
                            var isFace = document.getElementById('isFace');
                            isFace.setAttribute('class','au-text au-active');
                            isFace.innerHTML = '已授权';
                            //判断上个页面必填项是否全部完成
                            var authBadge = document.getElementById('auth-badge');
                            var faceFlag = Ext.getCmp('faceFlag'),
                                phoneFlag = Ext.getCmp('phoneFlag').getValue();
                            faceFlag.setValue('1');
                            phoneFlag == ""?authBadge.style.display = 'block':authBadge.style.display='none';
                            hidePage('#LiveCertificateView');
                        }
                    }
                );
            }else{
                whir.loading.remove();
                mui.alert('识别失败,请重试!','消息','确定',null,'div');
            }
        }

    },
    getMegLiveError:function (msg) {//人脸识别失败
        whir.loading.remove();
        mui.alert('识别失败,请重试!','消息','确定',null,'div');
    }
});
