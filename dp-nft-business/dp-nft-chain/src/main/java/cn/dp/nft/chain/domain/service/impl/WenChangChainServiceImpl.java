package cn.dp.nft.chain.domain.service.impl;

import cn.dp.nft.api.chain.constant.ChainOperateBizTypeEnum;
import cn.dp.nft.api.chain.constant.ChainOperateTypeEnum;
import cn.dp.nft.api.chain.constant.ChainType;
import cn.dp.nft.api.chain.request.ChainProcessRequest;
import cn.dp.nft.api.chain.request.ChainQueryRequest;
import cn.dp.nft.api.chain.response.ChainProcessResponse;
import cn.dp.nft.api.chain.response.data.ChainCreateData;
import cn.dp.nft.api.chain.response.data.ChainOperationData;
import cn.dp.nft.api.chain.response.data.ChainResultData;
import cn.dp.nft.base.exception.RepoErrorCode;
import cn.dp.nft.base.exception.SystemException;
import cn.dp.nft.chain.domain.constant.ChainCodeEnum;
import cn.dp.nft.chain.domain.constant.ChainOperateStateEnum;
import cn.dp.nft.chain.domain.constant.ChainStateEnum;
import cn.dp.nft.chain.domain.constant.WenChangChainConfiguration;
import cn.dp.nft.chain.domain.entity.*;
import cn.dp.nft.chain.domain.response.ChainResponse;
import cn.dp.nft.chain.domain.service.AbstractChainService;
import cn.dp.nft.chain.infrastructure.utils.WenChangChainUtils;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

import static cn.dp.nft.api.common.constant.CommonConstant.APP_NAME_UPPER;
import static cn.dp.nft.api.common.constant.CommonConstant.SEPARATOR;

/**
 * 文昌链服务
 *
 * @author yebahe
 */
@Service("wenChangChainService")
@Slf4j
public class WenChangChainServiceImpl extends AbstractChainService {

    @Autowired
    protected WenChangChainConfiguration wenChangChainConfiguration;

    @Override
    public ChainProcessResponse<ChainCreateData> createAddr(ChainProcessRequest chainProcessRequest) {
        chainProcessRequest.setBizId(chainProcessRequest.getUserId());
        chainProcessRequest.setBizType(ChainOperateBizTypeEnum.USER.name());
        WenChangCreateBody body = new WenChangCreateBody();
        body.setName(APP_NAME_UPPER + SEPARATOR + ChainOperateBizTypeEnum.USER + SEPARATOR + chainProcessRequest.getUserId());
        body.setOperationId(chainProcessRequest.getIdentifier());
        String path = "/v3/account";

        Long currentTime = System.currentTimeMillis();
        String signature = WenChangChainUtils.signRequest(path, body, currentTime, wenChangChainConfiguration.apiSecret());
        ChainProcessResponse response = doPostExecute(chainProcessRequest, ChainOperateTypeEnum.USER_CREATE, chainRequest -> chainRequest.build(body, path, signature, wenChangChainConfiguration.host(), currentTime));

        if (response.getSuccess() && response.getData() != null) {
            ChainOperateInfo chainOperateInfo = chainOperateInfoService.queryByOutBizId(chainProcessRequest.getBizId(), chainProcessRequest.getBizType(),
                    chainProcessRequest.getIdentifier());
            boolean updateResult = chainOperateInfoService.updateResult(chainOperateInfo.getId(),
                    ChainOperateStateEnum.SUCCEED.name(),
                    null);
        }
        return response;
    }

    @Override
    public ChainProcessResponse<ChainOperationData> chain(ChainProcessRequest chainProcessRequest) {
        WenChangChainBody body = new WenChangChainBody();
        body.setName(chainProcessRequest.getClassName());
        body.setOperationId(chainProcessRequest.getIdentifier());
        body.setClassId("nft" + chainProcessRequest.getClassId());
        body.setOwner(wenChangChainConfiguration.chainAddrSuper());
        String path = "/v3/native/nft/classes";

        Long currentTime = System.currentTimeMillis();
        String signature = WenChangChainUtils.signRequest(path, body, currentTime,
                wenChangChainConfiguration.apiSecret());

        return doPostExecute(chainProcessRequest, ChainOperateTypeEnum.COLLECTION_CHAIN, chainRequest -> chainRequest.build(body, path, signature, wenChangChainConfiguration.host(), currentTime));
    }

