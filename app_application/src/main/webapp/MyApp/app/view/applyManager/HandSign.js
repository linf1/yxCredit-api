/**
 * 客户手签
 * Created by zl on 2017/12/7 0007.
 */
Ext.define('MyApp.view.applyManager.HandSign',{
   extend:'Ext.Panel',
   xtype:'handSianView',
   requires:[
       'Ext.Panel'
   ],
   config:{
       id:'handSianView',
       showAnimation:{type:'slide', direction:'left'},
       scrollable:{direction: 'vertical',indicators: false},
       layout:{type:'vbox'},
       zIndex:5,
       items:[
           {
               xtype:'toolbar',
               title:'查看手签',
               docked : "top",
               style:'background-color:#FEE300;border-radius:0;',
               items:[
                   {
                       html: '<span class="icon-back" style="font-size:26px;color:#000;"></span>',
                       style:'margin-left:10px;color:#8c8c8c;margin-top:8px;',
                       listeners:{
                           tap:function () {
                               hidePage('#handSianView');
                           }
                       }
                   }
               ]
           },
           {
               xtype:'container',
               style:'background-color:#fff;height:240px;margin-top:10px;',
               items:[
                   {
                       html:'<img id="handSignImg" style="width:150px;display:block;margin:10px auto;"/>'
                   }
               ]
           }
       ],
       listeners:{
           show:function () {
               Ext.getCmp('handSianView').toGetSignImg();
           }
       }
   },
   toGetSignImg:function () {
       whir.loading.add("", 1);
       var orderId = Ext.getCmp('order_id').getValue();
       Comm.ajaxPost(
           '/image/getImageData',
           {orderId:orderId,type:'sqzp'},
           function (response) {
               whir.loading.remove();
               var msg = eval('('+response.responseText+')');
               console.log(msg);
               if(msg.retCode == "SUCCESS"){
                   var data = msg.retData;
                   var sqzpList = data.sqzpList;
                   var handSignImg = document.getElementById('handSignImg');
                   handSignImg.setAttribute('src',sqzpList[0].imgUrl);
               }
           }
       );
   } 
});
