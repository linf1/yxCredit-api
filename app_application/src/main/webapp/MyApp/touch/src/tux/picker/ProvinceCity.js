Ext.define('Ext.tux.picker.ProvinceCity', {
    extend : 'Ext.picker.Picker',
    xtype : 'ProvincepickerCity',
    config : {
        doneButton:"",
        cancelButton:"取消",
        id:'fyProvinceCityDistrict',
        slots:[
            {
                name : 'province',
                valueField: "id",
                id:'province',
                store:{
                    fields: [
                        'id',
                        'text',
                        'pid'
                    ],
                    sorters:"text"
                }
            }
        ],
        listeners: {
            show:function(){
                this.getProvince();
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
    getProvince:function(){
        isCheck=1;
        var me=this;
        Comm.ajaxPost(
            "/FYOpenAccount/getCity?systemId=1&isCheck=1",
            {flag:'province'},
            function (response) {
                var msg=eval('('+response.responseText+')');
                tlist=msg.retData.tlist;
                if(tlist.length>0){
                    me.setValue({province:''});
                    me.ProvinceSlot.getStore().removeAll();
                    me.ProvinceSlot.getStore().setData(tlist);
                }
            }
        );
       /* Ext.Ajax.request({
            url:"/FYOpenAccount/getCity?systemId=1&isCheck=1",
            method:'POST',
            params:{
                flag:'province'
            },
            success:function(response) {
                var msg=eval('('+response.responseText+')');
                tlist=msg.retData.tlist;
                if(tlist.length>0){
                    me.setValue({province:''});
                    me.ProvinceSlot.getStore().removeAll();
                    me.ProvinceSlot.getStore().setData(tlist);
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
        var id=records[0].data.pid;
        var provinceId=value.province;
        if(isCheck==1){
            Comm.ajaxPost(
                "/FYOpenAccount/getCity?systemId=1&isCheck=1",
                {
                    parentId:id,
                    flag:'city'
                },
                function (response) {
                    var msg=eval('('+response.responseText+')');
                    tlist=msg.retData.tlist;
                    if(tlist.length>0){
                        me.setValue({province:provinceId});
                        me.ProvinceSlot.getStore().removeAll();
                        me.ProvinceSlot.getStore().setData(tlist);
                        me.setDoneButton('确定');
                        isCheck=0;
                    }
                }
            );
            /*Ext.Ajax.request({
                url:"/FYOpenAccount/getCity?systemId=1&isCheck=1",
                method:'POST',
                params:{
                    parentId:id,
                    flag:'city'
                },
                success:function(response) {
                    var msg=eval('('+response.responseText+')');
                    tlist=msg.retData.tlist;
                    if(tlist.length>0){
                        me.setValue({province:provinceId});
                        me.ProvinceSlot.getStore().removeAll();
                        me.ProvinceSlot.getStore().setData(tlist);
                        me.setDoneButton('确定');
                        isCheck=0;
                    }
                }
            });*/
        }
        this.getRawValue(records);
    },
    setValue: function(value, animated) {
        var me =this,
            cdata = me.cityData[value.province],
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
                province: "省",
                provinceId:'',
                city : "市",
                cityId:'',
                district : "区(县)",
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
