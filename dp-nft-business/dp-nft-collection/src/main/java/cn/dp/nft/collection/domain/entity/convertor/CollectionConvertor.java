package cn.dp.nft.collection.domain.entity.convertor;

import cn.dp.nft.collection.domain.entity.Collection;
import cn.dp.nft.api.collection.constant.CollectionStateEnum;
import cn.dp.nft.api.collection.constant.CollectionVoState;
import cn.dp.nft.api.collection.model.CollectionVO;
import cn.dp.nft.api.collection.request.CollectionChainRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * @author yebahe
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CollectionConvertor {

    CollectionConvertor INSTANCE = Mappers.getMapper(CollectionConvertor.class);

    public static final int DEFAULT_MIN_SALE_TIME = 60;

    /**
     * 转换为VO
     *
     * @param request
     * @return
     */
    @Mapping(target = "inventory", source = "request.saleableInventory")
    @Mapping(target = "state", ignore = true)
    public CollectionVO mapToVo(Collection request);

    /**
     * 转换为实体
     *
     * @param request
     * @return
     */
    public Collection mapToEntity(CollectionChainRequest request);

    /**
     * 转换为VO
     *
     * @param request
     * @return
     */
    public List<CollectionVO> mapToVo(List<Collection> request);

    /**
     * 状态映射
     *
     * @param collection
     * @return
     */
    default CollectionVoState transState(Collection collection) {

        if (collection.getState().equals(CollectionStateEnum.INIT) || collection.getState().equals(CollectionStateEnum.REMOVED)) {
            return CollectionVoState.NOT_FOR_SALE;
        }

        Instant now = Instant.now();

        if (now.compareTo(collection.getSaleTime().toInstant()) >= 0) {
            if (collection.getSaleableInventory() > 0) {
                return CollectionVoState.SELLING;
            } else {
                return CollectionVoState.SOLD_OUT;
            }
        } else {
            if (ChronoUnit.MINUTES.between(now, collection.getSaleTime().toInstant()) > DEFAULT_MIN_SALE_TIME) {
                return CollectionVoState.WAIT_FOR_SALE;
            }
            return CollectionVoState.COMING_SOON;
        }
    }
}
