package xyz.haohao.sso.client.sso.ticket;

import xyz.haohao.sso.core.sso.ticket.AppTicket;
import lombok.NonNull;

public interface TicketDao {

    void save(@NonNull AppTicket t);

    AppTicket getAndFreshByTid(String tid);

    /**
     * session get by ticketId
     *
     * @param tid
     * @return
     */
    AppTicket getByTid(String tid);

    /**
     * 续期
     * @param t
     */
    AppTicket fresh(AppTicket t);

    void removeByTid(String tid);
}