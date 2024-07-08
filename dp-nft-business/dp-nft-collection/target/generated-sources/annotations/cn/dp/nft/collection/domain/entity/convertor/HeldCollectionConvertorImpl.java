package cn.dp.nft.collection.domain.entity.convertor;

import cn.dp.nft.api.collection.model.HeldCollectionDTO;
import cn.dp.nft.api.collection.model.HeldCollectionVO;
import cn.dp.nft.collection.domain.entity.HeldCollection;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-07T17:49:20+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.1 (Oracle Corporation)"
)
public class HeldCollectionConvertorImpl implements HeldCollectionConvertor {

    @Override
    public HeldCollectionVO mapToVo(HeldCollection request) {
        if ( request == null ) {
            return null;
        }

        HeldCollectionVO heldCollectionVO = new HeldCollectionVO();

        if ( request.getId() != null ) {
            heldCollectionVO.setId( request.getId() );
        }
        if ( request.getName() != null ) {
            heldCollectionVO.setName( request.getName() );
        }
        if ( request.getCover() != null ) {
            heldCollectionVO.setCover( request.getCover() );
        }
        if ( request.getPurchasePrice() != null ) {
            heldCollectionVO.setPurchasePrice( request.getPurchasePrice() );
        }
        if ( request.getCollectionId() != null ) {
            heldCollectionVO.setCollectionId( request.getCollectionId() );
        }
        if ( request.getSerialNo() != null ) {
            heldCollectionVO.setSerialNo( request.getSerialNo() );
        }
        if ( request.getNftId() != null ) {
            heldCollectionVO.setNftId( request.getNftId() );
        }
        if ( request.getPreId() != null ) {
            heldCollectionVO.setPreId( request.getPreId() );
        }
        if ( request.getUserId() != null ) {
            heldCollectionVO.setUserId( request.getUserId() );
        }
        if ( request.getState() != null ) {
            heldCollectionVO.setState( request.getState() );
        }
        if ( request.getTxHash() != null ) {
            heldCollectionVO.setTxHash( request.getTxHash() );
        }
        if ( request.getHoldTime() != null ) {
            heldCollectionVO.setHoldTime( request.getHoldTime() );
        }
        if ( request.getSyncChainTime() != null ) {
            heldCollectionVO.setSyncChainTime( request.getSyncChainTime() );
        }
        if ( request.getDeleteTime() != null ) {
            heldCollectionVO.setDeleteTime( request.getDeleteTime() );
        }
        if ( request.getBizNo() != null ) {
            heldCollectionVO.setBizNo( request.getBizNo() );
        }
        if ( request.getBizType() != null ) {
            heldCollectionVO.setBizType( request.getBizType() );
        }

        return heldCollectionVO;
    }

    @Override
    public HeldCollectionDTO mapToDto(HeldCollection request) {
        if ( request == null ) {
            return null;
        }

        HeldCollectionDTO heldCollectionDTO = new HeldCollectionDTO();

        if ( request.getId() != null ) {
            heldCollectionDTO.setId( request.getId() );
        }
        if ( request.getCollectionId() != null ) {
            heldCollectionDTO.setCollectionId( request.getCollectionId() );
        }
        if ( request.getUserId() != null ) {
            heldCollectionDTO.setUserId( request.getUserId() );
        }
        if ( request.getState() != null ) {
            heldCollectionDTO.setState( request.getState() );
        }
        if ( request.getBizNo() != null ) {
            heldCollectionDTO.setBizNo( request.getBizNo() );
        }
        if ( request.getBizType() != null ) {
            heldCollectionDTO.setBizType( request.getBizType() );
        }

        return heldCollectionDTO;
    }

    @Override
    public List<HeldCollectionVO> mapToVo(List<HeldCollection> request) {
        if ( request == null ) {
            return null;
        }

        List<HeldCollectionVO> list = new ArrayList<HeldCollectionVO>( request.size() );
        for ( HeldCollection heldCollection : request ) {
            list.add( mapToVo( heldCollection ) );
        }

        return list;
    }
}
