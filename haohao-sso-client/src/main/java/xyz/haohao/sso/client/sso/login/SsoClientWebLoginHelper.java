package xyz.haohao.sso.client.sso.login;

import xyz.haohao.sso.client.sso.conf.SsoConf;
import xyz.haohao.sso.client.sso.ticket.TicketDao;
import xyz.haohao.sso.core.sso.conf.SsoParamName;
import xyz.haohao.sso.core.sso.ticket.AppTicket;
import xyz.haohao.sso.core.util.CookieUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 基于Cookie的登录处理
 *
 * @Author: yezaishu@qq.com
 * @Date: 2019/12/12 12:12
 */
@Component
@Log4j2
public class SsoClientWebLoginHelper {
    private static SsoClientWebLoginHelper loginHelper;

    @Autowired
    private TicketDao ticketDao;
    @Autowired
    private SsoConf ssoConf;

    @PostConstruct
    public void init() {
        loginHelper = this;
        loginHelper.ticketDao = this.ticketDao;
        loginHelper.ssoConf = ssoConf;
    }

    /**
     * 登入处理
     *
     * @param response
     */
    public static void login(HttpServletResponse response, AppTicket ticket) {
        // 存储session
        loginHelper.ticketDao.save(ticket);
        CookieUtil.set(response, SsoParamName.APP_TICKET_ID, ticket.getTid(), false);
    }

    /**
     * 检查是否登录
     * 已登录：返回SsoUser
     * 否则，返回null
     *
     * @param request
     * @param response
     * @return
     */
    public static AppTicket loginCheck(HttpServletRequest request, HttpServletResponse response){
        String tid = CookieUtil.getValue(request, SsoParamName.APP_TICKET_ID);
        if (tid == null) {
            return null;
        }

        AppTicket appT = SsoClientTokenLoginHelper.loginCheck(tid);
        if (appT == null) {
            // remove old cookie
            CookieUtil.remove(request, response, SsoParamName.APP_TICKET_ID);
        }

        return appT;
    }

    /**
     * 登出处理
     *
     * @param request
     * @param response
     */
    public static void logout(HttpServletRequest request, HttpServletResponse response) {
        String tid = CookieUtil.getValue(request, SsoParamName.APP_TICKET_ID);
        if (tid == null) {
            return;
        }

        loginHelper.ticketDao.removeByTid(tid);
        CookieUtil.remove(request, response, SsoParamName.APP_TICKET_ID);
    }

}
