Ext.define('Ext.tux.picker.Products', {
    extend : 'Ext.picker.Picker',
    xtype : 'Productspicker',
    config : {
        doneButton:"",
        cancelButton:"取消",
        id:'getProductsProducts',
        slots:[
            {
                name : 'province',
                valueField: "id",
                id:'productType',
                store:{
                    fields: [
                        'id',
                        'text'
                    ],
                    sorters:"text"
                }
            }
        ],
        listeners: {
            show:function(){
                this.getProductsOne();
                valuesId ={
                    province: "",
                    provinceId:'',
                    city : "",
                    cityId:'',
                    district : "",
                    districtId:''
                }
            }
        }
    },
    getProductsOne:function(){
        var me=this;
        var merchant_id=Ext.ComponentQuery.query('#middleSaveview #merchant_id')[0].getValue();
        var product_id=Ext.ComponentQuery.query('#middleSaveview #product_id')[0].getValue();
        Comm.ajaxPost(
            '/productInfo/getType',
            {merchant_id:merchant_id,product_id:product_id},
            function (response) {
                var msg=eval('('+response.responseText+')');
                if (msg.retCode!="SUCCESS") {
                    return;
                }else{
                    var type=msg.retData.list;
                    var tlist=[];
                    for(var i=0;i<type.length;i++){
                        tlist.push({text:type[i].merchandise_type_name,id:type[i].merchandise_type_id});
                    }
                    if(tlist.length>0){
                        me.setValue({productType:''});
                        me.ProvinceSlot.getStore().removeAll();
                        me.ProvinceSlot.getStore().setData(tlist);
                    }
                }
            }
        );
        /*Ext.Ajax.request({
            url:"/productInfo/getType",
            method:'POST',
            params:{
                merchant_id:merchant_id,
                product_id:product_id
            },
            success:function(response) {
                var msg=eval('('+response.responseText+')');
                if (msg.retCode!="SUCCESS") {
                    return;
                }else{
                    var type=msg.retData.list;
                    var tlist=[];
                    for(var i=0;i<type.length;i++){
                        tlist.push({text:type[i].merchandise_type_name,id:type[i].merchandise_type_id});
                    }
                    if(tlist.length>0){
                        me.setValue({productType:''});
                        me.ProvinceSlot.getStore().removeAll();
                        me.ProvinceSlot.getStore().setData(tlist);
                    }
                }
            }
        });*/
    },
    initialize: function() {
        var me=this,
            innerItems = me.getInnerItems();
        me.callParent();
        me.ProvinceSlot = innerItems[0];
        me.ProvinceSlot.on("selectionchange",me.onProvinceSelect,me);
        me.setValue({province: 12, city: 94, district: 906});
    },
    onProvinceSelect:function(cmp ,records, eOpts){
        var me = this,
            record = records[0],
            value=me.getValue(true);
        var id=records[0].data.id;
        var provinceId=value.province;
        var merchant_id=Ext.ComponentQuery.query('#middleSaveview #merchant_id')[0].getValue();
        var product_id=Ext.ComponentQuery.query('#middleSaveview #product_id')[0].getValue();
        if(id!=provinceId){
            var ptype_id=Ext.ComponentQuery.query('#middleSaveview #ptype_id')[0].getValue();
            if(!ptype_id){
                ptype_id=provinceId;
            }
            Comm.ajaxPost(
                "/productInfo/getMerchantdise",
                {
                    merchandise_brand_id:ptype_id,
                    merchant_id:merchant_id,
                    product_id:product_id
                },
                function (response) {
                    var msg=eval('('+response.responseText+')');
                    var type=msg.retData.list;
                    var tlist=[];
                    for(var i=0;i<type.length;i++){
                        tlist.push({text:type[i].merchandise_name,id:type[i].merchandise_id});
                    }
                    if(tlist.length>0){
                        me.setValue({province:provinceId});
                        me.ProvinceSlot.getStore().removeAll();
                        me.ProvinceSlot.getStore().setData(tlist);
                        me.setDoneButton('确定');
                    }
                }
            );
            /*Ext.Ajax.request({
                url:"/productInfo/getMerchantdise",
                method:'POST',
                params:{
                    merchandise_brand_id:ptype_id,
                    merchant_id:merchant_id,
                    product_id:product_id
                },
                success:function(response) {
                    var msg=eval('('+response.responseText+')');
                    var type=msg.retData.list;
                    var tlist=[];
                    for(var i=0;i<type.length;i++){
                        tlist.push({text:type[i].merchandise_name,id:type[i].merchandise_id});
                    }
                    if(tlist.length>0){
                        me.setValue({province:provinceId});
                        me.ProvinceSlot.getStore().removeAll();
                        me.ProvinceSlot.getStore().setData(tlist);
                        me.setDoneButton('确定');
                    }
                }
            });*/
        }else{
            var brand_id=Ext.ComponentQuery.query('#middleSaveview #brand_id')[0].getValue();
            if(!brand_id){
                brand_id=provinceId;
            }
            Comm.ajaxPost(
                "/productInfo/getProcuctBrand",
                {
                    merchandise_type_id:brand_id,
                    merchant_id:merchant_id,
                    product_id:product_id
                },
                function (response) {
                    var msg=eval('('+response.responseText+')');
                    var type=msg.retData.list;
                    var tlist=[];
                    for(var i=0;i<type.length;i++){
                        tlist.push({text:type[i].merchandise_brand_name,id:type[i].merchandise_brand_id});
                    }
                    if(tlist.length>0){
                        me.setValue({province:provinceId});
                        me.ProvinceSlot.getStore().removeAll();
                        me.ProvinceSlot.getStore().setData(tlist);
                    }
                }
            );
            /*Ext.Ajax.request({
                url:"/productInfo/getProcuctBrand",
                method:'POST',
                params:{
                    merchandise_type_id:brand_id,
                    merchant_id:merchant_id,
                    product_id:product_id
                },
                success:function(response) {
                    var msg=eval('('+response.responseText+')');
                    var type=msg.retData.list;
                    var tlist=[];
                    for(var i=0;i<type.length;i++){
                        tlist.push({text:type[i].merchandise_brand_name,id:type[i].merchandise_brand_id});
                    }
                    if(tlist.length>0){
                        me.setValue({province:provinceId});
                        me.ProvinceSlot.getStore().removeAll();
                        me.ProvinceSlot.getStore().setData(tlist);
                    }
                }
            });*/
        }
        this.getRawValue(records);
    },
    setValue: function(value, animated) {
        var me =this,
            cdata = me.cityData[value.product],
            ddata = me.districtData[value.city];
        me.ProvinceSlot.getStore().setData(cdata);
        me.callParent([value, animated]);
    },
    onDoneButtonTap: function() {
        var me =this,
            oldValue = me._value,
            newValue = me.getValue(true),
            change = false;
        for( key in newValue){
            if(newValue[key]!= oldValue[key]){
                change = true;
                break;
            }
        }

        if (change) {
            me.fireEvent('change', me, newValue);
        }
        this.hide();
        me.setDoneButton('');
    },
    onCancelButtonTap: function() {
        var me=this;
        this.fireEvent('cancel', this);
        this.hide();
        me.setDoneButton('');
        Ext.util.InputBlocker.unblockInputs();
    },
    getRawValue: function(records){
        var me = this,
            values = {
                province: "请选择",
                provinceId:'',
                city : "请选择",
                cityId:'',
                district : "请选择",
                districtId:''
            },
            value = me.getValues(true);
        if(value.province){
            if(valuesId.city){
                if(valuesId.district){
                    valuesId.district = me.ProvinceSlot.getStore().getById(value.province).raw.text;
                    valuesId.districtId = me.ProvinceSlot.getStore().getById(value.province).data.id.toString();
                    return valuesId;
                }else{
                    valuesId.district = me.ProvinceSlot.getStore().getById(value.province).raw.text;
                    valuesId.districtId = me.ProvinceSlot.getStore().getById(value.province).data.id.toString();
                    return valuesId;
                }
            }
            if(valuesId.province){
                if(valuesId.city){
                    return;
                }else{
                    if(!valuesId.district){
                        valuesId.district = me.ProvinceSlot.getStore().getById(value.province).raw.text;
                        valuesId.districtId = me.ProvinceSlot.getStore().getById(value.province).data.id.toString();
                        return valuesId;
                    }else{
                        valuesId.city = me.ProvinceSlot.getStore().getById(value.province).raw.text;
                        valuesId.cityId = me.ProvinceSlot.getStore().getById(value.province).data.id.toString();
                    }
                }
            }
            if(value.province){
                if(valuesId.province){
                    return;
                }else{
                    valuesId.province = me.ProvinceSlot.getStore().getById(value.province).raw.text;
                    valuesId.provinceId = me.ProvinceSlot.getStore().getById(value.province).data.id;
                    if(value.province){
                        values.province = me.ProvinceSlot.getStore().getById(value.province).raw.text;
                        values.provinceId = me.ProvinceSlot.getStore().getById(value.province);
                        return values;
                    }
                }
            }
        }
    },
    getLevelLinkage:function(){
        var province=valuesId.province;
        var city=valuesId.city;
        var district=valuesId.district;
        if(province&&city&&district){
            return valuesId;
        }else{
            return valuesId={
                province: "",
                provinceId:'',
                city : "",
                cityId:'',
                district : "",
                districtId:''
            };
        }
    },
    cityData: {},
    districtData:{}
})
