package xyz.haohao.sso.server.service.impl;

import xyz.haohao.sso.core.ReturnT;
import xyz.haohao.sso.dao.domain.SysUser;
import xyz.haohao.sso.server.util.MessageUtil;
import xyz.haohao.sso.server.security.PasswordHelper;
import xyz.haohao.sso.server.service.UserAuthenticationService;
import xyz.haohao.sso.dao.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

/**
 * @Author: zaishu.ye
 * @Date: 2019/11/25 15:49
 */
@Configuration
@PropertySource("classpath:server.properties")
@ConditionalOnMissingBean(LdapUserAuthenticationServiceImpl.class)
@Service
public class JdbcUserAuthenticationServiceImpl implements UserAuthenticationService {

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private PasswordHelper passwordHelper;

    @Override
    public ReturnT<SysUser> auth(String merchantCode, String loginName, String password) {
        SysUser sysUser = sysUserService.findUser(merchantCode, loginName);

        if (sysUser == null) {
            return ReturnT.fail(MessageUtil.message("login.error.loginName"));
        }

        // 匹配密码
        if (!passwordHelper.matchPassword(password, sysUser)) {
            return ReturnT.fail(MessageUtil.message("login.error.password"));
        }

        return ReturnT.success(sysUser);
    }
}
