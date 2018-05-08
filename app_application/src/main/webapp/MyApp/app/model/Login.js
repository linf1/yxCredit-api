/**
 * 登录页model
 * Created by zl on 2017/11/21 0021.
 */
Ext.define('MyApp.model.Login',{
    extend:'Ext.data.Model',
    config:{
        fields:[
            {name:'txt_username',type:'string'},
            {name:'txt_password',type:'string'}
        ],
        validations:[
            /*{type:'presence',field:'txt_username',message:'必须输入手机号'},
             {type:'format',field:'txt_username',matcher:/^1[34578]\d{9}$/,message:'请输入合法的手机号码'},
             {type:'presence',field:'txt_password',message:'必须输入密码'},
             {type:'format',field:'txt_password',matcher:/^[0-9a-zA-Z]{6,20}$/,message:'只能填写6-20位字母数字'}*/
        ]
    },
    validate:function(options){
        var me = this,
            errors = me.callParent(arguments),
            txt_username = this.get('txt_username'),
            txt_password = this.get('txt_password');
        //姓名验证
        if(txt_username == ''){
            errors.add({
                field: 'txt_username',
                message: '手机号不能为空'
            });
        }else{
            var reg=/^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1})|(17[0-9]{1}))+\d{8})$/;
            res=reg.test(txt_username);
            if(!res){
                errors.add({
                    field:'txt_username',message:'请输入正确的手机号'
                })
            }
        };
        //密码验证
        if(txt_password == ''){
            errors.add({
                field: 'txt_password',
                message: '密码不能为空'
            });
        }else{
            var reg=/^[0-9a-zA-Z]{6,20}$/;
            res=reg.test(txt_password);
            if(!res){
                errors.add({
                    field:'txt_password',message:'密码只能填写6-20位字母数字'
                })
            }
        }
        return errors;
    }
});
