package cn.dp.nft.api.collection.response;

import cn.dp.nft.base.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wswyb001
 */
@Getter
@Setter
public class CollectionSaleResponse extends BaseResponse {
    /**
     * 持有藏品id
     */
    private Long heldCollectionId;

}
