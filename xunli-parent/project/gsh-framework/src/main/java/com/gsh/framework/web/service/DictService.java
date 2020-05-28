package com.gsh.framework.web.service;

import java.util.List;

import com.gsh.system.service.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gsh.system.domain.SysDictData;
import com.gsh.system.service.ISysDictDataService;

/**
 *  html调用 thymeleaf 实现字典读取
 * 
 * @author gsh
 */
@Service("dict")
public class DictService
{
    @Autowired
    private ISysDictDataService dictDataService;
    @Autowired
    private ISysDictTypeService dictTypeService;

    /**
     * 根据字典类型查询字典数据信息
     * 
     * @param dictType 字典类型
     * @return 参数键值
     */
    public List<SysDictData> getType(String dictType)
    {
        return dictTypeService.selectDictDataByType(dictType);
    }

    /**
     * 根据字典类型和字典键值查询字典数据信息
     * 
     * @param dictType 字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    public String getLabel(String dictType, String dictValue)
    {
        return dictDataService.selectDictLabel(dictType, dictValue);
    }

    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictCode 字典键值
     * @return 字典标签
     */
    public String getLabel(Long dictCode)
    {
        SysDictData sysDictData = dictDataService.selectDictDataById(dictCode);
        if(sysDictData != null){
            return sysDictData.getDictLabel();
        }
        return "";
    }
}
