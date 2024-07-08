package cn.dp.nft.collection.domain.entity.convertor;

import java.util.List;

import cn.dp.nft.collection.domain.entity.HeldCollection;
import cn.dp.nft.api.collection.model.HeldCollectionDTO;
import cn.dp.nft.api.collection.model.HeldCollectionVO;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

/**
 * @author yebahe
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface HeldCollectionConvertor {

    HeldCollectionConvertor INSTANCE = Mappers.getMapper(HeldCollectionConvertor.class);

    /**
     * 转换为vo
     *
     * @param request
     * @return
     */
    public HeldCollectionVO mapToVo(HeldCollection request);

    /**
     * 转换为 DTO
     * @param request
     * @return
     */
    public HeldCollectionDTO mapToDto(HeldCollection request);

    /**
     * 转换为vo
     *
     * @param request
     * @return
     */
    public List<HeldCollectionVO> mapToVo(List<HeldCollection> request);

}
