package xyz.haohao.sso.dao.mapper;

import xyz.haohao.sso.dao.domain.SysApp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 应用管理Mapper接口
 * 
 * @author zaishu.ye
 * @date 2019-11-21
 */
@Mapper
public interface SysAppMapper {
    /**
     * 查询应用
     * 
     * @param id 应用ID
     * @return 应用
     */
    SysApp selectById(Long id);

    SysApp selectByAppName(@Param("appName") String appName);

    SysApp selectByAppKey(@Param("appKey") String appKey);

    /**
     * 查询应用管理列表
     * 
     * @param sysApp 应用
     * @return 应用集合
     */
    List<SysApp> selectSysAppList(SysApp sysApp);

    /**
     * 新增应用管理
     * 
     * @param sysApp 应用
     * @return 结果
     */
    int insertSysApp(SysApp sysApp);

    /**
     * 修改应用
     * 
     * @param sysApp 应用
     * @return 结果
     */
    int updateSysApp(SysApp sysApp);

    /**
     * 删除接入应用
     * 
     * @param id 应用ID
     * @return 结果
     */
    int deleteSysAppById(Long id);

    /**
     * 批量删除应用接入
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteSysAppByIds(String[] ids);
}
