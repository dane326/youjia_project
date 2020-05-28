//package com.gsh.web.controller.system;
package com.gsh.system.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gsh.common.utils.DateUtils;
import com.gsh.system.domain.SysDataTracesDetail;
import com.gsh.system.service.ISysDataTracesDetailService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.gsh.common.annotation.Log;
import com.gsh.common.enums.BusinessType;
import com.gsh.system.domain.SysDataTracesLog;
import com.gsh.system.service.ISysDataTracesLogService;
import com.gsh.common.core.controller.BaseController;
import com.gsh.common.core.domain.AjaxResult;
import com.gsh.common.utils.poi.ExcelUtil;
import com.gsh.common.core.page.TableDataInfo;

/**
 * 数据追溯日志Controller
 * 
 * @author gsh
 * @date 2019-11-20
 */
@Controller
@RequestMapping("/system/datatraceslog")
public class SysDataTracesLogController extends BaseController {
    private String prefix = "system/datatraceslog";

    @Autowired
    private ISysDataTracesLogService sysDataTracesLogService;

    @Autowired
    private ISysDataTracesDetailService sysDataTracesDetailService;

    @RequiresPermissions("system:datatraceslog:view")
    @GetMapping()
    public String log(ModelMap mmap) {
        Map<String, Object> paramap = new HashMap<>();
        paramap.put("opObjectId", "");
        paramap.put("opObjectName", "");
        paramap.put("beginCreateTime", DateUtils.dateTime(DateUtils.addDays(DateUtils.getNowDate(),0-30)));
        paramap.put("endCreateTime", DateUtils.getDate());
        mmap.put("paramap", paramap);
        return prefix + "/log";
    }

    @RequiresPermissions("system:datatraceslog:view")
    @GetMapping("/list/{opObjectName}/{opObjectId}/{opTimeRange}")
    public String list(@PathVariable("opObjectName")String opObjectName, @PathVariable("opObjectId")Long opObjectId, @PathVariable("opTimeRange")Integer opTimeRange, ModelMap mmap) {
        Map<String, Object> paramap = new HashMap<>();
        paramap.put("opObjectId", opObjectId);
        paramap.put("opObjectName", opObjectName);
        paramap.put("beginCreateTime", DateUtils.dateTime(DateUtils.addDays(DateUtils.getNowDate(),0-opTimeRange)));
        paramap.put("endCreateTime", DateUtils.getDate());
        mmap.put("paramap", paramap);
        return prefix + "/log";
    }

    /**
     * 查询数据追溯日志列表
     */
    @RequiresPermissions("system:datatraceslog:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysDataTracesLog sysDataTracesLog){
        startPage();
        sysDataTracesLog.setDeleted("0");//查询未删除的数据
        List<SysDataTracesLog> list = sysDataTracesLogService.selectSysDataTracesLogList(sysDataTracesLog);
        return getDataTable(list);
    }

    /**
     * 导出数据追溯日志列表
     */
    @RequiresPermissions("system:datatraceslog:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysDataTracesLog sysDataTracesLog){
        sysDataTracesLog.setDeleted("0");//查询未删除的数据
        List<SysDataTracesLog> list = sysDataTracesLogService.selectSysDataTracesLogList(sysDataTracesLog);
        ExcelUtil<SysDataTracesLog> util = new ExcelUtil<SysDataTracesLog>(SysDataTracesLog.class);
        return util.exportExcel(list, "log");
    }

    /**
     * 新增数据追溯日志
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存数据追溯日志
     */
    @RequiresPermissions("system:datatraceslog:add")
    @Log(title = "数据追溯日志", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysDataTracesLog sysDataTracesLog){
        sysDataTracesLog.setDeleted("0");//查询未删除的数据
        return toAjax(sysDataTracesLogService.insertSysDataTracesLog(sysDataTracesLog));
    }

    /**
     * 修改数据追溯日志
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap){
        SysDataTracesLog sysDataTracesLog = sysDataTracesLogService.selectSysDataTracesLogById(id);
        mmap.put("sysDataTracesLog", sysDataTracesLog);
         return prefix + "/edit";
    }

    /**
     * 修改数据追溯日志
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, ModelMap mmap){
        SysDataTracesLog sysDataTracesLog = sysDataTracesLogService.selectSysDataTracesLogById(id);
        mmap.put("sysDataTracesLog", sysDataTracesLog);
        SysDataTracesDetail sysDataTracesDetail = new SysDataTracesDetail();
        sysDataTracesDetail.setDatatracesId(sysDataTracesLog.getId());
        mmap.put("sysDataTracesDetails", sysDataTracesDetailService.selectSysDataTracesDetailList(sysDataTracesDetail));
        return prefix + "/detail";
    }

    /**
     * 修改保存数据追溯日志
     */
    @RequiresPermissions("system:datatraceslog:edit")
    @Log(title = "数据追溯日志", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysDataTracesLog sysDataTracesLog){
        return toAjax(sysDataTracesLogService.updateSysDataTracesLog(sysDataTracesLog));
    }

    /**
     * 删除数据追溯日志
     */
    @RequiresPermissions("system:datatraceslog:remove")
    @Log(title = "数据追溯日志", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysDataTracesLogService.logicDeleteSysDataTracesLogByIds(ids));
    }
}
