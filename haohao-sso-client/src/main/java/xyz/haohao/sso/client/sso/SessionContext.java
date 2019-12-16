package xyz.haohao.sso.client.sso;

import xyz.haohao.sso.core.sso.ticket.AppTicket;

/**
 *
 * @Author: yezaishu@qq.com
 * @Date: 2019/12/12 12:12
 */
public class SessionContext {
    private static ThreadLocal<AppTicket> userHolder = new ThreadLocal<AppTicket>();
    
    public static void setUser(AppTicket loginUser) {
        userHolder.set(loginUser);
    }
    
    public static AppTicket getUser() {
        return userHolder.get();
    }
}