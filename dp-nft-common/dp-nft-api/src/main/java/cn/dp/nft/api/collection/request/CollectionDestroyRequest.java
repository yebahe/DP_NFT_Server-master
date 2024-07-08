package cn.dp.nft.api.collection.request;

import cn.dp.nft.api.collection.constant.CollectionEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wswyb001
 * @date 2024/01/17
 */

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CollectionDestroyRequest extends BaseCollectionRequest {
    /**
     * 幂等号
     */
    private String identifier;

    /**
     * '藏品id'
     */
    private Long collectionId;

    /**
     * '持有藏品id'
     */
    private Long heldCollectionId;


    @Override
    public CollectionEvent getEventType() {
        return CollectionEvent.DESTROY;
    }
}
