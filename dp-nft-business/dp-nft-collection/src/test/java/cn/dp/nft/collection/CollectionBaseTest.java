package cn.dp.nft.collection;

import cn.dp.nft.api.chain.service.ChainFacadeService;
import cn.dp.nft.api.collection.service.CollectionFacadeService;
import cn.dp.nft.api.order.OrderFacadeService;
import cn.dp.nft.api.user.service.UserFacadeService;
import cn.dp.nft.limiter.SlidingWindowRateLimiter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {NfTurboCollectionApplication.class})
@ActiveProfiles("test")
public class CollectionBaseTest {

    @MockBean
    private RedissonClient redissonClient;

    @MockBean
    protected SlidingWindowRateLimiter slidingWindowRateLimiter;

    @MockBean
    private ChainFacadeService chainFacadeService;

    @MockBean
    private UserFacadeService userFacadeService;

    @MockBean
    private CollectionFacadeService collectionFacadeService;

    @MockBean
    private OrderFacadeService orderFacadeService;

    @Test
    public void test(){

    }
}
