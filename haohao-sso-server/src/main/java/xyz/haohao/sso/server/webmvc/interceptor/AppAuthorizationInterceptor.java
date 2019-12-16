package xyz.haohao.sso.server.webmvc.interceptor;

import xyz.haohao.sso.core.ReturnT;
import xyz.haohao.sso.core.sso.conf.SsoParamName;
import xyz.haohao.sso.core.security.HttpAuthorizationUtil;
import xyz.haohao.sso.core.util.HttpUtil;
import xyz.haohao.sso.dao.domain.SysApp;
import xyz.haohao.sso.dao.service.ISysAppService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * SysApp访问认证拦截器
 *
 * @Author: yezaishu@qq.com
 * @Date: 2019/12/12 12:12
 */
@Component
@Log4j2
public class AppAuthorizationInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private ISysAppService sysAppService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        ReturnT authResult = this.auth(request);
        if (authResult.isFail()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authResult.getCode() + "-" + authResult.getMsg());
            return false;
        }

        return true;
    }

    /**
     * 执行App认证
     *
     * @param request
     * @return
     */
    public ReturnT auth(HttpServletRequest request) {
        String date = request.getHeader("Date") != null ?  request.getHeader("Date") : request.getParameter("Date");
        String auth = request.getHeader("Authorization") != null ? request.getHeader("Authorization") :request.getParameter("Authorization");
        String path = request.getServletPath();
        String method = request.getMethod();

        ReturnT<SysApp> authResult = auth(date, auth, path, method);

        log.info("app auth result: {}; path={}; method={}; remoteIp={}; date={}, Authorization={}", authResult.isSuccess() ? "auth pass.": authResult.getMsg(), request.getServletPath(), request.getMethod(), HttpUtil.getRealIp(request), date, auth);

        if (authResult.isSuccess()) {
            request.setAttribute(SsoParamName.SYS_APP, authResult.getData());
        }

        return authResult;
    }

    private ReturnT<SysApp> auth(String date, String auth, String path, String method) {
        try {
            if (date == null || auth == null) {
                return ReturnT.fail("forbidden: Authorization params missing.");
            }

            String appKey = auth.substring(0, auth.indexOf(":"));

            //check date indate
            if (HttpAuthorizationUtil.isDateExpired(date)) {
                return ReturnT.fail("forbidden: date value expired.");
            }

            SysApp sysApp = sysAppService.selectByAppKey(appKey);
            if (sysApp == null) {
                return ReturnT.fail("forbidden: illegal app.");
            }

            //generate Authorizetion for matching usage
            String auth2 = HttpAuthorizationUtil.generateAuthorization(path, method, date, appKey, sysApp.getAppSecret());
            if (!auth.equals(auth2)) {
                return ReturnT.fail("forbidden: authentication failed.");
            }

            return ReturnT.success(sysApp);
        } catch (Exception e) {
            log.error("SysApp Authorization auth failed.", e);
            return ReturnT.fail("forbidden: Authorization Exception."+e.getMessage());
        }
    }
}
