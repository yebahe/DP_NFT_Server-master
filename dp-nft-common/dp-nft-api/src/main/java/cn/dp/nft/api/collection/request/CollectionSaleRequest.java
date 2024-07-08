package cn.dp.nft.api.collection.request;

import cn.dp.nft.api.collection.constant.CollectionSaleBizType;
import cn.dp.nft.api.collection.constant.CollectionEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @author wswyb001
 * @date 2024/01/17
 */

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CollectionSaleRequest extends BaseCollectionRequest {

    /**
     * 藏品名称
     */
    private String name;

    /**
     * 藏品封面
     */
    private String cover;

    /**
     * 藏品类目id
     */
    private String classId;

    /**
     * 购入价格
     */
    private BigDecimal purchasePrice;

    /**
     * 幂等号
     */
    private String identifier;

    /**
     * '藏品id'
     */
    private Long collectionId;

    /**
     * '持有人id'
     */
    private String userId;

    /**
     * 销售数量
     */
    private Long quantity;

    /**
     * 业务单号
     */
    private String bizNo;

    /**
     * 业务类型
     * @see CollectionSaleBizType
     */
    private String bizType;


    @Override
    public CollectionEvent getEventType() {
        return CollectionEvent.SALE;
    }
}
