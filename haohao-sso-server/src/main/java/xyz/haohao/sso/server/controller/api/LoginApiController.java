package xyz.haohao.sso.server.controller.api;

import xyz.haohao.sso.core.ReturnT;
import xyz.haohao.sso.core.sso.conf.SsoParamName;
import xyz.haohao.sso.server.conf.ServerConstant;
import xyz.haohao.sso.server.login.ServerSsoWebLoginHelper;
import xyz.haohao.sso.dao.domain.SysUser;
import xyz.haohao.sso.server.service.UserAuthenticationService;
import xyz.haohao.sso.server.ticket.GlobalTicket;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * login by loginName and password in server side
 *
 * @Author: yezaishu@qq.com
 * @Date: 2019/12/12 12:12
 */
@Controller
@RequestMapping("/api")
@Log4j2
public class LoginApiController implements LoginApi {

    @Autowired
    private UserAuthenticationService userAuthenticationService;

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
    @PostMapping("/login")
    public String apiLogin(HttpServletRequest request,
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

        String redirectUrl = StringUtils.isBlank(ref) ?  "/" : ref;
        return "redirect:"+ redirectUrl;
    }

    private GlobalTicket buildGlobalTicket(SysUser sysUser) {
        GlobalTicket globalT = new GlobalTicket();
        // ticket info
        globalT.setTid(UUID.randomUUID().toString().replaceAll("-", ""));
        globalT.setCreateTime(System.currentTimeMillis());
        globalT.setRefreshTime(System.currentTimeMillis());
        // user info
        globalT.setMerchantCode(sysUser.getMerchantCode());
        globalT.setUserId(String.valueOf(sysUser.getUserId()));
        globalT.setLoginName(sysUser.getLoginName());
        globalT.setUserName(sysUser.getUserName());

        return globalT;
    }
}