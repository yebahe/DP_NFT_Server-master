package cn.dp.nft.chain.domain.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.dp.nft.api.chain.constant.ChainType;
import cn.dp.nft.base.utils.BeanNameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 链服务工厂
 *
 * @author yebahe
 */
@Service
public class ChainServiceFactory {

    @Autowired
    private final Map<String, ChainService> chainServiceMap = new ConcurrentHashMap<String, ChainService>();

    public ChainService get(ChainType chainType) {
        String beanName = BeanNameUtils.getBeanName(chainType.name(), "ChainService");

        //组装出beanName，并从map中获取对应的bean
        ChainService service = chainServiceMap.get(beanName);

        if (service != null) {
            return service;
        } else {
            throw new UnsupportedOperationException(
                    "No ChainService Found With chainType : " + chainType);
        }
    }
}
