package xyz.haohao.sso.client.sso.ticket;

import xyz.haohao.sso.client.sso.conf.SsoConf;
import xyz.haohao.sso.client.redis.JedisUtil;
import xyz.haohao.sso.core.sso.conf.SsoParamName;
import xyz.haohao.sso.core.sso.ticket.AppTicket;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class TicketDaoRedisImpl implements TicketDao {

    public static final int EXPIRE_MINUTES = 30;

    @Autowired
    private SsoConf ssoConf;

    private String prefix;
    private String redisKey(@NonNull String tid){
        if (prefix == null) {
            prefix = ssoConf.getAppName() + ":" + SsoParamName.APP_TICKET_ID + ":";
        }
        return prefix.concat(tid);
    }

    @Override
    public AppTicket getAndFreshByTid(String tid) {
        AppTicket t = getByTid(tid);
        return fresh(t);
    }

    /**
     * get AppTicket by appTicketId
     *
     * @param tid
     * @return
     */
    @Override
    public AppTicket getByTid(String tid) {
        if (tid == null) {
            return null;
        }

        Object objectValue = JedisUtil.getObjectValue(redisKey(tid));
        if (objectValue != null) {
            return (AppTicket) objectValue;
        }
        return null;
    }

    /**
     * put
     *
     * @param t
     */
    @Override
    public void save(@NonNull AppTicket t) {
        JedisUtil.setObjectValue(redisKey(t.getTid()), t, EXPIRE_MINUTES * 60);  // minite to second
    }

    /**
     * session续期
     * 1/4过期时长之后，续期,否则不续期
     * @param t
     * @return
     */
    @Override
    public AppTicket fresh(AppTicket t) {
        if (t == null) {
            return null;
        }

        long now = System.currentTimeMillis();
        if ((now - t.getRefreshTime()) > EXPIRE_MINUTES * 60 * 1000 /4) {
            t.setRefreshTime(now);
            save(t);
        }

        return t;
    }

    @Override
    public void removeByTid(String tid) {
        if (tid == null) {
            return;
        }
        JedisUtil.del(redisKey(tid));
    }

}
