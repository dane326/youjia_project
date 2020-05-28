package com.gsh.system.queryport.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectGroupByClause;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.dialect.oracle.parser.OracleStatementParser;

/**
 * 
 * @author yf
 *
 */
public class QueryPortDruidParser {
	
	/**
	 * 获取没有as别名内容的查询结果list
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public static List<String> parserColumn(String sql) throws Exception {

		List<String> columnList = new ArrayList<String>();
		//格式化
		//String formatSql = SQLUtils.format(sql, JdbcUtils.ORACLE_DRIVER);
		//使用Oracle解析  
		OracleStatementParser sqlStatementParser = new OracleStatementParser(sql) ;  
        //解析select查询  
        SQLSelectStatement sqlStatement = (SQLSelectStatement) sqlStatementParser.parseSelect() ;  
        SQLSelect sqlSelect = sqlStatement.getSelect() ;  
        //获取sql查询块  
        SQLSelectQueryBlock sqlSelectQuery = (SQLSelectQueryBlock)sqlSelect.getQuery() ;  
  
        StringBuffer out = new StringBuffer() ;  
        //创建sql解析的标准化输出  
        //SQLASTOutputVisitor sqlastOutputVisitor = SQLUtils.createFormatOutputVisitor(out , null , JdbcUtils.ORACLE_DRIVER) ;  
  
        //解析select项  
        out.delete(0, out.length()) ;  
        for (SQLSelectItem sqlSelectItem : sqlSelectQuery.getSelectList()) {  
            //sqlSelectItem.accept(sqlastOutputVisitor);  
            if(sqlSelectItem.getAlias()!=null){
            	columnList.add(sqlSelectItem.getAlias());
            }else{
            	columnList.add(QueryPortMainUtil.getColumnAliasName(sqlSelectItem.toString()));
            }
        }  
		return columnList;
	}
	
	/**
	 * 获取带as别名内容的查询结果list
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public static List<String> parserAsColumn(String sql) throws Exception {
		//格式化
		//String formatSql = SQLUtils.format(sql, JdbcUtils.ORACLE_DRIVER);
		//使用mysql解析  
		OracleStatementParser sqlStatementParser = new OracleStatementParser(sql) ;  
        //解析select查询  
        SQLSelectStatement sqlStatement = (SQLSelectStatement) sqlStatementParser.parseSelect() ;  
        SQLSelect sqlSelect = sqlStatement.getSelect() ;  
        //获取sql查询块  
        SQLSelectQueryBlock sqlSelectQuery = (SQLSelectQueryBlock)sqlSelect.getQuery() ;  
  
        StringBuffer out = new StringBuffer() ;  
        //创建sql解析的标准化输出  
        //SQLASTOutputVisitor sqlastOutputVisitor = SQLUtils.createFormatOutputVisitor(out , null , JdbcUtils.ORACLE_DRIVER) ;  
  
		return getExpressionColumnList(sqlSelectQuery.getSelectList(),out);
	}
	
	/**
	 * 解析查询语句
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public static Map<String,String> parserExpression(String sql) {

		Map<String,String> expressionMap = new HashMap<String,String>(16);
		//格式化
		//String formatSql = SQLUtils.format(sql, JdbcUtils.ORACLE_DRIVER);
		//使用mysql解析  
        OracleStatementParser sqlStatementParser = new OracleStatementParser(sql) ;  
        //解析select查询  
        SQLSelectStatement sqlStatement = (SQLSelectStatement) sqlStatementParser.parseSelect() ;  
        SQLSelect sqlSelect = sqlStatement.getSelect() ;  
        //获取sql查询块  
        SQLSelectQueryBlock sqlSelectQuery = (SQLSelectQueryBlock)sqlSelect.getQuery() ; 
        
        StringBuffer out = new StringBuffer() ;  
        //创建sql解析的标准化输出  
        //SQLASTOutputVisitor sqlastOutputVisitor = SQLUtils.createFormatOutputVisitor(out , null , JdbcUtils.ORACLE_DRIVER) ;  
        
        //解析select项  
        expressionMap.put("expressioncolumn", QueryPortMainUtil.getExpressionString(getExpressionColumnList(sqlSelectQuery.getSelectList(),out)));
		//解析from  
        expressionMap.put("expressionfrom", getExpressionFrom(sqlSelectQuery.getFrom(),out));
		//解析where  
        expressionMap.put("expressionwhere", getExpressionWhere(sqlSelectQuery.getWhere(),out));
		//解析GroupBy 
        expressionMap.put("expressiogroupby", getExpressionGroupBy(sqlSelectQuery.getGroupBy(),out));
		//解析having
        expressionMap.put("expressiohaving", getExpressionHaving(sqlSelectQuery.getGroupBy(),out));
		//解析OrderBy 
        expressionMap.put("expressioorderby", getExpressionOrderBy(sqlSelectQuery.getOrderBy(),out));
        
		return expressionMap;
	}

	/**
	 * 解析查询结果列
	 * @param selectList
	 * @param out
	 * @return
	 */
	private static List<String> getExpressionColumnList(List<SQLSelectItem> selectList, StringBuffer out) {
		List<String> columnList = new ArrayList<String>();
		//解析select项  
        //out.delete(0, out.length()) ;  
        for (SQLSelectItem sqlSelectItem : selectList) {  
            //sqlSelectItem.accept(sqlastOutputVisitor);  
            //去掉多余空格避免后续处理语句异常
			String temp = sqlSelectItem.toString();
			temp = temp.replaceAll(", ", ",");
			temp = temp.replaceAll(", ", ",");
            if(sqlSelectItem.getAlias()!=null){
            	columnList.add(QueryPortMainUtil.removeUselessAlias(temp));
            }else{
            	columnList.add(temp + QueryPortMainUtil.EXPRESSION_ALIAS_SIGN + QueryPortMainUtil.getColumnAliasName(temp));
            }
        }  
		return columnList;
	}

