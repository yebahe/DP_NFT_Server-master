package cn.dp.nft.pay.infrastructure.mapper;

import cn.dp.nft.pay.domain.entity.PayOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jakarta.validation.constraints.NotNull;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付订单mapper
 *
 * @author yebahe
 */
@Mapper
public interface PayOrderMapper extends BaseMapper<PayOrder> {

    /**
     * 根据bizNo和payer查询
     *
     * @param payerId
     * @param bizNo
     * @param bizType
     * @param payChannel
     * @return
     */
    PayOrder selectByBizNoAndPayer(String payerId, String bizNo, String bizType, String payChannel);

    /**
     * 根据payOrderId查询
     *
     * @param payOrderId
     * @return
     */
    PayOrder selectByPayOrderId(@NotNull String payOrderId);
}
