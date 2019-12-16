package xyz.haohao.sso.server.controller.api;

import xyz.haohao.sso.core.ReturnT;
import xyz.haohao.sso.server.conf.ServerConstant;
import xyz.haohao.sso.server.login.ServerSsoTokenLoginHelper;
import xyz.haohao.sso.server.SsoError;
import xyz.haohao.sso.server.ticket.GlobalTicket;
import xyz.haohao.sso.server.ticket.GlobalTicketRedisDao;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @Author: yezaishu@qq.com
 * @Date: 2019/12/12 12:12
 */
@RestController
@RequestMapping("/api/ticket")
@Log4j2
public class GlobalTicketController {

    @GetMapping("/verify")
    //@ApiOperation(value = "验证GlobalTicketId", notes = "验证GlobalTicketId(globalTid)有效性。应用系统在callback里收到globalTid后，访问本接口以验证globalTid，并获取登录用户信息(GlobalTicket)")
    public ReturnT<GlobalTicket> verify(@RequestParam(name = ServerConstant.GLOBAL_TICKET_ID)String globalTid,
                                        HttpServletRequest request, HttpServletResponse response) {
        log.info("verify globalTicketId: {}", globalTid);
        GlobalTicket globalTicket = ServerSsoTokenLoginHelper.loginCheck(globalTid);
        if (globalTicket == null) {
            return SsoError.ERROR_1000_NO_LOGIN;
        }

        return ReturnT.success(globalTicket);
    }

    @GetMapping("/fresh")
    //@ApiOperation(value = "globalTid续期", notes = "当过滤器发现globalTid距离上次续期时间已经超过1/4周期时，自动调用本接口进行globalTid续期")
    public void fresh(@RequestParam(ServerConstant.GLOBAL_TICKET_ID) String globalTid) {
        log.info("fresh globalTicket: {}", globalTid);
        GlobalTicket globalTicket = ServerSsoTokenLoginHelper.loginCheck(globalTid);
        if (globalTicket == null) {
            return ;
        }

        GlobalTicketRedisDao.fresh(globalTicket);
    }

    @DeleteMapping("/remove")
    public void remove(@RequestParam(ServerConstant.GLOBAL_TICKET_ID)String globalTid) {
        log.info("remove globalTicket: {}", globalTid);
        GlobalTicket globalTicket = ServerSsoTokenLoginHelper.loginCheck(globalTid);
        if (globalTicket == null) {
            return;
        }

        GlobalTicketRedisDao.del(globalTid);
    }
}