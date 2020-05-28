package com.gsh.web.controller.system;

import com.gsh.common.annotation.Log;
import com.gsh.common.core.controller.BaseController;
import com.gsh.common.core.domain.AjaxResult;
import com.gsh.common.core.domain.Ztree;
import com.gsh.common.core.page.TableDataInfo;
import com.gsh.common.enums.BusinessType;
import com.gsh.common.utils.poi.ExcelUtil;
import com.gsh.system.domain.SysDictData;
import com.gsh.system.service.ISysDictDataService;
import com.gsh.system.service.ISysDictTypeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据字典信息
 * 
 * @author gsh
 */
@Controller
@RequestMapping("/system/dict/data")
public class SysDictDataController extends BaseController
{
    private String prefix = "system/dict/data";

    @Autowired
    private ISysDictTypeService dictTypeService;

    @Autowired
    private ISysDictDataService dictDataService;

    @RequiresPermissions("system:dict:view")
    @GetMapping()
    public String dictData()
    {
        return prefix + "/data";
    }

    @PostMapping("/list")
    @RequiresPermissions("system:dict:list")
    @ResponseBody
    public Object list(SysDictData dictData)
    {
        //startPage();
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        //return getDataTable(list);
        return list;
    }

    @GetMapping("/get/{dictType}")
    @ResponseBody
    public TableDataInfo data(@PathVariable("dictType") String dictType)
    {
        SysDictData dictData = new SysDictData();
        dictData.setDictType(dictType);
        dictData.setDeleted("0");
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        return getDataTable(list);
    }

    @GetMapping("/code/{parentCode}")
    @ResponseBody
    public TableDataInfo codeData(@PathVariable("parentCode") Long parentCode)
    {
        SysDictData dictData = new SysDictData();
        dictData.setParentCode(parentCode);
        dictData.setDeleted("0");
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        return getDataTable(list);
    }

    @GetMapping("/type/{dictType}")
    @ResponseBody
    public TableDataInfo typeData(@PathVariable("dictType") String dictType)
    {
        SysDictData dictData = new SysDictData();
        dictData.setDictType(dictType);
        dictData.setDeleted("0");
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        return getDataTable(list);
    }

    @Log(title = "字典数据", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:dict:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysDictData dictData)
    {
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        ExcelUtil<SysDictData> util = new ExcelUtil<SysDictData>(SysDictData.class);
        return util.exportExcel(list, "字典数据");
    }

    /**
     * 新增字典类型
     */
    @GetMapping("/add/{dictType}/{parentCode}")
    public String add(@PathVariable("dictType") String dictType, @PathVariable("parentCode") Long parentCode, ModelMap mmap)
    {
        if(0 == parentCode){
            mmap.put("dictType", dictType);
            mmap.put("dictTypeId", dictTypeService.selectDictTypeByType(dictType).getDictId());
            mmap.put("parentCode", mmap.get("dictTypeId"));
        }else{
            SysDictData parentDictData = dictDataService.selectDictDataById(parentCode);
            mmap.put("dictType", parentDictData.getDictType() + "_" + parentDictData.getDictValue());
            mmap.put("dictTypeId", parentDictData.getDictTypeId());
            mmap.put("parentCode", parentCode);
        }
        return prefix + "/add";
    }

    /**
     * 新增保存字典类型
     */
    @Log(title = "字典数据", businessType = BusinessType.INSERT)
    @RequiresPermissions("system:dict:add")
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated SysDictData dict)
    {
        return toAjax(dictDataService.insertDictData(dict));
    }

    /**
     * 修改字典类型
     */
    @GetMapping("/edit/{dictCode}")
    public String edit(@PathVariable("dictCode") Long dictCode, ModelMap mmap)
    {
        SysDictData dict = dictDataService.selectDictDataById(dictCode);
        if(dict.getParentCode() > 0){
            SysDictData parentDictData = dictDataService.selectDictDataById(dict.getParentCode());
            if(parentDictData!=null){
                dict.setDictType(parentDictData.getDictType() + "_" + parentDictData.getDictValue());
            }
        }
        mmap.put("dict", dict);
        return prefix + "/edit";
    }

    /**
     * 修改保存字典类型
     */
    @Log(title = "字典数据", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:dict:edit")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated SysDictData dict)
    {
        return toAjax(dictDataService.updateDictData(dict));
    }

    @Log(title = "字典数据", businessType = BusinessType.DELETE)
    @RequiresPermissions("system:dict:remove")
    @GetMapping("/remove/{ids}/{parentCodes}")
    @ResponseBody
    public AjaxResult remove(@PathVariable String ids,@PathVariable String parentCodes)
    {
        return toAjax(dictDataService.deleteDictDataByIds(ids,parentCodes));
    }


    /**
     * 加载字典列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData()
    {
        List<Ztree> ztrees = dictDataService.selectDictDataTree(new SysDictData());
        return ztrees;
    }
}
