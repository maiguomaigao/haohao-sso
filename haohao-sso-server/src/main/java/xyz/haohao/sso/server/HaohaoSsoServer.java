package xyz.haohao.sso.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import xyz.haohao.sso.core.util.SpringContextUtil;
import xyz.haohao.sso.server.conf.ServerConf;

/**
 * sprigboot启动类
 * @Author: yezaishu@qq.com
 * @Date: 2019/12/12 12:12
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"xyz.haohao"})
@MapperScan("xyz.haohao.sso.dao.mapper")
public class HaohaoSsoServer {

	public static void main(String[] args) {
        SpringApplication.run(HaohaoSsoServer.class, args);


        Environment env = SpringContextUtil.getBean(org.springframework.core.env.Environment.class);
		System.out.println(
				"\n   _                             _" +
				"\n  | |                           | |                            _____" +
				"\n  | |__      _____     ___      | |__      _____     ___         \\\\" +
				"\n  |  _ \\    (____ |   / _ \\     |  _ \\    (____ |   / _ \\         \\\\" +
				"\n  | | | |   / ___ |  ( (_) )    | | | |   / ___ |  ( (_) )        //" +
				"\n  |_| |_|   \\_____|   \\___/     |_| |_|   \\_____|   \\___/     ___//___" +
				"\n_|\"\"\"\"\"\"\"| |\"\"\"\"\"\"\"| |\"\"\"\"\"\"\"| |\"\"\"\"\"\"\"| |\"\"\"\"\"\"\"| |\"\"\"\"\"\"\"| |\"\"\"\"\"\"\\\\\\" +
				"\n_`-O---O-'='-O---O-'='-O---O-'='-O---O-'=`-O---O-'=`-O---O-'=`-O---O-'''" +
				"\n_________________________________________________________________________" +
				"\n " + env.getProperty("spring.application.name") +
				"\n ServerPort  : " + env.getProperty("server.port") +
				"\n ContextPath : " + env.getProperty("spring.mvc.servlet.path") +
				"\n " +SpringContextUtil.getBean(ServerConf.class) +
				"\n_________________________________________________________________________");

	}


}