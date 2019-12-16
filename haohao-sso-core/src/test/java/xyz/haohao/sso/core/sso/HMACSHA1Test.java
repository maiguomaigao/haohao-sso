package xyz.haohao.sso.core.sso;

import xyz.haohao.sso.core.security.HMACSHA1;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;

/**
 * @Author: zaishu.ye
 * @Date: 2019/11/25 20:26
 */
@Log4j2
public class HMACSHA1Test {
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    @Test
    public void testDate() throws InterruptedException {
        HMACSHA1.getSignature("124", "456");



    }


}
