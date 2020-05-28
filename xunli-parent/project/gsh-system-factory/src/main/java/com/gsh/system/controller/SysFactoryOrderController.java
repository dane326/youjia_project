package com.gsh.system.controller;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.google.common.collect.Maps;
import com.gsh.common.annotation.Log;
import com.gsh.common.config.FactoryConfig;
import com.gsh.common.config.Global;
import com.gsh.common.core.controller.BaseController;
import com.gsh.common.core.domain.AjaxResult;
import com.gsh.common.core.page.TableDataInfo;
import com.gsh.common.enums.BusinessType;
import com.gsh.common.utils.StringUtils;
import com.gsh.common.utils.poi.ExcelUtil;
import com.gsh.product.constants.OrderTypeConstants;
import com.gsh.product.constants.PayMethodConstants;
import com.gsh.product.service.ISysProductService;
import com.gsh.system.domain.SysFactoryOrder;
import com.gsh.system.service.ISysFactoryOrderService;
import com.gsh.system.utils.FactoryUtils;
import com.gsh.system.utils.QRCodeUtils;

/**
 * 订单管理Controller
 * 
 * @author gsh
 * @date 2019-10-29
 */
@Controller
@RequestMapping("/system/factory/order")
public class SysFactoryOrderController extends BaseController {
	// 独立部署版，删除订单管理代码
	private String prefix = "system/factory/order";

	@Autowired
	private ISysFactoryOrderService sysFactoryOrderService;

	@Autowired
	private ISysProductService sysProductService;

	@RequiresPermissions("system:factory:order:view")
	@GetMapping()
	public String order() {
		return prefix + "/order";
	}

	/**
	 * 查询订单管理列表
	 */
	@RequiresPermissions("system:factory:order:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(SysFactoryOrder sysFactoryOrder) {
		startPage();
		Long deptId = FactoryUtils.getSysUser().getDept().getDeptId();
		sysFactoryOrder.setCreateByDeptid(deptId);// 默认查询该部门的数据.
		sysFactoryOrder.setDeleted("0");// 查询未删除的数据
		List<SysFactoryOrder> list = sysFactoryOrderService.selectSysFactoryOrderList(sysFactoryOrder);
		return getDataTable(list);
	}

	/**
	 * 导出订单管理列表
	 */
	@RequiresPermissions("system:factory:order:export")
	@PostMapping("/export")
	@ResponseBody
	public AjaxResult export(SysFactoryOrder sysFactoryOrder) {
		Long deptId = FactoryUtils.getSysUser().getDept().getDeptId();
		sysFactoryOrder.setCreateByDeptid(deptId);// 默认查询该部门的数据.
		sysFactoryOrder.setDeleted("0");// 查询未删除的数据
		List<SysFactoryOrder> list = sysFactoryOrderService.selectSysFactoryOrderList(sysFactoryOrder);
		ExcelUtil<SysFactoryOrder> util = new ExcelUtil<SysFactoryOrder>(SysFactoryOrder.class);
		return util.exportExcel(list, "order");
	}

	/**
	 * 删除订单管理
	 */
	@RequiresPermissions("system:factory:order:remove")
	@Log(title = "订单管理", businessType = BusinessType.DELETE)
	@PostMapping("/remove")
	@ResponseBody
	public AjaxResult remove(String ids) {
		return toAjax(sysFactoryOrderService.logicDeleteSysFactoryOrderByIds(ids));
	}

	/**
	 * 订单
	 */
	@GetMapping("/pay/{orderType}")
	public String pay(@PathVariable("orderType") String orderType, ModelMap mmap, HttpServletRequest request) {
		mmap.put("orderType", orderType);
		mmap.put("currentTimeMillis", System.currentTimeMillis());
		
		if(OrderTypeConstants.VERSIONS.equals(orderType)){
			mmap.put("versionTypes", sysProductService.selectInfoListByContextName(Global.getContextPathNoSeparator()));
		}
		return prefix + "/pay/" + orderType;
	}

	@PostMapping("/pay")
	@ResponseBody
	public AjaxResult pay(SysFactoryOrder sysFactoryOrder, HttpServletRequest request) throws AlipayApiException {
		sysFactoryOrder.setOrderNo(FactoryUtils.uuid());
		sysFactoryOrder.setFactoryCode(FactoryUtils.getFactoryCode());
		
		/*// 授权期限
		LocalDateTime localDate = LocalDateTime.now();
		if (StringUtils.isNotEmpty(sysFactoryOrder.getBuyNum())) {
			int buyNum = Integer.valueOf(sysFactoryOrder.getBuyNum());
			if ("year".equals(sysFactoryOrder.getBuyMethod())) {
				localDate = localDate.plusYears(buyNum);
			} else if ("month".equals(sysFactoryOrder.getBuyMethod())) {
				localDate = localDate.plusMonths(buyNum);
			}
		}
		sysFactoryOrder.setBuyPeriod(Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant()));
		// money
		sysFactoryOrder.setMoney("0.01");*/

		sysFactoryOrderService.insertSysFactoryOrder(sysFactoryOrder);
		Map<String, Object> resultMap = Maps.newHashMap();
		resultMap.put("codeUrl", prefix + "/QRcode/" + sysFactoryOrder.getOrderNo());
		resultMap.put("orderNo", sysFactoryOrder.getOrderNo());
		return AjaxResult.success("二维码信息", resultMap);
	}
	
	@RequiresPermissions("system:factory:order:view")
	@GetMapping("/QRcode/{orderNo}")
	public void qrcode(@PathVariable("orderNo") String orderNo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		String qrcode = FactoryConfig.getPayQRcodeUrl() + "/" + orderNo;
		//logger.info(qrcode);
		response.getOutputStream().write(QRCodeUtils.QREncode(qrcode, outStream));
	}
	
	@RequiresPermissions("system:factory:order:view")
	@GetMapping("/pay/query/{orderNo}")
	@ResponseBody
	public AjaxResult queryPay(@PathVariable("orderNo") String orderNo, HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = Maps.newHashMap();
		
		SysFactoryOrder sysFactoryOrder = new SysFactoryOrder();
		sysFactoryOrder.setOrderNo(orderNo);
		List<SysFactoryOrder> factoryOrders = sysFactoryOrderService.selectSysFactoryOrderList(sysFactoryOrder);
		if (CollectionUtils.isNotEmpty(factoryOrders)) {
			sysFactoryOrder = factoryOrders.get(0);
			String payStatus = sysFactoryOrder.getPayStatus();
			// 交易成功或者交易关闭
			if("1".equals(payStatus) || "2".equals(sysFactoryOrder.getPayStatus())){
				resultMap.put("payStatus", sysFactoryOrder.getPayStatus());
			} else {
				String payMethod = sysFactoryOrder.getPayMethod();
				if(StringUtils.isNotEmpty(payMethod)){
					if(PayMethodConstants.ALIPAY.equals(payMethod)){
						// 调用支付宝查询接口
					} else if(PayMethodConstants.WEIXIN.equals(payMethod)){
						// 调用微信查询接口
					}
				}
			}		
		}
		return AjaxResult.success("订单状态", resultMap);
	}
	
	@GetMapping("/sync/order")
	public void syncOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(FactoryConfig.syncDataToProdSwitch()){
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("deleted", "0");
			paramMap.put("factoryCode", 0);
			paramMap.put("contextName", Global.getContextPathNoSeparator());
			sysProductService.selectSysProdOrderList(paramMap);
		}
	}
}