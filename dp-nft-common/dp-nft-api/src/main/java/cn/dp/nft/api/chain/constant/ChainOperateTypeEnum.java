package cn.dp.nft.api.chain.constant;

/**
 * @author wswyb001
 * @date 2024/01/17
 */
public enum ChainOperateTypeEnum {
    /**
     * 用户创建
     */
    USER_CREATE,
    /**
     * 藏品上链
     */
    COLLECTION_CHAIN,

    /**
     * 藏品铸造
     */
    COLLECTION_MINT,

    /**
     * 藏品交易
     */
    COLLECTION_TRANSFER,

    /**
     * 藏品销毁
     */
    COLLECTION_DESTROY,

    /**
     * 藏品查询
     */
    COLLECTION_QUERY,

}
