package cn.dp.nft.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yebahe
 */
//@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "cn.dp.nft.gateway")
public class NfTurboGatewayApplication {

    public static void main(String[] args) {

        SpringApplication.run(NfTurboGatewayApplication.class, args);
    }

}
