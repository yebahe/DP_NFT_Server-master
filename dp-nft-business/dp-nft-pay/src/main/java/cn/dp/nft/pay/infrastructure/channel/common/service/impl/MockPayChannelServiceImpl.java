package cn.dp.nft.pay.infrastructure.channel.common.service.impl;

import cn.dp.nft.pay.infrastructure.channel.common.request.PayChannelRequest;
import cn.dp.nft.pay.infrastructure.channel.common.service.PayChannelService;
import cn.dp.nft.api.pay.constant.PayChannel;
import cn.dp.nft.base.utils.MoneyUtils;
import cn.dp.nft.pay.application.service.PayApplicationService;
import cn.dp.nft.pay.domain.event.PaySuccessEvent;
import cn.dp.nft.pay.infrastructure.channel.common.response.PayChannelResponse;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * mock支付渠道
 *
 * @author yebahe
 */
@Service("mockPayService")
@Slf4j
public class MockPayChannelServiceImpl implements PayChannelService {
    @Autowired
    private PayApplicationService payApplicationService;

    TransmittableThreadLocal<Map> context = new TransmittableThreadLocal<>();

    private static ThreadFactory chainResultProcessFactory = new ThreadFactoryBuilder()
            .setNameFormat("pay-process-pool-%d").build();

    ScheduledExecutorService scheduler = TtlExecutors.getTtlScheduledExecutorService(new ScheduledThreadPoolExecutor(10, chainResultProcessFactory));

    @Override
    public PayChannelResponse pay(PayChannelRequest payChannelRequest) {
        PayChannelResponse payChannelResponse = new PayChannelResponse();
        payChannelResponse.setSuccess(true);
        payChannelResponse.setPayUrl("http://www.nfturbo.com");
        Map<String, Serializable> params = new HashMap<>(12);
        params.put("payOrderId", payChannelRequest.getOrderId());
        params.put("paidAmount", payChannelRequest.getAmount());
        context.set(params);

        //异步线程延迟3秒钟之后调用 notify 方法
        scheduler.schedule(() -> {
            this.notify(null, null);
        }, 2, TimeUnit.SECONDS);

        return payChannelResponse;
    }

    @Override
    public boolean notify(HttpServletRequest request, HttpServletResponse response) {
        try {
            PaySuccessEvent paySuccessEvent = new PaySuccessEvent();
            paySuccessEvent.setChannelStreamId(UUID.randomUUID().toString());
            Map<String, Serializable> params = (Map<String, Serializable>) context.get();
            paySuccessEvent.setPaidAmount(MoneyUtils.centToYuan((Long) params.get("paidAmount")));
            paySuccessEvent.setPayOrderId((String) params.get("payOrderId"));
            paySuccessEvent.setPaySucceedTime(new Date());
            paySuccessEvent.setPayChannel(PayChannel.MOCK);
            boolean paySuccessResult = payApplicationService.paySuccess(paySuccessEvent);
        } catch (Exception e) {
            log.error("nofity error", e);
            return false;
        }
        return true;
    }
}
