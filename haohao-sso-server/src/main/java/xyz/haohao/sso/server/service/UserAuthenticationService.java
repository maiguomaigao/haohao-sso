package xyz.haohao.sso.server.service;

import xyz.haohao.sso.core.ReturnT;
import xyz.haohao.sso.dao.domain.SysUser;

/**
 * @Author: zaishu.ye
 * @Date: 2019/11/25 15:46
 */
public interface UserAuthenticationService {
    ReturnT<SysUser> auth(String merchantCode, String loginName, String password);
}
