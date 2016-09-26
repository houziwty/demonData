package com.data.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @ClassName: DataRow
 * @Description: * <b>描述：</b>数据行
 *               <p>
 *               <b>功能：</b>数据行的使用类
 *               <p>
 *               <b>用法：</b>
 *               <p>
 *               根据字段序号（从1开是的序号）或者字段名访问行中的字段<br/>
 *               提供int，boolean等常规数据类型的访问，<br/>
 *               对于非常规数据类型请自己调用获取getObject()方法后拆包<br/>
 * @author wangtianyu
 * @date 2016年9月22日 下午3:28:06
 * 
 */
public class DataRow {
	private static final Logger LOGGER = LoggerFactory.getLogger(DataRow.class);
	private int length;
	private DataTable dataTable;
	private Object[] values;

	public DataRow(DataTable dataTabel, ResultSet rs) {
		this.dataTable = dataTable;
		length = dataTable.getColumnCount();
		values = new Object[length];

		// 读取所有值
		try {
			int internalIndex;
			for (int i = 1; i <= values.length; i++) {
				internalIndex = i - 1;
				values[internalIndex] = rs.getObject(i);
			}
		} catch (SQLException e) {
			LOGGER.error(String.format("初始化DataRow出错：DataTable:%s", dataTable.getName()), e);
		}
	}
	/**
	 * 判定指定列是否为SQL NULL
	 * 
	 * @param columnIndex
	 *            列序号
	 * @return boolean
	 */
	public boolean isNull(int columnIndex)throws SQLException{
		checkColumnIndexArg(columnIndex);
		return values[columnIndex]==null;
	}
}
