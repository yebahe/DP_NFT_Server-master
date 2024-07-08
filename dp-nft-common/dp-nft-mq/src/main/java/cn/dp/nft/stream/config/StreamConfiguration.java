package cn.dp.nft.stream.config;

import cn.dp.nft.stream.producer.StreamProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yebahe
 */
@Configuration
public class StreamConfiguration {
    @Bean
    public StreamProducer streamProducer() {
        StreamProducer streamProducer = new StreamProducer();
        return streamProducer;
    }
}
