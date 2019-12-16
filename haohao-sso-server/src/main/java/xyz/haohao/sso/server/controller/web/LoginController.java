package xyz.haohao.sso.server.controller.web;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import xyz.haohao.sso.core.ReturnT;
import xyz.haohao.sso.core.sso.conf.SsoParamName;
import xyz.haohao.sso.core.sso.enums.SsoCallbackType;
import xyz.haohao.sso.core.security.HttpAuthorizationUtil;
import xyz.haohao.sso.server.conf.ServerConf;
import xyz.haohao.sso.server.conf.ServerConstant;
import xyz.haohao.sso.server.login.ServerSsoWebLoginHelper;
import xyz.haohao.sso.dao.domain.SysApp;
import xyz.haohao.sso.dao.domain.SysUser;
import xyz.haohao.sso.dao.service.ISysAppService;
import xyz.haohao.sso.server.service.UserAuthenticationService;
import xyz.haohao.sso.server.ticket.GlobalTicket;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * login by loginName and password in server side
 *
 * @Author: yezaishu@qq.com
 * @Date: 2019/12/12 12:12
 */
@Controller
@Log4j2
public class LoginController {

    @Autowired
    private ServerConf serverConf;

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    @Autowired
    private ISysAppService sysAppService;

    /**
     * Logout
     *
     * @param request
     * @param redirectAttributes
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        GlobalTicket globalT = ServerSsoWebLoginHelper.loginCheck(request, response);

        ServerSsoWebLoginHelper.logout(request, response);
        this.evict(globalT);

        redirectAttributes.addAttribute(SsoParamName.REF, request.getParameter(SsoParamName.REF));
        return "redirect:/autoLogin";
    }

    @Async
    void evict(GlobalTicket globalT) {
        if (globalT == null) {
            return;
        }

        for (String appKey : globalT.getAppTicketsGranted().keySet()) {
            String appTid = globalT.getAppTicketsGranted().get(appKey);
            if (appTid != null) {
                evict(appKey, globalT.getUserId());
            }
        }
    }

    @Async
    public void evict(String appKey, String tid) {
        log.info("evict app ticket from {}. tid={} ", appKey, tid);

        SysApp sysApp = sysAppService.selectByAppKey(appKey);
        if (sysApp == null) return;

        OkHttpClient okHttpClient = new OkHttpClient();

        String date = HttpAuthorizationUtil.date();
        String authorization = HttpAuthorizationUtil.generateAuthorization(sysApp.getCallbackPath(), "GET", date, sysApp.getAppKey(), sysApp.getAppSecret());

        String url = sysApp.getUrl().concat(sysApp.getCallbackPath()).concat("?")
                .concat(SsoParamName.CALLBACK_TYPE).concat("=").concat(SsoCallbackType.AUTO_EVICT.name())
                .concat("&").concat(SsoParamName.APP_TICKET_ID).concat("=").concat(tid);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Date", date)
                .addHeader("Authorization", authorization)
                .build();

        okhttp3.Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            log.info("evictUser from {}, response: {}", sysApp.getAppName(), response);
            if (response.isSuccessful()) {
                String body = response.body().string();//body.string() 只能被获取一次
                ReturnT returnT = JSONObject.parseObject(body, new TypeReference<ReturnT>(){});
                log.info("evictUser result from {}: {}", sysApp.getAppName(), returnT);

                return;
            } else {
                log.warn("evictUser from {}, fail. code={}, message={}", sysApp.getAppName(), response.code(), response.message());
            }
        } catch (IOException e) {
            log.error("call app auto logout exception.", e);
        }
    }

    /**
     * Login by merchantCode + loginName + password
     *
     * @param request
     * @param redirectAttributes
     * @param merchantCode merchantCode distributed to merchant
     * @param loginName merchant employee's login name
     * @param password
     * @return
     */
    @PostMapping("/doLogin")
    public String doLogin(HttpServletRequest request,
                          HttpServletResponse response,
                          RedirectAttributes redirectAttributes,
                          @NonNull String merchantCode,
                          @NonNull String loginName,
                          @NonNull String password,
                          boolean rememberMe) {
        // check loginName
        ReturnT<SysUser> authResult = userAuthenticationService.auth(merchantCode, loginName, password);

        if (authResult.isFail()) {
            redirectAttributes.addAttribute("errorMsg", authResult.getMsg());
            redirectAttributes.addAttribute(SsoParamName.APP_NAME, request.getParameter(SsoParamName.APP_NAME));
            redirectAttributes.addAttribute(SsoParamName.REF, request.getParameter(SsoParamName.REF));
            return "redirect:/autoLogin";
        }

        // 1、build GlobalTicket
        GlobalTicket globalTicket = buildGlobalTicket(authResult.getData());

        // 2、login, store storeKey + cookie sessionId
        ServerSsoWebLoginHelper.login(response, globalTicket, rememberMe);

        // 3. redirect
        request.setAttribute(ServerConstant.GLOBAL_TICKET, globalTicket);

        String ref = request.getParameter(SsoParamName.REF);

        if (StringUtils.isNoneBlank(ref)) {
            return "redirect:"+ref;
        } else {//redirect to upm
            SysApp upmApp = getUpmApp();
            if (upmApp == null) {
                return "redirect:/";
            }

            return "redirect:"+upmApp.getUrl()+upmApp.getIndexPath();
        }
    }

    private GlobalTicket buildGlobalTicket(SysUser sysUser) {
        GlobalTicket globalT = new GlobalTicket();
        globalT.setTid(UUID.randomUUID().toString().replaceAll("-", ""));
        globalT.setCreateTime(System.currentTimeMillis());
        globalT.setRefreshTime(System.currentTimeMillis());
        globalT.setMerchantCode(sysUser.getMerchantCode());
        globalT.setUserId(String.valueOf(sysUser.getUserId()));
        globalT.setLoginName(sysUser.getLoginName());
        globalT.setUserName(sysUser.getUserName());

        return globalT;
    }

    private SysApp getUpmApp() {
        return sysAppService.selectByAppName(serverConf.getUpmAppName());
    }
}