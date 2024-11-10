package cn.dp.nft.collection;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 藏品模块主要是用来做藏品的管理，主要包含了藏品的创建、交易、上链等动作。
 * @author yebahe
 */
@SpringBootApplication(scanBasePackages = "cn.dp.nft.collection")
@EnableDubbo
public class NfTurboCollectionApplication {

    public static void main(String[] args) {
        SpringApplication.run(NfTurboCollectionApplication.class, args);
    }

}
