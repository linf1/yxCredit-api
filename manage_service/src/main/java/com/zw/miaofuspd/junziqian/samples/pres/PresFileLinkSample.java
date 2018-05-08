package com.zw.miaofuspd.junziqian.samples.pres;

import com.junziqian.api.bean.Signatory;
import com.junziqian.api.common.IdentityType;
import com.junziqian.api.request.PresFileLinkRequest;
import com.junziqian.api.response.SignLinkResponse;
import com.junziqian.api.util.LogUtils;
import com.zw.miaofuspd.junziqian.samples.JunziqianClientInit;

import java.util.Map;

/**
 * 保全后的文件下载地址
 * @author yfx
 */
public class PresFileLinkSample extends JunziqianClientInit {
	public static String getJunziqian(Map map) {
		String appKey=map.get("appKey").toString();
		String appSecrete=map.get("appSecrete").toString();
		String services_url=map.get("services_url").toString();
		PresFileLinkRequest request = new PresFileLinkRequest();
		request.setApplyNo(map.get("contractNo").toString());
		Signatory signatory = new Signatory();
		signatory.setFullName(map.get("cusName").toString());
		signatory.setIdentityCard(map.get("cusCard").toString());
		signatory.setSignatoryIdentityType(IdentityType.IDCARD);
		request.setSignatory(signatory);
		SignLinkResponse response = getClient(services_url,appKey,appSecrete).presFileLink(request);
		LogUtils.logResponse(response);
		return response.getLink();
	}


	/*public static void main (String  [] args) {
		String appKey="5e4ff62c16b172f4";
		String appSecrete="8cd497a65e4ff62c16b172f40184e48b";
		String services_url="http://sandbox.api.junziqian.com/services";
		PresFileLinkRequest request = new PresFileLinkRequest();
		request.setApplyNo("APL974169910370508800");
		Signatory signatory = new Signatory();
		signatory.setFullName("张涛");
		signatory.setIdentityCard("342422199202218077");
		signatory.setSignatoryIdentityType(IdentityType.IDCARD);
		request.setSignatory(signatory);
		SignLinkResponse response = getClient(services_url,appKey,appSecrete).presFileLink(request);
		LogUtils.logResponse(response);

	}*/
}
