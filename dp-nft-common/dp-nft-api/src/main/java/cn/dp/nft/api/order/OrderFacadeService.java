package cn.dp.nft.api.order;

import cn.dp.nft.api.order.model.TradeOrderVO;
import cn.dp.nft.api.order.request.*;
import cn.dp.nft.api.order.response.OrderResponse;
import cn.dp.nft.base.response.PageResponse;
import cn.dp.nft.base.response.SingleResponse;


/**
 * @author yebahe
 */
public interface OrderFacadeService {

    /**
     * 创建订单
     *
     * @param request
     * @return
     */
    public OrderResponse create(OrderCreateRequest request);

    /**
     * 取消订单
     *
     * @param request
     * @return
     */
    public OrderResponse cancel(OrderCancelRequest request);

    /**
     * 订单超时
     *
     * @param request
     * @return
     */
    public OrderResponse timeout(OrderTimeoutRequest request);

    /**
     * 订单确认
     *
     * @param request
     * @return
     */
    public OrderResponse confirm(OrderConfirmRequest request);

    /**
     * 订单支付
     *
     * @param request
     * @return
     */
    public OrderResponse pay(OrderPayRequest request);

    /**
     * 订单详情
     *
     * @param orderId
     * @return
     */
    public SingleResponse<TradeOrderVO> getTradeOrder(String orderId);

    /**
     * 订单详情
     *
     * @param orderId
     * @param userId
     * @return
     */
    public SingleResponse<TradeOrderVO> getTradeOrder(String orderId, String userId);

    /**
     * 订单分页查询
     *
     * @param request
     * @return
     */
    public PageResponse<TradeOrderVO> pageQuery(OrderPageQueryRequest request);
}
