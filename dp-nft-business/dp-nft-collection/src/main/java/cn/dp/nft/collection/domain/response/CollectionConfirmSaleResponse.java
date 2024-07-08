package cn.dp.nft.collection.domain.response;

import cn.dp.nft.base.response.BaseResponse;
import cn.dp.nft.collection.domain.entity.Collection;
import cn.dp.nft.collection.domain.entity.HeldCollection;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yebahe
 */
@Setter
@Getter
public class CollectionConfirmSaleResponse extends BaseResponse {

    private Collection collection;

    private HeldCollection heldCollection;
}
