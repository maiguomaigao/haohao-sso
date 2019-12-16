package xyz.haohao.sso.dao.service;

import xyz.haohao.sso.dao.domain.SysApp;

import java.util.List;

/**
 * 应用管理Service接口
 * TODO 可加入缓存
 *
 * @author zaishu.ye
 * @date 2019-11-21
 */
public interface ISysAppService {
    /**
     * 查询应用管理
     * 
     * @param id 应用管理ID
     * @return 应用管理
     */
    SysApp selectById(Long id);

    /**
     * @param appName
     * @return
     */
    SysApp selectByAppName(String appName);

    SysApp selectByAppKey(String appKey);

    /**
     * 查询应用管理列表
     * 
     * @param sysApp 应用管理
     * @return 应用管理集合
     */
    List<SysApp> selectSysAppList(SysApp sysApp);

    /**
     * 新增应用管理
     * 
     * @param sysApp 应用管理
     * @return 结果
     */
    int insertSysApp(SysApp sysApp);

    /**
     * 修改应用管理
     * 
     * @param sysApp 应用管理
     * @return 结果
     */
    int updateSysApp(SysApp sysApp);

    /**
     * 批量删除应用管理
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteSysAppByIds(String ids);

    /**
     * 删除应用管理信息
     * 
     * @param id 应用管理ID
     * @return 结果
     */
    int deleteSysAppById(Long id);
}
