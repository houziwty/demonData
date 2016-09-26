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
	private Object[]values;
	public DataRow(DataTable dataTabel,ResultSet rs){
		this.dataTable=dataTable;
		length=dataTable.getColumnCount();
		values=new Object[length];
	}
}
