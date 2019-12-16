package xyz.haohao.sso.server.ticket;

import lombok.Data;

import java.io.Serializable;

/**
 * GrantTicket
 * GrantTicket is just use for AppTicket granting check.
 * GrantTicket is a shortly alive object, it will be destroyed after one granting checking, immediately.
 *
 * @author yezaishu@qq.com
 * @Date: 2019/11/28
 */
@Data
public class GrantTicket implements Serializable {
    private static final long serialVersionUID = 31L;
    String tid;
    String globalTid;
    String appKey;
}
