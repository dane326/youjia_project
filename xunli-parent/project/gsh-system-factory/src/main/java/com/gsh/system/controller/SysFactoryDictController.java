package com.gsh.system.controller;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gsh.common.annotation.Log;
import com.gsh.common.core.controller.BaseController;
import com.gsh.common.core.domain.AjaxResult;
import com.gsh.common.core.domain.Ztree;
import com.gsh.common.core.page.TableDataInfo;
import com.gsh.common.enums.BusinessType;
import com.gsh.common.utils.StringUtils;
import com.gsh.common.utils.poi.ExcelUtil;
import com.gsh.system.domain.SysFactoryDict;
import com.gsh.system.service.ISysFactoryDictService;
import com.gsh.system.utils.FactoryUtils;

/**
 * 组织字典Controller
 *
 * @author gsh
 * @date 2020-04-04
 */
@Controller
@RequestMapping("/system/factory/dict")
public class SysFactoryDictController extends BaseController {
    private String prefix = "system/factory/dict";

    @Autowired
    private ISysFactoryDictService factoryDictService;

    @RequiresPermissions("system:factory:dict:view")
    @GetMapping()
    public String factoryDict(ModelMap mmap)
    {
        mmap.put("factoryCode", FactoryUtils.getFactoryCode());
        if(FactoryUtils.getFactoryTreeMode()){
            return prefix + "/dicttree";
        }
        return prefix + "/dict";
    }

    /**
     * 查询组织字典树列表
     */
    @RequiresPermissions("system:factory:dict:list")
    @PostMapping("/list")
    @ResponseBody
    public List<SysFactoryDict> list(SysFactoryDict factoryDict) {
        factoryDict.setDeleted("0");
        if(null==factoryDict.getFactoryCode()){
            factoryDict.setFactoryCode(FactoryUtils.getFactoryCode());
        }
        List<SysFactoryDict> list = factoryDictService.selectSysFactoryDictList(factoryDict);
        return list;
    }
    
    /**
     * 导出组织字典列表
     */
    @RequiresPermissions("system:factory:dict:export")
    @Log(title = "组织字典", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysFactoryDict factoryDict){
        //Long deptId=ShiroUtils.getSysUser().getDept().getDeptId();
        //factoryDict.setCreateByDeptid(deptId);//默认查询该部门的数据.
        factoryDict.setDeleted("0");//查询未删除的数据
        List<SysFactoryDict> list = factoryDictService.selectSysFactoryDictList(factoryDict);
        ExcelUtil<SysFactoryDict> util = new ExcelUtil<SysFactoryDict>(SysFactoryDict.class);
        return util.exportExcel(list, "SysFactoryDict");
    }



    /**
     * 新增字典类型
     */
    @GetMapping("/add/{parentCode}")
    public String add(@PathVariable("parentCode") Long parentCode, ModelMap mmap)
    {
        SysFactoryDict factoryDict=null;
        if(0 == parentCode){
            factoryDict =new SysFactoryDict();
            factoryDict.setDictCode(0L);
            factoryDict.setDictLabel("主字典");
        }else{
            factoryDict = factoryDictService.selectSysFactoryDictById(parentCode);
            factoryDict.setParentType(factoryDict.getDictType());
            factoryDict.setParentValue(factoryDict.getDictValue());
            factoryDict.setDictType(factoryDict.getDictType()+"_"+factoryDict.getDictValue());
        }
        mmap.put("factoryDict", factoryDict);
        return prefix + "/add";
    }

    /**
     * 新增部门
     */
    @GetMapping("/{factoryCode}/add/{parentCode}")
    public String addByFactoryCode(@PathVariable("factoryCode") Long factoryCode, @PathVariable("parentCode") Long parentCode, ModelMap mmap)
    {
        SysFactoryDict factoryDict=null;
        if(0 == parentCode){
            factoryDict =new SysFactoryDict();
            factoryDict.setDictCode(0L);
            factoryDict.setDictLabel("主字典");
        }else{
            factoryDict = factoryDictService.selectSysFactoryDictById(parentCode);
            factoryDict.setParentType(factoryDict.getDictType());
            factoryDict.setParentValue(factoryDict.getDictValue());
            factoryDict.setDictType(factoryDict.getDictType()+"_"+factoryDict.getDictValue());
        }
        mmap.put("factoryDict", factoryDict);
        mmap.put("factoryCode", factoryCode);
        return prefix + "/add";
    }
    
