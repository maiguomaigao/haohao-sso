package xyz.haohao.sso.dao.service;

import xyz.haohao.sso.dao.domain.SysUser;

public interface ISysUserService {

    /**
     * 根据用户名查找系统用户
     * @param merchantCode
     * @param loginName
     * @return
     */
    SysUser findUser(String merchantCode, String loginName);

}
