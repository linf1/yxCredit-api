/**
 * Created by Administrator on 2017/10/30 0030.
 */
var completeMap;
//获得订单信息接口
$(function () {
    var orderId=$("#orderId").val();
    Comm.ajaxPost("/wechat/order/getOrderSchedule",{"orderId":orderId},function (sms) {
        completeMap=sms.retData.completeMap;
        //头部信息
        var titleMap = sms.retData.titleMap;
        //订单状态标题
        var progressList =sms.retData.progressList;
        //订单进度消息
        var detailList = sms.retData.detailList;
        completeMap.orderId=detailList[0].order_id;
        /**'0未提交;1借款申请;2自动化审批通过;3自动化审批拒绝;4自动化审批异常转人工;5人工审批通过;6人工审批拒绝;7合同确认;8放款成功;9结清'***/
        $("#wechat-forlending-amount").html(titleMap.amount);
        $("#wechat-forlending-periods").html(titleMap.periods);
        var stateText = titleMap.state;
        var orderLoanState="";
        switch (stateText){
            case '0':
                orderLoanState = '未提交';
                break;
            case '1':
                orderLoanState = '借款申请';
                break;
            case '2':
                orderLoanState = '自动化审批通过';
                break;
            case '3':
                orderLoanState = '自动化审批拒绝';
                break;
            case '4':
                orderLoanState = '自动化审批异常转人工';
                break;
            case '5':
                orderLoanState = '人工审批通过';
                break;
            case '6':
                orderLoanState = '人工审批拒绝';
                break;
            case '7':
                orderLoanState = '合同确认';
                break;
            case '8':
                orderLoanState = '放款成功';
                break;
            case '9':
                orderLoanState = '结清';
                break;
            case '10':
                orderLoanState = '关闭';
                break;
        }
        $("#wechat-forlending-tache").html(orderLoanState);
        var dataListTitle="";
        for(var i=progressList.length-1;i>=0;i--){
            dataListTitle+='<div class="orderStateContent" id="'+progressList[i].value+'">' +
                '<p style="font-weight: bold;"><span class="current_up"></span><span>'+progressList[i].text+'</span><span class="right"></span></p>' +
                '<div class="orderStateLine"></div></div>';
        }
        $(".orderStateContainer").html(dataListTitle);

        for(var j=0;j<detailList.length;j++){
            var time=timeFormat(detailList[j].creat_time);
            var tempHtml='';
            if(detailList[j].order_state=="0"){
                if(detailList[j].update_state=="1"){//有按钮
                    tempHtml+='<p style="font-weight: bold;"><span class="current"></span><span>'+detailList[j].title+'</span><span class="right">'+time+'</span></p>' +
                        '<div class="orderStateLine"><p>'+detailList[j].content+'</p><p  style="color:#FF6633" onclick="goToPage()">继续申请</p></div>';
                }else{
                    tempHtml+='<p style="font-weight: bold;"><span class="current_down"></span><span>'+detailList[j].title+'</span><span class="right">'+time+'</span></p>' +
                        '<div class="orderStateLine"><p>'+detailList[j].content+'</p></div>';
                }
            }else{
                if(detailList[j].update_state=="0"){//不可点击 已过
                    tempHtml+='<p style="font-weight: bold;"><span class="current_down"></span><span>'+detailList[j].title+'</span><span class="right">'+time+'</span></p>' +
                        '<div class="orderStateLine"><p>'+detailList[j].content+'</p></div>';
                }else{
                    tempHtml+='<p style="font-weight: bold;"><span class="current"></span><span>'+detailList[j].title+'</span><span class="right">'+time+'</span></p>' +
                        '<div class="orderStateLine"><p>'+detailList[j].content+'</p></div>';
                }
            }
            $("#"+detailList[j].order_state).html(tempHtml);
        }
    })
    function timeFormat(t) {
        return t.substring(0,4)+"-"+t.substring(4,6)+"-"+t.substring(6,8)+" "+t.substring(8,10)+":"+t.substring(10,12)
    }
})
function goToPage() {
    if(completeMap.identity_complete!="100"){window.location=_ctx+"/wechat/home/identityPage?orderId="+completeMap.orderId}
    if(completeMap.person_info_complete!="100"){window.location=_ctx+"/wechat/identity/personInfoPage?orderId="+completeMap.orderId}
    if(completeMap.authorization_complete!="100"){window.location=_ctx+"/wechat/basicinfo/accredit?orderId="+completeMap.orderId}
    if(completeMap.identity_complete=="100"&&completeMap.person_info_complete=="100"&&completeMap.authorization_complete=="100"){window.location=_ctx+"/wechat/basicinfo/accredit?orderId="+completeMap.orderId};
}

