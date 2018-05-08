/**基本信息
 * Created by zl on 2017/12/26 0026.
 */
Ext.define('MyApp.model.perinfo.BasicInfo',{
    extend:'Ext.data.Model',
    config: {
        fields:[
            {name:'idenType',type:'string'},//身份类型
            {name:'liveAddress', type: 'string'},//居住地址
            {name:'detailAddr',type:'string'},//详细地址
            {name:'companyName',type:'string'},//单位名称
            {name:'companyCode',type:'string'},//区号
            {name:'linkNumber',type:'string'},//联系电话
            {name:'positionName',type:'string'},//职位名称
            {name:'companyAddr',type:'string'},//单位地址
            {name:'comdeAddr',type:'string'}//详细地址
        ],
        validations:[

        ]
    },
    validate: function (options) {
        var me = this;
        errors = me.callParent(arguments),
            idenType = me.get('idenType'),//身份类型
            liveAddress = me.get('liveAddress'),//居住地址
            detailAddr = me.get('detailAddr'),//详细地址
            companyName = me.get('companyName'),//单位名称
            companyCode = me.get('companyCode'),//区号
            linkNumber = me.get('linkNumber'),//联系电话
            positionName = me.get('positionName'),//职位名称
            companyAddr = me.get('companyAddr'),//单位地址
            comdeAddr = me.get('comdeAddr');//单位地址
        var speciReg = /^[A-Za-z0-9\u4e00-\u9fa5]+$/;
        var regNum = /^\d{3,4}$/;
        var regNum2 = /\d{7,9}$/;
        var linkNumReg = /^[0-9]+$/;
        if(idenType == ""){
            errors.add({
                field: 'idenType',
                message: '请选择身份类型'
            })
        }
        if(liveAddress == ""){
            errors.add({
                field: 'liveAddress',
                message: '居住地址不能为空'
            })
        }
        if(detailAddr == ""){
            errors.add({
                field: 'detailAddr',
                message: '详细地址不能为空'
            })
        }else if(!speciReg.test(detailAddr)){
            errors.add({
                field: 'detailAddr',
                message: '详细地址不能输入特殊字符'
            })
        }
        if(companyName == ""){
            errors.add({
                field: 'companyName',
                message: '单位名称不能为空'
            })
        }
        if(linkNumber == ""){
            errors.add({
                field: 'linkNumber',
                message: '联系电话不能为空'
            })
        }else if(!linkNumReg.test(linkNumber)){
            errors.add({
                field: 'linkNumber',
                message: '联系电话只能输入数字'
            })
        }
        if(positionName == ""){
            errors.add({
                field: 'positionName',
                message: '职位名称不能为空'
            })
        }else if(!speciReg.test(positionName)){
            errors.add({
                field: 'positionName',
                message: '职位名称不能输入特殊字符'
            })
        }
        if(companyAddr == ""){
            errors.add({
                field: 'companyAddr',
                message: '单位地址不能为空'
            })
        }
        if(comdeAddr == ""){
            errors.add({
                field: 'comdeAddr',
                message: '单位详细地址不能为空'
            })
        }else if(!speciReg.test(comdeAddr)){
            errors.add({
                field: 'comdeAddr',
                message: '单位详细地址不能输入特殊字符'
            })
        }
        return errors;
    }
});
