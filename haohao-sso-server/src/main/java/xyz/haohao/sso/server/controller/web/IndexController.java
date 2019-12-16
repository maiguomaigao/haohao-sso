package xyz.haohao.sso.server.controller.web;

import xyz.haohao.sso.core.util.CookieUtil;
import xyz.haohao.sso.server.conf.ServerConstant;
import xyz.haohao.sso.server.login.ServerSsoWebLoginHelper;
import xyz.haohao.sso.dao.domain.SysApp;
import xyz.haohao.sso.dao.service.ISysAppService;
import xyz.haohao.sso.server.ticket.GlobalTicket;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @Author: yezaishu@qq.com
 * @Date: 2019/12/12 12:12
 */
@Controller
@Log4j2
public class IndexController {

    @Autowired
    private ISysAppService sysAppService;

    @GetMapping("/")
    public String index(Model model, HttpServletRequest request, HttpServletResponse response) {
        // 检查是否登录
        GlobalTicket globalTicket = ServerSsoWebLoginHelper.loginCheck(request, response);

        if (globalTicket == null) {
            return "redirect:/autoLogin";
        }
        model.addAttribute(ServerConstant.GLOBAL_TICKET_ID,  CookieUtil.getValue(request, ServerConstant.GLOBAL_TICKET_ID));
        model.addAttribute(ServerConstant.GLOBAL_TICKET, globalTicket);

        // 列出所有已接入应用
        List<SysApp> sysApps = sysAppService.selectSysAppList(new SysApp());
        model.addAttribute("sysApps", sysApps);

        return "index";
    }


}