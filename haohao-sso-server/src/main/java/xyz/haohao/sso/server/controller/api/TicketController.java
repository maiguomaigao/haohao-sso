package xyz.haohao.sso.server.controller.api;

import xyz.haohao.sso.core.ReturnT;
import xyz.haohao.sso.server.SsoError;
import xyz.haohao.sso.core.sso.conf.SsoParamName;
import xyz.haohao.sso.core.sso.ticket.AppTicket;
import xyz.haohao.sso.dao.domain.SysApp;
import xyz.haohao.sso.server.ticket.GlobalTicket;
import xyz.haohao.sso.server.ticket.GlobalTicketRedisDao;
import xyz.haohao.sso.server.ticket.GrantTicket;
import xyz.haohao.sso.server.ticket.GrantTicketRedisDao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 *
 * @Author: yezaishu@qq.com
 * @Date: 2019/12/12 12:12
 */
@RestController
@RequestMapping("/api/ticket")
@Log4j2
@Api(value = "GrantTicket")
public class TicketController {

    @GetMapping("/verifyGrantTicket")
    @ApiOperation(value = "验证GRANT_TICKET_ID", notes = "验证GRANT_TICKET_ID有效性。应用系统在callback里收到GRANT_TICKET_ID后，访问本接口以验证是否有效，并获取登录用户信息(AppTicket)")
    public ReturnT<AppTicket> verifyGrantTicket(HttpServletRequest request, HttpServletResponse response,
                                                @RequestParam(SsoParamName.GRANT_TICKET_ID) String grantTicketId) {

        log.info("new verify request: grantTicketId={}", grantTicketId);

        SysApp sysApp = (SysApp)request.getAttribute(SsoParamName.SYS_APP);

        log.info("auth path sys app: {}", sysApp);

        GrantTicket grantT = GrantTicketRedisDao.get(grantTicketId);
        log.info("GrantTicket get: {}", grantT);

        if (grantT == null) {
            return SsoError.ERROR_1000_NO_LOGIN;
        }

        // match app
        if (!grantT.getAppKey().equals(sysApp.getAppKey())) {
            return SsoError.ERROR_1001_INVALID_GRANT_TICKET;
        }

        // check GlobalTicket
        GlobalTicket globalT = GlobalTicketRedisDao.get(grantT.getGlobalTid());
        log.info("globalT: {}", globalT);
        if (globalT == null) {
            return SsoError.ERROR_1002_INVALID_GRANT_TICKET;
        }

        // build AppTicket, grant(band) AppTicket, update GlobalTicket
        AppTicket appT = buildAppTicket(globalT);
        globalT.grant(grantT, appT);
        GlobalTicketRedisDao.save(globalT);

        return ReturnT.success(appT);
    }

    private AppTicket buildAppTicket(GlobalTicket globalT) {
        AppTicket appT = new AppTicket();
        appT.setTid(UUID.randomUUID().toString().replaceAll("-", ""));
        appT.setLoginType(globalT.getLoginType());

        // user info
        appT.setMerchantCode(globalT.getMerchantCode());
        appT.setUserId(globalT.getUserId());
        appT.setLoginName(globalT.getLoginName());
        appT.setUserName(globalT.getUserName());

        return appT;
    }
}