package xyz.haohao.sso.server.ticket;

import xyz.haohao.sso.core.sso.conf.SsoParamName;
import xyz.haohao.sso.server.util.JedisUtil;
import lombok.NonNull;

/**
 * GrantTicket dao redis impl
 *
 *
 * @author yezaishu@qq.com
 * @date 2019/11/28
 */
public class GrantTicketRedisDao {

    /**
     * a GrantTicket lives short seconds in cache.
     */
    private static final int EXPIRE_SECONDS = 30;

    private static String redisKey(@NonNull String ticketId){
        return SsoParamName.GRANT_TICKET_ID.concat(":").concat(ticketId);
    }

    /**
     * save in redis, lives 30 seconds
     *
     * @param ticket
     */
    public static void save(GrantTicket ticket) {
        JedisUtil.setObjectValue(redisKey(ticket.getTid()), ticket, EXPIRE_SECONDS);
    }

    /**
     * get and delete
     *
     * @param ticketId
     * @return
     */
    public static GrantTicket get(String ticketId) {
        String redisKey = redisKey(ticketId);
        Object objectValue = JedisUtil.getObjectValue(redisKey);
        if (objectValue != null) {
            GrantTicket globalT = (GrantTicket) objectValue;
            JedisUtil.del(redisKey);
            return globalT;
        }
        return null;
    }

}
