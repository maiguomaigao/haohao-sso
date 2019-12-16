package xyz.haohao.sso.server.service.impl;

import xyz.haohao.sso.core.ReturnT;
import xyz.haohao.sso.dao.domain.SysUser;
import xyz.haohao.sso.server.security.PasswordHelper;
import xyz.haohao.sso.server.service.UserAuthenticationService;
import xyz.haohao.sso.dao.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

/**
 * @Author: zaishu.ye
 * @Date: 2019/11/25 15:49
 */
@Configuration
@PropertySource("classpath:server.properties")
@ConditionalOnProperty(name = "sso.authentication.type", havingValue = "LDAP")
@Service
@Primary
public class LdapUserAuthenticationServiceImpl implements UserAuthenticationService {

    @Autowired
    private ISysUserService ISysUserService;
    @Autowired
    private PasswordHelper passwordHelper;

    @Override
    //TODO LDAP impl
    public ReturnT<SysUser> auth(String merchantCode, String loginName, String password) {
        return null;
    }
}
