package com.data.database;

import java.util.ArrayList;
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
	 *         column @param @return 设定文件 @return boolean 返回类型 @throws
	 */
	public boolean addColumns(Column column) {
		// 列字段格式自检未通过，说明格式有问题，因此返回添加失败
		if (column == null || !column.check()) {
			return false;
		}
		if (columns == null) {
			columns = new ArrayList<Table.Column>();
		}
		// 不允许添加列名相同的字段(不区分大小写)
		for (Column columnTemp : columns) {
			if (columnTemp.getName().equalsIgnoreCase(column.getName())) {
				return false;
			}
		}
		return columns.add(column);
	}

	/**
	 * @Title: addPrimaryKey @Description: 向{@link
	 * Table}中增加主键，增加主键时必须要保证该对象中以存在相应的列 @param @param primaryKey @param @return
	 * 设定文件 @return boolean 返回类型 @throws
	 */
	public boolean addPrimaryKey(String primaryKey) {
		if (columns == null || columns.size() == 0) {
			return false;
		}
		for (Column column : columns) {
			if (column == null || column.getName() == null) {
				continue;
			}
			if (column.getName().equalsIgnoreCase(primaryKey)) {
				if (primaryKeys == null) {
					primaryKeys = new ArrayList<String>();
				}
				if (primaryKeys.contains(primaryKey)) {
					return false;
				}
				// 如果为主键,则当前列不能为空
				column.setNull(false);
				return primaryKeys.add(primaryKey);
			}
		}
		return false;
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

		public boolean check() {
			// TODO Auto-generated method stub
			return false;
		}

		/**
		 * @Title: createVarcharColumn @Description:
		 *         创建一个varchar类型的列 @param @param name @param @param
		 *         length @param @param isNull @param @param
		 *         defauleValue @param @return 设定文件 @return Column 返回类型 @throws
		 */
		public static Column createVarcharColumn(String name, int length, boolean isNull, String defauleValue) {
			Column column = new Column();
			column.name = name;
			column.length = length;
			column.isNull = isNull;
			column.columnType = ColumnType.VARCHAR;
			column.defauleValue = defauleValue != null && defauleValue.length() > 0 ? "'" + defauleValue + "'" : null;
			column.isAutoIncrement = false;
			return column;
		}

		public static Column createIntColumn(String name, boolean isNull, Integer defauleValue) {
			Column column = new Column();
			column.name = name;
			column.length = Column.INT_DEFAULT_LENGTH;
			column.isNull = isNull;
			column.columnType = ColumnType.INT;
			column.defauleValue = defauleValue != null ? String.valueOf(defauleValue) : null;
			column.isAutoIncrement = false;
			return column;
		}

		/**
		 * 创建一个具有自增特性的int类型的列,列长度默认是10,默认不能为空<br>
		 * 请注意，创建了此类型字段的表格，此字段默认会成为主键中的一个
		 * 
		 * @param name
		 *            列名称
		 * @return
		 */
		public static Column createAutoIncrementIntColumn(String name) {
			Column column = new Column();
			column.name = name;
			column.length = Column.INT_DEFAULT_LENGTH;
			column.isNull = true;
			column.columnType = ColumnType.INT;
			column.defauleValue = null;
			column.isAutoIncrement = true;
			return column;
		}
		
		/**
		 * 创建一个text类型的列字段
		 * 
		 * @param name
		 *            字段名称
		 * @param isNull
		 *            是否为空
		 * @return
		 */
		public static Column createTextColumn(String name,boolean isNull){
			Column column=new Column();
			column.name = name;
			column.isNull = isNull;
			column.columnType = ColumnType.TEXT;
			column.defauleValue = null;
			column.length = null;
			column.isAutoIncrement = false;
			return column;
		}
		
		/**
		 * 创建一个日期datime类型的列字段，当列字段有默认值时，会自动选用timestamp,无需调用者担心
		 * 
		 * @param name
		 *            列名称
		 * @param isNull
		 *            是否为空
		 * @param defauleValue
		 *            默认内容
		 * @return
		 */
		public static Column  createDateTimeColumn(String name, boolean isNull, String defauleValue) {
			Column column=new Column();
			column.name = name;
			column.isNull = isNull;
			if (defauleValue != null && defauleValue.length() > 0) {
				column.columnType = ColumnType.TIMESTAMP;
				column.isNull = true;
			} else {
				column.columnType = ColumnType.DATETIME;
			}
			column.defauleValue = defauleValue != null && defauleValue.length() > 0 ? defauleValue : null;
			column.length = null;
			column.isAutoIncrement = false;
			return column;
		}
		/**
		 * 创建一个Long类型的列，根据MYSQL特性，会自动启用BIGINT，默认长度63位
		 * 
		 * @param name
		 *            列字段名称
		 * @param isNull
		 *            是否为空
		 * @param defauleValue
		 *            默认值
		 * @return
		 */
		public static Column createLongColumn(String name, boolean isNull, Long defauleValue) {
			Column column = new Column();
			column.name = name;
			column.length = Column.LONG_DEFAULT_LENGTH;
			column.isNull = isNull;
			column.columnType = ColumnType.LONG;
			column.defauleValue = defauleValue != null ? String.valueOf(defauleValue) : null;
			column.isAutoIncrement = false;
			return column;
		}

		/**
		 * 创建一个double类型的列字段
		 * 
		 * @param name
		 *            列名称
		 * @param isNull
		 *            是否为空
		 * @param defauleValue
		 *            默认值
		 * @return
		 */
		public static Column createDoubleColumn(String name, boolean isNull, Double defauleValue) {
			Column column = new Column();
			column.name = name;
			column.length = null;
			column.isNull = isNull;
			column.columnType = ColumnType.DOUBLE;
			column.defauleValue = defauleValue != null ? String.valueOf(defauleValue) : null;
			column.isAutoIncrement = false;
			return column;
		}


		/**
		 * 以SQL语句的方式展示一个表结构
		 */
		public String toString(){
			StringBuilder stringBuilder=new StringBuilder();
			// Step 1.拼接字段名及类型
			stringBuilder.append("`").append(this.getName()).append("` ").append(this.getColumnType().getValue());

			// Step 2.拼接类型长度
			if (this.getLength() != null) {
				stringBuilder.append("(").append(this.getLength()).append(")");
			}

			// Step 3.拼接是否为空
			if (!this.isNull()) {
				stringBuilder.append(" not null");
			}

			// Step 4.拼接默认值
			if (this.defauleValue != null && this.defauleValue.length() > 0) {
				stringBuilder.append((" default " + this.defauleValue));
			}

			// Step 5.拼接是否自增
			if (this.isAutoIncrement) {
				stringBuilder.append(" auto_increment");
			}
			return stringBuilder.toString();
		}
		
		
		/**
		 * 覆盖此方法的目的在于进行数据库两张表中的列格式对比
		 */
		@Override
		public boolean equals(Object obj){
			if(obj==null){
				return false;
			}
			if(this==obj){
				return true;
			}
			if(this.getClass()!=obj.getClass()){
				return false;
			}
			if(!(obj instanceof Column)){
				return false;
			}
			Column column=(Column)obj;
			if (this.name != null) {
				if (!this.name.equalsIgnoreCase(column.name)) {
					return false;
				}
			} else if (column.name != null) {
				return false;
			}

			if (this.columnType != column.columnType) {
				return false;
			}

			if (this.length != null) {
				if (!this.length.equals(column.length)) {
					return false;
				}
			}

			if (this.isNull != column.isNull) {
				return false;
			}

			if (this.defauleValue != null) {
				if (!this.defauleValue.equalsIgnoreCase(column.defauleValue)) {
					return false;
				}
			} else if (column.defauleValue != null) {
				return false;
			}

			if (this.isAutoIncrement != column.isAutoIncrement) {
				return false;
			}

			return true;
		}
		/**
		 * 因为{@link Object#equals()}方法被覆盖了，为了防止{@link Column}
		 * 被放置在Map中找不到对象，此处也覆盖了{@link Object#hashCode()}方法
		 */
		@Override
		public int hashCode() {
			int result = 17;
			result = 31 * result + (name != null ? name.hashCode() : 0);
			result = 31 * result + (length != null ? length.hashCode() : 0);
			result = 31 * result + (isNull ? 1 : 0);
			result = 31 * result + (columnType != null ? columnType.getValue().hashCode() : 0);
			result = 31 * result + (defauleValue != null ? defauleValue.hashCode() : 0);
			result = 31 * result + (isAutoIncrement ? 1 : 0);
			return result;
		}
		public String getName() {
			return name;
		}

		public ColumnType getColumnType() {
			return columnType;
		}

		public Integer getLength() {
			return length;
		}

		public boolean isNull() {
			return isNull;
		}
		public String getDefauleValue() {
			return defauleValue;
		}

		public boolean isAutoIncrement() {
			return isAutoIncrement;
		}

		/**
		 * 为防止Column被其他类破坏规则，此处仅允许Table对他进行修改
		 * 
		 * @param isNull
		 */
		private void setNull(boolean isNull) {
			this.isNull = isNull;
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
