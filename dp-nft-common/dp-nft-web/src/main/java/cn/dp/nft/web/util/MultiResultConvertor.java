package cn.dp.nft.web.util;

import cn.dp.nft.base.response.PageResponse;
import cn.dp.nft.web.vo.MultiResult;

import static cn.dp.nft.base.response.ResponseCode.SUCCESS;

/**
 * @author yebahe
 */
public class MultiResultConvertor {

    public static <T> MultiResult<T> convert(PageResponse<T> pageResponse) {
        MultiResult<T> multiResult = new MultiResult<T>(true, SUCCESS.name(), SUCCESS.name(), pageResponse.getDatas(), pageResponse.getTotal(), pageResponse.getCurrentPage(), pageResponse.getPageSize());
        return multiResult;
    }
}
