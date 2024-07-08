package cn.dp.nft.api.collection.request;

import cn.dp.nft.base.request.PageRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wswyb001
 */
@Getter
@Setter
public class CollectionPageQueryRequest extends PageRequest {

    private String state;

    private String keyword;

}
