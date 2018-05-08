<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <%@include file ="../../common/input.jsp"%>
    <%@include file ="../../common/taglibs.jsp"%>
    <link rel="stylesheet" href="${ctx}/resources/merch-wechat/css/style.css${version}">
    <link rel="stylesheet" href="${ctx}/resources/merch-wechat/css/contract.css${version}">
    <title>确认合同</title>
    <style>
        .contractContent{
            font-size: 12px;
            margin: 54px 20px;
        }
        .contractContent h1{text-align: center;font-size: 16px;font-weight: bold}
        .contractContent h2{margin-top: 10px;}
        .contentPstyle{margin-top:10px;}
        .rightStyle{float: right}
        .productionInfo{margin: 10px 20px;}
        .productionInfo p{margin-bottom: 10px}
        #payWay,#payAfterDay,#place,#payPerson,#productionPeriods,#periodsMoney,
        #periodsMoney_big,#payDay,#userInfo,#userEmile,#userPhone,#userEmileTwo
        {display:inline-block;padding: 0 10px;border-bottom: 1px solid;}
        .confirmContract{position: fixed;bottom: 0;width:100%;height: 44px;  line-height: 44px;text-align: center;}
        #confirmContractBtn{width: 100%;height: 44px;line-height: 44px;border: none;outline: none;background: #BABECA;color:#fff;font-size: 16px;font-weight: bold;}
        .activeBtn{background: #FFCC00!important;color:#9A6A18!important}
    </style>
</head>
<body>
<div class="contractPage">
    <div class="toolBar">
        <p><span class="icon-back" onclick="history.go(-1)"></span><span>确认合同</span></p>
    </div>
    <div class="contractContent">
        <p>合同编号:<span id="contractNo">${info.contract} </span></p>
        <h1>《分期付款买卖协议》</h1>
        <p class="contentPstyle">甲方(出售方): <span id="contractOwner">${info.merchantName}</span></p>
        <p class="contentPstyle">乙方(购买方/用户): <span id="contractUser">${info.cusName}</span></p>
        <p class="contentPstyle">根据《中华人民共和国合同法》及相关法律法规规定，本着合作共赢、互惠互利的原则，双方经协商一致，达成如下协议：</p>
        <h2>第一条 合作范围</h2>
        <p class="contentPstyle">为全面规范地服务终端客户，根据甲方企业的市场拓展规划，甲方根据乙方的申请和对乙方的需求的审核，同意按照甲方的价格供应商品给乙方，并视甲方的能力进一步为乙方提供相关的技术支持和技术服务。乙方授权甲方及甲方的合作伙伴通过第三方机构对其征信进行查询并代为向有关机构支付相应的服务费用。</p>
        <h2>第二条 标的、数量、价款</h2>
        <div class="productionInfo">
            <p><label><span>商品名称 :</span><span id="productName">${info.merchandiseName}</span></label><label class="rightStyle"><span>单价 :</span><span id="productPrice">${info.price}</span>元</label></p>
            <p><label><span>商品型号 :</span><span id="productModel">${info.model}</span></label><label class="rightStyle"><span>数量 :</span><span id="productCount">${info.weight}</span></label></p>
            <p><label><span>总价 :</span><span id="productAllMoney">${info.price}</span>元</label><label class="rightStyle"><span>合计金额:</span><span id="productAllCount">${info.price}</span>元</label></p>
            <p>（以上表格行数不足时可另附表）</p>
        </div>
        <h2>第三条 交货方式及期限</h2>
        <p class="contentPstyle">双方同意按照如下第<span id="payWay">${info.type}</span>种方式交货：</p>
        <p>1.甲方应在接到乙方以书面方式的发货通知单后<span id="payAfterDay">${info.day}</span>天内送货到指定地点<span id="place">${info.cusAddress}</span>，运费由<span id="payPerson">${info.freight}</span>方承担。</p>
        <p>2.乙方至甲方指定地点自提货物。</p>
        <p>3.其他方式：</p>
        <h2>第四条 验收</h2>
        <p>1．验收方式乙方在甲方交货时当场验收。<br>2．验收内容<br>1）商品是否符合第二条约定。<br>2）商品产品包装贴有标签或者附具说明书。<br>3）标签或者说明书上应当注明商品名称、企业名称、产品批号和产品登记证号等。<br>4）标签或者说明书上应当注明商品的生产厂家、使用方法、生产日期等。<br>5）法律、法规另有规定的，标签或者说明书应当符合规定。</p>
        <h2>第五条 付款方式及期限</h2>
        <p>1.双方同意乙方以分期付款形式向甲方支付商品价款，鉴于分期付款将会给甲方经营造成资金压力及风险，乙方同意甲方的产品单价在市场价格基础上按照一定比例上浮。</p>
        <p>2.分期数共计<span id="productionPeriods">${info.payDay}</span>期，每期支付款项金额为<span id="periodsMoney">${info.repaymentAmount}</span>元（大写：<span id="periodsMoney_big">${info.value}</span>），付款日为每月<span id="payDay">${info.days}</span>日，如当月无此日的则以当月最后一天为付款日。</p>
        <p>3.乙方应于每期付款日当日10：00前将当期应支付款项存入下列专用付款帐号，并同意及授权甲方指定的第三方支付机构代收、代扣乙方应支付的分期款项。<br>乙方专用账户信息：<span id="userInfo">${info.bank_card} ${info.account_bank}</span></p>
        <h2>第六条 甲乙双方权利义务</h2>
        <p>1．甲方权利义务。<br>1）甲方按照第五条约定收取价款；<br>2）甲方应当向乙方说明商品的用途、使用方法；<br>3）为乙方提供所售商品的售后服务相关信息；</p>
        <p class="contentPstyle">2．乙方权利义务。<br>1）乙方应当按照所买商品标签或者说明书注明的事项正确使用商品。<br>2）非因产品质量问题乙方不得退换货。<br>3）乙方应按照第五条约定按期支付价款。</p>
        <h2>第七条 协议终止</h2>
        <p>乙方按协议约定履行全部付款义务的，本协议即终止。如果乙方任何一期价款逾期超过15天的，甲方可随时单方终止，甲方通知一旦发出本协议视作立即终止。甲方有权要求乙方：1）立即结清与甲方的产品全部欠款；2）赔偿甲方因此造成的损失并承担全部违约责任；3）如给第三人造成损失的，自行承担包括退货、维修、索赔等后续服务成本。</p>
        <h2>第八条 违约责任</h2>
        <p>1．商品经验收不符合第二条约定的，乙方有权要求甲方补足、更换、退货。<br>2．乙方迟延支付价款的，应当每日按照迟延部分价款的0.1%向甲方支付违约金。并一并赔偿因违约使得甲方产生的费用和损失，包括但不限于调查、诉讼费、律师费等。<br>3．因商品质量问题给乙方造成损失，乙方向甲方要求赔偿的，甲方应当予以赔偿。商品质量争议由法定产品质量检验机构鉴定。<br>4．当事人一方违约后，对方应当采取适当措施防止损失的扩大；没有采取适当措施致使损失扩大的，不得就扩大的损失要求赔偿。当事人因防止损失扩大而支出的合理费用，由违约方承担。</p>
        <h2>第九条 争议解决及文书的送达</h2>
        <p>1．双方因本合同引起的或与本合同有关的争议，均同意提请中国广州仲裁委员会, 按照申请仲裁时该会现行有效的网络仲裁规则进行网络仲裁，并进行书面审理。仲裁裁决是终局的，对双方均有约束力。</p>
        <p>2．乙方确认以<span id="userEmile">  </span>为联络邮箱，为双方之间及涉诉纠纷相关材料送达地址，并以<span id="userPhone"> </span>为联络手机号码，为短信通知号码。甲方确认以<span id="userEmileTwo">${info.email}</span>为联络邮箱，为双方之间及涉诉纠纷相关材料送达地址。按本合同约定由任何一方发给其他方的任何通知，应以电子邮件或者短信等形式发出，送至约定邮箱或者手机号码。收件人指定系统接受材料或通知之日，即视为送达和收到之日。如一方需变更联络邮箱或者手机号码的，应当书面通知并得到对方确认。</p>
        <h2>第十条 协议生效及其它</h2>
        <p>1.合同经双方签字（盖章）之日起生效。<br>2.本协议的任何修改、补充均须以书面形式作出。本协议的补充协议及附件与本协议具有同等法律效力。<br>3.本协议自签订之日起至商品价款全部清偿之日止， 乙方有义务在下列信息变更后的三日内以书面方式提供更新版的信息给甲方，包括但不限于乙方的联系地址、电话、联系人手机号码、电子邮箱的变更等， 若因乙方不及时提供上述变更信息而造成的法律后果由乙方承担。<br>4.如果本协议中的任何一条或多条违法适用的法律，则该条将被视为无效，但该无效条款并不影响本协议其他条款的效力。<br>5.本协议一式贰份，双方各保留壹份，具有同等法律效力。</p>
        <p class="contentPstyle">（以下无正文，为双方签章部分）</p>
        <p class="contentPstyle">供应方（甲方）：${info.merchantName}</p>
        <p>邮箱：<span id="contractTenantMail">${info.email}</span></p>
        <p>联系电话：<span id="contractTenantTel">${info.tel}</span></p>
        <p>签订时间：<span id="contractTenantTime">${info.year}年${info.month}月${info.days}日</span></p>
        <p class="contentPstyle">购买方（乙方）: <span id="userName">${info.cusName}</span></p>
        <p class="contentPstyle">身份证号：<span id="userCardId">${info.cusCard}</span></p>
        <p>地址（住址）：<span id="contractPlace">${info.cusAddress}</span></p>
        <p>联系电话：<span id="contractTel">${info.cusPhone}</span></p>
        <p>电子邮箱：<span id="contractMail"> </span></p>
        <p>签订时间：<span id="contractTime">${info.year}年${info.month}月${info.days}日</span></p>
    </div>
    <div class="confirmContract">
        <button id="confirmContractBtn" class="">阅读并同意</button>
    </div>
</div>
<script>
    $(document).ready(function () {
        var timeNum=5;
        var timer=setInterval(function () {
            if(timeNum==0){
                clearInterval(timer);
                timer=null;
                $("#confirmContractBtn").html("阅读并同意").addClass("activeBtn");
            }else{
                $("#confirmContractBtn").html("阅读并同意("+timeNum+")");
                timeNum--;
            }
        },1000)
        $("#confirmContractBtn").click(function () {
            window.location=_ctx+"/contractConfirmation/getLoanGreementResult?pdfPath=${info.pdfUrl}&orderId=${info.orderId}";
        })
    })
</script>
</body>
</html>