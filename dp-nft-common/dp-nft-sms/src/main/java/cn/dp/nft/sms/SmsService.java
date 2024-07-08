package cn.dp.nft.sms;

import cn.dp.nft.sms.response.SmsSendResponse;

/**
 * 短信服务
 *
 * @author yebahe
 */
public interface SmsService {
    /**
     * 发送短信
     *
     * @param phoneNumber
     * @param code
     * @return
     */
    public SmsSendResponse sendMsg(String phoneNumber, String code);
}
