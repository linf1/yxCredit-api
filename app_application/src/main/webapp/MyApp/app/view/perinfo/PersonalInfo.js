/**个人信息
 * Created by zl on 2017/11/23 0023.
 */
Ext.define('MyApp.view.perinfo.PersonalInfo',{
    extend:'Ext.Panel',
    xtype:'personalView',
    requires:[
        'Ext.Panel'
    ],
    config:{
        id:'personalView',
        showAnimation:{type:'slide', direction:'left'},
        layout:{type:'vbox'},
        items:[
            {
                xtype:'formpanel',
                id:'personalForm',
                flex:9,
                scrollable:{direction: 'vertical',indicators: false},
                style:'margin-top:10px;',
                items:[
                    {
                        xtype:'textfield',
                        id:'realName',
                        name:'realName',
                        clearIcon:false,
                        label:'<span style="font-size: 14px;color:#666;">真实姓名</span>',
                        readOnly:true,
                        style:'border-bottom:1px solid #f1f1f1;'
                    },
                    {
                        xtype:'textfield',
                        id:'sex',
                        name:'sex',
                        clearIcon:false,
                        label:'<span style="font-size: 14px;color:#666;">性别</span>',
                        readOnly:true,
                        style:'border-bottom:1px solid #f1f1f1;'
                    },
                    {
                        xtype:'textfield',
                        id:'birthday',
                        name:'birthday',
                        clearIcon:false,
                        label:'<span style="font-size: 14px;color:#666;">出生日期</span>',
                        readOnly:true,
                        style:'border-bottom:1px solid #f1f1f1;'
                    },
                    {
                        xtype:'textfield',
                        id:'idCardNum',
                        name:'idCardNum',
                        clearIcon:false,
                        label:'<span style="font-size: 14px;color:#666;">身份证号码</span>',
                        readOnly:true,
                        style:'border-bottom:1px solid #f1f1f1;'
                    },
                    {
                        xtype:'textareafield',
                        id:'birthAddr',
                        name:'birthAddr',
                        clearIcon:false,
                        label:'<span style="font-size: 14px;color:#666;">户籍地址</span>',
                        readOnly:true,
                        style:'border-bottom:1px solid #f1f1f1;height:65px;'
                    },
                    {
                        html:'<div class="link-man-content">联系人1</div>'
                    },
                    {
                        xtype:'textfield',
                        id:'linealRel',
                        name:'linealRel',
                        clearIcon:false,
                        label:'<span style="font-size: 14px;color:#666;">直系亲属</span>',
                        readOnly:true,
                        style:'border-bottom:1px solid #f1f1f1;'
                    },
                    {
                        xtype:'textfield',
                        id:'linealTel',
                        name:'linealTel',
                        clearIcon:false,
                        label:'<span style="font-size: 14px;color:#666;">联系方式</span>',
                        readOnly:true,
                        style:'border-bottom:1px solid #f1f1f1;'
                    },
                    {
                        xtype:'textfield',
                        id:'lineaRelation',
                        name:'lineaRelation',
                        clearIcon:false,
                        label:'<span style="font-size: 14px;color:#666;">关系</span>',
                        readOnly:true,
                        style:'border-bottom:1px solid #f1f1f1;'
                    },
                    {
                        html:'<div class="link-man-content">联系人2</div>'
                    },
                    {
                        xtype:'textfield',
                        id:'linealRel1',
                        name:'linealRel1',
                        clearIcon:false,
                        label:'<span style="font-size: 14px;color:#666;">直系亲属</span>',
                        readOnly:true,
                        style:'border-bottom:1px solid #f1f1f1;'
                    },
                    {
                        xtype:'textfield',
                        id:'linealTel1',
                        name:'linealTel1',
                        clearIcon:false,
                        label:'<span style="font-size: 14px;color:#666;">联系方式</span>',
                        readOnly:true,
                        style:'border-bottom:1px solid #f1f1f1;'
                    },
                    {
                        xtype:'textfield',
                        id:'lineaRelation1',
                        name:'lineaRelation1',
                        clearIcon:false,
                        label:'<span style="font-size: 14px;color:#666;">关系</span>',
                        readOnly:true,
                        style:'border-bottom:1px solid #f1f1f1;'
                    },
                    {
                        html:'<div class="link-man-content">联系人3</div>'
                    },
                    {
                        xtype:'textfield',
                        id:'otherRel',
                        name:'otherRel',
                        clearIcon:false,
                        label:'<span style="font-size: 14px;color:#666;">其他联系人</span>',
                        readOnly:true,
                        style:'border-bottom:1px solid #f1f1f1;'
                    },
                    {
                        xtype:'textfield',
                        id:'otherTel',
                        name:'otherTel',
                        clearIcon:false,
                        label:'<span style="font-size: 14px;color:#666;">联系方式</span>',
                        readOnly:true,
                        style:'border-bottom:1px solid #f1f1f1;'
                    },
                    {
                        xtype:'textfield',
                        id:'otherRelation',
                        name:'otherRelation',
                        clearIcon:false,
                        label:'<span style="font-size: 14px;color:#666;">关系</span>',
                        readOnly:true,
                        style:'border-bottom:1px solid #f1f1f1;'
                    },
                    {
                        html:'<div class="link-man-content">联系人4</div>'
                    },
                    {
                        xtype:'textfield',
                        id:'otherRel1',
                        name:'otherRel1',
                        clearIcon:false,
                        label:'<span style="font-size: 14px;color:#666;">其他联系人</span>',
                        readOnly:true,
                        style:'border-bottom:1px solid #f1f1f1;'
                    },
                    {
                        xtype:'textfield',
                        id:'otherTel1',
                        name:'otherTel1',
                        clearIcon:false,
                        label:'<span style="font-size: 14px;color:#666;">联系方式</span>',
                        readOnly:true,
                        style:'border-bottom:1px solid #f1f1f1;'
                    },
                    {
                        xtype:'textfield',
                        id:'otherRelation1',
                        name:'otherRelation1',
                        clearIcon:false,
                        label:'<span style="font-size: 14px;color:#666;">关系</span>',
                        readOnly:true,
                        style:'border-bottom:1px solid #f1f1f1;'
                    },
                    {
                        xtype:'hiddenfield',
                        id:'orderStateFlag'
                    }
                ]
            }
        ],
        listeners:{
            show:function () {
                Ext.getCmp('personalView').toGetPersonInfo();
            }
        }
    },
    toGetPersonInfo:function () {
        whir.loading.add("", 1);
        var me = this;
        var orderId = Ext.getCmp('order_id').getValue();
        Comm.ajaxPost(
            '/employeeApply/getApplyInfo',
            {orderId:orderId},
            function (response) {
                whir.loading.remove();
                var msg = eval('('+response.responseText+')');
                console.log(msg);
                if(msg.retCode == "SUCCESS"){
                    var data = msg.retData;
                    if(parseInt(data.commodityState) > 11){
                        //不可修改
                        Ext.getCmp('custHomeBtn').hide();
                        Ext.getCmp('orderStateFlag').setValue(data.commodityState);
                    }
                    if (parseInt(data.commodityState) > 14){Ext.getCmp('saveBasicInfoBtn').hide();}
                    var indentyMap = data.indentyMap,
                        linkManMap = data.linkManMap,
                        indetityStateMap = data.indetityStateMap;
                    me.assignPersonInfo(indentyMap,linkManMap,indetityStateMap);
                }
            }
        );
    },
    assignPersonInfo:function (indentyMap,linkManMap,indetityStateMap) {
        var realName = Ext.getCmp('realName'),//真实姓名
            sex = Ext.getCmp('sex'),//性别
            birthday = Ext.getCmp('birthday'),//出生日期
            idCardNum = Ext.getCmp('idCardNum'),//身份证号码
            birthAddr = Ext.getCmp('birthAddr');//户籍地址
        //联系人1-----------------
        var linealRel = Ext.getCmp('linealRel'),
            linealTel = Ext.getCmp('linealTel'),
            lineaRelation = Ext.getCmp('lineaRelation');
        //联系人2-----------------
        var linealRel1 = Ext.getCmp('linealRel1'),
            linealTel1 = Ext.getCmp('linealTel1'),
            lineaRelation1 = Ext.getCmp('lineaRelation1');
        //联系人3-------------------
        var otherRel = Ext.getCmp('otherRel'),
            otherTel = Ext.getCmp('otherTel'),
            otherRelation = Ext.getCmp('otherRelation');
        //联系人4-------------------
        var otherRel1 = Ext.getCmp('otherRel1'),
            otherTel1 = Ext.getCmp('otherTel1'),
            otherRelation1 = Ext.getCmp('otherRelation1');
        //状态-------------------
        var basicBadge = document.getElementById('basic-badge'),
            authBadge = document.getElementById('auth-badge'),
            isMove = document.getElementById('isMove'),//运营商授权
            isFace = document.getElementById('isFace');//人脸识别
        var faceFlag = Ext.getCmp('faceFlag'),
            phoneFlag = Ext.getCmp('phoneFlag');
        if(indentyMap){
            //存储客户电话，在运营商处待用
            Ext.getCmp('customerTel').setValue(indentyMap.customerTel);
            realName.setValue(indentyMap.realname);
            sex.setValue(indentyMap.sexName);
            birthday.setValue(indentyMap.birth);
            idCardNum.setValue(indentyMap.cardNo);
            birthAddr.setValue(indentyMap.cardRegissterAddress);
            Ext.getCmp('customerName').setValue(indentyMap.realname);
            Ext.getCmp('customerCardNum').setValue(indentyMap.cardNo);
        }
        if(linkManMap){
            //直系联系人
            var linkmanlist = linkManMap.linkmanlist;
            var linkmanlistF = linkmanlist[0],
                linkmanlistS = linkmanlist[1];
            //联1
            linealRel.setValue(linkmanlistF.linkName);
            linealTel.setValue(linkmanlistF.contact);
            lineaRelation.setValue(linkmanlistF.relationshipname);
            //联2
            linealRel1.setValue(linkmanlistS.linkName);
            linealTel1.setValue(linkmanlistS.contact);
            lineaRelation1.setValue(linkmanlistS.relationshipname);
            //其他联系人
            var olinkmanlist = linkManMap.olinkmanlist;
            var olinkmanlistF = olinkmanlist[0],
                olinkmanlistS = olinkmanlist[1];
            //联3
            otherRel.setValue(olinkmanlistF.linkName);
            otherTel.setValue(olinkmanlistF.contact);
            otherRelation.setValue(olinkmanlistF.relationshipname);
            //联4
            otherRel1.setValue(olinkmanlistS.linkName);
            otherTel1.setValue(olinkmanlistS.contact);
            otherRelation1.setValue(olinkmanlistS.relationshipname);
        }
        if(indetityStateMap){
            indetityStateMap.basicCompleteSpd == '1'?basicBadge.style.display='none':basicBadge.style.display='block';
            indetityStateMap.authorizationComplete == '1'?authBadge.style.display='none':authBadge.style.display='block';
            if(indetityStateMap.faceState == '1'){
                isFace.setAttribute('class','au-text au-active');
                isFace.innerHTML = '已授权';
                faceFlag.setValue('1');
            }else{
                isFace.setAttribute('class','au-text');
                isFace.innerHTML = '未授权';
                faceFlag.setValue('');
            }
            if(indetityStateMap.phoneState == '1'){
                isMove.setAttribute('class','au-text au-active');
                isMove.innerHTML = '已授权';
                phoneFlag.setValue('1');
            }else{
                isMove.setAttribute('class','au-text');
                isMove.innerHTML = '未授权';
                phoneFlag.setValue('');
            }
        }
    }
})
