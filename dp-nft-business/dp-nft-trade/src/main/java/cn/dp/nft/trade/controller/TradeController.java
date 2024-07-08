package cn.dp.nft.trade.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dp.nft.trade.exception.TradeErrorCode;
import cn.dp.nft.trade.exception.TradeException;
import cn.dp.nft.api.common.constant.BizOrderType;
import cn.dp.nft.api.goods.constant.GoodsType;
import cn.dp.nft.api.goods.model.BaseGoodsVO;
import cn.dp.nft.api.goods.service.GoodsFacadeService;
import cn.dp.nft.api.order.OrderFacadeService;
import cn.dp.nft.api.order.constant.TradeOrderState;
import cn.dp.nft.api.order.model.TradeOrderVO;
import cn.dp.nft.api.order.request.OrderCancelRequest;
import cn.dp.nft.api.order.request.OrderCreateRequest;
import cn.dp.nft.api.order.request.OrderTimeoutRequest;
import cn.dp.nft.api.order.response.OrderResponse;
import cn.dp.nft.api.pay.model.PayOrderVO;
import cn.dp.nft.api.pay.request.PayCreateRequest;
import cn.dp.nft.api.pay.response.PayCreateResponse;
import cn.dp.nft.api.pay.service.PayFacadeService;
import cn.dp.nft.api.user.constant.UserType;
import cn.dp.nft.base.response.SingleResponse;
import cn.dp.nft.base.utils.RemoteCallWrapper;
import cn.dp.nft.trade.param.BuyParam;
import cn.dp.nft.trade.param.CancelParam;
import cn.dp.nft.trade.param.PayParam;
import cn.dp.nft.web.vo.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static cn.dp.nft.api.user.constant.UserType.PLATFORM;
import static cn.dp.nft.web.filter.TokenFilter.tokenThreadLocal;

