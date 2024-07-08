package cn.dp.nft.order.domain.entity.convertor;

import cn.dp.nft.api.order.model.TradeOrderVO;
import cn.dp.nft.api.order.request.OrderCreateRequest;
import cn.dp.nft.order.domain.entity.TradeOrder;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-07T17:49:13+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.1 (Oracle Corporation)"
)
public class TradeOrderConvertorImpl implements TradeOrderConvertor {

    @Override
    public TradeOrder mapToEntity(OrderCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        TradeOrder tradeOrder = new TradeOrder();

        if ( request.getBuyerId() != null ) {
            tradeOrder.setBuyerId( request.getBuyerId() );
        }
        if ( request.getBuyerType() != null ) {
            tradeOrder.setBuyerType( request.getBuyerType() );
        }
        if ( request.getSellerId() != null ) {
            tradeOrder.setSellerId( request.getSellerId() );
        }
        if ( request.getSellerType() != null ) {
            tradeOrder.setSellerType( request.getSellerType() );
        }
        if ( request.getIdentifier() != null ) {
            tradeOrder.setIdentifier( request.getIdentifier() );
        }
        if ( request.getOrderAmount() != null ) {
            tradeOrder.setOrderAmount( request.getOrderAmount() );
        }
        tradeOrder.setItemCount( request.getItemCount() );
        if ( request.getItemPrice() != null ) {
            tradeOrder.setItemPrice( request.getItemPrice() );
        }
        if ( request.getGoodsId() != null ) {
            tradeOrder.setGoodsId( request.getGoodsId() );
        }
        if ( request.getGoodsType() != null ) {
            tradeOrder.setGoodsType( request.getGoodsType() );
        }
        if ( request.getGoodsName() != null ) {
            tradeOrder.setGoodsName( request.getGoodsName() );
        }
        if ( request.getGoodsPicUrl() != null ) {
            tradeOrder.setGoodsPicUrl( request.getGoodsPicUrl() );
        }
        if ( request.getSnapshotVersion() != null ) {
            tradeOrder.setSnapshotVersion( request.getSnapshotVersion() );
        }

        return tradeOrder;
    }

    @Override
    public TradeOrderVO mapToVo(TradeOrder request) {
        if ( request == null ) {
            return null;
        }

        TradeOrderVO tradeOrderVO = new TradeOrderVO();

        if ( request.getOrderId() != null ) {
            tradeOrderVO.setOrderId( request.getOrderId() );
        }
        if ( request.getBuyerId() != null ) {
            tradeOrderVO.setBuyerId( request.getBuyerId() );
        }
        if ( request.getBuyerType() != null ) {
            tradeOrderVO.setBuyerType( request.getBuyerType() );
        }
        if ( request.getSellerId() != null ) {
            tradeOrderVO.setSellerId( request.getSellerId() );
        }
        if ( request.getSellerType() != null ) {
            tradeOrderVO.setSellerType( request.getSellerType() );
        }
        if ( request.getOrderAmount() != null ) {
            tradeOrderVO.setOrderAmount( request.getOrderAmount() );
        }
        tradeOrderVO.setItemCount( request.getItemCount() );
        if ( request.getItemPrice() != null ) {
            tradeOrderVO.setItemPrice( request.getItemPrice() );
        }
        if ( request.getPaidAmount() != null ) {
            tradeOrderVO.setPaidAmount( request.getPaidAmount() );
        }
        if ( request.getPaySucceedTime() != null ) {
            tradeOrderVO.setPaySucceedTime( request.getPaySucceedTime() );
        }
        if ( request.getOrderConfirmedTime() != null ) {
            tradeOrderVO.setOrderConfirmedTime( request.getOrderConfirmedTime() );
        }
        if ( request.getOrderClosedTime() != null ) {
            tradeOrderVO.setOrderClosedTime( request.getOrderClosedTime() );
        }
        if ( request.getGoodsId() != null ) {
            tradeOrderVO.setGoodsId( request.getGoodsId() );
        }
        if ( request.getGoodsName() != null ) {
            tradeOrderVO.setGoodsName( request.getGoodsName() );
        }
        if ( request.getGoodsType() != null ) {
            tradeOrderVO.setGoodsType( request.getGoodsType() );
        }
        if ( request.getGoodsPicUrl() != null ) {
            tradeOrderVO.setGoodsPicUrl( request.getGoodsPicUrl() );
        }
        if ( request.getPayChannel() != null ) {
            tradeOrderVO.setPayChannel( request.getPayChannel() );
        }
        if ( request.getPayStreamId() != null ) {
            tradeOrderVO.setPayStreamId( request.getPayStreamId() );
        }
        if ( request.getOrderState() != null ) {
            tradeOrderVO.setOrderState( request.getOrderState() );
        }
        if ( request.getPayExpireTime() != null ) {
            tradeOrderVO.setPayExpireTime( request.getPayExpireTime() );
        }

        tradeOrderVO.setTimeout( request.isTimeout() );

        return tradeOrderVO;
    }

    @Override
    public List<TradeOrderVO> mapToVo(List<TradeOrder> request) {
        if ( request == null ) {
            return null;
        }

        List<TradeOrderVO> list = new ArrayList<TradeOrderVO>( request.size() );
        for ( TradeOrder tradeOrder : request ) {
            list.add( mapToVo( tradeOrder ) );
        }

        return list;
    }
}
