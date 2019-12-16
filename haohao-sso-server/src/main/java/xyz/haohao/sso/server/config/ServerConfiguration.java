package xyz.haohao.sso.server.config;

import xyz.haohao.sso.server.conf.ServerConf;
import xyz.haohao.sso.server.util.JedisUtil;
import xyz.haohao.sso.server.ticket.GlobalTicketRedisDao;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 *
 * @Author: yezaishu@qq.com
 * @Date: 2019/12/12 12:12
 */
@Configuration
@PropertySource("classpath:server.properties")
public class ServerConfiguration implements InitializingBean, DisposableBean {

    @Autowired
    ServerConf serverConf;

    @Override
    public void afterPropertiesSet() throws Exception {
        GlobalTicketRedisDao.setExpireMinutes(serverConf.getGlobalTicketExpireMinutes());
        JedisUtil.init(serverConf.getRedisAddress());
    }

    @Override
    public void destroy() throws Exception {
        JedisUtil.close();
    }
}