/**
 * @author yebahe
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("trade")
public class TradeController {

    @Autowired
    private OrderFacadeService orderFacadeService;

    @Autowired
    private PayFacadeService payFacadeService;

    @Autowired
    private GoodsFacadeService goodsFacadeService;

    @RequestMapping("test")
    public String test() {
        return "test";
    }

    /**
     * 下单
     *
     * @param
     * @return 订单号
     */
    @PostMapping("/buy")
    public Result<String> buy(@Valid @RequestBody BuyParam buyParam) {
        String userId = (String) StpUtil.getLoginId();
        //创建订单
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest();
        orderCreateRequest.setIdentifier(tokenThreadLocal.get());
        orderCreateRequest.setBuyerId(userId);
        orderCreateRequest.setGoodsId(buyParam.getGoodsId());
        orderCreateRequest.setGoodsType(GoodsType.valueOf(buyParam.getGoodsType()));
        orderCreateRequest.setItemCount(buyParam.getItemCount());
        BaseGoodsVO goodsVO = goodsFacadeService.getGoods(buyParam.getGoodsId(), GoodsType.valueOf(buyParam.getGoodsType()));
        if (goodsVO == null || !goodsVO.available()) {
            throw new TradeException(TradeErrorCode.GOODS_NOT_FOR_SALE);
        }
        orderCreateRequest.setItemPrice(goodsVO.getPrice());
        orderCreateRequest.setSellerId(goodsVO.getSellerId());
        orderCreateRequest.setGoodsName(goodsVO.getGoodsName());
        orderCreateRequest.setGoodsPicUrl(goodsVO.getGoodsPicUrl());
        orderCreateRequest.setSnapshotVersion(goodsVO.getVersion());
        orderCreateRequest.setOrderAmount(orderCreateRequest.getItemPrice().multiply(new BigDecimal(orderCreateRequest.getItemCount())));

        OrderResponse orderResponse = RemoteCallWrapper.call(req -> orderFacadeService.create(req), orderCreateRequest, "createOrder");

        if (orderResponse.getSuccess()) {
            return Result.success(orderResponse.getOrderId());
        }

        throw new TradeException(TradeErrorCode.ORDER_CREATE_FAILED);
    }

    /**
     * 支付
     *
     * @param
     * @return 支付链接地址
     */
    @PostMapping("/pay")
    public Result<PayOrderVO> pay(@Valid @RequestBody PayParam payParam) {
        String userId = (String) StpUtil.getLoginId();
        SingleResponse<TradeOrderVO> singleResponse = orderFacadeService.getTradeOrder(payParam.getOrderId(), userId);

        TradeOrderVO tradeOrderVO = singleResponse.getData();

        if (tradeOrderVO == null) {
            throw new TradeException(TradeErrorCode.GOODS_NOT_EXIST);
        }

        if (tradeOrderVO.getOrderState() != TradeOrderState.CONFIRM) {
            throw new TradeException(TradeErrorCode.ORDER_IS_CANNOT_PAY);
        }

        if (tradeOrderVO.getTimeout()) {
            doAsyncTimeoutOrder(tradeOrderVO);
            throw new TradeException(TradeErrorCode.ORDER_IS_CANNOT_PAY);
        }

        if (!tradeOrderVO.getBuyerId().equals(userId)) {
            throw new TradeException(TradeErrorCode.PAY_PERMISSION_DENIED);
        }

        PayCreateRequest payCreateRequest = new PayCreateRequest();
        payCreateRequest.setOrderAmount(tradeOrderVO.getOrderAmount());
        payCreateRequest.setBizNo(tradeOrderVO.getOrderId());
        payCreateRequest.setBizType(BizOrderType.TRADE_ORDER);
        payCreateRequest.setMemo(tradeOrderVO.getGoodsName());
        payCreateRequest.setPayChannel(payParam.getPayChannel());
        payCreateRequest.setPayerId(tradeOrderVO.getBuyerId());
        payCreateRequest.setPayerType(tradeOrderVO.getBuyerType());
        payCreateRequest.setPayeeId(tradeOrderVO.getSellerId());
        payCreateRequest.setPayeeType(tradeOrderVO.getSellerType());

        PayCreateResponse payCreateResponse = RemoteCallWrapper.call(req -> payFacadeService.generatePayUrl(req), payCreateRequest, "generatePayUrl");

        if (payCreateResponse.getSuccess()) {
            PayOrderVO payOrderVO = new PayOrderVO();
            payOrderVO.setPayOrderId(payCreateResponse.getPayOrderId());
            payOrderVO.setPayUrl(payCreateResponse.getPayUrl());
            return Result.success(payOrderVO);
        }

        throw new TradeException(TradeErrorCode.PAY_CREATE_FAILED);
    }

    private void doAsyncTimeoutOrder(TradeOrderVO tradeOrderVO) {
        if (tradeOrderVO.getOrderState() != TradeOrderState.CLOSED) {
            Thread.ofVirtual().start(() -> {
                OrderTimeoutRequest cancelRequest = new OrderTimeoutRequest();
                cancelRequest.setOperatorType(PLATFORM);
                cancelRequest.setOperator(PLATFORM.getDesc());
                cancelRequest.setOrderId(tradeOrderVO.getOrderId());
                cancelRequest.setOperateTime(new Date());
                cancelRequest.setIdentifier(UUID.randomUUID().toString());
                orderFacadeService.timeout(cancelRequest);
            });
        }
    }

    /**
     * 取消订单
     *
     * @param
     * @return 是否成功
     */
    @PostMapping("/cancel")
    public Result<Boolean> cancel(@Valid @RequestBody CancelParam cancelParam) {
        String userId = (String) StpUtil.getLoginId();

        OrderCancelRequest orderCancelRequest = new OrderCancelRequest();
        orderCancelRequest.setIdentifier(cancelParam.getOrderId());
        orderCancelRequest.setOperateTime(new Date());
        orderCancelRequest.setOrderId(cancelParam.getOrderId());
        orderCancelRequest.setOperator(userId);
        orderCancelRequest.setOperatorType(UserType.CUSTOMER);

        OrderResponse orderResponse = RemoteCallWrapper.call(req -> orderFacadeService.cancel(req), orderCancelRequest, "cancelOrder");

        if (orderResponse.getSuccess()) {
            return Result.success(true);
        }

        throw new TradeException(TradeErrorCode.ORDER_CANCEL_FAILED);
    }

}
