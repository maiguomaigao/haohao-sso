package xyz.haohao.sso.dao.service.impl;

import xyz.haohao.sso.dao.domain.SysApp;
import xyz.haohao.sso.dao.mapper.SysAppMapper;
import xyz.haohao.sso.dao.service.ISysAppService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 应用Service业务层处理
 * 
 * @author zaishu.ye
 * @date 2019-11-21
 */
@Service
public class SysAppServiceImpl implements ISysAppService {
    @Autowired
    private SysAppMapper sysAppMapper;

    /**
     * 查询应用管理
     * 
     * @param id 应用管理ID
     * @return 应用管理
     */
    @Override
    public SysApp selectById(Long id) {
        return sysAppMapper.selectById(id);
    }

    @Override
    public SysApp selectByAppName(String appName) {
        return StringUtils.isBlank(appName)? null : sysAppMapper.selectByAppName(appName);
    }

    @Override
    public SysApp selectByAppKey(String appKey) {
        return StringUtils.isBlank(appKey)? null : sysAppMapper.selectByAppKey(appKey);
    }

    /**
     * 查询应用管理列表
     * 
     * @param sysApp 应用管理
     * @return 应用管理
     */
    @Override
    public List<SysApp> selectSysAppList(SysApp sysApp) {
        return sysAppMapper.selectSysAppList(sysApp);
    }

    /**
     * 新增应用管理
     * 
     * @param sysApp 应用管理
     * @return 结果
     */
    @Override
    public int insertSysApp(SysApp sysApp) {
        return sysAppMapper.insertSysApp(sysApp);
    }

    /**
     * 修改应用管理
     * 
     * @param sysApp 应用管理
     * @return 结果
     */
    @Override
    public int updateSysApp(SysApp sysApp) {
        return sysAppMapper.updateSysApp(sysApp);
    }

    /**
     * 删除应用管理对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysAppByIds(String ids) {
        return StringUtils.isBlank(ids) ? 0 : sysAppMapper.deleteSysAppByIds(ids.split(","));
    }

    /**
     * 删除应用管理信息
     * 
     * @param id 应用管理ID
     * @return 结果
     */
    @Override
    public int deleteSysAppById(Long id) {
        return id == null || id <= 0 ? 0 : sysAppMapper.deleteSysAppById(id);
    }
}
