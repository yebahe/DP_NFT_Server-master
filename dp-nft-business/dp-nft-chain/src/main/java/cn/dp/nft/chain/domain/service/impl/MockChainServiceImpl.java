package cn.dp.nft.chain.domain.service.impl;

import cn.dp.nft.api.chain.constant.ChainOperateTypeEnum;
import cn.dp.nft.api.chain.constant.ChainType;
import cn.dp.nft.api.chain.request.ChainProcessRequest;
import cn.dp.nft.api.chain.request.ChainQueryRequest;
import cn.dp.nft.api.chain.response.ChainProcessResponse;
import cn.dp.nft.api.chain.response.data.ChainCreateData;
import cn.dp.nft.api.chain.response.data.ChainOperationData;
import cn.dp.nft.api.chain.response.data.ChainResultData;
import cn.dp.nft.chain.domain.constant.ChainCodeEnum;
import cn.dp.nft.chain.domain.constant.ChainStateEnum;
import cn.dp.nft.chain.domain.entity.ChainRequest;
import cn.dp.nft.chain.domain.response.ChainResponse;
import cn.dp.nft.chain.domain.service.AbstractChainService;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * mock链服务
 *
 * @author yebahe
 */
@Service("mockChainService")
public class MockChainServiceImpl extends AbstractChainService {

    @Override
    public ChainProcessResponse<ChainCreateData> createAddr(ChainProcessRequest request) {
        return new ChainProcessResponse.Builder().responseCode(ChainCodeEnum.SUCCESS.name()).data(
                new ChainCreateData(request.getIdentifier(), UUID.randomUUID().toString(), "mockBlockChainName",
                        ChainType.MOCK.name())).buildSuccess();
    }

    @Override
    public ChainProcessResponse<ChainOperationData> mint(ChainProcessRequest request) {
        return doPostExecute(request, ChainOperateTypeEnum.COLLECTION_CHAIN, chainRequest -> {
        });
    }

    @Override
    public ChainProcessResponse<ChainOperationData> chain(ChainProcessRequest chainProcessRequest) {
        return doPostExecute(chainProcessRequest, ChainOperateTypeEnum.COLLECTION_CHAIN, chainRequest -> {
        });
    }

    @Override
    public ChainProcessResponse<ChainOperationData> transfer(ChainProcessRequest request) {
        return new ChainProcessResponse.Builder().responseCode("200").responseMessage("SUCCESS").buildSuccess();
    }

    @Override
    public ChainProcessResponse<ChainOperationData> destroy(ChainProcessRequest request) {
        return new ChainProcessResponse.Builder().responseCode("200").responseMessage("SUCCESS").buildSuccess();
    }

    @Override
    public ChainProcessResponse<ChainResultData> queryChainResult(ChainQueryRequest request) {
        ChainProcessResponse<ChainResultData> response = new ChainProcessResponse<>();
        response.setSuccess(true);
        response.setResponseCode("200");
        response.setResponseMessage("SUCCESS");
        ChainResultData data = new ChainResultData();
        data.setTxHash(UUID.randomUUID().toString());
        data.setNftId("nftId");
        data.setState(ChainStateEnum.SUCCEED.name());
        response.setData(data);
        return response;
    }

    @Override
    protected ChainResponse doPost(ChainRequest chainRequest) {
        ChainResponse chainResponse = new ChainResponse();
        chainResponse.setSuccess(true);
        JSONObject data = new JSONObject();
        data.put("success",true);
        data.put("chainType","mock");
        chainResponse.setData(data);
        return chainResponse;
    }

    @Override
    protected ChainResponse doDelete(ChainRequest chainRequest) {
        ChainResponse chainResponse = new ChainResponse();
        chainResponse.setSuccess(true);
        JSONObject data = new JSONObject();
        data.put("success",true);
        data.put("chainType","mock");
        chainResponse.setData(data);
        return chainResponse;
    }

    @Override
    protected ChainResponse doGetQuery(ChainRequest chainRequest) {
        ChainResponse chainResponse = new ChainResponse();
        chainResponse.setSuccess(true);
        JSONObject data = new JSONObject();
        data.put("success",true);
        data.put("chainType","mock");
        chainResponse.setData(data);
        return chainResponse;
    }

    @Override
    protected String chainType() {
        return ChainType.MOCK.name();
    }
}
