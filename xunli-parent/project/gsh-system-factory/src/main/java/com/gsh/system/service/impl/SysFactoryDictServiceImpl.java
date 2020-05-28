package com.gsh.system.service.impl;

import java.util.*;

import com.gsh.common.constant.UserConstants;
import com.gsh.common.core.domain.Ztree;
import com.gsh.common.utils.DateUtils;
import com.gsh.common.utils.StringUtils;
import com.gsh.system.domain.SysFactoryInfo;
import com.gsh.system.mapper.SysFactoryDictMapper;
import com.gsh.system.mapper.SysFactoryInfoMapper;
import com.gsh.system.utils.FactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gsh.system.domain.SysFactoryDict;
import com.gsh.system.service.ISysFactoryDictService;
import com.gsh.common.core.text.Convert;

/**
 * 组织字典Service业务层处理
 * 
 * @author gsh
 * @date 2020-04-04
 */
@Service
public class SysFactoryDictServiceImpl implements ISysFactoryDictService
{
    @Autowired
    private SysFactoryDictMapper factoryDictMapper;

    @Autowired
    private SysFactoryInfoMapper factoryInfoMapper;

    /**
     * 查询组织字典
     * 
     * @param dictCode 组织字典ID
     * @return 组织字典
     */
    @Override
    public SysFactoryDict selectSysFactoryDictById(Long dictCode)
    {
        return factoryDictMapper.selectSysFactoryDictById(dictCode);
    }

    /**
     * 查询组织字典列表
     * 
     * @param factoryDict 组织字典
     * @return 组织字典
     */
    @Override
    public List<SysFactoryDict> selectSysFactoryDictList(SysFactoryDict factoryDict)
    {
        return factoryDictMapper.selectSysFactoryDictList(factoryDict);
    }

    /**
     * 新增组织字典
     * 
     * @param factoryDict 组织字典
     * @return 结果
     */
    @Override
    public int insertSysFactoryDict(SysFactoryDict factoryDict)
    {
        if(FactoryUtils.isPlatformFactoryCode()){
            String dictType = factoryDict.getParentType();
            String dictValue = factoryDict.getParentValue();
            int isInsert=0;
            if(StringUtils.isNotEmpty(dictType)){
                isInsert=insertInfoDictHasParent(factoryDict,dictType,dictValue);
            }else{
                isInsert=insertInfoDictMain(factoryDict);
            }
            return isInsert;
        }else{
            SysFactoryDict parentFactoryDict = new SysFactoryDict();
            parentFactoryDict.setChildNodesNum(1L);
            parentFactoryDict.setDictCode(factoryDict.getParentCode());
            factoryDictMapper.updateSysFactoryDict(parentFactoryDict);
            return factoryDictMapper.insertSysFactoryDict(factoryDict);
        }


    }
    /**
     * 新增租户字典存在父类
     */
    public int insertInfoDictHasParent(SysFactoryDict factoryDict,String dictType,String dictValue){
        SysFactoryDict info = new SysFactoryDict();
        info.setDictType(dictType);
        info.setDictValue(dictValue);
        List<SysFactoryDict> dictList= factoryDictMapper.selectSysFactoryDictList(info);
        Map<Long,Long> dictMap = new HashMap<Long,Long>();
        for(SysFactoryDict dict : dictList){
            dictMap.put(dict.getDictCode(),dict.getFactoryCode());
        }
        Set<Long> keys = dictMap.keySet();
        for(Long key :keys){
            factoryDict.setParentCode(key);
            factoryDict.setFactoryCode(dictMap.get(key));
            factoryDict.setCreateTime(DateUtils.getNowDate());
            int val=factoryDictMapper.insertSysFactoryDict(factoryDict);
            //更新父类是否存在子节点字段
            SysFactoryDict parentFactoryDict = new SysFactoryDict();
            parentFactoryDict.setChildNodesNum(1L);
            parentFactoryDict.setDictCode(factoryDict.getParentCode());
            factoryDictMapper.updateSysFactoryDict(parentFactoryDict);
            if(val==0){
                return 0;
            }
        }
        return 1;
    }

    /**
     * 新增租户字典不存在父类
     */
    public int insertInfoDictMain(SysFactoryDict factoryDict){
        List<SysFactoryInfo> infoList = factoryInfoMapper.selectAllSysFactoryInfo();
        for(SysFactoryInfo factoryInfo : infoList){
            factoryDict.setFactoryCode(factoryInfo.getId());
            //更新父类是否存在子节点字段
            SysFactoryDict parentFactoryDict = new SysFactoryDict();
            parentFactoryDict.setChildNodesNum(1L);
            parentFactoryDict.setDictCode(factoryDict.getParentCode());
            factoryDictMapper.updateSysFactoryDict(parentFactoryDict);
            int val=factoryDictMapper.insertSysFactoryDict(factoryDict);
            if(val==0){
                return 0;
            }
        }
        return 1;
    }


