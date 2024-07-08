package cn.dp.nft.trade;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yebahe
 */
@SpringBootApplication(scanBasePackages = "cn.dp.nft.trade")
@EnableDubbo
public class NfTurboTradeApplication {

    public static void main(String[] args) {
        SpringApplication.run(NfTurboTradeApplication.class, args);
    }

}
