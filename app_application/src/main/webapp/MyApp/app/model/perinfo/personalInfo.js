/**个人信息model
 * Created by Administrator on 2017/10/24 0024.
 */
Ext.define('MyApp.model.perinfo.personalInfo',{
    extend:'Ext.data.Model',
    config: {
        fields:[
            {name:'marryStatus',type:'string'},//婚况
            {name:'education', type: 'string'},//教育状况
            {name:'companyName',type:'string'},//公司名称
            {name:'linkNumber',type:'string'},//联系电话
            {name:'companyAddress',type:'string'},//单位地址
            {name:'detailAddr',type:'string'},//详细地址
            {name:'linealRel',type:'string'},//直系亲属姓名
            {name:'PersonDirectRelativesPhone',type:'string'},//联系人1联系方式
            {name:'relationship',type:'string'},//联系人1关系
            {name:'otherRel',type:'string'},//其他联系人姓名
            {name:'otherRelativesPhone',type:'string'},//联系人2联系方式
            {name:'relationship1',type:'string'}//联系人2关系
        ],
        validations:[

        ]
    },


    //判断验证码是否一致
    validate: function (options) {
        var me = this;
        errors = me.callParent(arguments),
            marryStatus = me.get('marryStatus'),//婚况
            education = me.get('education'),//教育状况
            companyName = me.get('companyName'),//公司名称
            linkNumber = me.get('linkNumber'),//联系电话
            companyAddress = me.get('companyAddress'),//单位地址
            detailAddr = me.get('detailAddr'),//详细地址
            linealRel = me.get('linealRel'),//直系亲属姓名
            PersonDirectRelativesPhone = me.get('PersonDirectRelativesPhone'),//联系人1联系方式
            relationship = me.get('relationship'),//联系人1关系
            otherRel = me.get('otherRel'),//其他联系人姓名
            otherRelativesPhone = me.get('otherRelativesPhone'),//联系人2联系方式
            relationship1 = me.get('relationship1');//联系人2关系

        var reg = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1})|(17[0-9]{1}))+\d{8})$/;
        var regNum = /^\d{3,4}-\d{7,9}$/;
        if(marryStatus == ""){
            errors.add({
                field: 'marryStatus',
                message: '婚姻状况不能为空'
            })
        }
        if(education == ""){
            errors.add({
                field: 'education',
                message: '教育状况不能为空'
            })
        }
        if(companyName == ""){
            errors.add({
                field: 'companyName',
                message: '公司名称不能为空'
            })
        }
        if(linkNumber == ""){
            errors.add({
                field: 'linkNumber',
                message: '联系电话不能为空'
            })
        }else if(!regNum.test(linkNumber)){
            errors.add({
                field: 'linkNumber',
                message: '联系电话格式不正确'
            })
        }
        if(companyAddress == ""){
            errors.add({
                field: 'companyAddress',
                message: '单位地址不能为空'
            })
        }
        if(detailAddr == ""){
            errors.add({
                field: 'detailAddr',
                message: '详细地址不能为空'
            })
        }
        if(linealRel == ""){
            errors.add({
                field: 'linealRel',
                message: '直系亲属姓名不能为空'
            })
        }
        if(PersonDirectRelativesPhone == ""){
            errors.add({
                field: 'PersonDirectRelativesPhone',
                message: '直系亲属号码不能为空'
            })
        }else if(!reg.test(PersonDirectRelativesPhone)){
            errors.add({
                field: 'PersonDirectRelativesPhone',
                message: '直系亲属号码格式不正确'
            })
        }
        if(relationship == ""){
            errors.add({
                field: 'relationship',
                message: '直系亲属关系不能为空'
            })
        }

        if(otherRel == ""){
            errors.add({
                field: 'otherRel',
                message: '其他联系人姓名不能为空'
            })
        }
        if(otherRelativesPhone == ""){
            errors.add({
                field: 'otherRelativesPhone',
                message: '其他联系人号码不能为空'
            })
        }else if(!reg.test(otherRelativesPhone)){
            errors.add({
                field: 'PersonDirectRelativesPhone',
                message: '其他联系人号码格式不正确'
            })
        }
        if(relationship1 == ""){
            errors.add({
                field: 'relationship1',
                message: '其他联系人关系不能为空'
            })
        }
        if(PersonDirectRelativesPhone!="" && otherRelativesPhone!=""){
            if(PersonDirectRelativesPhone === otherRelativesPhone){
                errors.add({
                    field: 'relateH',
                    message: '联系人号码不能相同'
                })
            }
        }

        return errors;
    }

});
