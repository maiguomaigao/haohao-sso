package xyz.haohao.sso.client.sso.conf;

import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


/**
 *
 * @Author: yezaishu@qq.com
 * @Date: 2019/12/12 12:12
 */
@Configuration
@PropertySource("classpath:sso.properties")
@Data
@ToString(exclude = "appSecret")
public class SsoConf {

    // ********* required configs *********
    @Value("${sso.app.name}")
    private String appName;

    @Value("${sso.app.key}")
    private String appKey;

    @Value("${sso.app.secret}")
    private String appSecret;

    @Value("${sso.server}")
    private String ssoServer;



    // ********* optional configs *********
    @Value("${sso.path.index:/}")
    private String indexPath;

    @Value("${sso.path.login:/autoLogin}")
    private String loginPath;

    @Value("${sso.path.logout:/logout}")
    private String logoutPath;

    @Value("${sso.path.excludePathPatterns:}")
    private String excludePathPatterns;

    @Value("${sso.path.callback:/sso/callback}")
    private String callbackPath;

    @Value("${sso.app.httpsession.enable:false}")
    private String clientSessionEnable;

    @Value("${sso.app.httpsession.sessionid:appsid}")
    private String clientSessionId;

/*
    @Value("${sso.app.domains}")
    private String appDomains;
*/

    @Value("${sso.redis.address:}")
    private String redisAddress;
/*

    @Value("${sso.redis.database}")
    private int database;
*/



}
