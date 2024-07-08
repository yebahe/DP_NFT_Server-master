package cn.dp.nft.collection.domain.request;

import cn.dp.nft.base.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yebahe
 */
@Getter
@Setter
public class CollectionInventoryRequest extends BaseRequest {

    @NotNull(message = "collectionId is null")
    private String collectionId;

    private String identifier;

    private Integer inventory;
}
