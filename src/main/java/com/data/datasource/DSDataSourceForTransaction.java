package com.data.datasource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Demon on 2017/7/24.
 * 多数据库数据源，用于创建代理的Connection，来实现多库操作事务
 */
public class DSDataSourceForTransaction extends DynamicDataSource {
    @Override
    public Connection getConnection() throws SQLException {
        return new DSConnectionTransaction(this,this.getDsh());
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return new DSConnectionTransaction(this,this.getDsh());
    }
}
