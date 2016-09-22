package com.data.database;


/**
 * @ClassName: DataColumn
 * @Description: * <b>描述：</b>数据库column的类
 *               <p>
 *               <b>功能：</b>数据库DataCoumn的访问类
 *               <p>
 *               <b>用法：</b>
 * 
 *               <pre>
 * 				<code> DataColumn dc = table.getColumn(1);
 *               从1开始，参考：{@link DataTable}
 *               System.out.println(dc.getColumnName()); DataColumn dc2 =
 *               table.getColumn("UserId");
 *               System.out.println(dc2.getColumnName());
 * @author wangtianyu
 * @date 2016年9月18日 下午3:32:23
 * 
 */
public class DataColumn {
	private DataTable dataTable;
	private int sqlType;
	private String name;

	/**
	 * 构造函数
	 */
	public DataColumn() {
	}

	/**
	 * 构造函数
	 * 
	 * @param dataTable
	 */
	public DataColumn(DataTable dataTable) {
		this.dataTable = dataTable;
	}

	/**
	 * 构造函数
	 * 
	 * @param dataTable
	 *            所属数据表对象
	 * @param name
	 *            列名
	 * @param type
	 *            列SQL type
	 */
	public DataColumn(DataTable dataTable, String name, int type) {
		this.dataTable = dataTable;
		this.name = name;
		this.sqlType = type;
	}

	public DataTable getDataTable() {
		return dataTable;
	}

	public void setDataTable(DataTable dataTable) {
		this.dataTable = dataTable;
	}

	public int getSqlType() {
		return sqlType;
	}

	public void setSqlType(int sqlType) {
		this.sqlType = sqlType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
