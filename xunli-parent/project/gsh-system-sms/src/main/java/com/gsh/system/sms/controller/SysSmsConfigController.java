//package com.gsh.aiexam.web.controller.sms;
package com.gsh.system.sms.controller;

import com.gsh.system.sms.domain.SysSmsConfig;
import com.gsh.system.sms.domain.SysSmsRecord;
import com.gsh.system.sms.service.ISysSmsConfigService;
import com.gsh.system.sms.service.ISysSmsRecordService;
import com.gsh.common.annotation.Log;
import com.gsh.common.core.controller.BaseController;
import com.gsh.common.core.domain.AjaxResult;
import com.gsh.common.core.page.TableDataInfo;
import com.gsh.common.enums.BusinessType;
import com.gsh.common.utils.poi.ExcelUtil;
import com.gsh.framework.util.ShiroUtils;
import com.gsh.system.utils.FactoryUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 短信接口配置Controller
 * 
 * @author gsh
 * @date 2019-09-04
 */
@Controller
@RequestMapping("/system/sms/config")
public class SysSmsConfigController extends BaseController {
	private String prefix = "system/sms/config";

	@Autowired
	private ISysSmsConfigService sysSmsConfigService;
	
	@Autowired
	private ISysSmsRecordService sysSmsRecordService;

	@RequiresPermissions("system:sms:config:view")
	@GetMapping()
	public String config(ModelMap mmap) {
		mmap.put("factoryCode", FactoryUtils.getFactoryCode());
		if(FactoryUtils.getFactoryTreeMode()){
			return prefix + "/configtree";
		}
		return prefix + "/config";
	}

	/**
	 * 查询短信接口配置列表
	 */
	@RequiresPermissions("system:sms:config:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(SysSmsConfig sysSmsConfig) {
		startPage();
		// 查询未删除的数据
		sysSmsConfig.setDeleted("0");
		List<SysSmsConfig> list = sysSmsConfigService.selectSysSmsConfigList(sysSmsConfig);
		return getDataTable(list);
	}

	/**
	 * 导出短信接口配置列表
	 */
	@RequiresPermissions("system:sms:config:export")
	@PostMapping("/export")
	@ResponseBody
	public AjaxResult export(SysSmsConfig sysSmsConfig) {
		// 查询未删除的数据
		sysSmsConfig.setDeleted("0");
		List<SysSmsConfig> list = sysSmsConfigService.selectSysSmsConfigList(sysSmsConfig);
		ExcelUtil<SysSmsConfig> util = new ExcelUtil<SysSmsConfig>(SysSmsConfig.class);
		return util.exportExcel(list, "config");
	}

	/**
	 * 新增短信接口配置
	 */
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}

	/**
	 * 新增短信接口配置
	 */
	@GetMapping("/{factoryCode}/add")
	public String addByFactoryCode(@PathVariable("factoryCode") Long factoryCode, ModelMap mmap)
	{
		mmap.put("factoryCode", factoryCode);
		return prefix + "/add";
	}

	/**
	 * 新增保存短信接口配置
	 */
	@RequiresPermissions("system:sms:config:add")
	@Log(title = "短信接口配置", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(SysSmsConfig sysSmsConfig) {
		return toAjax(sysSmsConfigService.insertSysSmsConfig(sysSmsConfig));
	}

	/**
	 * 修改短信接口配置
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap) {
		SysSmsConfig sysSmsConfig = sysSmsConfigService.selectSysSmsConfigById(id);
		mmap.put("sysSmsConfig", sysSmsConfig);
		return prefix + "/edit";
	}

	/**
	 * 修改保存短信接口配置
	 */
	@RequiresPermissions("system:sms:config:edit")
	@Log(title = "短信接口配置", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(SysSmsConfig sysSmsConfig) {
		return toAjax(sysSmsConfigService.updateSysSmsConfig(sysSmsConfig));
	}

	/**
	 * 测试短信接口配置
	 */
	@RequiresPermissions("system:sms:config:test")
	@Log(title = "测试短信接口配置", businessType = BusinessType.OTHER)
	@GetMapping("/triy/{id}")
	@ResponseBody
	public AjaxResult triy(@PathVariable("id") Long id) {
		SysSmsRecord sysSmsRecord = new SysSmsRecord();
		sysSmsRecord.setReceiverLoginName(ShiroUtils.getLoginName());
		sysSmsRecord.setReceiverMobile("13838345915");
		sysSmsRecord.setSmsContent("387645");
		sysSmsRecord.setSmsType("sms_verify");
		return toAjax(sysSmsConfigService.triySysSmsConfig(id, sysSmsRecord));
	}

	/**
	 * 删除短信接口配置
	 */
	@RequiresPermissions("system:sms:config:remove")
	@Log(title = "短信接口配置", businessType = BusinessType.DELETE)
	@PostMapping("/remove")
	@ResponseBody
	public AjaxResult remove(String ids) {
		return toAjax(sysSmsConfigService.logicDeleteSysSmsConfigByIds(ids));
	}
}
