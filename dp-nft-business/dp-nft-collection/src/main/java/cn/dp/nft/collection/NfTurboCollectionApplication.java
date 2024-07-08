package cn.dp.nft.collection;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yebahe
 */
@SpringBootApplication(scanBasePackages = "cn.dp.nft.collection")
@EnableDubbo
public class NfTurboCollectionApplication {

    public static void main(String[] args) {
        SpringApplication.run(NfTurboCollectionApplication.class, args);
    }

}
