package cn.dp.nft.api.collection.response;

import cn.dp.nft.base.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wswyb001
 */
@Getter
@Setter
public class CollectionChainResponse extends BaseResponse {
    /**
     * 藏品id
     */
    private Long collectionId;

}