    /**
     * 修改组织字典
     * 
     * @param factoryDict 组织字典
     * @return 结果
     */
    @Override
    public int updateSysFactoryDict(SysFactoryDict factoryDict)
    {
        factoryDict.setUpdateTime(DateUtils.getNowDate());
        return factoryDictMapper.updateSysFactoryDict(factoryDict);
    }

    /**
     * 删除组织字典对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysFactoryDictByIds(String ids)
    {
        return factoryDictMapper.deleteSysFactoryDictByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除组织字典信息
     * 
     * @param dictCode 组织字典ID
     * @return 结果
     */
    @Override
    public int deleteSysFactoryDictById(Long dictCode)
    {
        return factoryDictMapper.deleteSysFactoryDictById(dictCode);
    }

    /**
     * 逻辑删除组织字典对象
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int logicDeleteSysFactoryDictByIds(String ids)
    {
        return factoryDictMapper.logicDeleteSysFactoryDictByIds(Convert.toStrArray(ids));
    }

    /**
     * 逻辑删除组织字典信息
     *
     * @param dictCode 组织字典ID
     * @return 结果
     */
    @Override
    public int logicDeleteSysFactoryDictById(Long dictCode,Long parentCode)
    {
        SysFactoryDict parentFactoryDict = new SysFactoryDict();
        parentFactoryDict.setChildNodesNum(-1L);
        parentFactoryDict.setDictCode(parentCode);
        factoryDictMapper.updateSysFactoryDict(parentFactoryDict);
        return factoryDictMapper.logicDeleteSysFactoryDictById(dictCode);
    }

    /**
     * 逻辑删除组织字典信息
     *
     * @param parentCode 组织字典ID
     * @return 结果
     */
    @Override
    public int selectCountSysFactoryDictByParentCode(Long parentCode)
    {
        return factoryDictMapper.selectCountSysFactoryDictByParentCode(parentCode);
    }


    /**
     * 查询组织字典树列表
     * 
     * @return 所有组织字典信息
     */
    @Override
    public List<Ztree> selectSysFactoryDictTree()
    {
        SysFactoryDict info=new SysFactoryDict();
        info.setDeleted("0");
        info.setFactoryCode(FactoryUtils.getFactoryCode());
        info.setChildNodesNum(0L);
        List<SysFactoryDict> factoryDictList = factoryDictMapper.selectSysFactoryDictList(info);
        List<Ztree> ztrees = new ArrayList<Ztree>();
        for (SysFactoryDict factoryDict : factoryDictList)
        {
            Ztree ztree = new Ztree();
            ztree.setId(factoryDict.getDictCode());
            ztree.setpId(factoryDict.getParentCode());
            ztree.setKey(factoryDict.getDictType()+"_"+factoryDict.getDictValue());
            ztree.setValue(factoryDict.getDictLabel());
            ztree.setName(factoryDict.getDictLabel()+"&nbsp;&nbsp;&nbsp;"+factoryDict.getDictType()+"_"+factoryDict.getDictValue());
            ztree.setTitle(factoryDict.getDictLabel());
            ztrees.add(ztree);
        }
        return ztrees;
    }

    /**
     * 限制一级组织字典唯一
     *
     * @param factoryDict 字典类型
     * @return 结果
     */
    @Override
    public String checkFactoryDictUnique(SysFactoryDict factoryDict)
    {
        Long dictCode = StringUtils.isNull(factoryDict.getDictCode()) ? -1L : factoryDict.getDictCode();
        SysFactoryDict info = factoryDictMapper.checkFactoryDictUnique(factoryDict.getDictType());
        if (StringUtils.isNotNull(info) && info.getDictCode().longValue() != dictCode.longValue())
        {
            return UserConstants.DICT_TYPE_NOT_UNIQUE;
        }
        return UserConstants.DICT_TYPE_UNIQUE;
    }

    /**
     * 根据条件分页查询字典数据
     *
     * @param factoryDict 字典数据信息
     * @return 字典数据集合信息
     */
    @Override
    public List<SysFactoryDict> selectFactoryDictDataList(SysFactoryDict factoryDict) {
        return factoryDictMapper.selectFactoryDictDataList(factoryDict);
    }
}
