//package com.gsh.aiexam.web.controller.sms;
package com.gsh.system.sms.controller;

import com.gsh.common.annotation.Log;
import com.gsh.common.core.controller.BaseController;
import com.gsh.common.core.domain.AjaxResult;
import com.gsh.common.core.page.TableDataInfo;
import com.gsh.common.enums.BusinessType;
import com.gsh.common.utils.poi.ExcelUtil;
import com.gsh.framework.util.ShiroUtils;
import com.gsh.system.sms.domain.SysSmsRecord;
import com.gsh.system.sms.service.ISysSmsRecordService;
import com.gsh.system.utils.FactoryUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 短信发送记录Controller
 * 
 * @author gsh
 * @date 2019-09-04
 */
@Controller
@RequestMapping("/system/sms/record")
public class SysSmsRecordController extends BaseController {
	private String prefix = "system/sms/record";

	@Autowired
	private ISysSmsRecordService sysSmsRecordService;

	@RequiresPermissions("system:sms:record:view")
	@GetMapping()
	public String record(ModelMap mmap) {
		mmap.put("factoryCode", FactoryUtils.getFactoryCode());
		if(FactoryUtils.getFactoryTreeMode()){
			return prefix + "/recordtree";
		}
		return prefix + "/record";
	}

	/**
	 * 查询短信发送记录列表
	 */
	@RequiresPermissions("system:sms:record:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(SysSmsRecord sysSmsRecord) {
		startPage();
		Long deptId = ShiroUtils.getSysUser().getDept().getDeptId();
		// 默认查询该部门的数据.
		sysSmsRecord.setCreateByDeptid(deptId);
		// 查询未删除的数据
		sysSmsRecord.setDeleted("0");
		List<SysSmsRecord> list = sysSmsRecordService.selectSysSmsRecordList(sysSmsRecord);
		return getDataTable(list);
	}

	/**
	 * 导出短信发送记录列表
	 */
	@RequiresPermissions("system:sms:record:export")
	@PostMapping("/export")
	@ResponseBody
	public AjaxResult export(SysSmsRecord sysSmsRecord) {
		Long deptId = ShiroUtils.getSysUser().getDept().getDeptId();
		// 默认查询该部门的数据.
		sysSmsRecord.setCreateByDeptid(deptId);
		// 查询未删除的数据
		sysSmsRecord.setDeleted("0");
		List<SysSmsRecord> list = sysSmsRecordService.selectSysSmsRecordList(sysSmsRecord);
		ExcelUtil<SysSmsRecord> util = new ExcelUtil<SysSmsRecord>(SysSmsRecord.class);
		return util.exportExcel(list, "record");
	}

	/**
	 * 新增短信发送记录
	 */
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}

	/**
	 * 新增保存短信发送记录
	 */
	@RequiresPermissions("system:sms:record:add")
	@Log(title = "短信发送记录", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(SysSmsRecord sysSmsRecord) {
		// 查询未删除的数据
		sysSmsRecord.setDeleted("0");

		return toAjax(sysSmsRecordService.insertSysSmsRecord(sysSmsRecord));
	}

	/**
	 * 修改短信发送记录
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap) {
		SysSmsRecord sysSmsRecord = sysSmsRecordService.selectSysSmsRecordById(id);
		mmap.put("sysSmsRecord", sysSmsRecord);
		return prefix + "/edit";
	}

	/**
	 * 修改保存短信发送记录
	 */
	@RequiresPermissions("system:sms:record:edit")
	@Log(title = "短信发送记录", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(SysSmsRecord sysSmsRecord) {
		return toAjax(sysSmsRecordService.updateSysSmsRecord(sysSmsRecord));
	}

	/**
	 * 删除短信发送记录
	 */
	@RequiresPermissions("system:sms:record:remove")
	@Log(title = "短信发送记录", businessType = BusinessType.DELETE)
	@PostMapping("/remove")
	@ResponseBody
	public AjaxResult remove(String ids) {
		return toAjax(sysSmsRecordService.logicDeleteSysSmsRecordByIds(ids));
	}
}
