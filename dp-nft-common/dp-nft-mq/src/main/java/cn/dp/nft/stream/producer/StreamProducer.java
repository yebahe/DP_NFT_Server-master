package cn.dp.nft.stream.producer;

import java.util.UUID;

import cn.dp.nft.stream.param.MessageBody;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;

/**
 * @author yebahe
 */
public class StreamProducer {

    private static Logger logger = LoggerFactory.getLogger(StreamProducer.class);

    @Autowired
    private StreamBridge streamBridge;

    public boolean send(String bingingName, String tag, String msg) {
        // 构建消息对象
        MessageBody message = new MessageBody()
                .setIdentifier(UUID.randomUUID().toString())
                .setBody(msg);
        logger.info("send message : {} , {}", bingingName, JSON.toJSONString(message));
        boolean result = streamBridge.send(bingingName, MessageBuilder.withPayload(message).setHeader("TAGS", tag)
                .build());
        logger.info("send result : {} , {}", bingingName, result);
        return result;
    }

    public boolean send(String bingingName, String tag, String msg, String headerKey, String headerValue) {
        // 构建消息对象
        MessageBody message = new MessageBody()
                .setIdentifier(UUID.randomUUID().toString())
                .setBody(msg);
        logger.info("send message : {} , {}", bingingName, JSON.toJSONString(message));
        boolean result = streamBridge.send(bingingName, MessageBuilder.withPayload(message).setHeader("TAGS", tag).setHeader(headerKey, headerValue)
                .build());
        logger.info("send result : {} , {}", bingingName, result);
        return result;
    }
}
