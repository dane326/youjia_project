package com.gsh.system.queryport.controller;

import com.gsh.common.annotation.Log;
import com.gsh.common.core.controller.BaseController;
import com.gsh.common.core.domain.AjaxResult;
import com.gsh.common.core.page.TableDataInfo;
import com.gsh.common.enums.BusinessType;
import com.gsh.common.utils.poi.ExcelUtil;
import com.gsh.framework.util.ShiroUtils;
import com.gsh.system.queryport.domain.QueryportChart;
import com.gsh.system.queryport.domain.QueryportColumn;
import com.gsh.system.queryport.domain.QueryportMain;
import com.gsh.system.queryport.domain.QueryportParam;
import com.gsh.system.queryport.service.IQueryportChartService;
import com.gsh.system.queryport.service.IQueryportColumnService;
import com.gsh.system.queryport.service.IQueryportMainService;
import com.gsh.system.queryport.service.IQueryportParamService;
import com.gsh.system.queryport.util.QueryPortMainUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * 查询视图配置Controller
 * 
 * @author Yang Feng
 * @date 2020-02-22
 */
@Controller
@RequestMapping("/system/queryport")
public class QueryportMainController extends BaseController {
    private String prefix = "system/queryport/main";

    @Autowired
    private IQueryportMainService queryportMainService;

    @Autowired
    private IQueryportColumnService queryportColumnService;

    @Autowired
    private IQueryportParamService queryportParamService;

    @Autowired
    private IQueryportChartService queryportChartService;

    @RequiresPermissions("system:queryport:view")
    @GetMapping()
    public String queryport()
    {
        return prefix + "/queryport";
    }

    @GetMapping("/main")
    public String main()
    {
        return prefix + "/main";
    }

    @GetMapping("/tabfield")
    public String tabfield()
    {
        return prefix + "/tabfieldui";
    }

    /**
     * 查询查询视图配置列表
     */
    @RequiresPermissions("system:queryport:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(QueryportMain queryportMain){
        startPage();
        Long deptId=ShiroUtils.getSysUser().getDept().getDeptId();
        queryportMain.setCreateByDeptid(deptId);//默认查询该部门的数据.
        queryportMain.setDeleted("0");//查询未删除的数据
        List<QueryportMain> list = queryportMainService.selectQueryportMainList(queryportMain);
        return getDataTable(list);
    }


    /**
     * 新增查询视图配置
     */
    @GetMapping("/column/{mainId}")
    public String column(@PathVariable("mainId") Long mainId, ModelMap mmap)
    {
        mmap.put("mainId", mainId);
        mmap.put("queryportMain",queryportMainService.selectQueryportMainById(mainId));
        return prefix + "/column";
    }

    /**
     * 新增查询视图配置
     */
    @GetMapping("/param/{mainId}")
    public String param(@PathVariable("mainId") Long mainId, ModelMap mmap)
    {
        mmap.put("mainId", mainId);
        mmap.put("queryportMain",queryportMainService.selectQueryportMainById(mainId));
        return prefix + "/param";
    }

    /**
     * 查询查询视图配置列表
     */
    @RequiresPermissions("system:queryport:list")
    @PostMapping("/column/list")
    @ResponseBody
    public TableDataInfo columnList(QueryportColumn queryportColumn){
        queryportColumn.setDeleted("0");//查询未删除的数据
        List<QueryportColumn> list = queryportColumnService.selectQueryportColumnList(queryportColumn);
        return getDataTable(list);
    }

    /**
     * 查询查询视图配置列表
     */
    @RequiresPermissions("system:queryport:list")
    @PostMapping("/param/list")
    @ResponseBody
    public TableDataInfo paramList(QueryportParam queryportParam){
        queryportParam.setDeleted("0");//查询未删除的数据
        List<QueryportParam> list = queryportParamService.selectQueryportParamList(queryportParam);
        return getDataTable(list);
    }

