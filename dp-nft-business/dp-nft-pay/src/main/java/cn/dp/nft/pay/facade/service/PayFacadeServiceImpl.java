package cn.dp.nft.pay.facade.service;

import cn.dp.nft.pay.domain.entity.convertor.PayOrderConvertor;
import cn.dp.nft.pay.domain.service.PayOrderService;
import cn.dp.nft.pay.infrastructure.channel.common.request.PayChannelRequest;
import cn.dp.nft.pay.infrastructure.channel.common.response.PayChannelResponse;
import cn.dp.nft.pay.infrastructure.channel.common.service.PayChannelServiceFactory;
import cn.dp.nft.api.pay.constant.PayOrderState;
import cn.dp.nft.api.pay.model.PayOrderVO;
import cn.dp.nft.api.pay.request.PayCreateRequest;
import cn.dp.nft.api.pay.request.PayQueryByBizNo;
import cn.dp.nft.api.pay.request.PayQueryCondition;
import cn.dp.nft.api.pay.request.PayQueryRequest;
import cn.dp.nft.api.pay.response.PayCreateResponse;
import cn.dp.nft.api.pay.service.PayFacadeService;
import cn.dp.nft.base.exception.BizException;
import cn.dp.nft.base.exception.RepoErrorCode;
import cn.dp.nft.base.response.MultiResponse;
import cn.dp.nft.base.response.SingleResponse;
import cn.dp.nft.base.utils.MoneyUtils;
import cn.dp.nft.lock.DistributeLock;
import cn.dp.nft.pay.domain.entity.PayOrder;
import cn.dp.nft.rpc.facade.Facade;
import cn.hutool.core.lang.Assert;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author yebahe
 */
@DubboService(version = "1.0.0")
public class PayFacadeServiceImpl implements PayFacadeService {

    private static final Logger logger = LoggerFactory.getLogger(PayFacadeServiceImpl.class);

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private PayChannelServiceFactory payChannelServiceFactory;

    @Facade
    @DistributeLock(keyExpression = "#payCreateRequest.bizNo", scene = "GENERATE_PAY_URL")
    @Override
    public PayCreateResponse generatePayUrl(PayCreateRequest payCreateRequest) {
        PayCreateResponse response = new PayCreateResponse();
        PayOrder payOrder = payOrderService.create(payCreateRequest);

        if (payOrder.getOrderState() != PayOrderState.TO_PAY) {
            response.setPayOrderId(payOrder.getPayOrderId());
            response.setPayUrl(payOrder.getPayUrl());
            response.setSuccess(true);
            return response;
        }

        PayChannelResponse payChannelResponse = doPay(payCreateRequest, payOrder);

        if (payChannelResponse.getSuccess()) {
            boolean updateResult = payOrderService.paying(payOrder.getPayOrderId(), payChannelResponse.getPayUrl());
            Assert.isTrue(updateResult, () -> new BizException(RepoErrorCode.UPDATE_FAILED));
            response.setSuccess(true);
            response.setPayOrderId(payOrder.getPayOrderId());
            response.setPayUrl(payChannelResponse.getPayUrl());
        } else {
            response.setSuccess(false);
            response.setResponseCode(payChannelResponse.getResponseCode());
            response.setResponseMessage(payChannelResponse.getResponseMessage());
        }
        return response;
    }

    @Override
    @Facade
    public MultiResponse<PayOrderVO> queryPayOrders(PayQueryRequest payQueryRequest) {

        PayQueryCondition payQueryCondition = payQueryRequest.getPayQueryCondition();

        if (payQueryCondition instanceof PayQueryByBizNo payQueryByBizNo) {
            List<PayOrder> payOrders = payOrderService.queryByBizNo(payQueryByBizNo.getBizNo(), payQueryByBizNo.getBizType(), payQueryRequest.getPayerId(), payQueryRequest.getPayOrderState());
            var payQueryResponse = new MultiResponse<PayOrderVO>();
            payQueryResponse.setSuccess(true);
            payQueryResponse.setDatas(PayOrderConvertor.INSTANCE.mapToVo(payOrders));
            return payQueryResponse;
        }

        throw new UnsupportedOperationException("unsupported payQueryCondition : " + payQueryCondition);
    }

    @Override
    @Facade
    public SingleResponse<PayOrderVO> queryPayOrder(String payOrderId) {
        return SingleResponse.of(PayOrderConvertor.INSTANCE.mapToVo(payOrderService.queryByOrderId(payOrderId)));
    }

    @Override
    public SingleResponse<PayOrderVO> queryPayOrder(String payOrderId, String payerId) {
        return SingleResponse.of(PayOrderConvertor.INSTANCE.mapToVo(payOrderService.queryByOrderIdAndPayer(payOrderId, payerId)));
    }

    private PayChannelResponse doPay(PayCreateRequest payCreateRequest, PayOrder payOrder) {
        PayChannelRequest payChannelRequest = new PayChannelRequest();
        payChannelRequest.setAmount(MoneyUtils.yuanToCent(payCreateRequest.getOrderAmount()));
        payChannelRequest.setDescription(payCreateRequest.getMemo());
        payChannelRequest.setOrderId(payOrder.getPayOrderId());
        payChannelRequest.setAttach(payCreateRequest.getBizNo());
        PayChannelResponse payChannelResponse = payChannelServiceFactory.get(payCreateRequest.getPayChannel()).pay(payChannelRequest);
        return payChannelResponse;
    }
}
