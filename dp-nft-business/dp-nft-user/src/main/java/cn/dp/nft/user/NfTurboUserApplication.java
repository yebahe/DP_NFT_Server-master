package cn.dp.nft.user;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yebahe
 */
@SpringBootApplication(scanBasePackages = "cn.dp.nft.user")
@EnableDubbo
public class NfTurboUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(NfTurboUserApplication.class, args);
    }

}
