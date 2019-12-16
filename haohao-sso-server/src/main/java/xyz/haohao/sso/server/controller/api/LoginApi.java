package xyz.haohao.sso.server.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.NonNull;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: zaishu.ye
 * @Date: 2019/12/4 16:35
 */
@Api("登录")
public interface LoginApi {

    @ApiOperation(value = "雇员登录",
            notes = "",
            httpMethod = "POST")
    String apiLogin(HttpServletRequest request,
                   HttpServletResponse response,
                   RedirectAttributes redirectAttributes,
                   @ApiParam(name = "merchantCode", value = "商户号", required = true, example = "makro") @NonNull String merchantCode,
                   @ApiParam(name = "loginName", value = "雇员登录名", required = true, example = "testuser") @NonNull String loginName,
                   @ApiParam(name = "password", value = "雇员登录密码: Base64(原始密码)", required = true)@NonNull String password,
                   @ApiParam(name = "rememberMe", value = "是否记住登录状态", defaultValue = "false") boolean rememberMe);


}
