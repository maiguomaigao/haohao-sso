package xyz.haohao.sso.client.sso.test;

import xyz.haohao.sso.core.util.URLUTF8Encoder;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;

/**
 * @Author: zaishu.ye
 * @Date: 2019/11/29 13:10
 */
@Log4j2
public class UrlEncoderTest {

    @Test
    public void testUrlPathEncoder() {
        String url = "Date=2019-11-29T12:35:47.858+08";

        log.info("URLUTF8Encoder encoded: {}", URLUTF8Encoder.encode(url));
        log.info("URLUTF8Encoder encoded: {}", URLUTF8Encoder.encode(url));

        String expected = "date%3d2019-11-29t12%3a35%3a47.858%2b08";
        //Assert.assertEquals(expected, URLPathEncoder.encodePath(url));



    }
}
