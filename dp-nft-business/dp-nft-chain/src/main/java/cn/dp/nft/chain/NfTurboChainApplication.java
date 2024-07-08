package cn.dp.nft.chain;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yebahe
 */
@SpringBootApplication(scanBasePackages = "cn.dp.nft.chain")
@EnableDubbo
public class NfTurboChainApplication {

    public static void main(String[] args) {
        SpringApplication.run(NfTurboChainApplication.class, args);
    }

}
