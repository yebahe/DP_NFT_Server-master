package cn.dp.nft.collection.domain.request;

import cn.dp.nft.collection.domain.constant.HeldCollectionEventType;
import lombok.*;

/**
 * @author yebahe
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class HeldCollectionActiveRequest extends BaseHeldCollectionRequest {

    /**
     * 'nftId'
     */
    private String nftId;

    /**
     * 'txHash'
     */
    private String txHash;

    @Override
    public HeldCollectionEventType getEventType() {
        return HeldCollectionEventType.ACTIVE;
    }
}
