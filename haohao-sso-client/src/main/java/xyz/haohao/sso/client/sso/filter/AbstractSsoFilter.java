package xyz.haohao.sso.client.sso.filter;

import xyz.haohao.sso.client.sso.conf.SsoConf;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.AntPathMatcher;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServlet;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @Author: yezaishu@qq.com
 * @Date: 2019/12/12 12:12
 */
@Log4j2
public abstract class AbstractSsoFilter extends HttpServlet implements Filter {
    @Setter
    protected SsoConf ssoConf;
    protected final AntPathMatcher antPathMatcher = new AntPathMatcher();

    protected String appKey;
    protected String appSecret;
    protected String ssoServer;
    protected String indexPath;
    protected String loginPath;
    protected String logoutPath;
    protected String callbackPath;
    protected Set<String> excludePathPatterns = new HashSet<>();

    @Override
    public void init(FilterConfig filterConfig) {
        appKey = ssoConf.getAppKey();
        appSecret = ssoConf.getAppSecret();
        ssoServer = ssoConf.getSsoServer();
        indexPath = ssoConf.getIndexPath();
        loginPath = ssoConf.getLoginPath();
        logoutPath = ssoConf.getLogoutPath();
        callbackPath = ssoConf.getCallbackPath();

        String[] commonExcludes = ssoConf.getExcludePathPatterns().split(",");

        excludePathPatterns.add(ssoConf.getCallbackPath());
        for (String s : excludePathPatterns) {
            excludePathPatterns.add(s.trim());
        }

        log.info("{} init success.", this.getClass());
    }

}
