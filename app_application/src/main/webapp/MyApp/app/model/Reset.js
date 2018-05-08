/**
 * Created by zl on 2017/12/5 0005.
 */
Ext.define('MyApp.model.Reset',{
    extend:'Ext.data.Model',
    config:{
        fields:[
            {name:'resetTel',type:'string'},
            {name:'resetPass',type:'string'},
            {name:'resetNewPass',type:'string'},
            {name:'comNewPass',type:'string'}
        ],
        validations:[]
    },
    validate:function(options) {
        var me = this,
            errors = me.callParent(arguments),
            resetTel = me.get('resetTel'),//手机号
            resetPass = me.get('resetPass'),//原密码
            resetNewPass = me.get('resetNewPass'),//新密码
            comNewPass = me.get('comNewPass');//确认密码
        var telReg = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1})|(17[0-9]{1}))+\d{8})$/;
        var passReg = /^[0-9a-zA-Z]{6,20}$/;
        if(resetTel == ""){
            errors.add({
                field: 'resetTel',
                message: '手机号不能为空'
            });
        }else if(!telReg.test(resetTel)){
            errors.add({
                field: 'resetTel',
                message: '手机号码格式不正确'
            });
        }
        if(resetPass == ""){
            errors.add({
                field: 'resetPass',
                message: '原密码不能为空'
            });
        }else if(!passReg.test(resetPass)){
            errors.add({
                field: 'resetPass',
                message: '原密码只能输入6-20位数字字母'
            });
        }
        if(resetNewPass == ""){
            errors.add({
                field: 'resetNewPass',
                message: '新密码不能为空'
            });
        }else if(!passReg.test(resetNewPass)){
            errors.add({
                field: 'resetNewPass',
                message: '新密码只能输入6-20位数字字母'
            });
        }
        if(resetNewPass != ""&&resetPass == resetNewPass){
            errors.add({
                field: 'resetNewPass',
                message: '新密码不能与原密码相同'
            });
        }
        if(comNewPass == ""){
            errors.add({
                field: 'comNewPass',
                message: '确认密码不能为空'
            });
        }else if(comNewPass !=resetNewPass){
            errors.add({
                field: 'comNewPass',
                message: '确认密码与新密码不一致'
            });
        }
        return errors;
    }
});
