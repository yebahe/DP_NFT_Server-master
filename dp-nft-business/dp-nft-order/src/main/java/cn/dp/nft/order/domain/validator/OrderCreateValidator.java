package cn.dp.nft.order.domain.validator;

import cn.dp.nft.order.domain.exception.OrderException;
import cn.dp.nft.api.order.request.OrderCreateRequest;

/**
 * 订单校验
 *
 * @author yebahe
 */
public interface OrderCreateValidator {
    /**
     * 设置下一个校验器
     *
     * @param nextValidator
     */
    public void setNext(OrderCreateValidator nextValidator);

    /**
     * 返回下一个校验器
     *
     * @return
     */
    public OrderCreateValidator getNext();

    /**
     * 校验
     *
     * @param request
     * @throws Exception
     */
    public void validate(OrderCreateRequest request) throws OrderException;
}
