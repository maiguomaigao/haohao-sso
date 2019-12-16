package xyz.haohao.sso.server.controller.web;

import xyz.haohao.sso.core.sso.conf.SsoParamName;
import xyz.haohao.sso.core.sso.enums.SsoCallbackType;
import xyz.haohao.sso.core.util.URLUTF8Encoder;
import xyz.haohao.sso.dao.domain.SysApp;
import xyz.haohao.sso.server.login.ServerSsoWebLoginHelper;
import xyz.haohao.sso.server.service.UserAuthenticationService;
import xyz.haohao.sso.server.ticket.GlobalTicket;
import xyz.haohao.sso.server.ticket.GlobalTicketRedisDao;
import xyz.haohao.sso.server.ticket.GrantTicket;
import xyz.haohao.sso.server.ticket.GrantTicketRedisDao;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * AutoLogin
 * one request startup by client filter, which won't show login ui page, i call it 'a auto login' request
 * while a 'auto login' request success, it will get a grantTicketId from callback url.
 *
 * @Author: yezaishu@qq.com
 * @Date: 2019/12/12 12:12
 */
@Controller
@Log4j2
@Api("自动登录")
public class AutoLoginController {

    @Autowired
    UserAuthenticationService userAuthenticationService;

    /**
     *
     * @param model
     * @param request
     * @return
     */
    @GetMapping("/autoLogin")
    public String autoLogin(HttpServletRequest request, HttpServletResponse response, Model model) {
        log.info("new auto login request...url={}", request.getRequestURL());
        // check web browser login status
        GlobalTicket globalT = ServerSsoWebLoginHelper.loginCheck(request, response);
        if (globalT == null) {
            model.addAttribute("errorMsg", request.getParameter("errorMsg"));

            // 参数再传递
            model.addAttribute(SsoParamName.APP_NAME, request.getParameter(SsoParamName.APP_NAME));
            model.addAttribute(SsoParamName.REF, request.getParameter(SsoParamName.REF));

            // goto login page
            return "login";
        }

        // already login in one web browser instance
        SysApp sysApp = (SysApp)request.getAttribute(SsoParamName.SYS_APP);
        if (sysApp == null) {// without app param or can't recognize app，return to inner index
            return "redirect:/";
        }

        // preGrant to app
        GrantTicket grantT = preGrant(globalT, sysApp);

        // build callbackUrl
        String callbackUrl = sysApp.getUrl().concat(sysApp.getCallbackPath()).concat("?")
                .concat(SsoParamName.CALLBACK_TYPE).concat("=").concat(SsoCallbackType.AUTO_LOGIN.name())
                .concat("&").concat(SsoParamName.GRANT_TICKET_ID).concat("=").concat(grantT.getTid());

        String ref = request.getParameter(SsoParamName.REF);
        if (StringUtils.isNotBlank(ref)) {
            callbackUrl = callbackUrl.concat("&").concat(SsoParamName.REF).concat("=").concat(URLUTF8Encoder.encode(ref));
        }

        log.info("callback url: {}", callbackUrl);

        return "redirect:"+callbackUrl;
    }

    private GrantTicket preGrant(GlobalTicket globalT, SysApp sysApp){
        // create GrantTicket
        GrantTicket grantT = new GrantTicket();
        grantT.setTid(UUID.randomUUID().toString().replaceAll("-", ""));
        grantT.setGlobalTid(globalT.getTid());
        grantT.setAppKey(sysApp.getAppKey());

        globalT.preGrant(grantT);
        GlobalTicketRedisDao.save(globalT);
        GrantTicketRedisDao.save(grantT);

        return grantT;
    }
}