package xyz.haohao.sso.client.sso.login;

import xyz.haohao.sso.client.sso.net.ServerApiCaller;
import xyz.haohao.sso.client.sso.ticket.TicketDao;
import xyz.haohao.sso.core.sso.conf.SsoParamName;
import xyz.haohao.sso.core.sso.ticket.AppTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * 基于Token的登录登出处理（不使用cookie，token需要每次请求时使用header传入）
 *
 * @Author: yezaishu@qq.com
 * @Date: 2019/12/12 12:12
 */
@Component
public class SsoClientTokenLoginHelper {

    @Autowired
    private ServerApiCaller serverApiCaller;
    @Autowired
    private TicketDao ticketDao;

    private static SsoClientTokenLoginHelper loginHelper;

    @PostConstruct
    public void init() {
        loginHelper = this;
        loginHelper.serverApiCaller = this.serverApiCaller;
        loginHelper.ticketDao = this.ticketDao;
    }

    /**
     * 校验sessionId
     * 1. 校验sessionId有效性
     * 2. session续期
     *
     * @param appTid
     * @return
     */
    public static AppTicket loginCheck(String appTid){
        if (appTid == null) {
            return null;
        }

        return loginHelper.ticketDao.getAndFreshByTid(appTid);
    }

    /**
     * token模式登录检查
     * 1.从header里取globalTicketId
     * 2.校验globalTicketId
     *
     * @param request
     * @return
     */
    public static AppTicket loginCheck(HttpServletRequest request){
        return loginCheck(request.getHeader(SsoParamName.APP_TICKET_ID));
    }

    /**
     * 登出
     *
     * @param request
     */
    public static void logout(HttpServletRequest request) {
        String tid = request.getHeader(SsoParamName.APP_TICKET_ID);
        loginHelper.ticketDao.removeByTid(tid);
    }

    public static void logout(String tid) {
        loginHelper.ticketDao.removeByTid(tid);
    }
}
