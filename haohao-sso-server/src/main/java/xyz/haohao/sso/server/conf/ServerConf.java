package xyz.haohao.sso.server.conf;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 *
 * @Author: yezaishu@qq.com
 * @Date: 2019/12/12 12:12
 */
@Configuration
@PropertySource("classpath:server.properties")
@Data
public class ServerConf {
    @Value("${server.conf.redis.address}")
    private String redisAddress;

    @Value("${server.conf.global.ticket.expire.minutes:720}")
    private int globalTicketExpireMinutes;

    @Value("${server.conf.upmAppName:haohao-upm}")
    private String upmAppName;
}
