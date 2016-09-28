package com.data.database;

import java.util.List;

/**
 * @ClassName: Table
 * @Description: * <b>描述: </b>此类为数据库"表结构-->JAVA对象的映射"，通过JAVA对象形式表现数据库的表结构<br>
 *               暂时支持<code>varchar、text、int、long、double、datetime、timestamp</code>类型，
 *               ,以及设置主键、自增字段、默认值等功能，创建对象后调用<code>toString()</code>方法可将对象转换为SQL语句
 *               此类为了保证数据库表结构的约束性，不对外提供对象成员的修改方法。
 *               <p>
 *               <b>功能: </b>提供 数据库"表结构-->JAVA对象的映射"
 *               <p>
 *               <b>用法: </b>此类与{@link MonDBHelper}配合使用效果最佳
 * @author wangtianyu
 * @date 2016年9月27日 下午4:03:58
 * 
 */
public class Table {
	private String tableName;
	private List<Column> columns = null;
	private List<String> primaryKeys = null;
	private String extension = null;

	public Table(String tableName) {
		this.tableName = tableName;
	}

	public String getTableName() {
		return tableName;
	}

	/**
	 * 此处用于设置扩展的建表语句，该语句会包含在{@link Table#toString()} 方法生成的建表脚本中，<br>
	 * 可以用它加入例如索引等扩展语句
	 * 
	 * @param extendSQL
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}

	/**
	 * @Title: addColumns @Description: 为表添加列字段 @param @param
	 * column @param @return 设定文件 @return boolean 返回类型 @throws
	 */
	public boolean addColumns(Column column) {
		return columns.add(column);
	}

	/**
	 * 数据库中的列格式类型
	 */
	public static class Column {
		private String name;
		private ColumnType columnType;
		private Integer length;
		private boolean isNull;
		private boolean isAutoIncrement;
		private String defauleValue;
		private static final int INT_DEFAULT_LENGTH = 11;
		private static final int LONG_DEFAULT_LENGTH = 63;

		/**
		 * 为提高安全性,保证每一列的类型与格式相对应，符合数据库的规范要求，因此屏蔽了默认的构造方法
		 */
		private Column() {

		}
		/** 
		* @Title: createVarcharColumn 
		* @Description:  创建一个varchar类型的列
		* @param @param name
		* @param @param length
		* @param @param isNull
		* @param @param defauleValue
		* @param @return    设定文件 
		* @return Column    返回类型 
		* @throws 
		*/
		public static Column createVarcharColumn(String name, int length, boolean isNull, String defauleValue){
			Column column=new Column();
			column.name=name;
			column.length=length;
			column.isNull=isNull;
			column.columnType=ColumnType.VARCHAR;
			column.defauleValue= defauleValue != null && defauleValue.length() > 0 ? "'" + defauleValue + "'" : null;
			column.isAutoIncrement = false;
			return column;	
		}
		
		public static Column createIntColumn(String name, boolean isNull, Integer defauleValue){
			Column column=new Column();
			column.name=name;
			column.length=Column.INT_DEFAULT_LENGTH;
			column.isNull=isNull;
			column.columnType=ColumnType.INT;
			column.defauleValue = defauleValue != null ? String.valueOf(defauleValue) : null;
			column.isAutoIncrement = false;
			return column;		
		}

	}

	enum ColumnType {
		INT("int"), LONG("bigint"), DOUBLE("double"), DATETIME("datetime"), TIMESTAMP("timestamp"), VARCHAR(
				"varchar"), TEXT("text");
		private String value;

		private ColumnType(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}
}
