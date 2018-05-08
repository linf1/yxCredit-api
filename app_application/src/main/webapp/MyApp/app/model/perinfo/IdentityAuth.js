/**
 * Created by zl on 2017/10/25 0025.
 * 身份认证model
 */
Ext.define('MyApp.model.perinfo.IdentityAuth',{
    extend:'Ext.data.Model',
    config: {
        fields:[
            {name:'idcardFrontId',type:'string'},//身份证正面
            {name:'idcardBackId', type: 'string'},//身份证反面
            {name:'scanName',type:'string'},//真实姓名
            {name:'scanSex',type:'string'},//性别
            {name:'scanBirth',type:'string'},//出生日期
            {name:'scanCard',type:'string'},//身份证号码
            {name:'scanAddress',type:'string'}//户籍地址
        ],
        validations:[

        ]
    },


    //判断验证码是否一致
    validate: function (options) {
        var me = this;
        errors = me.callParent(arguments),
            idcardFrontId = me.get('idcardFrontId'),//身份证正面
            idcardBackId = me.get('idcardBackId'),//身份证反面
            scanName = me.get('scanName'),//真实姓名
            scanSex = me.get('scanSex'),//性别
            scanBirth = me.get('scanBirth'),//出生日期
            scanCard = me.get('scanCard'),//身份证号码
            scanAddress = me.get('scanAddress');//户籍地址

        if(idcardFrontId == ""){
            errors.add({
                field: 'idcardFrontId',
                message: '身份证正面不能为空'
            })
        }
        if(idcardBackId == ""){
            errors.add({
                field: 'idcardBackId',
                message: '身份证反面不能为空'
            })
        }
        if(scanName == ""){
            errors.add({
                field: 'scanName',
                message: '真实姓名不能为空'
            })
        }else{
            var nameReg =  /^[\u2E80-\u9FFF]+$/;
            var nameRes = nameReg.test(scanName);
            if(!nameRes){
                errors.add({
                    field: 'scanName',
                    message: '姓名只能输入汉字'
                });
            }
        }
        if(scanSex == ""){
            errors.add({
                field: 'scanSex',
                message: '性别不能为空'
            })
        }
        if(scanBirth == null){
            errors.add({
                field: 'scanBirth',
                message: '出生日期不能为空'
            })
        }
        if(scanCard == ""){
            errors.add({
                field: 'scanCard',
                message: '身份证号码不能为空'
            })
        }else{
            var reg = /^(\d{18}$|^\d{17}(\d|X|x))$/;
            var res = reg.test(scanCard);
            if(!res){
                errors.add({
                    field: 'scanCard',
                    message: '身份证号格式不正确'
                });
            }
        }
        if(scanAddress == ""){
            errors.add({
                field: 'scanAddress',
                message: '户籍地址不能为空'
            })
        }

        return errors;
    }

});

