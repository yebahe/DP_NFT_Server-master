package cn.dp.nft.notice.facade;

import cn.dp.nft.notice.domain.constant.NoticeState;
import cn.dp.nft.api.notice.response.NoticeResponse;
import cn.dp.nft.api.notice.service.NoticeFacadeService;
import cn.dp.nft.base.exception.SystemException;
import cn.dp.nft.limiter.SlidingWindowRateLimiter;
import cn.dp.nft.notice.domain.entity.Notice;
import cn.dp.nft.notice.domain.service.NoticeService;
import cn.dp.nft.rpc.facade.Facade;
import cn.dp.nft.sms.SmsService;
import cn.dp.nft.sms.response.SmsSendResponse;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static cn.dp.nft.api.notice.constant.NoticeConstant.CAPTCHA_KEY_PREFIX;
import static cn.dp.nft.base.exception.BizErrorCode.SEND_NOTICE_DUPLICATED;

/**
 * @author yebahe
 */
@DubboService(version = "1.0.0")
public class NoticeFacadeServiceImpl implements NoticeFacadeService {

    @Autowired
    private SlidingWindowRateLimiter slidingWindowRateLimiter;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private SmsService smsService;

    /**
     * 生成并发送短信验证码
     *
     * @param telephone
     * @return
     */
    @Facade
    @Override
    public NoticeResponse generateAndSendSmsCaptcha(String telephone) {
        Boolean access = slidingWindowRateLimiter.tryAcquire(telephone, 1, 60);

        if (!access) {
            throw new SystemException(SEND_NOTICE_DUPLICATED);
        }

        // 生成验证码
        String captcha = RandomUtil.randomNumbers(4);

        // 验证码存入Redis
        redisTemplate.opsForValue().set(CAPTCHA_KEY_PREFIX + telephone, captcha, 5, TimeUnit.MINUTES);

        Notice notice = noticeService.saveCaptcha(telephone, captcha);

        Thread.ofVirtual().start(() -> {
            SmsSendResponse result = smsService.sendMsg(notice.getTargetAddress(), notice.getNoticeContent());
            if (result.getSuccess()) {
                notice.setState(NoticeState.SUCCESS);
                notice.setSendSuccessTime(new Date());
                noticeService.updateById(notice);
            } else {
                notice.setState(NoticeState.FAILED);
                notice.addExtendInfo("executeResult", JSON.toJSONString(result));
                noticeService.updateById(notice);
            }
        });

        return new NoticeResponse.Builder().setSuccess(true).build();
    }
}
