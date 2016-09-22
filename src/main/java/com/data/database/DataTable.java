package com.data.database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/** 
* @ClassName: DataTable 
* @Description:  * <b>描述：</b> 数据库表<br/>
 * <p>
 * <b>功能：</b> 提供对表内的列和行的访问
 * <p>
 * <b>用法：</b>
 * 
 * 注意：对于列的访问，序号从1开始，比如访问第一行的第一列：myTable.getRow(0).getInt(1);</br>
 * 根据名称获取列序号也是一样的，获取的都是从1开始的编号；</br>
 * 这么做是为了和JDBC保持一致</br>  
* @author wangtianyu 
* @date 2016年9月18日 下午3:27:32 
*  
*/
public class DataTable {
private static final DataColumn[]EMPTY_COLUMNS = new DataColumn[0];
private static final Map<String,Integer>EMPTY_COLUMN_NAME_INDEX=Collections.unmodifiableMap(new HashMap<String, Integer>(1));
private static final List<DataRow>EMPTY_ROWS=Collections.unmodifiableList(new ArrayList<DataRow>(1)); 
private String name;
}
