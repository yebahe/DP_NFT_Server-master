package cn.dp.nft.notice.domain.service;

import cn.dp.nft.notice.domain.constant.NoticeState;
import cn.dp.nft.notice.domain.constant.NoticeType;
import cn.dp.nft.notice.infrastructure.mapper.NoticeMapper;
import cn.dp.nft.base.exception.BizException;
import cn.dp.nft.notice.domain.entity.Notice;
import cn.dp.nft.sms.SmsService;
import cn.dp.nft.sms.response.SmsSendResponse;
import cn.dp.nft.user.domain.entity.User;
import cn.dp.nft.user.domain.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static cn.dp.nft.base.exception.BizErrorCode.NOTICE_SAVE_FAILED;

/**
 * 通知服务
 *
 * @author yebahe
 */
@Service
@AllArgsConstructor
@Slf4j
public class NoticeService extends ServiceImpl<NoticeMapper, Notice> {

    private static final int PAGE_SIZE = 100;// 每次查询和发送的批次大小
    private static final int THREAD_POOL_SIZE = 5;  // 限制并发线程数（分批并发发送短信）

    private final UserService userService;
    private final SmsService smsService;
    private final ExecutorService executor;

    private static final String SMS_NOTICE_TITLE = "验证码";

    public Page<Notice> pageQueryForRetry(int currentPage, int pageSize) {
        // 创建分页对象 Page，指定当前页数和每页显示的记录条数
        Page<Notice> page = new Page<>(currentPage, pageSize);

        // 创建查询条件包装器 QueryWrapper，用于构造 SQL 查询条件
        QueryWrapper<Notice> wrapper = new QueryWrapper<>();

        // 查询条件：过滤出状态为 INIT 或 FAILED 的通知记录
        // 表示只查询那些状态在初始化（INIT）或失败（FAILED）之间的通知
        wrapper.between("state", NoticeState.INIT.name(), NoticeState.FAILED);

        // 排序条件：按创建时间 gmt_create 进行升序排列
        wrapper.orderBy(true, true, "gmt_create");

        // 调用分页查询方法，将分页对象 page 和查询条件 wrapper 传递进去，返回分页结果
        return this.page(page, wrapper);
    }



    public Notice saveCaptcha(String telephone, String captcha) {
        Notice notice = Notice.builder()
                .noticeTitle(SMS_NOTICE_TITLE)
                .noticeContent(captcha)
                .noticeType(NoticeType.SMS)
                .targetAddress(telephone)
                .state(NoticeState.INIT)
                .build();

        Boolean saveResult = this.save(notice);

        if (!saveResult) {
            throw new BizException(NOTICE_SAVE_FAILED);
        }

        return notice;
    }


    public void sendActivitySms(String message) {
        int currentPage = 1;
        Page<User> page; // 用户分页

        // 创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        // 分页查询用户并发送短信
        while (true) {
            page = userService.getUserPage(currentPage, PAGE_SIZE); // 分页查询用户
            List<User> users = page.getRecords(); // 获取用户列表

            if (users.isEmpty()) {
                break; // 如果没有用户，退出循环
            }

            // 提交每批次的短信发送任务
            for (User user : users) {
                executorService.submit(() -> {
                    sendSmsToUser(user, message); // 异步发送短信
                });
            }
            currentPage++; // 增加当前页码
        }

        executorService.shutdown(); // 关闭线程池
        while (!executorService.isTerminated()) {
            // 等待所有任务完成
        }
        log.info("All SMS messages have been sent.");
    }

    private void sendSmsToUser(User user, String message) {
        try {
            SmsSendResponse response = smsService.sendMsg(user.getTelephone(), message); // 发送短信
            if (response.getSuccess()) {
                log.info("SMS sent successfully to user: {}", user.getId());
            } else {
                log.error("Failed to send SMS to user: {}. Error: {}", user.getId(), response.getErrorMessage());
                // 可以实现重试逻辑
            }
        } catch (Exception e) {
            log.error("Exception occurred while sending SMS to user: {}. Error: {}", user.getId(), e.getMessage());
            // 可以实现重试逻辑
        }
    }

}
