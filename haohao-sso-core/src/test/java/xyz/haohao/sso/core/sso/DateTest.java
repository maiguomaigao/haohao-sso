package xyz.haohao.sso.core.sso;

import xyz.haohao.sso.core.security.HttpAuthorizationUtil;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @Author: zaishu.ye
 * @Date: 2019/11/25 20:26
 */
@Log4j2
public class DateTest {
    public static final ZoneId UTC = ZoneId.of("UTC");
    public static final String UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(UTC_FORMAT).withZone(UTC);
    public static final int IN_DATE_SECONDS = 10;//10秒内有效



    @Test
    public void testDate() throws InterruptedException {

        Instant instant = ZonedDateTime.now().toInstant();
        long epochSecond = instant.getEpochSecond();
log.info(new Timestamp(epochSecond));
        log.info(Timestamp.from(ZonedDateTime.now().toInstant()));

        log.info(System.currentTimeMillis());

        log.info(new Timestamp(System.currentTimeMillis()));


        log.info(LocalDateTime.ofInstant(Instant.ofEpochMilli(new Date().getTime()), UTC).format(FORMATTER));
        log.info(LocalDateTime.now(UTC).format(FORMATTER));

        System.out.println(DateTimeFormatter.ISO_ORDINAL_DATE.format(ZonedDateTime.now()));
        System.out.println(DateTimeFormatter.ISO_DATE_TIME.format(ZonedDateTime.now()));
        System.out.println(DateTimeFormatter.ISO_DATE.format(ZonedDateTime.now()));
        System.out.println(DateTimeFormatter.ISO_INSTANT.format(ZonedDateTime.now()));
        System.out.println(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(ZonedDateTime.now()));
        System.out.println(DateTimeFormatter.ISO_LOCAL_DATE.format(ZonedDateTime.now()));
        System.out.println(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(ZonedDateTime.now()));
        System.out.println(DateTimeFormatter.ISO_OFFSET_DATE.format(ZonedDateTime.now()));
        System.out.println(DateTimeFormatter.ISO_OFFSET_TIME.format(ZonedDateTime.now()));
        System.out.println(DateTimeFormatter.ISO_OFFSET_DATE.format(ZonedDateTime.now()));
        System.out.println(DateTimeFormatter.ISO_OFFSET_DATE.format(ZonedDateTime.now()));
        System.out.println(DateTimeFormatter.ISO_OFFSET_DATE.format(ZonedDateTime.now()));
        System.out.println(DateTimeFormatter.ISO_OFFSET_DATE.format(ZonedDateTime.now()));
        System.out.println(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(ZonedDateTime.now()));
        System.out.println(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(ZonedDateTime.now()));
        log.info("--------");
        String s = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(ZonedDateTime.now());
        log.info(s);
        ZonedDateTime t = ZonedDateTime.from(DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(s));
        log.info(t.toOffsetDateTime());
        log.info(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(t));
        ZonedDateTime t2 = t.plusSeconds(10);
        log.info(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(t));
        log.info(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(t2));


        String s1 = "2019-11-25T20:57:13.016+08:00", s2= "2019-11-25T14:57:13.016+02:00";
        log.info(ZonedDateTime.from(DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(s1)));
        log.info(ZonedDateTime.from(DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(s2)));

        log.info(ZonedDateTime.from(DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(s1)).isEqual(ZonedDateTime.from(DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(s2))));


        String d = HttpAuthorizationUtil.date();
        log.info(d);
        ZonedDateTime authDate = ZonedDateTime.from(DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(d));
        log.info(authDate);
        log.info(authDate.plusSeconds(10));

        //Thread.sleep(5000);
        log.info(HttpAuthorizationUtil.isDateExpired(d));


        log.info(authDate.plusSeconds(IN_DATE_SECONDS));




        //Thread.sleep(5000);
        log.info(HttpAuthorizationUtil.isDateExpired(d));



    }


}
