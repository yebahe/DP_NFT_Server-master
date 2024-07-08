package cn.dp.nft.pay.infrastructure.channel.wechat.service.impl;

import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import cn.dp.nft.pay.infrastructure.channel.wechat.response.WxPayChannelResponse;
import cn.dp.nft.api.pay.constant.PayChannel;
import cn.dp.nft.base.utils.MoneyUtils;
import cn.dp.nft.pay.application.service.PayApplicationService;
import cn.dp.nft.pay.domain.event.PaySuccessEvent;
import cn.dp.nft.pay.infrastructure.channel.wechat.entity.WxNotifyEntity;
import com.alibaba.fastjson.JSON;

import cn.dp.nft.pay.infrastructure.channel.wechat.entity.WxPayBean;
import cn.dp.nft.pay.infrastructure.channel.common.request.PayChannelRequest;
import cn.dp.nft.pay.infrastructure.channel.common.response.PayChannelResponse;
import cn.dp.nft.pay.infrastructure.channel.common.service.PayChannelService;
import cn.dp.nft.pay.infrastructure.channel.common.utils.HttpKit;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.json.JSONUtil;
import com.ijpay.core.IJPayHttpResponse;
import com.ijpay.core.enums.AuthTypeEnum;
import com.ijpay.core.enums.RequestMethodEnum;
import com.ijpay.core.kit.PayKit;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.core.utils.DateTimeZoneUtil;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.enums.WxDomainEnum;
import com.ijpay.wxpay.enums.v3.BasePayApiEnum;
import com.ijpay.wxpay.model.v3.Amount;
import com.ijpay.wxpay.model.v3.UnifiedOrderModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static cn.dp.nft.base.response.ResponseCode.SUCCESS;

/**
 * @author yebahe
 */
@Service("wechatPayChannelService")
@Slf4j
public class WxPayChannelServiceImpl implements PayChannelService {
    @Autowired
    WxPayBean wxPayBean;

    @Autowired
    private PayApplicationService payApplicationService;

    String serialNo;

    @Override
    public PayChannelResponse pay(PayChannelRequest payChannelRequest) {
        WxPayChannelResponse resp = new WxPayChannelResponse();

        try {
            String timeExpire = DateTimeZoneUtil.dateToTimeZone(System.currentTimeMillis() + 1000 * 60 * 3);
            UnifiedOrderModel unifiedOrderModel = new UnifiedOrderModel()
                    .setAppid(wxPayBean.getAppId())
                    .setMchid(wxPayBean.getMchId())
                    .setDescription(payChannelRequest.getDescription())
                    .setOut_trade_no(payChannelRequest.getOrderId())
                    .setTime_expire(timeExpire)
                    //附加数据，暂时先设置为与商品信息一致
                    .setAttach(payChannelRequest.getAttach())
                    .setNotify_url(wxPayBean.getDomain().concat("/wxPay/payNotify"))
                    .setAmount(new Amount().setTotal(Integer.valueOf(String.valueOf(payChannelRequest.getAmount()))));

            log.info("request {}", JSONUtil.toJsonStr(unifiedOrderModel));
            IJPayHttpResponse response = WxPayApi.v3(
                    RequestMethodEnum.POST,
                    WxDomainEnum.CHINA.toString(),
                    BasePayApiEnum.NATIVE_PAY.toString(),
                    wxPayBean.getMchId(),
                    getSerialNumber(),
                    null,
                    wxPayBean.getKeyPath(),
                    JSONUtil.toJsonStr(unifiedOrderModel),
                    AuthTypeEnum.RSA.getCode()
            );
            log.info("response {}", response);
            // 根据证书序列号查询对应的证书来验证签名结果
            boolean verifySignature = WxPayKit.verifySignature(response, wxPayBean.getPlatformCertPath());
            log.info("verifySignature: {}", verifySignature);
            String body = response.getBody();
            Map bodyMap = JSON.parseObject(body, Map.class);
            resp.setPayUrl(bodyMap.get("code_url").toString());
            resp.setSuccess(true);
            return resp;
        } catch (Exception e) {
            log.error("pay error ", e);
            resp.setSuccess(false);
            return resp;
        }
    }

    @Override
    public boolean notify(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> map = new HashMap<>(12);
        try {
            String timestamp = request.getHeader("Wechatpay-Timestamp");
            String nonce = request.getHeader("Wechatpay-Nonce");
            String serialNo = request.getHeader("Wechatpay-Serial");
            String signature = request.getHeader("Wechatpay-Signature");

            log.info("timestamp:{} nonce:{} serialNo:{} signature:{}", timestamp, nonce, serialNo, signature);
            String result = HttpKit.readData(request);
            log.info("支付通知密文 {}", result);

            // 需要通过证书序列号查找对应的证书，verifyNotify 中有验证证书的序列号
            String plainText = WxPayKit.verifyNotify(serialNo, result, signature, nonce, timestamp,
                    wxPayBean.getApiKey3(), wxPayBean.getPlatformCertPath());

            log.info("支付通知明文 {}", plainText);
            if (StrUtil.isEmpty(plainText)) {
                response.setStatus(500);
                map.put("code", "ERROR");
                map.put("message", "签名错误");
            }

            WxNotifyEntity wxNotifyEntity = JSON.parseObject(plainText, WxNotifyEntity.class);

            PaySuccessEvent paySuccessEvent = new PaySuccessEvent();
            paySuccessEvent.setChannelStreamId(wxNotifyEntity.getTransactionId());
            paySuccessEvent.setPaidAmount(MoneyUtils.centToYuan(Long.valueOf(wxNotifyEntity.getAmount().getTotal())));
            paySuccessEvent.setPayOrderId(wxNotifyEntity.getOutTradeNo());
            paySuccessEvent.setPaySucceedTime(DateUtil.parseUTC(wxNotifyEntity.getSuccessTime()));
            paySuccessEvent.setPayChannel(PayChannel.WECHAT);

            boolean paySuccessResult = payApplicationService.paySuccess(paySuccessEvent);

            if (paySuccessResult) {
                response.setStatus(200);
                map.put("code", SUCCESS.name());
                map.put("message", SUCCESS.name());
            } else {
                response.setStatus(500);
                map.put("code", "ERROR");
                map.put("message", "内部处理失败");
            }

            response.setHeader("Content-type", ContentType.JSON.toString());
            response.getOutputStream().write(JSONUtil.toJsonStr(map).getBytes(StandardCharsets.UTF_8));
            response.flushBuffer();
        } catch (Exception e) {
            log.error("nofity error", e);
            return false;
        }
        return true;
    }

    private String getSerialNumber() {
        if (StrUtil.isEmpty(serialNo)) {
            // 获取证书序列号
            X509Certificate certificate = PayKit.getCertificate(wxPayBean.getCertPath());
            if (null != certificate) {
                serialNo = certificate.getSerialNumber().toString(16).toUpperCase();
                // 提前两天检查证书是否有效
                boolean isValid = PayKit.checkCertificateIsValid(certificate, wxPayBean.getMchId(), -2);
                log.info("cert is valid {} effectiveTime {}", isValid, DateUtil.format(certificate.getNotAfter(), DatePattern.NORM_DATETIME_PATTERN));
            }
        }
        System.out.println("serialNo:" + serialNo);
        return serialNo;
    }
}
