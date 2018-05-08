<%--*基本信息*--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="infoContent">
    <div class="identityCategory">
       <p>身份类别</p>
       <div class="category">
           <div class="categoryItems">
               <input type="hidden" value="" id="category_work">
               <img id="category_work_img" src="${ctx}/resources/merch-wechat/images/workMan_d.png">
               <p>工薪</p>
           </div>
           <div class="categoryItems">
               <input type="hidden" value="" id="category_boss">
               <img id="category_boss_img"  src="${ctx}/resources/merch-wechat/images/bossMan_d.png">
               <p>个体</p>
           </div>
       </div>
   </div>
    <div class="basicInfoContainer">
        <div class="weui-cells_form">
            <div class="weui-cells">
                <div class="weui-cell">
                    <div class="weui-cell__hd"><label class="weui-label">居住地址</label></div>
                    <div class="weui-cell__bd">
                        <input class="weui-input" type="text" readonly id="liveAddr" name="liveAddr">
                    </div>
                </div>
                <div class="weui-cell">
                    <div class="weui-cell__hd"><label class="weui-label">详细地址</label></div>
                    <div class="weui-cell__bd">
                        <input class="weui-input" type="text" readonly id="livePlace" name="livePlace" maxlength="150">
                    </div>
                </div>
            </div>
            <div class="weui-cells">
                <div class="weui-cell">
                    <div class="weui-cell__hd"><label class="weui-label">单位名称</label></div>
                    <div class="weui-cell__bd">
                        <input class="weui-input" type="text" readonly id="companyName" name="companyName" maxlength="50">
                    </div>
                </div>
                <div class="weui-cell">
                    <div class="weui-cell__hd"><label class="weui-label">单位电话</label></div>
                    <div class="weui-cell__bd">
                        <input class="weui-input" type="text" readonly id="companyPhone" name="companyPhone" maxlength="12">
                    </div>
                </div>
                <div class="weui-cell">
                    <div class="weui-cell__hd"><label class="weui-label">职位名称</label></div>
                    <div class="weui-cell__bd">
                        <input class="weui-input" type="text" id= "posLevel" name="companyPlace" maxlength="50">
                    </div>
                </div>
                <div class="weui-cell">
                    <div class="weui-cell__hd"><label class="weui-label">单位地址</label></div>
                    <div class="weui-cell__bd">
                        <input class="weui-input" type="text" readonly id="companyAddr" name="companyAddr">
                    </div>
                </div>
                <div class="weui-cell">
                    <div class="weui-cell__hd"><label class="weui-label">详细地址</label></div>
                    <div class="weui-cell__bd">
                        <input class="weui-input" type="text" id="companyAddrDetail"  name="companyPlace" maxlength="50">
                    </div>
                </div>
            </div>
        </div>
    </div>
    <%--<div class="commBtn">--%>
        <%--<button>保存</button>--%>
    <%--</div>--%>
</div>