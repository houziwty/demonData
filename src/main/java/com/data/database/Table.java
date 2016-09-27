package com.data.database;

import java.util.List;


/** 
* @ClassName: Table 
* @Description:  * <b>描述: </b>此类为数据库"表结构-->JAVA对象的映射"，通过JAVA对象形式表现数据库的表结构<br>
 * 暂时支持<code>varchar、text、int、long、double、datetime、timestamp</code>类型，
 * ,以及设置主键、自增字段、默认值等功能，创建对象后调用<code>toString()</code>方法可将对象转换为SQL语句
 * 此类为了保证数据库表结构的约束性，不对外提供对象成员的修改方法。
 * <p>
 * <b>功能: </b>提供 数据库"表结构-->JAVA对象的映射"
 * <p>
 * <b>用法: </b>此类与{@link MonDBHelper}配合使用效果最佳 
* @author wangtianyu 
* @date 2016年9月27日 下午4:03:58 
*  
*/
public class Table {
	private String tableName;
	private List<Column> columns = null;

	/**
	 * 数据库中的列格式类型
	 * 
	 * @author Lv.Mingwei
	 * 
	 */
	public static class Column {
	}
}
