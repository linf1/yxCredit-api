function confirmContract() {
    if(!$("#isCheck").is(":checked")){
        layer.open({content: '请勾选已阅读并同意合同',skin: 'msg',time: 2 });
        return;
    }
    upDateContract();
}
$(function () {
    var isChecked=$("#isChecked").val();
    debugger;
    if(isChecked=="true"){//查看合同
        initPageCheck();
        $(".confirmBtnContainer").hide();
        $("#exterlinkframe").attr("height","100%");
    }else{//签订合同
        initPage();
        $(".confirmBtnContainer").show();
        $("#exterlinkframe").attr("height","85%");
    }
})
/**获取待签合同**/
function initPage() {
    Comm.ajaxPost("/contractConfirmation/getContractGreement","",function (data) {
        var ctx1 ="/PDF/web/viewer.html";
        var host = data.retData.host;
        $("#pdfUrl").val(data.retData.pdfUrl);
        $("#orderId").val(data.retData.orderId)
        $("#exterlinkframe").attr("src",_ctx+ctx1+"?file="+_ctx+data.retData.pdfUrl);
    })
}
/**获取已签合同**/
function initPageCheck() {
    var orderId=$("#orderId").val();
    Comm.ajaxPost("/contractConfirmation/getAlreadyLoanGreementResult","orderId="+orderId,function (data) {
        var ctx1 ="/PDF/web/viewer.html";
        var host = data.retData.host;
        $("#pdfUrl").val(data.retData.pdfUrl);
        $("#exterlinkframe").attr("src",_ctx+ctx1+"?file="+_ctx+data.retData.pdfUrl);
    })
}



/**确认合同**/
function upDateContract() {
    var orderId=$("#orderId").val();
    var pdfPath=$("#pdfUrl").val();
    var pamars ={
        orderId:orderId,
        pdfPath:pdfPath
    }
    Comm.ajaxPost("/contractConfirmation/getLoanGreementResult",pamars,function (data) {
        $(".confirmBtnContainer").hide();
        $("#exterlinkframe").attr("height","100%");
        initPageCheck();
        linked.agreeContract();

    })
}