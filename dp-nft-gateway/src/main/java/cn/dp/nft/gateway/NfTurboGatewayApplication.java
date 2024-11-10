package cn.dp.nft.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author yebahe
 */
@EnableDiscoveryClient // 启用服务发现功能，使应用能注册到服务注册中心
@SpringBootApplication(scanBasePackages = "cn.dp.nft.gateway") // 启用 Spring Boot 的自动配置和组件扫描
@EnableAspectJAutoProxy
public class NfTurboGatewayApplication {

    public static void main(String[] args) {
        // 启动 Spring Boot 应用程序
        SpringApplication.run(NfTurboGatewayApplication.class, args);
    }
}
