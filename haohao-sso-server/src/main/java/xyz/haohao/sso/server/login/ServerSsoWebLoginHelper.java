package xyz.haohao.sso.server.login;

import xyz.haohao.sso.core.util.CookieUtil;
import xyz.haohao.sso.server.conf.ServerConstant;
import xyz.haohao.sso.server.ticket.GlobalTicket;
import xyz.haohao.sso.server.ticket.GlobalTicketRedisDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 基于Cookie的登录处理
 *
 * @Author: yezaishu@qq.com
 * @Date: 2019/12/12 12:12
 */
public class ServerSsoWebLoginHelper {


    /**
     * 登入处理
     *
     * @param response
     */
    public static void login(HttpServletResponse response, GlobalTicket globalTicket, boolean rememberMe) {
        // 存储session
        GlobalTicketRedisDao.save(globalTicket);

        //设置Cookie
        CookieUtil.set(response, ServerConstant.GLOBAL_TICKET_ID, globalTicket.getTid(), rememberMe);
    }

    /**
     * 登出处理
     *
     * @param request
     * @param response
     */
    public static void logout(HttpServletRequest request, HttpServletResponse response) {
        String globalTid = CookieUtil.getValue(request, ServerConstant.GLOBAL_TICKET_ID);
        if (globalTid==null) {
            return;
        }

        GlobalTicketRedisDao.del(globalTid);
        CookieUtil.remove(request, response, ServerConstant.GLOBAL_TICKET_ID);
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
    public static GlobalTicket loginCheck(HttpServletRequest request, HttpServletResponse response){
        String globalTid = CookieUtil.getValue(request, ServerConstant.GLOBAL_TICKET_ID);
        if (globalTid == null) {
            return null;
        }

        // cookie user
        GlobalTicket globalTicket = ServerSsoTokenLoginHelper.loginCheck(globalTid);
        if (globalTicket != null) {
            return globalTicket;
        }

        // redirect user

        // remove old cookie
        ServerSsoWebLoginHelper.removeGlobalTicketIdByCookie(request, response);

        // set new cookie
        String globalTicketId = request.getParameter(ServerConstant.GLOBAL_TICKET_ID);
        globalTicket = ServerSsoTokenLoginHelper.loginCheck(globalTicketId);
        if (globalTicket != null) {
            CookieUtil.set(response, ServerConstant.GLOBAL_TICKET_ID, globalTicketId, false);    // expire when browser close （client cookie）
            return globalTicket;
        }

        return null;
    }

    /**
     * client logout, cookie only
     *
     * @param request
     * @param response
     */
    public static void removeGlobalTicketIdByCookie(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.remove(request, response, ServerConstant.GLOBAL_TICKET_ID);
    }

    /**
     * get sessionid by cookie
     *
     * @param request
     * @return
     */
    public static String getGlobalTicketIdByCookie(HttpServletRequest request) {
        return CookieUtil.getValue(request, ServerConstant.GLOBAL_TICKET_ID);
    }
}
