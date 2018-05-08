package com.zw.miaofuspd.junziqian.samples.account;

import com.junziqian.api.common.Constants;
import com.junziqian.api.common.FileUtils;
import com.junziqian.api.common.org.OrganizationType;
import com.junziqian.api.request.builder.OrganizationCreateBuilder;
import com.junziqian.api.response.OrganizationCreateResponse;
import com.junziqian.api.util.LogUtils;
import com.zw.miaofuspd.junziqian.samples.JunziqianClientInit;

/**
 * Created by wlinguo on 2016-07-01.
 */
public class AccountCreateSample extends JunziqianClientInit {

    static String img = "d:\\download.jpg";

    public static void main(String[] args) {
        OrganizationCreateBuilder builder = new OrganizationCreateBuilder();
        builder.withEmailOrMobile("wlinguo@mail.bccto.me");
        builder.withName("文林果测试企业");
        builder.withLegalName("");
        builder.withLegalIdentityCard("");
        builder.withLegalMobile("");

        builder.withIdentificationType(Constants.IDENTIFICATION_TYPE_TRADITIONAL);
        builder.withOrganizationType(OrganizationType.ENTERPRISE.getCode());
        builder.withOrganizationRegNo("500903000035444");
        builder.withOrganizationRegImg(FileUtils.uploadFile(img));
        builder.withOrganizationCode("58016467-6");
        builder.withOrganizationCodeImg(FileUtils.uploadFile(img));
        builder.withTaxCertificateImg(FileUtils.uploadFile(img));
        builder.withSignApplication(FileUtils.uploadFile(img));
        builder.withSignImg(FileUtils.uploadFile(img));
        OrganizationCreateResponse response = getClient("http://sandbox.api.junziqian.com/services","5e4ff62c16b172f4","8cd497a65e4ff62c16b172f40184e48b").organizationCreate(builder.build());
        LogUtils.logResponse(response);
    }

}