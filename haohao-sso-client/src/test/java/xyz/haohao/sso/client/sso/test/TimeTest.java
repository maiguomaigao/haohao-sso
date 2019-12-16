package xyz.haohao.sso.client.sso.test;

import xyz.haohao.sso.core.sso.ticket.AppTicket;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;

/**
 * @Author: zaishu.ye
 * @Date: 2019/12/2 10:22
 */
@Log4j2
public class TimeTest {
    public static final int EXPIRE_MINUTES = 30;

    @Test
    public void test(){
        AppTicket t = new AppTicket();
        log.info(t);

        long now = System.currentTimeMillis();
        log.info("{}-{}={}", now, t.getRefreshTime(), now - t.getRefreshTime());

        if ((now - t.getRefreshTime()) > EXPIRE_MINUTES * 60 * 1000 /4) {
            t.setRefreshTime(System.currentTimeMillis());
        }

        log.info(t);
    }
}
