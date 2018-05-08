/**添加新商品model
 * Created by zl on 2017/12/18 0018.
 */
Ext.define('MyApp.model.proManage.Addnewgoods',{
    extend:'Ext.data.Model',
    config:{
        fields:[
            {name:'newType',type:'string'},
            {name:'newBrandName',type:'string'},
            {name:'newModel',type:'string'},
            {name:'newEdition',type:'string'}
        ],
        validations:[
        ]
    },
    validate:function(options){
        var me = this,
            errors = me.callParent(arguments),
            newType = this.get('newType'),
            newBrandName = this.get('newBrandName'),
            newModel = this.get('newModel'),
            newEdition = this.get('newEdition');
        if(newType == ''){
            errors.add({
                field: 'newType',
                message: '商品类型不能为空'
            });
        }
        if(newBrandName == ''){
            errors.add({
                field: 'newBrandName',
                message: '品牌名称不能为空'
            });
        }
        if(newModel == ""){
            errors.add({
                field: 'newModel',
                message: '具体型号不能为空'
            });
        }
        if(newEdition == ""){
            errors.add({
                field: 'newEdition',
                message: '版本不能为空'
            });
        }
        return errors;
    }
});
