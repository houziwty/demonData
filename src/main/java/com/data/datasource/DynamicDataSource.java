package com.data.datasource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * Created by Demon on 2017/6/19.
 * 动态数据源
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    private final Logger log = Logger.getLogger(this.getClass());

    /**
     * dataSource
     */
    private Map<String, DataSource> dataSource;

    private DataSourceHolder dsh;

    public Map<String, DataSource> getDataSource() {
        return dataSource;
    }

    public void setDataSource(Map<String, DataSource> dataSource) {
        this.dataSource = dataSource;
    }

    public DataSourceHolder getDsh() {
        return dsh;
    }

    public void setDsh(DataSourceHolder dsh) {
        this.dsh = dsh;
    }

    @Override
    protected Object determineCurrentLookupKey() {
//do nothing
        return null;
    }
    @Override
    protected  DataSource determineTargetDataSource(){
        DataSource returnDataSource=this.dataSource.get(this.dsh.getDataSource());
        return returnDataSource;
    }

}
