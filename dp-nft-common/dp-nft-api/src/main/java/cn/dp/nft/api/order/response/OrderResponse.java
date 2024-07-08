package cn.dp.nft.api.order.response;

import cn.dp.nft.base.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

import static cn.dp.nft.base.exception.BizErrorCode.DUPLICATED;

/**
 * @author yebahe
 */
@Getter
@Setter
public class OrderResponse extends BaseResponse {

    private String orderId;

    private String streamId;

    public static class OrderResponseBuilder {
        private String orderId;
        private String streamId;

        public OrderResponseBuilder orderId(String orderId) {
            this.orderId = orderId;
            return this;
        }

        public OrderResponseBuilder streamId(String streamId) {
            this.streamId = streamId;
            return this;
        }

        public OrderResponse buildSuccess() {
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setOrderId(orderId);
            orderResponse.setStreamId(streamId);
            orderResponse.setSuccess(true);
            return orderResponse;
        }

        public OrderResponse buildDuplicated() {
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setOrderId(orderId);
            orderResponse.setStreamId(streamId);
            orderResponse.setSuccess(true);
            orderResponse.setResponseCode(DUPLICATED.getCode());
            orderResponse.setResponseMessage(DUPLICATED.getMessage());
            return orderResponse;
        }

        public OrderResponse buildFail(String msg, String code) {
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setOrderId(orderId);
            orderResponse.setStreamId(streamId);
            orderResponse.setSuccess(false);
            orderResponse.setResponseCode(code);
            orderResponse.setResponseMessage(msg);
            return orderResponse;
        }
    }
}
