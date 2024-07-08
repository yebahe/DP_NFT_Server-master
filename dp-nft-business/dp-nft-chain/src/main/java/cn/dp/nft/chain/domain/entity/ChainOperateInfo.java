package cn.dp.nft.chain.domain.entity;

import java.util.Date;

import cn.dp.nft.api.chain.constant.ChainOperateBizTypeEnum;
import cn.dp.nft.api.chain.constant.ChainOperateTypeEnum;
import cn.dp.nft.datasource.domain.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 链操作
 * </p>
 *
 * @author wswyb001
 * @since 2024-01-19
 */
@Getter
@Setter
public class ChainOperateInfo extends BaseEntity {

    /**
     * 链类型
     */
    private String chainType;

    /**
     * 业务id
     */
    private String bizId;

    /**
     * 业务类型
     *
     * @see ChainOperateBizTypeEnum
     */
    private String bizType;

    /**
     * 操作类型
     * @see ChainOperateTypeEnum
     */
    private String operateType;

    /**
     * 状态
     */
    private String state;

    /**
     * 操作时间
     */
    private Date operateTime;

    /**
     * 成功时间
     */
    private Date succeedTime;

    /**
     * 入参
     */
    private String param;

    /**
     * 返回结果
     */
    private String result;

    /**
     * 外部业务id
     */
    private String outBizId;
}
