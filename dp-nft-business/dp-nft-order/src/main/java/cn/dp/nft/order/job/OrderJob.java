package cn.dp.nft.order.job;

import cn.dp.nft.order.domain.entity.TradeOrder;
import cn.dp.nft.order.domain.service.OrderManageService;
import cn.dp.nft.order.domain.service.OrderReadService;
import cn.dp.nft.api.common.constant.BizOrderType;
import cn.dp.nft.api.order.OrderFacadeService;
import cn.dp.nft.api.order.request.OrderConfirmRequest;
import cn.dp.nft.api.order.request.OrderTimeoutRequest;
import cn.dp.nft.api.pay.constant.PayOrderState;
import cn.dp.nft.api.pay.model.PayOrderVO;
import cn.dp.nft.api.pay.request.PayQueryByBizNo;
import cn.dp.nft.api.pay.request.PayQueryRequest;
import cn.dp.nft.api.pay.service.PayFacadeService;
import cn.dp.nft.api.user.constant.UserType;
import cn.dp.nft.base.response.MultiResponse;
import cn.dp.nft.datasource.sharding.id.BusinessCode;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.infra.hint.HintManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author yebahe
 */
@Component
public class OrderJob {

    @Autowired
    private OrderFacadeService orderFacadeService;

    @Autowired
    private OrderManageService orderManageService;

    @Autowired
    private OrderReadService orderReadService;

    @Autowired
    private PayFacadeService payFacadeService;

    private static final int CAPACITY = 2000;

    private final BlockingQueue<TradeOrder> orderConfirmBlockingQueue = new LinkedBlockingQueue<>(CAPACITY);

    private final BlockingQueue<TradeOrder> orderTimeoutBlockingQueue = new LinkedBlockingQueue<>(CAPACITY);

    /**
     * todo 使用动态线程池
     */
    private final ForkJoinPool forkJoinPool = new ForkJoinPool(10);

    private static final int PAGE_SIZE = 500;

    private static final Logger LOG = LoggerFactory.getLogger(OrderJob.class);

    private static final TradeOrder POISON = new TradeOrder();

    private static int MAX_TAIL_NUMBER = 99;

    @XxlJob("orderTimeOutExecute")
    @Deprecated
    public ReturnT<String> orderTimeOutExecute() {
        try {
            int shardIndex = XxlJobHelper.getShardIndex();
            int shardTotal = XxlJobHelper.getShardTotal();

            LOG.info("orderTimeOutExecute start to execute , shardIndex is {} , shardTotal is {}", shardIndex, shardTotal);

            List<String> buyerIdTailNumberList = new ArrayList<>();
            for (int i = 0; i < MAX_TAIL_NUMBER; i++) {
                if (i % shardTotal == shardIndex) {
                    buyerIdTailNumberList.add(StringUtils.leftPad(String.valueOf(i), 2, "0"));
                }
            }

            try {
                buyerIdTailNumberList.forEach(buyerIdTailNumber -> {
                    int currentPage = 1;
                    Page<TradeOrder> page = orderReadService.pageQueryTimeoutOrders(currentPage, PAGE_SIZE, buyerIdTailNumber);
                    orderTimeoutBlockingQueue.addAll(page.getRecords());
                    forkJoinPool.execute(this::executeTimeout);

                    while (page.hasNext()) {
                        currentPage++;
                        page = orderReadService.pageQueryTimeoutOrders(currentPage, PAGE_SIZE, buyerIdTailNumber);
                        orderTimeoutBlockingQueue.addAll(page.getRecords());
                    }
                });
            } finally {
                orderTimeoutBlockingQueue.add(POISON);
                LOG.info("POISON added to blocking queue");
            }

            return ReturnT.SUCCESS;
        } catch (Exception e) {
            LOG.error("orderTimeOutExecute failed", e);
            throw e;
        }
    }

    @XxlJob("orderConfirmExecute")
    @Deprecated
    public ReturnT<String> orderConfirmExecute() {

        int shardIndex = XxlJobHelper.getShardIndex();
        int shardTotal = XxlJobHelper.getShardTotal();

        LOG.info("orderConfirmExecute start to execute , shardIndex is {} , shardTotal is {}", shardIndex, shardTotal);

        List<String> buyerIdTailNumberList = new ArrayList<>();
        for (int i = 0; i < MAX_TAIL_NUMBER; i++) {
            if (i % shardTotal == shardIndex) {
                buyerIdTailNumberList.add(StringUtils.leftPad(String.valueOf(i), 2, "0"));
            }
        }

        try {
            buyerIdTailNumberList.forEach(buyerIdTailNumber -> {
                int currentPage = 1;
                Page<TradeOrder> page = orderReadService.pageQueryNeedConfirmOrders(currentPage, PAGE_SIZE, buyerIdTailNumber);
                orderConfirmBlockingQueue.addAll(page.getRecords());
                forkJoinPool.execute(this::executeConfirm);

                while (page.hasNext()) {
                    currentPage++;
                    page = orderReadService.pageQueryNeedConfirmOrders(currentPage, PAGE_SIZE, buyerIdTailNumber);
                    orderConfirmBlockingQueue.addAll(page.getRecords());
                }
            });
        } finally {
            orderConfirmBlockingQueue.add(POISON);
            LOG.info("POISON added to blocking queue");
        }

        return ReturnT.SUCCESS;
    }

    private void executeConfirm() {
        TradeOrder tradeOrder = null;
        try {
            while (true) {
                tradeOrder = orderConfirmBlockingQueue.take();
                if (tradeOrder == POISON) {
                    break;
                }
                executeConfirmSingle(tradeOrder);
            }
        } catch (InterruptedException e) {
            LOG.error("executeConfirm failed", e);
        }
    }

