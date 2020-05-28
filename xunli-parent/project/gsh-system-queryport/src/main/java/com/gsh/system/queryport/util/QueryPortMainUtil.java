package com.gsh.system.queryport.util;

import com.gsh.common.exception.BusinessException;
import com.gsh.common.utils.DateUtils;
import com.gsh.common.utils.StringUtils;
import com.gsh.common.utils.spring.SpringUtils;
import com.gsh.framework.util.Id2NameUtil;
import com.gsh.framework.util.ShiroUtils;
import com.gsh.framework.web.service.DictService;
import com.gsh.system.queryport.domain.QueryportColumn;
import com.gsh.system.queryport.domain.QueryportMain;
import com.gsh.system.queryport.domain.QueryportParam;
import com.gsh.system.service.ISysDeptService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 查询视图 工具类
 */
public class QueryPortMainUtil {

    /**
     * //别名分隔标记
     */
    public static final String EXPRESSION_ALIAS_SIGN = " AS ";

    /**
     * //条件分隔标记
     */
    public static final String CONDITON_CONNECT_SIGN = " AND ";

    /**
     * //替换并移除的条件标记
     */
    public static final String EXPRESSION_MOVE_SIGN = "#######";

    /**
     * //替换并移除的条件标记
     */
    public static final String PARAM_REGX = "#\\{(.*?)\\}";

    /**
     * //可多选的查询条件类型
     */
    public static final String[] MULTI_PARAM_INPUT_MODE = {
            "dictSelectMulti", "dictCheckBox",
            "codeDictSelectMulti", "codeDictCheckBox",
            "userTreeMulti", "deptTreeMulti", "roleTreeMulti",
            "factoryDeptTreeMulti", "factoryUserTreeMulti", "factoryRoleTreeMulti"
    };

    /**
     * //可多选的查询条件类型List
     */
    public static final List<String> MULTI_PARAM_INPUT_MODE_LIST = Arrays.asList(MULTI_PARAM_INPUT_MODE);

    /**
     * //日期格式化字符串
     */
    public static final String DATE_FORMATE_STR = "yyyy-MM-dd";

    /**
     * //时间格式化字符串
     */
    public static final String TIME_FORMATE_STR = "yyyy-MM-dd HH:mm:ss";

     /**
     * 样式列表
     */
    private static Map<String, CellStyle> styles;

