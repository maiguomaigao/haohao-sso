package xyz.haohao.sso.dao.domain;

import lombok.Data;
import lombok.ToString;

/**
 *
 * @Author: yezaishu@qq.com
 * @Date: 2019/12/12 12:12
 */
@Data
@ToString(exclude = "credential")
public class SysUser extends BaseDomain {
    private static final long serialVersionUID = 1L;

    /**
     * 商户号
     */
    private String merchantCode;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 登录名称
     */
    private String loginName;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 密码
     */
    private String credential;

    /**
     * 盐加密
     */
    private String salt;

}
