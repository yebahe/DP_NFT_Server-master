package cn.dp.nft.sms;

import cn.dp.nft.lock.DistributeLock;
import cn.dp.nft.sms.response.SmsSendResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mock短信服务
 *
 * @author yebahe
 */
@Slf4j
@Setter
public class MockSmsService implements SmsService {

    private static Logger logger = LoggerFactory.getLogger(MockSmsService.class);

    @DistributeLock(scene = "SEND_SMS", keyExpression = "#phoneNumber")
    @Override
    public SmsSendResponse sendMsg(String phoneNumber, String code) {
        SmsSendResponse smsSendResponse = new SmsSendResponse();
        smsSendResponse.setSuccess(true);
        return smsSendResponse;
    }
}