	/**
	 * 解析from
	 * @param from
	 * @param out
	 * @return
	 */
	private static String getExpressionFrom(SQLTableSource from, StringBuffer out) {
		if(from != null){
			out.delete(0, out.length()) ;  
			//from.accept(sqlastOutputVisitor);
			return from.toString();
		}
		return "";
	}

	/**
	 * 解析order by
	 * @param orderBy
	 * @param out
	 * @return
	 */
	private static String getExpressionOrderBy(SQLOrderBy orderBy, StringBuffer out) {
		if(orderBy != null){
			out.delete(0, out.length()) ;
			//out.delete(0, out.length()) ;  
			//orderBy.accept(sqlastOutputVisitor);
			for(SQLSelectOrderByItem item : orderBy.getItems()){
				out.append(item.getExpr().toString());
				if(item.getType() != null){
					out.append(" ").append(item.getType());
				}
				out.append(",");
			}
			return out.toString().substring(0,out.length()-1);
		}
		return "";
	}

	/**
	 * 解析having
	 * @param groupBy
	 * @param out
	 * @return
	 */
	private static String getExpressionHaving(SQLSelectGroupByClause groupBy, StringBuffer out) {
		if(groupBy != null){
			out.delete(0, out.length()) ;
			if(groupBy.getHaving()!=null){
				return groupBy.getHaving().toString();
			}
		}
		return "";
	}

	/**
	 * 解析group by
	 * @param groupBy
	 * @param out
	 * @return
	 */
	private static String getExpressionGroupBy(SQLSelectGroupByClause groupBy, StringBuffer out) {
		if(groupBy != null){
			out.delete(0, out.length()) ;  
			//groupBy.accept(sqlastOutputVisitor);
			for(SQLExpr item : groupBy.getItems()){
				out.append(item.toString()).append(",");
			}
			return out.toString().substring(0,out.length()-1);
		}
		return "";
	}

	/**
	 * 解析where条件，默认为1=1
	 * @param where
	 * @param out
	 * @return
	 */
	private static String getExpressionWhere(SQLExpr where, StringBuffer out) {
		if(where != null){
			//out.delete(0, out.length()) ;  
			//where.accept(sqlastOutputVisitor);
			return where.toString();
		}
		return "1=1";
	}
	
}
