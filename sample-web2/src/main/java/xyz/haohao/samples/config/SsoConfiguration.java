package xyz.haohao.samples.config;

import xyz.haohao.sso.client.redis.JedisUtil;
import xyz.haohao.sso.client.sso.conf.SsoConf;
import xyz.haohao.sso.client.sso.filter.web.SsoCallbackWebFilter;
import xyz.haohao.sso.client.sso.filter.web.SsoLoginWebFilter;
import xyz.haohao.sso.client.sso.filter.web.SsoLogoutWebFilter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Log4j2
public class SsoConfiguration implements InitializingBean, DisposableBean {

    @Autowired
    private SsoConf ssoConf;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("-------------SsoConf------------ \n{}\n", ssoConf);
        JedisUtil.init(ssoConf.getRedisAddress());
    }

    @Override
    public void destroy() throws Exception {
        JedisUtil.close();
    }

    @Bean
    public FilterRegistrationBean ssoLoginWebFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();

        SsoLoginWebFilter filter = new SsoLoginWebFilter(ssoConf);

        registration.setFilter(filter);
        registration.setName(filter.getClass().getName());
        registration.setOrder(1);
        registration.addUrlPatterns("/*");
        return registration;
    }

    @Bean
    public FilterRegistrationBean ssoLogoutFilterRegistration() {
        // filter init
        FilterRegistrationBean registration = new FilterRegistrationBean();

        SsoLogoutWebFilter filter = new SsoLogoutWebFilter(ssoConf);

        registration.setFilter(filter);
        registration.setName(filter.getClass().getName());
        registration.setOrder(2);
        registration.addUrlPatterns(ssoConf.getLogoutPath());
        return registration;
    }

    @Bean
    public FilterRegistrationBean ssoCallbackFilterRegistration() {
        // filter init
        FilterRegistrationBean registration = new FilterRegistrationBean();

        SsoCallbackWebFilter filter = new SsoCallbackWebFilter(ssoConf);

        registration.setFilter(filter);
        registration.setName(filter.getClass().getName());
        registration.setOrder(3);
        registration.addUrlPatterns(ssoConf.getCallbackPath());
        return registration;
    }

/*

    @Bean
    public FilterRegistrationBean appHttpSessionFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();

        AppSessionFilter filter = new AppSessionFilter(ssoConf);
        registration.setFilter(filter);
        registration.setName("AppHttpSessionFilter");
        registration.setOrder(2);
        registration.addUrlPatterns("/*");
        return registration;
    }
*/


}
