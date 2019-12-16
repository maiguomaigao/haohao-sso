package xyz.haohao.sso.server.ticket;

import xyz.haohao.sso.server.conf.ServerConstant;
import xyz.haohao.sso.server.util.JedisUtil;
import lombok.NonNull;

/**
 * GlobalTicket dao redis impl
 *
 * @Author: yezaishu@qq.com
 * @Date: 2019/12/12 12:12
 */
public class GlobalTicketRedisDao {

    private static int expireMinutes;
    public static void setExpireMinutes(int minutes) {
        expireMinutes = minutes;
    }

    public static int getExpireMinutes() {
        return expireMinutes;
    }

    private static final String PREFIX = ServerConstant.GLOBAL_TICKET_ID.concat(":");
    private static String redisKey(@NonNull String tid){
        return PREFIX.concat(tid);
    }

    private static String redisKey(@NonNull GlobalTicket t){
        return redisKey(t.getTid());
    }

    /**
     * get
     *
     * @param tid
     * @return
     */
    public static GlobalTicket get(String tid) {
        if (tid == null) {
            return null;
        }

        Object objectValue = JedisUtil.getObjectValue(redisKey(tid));
        return objectValue != null ? (GlobalTicket) objectValue : null;
    }

    /**
     * remove
     *
     * @param t
     */
    public static void del(@NonNull GlobalTicket t) {
        del(redisKey(t.getTid()));
    }

    /**
     * remove
     *
     * @param tid
     */
    public static void del(String tid) {
        if (tid == null) {
            return;
        }
        JedisUtil.del(redisKey(tid));
    }

    /**
     * put
     * @param t
     */
    public static void save(@NonNull GlobalTicket t) {
        t.setRefreshTime(System.currentTimeMillis());
        JedisUtil.setObjectValue(redisKey(t), t, expireMinutes * 60);  // minite to second
    }

    /**
     * session续期
     * 1/4过期时长之后，续期,否则不续期
     * @param t
     * @return
     */
    public static void fresh(GlobalTicket t) {
        if (t == null) {
            return;
        }
        if ((System.currentTimeMillis() - t.getRefreshTime()) > expireMinutes * 60 * 1000 /4) {
            t.setRefreshTime(System.currentTimeMillis());
            save(t);
        }
    }

}
