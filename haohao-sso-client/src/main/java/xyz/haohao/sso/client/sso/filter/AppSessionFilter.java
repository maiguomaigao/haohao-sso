package xyz.haohao.sso.client.sso.filter;

import xyz.haohao.sso.client.sso.conf.SsoConf;
import lombok.extern.log4j.Log4j2;

import javax.servlet.*;
import java.io.IOException;

/**
 *
 * @Author: yezaishu@qq.com
 * @Date: 2019/12/12 12:12
 */
@Log4j2
public class AppSessionFilter extends AbstractSsoFilter {

    public AppSessionFilter(SsoConf ssoConf) {
        this.ssoConf = ssoConf;
    }

    private String appSessionEnable;
    private String appSessionId;

    @Override
    public void init(FilterConfig filterConfig) {
        appSessionEnable = super.ssoConf.getClientSessionEnable();
        appSessionId = super.ssoConf.getClientSessionId();

        log.info("AppSessionFilter init success.");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("---- AppSessionFilter----appSessionEnable={}; appSessionId={}", appSessionEnable, appSessionId);
        chain.doFilter(request, response);
        return;
    }
}
