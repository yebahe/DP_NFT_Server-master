package cn.dp.nft.notice.job;

import cn.dp.nft.notice.domain.constant.NoticeState;
import cn.dp.nft.notice.domain.entity.Notice;
import cn.dp.nft.notice.domain.service.NoticeService;
import cn.dp.nft.sms.SmsService;
import cn.dp.nft.sms.response.SmsSendResponse;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 定时任务：通知发送重试任务
 * 这个任务会定期执行，处理因失败而需要重试发送的通知
 * 使用XXL-JOB框架实现
 *
 * @author yebahe
 */
@Component  // 将该类注册为Spring组件，便于依赖注入
public class NoticeSendRetryJob {

    // 注入通知服务，负责数据库操作，如查询需要重试的通知和更新通知状态
    @Autowired
    private NoticeService noticeService;

    // 注入短信服务，用于发送短信
    @Autowired
    private SmsService smsService;

    // 每页处理100条通知记录，分页查询
    private static final int PAGE_SIZE = 100;

    // 日志记录器，用于记录任务执行中的日志信息
    private static final Logger LOG = LoggerFactory.getLogger(NoticeSendRetryJob.class);

    // 定时任务的执行方法，XXL-JOB调度框架会调用此方法
    @XxlJob("noticeSendRetryJob")
    public ReturnT<String> execute() {

        int currentPage = 1; // 当前处理的页码，从第一页开始
        // 分页查询需要重试的通知，每页100条数据
        Page<Notice> page = noticeService.pageQueryForRetry(currentPage, PAGE_SIZE);

        // 处理当前页中的每条通知，调用executeSingle方法
        page.getRecords().forEach(this::executeSingle);

        // 如果还有下一页，继续分页查询，直到所有数据处理完毕
        while (page.hasNext()) {
            currentPage++; // 增加页码
            // 查询下一页的数据
            page = noticeService.pageQueryForRetry(currentPage, PAGE_SIZE);
            // 处理下一页中的每条通知
            page.getRecords().forEach(this::executeSingle);
        }

        // 返回任务成功状态
        return ReturnT.SUCCESS;
    }

    /**
     * 对单个通知进行重试发送的处理逻辑
     * @param notice 需要重试的通知
     */
    private void executeSingle(Notice notice) {
        // 打印日志，标记当前正在处理的通知ID
        LOG.info("start to execute notice retry , noticeId is {}", notice.getId());

        // 调用短信发送服务，发送短信到通知的目标地址，并返回发送结果
        SmsSendResponse result = smsService.sendMsg(notice.getTargetAddress(), notice.getNoticeContent());

        // 根据短信发送的结果来更新通知的状态
        if (result.getSuccess()) {  // 如果短信发送成功
            notice.setState(NoticeState.SUCCESS);  // 更新通知状态为成功
            notice.setSendSuccessTime(new Date()); // 记录成功发送的时间
            notice.setLockVersion(1);  // 更新乐观锁版本，避免并发问题
        } else {  // 如果短信发送失败
            notice.setState(NoticeState.FAILED);  // 更新通知状态为失败
            notice.setLockVersion(1);  // 更新乐观锁版本
            // 将失败的结果存储到扩展信息中，以便后续排查问题
            notice.addExtendInfo("executeResult", JSON.toJSONString(result));
        }
        // 将更新后的通知状态写回数据库
        noticeService.updateById(notice);
    }

    @XxlJob("noticePushTask")
    public ReturnT<String> executePushTask() {
        // 去数据库任务推送表中查找数据
        // 查出需要推送的消息
        //  return
        return null;

    }
}
