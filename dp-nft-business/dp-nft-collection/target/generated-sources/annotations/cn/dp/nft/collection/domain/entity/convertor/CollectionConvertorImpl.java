package cn.dp.nft.collection.domain.entity.convertor;

import cn.dp.nft.api.collection.model.CollectionVO;
import cn.dp.nft.api.collection.request.CollectionChainRequest;
import cn.dp.nft.collection.domain.entity.Collection;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-08T18:44:41+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.1 (Oracle Corporation)"
)
public class CollectionConvertorImpl implements CollectionConvertor {

    @Override
    public CollectionVO mapToVo(Collection request) {
        if ( request == null ) {
            return null;
        }

        CollectionVO collectionVO = new CollectionVO();

        if ( request.getSaleableInventory() != null ) {
            collectionVO.setInventory( request.getSaleableInventory() );
        }
        if ( request.getId() != null ) {
            collectionVO.setId( request.getId() );
        }
        if ( request.getName() != null ) {
            collectionVO.setName( request.getName() );
        }
        if ( request.getCover() != null ) {
            collectionVO.setCover( request.getCover() );
        }
        if ( request.getClassId() != null ) {
            collectionVO.setClassId( request.getClassId() );
        }
        if ( request.getPrice() != null ) {
            collectionVO.setPrice( request.getPrice() );
        }
        if ( request.getQuantity() != null ) {
            collectionVO.setQuantity( request.getQuantity() );
        }
        if ( request.getCreateTime() != null ) {
            collectionVO.setCreateTime( request.getCreateTime() );
        }
        if ( request.getSaleTime() != null ) {
            collectionVO.setSaleTime( request.getSaleTime() );
        }
        if ( request.getSyncChainTime() != null ) {
            collectionVO.setSyncChainTime( request.getSyncChainTime() );
        }
        if ( request.getVersion() != null ) {
            collectionVO.setVersion( request.getVersion() );
        }

        return collectionVO;
    }

    @Override
    public Collection mapToEntity(CollectionChainRequest request) {
        if ( request == null ) {
            return null;
        }

        Collection collection = new Collection();

        if ( request.getName() != null ) {
            collection.setName( request.getName() );
        }
        if ( request.getCover() != null ) {
            collection.setCover( request.getCover() );
        }
        if ( request.getClassId() != null ) {
            collection.setClassId( request.getClassId() );
        }
        if ( request.getPrice() != null ) {
            collection.setPrice( request.getPrice() );
        }
        if ( request.getQuantity() != null ) {
            collection.setQuantity( request.getQuantity() );
        }
        if ( request.getCreateTime() != null ) {
            collection.setCreateTime( request.getCreateTime() );
        }
        if ( request.getSaleTime() != null ) {
            collection.setSaleTime( request.getSaleTime() );
        }
        if ( request.getCreatorId() != null ) {
            collection.setCreatorId( request.getCreatorId() );
        }

        return collection;
    }

    @Override
    public List<CollectionVO> mapToVo(List<Collection> request) {
        if ( request == null ) {
            return null;
        }

        List<CollectionVO> list = new ArrayList<CollectionVO>( request.size() );
        for ( Collection collection : request ) {
            list.add( mapToVo( collection ) );
        }

        return list;
    }
}