    /**
     * 导出查询视图配置列表
     */
    @RequiresPermissions("system:queryport:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(QueryportMain queryportMain){
        Long deptId=ShiroUtils.getSysUser().getDept().getDeptId();
        queryportMain.setCreateByDeptid(deptId);//默认查询该部门的数据.
        queryportMain.setDeleted("0");//查询未删除的数据
        List<QueryportMain> list = queryportMainService.selectQueryportMainList(queryportMain);
        ExcelUtil<QueryportMain> util = new ExcelUtil<QueryportMain>(QueryportMain.class);
        return util.exportExcel(list, "queryport");
    }

    /**
     * 新增查询视图配置
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存查询视图配置
     */
    @RequiresPermissions("system:queryport:add")
    @Log(title = "查询视图配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(QueryportMain queryportMain){
        if(QueryPortMainUtil.validateStatement(queryportMain.getStatement())){
            queryportMain.setDeleted("0");//查询未删除的数据
            //主要配置数据
            queryportMainService.insertQueryportMain(queryportMain);
            //参数配置初始化
            queryportParamService.insertQueryportParam(queryportMain);
            //列配置初始化
            queryportColumnService.insertQueryportColumn(queryportMain);
            //图形配置初始化
            //queryportChartService.insertQueryportChart(queryportMain);
            return AjaxResult.success(queryportMain);
        }else{
            return AjaxResult.error("error");
        }
    }

