package xyz.haohao.sso.server.webmvc.resolver;

import xyz.haohao.sso.core.ReturnT;
import xyz.haohao.sso.core.sso.SsoException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 统一异常处理（Controller切面方式实现）
 *
 *  1、@ControllerAdvice：扫描所有Controller；
 *  2、@ControllerAdvice(annotations=RestController.class)：扫描指定注解类型的Controller；
 *  3、@ControllerAdvice(basePackages={"com.aaa","com.bbb"})：扫描指定package下的Controller
 *
 * @Author: yezaishu@qq.com
 * @Date: 2019/12/12 12:12
 */
@Component
@Log4j2
@ControllerAdvice
public class WebExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Object handler,
                                         Exception ex) {

        log.error("WebExceptionResolver:{}", ex);

        // if json
        boolean isJson = false;
        HandlerMethod method = (HandlerMethod)handler;
        ResponseBody responseBody = method.getMethodAnnotation(ResponseBody.class);
        if (responseBody != null) {
            isJson = true;
        }

        // error result
        ReturnT<String> errorResult = null;
        if (ex instanceof SsoException) {
            errorResult = new ReturnT(ReturnT.FAIL_CODE, ex.getMessage());
        } else {
            errorResult = new ReturnT(ReturnT.FAIL_CODE, ex.toString().replaceAll("\n", "<br/>"));
        }

        // response
        ModelAndView mv = new ModelAndView();
        if (isJson) {
            try {
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().print("{\"code\":"+errorResult.getCode()+", \"msg\":\""+ errorResult.getMsg() +"\"}");
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
            return mv;
        } else {
            mv.addObject("exceptionMsg", errorResult.getMsg());
            mv.setViewName("exception");
            return mv;
        }
    }

}