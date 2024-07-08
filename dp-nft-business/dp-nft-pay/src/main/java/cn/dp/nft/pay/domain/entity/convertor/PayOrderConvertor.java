package cn.dp.nft.pay.domain.entity.convertor;

import cn.dp.nft.api.pay.model.PayOrderVO;
import cn.dp.nft.api.pay.request.PayCreateRequest;
import cn.dp.nft.pay.domain.entity.PayOrder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author yebahe
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PayOrderConvertor {

    PayOrderConvertor INSTANCE = Mappers.getMapper(PayOrderConvertor.class);

    /**
     * 转换实体
     *
     * @param request
     * @return
     */
    public PayOrder mapToEntity(PayCreateRequest request);

    /**
     * 转换vo
     *
     * @param request
     * @return
     */
    public PayOrderVO mapToVo(PayOrder request);

    /**
     * 转换vo
     *
     * @param request
     * @return
     */
    public List<PayOrderVO> mapToVo(List<PayOrder> request);
}
