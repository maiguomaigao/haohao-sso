package xyz.haohao.samples.controller;

import xyz.haohao.sso.client.sso.SessionContext;
import xyz.haohao.sso.client.sso.conf.SsoConf;
import xyz.haohao.sso.core.sso.conf.SsoParamName;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping
@Log4j2
public class IndexController {
    @Autowired
    private SsoConf ssoConf;

    @GetMapping("/")
    public String index(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException, ServletException {
        model.addAttribute(SsoParamName.APP_TICKET, SessionContext.getUser());
        return "index";
    }

}