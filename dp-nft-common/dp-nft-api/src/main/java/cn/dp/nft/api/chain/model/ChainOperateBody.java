package cn.dp.nft.api.chain.model;

import cn.dp.nft.api.chain.constant.ChainOperateBizTypeEnum;
import cn.dp.nft.api.chain.constant.ChainOperateTypeEnum;
import cn.dp.nft.api.chain.constant.ChainType;
import cn.dp.nft.api.chain.response.data.ChainResultData;
import lombok.Getter;
import lombok.Setter;

/**
 * 链操作的方法体
 *
 * @author yebahe
 */
@Setter
@Getter
public class ChainOperateBody {
    /**
     * 业务id
     */
    private String bizId;
    /**
     * 业务类型
     */
    private ChainOperateBizTypeEnum bizType;

    /**
     * 操作类型
     */
    private ChainOperateTypeEnum operateType;
    /**
     * 操作信息id
     */
    private Long operateInfoId;

    /**
     * 链类型
     */
    private ChainType chainType;

    /**
     * 具体业务数据
     */
    private ChainResultData chainResultData;
}
