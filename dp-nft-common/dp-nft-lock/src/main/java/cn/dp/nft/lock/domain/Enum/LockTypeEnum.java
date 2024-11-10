package cn.dp.nft.lock.domain.Enum;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
public enum LockTypeEnum {
    /**
     * 自定义 key 前缀
     */
    AUTH("AUTH", "AUTH模块"),

    BUSINESS("BUSINESS", "BUSINESS"),

    LIMITER("LIMITER", "LIMITER");
    private String code;
    private String desc;

    LockTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getUniqueKey(String key) {
        return String.format("%s:%s", this.getCode(), key);
    }
}