package cn.dp.nft.pay.infrastructure.channel.common.service;

import cn.dp.nft.api.pay.constant.PayChannel;
import cn.dp.nft.base.utils.BeanNameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static cn.dp.nft.base.constant.ProfileConstant.PROFILE_DEV;

/**
 * 支付渠道服务工厂
 *
 * @author yebahe
 */
@Service
public class PayChannelServiceFactory {

    @Autowired
    private final Map<String, PayChannelService> serviceMap = new ConcurrentHashMap<String, PayChannelService>();

    @Value("${spring.profiles.active}")
    private String profile;

    public PayChannelService get(PayChannel payChannel) {

        if (PROFILE_DEV.equals(profile)) {
            return serviceMap.get("mockPayService");
        }

        String beanName = BeanNameUtils.getBeanName(payChannel.name(), "PayChannelService");

        //组装出beanName，并从map中获取对应的bean
        PayChannelService payChannelService = serviceMap.get(beanName);

        if (payChannelService != null) {
            return payChannelService;
        } else {
            throw new UnsupportedOperationException(
                    "No PayChannelService Found With payChannel : " + payChannel);
        }
    }
}