    @Override
    public ChainProcessResponse<ChainOperationData> mint(ChainProcessRequest chainProcessRequest) {
        WenChangMintBody body = new WenChangMintBody();
        body.setName(chainProcessRequest.getClassName() + "#" + chainProcessRequest.getSerialNo());
        body.setRecipient(chainProcessRequest.getRecipient());
        body.setOperationId(chainProcessRequest.getIdentifier());
        String path = "/v3/native/nft/nfts/" + "nft" + chainProcessRequest.getClassId();

        Long currentTime = System.currentTimeMillis();
        String signature = WenChangChainUtils.signRequest(path, body, currentTime,
                wenChangChainConfiguration.apiSecret());

        return doPostExecute(chainProcessRequest, ChainOperateTypeEnum.COLLECTION_MINT, chainRequest -> chainRequest.build(body, path, signature, wenChangChainConfiguration.host(), currentTime));

    }

    @Override
    public ChainProcessResponse<ChainOperationData> transfer(ChainProcessRequest chainProcessRequest) {
        WenChangTransferBody body = new WenChangTransferBody();
        body.setRecipient(chainProcessRequest.getRecipient());
        body.setOperationId(chainProcessRequest.getIdentifier());
        String path = "/v3/native/nft/nft-transfers/" + "nft" + chainProcessRequest.getClassId() + "/"
                + chainProcessRequest.getOwner() + "/" + chainProcessRequest.getNtfId();

        Long currentTime = System.currentTimeMillis();
        String signature = WenChangChainUtils.signRequest(path, body, currentTime,
                wenChangChainConfiguration.apiSecret());

        return doPostExecute(chainProcessRequest, ChainOperateTypeEnum.COLLECTION_TRANSFER, chainRequest -> chainRequest.build(body, path, signature, wenChangChainConfiguration.host(), currentTime));

    }

    @Override
    public ChainProcessResponse<ChainOperationData> destroy(ChainProcessRequest chainProcessRequest) {
        WenChangRequestBody body = new WenChangRequestBody();
        body.setOperationId(chainProcessRequest.getIdentifier());
        String path = "/v3/native/nft/nfts/" + "nft" + chainProcessRequest.getClassId() + "/"
                + chainProcessRequest.getOwner() + "/" + chainProcessRequest.getNtfId();

        Long currentTime = System.currentTimeMillis();
        String signature = WenChangChainUtils.signRequest(path, body, currentTime,
                wenChangChainConfiguration.apiSecret());

        return doDeleteExecute(chainProcessRequest, chainRequest -> chainRequest.build(body, path, signature, wenChangChainConfiguration.host(), currentTime));
    }

