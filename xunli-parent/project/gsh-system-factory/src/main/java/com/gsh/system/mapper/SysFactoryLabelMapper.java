package com.gsh.system.mapper;

import java.util.List;

import com.gsh.system.domain.SysFactoryLabel;
import org.apache.ibatis.annotations.Param;

/**
 * 标签Mapper接口
 * 
 * @author gsh
 * @date 2019-10-17
 */
public interface SysFactoryLabelMapper 
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
    public SysFactoryLabel selectSysFactoryLabelByName(@Param("lableName") String lableName , @Param("factoryCode") Long factoryCode);

    /**
     * 查询标签列表
     * 
     * @param sysFactoryLabel 标签
     * @return 标签集合
     */
    public List<SysFactoryLabel> selectSysFactoryLabelList(SysFactoryLabel sysFactoryLabel);
    /**
     * 查询标签列表
     *
     * @param userid 标签
     * @return 标签集合
     */
    public List<SysFactoryLabel> selectLabelsByUserId(Long userid);

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
     * 删除标签
     *
     * @param lableId 标签ID
     * @return 结果
     */
    public int deleteSysFactoryLabelById(Long lableId);

    /**
     * 批量删除标签
     *
     * @param lableIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysFactoryLabelByIds(String[] lableIds);

    /**
     *逻辑删除标签
     *
     * @param lableId 标签ID
     * @return 结果
     */
    public int logicDeleteSysFactoryLabelById(Long lableId);

    /**
     * 批量逻辑删除标签
     *
     * @param lableIds 需要删除的数据ID
     * @return 结果
     */
    public int logicDeleteSysFactoryLabelByIds(String[] lableIds);
    List<SysFactoryLabel> selectLabelsByPaperId(String paperid);
}