    /**
     * 修改查询视图配置
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap){
        QueryportMain queryportMain = queryportMainService.selectQueryportMainById(id);
        mmap.put("queryportMain", queryportMain);
        return prefix + "/edit";
    }

    /**
     * 修改保存查询视图配置
     */
    @RequiresPermissions("system:queryport:edit")
    @Log(title = "查询视图配置", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(QueryportMain queryportMain) throws Exception {
        if(QueryPortMainUtil.validateStatement(queryportMain.getStatement())){
            queryportMainService.updateQueryportMain(queryportMain);
            queryportParamService.updateQueryportParam(queryportMain);
            queryportColumnService.updateQueryportColumn(queryportMain);
            return toAjax(1);
        }else{
            return AjaxResult.error("error");
        }
    }

    /**
     * 修改保存查询视图配置
     */
    @RequiresPermissions("system:queryport:edit")
    @Log(title = "查询视图配置列保存", businessType = BusinessType.UPDATE)
    @PostMapping("/column/save")
    @ResponseBody
    public AjaxResult columnSave(QueryportMain queryportMain) throws Exception {
        if(queryportMain != null && queryportMain.getQueryportColumns() != null){
            for (QueryportColumn queryportColumn: queryportMain.getQueryportColumns()) {
                queryportColumnService.updateQueryportColumn(queryportColumn);
            }
            return toAjax(1);
        }else{
            return AjaxResult.error("error");
        }
    }

    /**
     * 修改保存查询视图配置
     */
    @RequiresPermissions("system:queryport:edit")
    @Log(title = "查询视图配置参数保存", businessType = BusinessType.UPDATE)
    @PostMapping("/param/save")
    @ResponseBody
    public AjaxResult paramSave(QueryportMain queryportMain) throws Exception {
        if(queryportMain != null && queryportMain.getQueryportParams() != null){
            for (QueryportParam queryportParam: queryportMain.getQueryportParams()) {
                queryportParamService.updateQueryportParam(queryportParam);
            }
        }
        return toAjax(1);
    }

     @Log(title = "查询视图配置图标删除", businessType = BusinessType.DELETE)
    @PostMapping("/chart/delete")
    @ResponseBody
    public AjaxResult chartDelte(String ids) {
        return toAjax(queryportChartService.deleteQueryportChartByIds(ids));
    }

    /**
     * 视图配置
     */
    @GetMapping("/chart/{mainId}")
    public String chart(@PathVariable("mainId") Long mainId, ModelMap mmap)
    {
        QueryportMain queryportMain = queryportMainService.selectQueryportMainById(mainId);
        mmap.put("queryportMain", queryportMain);
        return prefix + "/chart";
    }

    /**
     * 查询视图配置列表
     */
    @PostMapping("/chart/list")
    @ResponseBody
    public TableDataInfo chartList(QueryportChart queryportChart,ModelMap mmap){
        List<QueryportChart> list = queryportChartService.selectQueryportChartList(queryportChart);
        return getDataTable(list);
    }

    /**
     * 修改保存查询视图配置
     */
    @Log(title = "查询视图配置参数保存", businessType = BusinessType.UPDATE)
    @PostMapping("/chart/save")
    @ResponseBody
    public AjaxResult chartSave(QueryportMain queryportMain) throws Exception {
        if(queryportMain != null && queryportMain.getQueryportCharts() != null){
            for (QueryportChart queryportChart: queryportMain.getQueryportCharts()) {
                if(queryportChart.getId()<0){
                    queryportChartService.insertQueryportChart(queryportChart);
                }else{

                    queryportChartService.updateQueryportChart(queryportChart);
                }
            }
        }else {
            if("shape".equals(queryportMain.getShowType())){
                return AjaxResult.error("视图显示方式必须配置chart");
            }
        }
        return toAjax(1);
    }

    /**
     * 修改查询视图配置
     */
    @GetMapping("/display/{mainId}")
    @Log(title = "查询视图", businessType = BusinessType.OTHER)
    public String display(@PathVariable("mainId") Long mainId, ModelMap mmap){
        //验证权限
        queryportMainService.validateQueryportMainPerms(mainId);
        QueryportMain queryportMain = queryportMainService.selectQueryportMainByMainId(mainId);
        QueryPortMainUtil.initParamDefaultValues(queryportMain);
        mmap.put("queryportMain", queryportMain);
        return prefix + "/display";
    }


    /**
     * 查询查询视图配置列表
     */
    @PostMapping("/display/list")
    @ResponseBody
    public TableDataInfo displayList(QueryportMain queryport){
        queryportMainService.validateQueryportMainPerms(queryport.getId());
        QueryportMain queryportMain = queryportMainService.selectQueryportMainByMainId(queryport.getId());
        queryportMain.setParams(queryport.getParams());
        return queryportMainService.queryportMainForPage(queryportMain);
    }
    /**
     * 修改查询视图配置
     */
    @Log(title = "查询视图", businessType = BusinessType.EXPORT)
    @PostMapping("/display/export")
    public void displayExport(QueryportMain main, HttpServletResponse response){
        QueryportMain queryportMain = queryportMainService.selectQueryportMainByMainId(main.getId());
        queryportMain.setParams(main.getParams());
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            logger.info("正在导出xlsx....");
            List<?> list = queryportMainService.queryportMainForList(queryportMain).getRows();
            QueryPortMainUtil.exportExcel(list,"sheet",queryportColumnService.selectQueryportColumnList(main.getId()),os);
            logger.info("导出成功,共计"+list.size()+"条数据");
            byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            // 设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename="+ new String((queryportMain.getName() + ".xlsx").getBytes(), "iso-8859-1"));
            response.setContentLength(content.length);
            ServletOutputStream outputStream = response.getOutputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            BufferedOutputStream bos = new BufferedOutputStream(outputStream);
            byte[] buff = new byte[8192];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
            bis.close();
            bos.close();
            outputStream.flush();
            outputStream.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询查询视图配置列表
     */
    @GetMapping("/conf/{mainId}")
    @ResponseBody
    public AjaxResult conf(@PathVariable("mainId") Long mainId){
        return AjaxResult.success("操作成功", queryportMainService.selectQueryportMainByMainId(mainId));
    }

    /**
     * 删除查询视图配置
     */
    @RequiresPermissions("system:queryport:remove")
    @Log(title = "查询视图配置", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(queryportMainService.logicDeleteQueryportMainByIds(ids));
    }
}
