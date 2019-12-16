package xyz.haohao.sso.server.controller.api;

import xyz.haohao.sso.core.sso.conf.SsoParamName;
import xyz.haohao.sso.core.sso.ticket.AppTicket;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @Author: yezaishu@qq.com
 * @Date: 2019/12/12 12:12
 */
@Api("登录")
public interface TicketApi {

    @ApiOperation(value = "雇员登录",
            notes = "",
            httpMethod = "POST")
    AppTicket verify(HttpServletRequest request,
                     HttpServletResponse response,
                     RedirectAttributes redirectAttributes,
                     @ApiParam(name = "merchantCode", value = "商户编号", required = true) String merchantCode,
                     @ApiParam(name = "loginName", value = "雇员登录名", required = true) String loginName,
                     @ApiParam(name = "password", value = "雇员登录密码: Base64(原始密码)", required = true) String password,
                     @ApiParam(name = "rememberMe", value = "是否记住登录状态", defaultValue = "false") boolean rememberMe);

    @ApiOperation(value = "刷新Ticket",
    notes = "刷新Ticket请求由集成客户端主动发起，每隔5分钟进行一次，以确保所有接入应用Ticket有效期同步。",
    httpMethod = "GET")
    void refresh(@ApiParam(name = SsoParamName.APP_TICKET_ID, value = "AppTicket.tid", required = true) String tid);
}
