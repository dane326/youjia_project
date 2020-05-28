package com.gsh.system.service;

import java.util.List;

import com.gsh.common.core.domain.Ztree;
import com.gsh.system.domain.SysFactoryDict;

/**
 * 组织字典Service接口
 * 
 * @author gsh
 * @date 2020-04-04
 */
public interface ISysFactoryDictService
{
    /**
     * 查询组织字典
     * 
     * @param dictCode 组织字典ID
     * @return 组织字典
     */
    public SysFactoryDict selectSysFactoryDictById(Long dictCode);

    /**
     * 查询组织字典列表
     * 
     * @param factoryDict 组织字典
     * @return 组织字典集合
     */
    public List<SysFactoryDict> selectSysFactoryDictList(SysFactoryDict factoryDict);

    /**
     * 新增组织字典
     * 
     * @param factoryDict 组织字典
     * @return 结果
     */
    public int insertSysFactoryDict(SysFactoryDict factoryDict);

    /**
     * 修改组织字典
     * 
     * @param factoryDict 组织字典
     * @return 结果
     */
    public int updateSysFactoryDict(SysFactoryDict factoryDict);

    /**
     * 批量删除组织字典
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysFactoryDictByIds(String ids);

    /**
     * 删除组织字典信息
     * 
     * @param dictCode 组织字典ID
     * @return 结果
     */
    public int deleteSysFactoryDictById(Long dictCode);
    /**
     * 批量逻辑删除组织字典
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int logicDeleteSysFactoryDictByIds(String ids);

    /**
     * 逻辑删除组织字典信息
     *
     * @param dictCode 组织字典ID
     * @return 结果
     */
    public int logicDeleteSysFactoryDictById(Long dictCode, Long patenctCode);

    /**
     * 判断是否存在下级字典值
     *
     * @param parentCode 组织字典ID
     * @return 结果
     */
    public int selectCountSysFactoryDictByParentCode(Long parentCode);


    /**
     * 查询组织字典树列表
     *
     * @return 所有组织字典信息
     */
    public List<Ztree> selectSysFactoryDictTree();


    /**
     * 校验字典类型称是否唯一
     *
     * @param factoryDict 字典类型
     * @return 结果
     */
    public String checkFactoryDictUnique(SysFactoryDict factoryDict);

    /**
     * 根据条件分页查询字典数据
     *
     * @param factoryDict 字典数据信息
     * @return 字典数据集合信息
     */
    public List<SysFactoryDict> selectFactoryDictDataList(SysFactoryDict factoryDict);
}
