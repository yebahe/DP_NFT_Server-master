package cn.dp.nft.api.goods.constant;

/**
 * @author yebahe
 */
public enum GoodsType {

    /** 藏品 */
    COLLECTION("藏品"),

    /**
     * 盲盒
     */
    BLIND_BOX("盲盒");


    private String value;

    GoodsType(String value) {
        this.value = value;
    }
}
