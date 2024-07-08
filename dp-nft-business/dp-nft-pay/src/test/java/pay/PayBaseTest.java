package pay;

import cn.dp.nft.limiter.SlidingWindowRateLimiter;
import cn.dp.nft.pay.NfTurboPayApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {NfTurboPayApplication.class})
@ActiveProfiles("test")
public class PayBaseTest {
    @MockBean
    private RedissonClient redissonClient;

    @MockBean
    protected SlidingWindowRateLimiter slidingWindowRateLimiter;

    @Test
    public void test(){

    }
}
