package xyz.haohao.sso.client.sso.filter.web;

import com.alibaba.fastjson.JSONObject;
import xyz.haohao.sso.client.sso.conf.SsoConf;
import xyz.haohao.sso.client.sso.filter.AbstractSsoFilter;
import xyz.haohao.sso.client.sso.login.SsoClientTokenLoginHelper;
import xyz.haohao.sso.client.sso.login.SsoClientWebLoginHelper;
import xyz.haohao.sso.client.sso.net.ServerApiCaller;
import xyz.haohao.sso.core.ReturnT;
import xyz.haohao.sso.core.security.HttpAuthorizationUtil;
import xyz.haohao.sso.core.sso.conf.SsoParamName;
import xyz.haohao.sso.core.sso.enums.SsoCallbackType;
import xyz.haohao.sso.core.sso.ticket.AppTicket;
import xyz.haohao.sso.core.util.SpringContextUtil;
import xyz.haohao.sso.core.util.URLUTF8Encoder;
import lombok.extern.log4j.Log4j2;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Cookie Base implement
 *
 * @Author: yezaishu@qq.com
 * @Date: 2019/12/12 12:12
 */
@Log4j2
public class SsoCallbackWebFilter extends AbstractSsoFilter {

    public SsoCallbackWebFilter(SsoConf ssoConf) {
        this.ssoConf = ssoConf;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (!serverAuth(request)) {
            log.warn("server auth fail.");
            return;
        }

        String callbackTypeStr = request.getParameter(SsoParamName.CALLBACK_TYPE);
        SsoCallbackType ssoCallbackType = SsoCallbackType.valueOf(callbackTypeStr);
        if (ssoCallbackType == null) {
            return;
        }

        log.info("server '{}' callback.", ssoCallbackType);

        switch (ssoCallbackType) {
            case AUTO_LOGIN:{
                autoLoginCallback(request, response, chain);
            }
            case AUTO_EVICT:{
                autoEvictCallback(request, response, chain);
            }

        }
    }

    /**
     *
     */
    private boolean serverAuth(ServletRequest req) {
        HttpServletRequest request = (HttpServletRequest)req;

        String date = request.getHeader("Date") != null ?  request.getHeader("Date") : request.getParameter("Date");
        String auth = request.getHeader("Authorization") != null ? request.getHeader("Authorization") :request.getParameter("Authorization");
        String path = request.getServletPath();
        String method = request.getMethod();

        if (date == null || auth == null) {
            return false;
        }

        //check date indate
        if (HttpAuthorizationUtil.isDateExpired(date)) {
            return false;
        }

        //generate Authorizetion for matching usage
        String auth2 = HttpAuthorizationUtil.generateAuthorization(path, method, date, appKey, appSecret);
        return auth.equals(auth2);
    }

    /**
     *  Ticket callback
     *  1. get grantTicketId
     *  2. verify grantTicketId, and obtains AppTicket
     *  3. other app processes...
     *     such as: create common HttpSession, write cookies, save grantTicketId in localstorage...etc...
     */
    private void autoLoginCallback(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException {
        log.info("Auto Login Callback");

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String tid = request.getParameter(SsoParamName.GRANT_TICKET_ID);
        if (tid == null) {
            return;
        }

        // verify grantTicketId and obtain AppTicket
        ServerApiCaller apiCaller = SpringContextUtil.getBean(ServerApiCaller.class);
        ReturnT<AppTicket> result = apiCaller.verifyGrantTicket(tid);
        log.info("verify GrantTicket result: {}", result);

        // write cookie
        if (result.isSuccess()) {
            log.info("Auto Login Success.");
            SsoClientWebLoginHelper.login(res, result.getData());

            String refUrl = request.getParameter(SsoParamName.REF);
            String redirectUrl = (refUrl != null && refUrl.trim().length() > 0) ? refUrl : indexPath;

            // auto login success. redirect to refUrl or index
            log.info("Auto Login Success. redirect to :{}", redirectUrl);
            res.sendRedirect(redirectUrl);
            //chain.doFilter(request, response);
        } else {
            String link = req.getRequestURL().toString();
            link = antPathMatcher.match("/favicon.**", link) ? link.substring(0, link.indexOf("/favicon")) : link;

            String redirectUrl = ssoServer.concat(loginPath).concat("?").concat(SsoParamName.REF).concat("=").concat(URLUTF8Encoder.encode(link));

            log.warn("Auto Login Fail. redirect to :{}", redirectUrl);
            res.sendRedirect(redirectUrl);
        }

        return;
    }

    private void autoEvictCallback(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String tid = request.getParameter(SsoParamName.APP_TICKET_ID);
        log.info("Auto Evict Callback tid={}", tid);

        // remove AppTicket Cache
        SsoClientTokenLoginHelper.logout(tid);

        res.setStatus(HttpServletResponse.SC_OK);
        res.setContentType("application/json;charset=UTF-8");

        res.getWriter().println(JSONObject.toJSON(ReturnT.success()));

        log.info("Auto Evict Success. tid={}", tid);
        return;
    }
}
