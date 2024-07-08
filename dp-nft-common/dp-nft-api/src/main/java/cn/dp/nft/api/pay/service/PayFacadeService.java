package cn.dp.nft.api.pay.service;

import cn.dp.nft.api.pay.request.PayCreateRequest;
import cn.dp.nft.api.pay.model.PayOrderVO;
import cn.dp.nft.api.pay.request.PayQueryRequest;
import cn.dp.nft.api.pay.response.PayCreateResponse;
import cn.dp.nft.base.response.MultiResponse;
import cn.dp.nft.base.response.SingleResponse;

/**
 * @author yebahe
 */
public interface PayFacadeService {

    /**
     * 生成支付链接
     *
     * @param payCreateRequest
     * @return
     */
    public PayCreateResponse generatePayUrl(PayCreateRequest payCreateRequest);

    /**
     * 查询支付订单
     *
     * @param payQueryRequest
     * @return
     */
    public MultiResponse<PayOrderVO> queryPayOrders(PayQueryRequest payQueryRequest);

    /**
     * 查询支付订单
     *
     * @param payOrderId
     * @return
     */
    public SingleResponse<PayOrderVO> queryPayOrder(String payOrderId);

    /**
     * 查询支付订单
     *
     * @param payOrderId
     * @param payerId
     * @return
     */
    public SingleResponse<PayOrderVO> queryPayOrder(String payOrderId, String payerId);


}
