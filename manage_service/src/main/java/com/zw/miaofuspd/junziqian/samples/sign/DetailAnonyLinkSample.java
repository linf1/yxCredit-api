package com.zw.miaofuspd.junziqian.samples.sign;

import com.junziqian.api.request.DetailAnonyLinkRequest;
import com.zw.miaofuspd.junziqian.samples.JunziqianClientInit;

public class DetailAnonyLinkSample extends JunziqianClientInit {
    public static void main(String[] args) {
    	DetailAnonyLinkRequest request = new DetailAnonyLinkRequest();
        request.setApplyNo("APL930792982683193344");
//        SignLinkResponse response = getClient().detailAnonyLink(request);
//        LogUtils.logResponse(response);
    }
}
