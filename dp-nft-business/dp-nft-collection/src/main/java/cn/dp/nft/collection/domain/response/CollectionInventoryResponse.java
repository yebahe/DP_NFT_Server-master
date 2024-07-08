package cn.dp.nft.collection.domain.response;

import cn.dp.nft.base.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yebahe
 */
@Getter
@Setter
public class CollectionInventoryResponse extends BaseResponse {

    private String collectionId;

    private String identifier;

    private Integer inventory;
}
