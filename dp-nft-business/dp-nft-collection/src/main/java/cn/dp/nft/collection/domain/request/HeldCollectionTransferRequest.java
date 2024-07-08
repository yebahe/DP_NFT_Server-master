package cn.dp.nft.collection.domain.request;

import cn.dp.nft.collection.domain.constant.HeldCollectionEventType;
import lombok.*;

/**
 * @author wswyb001
 * @date 2024/01/17
 */

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class HeldCollectionTransferRequest extends BaseHeldCollectionRequest {

    /**
     * '买家id'
     */
    private Long buyerId;

    /**
     * '卖家id'
     */
    private Long sellerId;

    @Override
    public HeldCollectionEventType getEventType() {
        return HeldCollectionEventType.TRANSFER;
    }
}
