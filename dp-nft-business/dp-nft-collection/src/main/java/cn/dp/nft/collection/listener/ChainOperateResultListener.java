package cn.dp.nft.collection.listener;

import cn.dp.nft.collection.exception.CollectionErrorCode;
import cn.dp.nft.collection.exception.CollectionException;
import cn.dp.nft.api.chain.model.ChainOperateBody;
import cn.dp.nft.api.chain.response.data.ChainResultData;
import cn.dp.nft.api.collection.constant.CollectionStateEnum;
import cn.dp.nft.api.collection.constant.HeldCollectionState;
import cn.dp.nft.collection.domain.entity.Collection;
import cn.dp.nft.collection.domain.entity.HeldCollection;
import cn.dp.nft.collection.domain.request.HeldCollectionActiveRequest;
import cn.dp.nft.collection.domain.service.CollectionService;
import cn.dp.nft.collection.domain.service.impl.HeldCollectionService;
import cn.dp.nft.stream.param.MessageBody;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.function.Consumer;

/**
 * 链操作结果监听器
 *
 * @author yebahe
 */
@Slf4j
@Component
public class ChainOperateResultListener {

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private HeldCollectionService heldCollectionService;

    @Bean
    Consumer<Message<MessageBody>> chain() {
        return msg -> {
            String messageId = msg.getHeaders().get("ROCKET_MQ_MESSAGE_ID", String.class);
            String tag = msg.getHeaders().get("ROCKET_TAGS", String.class);
            ChainOperateBody chainOperateBody = JSON.parseObject(msg.getPayload().getBody(), ChainOperateBody.class);
            log.info("messageId:{},chainOperateBody:{}，tag:{}", messageId, chainOperateBody, tag);
            //更新相关业务表
            ChainResultData chainResultData = chainOperateBody.getChainResultData();
            //成功情况处理
            switch (chainOperateBody.getOperateType()) {
                case COLLECTION_CHAIN:
                    //藏品上链成功更新,只有一个txHash
                    Collection collection = collectionService.getById(chainOperateBody.getBizId());
                    if (null == collection) {
                        throw new CollectionException(CollectionErrorCode.COLLECTION_QUERY_FAIL);
                    }
                    collection.setState(CollectionStateEnum.SUCCEED);
                    collection.setSyncChainTime(new Date());
                    collectionService.updateById(collection);
                    break;
                case COLLECTION_MINT:
                    HeldCollectionActiveRequest request = new HeldCollectionActiveRequest();
                    request.setHeldCollectionId(Long.valueOf(chainOperateBody.getBizId()));
                    request.setIdentifier(chainOperateBody.getOperateInfoId().toString());
                    request.setNftId(chainResultData.getNftId());
                    request.setTxHash(chainResultData.getTxHash());
                    boolean result = heldCollectionService.active(request);
                    Assert.isTrue(result, "active held collection failed");
                    break;
                case COLLECTION_TRANSFER:
                    //藏品铸造成功有nftId和txHash
                    HeldCollection transferCollection = heldCollectionService.queryByNftIdAndState(chainOperateBody.getBizId(),
                            HeldCollectionState.INIT.name());
                    if (null == transferCollection) {
                        throw new CollectionException(CollectionErrorCode.HELD_COLLECTION_QUERY_FAIL);
                    }
                    transferCollection.actived(chainResultData.getNftId(), chainResultData.getTxHash());
                    heldCollectionService.updateById(transferCollection);
                    break;
                case COLLECTION_DESTROY:
                    //藏品铸造成功有nftId和txHash
                    HeldCollection destroyCollection = heldCollectionService.queryByNftIdAndState(chainOperateBody.getBizId(),
                            HeldCollectionState.ACTIVED.name());
                    if (null == destroyCollection) {
                        throw new CollectionException(CollectionErrorCode.HELD_COLLECTION_QUERY_FAIL);
                    }
                    destroyCollection.destroy();
                    heldCollectionService.updateById(destroyCollection);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + chainOperateBody.getBizType().name());

            }
        };
    }

}
