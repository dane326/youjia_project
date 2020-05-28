package com.gsh.system.service;

import java.util.List;

import com.gsh.common.core.domain.Ztree;
import com.gsh.system.domain.SysDictData;

/**
 * 字典 业务层
 * 
 * @author gsh
 */
public interface ISysDictDataService
{
    /**
     * 根据条件分页查询字典数据
     * 
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    public List<SysDictData> selectDictDataList(SysDictData dictData);

    /**
     * 根据字典类型查询字典数据
     *
     * @param parentCode 字典类型
     * @return 字典数据集合信息
     */
    public List<SysDictData> selectDictDataByParentCode(Long parentCode);

    /**
     * 根据字典类型和字典键值查询字典数据信息
     * 
     * @param dictType 字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    public String selectDictLabel(String dictType, String dictValue);

    /**
     * 根据字典数据ID查询信息
     *
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    public SysDictData selectDictDataById(Long dictCode);

    /**
     * 批量删除字典数据
     * 
     * @param ids 需要删除的数据
     * @param parentCodes 需要跟新的父类数据
     * @return 结果
     */
    public int deleteDictDataByIds(String ids,String parentCodes);

    /**
     * 新增保存字典数据信息
     * 
     * @param dictData 字典数据信息
     * @return 结果
     */
    public int insertDictData(SysDictData dictData);

    /**
     * 修改保存字典数据信息
     * 
     * @param dictData 字典数据信息
     * @return 结果
     */
    public int updateDictData(SysDictData dictData);

    /**
     * 查询字典类型树
     *
     * @param dictData 字典类型
     * @return 所有字典类型
     */
    public List<Ztree> selectDictDataTree(SysDictData dictData);
}