    private static final Logger log = LoggerFactory.getLogger(QueryPortMainUtil.class);


    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String sql= ""
                + "select a.areaid,a.areaname,nvl(mm.autocount,0) autocount,nvl(mm.autoholdcount,0) autoholdcount from taw_system_area_tower a , ( "
                + "   select m.maincity, "
                + "          sum(case when m.senduserid = 'admin' then 1 else 0 end) autocount, "
                + "          sum(case when m.senduserid = 'admin' and m.status = 1 then 1 else 0 end) autoholdcount "
                + "     from pnr_faulttowersheet_main m   "
                + "    where m.sendtime between to_date('2018-10-20 00:00:00', 'yyyy-mm-dd hh24:mi:ss')  and to_date('2018-10-20 10:00:00', 'yyyy-mm-dd hh24:mi:ss') "
                + "      and m.deleted <>1 "
                + "      and m.status<>-12 "
                + "    group by  m.maincity) mm "
                + " where mm.maincity (+) = a.areaid "
                + " and a.parentareaid=17 "
                + " order by a.remark"
                + "";
        System.out.println(sql);
        QueryPortDruidParser.parserAsColumn(sql);
    }


    /**
     * 验证语句
     * @param statement
     * @return
     */
    public static boolean validateStatement(String statement) {
        if(statement!=null
                && !"".equals(statement)
                && (statement.startsWith("select ") || statement.startsWith("SELECT "))
                && statement.indexOf(";")==-1){
            return true;
        }
        return false;
    }

    /**
     * 解析查询条件
     * @param sql
     * @return
     * @throws Exception
     */
    public static Map<String,String> parserExpression(String sql) {
        return QueryPortDruidParser.parserExpression(sql);
    }

    /**
     * 解析查询条件
     * @param queryportMain
     * @throws Exception
     */
    public static void parserExpressionToMain(QueryportMain queryportMain) {
        Map<String, String> expressionMap = QueryPortMainUtil.parserExpression(queryportMain.getStatement());
        queryportMain.setExpressionColumn(expressionMap.get("expressioncolumn"));
        queryportMain.setExpressionFrom(expressionMap.get("expressionfrom"));
        queryportMain.setExpressionWhere(expressionMap.get("expressionwhere"));
        queryportMain.setExpressionGroupby(expressionMap.get("expressiogroupby"));
        queryportMain.setExpressionHaving(expressionMap.get("expressiohaving"));
        queryportMain.setExpressionOrderby(expressionMap.get("expressioorderby"));
    }

    /**
     * 如果没有定义别名，组织列名，缺掉特殊字符内容 做为别名
     * @param column
     * @return
     */
    public static String getColumnAliasName(String column) {
        String columnAlias = column;
        columnAlias = columnAlias.replaceAll("[^\\w]","");
        return columnAlias;
    }



    /**
     * getExpressionString
     * @param expression
     * @return
     */
    public static String getExpressionString(Object expression) {
        String result = "";
        if(expression!=null){
            result = expression.toString();
            if(result.startsWith("[")){
                result = result.substring(1,result.length());
            }
            if(result.endsWith("]")){
                result = result.substring(0,result.length()-1);
            }
        }
        return result;
    }

    /**
     * 去掉多余的 AS
     * @param temp
     * @return
     */
    public static String removeUselessAlias(String temp) {
        if(findAlias(temp,QueryPortMainUtil.EXPRESSION_ALIAS_SIGN) > 1){
            temp = temp.replaceFirst(QueryPortMainUtil.EXPRESSION_ALIAS_SIGN, " ");
        }else{
            return temp;
        }
        return removeUselessAlias(temp);
    }



    /**
     * 查找匹配内容在指定字符串中出现的次数
     * @param source
     * @param regexStr
     * @return
     */
    public static int findAlias(String source, String regexStr) {
        Pattern expression = Pattern.compile(regexStr);
        Matcher matcher = expression.matcher(source);

        int n = 0;
        while (matcher.find()) {
            n++;
        }
        return n;
    }



    /**
     * 正则表达式匹配两个指定字符串中间的内容
     * @param soap 匹配的内容
     * @param rgex 匹配规则
     * @param repeat 是否允许重复
     * @return
     */
    public static List<String> parserParam(String soap, String rgex, boolean repeat){
        List<String> list = new ArrayList<String>();
        // 匹配的模式
        Pattern pattern = Pattern.compile(rgex);
        Matcher m = pattern.matcher(soap);
        while (m.find()) {
            int i = 1;
            String p = m.group(i);
            if(!list.contains(p)){
                list.add(m.group(i));
            }else if(repeat){
                list.add(m.group(i));
            }
            i++;
        }
        return list;
    }


    /**
     * 获取没有as别名内容的查询结果list
     * @param sql
     * @return
     * @throws Exception
     */
    public static List<String> parserColumn(String sql) throws Exception {
        return QueryPortDruidParser.parserColumn(sql);
    }

    /**
     * 组织查询条件和查询语句
     * @param queryportMain
     * @return
     */
    public static List<Object> dealQueryPortParameter(QueryportMain queryportMain, StringBuffer queryStatementBuffer) {
        String queryStatement = "";
        queryStatement = QueryPortMainUtil.reDealStatementParser(queryportMain, "queryBinding");
        List<Object> queryParamList = new ArrayList<Object>();
        queryStatement = resolveQueryPortParameter(queryportMain, queryStatement, queryParamList);
        if(queryStatement.indexOf("#{")>0 && queryStatement.indexOf("}")>0){
            queryStatement = QueryPortMainUtil.dealStatementParameters(queryStatement, queryportMain, "query");
        }
        queryStatementBuffer.delete(0, queryStatementBuffer.length());
        queryStatementBuffer.append(queryStatement);
        return queryParamList;
    }

    /**
     * 解析查询参数
     * @param queryportMain
     * @param queryStatement
     * @param queryParamList
     * @return
     */
    private static String resolveQueryPortParameter(QueryportMain queryportMain, String queryStatement, List<Object> queryParamList) {
        List<String> parserParamList = QueryPortMainUtil.parserParam(queryStatement, PARAM_REGX, true);
        String parameterSign = "?";
        String[] paramSignValues = {};
        for (String parameter : parserParamList) {
            String parameterInputMode = queryportMain.getQueryportParamsMap().get(parameter).getInputMode();
            if (MULTI_PARAM_INPUT_MODE_LIST.contains(parameterInputMode)) {
                parameterSign = "";
                paramSignValues = queryportMain.getParams().get(parameter).toString().split(",");
                for (String parameterValue : paramSignValues) {
                    parameterSign += " ? ,";
                    queryParamList.add(convertQueryPortParameterType(parameterValue, queryportMain.getQueryportParamsMap().get(parameter).getParamType()));
                }
                parameterSign = parameterSign.endsWith(",") ? parameterSign.substring(0, parameterSign.length() - 1) : parameterSign;
            } else {
                queryParamList.add(convertQueryPortParameterType((String) queryportMain.getParams().get(parameter), queryportMain.getQueryportParamsMap().get(parameter).getParamType()));
            }
            queryStatement = queryStatement.replaceFirst("'#\\{" + parameter + "\\}'", parameterSign);
        }
        return queryStatement;
    }

    /**
     * 转换输入参数类型
     * @param value
     * @param type
     * @return
     */
    private static Object convertQueryPortParameterType(String value, String type){
        log.info(value);
        if(StringUtils.isNotEmpty(value)) {
            if ("integer".equals(type)) {
                return Integer.parseInt(value);
            }else if ("double".equals(type)) {
                return Double.parseDouble(value);
            }else if ("date".equals(type)) {
                return DateUtils.dateTime(DATE_FORMATE_STR, value);
            }else if ("time".equals(type)) {
                return DateUtils.dateTime(TIME_FORMATE_STR, value);
            }
        }
        return value;
    }


    /**
     * 重新组织语句，解析方式
     * @param queryportMain
     * @return
     */
    public static String reDealStatementParser(QueryportMain queryportMain, String operateType) {
        StringBuffer selectSQl = new StringBuffer();
        //获取where后面的参数
        String expressionwhere = dealStatementParameters(queryportMain.getExpressionWhere(), queryportMain, operateType);
        //取出参数
        String [] conditions = expressionwhere.split("AND");
        String newConditionSQl = "";
        //参数为空处理
        if(expressionwhere.indexOf(EXPRESSION_MOVE_SIGN) > 0){
            for(String condition : conditions){
                if(condition.indexOf(EXPRESSION_MOVE_SIGN)==-1){
                    newConditionSQl += condition + CONDITON_CONNECT_SIGN;
                }
            }
        }else{
            //没有参数为空的情况
            newConditionSQl = expressionwhere;
        }

        //替换子查询中可能包含的 as
        newConditionSQl = newConditionSQl.replaceAll(EXPRESSION_ALIAS_SIGN, " ");
        //如果语句已and结尾，则去掉最后的 And
        newConditionSQl = newConditionSQl.endsWith(CONDITON_CONNECT_SIGN)?newConditionSQl.substring(0,newConditionSQl.length()-5):newConditionSQl;
        //拼接查询参数
        selectSQl.append("select ");
        //拼接查询字段
        selectSQl.append(reDealExpressionColumn(queryportMain, operateType) + " \n");

        selectSQl.append("from ");
        if(StringUtils.isNotBlank(queryportMain.getExpressionFrom())){
            selectSQl.append(queryportMain.getExpressionFrom().replaceAll(EXPRESSION_ALIAS_SIGN, " ")  + " \n");
        }
        if(StringUtils.isNotBlank(newConditionSQl)){
            selectSQl.append("where ");
            selectSQl.append(newConditionSQl + " \n");
        }
        //是否分组
        if(StringUtils.isNotBlank(queryportMain.getExpressionGroupby())){
            selectSQl.append("group by ");
            selectSQl.append(queryportMain.getExpressionGroupby()  + " \n");
        }
        if(StringUtils.isNotBlank(queryportMain.getExpressionHaving())){
            selectSQl.append("having ");
            selectSQl.append(queryportMain.getExpressionHaving()  + " \n");
        }
        //是否排序
        if(StringUtils.isNotBlank(queryportMain.getExpressionOrderby())){
            selectSQl.append("order by ");
            selectSQl.append(queryportMain.getExpressionOrderby()  + " ");
        }
        //如果语句中仍然存在变量，则再次进行处理
        String queryStatement = selectSQl.toString();
        if(queryStatement.indexOf("#{")>0 && queryStatement.indexOf("}")>0){
            queryStatement = QueryPortMainUtil.dealStatementParameters(queryStatement, queryportMain, operateType);
        }
        return queryStatement;
    }



    /**
     *
     * @param expression
     * @param queryportMain
     * @return
     */
    public static String dealStatementParameters(String expression, QueryportMain queryportMain, String operateType) {
        //遍历参数列表
        for(QueryportParam param : queryportMain.getQueryportParams()){
            //进行参数初始化操作
            //PnrQueryPortMainUtil.setTypeParamValue(this.getUser(request), param);
            //request.setAttribute(param.getEname(), StaticMethod.nullObject2String(request.getParameter(param.getEname())));
            //param.setParamvalue(StaticMethod.nullObject2String(request.getParameter(param.getEname())));
            param.setParamValue((String) queryportMain.getParams().get(param.getEname()));
            if(StringUtils.isBlank(param.getParamValue())){
                param.setParamValue(setQueryportParamDefaultValue(param));
            }
            //需要进行类型判断
            //参数类型,时间格式区分
            String paramInputMode=param.getInputMode();
            if(StringUtils.isNotEmpty(param.getParamValue())){
                if("queryBinding".equals(operateType)){
                    //expression = expression.replaceAll("#\\{"+param.getEname()+"\\}", ""+param.getParamvalue()+"");
                }else{
                    expression = expression.replaceAll("#\\{"+param.getEname()+"\\}", ""+param.getParamValue()+"");

                }
            }else{
                expression = expression.replaceAll("#\\{"+param.getEname()+"\\}", EXPRESSION_MOVE_SIGN);
            }
        }
        return expression;
    }

    /**
     * 根据操作类型 组织 查询结果列内容
     * 例如：导出操作时去掉Exportflag为false的列
     * @param queryportMain
     * @param operateType
     * @return
     */
    private static String reDealExpressionColumn(QueryportMain queryportMain, String operateType) {
        //导出操作去掉页面不显示的列
        if("export".equals(operateType)){
            return dealStatementParameters(queryportMain.getExpressionExportColumn(), queryportMain, operateType);
        }else{
            return dealStatementParameters(queryportMain.getExpressionColumn(), queryportMain, operateType);
        }
    }

    public static void exportExcel(List<?> list, String sheetName, List<QueryportColumn> headers,OutputStream out)
    {
        Workbook wb = new SXSSFWorkbook(500);
        try
        {
            // 取出一共有多少个sheet.
            double sheetNo = Math.ceil(list.size() / 65536);
            for (int index = 0; index <= sheetNo; index++)
            {
                Sheet sheet = wb.createSheet();
                styles = createStyles(wb);
                // 设置工作表的名称.
                if (sheetNo == 0)
                {
                    wb.setSheetName(index, sheetName);
                }
                else
                {
                    wb.setSheetName(index, sheetName + index);
                }
                // 产生一行
                Row row = sheet.createRow(0);
                int column = 0;
                // 写入各个字段的列头名称
                for (int i = 0 ; i < headers.size(); i++)
                {
                    QueryportColumn qc = headers.get(i);
                    if("Y".equals(qc.getExportflag())){
                        //设置列宽
                        sheet.setColumnWidth(i, (int)(qc.getCname().getBytes().length * 1.2d * 256 > 12 * 256 ? qc.getCname().getBytes().length * 1.2d * 256 : 12 * 256));
                        createCell(qc.getCname(), row, column++);
                    }
                }
                 fillExcelData(list, headers, index,  sheet,  row);
            }
            wb.write(out);
        }
        catch (Exception e)
        {
            log.error("导出Excel异常{}", e.getMessage());
            throw new BusinessException("导出Excel失败，请联系网站管理员！");
        }
        finally
        {
            if (wb != null)
            {
                try
                {
                    wb.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
            if (out != null)
            {
                try
                {
                    out.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        }
    }

     /**
     * 创建单元格
     */
    public static Cell createCell(String name, Row row, int column)
    {
        // 创建列
        Cell cell = row.createCell(column);
        // 写入列信息
        cell.setCellValue(name);
        cell.setCellStyle(styles.get("header"));
        return cell;
    }

    /**
     * 创建表格样式
     *
     * @param wb 工作薄对象
     * @return 样式列表
     */
    private static Map<String, CellStyle> createStyles(Workbook wb)
    {
        // 写入各条记录,每条记录对应excel表中的一行
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        Font dataFont = wb.createFont();
        dataFont.setFontName("Arial");
        dataFont.setFontHeightInPoints((short) 10);
        style.setFont(dataFont);
        styles.put("data", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = wb.createFont();
        headerFont.setFontName("Arial");
        headerFont.setFontHeightInPoints((short) 10);
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(headerFont);
        styles.put("header", style);

        return styles;
    }

    public static void fillExcelData(List<?> list, List<QueryportColumn> queryportColumns, int index, Sheet sheet, Row row)
    {
        DictService dt = SpringUtils.getBean("dict");
        Id2NameUtil id2NameUtil = SpringUtils.getBean("id2NameUtil");
        int startNo = index * 65536;
        int endNo = Math.min(startNo + 65536, list.size());
        for (int i = startNo; i < endNo; i++)
        {
            row = sheet.createRow(i + 1 - startNo);
            // 得到导出对象.
            Map<String,Object> vo = (Map<String,Object>) list.get(i);
            int column = 0;
            for (QueryportColumn qc : queryportColumns)
            {
                //必须有导出标志才能导出
                if("Y".equals(qc.getExportflag())){
                    Object value = vo.get(qc.getEname());
                    if(value != null) {
                        //字典类型特殊处理
                        if ("dict".equals(qc.getConvertType())) {
                            value = dt.getLabel(qc.getTypeParam1(), value.toString());
                        } else if ("dictCode".equals(qc.getConvertType())) {
                            value = id2NameUtil.getNameByIds("com.gsh.system.domain.SysDictData", value.toString());
                        } else if ("user".equals(qc.getConvertType())) {
                            value = id2NameUtil.getNameByIds("com.gsh.system.domain.SysUser", value.toString());
                        } else if ("dept".equals(qc.getConvertType())) {
                            value = id2NameUtil.getNameByIds("com.gsh.system.domain.SysDept", value.toString());
                        } else if ("role".equals(qc.getConvertType())) {
                            value = id2NameUtil.getNameByIds("com.gsh.system.domain.SysRole", value.toString());
                        } else if ("date".equals(qc.getConvertType())) {
                            if(value instanceof Date){
                                value = DateUtils.parseDateToStr(DATE_FORMATE_STR, (Date) value);
                            }
                        } else if ("time".equals(qc.getConvertType())) {
                            if(value instanceof Date) {
                                value = DateUtils.parseDateToStr(TIME_FORMATE_STR, (Date) value);
                            }
                        }
                        addCell(row, value, column++);
                    }
                }

            }
        }
    }
    /**
     * 添加单元格
     */
    public static Cell addCell(Row row, Object value, int column)
    {
        String DEFAULT_DATE_PATTERN="yyyy年MM月dd日 HH时MM分SS秒";
        Cell cell = null;
        try {
            // 创建cell
            cell = row.createCell(column);
            cell.setCellStyle(styles.get("data"));
            String cellValue = "";
            if(value==null) {
              cellValue = "";
            }else if(value instanceof Date) {
                cellValue = new SimpleDateFormat(DEFAULT_DATE_PATTERN).format(value);
            }else {
                cellValue = value.toString();
            }
            cell.setCellValue(cellValue);
        }
        catch (Exception e)
        {
            log.error("导出Excel失败{}", e);
        }
        return cell;
    }

    /**
     * 初始化参数的默认值
     * @param queryportMain
     */
    public static void initParamDefaultValues(QueryportMain queryportMain) {
        queryportMain.getQueryportParams().forEach(queryportParam -> {
            queryportParam.setDefaultValue(setQueryportParamDefaultValue(queryportParam));
        });
    }

    private static String setQueryportParamDefaultValue(QueryportParam queryportParam) {
        String defaultValue = "";
        if("current-user-id".equals(queryportParam.getDefaultValue())){
            defaultValue = String.valueOf(ShiroUtils.getUserId());
        } else if("current-user-dept-id".equals(queryportParam.getDefaultValue())){
            defaultValue = String.valueOf(ShiroUtils.getSysUser().getDeptId());
        } else if("current-user-parent-dept-id".equals(queryportParam.getDefaultValue())){
            defaultValue = String.valueOf(SpringUtils.getBean(ISysDeptService.class).selectDeptById(ShiroUtils.getSysUser().getDeptId()).getParentId());
        } else if("current-user-login-name".equals(queryportParam.getDefaultValue())){
            defaultValue = String.valueOf(ShiroUtils.getSysUser().getLoginName());
        } else if("current-date".equals(queryportParam.getDefaultValue())){
            defaultValue = DateUtils.parseDateToStr(DATE_FORMATE_STR, DateUtils.getNowDate());
        } else if("current-time".equals(queryportParam.getDefaultValue())){
            defaultValue = DateUtils.parseDateToStr(TIME_FORMATE_STR, DateUtils.getNowDate());
        } else if("yesterday-date".equals(queryportParam.getDefaultValue())){
            defaultValue = DateUtils.parseDateToStr(DATE_FORMATE_STR, DateUtils.addDays(DateUtils.getNowDate(), -1));
        } else if("current-month-first-date".equals(queryportParam.getDefaultValue())){
            defaultValue = DateUtils.parseDateToStr("yyyy-MM", DateUtils.addDays(DateUtils.getNowDate(), 0)) + "-01";
        } else if("previous-month-first-date".equals(queryportParam.getDefaultValue())){
            defaultValue = DateUtils.parseDateToStr("yyyy-MM", DateUtils.addDays(DateUtils.getNowDate(), -30)) + "-01";
        } else if("next-month-first-date".equals(queryportParam.getDefaultValue())){
            defaultValue = DateUtils.parseDateToStr("yyyy-MM", DateUtils.addDays(DateUtils.getNowDate(), 30)) + "-01";
        }
        return defaultValue;
    }
}
