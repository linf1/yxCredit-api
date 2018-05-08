$(function () {
    getSelect();
});


/**个人信息**/
$("[name=companyAddr]").cityPicker({
    title: "请选择地址"
});
$("#personInfoBtn").on("click",function () {
    var isValidate=checkPersonInfo();
    if(isValidate){
        updataPersonInfo();
    }
})

/**
 * 获取下拉选
 */
function getSelect(){
    Comm.ajaxPost("/wechat/basicinfo/getMiaofuBasicInfo","",function (sms) {
        var marriageSelect = sms.retData.maritalList;
        var education = sms.retData.eduList;
        var immediateRel = sms.retData.linkManList;
        var otherRel = sms.retData.otherLinkManList;
        console.log(sms.retData);
        setSelect("marriageSelect",marriageSelect,sms.retData.basicMap.marry);
        setSelect("education",education,sms.retData.basicMap.educational);
        setSelect("immediateRel",immediateRel,"");
        setSelect("otherRel",otherRel,"");


        if(sms.retData.OccupationMap != null){
            $("[name=companyName]").val(sms.retData.OccupationMap.company_name)//公司名称
            $("[name=companyPhone]").val(sms.retData.OccupationMap.company_phone)//公司电话
            $("[name=companyPlace]").val(sms.retData.OccupationMap.address)//公司详细地址
            $("[name=companyAddr]").val(sms.retData.OccupationMap.province_name+" "+sms.retData.OccupationMap.city_name+" "+sms.retData.OccupationMap.district_name)//公司地址省市区
            $("[name=companyAddr]").attr("data-codes",sms.retData.OccupationMap.province_id+","+sms.retData.OccupationMap.city_id+","+sms.retData.OccupationMap.district_id);
        }

        if(sms.retData.olist.length >= 2){
            $("[name=linkDirectId]").val(sms.retData.olist[0].id);//直系亲属ID
            $("[name=linkOtherId]").val(sms.retData.olist[1].id);//其他联系人ID


            $("[name=immediateName]").val(sms.retData.olist[0].link_name),////直系亲属姓名
            $("[name=immediatePhone]").val(sms.retData.olist[0].contact),//直系亲属手机号
            $("[name=immediateRel]").val(sms.retData.olist[0].relationship);//直系亲属关系

            $("[name=otherName]").val(sms.retData.olist[1].link_name),//其他联系人姓名
            $("[name=otherPhone]").val(sms.retData.olist[1].contact),//其他联系人电话
            $("[name=otherRel]").val(sms.retData.olist[1].relationship);//其他联系人关系
        }

    });
}

/**
 * 设置下拉选
 */
function setSelect(name,list,str){
    var listStr ="<option value=''>请选择</option>" ;
    for(var i = 0; i < list.length;i++){
        listStr +=  '<option value="'+list[i].value+'">'+list[i].text+'</option>'
    }
    $("[name="+name+"]").html(listStr);
    $("[name="+name+"]").val(str);
}


