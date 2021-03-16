import com.twitter.vladimirbot.VladimirBotApplication;
import com.twitter.vladimirbot.service.TwitterService;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = VladimirBotApplication.class)
@ActiveProfiles(profiles = "test")
public class TwitterClientTest {

    @Resource
    TwitterService twitterService;


    @Test
    public void testCountNumbers() throws InterruptedException {

        Assert.assertEquals( 6, (int) twitterService.numberCount( "testStringWithNumber238123" ) );
        Assert.assertEquals( 8, (int) twitterService.numberCount( "tes34tStringWithNumber238123" ) );

    }
}