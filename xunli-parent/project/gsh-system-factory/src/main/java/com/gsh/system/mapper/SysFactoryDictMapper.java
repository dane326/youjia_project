package com.gsh.system.mapper;

import java.util.List;

import com.gsh.system.domain.SysFactoryDict;

/**
 * 组织字典Mapper接口
 * 
 * @author gsh
 * @date 2020-04-04
 */
public interface SysFactoryDictMapper
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
     * 删除组织字典
     *
     * @param dictCode 组织字典ID
     * @return 结果
     */
    public int deleteSysFactoryDictById(Long dictCode);

    /**
     * 批量删除组织字典
     *
     * @param dictCodes 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysFactoryDictByIds(String[] dictCodes);

    /**
     *逻辑删除组织字典
     *
     * @param dictCode 组织字典ID
     * @return 结果
     */
    public int logicDeleteSysFactoryDictById(Long dictCode);

    /**
     * 批量逻辑删除组织字典
     *
     * @param dictCodes 需要删除的数据ID
     * @return 结果
     */
    public int logicDeleteSysFactoryDictByIds(String[] dictCodes);

    /**
     * 判断是否存在下级字典值
     *
     * @param parentCode 组织字典ID
     * @return 结果
     */
    public int selectCountSysFactoryDictByParentCode(Long parentCode);


    /**
     * 校验字典类型称是否唯一
     *
     * @param dictType 字典类型
     * @return 结果
     */
    public SysFactoryDict checkFactoryDictUnique(String dictType);

    /**
     * 根据条件分页查询字典数据
     *
     * @param factoryDict 字典数据信息
     * @return 字典数据集合信息
     */
    List<SysFactoryDict> selectFactoryDictDataList(SysFactoryDict factoryDict);
}
