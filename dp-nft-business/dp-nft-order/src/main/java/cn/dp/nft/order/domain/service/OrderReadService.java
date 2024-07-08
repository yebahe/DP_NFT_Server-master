package cn.dp.nft.order.domain.service;

import cn.dp.nft.order.infrastructure.mapper.OrderMapper;
import cn.dp.nft.api.order.constant.TradeOrderState;
import cn.dp.nft.order.domain.entity.TradeOrder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.Date;

/**
 * @author yebahe
 */
@Service
public class OrderReadService extends ServiceImpl<OrderMapper, TradeOrder> {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 按照订单号查询订单信息
     *
     * @param orderId
     * @return
     */
    public TradeOrder getOrder(String orderId) {
        return orderMapper.selectByOrderId(orderId);
    }

    public TradeOrder getOrder(String orderId, String buyerId) {
        return orderMapper.selectByOrderIdAndBuyer(orderId, buyerId);
    }

    public Page<TradeOrder> pageQueryByState(String buyerId, String state, int currentPage, int pageSize) {
        Page<TradeOrder> page = new Page<>(currentPage, pageSize);
        QueryWrapper<TradeOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("buyer_id", buyerId);
        if (state != null) {
            wrapper.eq("order_state", state);
        }
        wrapper.orderBy(true, false, "gmt_create");

        return this.page(page, wrapper);
    }

    /**
     * 分页查询已经超时的订单
     *
     * @param currentPage
     * @param pageSize
     * @param buyerIdTailNumber 买家 ID 的尾号
     * @return
     */
    public Page<TradeOrder> pageQueryTimeoutOrders(int currentPage, int pageSize, @Nullable String buyerIdTailNumber) {
        Page<TradeOrder> page = new Page<>(currentPage, pageSize);

        QueryWrapper<TradeOrder> wrapper = new QueryWrapper<>();
        wrapper.in("order_state", TradeOrderState.CONFIRM.name(), TradeOrderState.CREATE.name());
        wrapper.lt("gmt_create", DateUtils.addMinutes(new Date(), -TradeOrder.DEFAULT_TIME_OUT_MINUTES));
        if (buyerIdTailNumber != null) {
            wrapper.likeLeft("buyer_id", buyerIdTailNumber);
        }
        wrapper.orderBy(true, true, "gmt_create");

        return this.page(page, wrapper);
    }

    /**
     * 分页查询待Confirm订单
     *
     * @param currentPage
     * @param pageSize
     * @param buyerIdTailNumber
     * @return
     */
    public Page<TradeOrder> pageQueryNeedConfirmOrders(int currentPage, int pageSize, @Nullable String buyerIdTailNumber) {
        Page<TradeOrder> page = new Page<>(currentPage, pageSize);

        QueryWrapper<TradeOrder> wrapper = new QueryWrapper<>();
        wrapper.isNull("order_confirmed_time");
        wrapper.orderBy(true, true, "gmt_create");
        if (buyerIdTailNumber != null) {
            wrapper.likeLeft("buyer_id", buyerIdTailNumber);
        }

        return this.page(page, wrapper);
    }

}
