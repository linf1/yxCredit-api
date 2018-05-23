package com.zw.miaofuspd.api.contractsign;

import com.api.model.BYXSettings;
import com.api.model.common.BYXRequest;
import com.api.model.common.BYXResponse;
import com.api.model.contractsign.ContractSignRequest;
import com.api.model.contractsign.ContractSignResponse;
import com.api.model.contractsign.ContractSignSettings;
import com.api.service.contractsign.IContractSignService;
import com.zw.api.HttpClientUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * 合同签章API服务实现
 * @author hanwannan
 */
@Service(IContractSignService.BEAN_ID)
public class ContractSignServiceImpl implements IContractSignService {

    private static Logger logger = LoggerFactory.getLogger(ContractSignServiceImpl.class);
    public static final String TOP_TYPE = "ThirdPartner:ContractSignServiceImpl:";

    @Autowired
    private ContractSignSettings contractSignSettings;

    @Autowired
    private BYXSettings byxSettings;

    @Override
    public ContractSignResponse signContract(ContractSignRequest request) throws Exception {
        ContractSignResponse response=new ContractSignResponse();

        File pdfFile = new File(request.getUnsignPath());
        try {
            byte[] unsignPdfStream = FileUtils.readFileToByteArray(pdfFile);
            //赋文件流
            request.setUnsignStream(unsignPdfStream);
            //赋源文件名
            request.setUnsignFileOrignName(pdfFile.getName());
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            //返回结果
            return ContractSignResponse.error("待签署的pdf文件转文件流失败");
        }

        logger.info(TOP_TYPE + "开始启动电子签章客户端调用接口:");
        String result = HttpClientUtil.post(contractSignSettings.getRequestUrl(), ContractSignRequest.getBYXRequest(request, byxSettings),byxSettings.getSignContractHeadRequest());
        return ContractSignResponse.getBYXResponse(result, byxSettings);
    }
}