    @Override
    public ChainProcessResponse<ChainResultData> queryChainResult(ChainQueryRequest chainQueryRequest) {

        var operateInfoId = chainOperateInfoService.insertInfo(ChainType.WEN_CHANG.name(),
                chainQueryRequest.getOperationInfoId(), ChainOperateBizTypeEnum.CHAIN_OPERATION.name(), ChainOperateTypeEnum.COLLECTION_QUERY.name(),
                JSON.toJSONString(chainQueryRequest),chainQueryRequest.getOperationId());

        String path = "/v3/native/tx/" + chainQueryRequest.getOperationId();
        Long currentTime = System.currentTimeMillis();
        String signature = WenChangChainUtils.signRequest(path, null, currentTime,
                wenChangChainConfiguration.apiSecret());

        ChainRequest chainRequest = new ChainRequest();
        chainRequest.build(null, path, signature, wenChangChainConfiguration.host(), currentTime);

        ChainResponse result = doGetQuery(chainRequest);
        log.info("wen chang query result:{}", result);

        boolean updateResult = chainOperateInfoService.updateResult(operateInfoId,
                ChainOperateStateEnum.SUCCEED.name(),
                result.getSuccess() ? result.getData().toString() : result.getError().toString());

        if (!updateResult) {
            throw new SystemException(RepoErrorCode.UPDATE_FAILED);
        }

        ChainProcessResponse<ChainResultData> response = new ChainProcessResponse<>();
        response.setSuccess(result.getSuccess());
        response.setResponseCode(result.getResponseCode());
        response.setResponseMessage(result.getResponseMessage());
        if (result.getSuccess()) {
            String txHash = result.getData().getString("tx_hash");
            String status = result.getData().getString("status");
            var nft = (HashMap) result.getData().get("nft");
            String nftId = (String) nft.get("id");

            ChainResultData data = new ChainResultData();
            data.setTxHash(txHash);
            data.setNftId(nftId);
            switch (status) {
                case "0":
                    data.setState(ChainStateEnum.PROCESSING.name());
                    break;
                case "1":
                    data.setState(ChainStateEnum.SUCCEED.name());
                    break;
                case "2":
                    data.setState(ChainStateEnum.FAILED.name());
                    break;
                case "3":
                    data.setState(ChainStateEnum.INIT.name());
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + status);
            }
            response.setData(data);
        }
        return response;

    }

    @Override
    protected ChainResponse doPost(ChainRequest chainRequest) {

        HttpRequest post = HttpRequest.post(chainRequest.getHost() + chainRequest.getPath());
        post.addHeaders(WenChangChainUtils.configureHeaders(chainRequest.getSignature(), chainRequest.getCurrentTime(),
                wenChangChainConfiguration.apiKey()));
        post.body(JSON.toJSONString(chainRequest.getBody()));

        String responseStr = "";
        ChainResponse result = new ChainResponse();
        try {
            responseStr = post.execute().body();
        } catch (Exception e) {
            log.error("http client error, request: {}, response: {}", chainRequest, responseStr);
            result.setResponseCode(ChainCodeEnum.CHAIN_POST_ERROR.name());
            result.setResponseMessage(e.getMessage());
            result.setSuccess(false);
            return result;
        }
        return JSON.parseObject(responseStr, ChainResponse.class);
    }

    @Override
    protected ChainResponse doDelete(ChainRequest chainRequest) {

        HttpRequest delete = HttpRequest.delete(chainRequest.getHost() + chainRequest.getPath());
        delete.addHeaders(WenChangChainUtils.configureHeaders(chainRequest.getSignature(), chainRequest.getCurrentTime(),
                wenChangChainConfiguration.apiKey()));
        delete.body(JSON.toJSONString(chainRequest.getBody()));

        String responseStr = "";
        ChainResponse result = new ChainResponse();
        try {
            responseStr = delete.execute().body();
        } catch (Exception e) {
            log.error("http client error, request: {}, response: {}", chainRequest, responseStr);
            result.setResponseCode(ChainCodeEnum.CHAIN_POST_ERROR.name());
            result.setResponseMessage(e.getMessage());
            result.setSuccess(false);
            return result;
        }
        return JSON.parseObject(responseStr, ChainResponse.class);
    }

    @Override
    protected ChainResponse doGetQuery(ChainRequest chainRequest) {

        HttpRequest get = HttpRequest.get(chainRequest.getHost() + chainRequest.getPath());
        get.addHeaders(WenChangChainUtils.configureHeaders(chainRequest.getSignature(), chainRequest.getCurrentTime(),
                wenChangChainConfiguration.apiKey()));

        String responseStr = "";
        ChainResponse result = new ChainResponse();
        try {
            responseStr = get.execute().body();
        } catch (Exception e) {
            log.error("http client error, request: {}, response: {}", chainRequest, responseStr);
            result.setResponseCode(ChainCodeEnum.CHAIN_POST_ERROR.name());
            result.setResponseMessage(e.getMessage());
            result.setSuccess(false);
            return result;
        }
        return JSON.parseObject(responseStr, ChainResponse.class);
    }

    @Override
    protected String chainType() {
        return ChainType.WEN_CHANG.name();
    }

}
