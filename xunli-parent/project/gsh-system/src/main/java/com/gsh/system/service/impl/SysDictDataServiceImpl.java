package com.gsh.system.service.impl;

import com.gsh.common.constant.UserConstants;
import com.gsh.common.core.domain.Ztree;
import com.gsh.common.core.text.Convert;
import com.gsh.system.domain.SysDictData;
import com.gsh.system.mapper.SysDictDataMapper;
import com.gsh.system.service.ISysDictDataService;
import com.gsh.system.utils.DictUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 字典 业务层处理
 * 
 * @author gsh
 */
@Service
public class SysDictDataServiceImpl implements ISysDictDataService
{
    @Autowired
    private SysDictDataMapper dictDataMapper;

    /**
     * 根据条件分页查询字典数据
     * 
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    @Override
    public List<SysDictData> selectDictDataList(SysDictData dictData)
    {
        return dictDataMapper.selectDictDataList(dictData);
    }

    /**
     * 根据字典类型查询字典数据
     *
     * @param parentCode 字典类型
     * @return 字典数据集合信息
     */
    @Override
    @Cacheable(value="sys_dict_cache", key="#parentCode", unless="#result == null")
    public List<SysDictData> selectDictDataByParentCode(Long parentCode)
    {
        return dictDataMapper.selectDictDataByParentCode(parentCode);
    }

    /**
     * 根据字典类型和字典键值查询字典数据信息
     * 
     * @param dictType 字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    @Override
    public String selectDictLabel(String dictType, String dictValue)
    {
        return dictDataMapper.selectDictLabel(dictType, dictValue);
    }

    /**
     * 根据字典数据ID查询信息
     * 
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    @Override
    public SysDictData selectDictDataById(Long dictCode)
    {
        return dictDataMapper.selectDictDataById(dictCode);
    }

    /**
     * 批量删除字典数据
     *
     * @param ids 需要删除的数据
     * @param parentCodes 需要跟新的父类数据
     * @return 结果
     */
    @Override
    public int deleteDictDataByIds(String ids,String parentCodes)
    {
        SysDictData dictData = new SysDictData();
        dictData.setChildNodesNum(-1L);
        String[] parentCodesData = Convert.toStrArray(parentCodes);
        for(String parentCode: parentCodesData){
            dictData.setDictCode(Long.parseLong(parentCode));
            dictDataMapper.updateDictData(dictData);
        }
        int row = dictDataMapper.deleteDictDataByIds(Convert.toStrArray(ids));
        if (row > 0)
        {
            DictUtils.clearDictCache();
        }
        return row;
    }

    /**
     * 新增保存字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */
    @Override
    public int insertDictData(SysDictData dictData)
    {
        //更新父节点字类个数
        SysDictData parentDictData = new SysDictData();
        parentDictData.setChildNodesNum(1L);
        parentDictData.setDictCode(dictData.getParentCode());
        dictDataMapper.updateDictData(parentDictData);
        int row = dictDataMapper.insertDictData(dictData);
        if (row > 0)
        {
            DictUtils.clearDictCache();
        }
        return row;
    }

    /**
     * 修改保存字典数据信息
     *
     * @param dictData 字典数据信息
     * @return 结果
     */
    @Override
    public int updateDictData(SysDictData dictData)
    {
        int row = dictDataMapper.updateDictData(dictData);
        if (row > 0)
        {
            DictUtils.clearDictCache();
        }
        return row;
    }

    /**
     * 查询字典类型树
     *
     * @param dictData 字典类型
     * @return 所有字典类型
     */
    @Override
    public List<Ztree> selectDictDataTree(SysDictData dictData)
    {
        dictData.setChildNodesNum(0L);
        List<Ztree> ztrees = new ArrayList<Ztree>();
        List<SysDictData> dictDataList = dictDataMapper.selectDictDataList(dictData);
        for (SysDictData dict : dictDataList)
        {
            if (UserConstants.DICT_NORMAL.equals(dict.getStatus()))
            {
                Ztree ztree = new Ztree();
                ztree.setId(dict.getDictCode());
                ztree.setpId(dict.getParentCode());
                ztree.setKey(dict.getDictType()+"_"+dict.getDictValue());
                ztree.setValue(dict.getDictLabel());
                ztree.setName(dict.getDictLabel()+"&nbsp;&nbsp;&nbsp;"+dict.getDictType()+"_"+dict.getDictValue());
                ztree.setTitle(dict.getDictLabel());
                ztrees.add(ztree);
            }
        }
        return ztrees;
    }
}
