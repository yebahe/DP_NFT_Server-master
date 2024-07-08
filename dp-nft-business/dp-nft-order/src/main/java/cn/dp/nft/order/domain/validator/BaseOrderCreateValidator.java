package cn.dp.nft.order.domain.validator;

import cn.dp.nft.api.order.request.OrderCreateRequest;
import cn.dp.nft.order.domain.exception.OrderException;

/**
 * 订单校验
 *
 * @author yebahe
 */
public abstract class BaseOrderCreateValidator implements OrderCreateValidator {

    protected OrderCreateValidator nextValidator;

    @Override
    public void setNext(OrderCreateValidator nextValidator) {
        this.nextValidator = nextValidator;
    }

    @Override
    public OrderCreateValidator getNext() {
        return nextValidator;
    }

    /**
     * 校验
     *
     * @param request
     * @throws Exception
     */
    public void validate(OrderCreateRequest request) throws OrderException {
        doValidate(request);

        if (nextValidator != null) {
            nextValidator.validate(request);
        }
    }

    protected abstract void doValidate(OrderCreateRequest request) throws OrderException;
}
