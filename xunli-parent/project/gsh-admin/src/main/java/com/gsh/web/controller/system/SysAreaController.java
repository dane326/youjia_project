package com.gsh.web.controller.system;

import com.gsh.common.annotation.Log;
import com.gsh.common.core.controller.BaseController;
import com.gsh.common.core.domain.AjaxResult;
import com.gsh.common.core.domain.Ztree;
import com.gsh.common.enums.BusinessType;
import com.gsh.common.utils.StringUtils;
import com.gsh.common.utils.poi.ExcelUtil;
import com.gsh.framework.util.ShiroUtils;
import com.gsh.system.domain.SysArea;
import com.gsh.system.service.ISysAreaService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 地域Controller
 * 
 * @author gsh-xrl
 * @date 2019-11-12
 */
@Controller
@RequestMapping("/system/area")
public class SysAreaController extends BaseController
{
    private String prefix = "system/area";

    @Autowired
    private ISysAreaService sysAreaService;

    @RequiresPermissions("system:area:view")
    @GetMapping()
    public String area()
    {
        return prefix + "/area";
    }

    /**
     * 查询地域树列表
     */
    @RequiresPermissions("system:area:list")
    @PostMapping("/list")
    @ResponseBody
    public List<SysArea> list(SysArea sysArea)
    {
        List<SysArea> list = sysAreaService.selectSysAreaList(sysArea);
        return list;
    }

    /**
     * 导出地域列表
     */
    @RequiresPermissions("system:area:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysArea sysArea)
    {
        List<SysArea> list = sysAreaService.selectSysAreaList(sysArea);
        ExcelUtil<SysArea> util = new ExcelUtil<SysArea>(SysArea.class);
        return util.exportExcel(list, "area");
    }

    /**
     * 新增地域
     */
    @GetMapping(value = { "/add/{areaId}", "/add/" })
    public String add(@PathVariable(value = "areaId", required = false) Long areaId, ModelMap mmap)
    {
        if (StringUtils.isNotNull(areaId))
        {
            mmap.put("sysArea", sysAreaService.selectSysAreaById(areaId));
        }
        return prefix + "/add";
    }

    /**
     * 新增保存地域
     */
    @RequiresPermissions("system:area:add")
    @Log(title = "地域", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysArea sysArea)
    {
        if ("1".equals(this.sysAreaService.checkAreaNameUnique(sysArea))) {
            return this.error("新增地域'" + sysArea.getAreaName() + "'失败，地域名称已存在");
        } else {
            sysArea.setCreateBy(ShiroUtils.getLoginName());
            return this.toAjax(this.sysAreaService.insertSysArea(sysArea));
        }
    }

    /**
     * 修改地域
     */
    @GetMapping("/edit/{areaId}")
    public String edit(@PathVariable("areaId") Long areaId, ModelMap mmap)
    {
        SysArea sysArea = sysAreaService.selectSysAreaById(areaId);
        mmap.put("sysArea", sysArea);
        return prefix + "/edit";
    }


    /**
     * 修改保存地域
     */
    @RequiresPermissions("system:area:edit")
    @Log(title = "地域", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysArea sysArea)
    {
        return toAjax(sysAreaService.updateSysArea(sysArea));
    }

    /**
     * 删除
     */
    @RequiresPermissions("system:area:remove")
    @Log(title = "地域", businessType = BusinessType.DELETE)
    @GetMapping("/remove/{areaId}")
    @ResponseBody
    public AjaxResult remove(@PathVariable("areaId") Long areaId)
    {
        return toAjax(sysAreaService.deleteSysAreaById(areaId));
    }

    /**
     * 选择地域树
     */
    @GetMapping(value = { "/selectAreaTree/{areaId}", "/selectAreaTree/" })
    public String selectAreaTree(@PathVariable(value = "areaId", required = false) Long areaId, ModelMap mmap)
    {
        if (StringUtils.isNotNull(areaId))
        {
            mmap.put("sysArea", sysAreaService.selectSysAreaById(areaId));
        }
        return prefix + "/tree";
    }

    /**
     * 加载地域树列表
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData()
    {
        List<Ztree> ztrees = sysAreaService.selectSysAreaTree();
        return ztrees;
    }
}
