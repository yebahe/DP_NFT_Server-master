package cn.dp.nft.order.infrastructure.mapper;

import cn.dp.nft.order.domain.entity.TradeOrderStream;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单流水Mapper
 *
 * @author yebahe
 */
@Mapper
public interface OrderStreamMapper extends BaseMapper<TradeOrderStream> {

    /**
     * 根据流标识查询
     *
     * @param streamIdentifier
     * @param streamType
     * @param orderId
     * @return
     */
    TradeOrderStream selectByIdentifier(String streamIdentifier, String streamType, String orderId);
}
