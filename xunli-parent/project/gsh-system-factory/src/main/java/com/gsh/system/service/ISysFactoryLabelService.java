package com.gsh.system.service;

import java.util.List;

import com.gsh.system.domain.SysFactoryLabel;

/**
 * 标签Service接口
 * 
 * @author gsh
 * @date 2019-10-17
 */
public interface ISysFactoryLabelService 
{
    /**
     * 查询标签
     * 
     * @param lableId 标签ID
     * @return 标签
     */
    public SysFactoryLabel selectSysFactoryLabelById(Long lableId);
    /**
     * 查询标签
     *
     * @param lableName 标签名称
     * @param factoryCode
     * @return 标签
     */
    public SysFactoryLabel selectSysFactoryLabelByName(String lableName , Long factoryCode);


    /**
     * 查询标签列表
     * 
     * @param sysFactoryLabel 标签
     * @return 标签集合
     */
    public List<SysFactoryLabel> selectSysFactoryLabelList(SysFactoryLabel sysFactoryLabel);

    /**
     * 新增标签
     * 
     * @param sysFactoryLabel 标签
     * @return 结果
     */
    public int insertSysFactoryLabel(SysFactoryLabel sysFactoryLabel);

    /**
     * 修改标签
     * 
     * @param sysFactoryLabel 标签
     * @return 结果
     */
    public int updateSysFactoryLabel(SysFactoryLabel sysFactoryLabel);

    /**
     * 批量删除标签
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysFactoryLabelByIds(String ids);

    /**
     * 删除标签信息
     * 
     * @param lableId 标签ID
     * @return 结果
     */
    public int deleteSysFactoryLabelById(Long lableId);
    /**
     * 批量逻辑删除标签
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int logicDeleteSysFactoryLabelByIds(String ids);

    /**
     * 逻辑删除标签信息
     *
     * @param lableId 标签ID
     * @return 结果
     */
    public int logicDeleteSysFactoryLabelById(Long lableId);

    /**
     * 获取用户对应标签
     * @param userId 用户id
     * @return 结果
     */
    public List<SysFactoryLabel>  selectLabelsByUserId(Long userId);

    public List<SysFactoryLabel> selectLabelsByPaperId(String paperid);

}
