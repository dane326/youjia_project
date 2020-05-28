//package com.gsh.web.controller.system;
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
import com.gsh.common.enums.BusinessType;
import com.gsh.system.domain.SysDataTracesDetail;
import com.gsh.system.service.ISysDataTracesDetailService;
import com.gsh.common.core.controller.BaseController;
import com.gsh.common.core.domain.AjaxResult;
import com.gsh.common.utils.poi.ExcelUtil;
import com.gsh.common.core.page.TableDataInfo;

/**
 * 数据追溯日志详情Controller
 * 
 * @author gsh
 * @date 2019-11-20
 */
@Controller
@RequestMapping("/system/detail")
public class SysDataTracesDetailController extends BaseController {
    private String prefix = "system/detail";

    @Autowired
    private ISysDataTracesDetailService sysDataTracesDetailService;

    @RequiresPermissions("system:detail:view")
    @GetMapping()
    public String detail()
    {
        return prefix + "/detail";
    }

    /**
     * 查询数据追溯日志详情列表
     */
    @RequiresPermissions("system:detail:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysDataTracesDetail sysDataTracesDetail){
        startPage();
        List<SysDataTracesDetail> list = sysDataTracesDetailService.selectSysDataTracesDetailList(sysDataTracesDetail);
        return getDataTable(list);
    }

    /**
     * 导出数据追溯日志详情列表
     */
    @RequiresPermissions("system:detail:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysDataTracesDetail sysDataTracesDetail){
        List<SysDataTracesDetail> list = sysDataTracesDetailService.selectSysDataTracesDetailList(sysDataTracesDetail);
        ExcelUtil<SysDataTracesDetail> util = new ExcelUtil<SysDataTracesDetail>(SysDataTracesDetail.class);
        return util.exportExcel(list, "detail");
    }

    /**
     * 新增数据追溯日志详情
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存数据追溯日志详情
     */
    @RequiresPermissions("system:detail:add")
    @Log(title = "数据追溯日志详情", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysDataTracesDetail sysDataTracesDetail){
        sysDataTracesDetail.setDeleted("0");//查询未删除的数据
        return toAjax(sysDataTracesDetailService.insertSysDataTracesDetail(sysDataTracesDetail));
    }

    /**
     * 修改数据追溯日志详情
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap){
        SysDataTracesDetail sysDataTracesDetail = sysDataTracesDetailService.selectSysDataTracesDetailById(id);
        mmap.put("sysDataTracesDetail", sysDataTracesDetail);
        return prefix + "/edit";
    }

    /**
     * 修改保存数据追溯日志详情
     */
    @RequiresPermissions("system:detail:edit")
    @Log(title = "数据追溯日志详情", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysDataTracesDetail sysDataTracesDetail){
        return toAjax(sysDataTracesDetailService.updateSysDataTracesDetail(sysDataTracesDetail));
    }

    /**
     * 删除数据追溯日志详情
     */
    @RequiresPermissions("system:detail:remove")
    @Log(title = "数据追溯日志详情", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysDataTracesDetailService.logicDeleteSysDataTracesDetailByIds(ids));
    }
}
