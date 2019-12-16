package xyz.haohao.sso.core.security;

import lombok.extern.log4j.Log4j2;
import xyz.haohao.sso.core.ReturnT;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @Author: yezaishu@qq.com
 * @Date: 2019/12/12 12:12
 */
@Log4j2
public class HttpAuthorizationUtil {

    public static final long DATE_EXPIRE_SECONDS = 30;//30秒内有效
    static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    /**
     * @param path
     * @param method
     * @param date     时间请使用HttpAuthorizationUtil#date()获取
     * @param appKey
     * @param appSecret
     * @return
     */
    public static String generateAuthorization(String path, String method, String date, String appKey, String appSecret) {
        //log.info("generateAuthorization: path={}; method={}; date={}, appKey={}", path, method, date, appKey);

        String stringToSign = method + " " + path + "\n" + date;
        String signature = HMACSHA1.getSignature(stringToSign, appSecret);
        String authorization = appKey + ":" + signature;
        return authorization;
    }

    public static String date() {
        return FORMATTER.format(ZonedDateTime.now());
    }

    /**
     * 校验用于签名的日期有效期，30秒内有效，所以服务器必须设置ntp
     * 验证Date是否有效，超过30秒认定无效
     * 强制每次必须重新生成新的Date
     * @param date
     * @return
     */
    public static boolean isDateExpired(String date) {
        if (date == null) {
            return false;
        }

        try {
            ZonedDateTime authDate = ZonedDateTime.from(FORMATTER.parse(date));
            ZonedDateTime now = ZonedDateTime.now();

            long diff = now.toInstant().getEpochSecond() - authDate.toInstant().getEpochSecond();

            return !(diff >= 0 && diff <= DATE_EXPIRE_SECONDS);
        }catch (Exception e) {
            log.error("Date format error.", e);
        }
        return false;
    }
}
