package cn.dp.nft.collection.domain.request;

import cn.dp.nft.collection.domain.constant.HeldCollectionEventType;
import cn.dp.nft.base.request.BaseRequest;
import lombok.*;

/**
 * @author yebahe
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseHeldCollectionRequest extends BaseRequest {
    /**
     * 幂等号
     */
    private String identifier;

    /**
     * '持有藏品id'
     */
    private Long heldCollectionId;

    /**
     * 事件类型
     *
     * @return
     */
    public abstract HeldCollectionEventType getEventType();
}
