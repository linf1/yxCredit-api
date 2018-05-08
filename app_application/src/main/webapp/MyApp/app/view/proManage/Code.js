/**
 * 订单详情
 */
Ext.define('MyApp.view.proManage.Code', {
    extend: 'Ext.Panel',
    xtype: 'codeView',
    requires: [],
    config: {
        id: 'codeView',
        showAnimation: {type: 'slide', direction: 'right'},
        layout: {type: 'vbox'},
        zIndex:5,
        style: 'background:#F6F6F6',
        scrollable:{direction: 'vertical',indicators: false},
        items: [
            {
                xtype: 'toolbar',
                title: '<span>商品二维码</span>',
                style: 'background-color: #FDE300;border-bottom:0;',
                docked:'top',
                items: [
                    {
                        html: '<span class="icon-back" style="font-size:26px;color:#000;"></span>',
                        style: 'margin-left:10px;color:#8c8c8c;margin-top:5px;',
                        listeners: {
                            tap: function () {
                                hidePage('#codeView');
                            }
                        }
                    }
                ]
            },
            {
                xtype: "panel",
                style: 'background-color: #FDE300;',
                id:'code',
                items:[
                    {
                        html: '<div class="code-box"></div>' +
                        '<div class="code">' +
                        '<div class="code-top">' +
                        '<p class="code-textone">扫一扫，获取商品分期信息</p>' +
                        '<img style="width:200px;" id="codeImg" src="" alt="">' +
                        '<p>(二维码10分钟内有效)</p>'+
                        '<p id="productMoney" class="code-texttwo">￥5780.00</p>' +
                        '</div>' +
                        '<div class="code-center">' +
                        '<span class="one"></span>' +
                        '<span class="code-center-text">申请分期金额</span>' +
                        '<span class="two"></span>' +
                        '</div>' +
                        '<div class="code-bottom">' +
                        '<table>' +
                        '<tr>' +
                        '<td style="text-align: left;width:100px;font-size: 14px;padding: 5px 0;">每期还款金额</td>' +
                        '<td id="termMoney" style="text-align: right;font-size: 14px;" ></td>' +
                        '</tr>' +
                        '<tr>' +
                        '<td style="text-align: left;width:100px;font-size: 14px;padding: 5px 0;">期数</td>' +
                        '<td id="proPeriods" style="text-align: right;font-size: 14px;" ></td>' +
                        '</tr>' +
                        '<tr>' +
                        '<td style="text-align: left;width:100px;font-size: 14px;color: #9D9D9D;padding: 5px 0;">商品金额</td>' +
                        '<td id="proAmount" style="text-align: right;font-size: 14px;color: #9D9D9D;" ></td>' +
                        '</tr>' +
                        '<tr>' +
                        '<td style="text-align: left;width:100px;font-size: 14px;color: #9D9D9D;padding: 5px 0;">首付金额</td>' +
                        '<td id="firstAmount" style="text-align: right;font-size: 14px;color: #9D9D9D;" ></td>' +
                        '</tr>' +
                        '</table>' +
                        '</div>' +
                        '<div class="code-bottom-one">' +
                        '<span id="comName" style="font-size: 14px;padding-left:15px;position: relative;right:8px;float:left;"></span>' +
                        '<p style="float:right;padding-right:15px;">' +
                        '<span id="merchandiseModelName" class="bottom-type" style="position: relative;left: 20px;"></span>' +
                        '<span id="merchandiseVersionName" class="bottom-type" style="position: relative;left: 20px;"></span>' +
                        '</p>'+
                        '</div>' +
                        '<img src="resources/images/demo1.png" class="code_img" alt="">' +
                        '</div>'
                    },
                    {
                        xtype:'button',
                        html:'<span style="color:#1a1a1d;">返回首页</span>',
                        style:'width:180px;margin:16px auto;border:none;border-radius:50px;background-color:#FF6600;height:40px;box-shadow: 0px 3px 3px rgba(248,214,201,1);',
                        listeners:{
                            tap:function () {
                                if(Ext.getCmp('makeCodeView')){
                                    Ext.getCmp('makeCodeView').destroy();
                                }
                                hidePage('codeView');
                            }
                        }
                    }
                ]
            }
        ],
        listeners: {
            show:function () {
                var data = makeCodeThis.data;
                console.log(data);
                Ext.getCmp('codeView').assignCode(data)
            }
        }
    },
    initialize: function () {

    },
    assignCode:function (data) {
        var codeImg =  document.getElementById('codeImg'),
            productMoney = document.getElementById('productMoney'),
            termMoney = document.getElementById('termMoney'),
            proPeriods = document.getElementById('proPeriods'),
            proAmount = document.getElementById('proAmount'),
            firstAmount = document.getElementById('firstAmount'),
            comName = document.getElementById('comName'),
            merchandiseModelName = document.getElementById('merchandiseModelName'),
            merchandiseVersionName = document.getElementById('merchandiseVersionName');
        codeImg.src=data.base64String;
        productMoney.innerHTML = '￥'+data.fenqiMoney;
        termMoney.innerHTML = '￥'+data.monthPay;
        proAmount.innerHTML = '￥'+data.allMoney;
        firstAmount.innerHTML = '￥'+data.downPayMoney;
        proPeriods.innerHTML = data.periods;
        comName.innerHTML = data.merchandiseBrandName;
        merchandiseModelName.innerHTML = data.merchandiseModelName;
        merchandiseVersionName.innerHTML = data.merchandiseVersionName;
    }
});