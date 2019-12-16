package xyz.haohao.sso.client.sso.filter.web;

import xyz.haohao.sso.client.sso.SessionContext;
import xyz.haohao.sso.client.sso.conf.SsoConf;
import xyz.haohao.sso.client.sso.filter.AbstractSsoFilter;
import xyz.haohao.sso.client.sso.login.SsoClientWebLoginHelper;
import xyz.haohao.sso.core.sso.conf.SsoParamName;
import xyz.haohao.sso.core.security.HttpAuthorizationUtil;
import xyz.haohao.sso.core.sso.ticket.AppTicket;
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
public class SsoLoginWebFilter extends AbstractSsoFilter {

    public SsoLoginWebFilter() {
    }

    public SsoLoginWebFilter(SsoConf ssoConf) {
        this.ssoConf = ssoConf;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String servletPath = req.getServletPath();

        // ignore filter free requests
        for (String pattern : excludePathPatterns) {
            // ANT表达式匹配
            if (antPathMatcher.match(pattern, servletPath)) {
                log.info("path: {} excluded.", servletPath);
                chain.doFilter(request, response);//filter free
                return;
            }
        }

        // valid login user, cookie + redirect
        AppTicket t = SsoClientWebLoginHelper.loginCheck(req, res);

        // invalid login, set http Authorization param, redirect to login page
        if (t == null) {
            String contentType = req.getHeader("content-type");
            boolean isJson = contentType != null && contentType.contains("json");
            if (isJson) {// json format response
                res.setContentType("application/json;charset=utf-8");
                res.getWriter().println("{\"code\":" + HttpServletResponse.SC_UNAUTHORIZED + ", \"msg\":\"UNAUTHORIZED\"}");
                return;
            }

            // redirect to sso server login page
            String serverLoginUrl = ssoServer.concat(loginPath);

            //set authorization params in url. [Date], [Authorization]
            String date = HttpAuthorizationUtil.date();
            String authorization = HttpAuthorizationUtil.generateAuthorization(loginPath, "GET", date, appKey, appSecret);

            // concat auth params
            serverLoginUrl = serverLoginUrl.concat("?Date=").concat(URLUTF8Encoder.encode(date));
            serverLoginUrl = serverLoginUrl.concat("&Authorization=").concat(authorization);

            // concat current url as redirect
            String ref = req.getRequestURL().toString();
            if (ref.endsWith("/favicon.ico"))
                ref = ref.substring(0, ref.indexOf("/favicon.ico"));

            serverLoginUrl = serverLoginUrl.concat("&").concat(SsoParamName.REF).concat("=").concat(URLUTF8Encoder.encode(ref));

            log.info("redirecting to:{}", serverLoginUrl);

            res.sendRedirect(serverLoginUrl);
            return;
        }

        // put session in request
        ((HttpServletRequest) request).getSession().setAttribute(t.getTid(), t);

        SessionContext.setUser(t);
        // already login, goto next filter
        chain.doFilter(request, response);
        return;
    }

}