    /**
     * 新增保存组织字典
     */
    @RequiresPermissions("system:factory:dict:add")
    @Log(title = "组织字典", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysFactoryDict factoryDict){
        factoryDict.setDeleted("0");//查询未删除的数据
        return toAjax(factoryDictService.insertSysFactoryDict(factoryDict));
    }


    /**
     * 修改组织字典
     */
    @GetMapping("/edit/{dictCode}")
    public String edit(@PathVariable("dictCode") Long dictCode, ModelMap mmap){
        SysFactoryDict factoryDict = factoryDictService.selectSysFactoryDictById(dictCode);
        if(factoryDict.getParentCode() > 0){
            SysFactoryDict parentDictData = factoryDictService.selectSysFactoryDictById(factoryDict.getParentCode());
            if(parentDictData!=null){
                factoryDict.setDictType(parentDictData.getDictType() + "_" + parentDictData.getDictValue());
            }
        }
        mmap.put("factoryDict", factoryDict);
        return prefix + "/edit";
    }

    /**
     * 修改保存组织字典
     */
    @RequiresPermissions("system:factory:dict:edit")
    @Log(title = "组织字典", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysFactoryDict factoryDict){
        return toAjax(factoryDictService.updateSysFactoryDict(factoryDict));
    }

    /**
     * 删除
     */
    @RequiresPermissions("system:factory:dict:remove")
    @Log(title = "组织字典", businessType = BusinessType.DELETE)
    @GetMapping("/remove/{dictCode}/{parentCode}")
    @ResponseBody
    public AjaxResult remove(@PathVariable("dictCode") Long dictCode,@PathVariable("parentCode") Long parentCode){
        if (factoryDictService.selectCountSysFactoryDictByParentCode(dictCode) > 0)
        {
            return AjaxResult.warn("存在下级字典,不允许删除");
        }else{
            return toAjax(factoryDictService.logicDeleteSysFactoryDictById(dictCode,parentCode));
        }

    }
        
    /**
     * 选择组织字典树
     */
   @GetMapping(value = { "/selectFactoryDictTree/{dictCode}", "/selectFactoryDictTree/" })
    public String selectFactoryDictTree(@PathVariable(value = "dictCode", required = false) Long dictCode, ModelMap mmap){
        if (StringUtils.isNotNull(dictCode)){
            mmap.put("factoryDict", factoryDictService.selectSysFactoryDictById(dictCode));
        }
        return prefix + "/tree";
    }

    /**
     * 加载组织字典树列表
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData(){
        List<Ztree> ztrees = factoryDictService.selectSysFactoryDictTree();
        return ztrees;
    }

    @GetMapping("/code/{parentCode}")
    @ResponseBody
    public TableDataInfo codeData(@PathVariable("parentCode") Long parentCode)
    {
        SysFactoryDict dictData = new SysFactoryDict();
        dictData.setParentCode(parentCode);
        dictData.setDeleted("0");
        dictData.setParentCode(FactoryUtils.getFactoryCode());
        List<SysFactoryDict> list = factoryDictService.selectSysFactoryDictList(dictData);
        return getDataTable(list);
    }

    @GetMapping("/type/{dictType}")
    @ResponseBody
    public TableDataInfo typeData(@PathVariable("dictType") String dictType)
    {
        SysFactoryDict dictData = new SysFactoryDict();
        dictData.setDictType(dictType);
        dictData.setDeleted("0");
        dictData.setFactoryCode(FactoryUtils.getFactoryCode());
        List<SysFactoryDict> list = factoryDictService.selectSysFactoryDictList(dictData);
        return getDataTable(list);
    }

    /**
     * 校验字典类型
     */
    @PostMapping("/checkFactoryDictUnique")
    @ResponseBody
    public String checkFactoryDictUnique(SysFactoryDict factoryDict)
    {
        return factoryDictService.checkFactoryDictUnique(factoryDict);
    }
}
