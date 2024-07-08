package cn.dp.nft.pay.infrastructure.channel.wechat.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yebahe
 */
@Getter
@Setter
public class WxNotifyResourceEntitySceneInfoEntity {
    @JSONField(name = "device_id")
    private String deviceId;
}
