//package com.gsh.aiexam.web.controller.sms;
package com.gsh.system.sms.controller;

import com.gsh.common.annotation.Log;
import com.gsh.common.core.controller.BaseController;
import com.gsh.common.core.domain.AjaxResult;
import com.gsh.common.core.page.TableDataInfo;
import com.gsh.common.enums.BusinessType;
import com.gsh.common.utils.poi.ExcelUtil;
import com.gsh.framework.util.ShiroUtils;
import com.gsh.system.sms.domain.SysSmsTemplate;
import com.gsh.system.sms.service.ISysSmsTemplateService;
import com.gsh.system.utils.FactoryUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 短信模板配置Controller
 * 
 * @author gsh
 * @date 2019-09-04
 */
@Controller
@RequestMapping("/system/sms/template")
public class SysSmsTemplateController extends BaseController {
	private String prefix = "system/sms/template";

	@Autowired
	private ISysSmsTemplateService sysSmsTemplateService;

	@RequiresPermissions("system:sms:template:view")
	@GetMapping()
	public String template(ModelMap mmap){
		mmap.put("factoryCode", FactoryUtils.getFactoryCode());
		if(FactoryUtils.getFactoryTreeMode()){
			return prefix + "/templatetree";
		}
		return prefix + "/template";
	}

	/**
	 * 查询短信模板配置列表
	 */
	@RequiresPermissions("system:sms:template:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(SysSmsTemplate sysSmsTemplate) {
		startPage();
		Long deptId = ShiroUtils.getSysUser().getDept().getDeptId();
		// 默认查询该部门的数据.
		sysSmsTemplate.setCreateByDeptid(deptId);
		// 查询未删除的数据
		sysSmsTemplate.setDeleted("0");
		List<SysSmsTemplate> list = sysSmsTemplateService.selectSysSmsTemplateList(sysSmsTemplate);
		return getDataTable(list);
	}

	/**
	 * 导出短信模板配置列表
	 */
	@RequiresPermissions("system:sms:template:export")
	@PostMapping("/export")
	@ResponseBody
	public AjaxResult export(SysSmsTemplate sysSmsTemplate) {
		Long deptId = ShiroUtils.getSysUser().getDept().getDeptId();
		// 默认查询该部门的数据.
		sysSmsTemplate.setCreateByDeptid(deptId); 
		// 查询未删除的数据
		sysSmsTemplate.setDeleted("0");
		List<SysSmsTemplate> list = sysSmsTemplateService.selectSysSmsTemplateList(sysSmsTemplate);
		ExcelUtil<SysSmsTemplate> util = new ExcelUtil<SysSmsTemplate>(SysSmsTemplate.class);
		return util.exportExcel(list, "template");
	}

	/**
	 * 新增短信模板配置
	 */
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}

	/**
	 * 新增短信模板配置
	 */
	@GetMapping("/{factoryCode}/add")
	public String addByFactoryCode(@PathVariable("factoryCode") Long factoryCode, ModelMap mmap)
	{
		mmap.put("factoryCode", factoryCode);
		return prefix + "/add";
	}

	/**
	 * 新增保存短信模板配置
	 */
	@RequiresPermissions("system:sms:template:add")
	@Log(title = "短信模板配置", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(SysSmsTemplate sysSmsTemplate) {
		return toAjax(sysSmsTemplateService.insertSysSmsTemplate(sysSmsTemplate));
	}

	/**
	 * 修改短信模板配置
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap) {
		SysSmsTemplate sysSmsTemplate = sysSmsTemplateService.selectSysSmsTemplateById(id);
		mmap.put("sysSmsTemplate", sysSmsTemplate);
		return prefix + "/edit";
	}

	/**
	 * 修改保存短信模板配置
	 */
	@RequiresPermissions("system:sms:template:edit")
	@Log(title = "短信模板配置", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(SysSmsTemplate sysSmsTemplate) {
		return toAjax(sysSmsTemplateService.updateSysSmsTemplate(sysSmsTemplate));
	}

	/**
	 * 删除短信模板配置
	 */
	@RequiresPermissions("system:sms:template:remove")
	@Log(title = "短信模板配置", businessType = BusinessType.DELETE)
	@PostMapping("/remove")
	@ResponseBody
	public AjaxResult remove(String ids) {
		return toAjax(sysSmsTemplateService.logicDeleteSysSmsTemplateByIds(ids));
	}
}
