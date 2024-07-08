package cn.dp.nft.api.notice.service;


import cn.dp.nft.api.notice.response.NoticeResponse;

/**
 * @author yebahe
 */
public interface NoticeFacadeService {
    /**
     * 生成并发送短信验证码
     *
     * @param telephone
     * @return
     */
    public NoticeResponse generateAndSendSmsCaptcha(String telephone);
}
