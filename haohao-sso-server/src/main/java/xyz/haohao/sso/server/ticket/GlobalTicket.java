package xyz.haohao.sso.server.ticket;

import xyz.haohao.sso.core.sso.enums.LoginType;
import xyz.haohao.sso.core.sso.ticket.AppTicket;
import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * GlobalTicket
 * an GlobalTicket object proves one user had loggedin, it lives in Server side only.
 * @Author: yezaishu@qq.com
 * @Date: 2019/11/28
 */
@Data
public class GlobalTicket implements Serializable {
    private static final long serialVersionUID = 42L;

    //an uuid，use for generate a globalTicketId
    private String tid;
    private long createTime;
    private long refreshTime;

    private String merchantCode;
    private String userId;
    private String loginName;
    private String userName;
    private LoginType loginType;

    private Set<String> appsGranted = new HashSet<>();

    /**
     * key: SysApp.appKey
     * value: AppTicket.tid
     * 在GrantTicket签发后，应用未完成grantTicketId验证并签发AppTicket前，value为空串
     *
     */
    private Map<String, String> appTicketsGranted = new ConcurrentHashMap();

    /**
     * 这个过程可有可无
     */
    public void preGrant(GrantTicket grantT){
        appTicketsGranted.put(grantT.getAppKey(), "");
    }

    public void grant(GrantTicket grantT, AppTicket appT) {
        appTicketsGranted.put(grantT.getAppKey(), appT.getTid());
    }

}
