package xyz.haohao.sso.server.login;

import xyz.haohao.sso.server.conf.ServerConstant;
import xyz.haohao.sso.server.ticket.GlobalTicket;
import xyz.haohao.sso.server.ticket.GlobalTicketRedisDao;

import javax.servlet.http.HttpServletRequest;

/**
 * 基于Token的登录处理（不使用cookie，token需要每次请求时使用header传入）
 *
 * @Author: yezaishu@qq.com
 * @Date: 2019/12/12 12:12
 */
public class ServerSsoTokenLoginHelper {

    /**
     * 保存登录session
     *
     * @param globalTicket
     */
    public static void login(GlobalTicket globalTicket) {
        GlobalTicketRedisDao.save(globalTicket);
    }

    /**
     * client logout
     *
     * @param globalTid
     */
    public static void logout(String globalTid) {
        GlobalTicketRedisDao.del(globalTid);
    }

    /**
     * 登出
     *
     * @param request
     */
    public static void logout(HttpServletRequest request) {
        String globalTid = request.getHeader(ServerConstant.GLOBAL_TICKET_ID);
        logout(globalTid);
    }

    /**
     * 检查sessionId有效性
     *
     * @param globalTid
     * @return
     */
    public static GlobalTicket loginCheck(String globalTid){
        if (globalTid == null) {
            return null;
        }

        GlobalTicket globalTicket = GlobalTicketRedisDao.get(globalTid);
        return globalTicket != null ? globalTicket : null;
    }

    /**
     * login check
     *
     * @param request
     * @return
     */
    public static GlobalTicket loginCheck(HttpServletRequest request){
        String globalTid = request.getHeader(ServerConstant.GLOBAL_TICKET_ID);
        return loginCheck(globalTid);
    }
}
