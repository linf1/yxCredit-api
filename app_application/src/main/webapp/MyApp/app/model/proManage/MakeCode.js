/**
 * 生成二维码
 * Created by zl on 2017/12/25 0025.
 */
Ext.define('MyApp.model.proManage.MakeCode',{
    extend:'Ext.data.Model',
    config:{
        fields:[
            {name:'totalPrice',type:'string'},//总价格
            {name:'downAmount',type:'string'},//首付金额
            {name:'offlineOrder',type:'string'},//首付金额
            {name:'interestRate',type:'string'},//利率方案
            {name:'servicePackage',type:'string'}//服务包
        ],
        validations:[
        ]
    },
    validate:function(options){
        var me = this,
            errors = me.callParent(arguments),
            totalPrice = me.get('totalPrice'),
            downAmount = me.get('downAmount'),
            offlineOrder = me.get('offlineOrder'),
            interestRate = me.get('interestRate'),
            servicePackage = me.get('servicePackage');
        var numReg = /^([0]|([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
        if(totalPrice == ""){
            errors.add({
                field: 'totalPrice',
                message: '商品总价不能为空'
            });
        }else if(!numReg.test(totalPrice)){
            errors.add({
                field: 'totalPrice',
                message: '请正确输入商品总价'
            });
        }
        if(downAmount == ""){
            errors.add({
                field: 'downAmount',
                message: '首付金额不能为空'
            });
        }else if(!numReg.test(downAmount)){
            errors.add({
                field: 'downAmount',
                message: '请正确输入首付金额'
            });
        }
        if(parseFloat(downAmount) > parseFloat(totalPrice)){
            errors.add({
                field: 'Amount112',
                message: '首付金额不能高于产品总金额'
            });
        }
        if(offlineOrder == ""){
            errors.add({
                field: 'offlineOrder',
                message: '请选择订单类型'
            });
        }
        if(interestRate == ""){
            errors.add({
                field: 'interestRate',
                message: '请选择利率方案'
            });
        }
        if(servicePackage == ""){
            errors.add({
                field: 'servicePackage',
                message: '请选择服务包'
            });
        }
        return errors;
    }
})
