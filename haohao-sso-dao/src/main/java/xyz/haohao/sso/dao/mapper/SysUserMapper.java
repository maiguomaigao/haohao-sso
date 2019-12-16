package xyz.haohao.sso.dao.mapper;

import xyz.haohao.sso.dao.domain.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户表 数据层
 *
 * @Author: yezaishu@qq.com
 * @Date: 2019/12/12 12:12
 */
@Mapper
public interface SysUserMapper {

    /**
     * 通过用户名查询用户
     *
     * @param loginName
     * @return 用户对象信息
     */
    SysUser selectUserByLoginName(@Param("merchantCode")String merchantCode, @Param("loginName") String loginName);

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    SysUser selectUserById(@Param("userId") Long userId);


}
