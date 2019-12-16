package xyz.haohao.sso.server;

import xyz.haohao.sso.core.ReturnT;

/**
 * @Author: zaishu.ye
 * @Date: 2019/11/19 19:02
 */
public class SsoError {

    /**
     * login fail result
     */
    public static final ReturnT ERROR_1000_NO_LOGIN = new ReturnT(1000, "sso not login.");

    public static final ReturnT ERROR_1001_INVALID_GRANT_TICKET = new ReturnT(1001, "invalid grant ticket. invalid appKey");

    public static final ReturnT ERROR_1002_INVALID_GRANT_TICKET = new ReturnT(1002, "invalid grant ticket. invalid GlobalTicket");


}
