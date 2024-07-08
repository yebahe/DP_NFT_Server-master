package cn.dp.nft.stream.param;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 消息体
 *
 * @author yebahe
 */
@Data
@Accessors(chain = true)
public class MessageBody {
    /**
     * 幂等号
     */
    private String identifier;
    /**
     * 消息体
     */
    private String body;
}