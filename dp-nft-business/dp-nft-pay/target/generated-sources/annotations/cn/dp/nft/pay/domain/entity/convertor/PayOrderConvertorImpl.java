package cn.dp.nft.pay.domain.entity.convertor;

import cn.dp.nft.api.pay.model.PayOrderVO;
import cn.dp.nft.api.pay.request.PayCreateRequest;
import cn.dp.nft.pay.domain.entity.PayOrder;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-08T18:44:33+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.1 (Oracle Corporation)"
)
public class PayOrderConvertorImpl implements PayOrderConvertor {

    @Override
    public PayOrder mapToEntity(PayCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        PayOrder payOrder = new PayOrder();

        if ( request.getPayerId() != null ) {
            payOrder.setPayerId( request.getPayerId() );
        }
        if ( request.getPayerType() != null ) {
            payOrder.setPayerType( request.getPayerType() );
        }
        if ( request.getPayeeId() != null ) {
            payOrder.setPayeeId( request.getPayeeId() );
        }
        if ( request.getPayeeType() != null ) {
            payOrder.setPayeeType( request.getPayeeType() );
        }
        if ( request.getBizNo() != null ) {
            payOrder.setBizNo( request.getBizNo() );
        }
        if ( request.getBizType() != null ) {
            payOrder.setBizType( request.getBizType().name() );
        }
        if ( request.getOrderAmount() != null ) {
            payOrder.setOrderAmount( request.getOrderAmount() );
        }
        if ( request.getPayChannel() != null ) {
            payOrder.setPayChannel( request.getPayChannel() );
        }
        if ( request.getMemo() != null ) {
            payOrder.setMemo( request.getMemo() );
        }

        return payOrder;
    }

    @Override
    public PayOrderVO mapToVo(PayOrder request) {
        if ( request == null ) {
            return null;
        }

        PayOrderVO payOrderVO = new PayOrderVO();

        if ( request.getPayOrderId() != null ) {
            payOrderVO.setPayOrderId( request.getPayOrderId() );
        }
        if ( request.getPayUrl() != null ) {
            payOrderVO.setPayUrl( request.getPayUrl() );
        }
        if ( request.getOrderState() != null ) {
            payOrderVO.setOrderState( request.getOrderState() );
        }

        return payOrderVO;
    }

    @Override
    public List<PayOrderVO> mapToVo(List<PayOrder> request) {
        if ( request == null ) {
            return null;
        }

        List<PayOrderVO> list = new ArrayList<PayOrderVO>( request.size() );
        for ( PayOrder payOrder : request ) {
            list.add( mapToVo( payOrder ) );
        }

        return list;
    }
}
