package xyz.haohao.sso.dao.domain;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 应用管理对象 sys_app
 *
 * @Author: yezaishu@qq.com
 * @Date: 2019/12/12 12:12
 */
@Data
@ToString(exclude = "appSecret")
public class SysApp extends BaseDomain
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** app_name */
    private String appName;

    /** url */
    private String url;

    /** indexPath */
    private String indexPath;

    /** ip list */
    private String ip;

    /** app key */
    private String appKey;

    /** app secret */
    private String appSecret;

    /** callback path */
    private String callbackPath;

    /** locked */
    private Boolean locked;

    /** gmt create */
    private Date gmtCreate;

    /** gmt update */
    private Date gmtUpdate;

}
