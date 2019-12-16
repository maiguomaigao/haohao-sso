package xyz.haohao.sso.dao.service.impl;

import xyz.haohao.sso.dao.domain.SysUser;
import xyz.haohao.sso.dao.mapper.SysUserMapper;
import xyz.haohao.sso.dao.service.ISysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements ISysUserService {

    @Autowired
    private SysUserMapper userMapper;

    @Override
    public SysUser findUser(String merchantCode, String loginName) {
        if (StringUtils.isBlank(merchantCode) || StringUtils.isBlank(loginName)) {
            return null;
        }

        SysUser sysUser = userMapper.selectUserByLoginName(merchantCode, loginName);

        if (sysUser == null) {
            return null;
        }

        return sysUser;
    }


}