/**个人信息校验**/
function checkPersonInfo() {
    var marriage=$("[name=marriageSelect]").val(),//婚姻状态
        education=$("[name=education]").val(),//学历
        companyName=$("[name=companyName]").val(),//公司名称
        companyPhone=$("[name=companyPhone]").val(),//公司电话
        companyAddr=$("[name=companyAddr]").attr("data-codes"),//公司地址省市区
        companyPlace=$("[name=companyPlace]").val(),//公司详细地址
        immediateName=$("[name=immediateName]").val(),//直系亲属姓名
        immediatePhone=$("[name=immediatePhone]").val(),//直系亲属手机号
        immediateRel=$("[name=immediateRel]").val(),//直系亲属关系
        otherName=$("[name=otherName]").val(),//其他联系人姓名
        otherPhone=$("[name=otherPhone]").val(),//其他联系人电话
        otherRel=$("[name=otherRel]").val();//其他联系人关系

    /**汉字字母数字校验**/
    var wordReg=/^([a-zA-Z0-9\u4e00-\u9fa5\·]{1,50})$/;
    /**手机号检验**/
    var isPhoneReg=/^1[34578]\d{9}$/;
    /**座机正则**/
    var isTel=/^((0\d{2,3}-\d{7,8})|(1[3584]\d{9}))$/;

    if(!marriage){
        layer.open({content: '婚况不能为空',skin: 'msg',time: 2 });
        return false;
    }
    if(!education){
        layer.open({content: '学历不能为空',skin: 'msg',time: 2 });
        return false;
    }
    if(!companyName){
        layer.open({content: '公司名称不能为空',skin: 'msg',time: 2 });
        return false;
    }else if(!wordReg.test(companyName)){
        layer.open({content: '公司名称格式不正确',skin: 'msg',time: 2 });
        return false;
    }
    if(!companyPhone){
        layer.open({content: '公司电话不能为空',skin: 'msg',time: 2 });
        return false;
    }else if(!isTel.test(companyPhone)){
        layer.open({content: '公司电话格式不正确',skin: 'msg',time: 2 });
        return false;
    }
    if(!companyAddr){
        layer.open({content: '单位地址不能为空',skin: 'msg',time: 2 });
        return false;
    }
    if(!companyPlace){
        layer.open({content: '公司详细地址不能为空',skin: 'msg',time: 2 });
        return false;
    }else if(!wordReg.test(companyPlace)){
        layer.open({content: '公司详细地址格式不正确',skin: 'msg',time: 2 });
        return false;
    }
    if(!immediateName){
        layer.open({content: '直系亲属姓名不能为空',skin: 'msg',time: 2 });
        return false;
    }else if(!wordReg.test(immediateName)){
        layer.open({content: '直系亲属姓名格式不正确',skin: 'msg',time: 2 });
        return false;
    }
    if(!immediatePhone){
        layer.open({content: '直系亲属手机号不能为空',skin: 'msg',time: 2 });
        return false;
    }else if(!isPhoneReg.test(immediatePhone)){
        layer.open({content: '直系亲属手机号格式不正确',skin: 'msg',time: 2 });
        return false;
    }
    if(!immediateRel){
        layer.open({content: '直系亲属关系不能为空',skin: 'msg',time: 2 });
        return false;
    }

    if(!otherName){
        layer.open({content: '其他联系人姓名不能为空',skin: 'msg',time: 2 });
        return false;
    }else if(!wordReg.test(otherName)){
        layer.open({content: '其他联系人姓名格式不正确',skin: 'msg',time: 2 });
        return false;
    }
    if(!otherPhone){
        layer.open({content: '其他联系人手机号不能为空',skin: 'msg',time: 2 });
        return false;
    }else if(!isPhoneReg.test(otherPhone)){
        layer.open({content: '其他联系人手机号格式不正确',skin: 'msg',time: 2 });
        return false;
    }
    if(!otherRel){
        layer.open({content: '其他联系人关系不能为空',skin: 'msg',time: 2 });
        return false;
    }
    return true;
}
/**信息检验提交**/
function updataPersonInfo() {
    var orderId=$("#orderId").val();

    var params= {};
        params.marryStatus = $("[name=marriageSelect]").find("option:selected").text();//婚姻状态名称
        params.marryValue = $("[name=marriageSelect]").val();//婚姻状态值
        params.education = $("[name=education]").find("option:selected").text();//学历名称
        params.educateValue =   $("[name=education]").val();//学历的值
        params.companyName = $("[name=companyName]").val();//公司名称
        params.linkNumber =$("[name=companyPhone]").val();//公司电话
        params.companyAddress = $("[name=companyAddr]").val().replace(new RegExp(" ","g"),"/");//公司地址省市区
        params.addressId = $("[name=companyAddr]").attr("data-codes").replace(new RegExp(',','g'),'/');
        params.detailAddr = $("[name=companyPlace]").val();//公司详细地址
        params.linealRel = $("[name=immediateName]").val();//直系亲属姓名
        params.PersonDirectRelativesPhone = $("[name=immediatePhone]").val();//直系亲属手机号
        params.relationship = $("[name=immediateRel]").find("option:selected").text();
        params.relationValue = $("[name=immediateRel]").val();//直系亲属关系
        params.otherRel = $("[name=otherName]").val();//其他联系人姓名
        params.otherRelativesPhone = $("[name=otherPhone]").val();//其他联系人电话
        params.otherRelationship = $("[name=otherRel]").find("option:selected").text();
        params.relationOtherValue = $("[name=otherRel]").val();//其他联系人关系
        params.linkDirectId = $("[name=linkDirectId]").val();//直系亲属ID
        params.linkOtherId = $("[name=linkOtherId]").val();//其他联系人ID
        params.person_info_complete = "100";
        params.orderId = orderId;

    Comm.ajaxPost("/wechat/basicinfo/addMiaofuBasicInfo",{"data":JSON.stringify(params)},function (sms) {
        window.location=_ctx+"/wechat/basicinfo/accredit?orderId="+orderId;
    });

}