package cn.dp.nft.trade.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dp.nft.api.order.OrderFacadeService;
import cn.dp.nft.api.order.constant.TradeOrderState;
import cn.dp.nft.api.order.model.TradeOrderVO;
import cn.dp.nft.api.order.request.OrderPageQueryRequest;
import cn.dp.nft.api.order.request.OrderTimeoutRequest;
import cn.dp.nft.api.pay.model.PayOrderVO;
import cn.dp.nft.api.pay.service.PayFacadeService;
import cn.dp.nft.base.response.PageResponse;
import cn.dp.nft.base.response.SingleResponse;
import cn.dp.nft.web.util.MultiResultConvertor;
import cn.dp.nft.web.vo.MultiResult;
import cn.dp.nft.web.vo.Result;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

import static cn.dp.nft.api.user.constant.UserType.PLATFORM;

/**
 * @author yebahe
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderFacadeService orderFacadeService;

    @Autowired
    private PayFacadeService payFacadeService;

    /**
     * 订单列表
     *
     * @param
     * @return 结果
     */
    @GetMapping("/orderList")
    public MultiResult<TradeOrderVO> orderList(String state, int pageSize, int currentPage) {
        String userId = (String) StpUtil.getLoginId();
        OrderPageQueryRequest orderPageQueryRequest = new OrderPageQueryRequest();
        orderPageQueryRequest.setBuyerId(userId);
        orderPageQueryRequest.setState(state);
        orderPageQueryRequest.setCurrentPage(currentPage);
        orderPageQueryRequest.setPageSize(pageSize);
        PageResponse<TradeOrderVO> pageResponse = orderFacadeService.pageQuery(orderPageQueryRequest);
        return MultiResultConvertor.convert(pageResponse);
    }

    @GetMapping("/orderDetail")
    public Result<TradeOrderVO> orderDetail(@NotNull String orderId) {
        String userId = (String) StpUtil.getLoginId();
        SingleResponse<TradeOrderVO> singleResponse = orderFacadeService.getTradeOrder(orderId, userId);
        if (singleResponse.getSuccess()) {
            TradeOrderVO tradeOrderVO = singleResponse.getData();
            if (tradeOrderVO.getTimeout() && tradeOrderVO.getOrderState() == TradeOrderState.CONFIRM) {
                //如果订单已经超时，并且尚未关闭，则执行一次关单后再返回数据
                OrderTimeoutRequest cancelRequest = new OrderTimeoutRequest();
                cancelRequest.setOperatorType(PLATFORM);
                cancelRequest.setOperator(PLATFORM.getDesc());
                cancelRequest.setOrderId(tradeOrderVO.getOrderId());
                cancelRequest.setOperateTime(new Date());
                cancelRequest.setIdentifier(UUID.randomUUID().toString());
                orderFacadeService.timeout(cancelRequest);
                singleResponse = orderFacadeService.getTradeOrder(orderId, userId);
            }
            return Result.success(singleResponse.getData());
        } else {
            return Result.error(singleResponse.getResponseCode(), singleResponse.getResponseMessage());
        }
    }

    /**
     * 订单列表
     *
     * @param
     * @return 结果
     */
    @GetMapping("/getPayStatus")
    public Result<PayOrderVO> getPayStatus(@NotNull String payOrderId) {
        String userId = (String) StpUtil.getLoginId();
        SingleResponse<PayOrderVO> singleResponse = payFacadeService.queryPayOrder(payOrderId, userId);
        return new Result(singleResponse);
    }

}
