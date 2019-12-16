package xyz.haohao.sso.core.sso.ticket;

import xyz.haohao.sso.core.sso.enums.LoginType;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * @Author: yezaishu@qq.com
 * @Date: 2019/12/12 12:12
 */
@Data
public class AppTicket implements Serializable {
    private static final long serialVersionUID = 42L;

    // ticket info
    private String tid;
    private long grantTime = System.currentTimeMillis();
    private long refreshTime = System.currentTimeMillis();
    private LoginType loginType;

    // user info
    private String merchantCode;
    private String userId;
    private String loginName;
    private String userName;
}
