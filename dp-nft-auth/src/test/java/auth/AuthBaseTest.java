package auth;

import cn.dp.nft.auth.NfTurboAuthApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {NfTurboAuthApplication.class})
@ActiveProfiles("test")
public class AuthBaseTest {

    @Test
    public void test(){

    }
}