    private void executeTimeout() {
        TradeOrder tradeOrder = null;
        try {
            while (true) {
                tradeOrder = orderTimeoutBlockingQueue.take();
                if (tradeOrder == POISON) {
                    break;
                }
                executeTimeoutSingle(tradeOrder);
            }
        } catch (InterruptedException e) {
            LOG.error("executeTimeout failed", e);
        }
    }

    @XxlJob("orderTimeOutExecuteWithHint")
    @Deprecated
    public ReturnT<String> orderTimeOutExecuteWithHint() {
        try {
            int shardIndex = XxlJobHelper.getShardIndex();
            int shardTotal = XxlJobHelper.getShardTotal();

            LOG.info("orderTimeOutExecute start to execute , shardIndex is {} , shardTotal is {}", shardIndex, shardTotal);

            int shardingTableCount = BusinessCode.TRADE_ORDER.tableCount();

            if (shardIndex >= shardingTableCount) {
                return ReturnT.SUCCESS;
            }

            List<Integer> shardingTableIndexes = new ArrayList<>();
            for (int realTableIndex = 0; realTableIndex < shardingTableCount; realTableIndex++) {
                if (realTableIndex % shardTotal == shardIndex) {
                    shardingTableIndexes.add(realTableIndex);
                }
            }

            shardingTableIndexes.forEach(index -> {

                try (HintManager hintManager = HintManager.getInstance()) {
                    LOG.info("shardIndex {} is execute", index);
                    hintManager.addTableShardingValue("trade_order", "000" + index);
                    int currentPage = 1;
                    Page<TradeOrder> page = orderReadService.pageQueryTimeoutOrders(currentPage, PAGE_SIZE, null);
                    page.getRecords().forEach(this::executeTimeoutSingle);
                    while (page.hasNext()) {
                        currentPage++;
                        page = orderReadService.pageQueryTimeoutOrders(currentPage, PAGE_SIZE, null);
                        page.getRecords().forEach(this::executeTimeoutSingle);
                    }
                }
            });

            return ReturnT.SUCCESS;
        } catch (Exception e) {
            LOG.error("orderTimeOutExecute failed", e);
            throw e;
        }
    }

    @XxlJob("orderConfirmExecuteWithHint")
    @Deprecated
    public ReturnT<String> orderConfirmExecuteWithHint() {

        int shardIndex = XxlJobHelper.getShardIndex();
        int shardTotal = XxlJobHelper.getShardTotal();

        int shardingTableCount = BusinessCode.TRADE_ORDER.tableCount();

        if (shardIndex >= shardingTableCount) {
            return ReturnT.SUCCESS;
        }

        List<Integer> shardingTableIndexes = new ArrayList<>();
        for (int realTableIndex = 0; realTableIndex < shardingTableCount; realTableIndex++) {
            if (realTableIndex % shardTotal == shardIndex) {
                shardingTableIndexes.add(realTableIndex);
            }
        }

        shardingTableIndexes.parallelStream().forEach(index -> {
            HintManager hintManager = HintManager.getInstance();
            hintManager.addTableShardingValue("trade_order", "000" + index);
            int currentPage = 1;
            Page<TradeOrder> page = orderReadService.pageQueryNeedConfirmOrders(currentPage, PAGE_SIZE, null);
            page.getRecords().forEach(this::executeConfirmSingle);
            while (page.hasNext()) {
                currentPage++;
                page = orderReadService.pageQueryNeedConfirmOrders(currentPage, PAGE_SIZE, null);
                page.getRecords().forEach(this::executeConfirmSingle);
            }
        });

        return ReturnT.SUCCESS;
    }

    private void executeTimeoutSingle(TradeOrder tradeOrder) {
        //查询支付单，判断是否已经支付成功。
        PayQueryRequest request = new PayQueryRequest();
        request.setPayerId(tradeOrder.getBuyerId());
        request.setPayOrderState(PayOrderState.PAID);
        PayQueryByBizNo payQueryByBizNo = new PayQueryByBizNo();
        payQueryByBizNo.setBizNo(tradeOrder.getOrderId());
        payQueryByBizNo.setBizType(BizOrderType.TRADE_ORDER.name());
        request.setPayQueryCondition(payQueryByBizNo);
        MultiResponse<PayOrderVO> payQueryResponse = payFacadeService.queryPayOrders(request);

        if (payQueryResponse.getSuccess() && CollectionUtils.isEmpty(payQueryResponse.getDatas())) {
            LOG.info("start to execute order timeout , orderId is {}", tradeOrder.getOrderId());
            OrderTimeoutRequest orderTimeoutRequest = new OrderTimeoutRequest();
            orderTimeoutRequest.setOrderId(tradeOrder.getOrderId());
            orderTimeoutRequest.setOperateTime(new Date());
            orderTimeoutRequest.setOperator(UserType.PLATFORM.name());
            orderTimeoutRequest.setOperatorType(UserType.PLATFORM);
            orderFacadeService.timeout(orderTimeoutRequest);
        }
    }


    private void executeConfirmSingle(TradeOrder tradeOrder) {
        OrderConfirmRequest confirmRequest = new OrderConfirmRequest();
        confirmRequest.setOperator(UserType.PLATFORM.name());
        confirmRequest.setOperatorType(UserType.PLATFORM);
        confirmRequest.setOrderId(tradeOrder.getOrderId());
        confirmRequest.setIdentifier(tradeOrder.getIdentifier());
        confirmRequest.setOperateTime(new Date());

        orderFacadeService.confirm(confirmRequest);
    }
}
