package cn.dp.nft.goods;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 主要是为了抽象出一个商品域，把底层的具体商品类型屏蔽掉，因为我们现在只有collection（藏品）、
 * 后续还会有其他的商品可以售卖的，比如持有藏品（held\_collection）的转让、盲盒的销售等等
 * @author yebahe
 */
@SpringBootApplication(scanBasePackages = "cn.dp.nft.goods")
@EnableDubbo
public class NfTurboGoodsApplication {

    public static void main(String[] args) {
        SpringApplication.run(NfTurboGoodsApplication.class, args);
    }

}
