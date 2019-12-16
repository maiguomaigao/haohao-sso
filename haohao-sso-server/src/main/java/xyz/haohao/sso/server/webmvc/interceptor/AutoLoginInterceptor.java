package xyz.haohao.sso.server.webmvc.interceptor;

import xyz.haohao.sso.core.ReturnT;
import xyz.haohao.sso.core.sso.conf.SsoParamName;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Auto Login 拦截器
 *
 * @Author: yezaishu@qq.com
 * @Date: 2019/12/12 12:12
 */
@Component
@Log4j2
public class AutoLoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private AppAuthorizationInterceptor appAuthorizationInterceptor;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String date = request.getHeader("Date") != null ?  request.getHeader("Date") : request.getParameter("Date");
        String auth = request.getHeader("Authorization") != null ? request.getHeader("Authorization") :request.getParameter("Authorization");

        if (date != null && auth != null) {// with Authorization info, do auth
            ReturnT authResult = appAuthorizationInterceptor.auth(request);
            if (authResult.isFail()) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authResult.getCode() + "-" + authResult.getMsg());
                return false;
            }

        } else {// set server side login
            request.setAttribute(SsoParamName.APP_NAME, "ssoserver");//won't find a sysapp named 'ssoserver' in sys_app table.
        }

        return true;
    }

}
