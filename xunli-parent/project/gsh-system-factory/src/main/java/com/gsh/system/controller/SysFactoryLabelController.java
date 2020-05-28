//package com.gsh.aiexam.web.controller.aiexam;
package com.gsh.system.controller;
import java.util.List;

import com.gsh.system.domain.SysFactoryLabel;
import com.gsh.system.service.ISysFactoryLabelService;

import com.gsh.system.utils.FactoryUtils;
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
import com.gsh.common.enums.BusinessType;
import com.gsh.common.core.controller.BaseController;
import com.gsh.common.core.domain.AjaxResult;
import com.gsh.common.utils.poi.ExcelUtil;
import com.gsh.common.core.page.TableDataInfo;

/**
 * 标签Controller
 * 
 * @author gsh
 * @date 2019-10-17
 */
@Controller
@RequestMapping("/system/factory/label")
public class SysFactoryLabelController extends BaseController {
    private String prefix = "system/factory/label";

    @Autowired
    private ISysFactoryLabelService sysFactoryLabelService;

    @RequiresPermissions("system:factory:label:view")
    @GetMapping()
    public String label()
    {
        return prefix + "/label";
    }

    /**
     * 查询标签列表
     */
    @RequiresPermissions("system:factory:label:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysFactoryLabel sysFactoryLabel){
        startPage();
        Long deptId= FactoryUtils.getSysUser().getDept().getDeptId();
        sysFactoryLabel.setCreateByDeptid(deptId);//默认查询该部门的数据.
        sysFactoryLabel.setDeleted("0");//查询未删除的数据
        List<SysFactoryLabel> list = sysFactoryLabelService.selectSysFactoryLabelList(sysFactoryLabel);
        return getDataTable(list);
    }

    /**
     * 导出标签列表
     */
    @RequiresPermissions("system:factory:label:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysFactoryLabel sysFactoryLabel){
        Long deptId= FactoryUtils.getSysUser().getDept().getDeptId();
        sysFactoryLabel.setCreateByDeptid(deptId);//默认查询该部门的数据.
        sysFactoryLabel.setDeleted("0");//查询未删除的数据
        List<SysFactoryLabel> list = sysFactoryLabelService.selectSysFactoryLabelList(sysFactoryLabel);
        ExcelUtil<SysFactoryLabel> util = new ExcelUtil<SysFactoryLabel>(SysFactoryLabel.class);
        return util.exportExcel(list, "label");
    }

    /**
     * 新增标签
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存标签
     */
    @RequiresPermissions("system:factory:label:add")
    @Log(title = "标签", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysFactoryLabel sysFactoryLabel){
        sysFactoryLabel.setDeleted("0");
        sysFactoryLabel.setFactoryCode(FactoryUtils.getFactoryCode());
        sysFactoryLabel.setCreateByDeptid(FactoryUtils.getSysUser().getDeptId());
        return toAjax(sysFactoryLabelService.insertSysFactoryLabel(sysFactoryLabel));
    }

    /**
     * 修改标签
     */
    @GetMapping("/edit/{lableId}")
    public String edit(@PathVariable("lableId") Long lableId, ModelMap mmap){
        SysFactoryLabel sysFactoryLabel = sysFactoryLabelService.selectSysFactoryLabelById(lableId);
        mmap.put("sysFactoryLabel", sysFactoryLabel);
        return prefix + "/edit";
    }

    /**
     * 修改保存标签
     */
    @RequiresPermissions("system:factory:label:edit")
    @Log(title = "标签", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysFactoryLabel sysFactoryLabel){
        return toAjax(sysFactoryLabelService.updateSysFactoryLabel(sysFactoryLabel));
    }

    /**
     * 删除标签
     */
    @RequiresPermissions("system:factory:label:remove")
    @Log(title = "标签", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysFactoryLabelService.logicDeleteSysFactoryLabelByIds(ids));
    }
}
