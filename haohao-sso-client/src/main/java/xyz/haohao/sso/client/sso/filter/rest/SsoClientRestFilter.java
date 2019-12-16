package xyz.haohao.sso.client.sso.filter.rest;

import xyz.haohao.sso.client.sso.SessionContext;
import xyz.haohao.sso.client.sso.conf.SsoConf;
import xyz.haohao.sso.client.sso.filter.AbstractSsoFilter;
import xyz.haohao.sso.client.sso.login.SsoClientTokenLoginHelper;
import xyz.haohao.sso.core.ReturnT;
import xyz.haohao.sso.core.sso.ticket.AppTicket;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token sso filter
 * Token base implement.
 *
 * @Author: yezaishu@qq.com
 * @Date: 2019/12/12 12:12
 */
@Log4j2
public class SsoClientRestFilter extends AbstractSsoFilter {

    public SsoClientRestFilter(SsoConf ssoConf) {
        this.ssoConf = ssoConf;
    }

    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private String logoutPath;
    private String excludePathPatterns;


    @Override
    public void init(FilterConfig filterConfig) {
        logoutPath = ssoConf.getLogoutPath();
        excludePathPatterns = ssoConf.getExcludePathPatterns();

        log.info("SsoClientTokenFilter init.");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String servletPath = req.getServletPath();

        // exclude filter free request
        if (excludePathPatterns != null && excludePathPatterns.trim().length() > 0) {
            for (String path : excludePathPatterns.split(",")) {
                String pattern = path.trim();

                // 支持ANT表达式
                if (antPathMatcher.match(pattern, servletPath)) {
                    // excluded path, allow
                    chain.doFilter(request, response);
                    return;
                }

            }
        }

        // filter logout request
        if (antPathMatcher.match(logoutPath, servletPath)) {
            // logout
            SsoClientTokenLoginHelper.logout(req);

            // response
            res.setStatus(HttpServletResponse.SC_OK);
            res.setContentType("application/json;charset=UTF-8");
            res.getWriter().println("{\"code\":" + ReturnT.SUCCESS_CODE + ", \"msg\":\"\"}");

            return;
        }

        // login check
        AppTicket t = SsoClientTokenLoginHelper.loginCheck(req);
        if (t == null) {
            res.setStatus(HttpServletResponse.SC_OK);
            res.setContentType("application/json;charset=UTF-8");
            res.getWriter().println("{\"code\":" + HttpServletResponse.SC_UNAUTHORIZED + ", \"msg\":\"UNAUTHORIZED\"}");
            return;
        }

        // set sso user
        SessionContext.setUser(t);

        // already login, allow
        chain.doFilter(request, response);
        return;
    }


}
