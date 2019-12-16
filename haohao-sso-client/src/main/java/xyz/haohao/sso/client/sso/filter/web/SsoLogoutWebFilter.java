package xyz.haohao.sso.client.sso.filter.web;

import xyz.haohao.sso.client.sso.conf.SsoConf;
import xyz.haohao.sso.client.sso.filter.AbstractSsoFilter;
import xyz.haohao.sso.client.sso.login.SsoClientWebLoginHelper;
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
public class SsoLogoutWebFilter extends AbstractSsoFilter {

    public SsoLogoutWebFilter(SsoConf ssoConf) {
        this.ssoConf = ssoConf;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("logout...");

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // 1. remove AppTicket Cookie
        SsoClientWebLoginHelper.logout(req, res);

        // 3. redirect to logout Page
        res.sendRedirect(ssoServer.concat(logoutPath));
        return;
    }
}
