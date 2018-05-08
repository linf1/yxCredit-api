$(function () {
    var height=screen.height;
    $("iframe").css("height",height+"px");
    loadZhimaInfo();
})
function loadZhimaInfo() {
    whir.loading.add("", 1);
    Comm.ajaxPost('/wechat/identity/getPageInfoForSesameSQ', "", function (response) {
        var retData = response.retData;
        if(retData){
            $("#customer_id").val(retData.id);
            $("#customerName").val(retData.person_name);
            $("#customerIdentCard").val(retData.card);
            $("#uesrphonenum").val(retData.tel);
            $("#appHost").val(retData.host);
            var customer_id=retData.id;
            var customerName=retData.person_name;
            var customerIdentCard=retData.card;
            var uesrphonenum=retData.tel;
            var appHost=retData.host;
            getZhiMaPage(customer_id,customerName,customerIdentCard,uesrphonenum,appHost);
        }
        }
    )
}
function getZhiMaPage(customer_id,customerName,customerIdentCard,uesrphonenum,appHost) {
    Comm.ajaxPost('http://phone.51nbapi.com/baiqishi/zmAuthorization.ashx', {
            name: customerName,
            certNo: customerIdentCard,
            mobile:uesrphonenum,
            callbackUrl:appHost+"/wechat/identity/addSesame?customer_id="+customer_id+"&complete=100"
        }, function (response) {
            var resultData = response.resultData;
            if(resultData){
                var authInfoUrl = resultData.authInfoUrl;
                $('iframe').attr("src",authInfoUrl);
            }
        }
    )
}
