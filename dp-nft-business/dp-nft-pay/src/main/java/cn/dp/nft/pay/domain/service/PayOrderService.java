package cn.dp.nft.pay.domain.service;

import cn.dp.nft.api.pay.constant.PayOrderState;
import cn.dp.nft.api.pay.request.PayCreateRequest;
import cn.dp.nft.base.exception.BizException;
import cn.dp.nft.base.exception.RepoErrorCode;
import cn.dp.nft.pay.domain.entity.PayOrder;
import cn.dp.nft.pay.domain.event.PaySuccessEvent;
import cn.dp.nft.pay.infrastructure.mapper.PayOrderMapper;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author yebahe
 */
@Service
public class PayOrderService extends ServiceImpl<PayOrderMapper, PayOrder> {
    private static final Logger logger = LoggerFactory.getLogger(PayOrderService.class);

    @Autowired
    private PayOrderMapper payOrderMapper;

    public PayOrder create(PayCreateRequest payCreateRequest) {
        PayOrder existPayOrder = payOrderMapper.selectByBizNoAndPayer(payCreateRequest.getPayerId(), payCreateRequest.getBizNo(), payCreateRequest.getBizType().name(), payCreateRequest.getPayChannel().name());

        if (existPayOrder != null) {
            if (existPayOrder.getOrderState() != PayOrderState.EXPIRED) {
                return existPayOrder;
            }
        }

        PayOrder payOrder = PayOrder.create(payCreateRequest);
        boolean saveResult = save(payOrder);
        Assert.isTrue(saveResult, () -> new BizException(RepoErrorCode.INSERT_FAILED));

        return payOrder;
    }


    public Boolean paying(String payOrderId, String payUrl) {
        PayOrder payOrder = payOrderMapper.selectByPayOrderId(payOrderId);
        payOrder.paying(payUrl);

        //fixme 改成按照支付单号更新
        boolean saveResult = saveOrUpdate(payOrder);
        Assert.isTrue(saveResult, () -> new BizException(RepoErrorCode.UPDATE_FAILED));

        return true;
    }


    public Boolean paySuccess(PaySuccessEvent paySuccessEvent) {
        PayOrder payOrder = payOrderMapper.selectByPayOrderId(paySuccessEvent.getPayOrderId());
        payOrder.paySuccess(paySuccessEvent);

        boolean saveResult = saveOrUpdate(payOrder);
        Assert.isTrue(saveResult, () -> new BizException(RepoErrorCode.UPDATE_FAILED));

        return true;
    }

    public Boolean payExpired(String payOrderId) {
        PayOrder payOrder = payOrderMapper.selectByPayOrderId(payOrderId);
        payOrder.payExpired();

        boolean saveResult = saveOrUpdate(payOrder);
        Assert.isTrue(saveResult, () -> new BizException(RepoErrorCode.UPDATE_FAILED));

        return true;
    }

    public List<PayOrder> queryByBizNo(String bizNo, String bizOrderType, String payerId, PayOrderState payOrderState) {
        QueryWrapper<PayOrder> queryWrapper = new QueryWrapper();
        queryWrapper.eq("biz_no", bizNo);
        queryWrapper.eq("biz_type", bizOrderType);
        queryWrapper.eq("payer_id", payerId);
        queryWrapper.eq("order_state", payOrderState.name());

        return this.list(queryWrapper);
    }

    public PayOrder queryByOrderId(String payOrderId) {
        QueryWrapper<PayOrder> queryWrapper = new QueryWrapper();
        queryWrapper.eq("pay_order_id", payOrderId);
        return this.getOne(queryWrapper);
    }

    public PayOrder queryByOrderIdAndPayer(String payOrderId,String payerId) {
        QueryWrapper<PayOrder> queryWrapper = new QueryWrapper();
        queryWrapper.eq("pay_order_id", payOrderId);
        queryWrapper.eq("payer_id", payerId);
        return this.getOne(queryWrapper);
    }

    public Page<PayOrder> pageQueryTimeoutOrders(int currentPage, int pageSize) {
        Page<PayOrder> page = new Page<>(currentPage, pageSize);

        QueryWrapper<PayOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_state", PayOrderState.PAYING);
        wrapper.lt("gmt_create", DateUtils.addMinutes(new Date(), -PayOrder.DEFAULT_TIME_OUT_MINUTES));
        wrapper.orderBy(true, true, "gmt_create");

        return this.page(page, wrapper);
    }
}
