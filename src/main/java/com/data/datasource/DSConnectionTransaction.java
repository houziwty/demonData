package com.data.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.concurrent.Executor;

/**
 * Created by Demon on 2017/6/19.
 * 用于分库事务:例如 事务内 A sql对应A库，
 * B sql 对应B库，事务提交时需要保证一至性:其中任何一个失败都需要两个SQL的事务都回滚。,
 * 实现方式：代理多个库实际的connection，如果一个某个库的sql操作失败，
 * 所有connection rollback,如果未出现异常，则所有的connection commit;
 * 对外（spring） 来说是一个connection，实际上它代理了多个connection，可以对应同一个为库或多个库
 * 线程内对象，每一条线程对应一个DynamicConnection，聚合多个实际的connection,
 * 对于每一个sql操作，根据分库对就的KEY,找到实际的connection再执行操作。
 * 对于 commit操作, 调用内部N个实际的connection commit
 * 对于 rollback操作, 调用内部N个实际的connection rollback
 * 对于 close操作, 调用内部N个实际的connection close
 */
public class DSConnectionTransaction implements Connection {

    private static final Logger logger = LoggerFactory.getLogger(DSConnectionTransaction.class);
    DynamicDataSource dynamicDataSource = null;
    DataSourceHolder dataSourceHolder = null;
    Map<String, Connection> connectionMap = new HashMap<>();
    LinkedList<Connection> connSequenceList = new LinkedList<>();

    int initTransactionLevel = -9;
    int transactionLevel = initTransactionLevel;


    //全局属性对应多个Connection
    boolean commitFlag = true;

    //
    boolean closeFlag = false;

    public DSConnectionTransaction(DynamicDataSource dynamicDataSource, DataSourceHolder dataSourceHolder) {
        this.dynamicDataSource = dynamicDataSource;
        this.dataSourceHolder = dataSourceHolder;
    }

    public Connection getCurrentConnection() {
        return getCurrentConnection(false);
    }

    public Connection getCurrentConnectionForTransaction() {
        return getCurrentConnection(true);
    }

    //获取连接池
    private Connection getCurrentConnection(boolean isForCommit) {
        String currentDataSourceKey = this.dataSourceHolder.getDataSource();
        if (currentDataSourceKey == null)
            throw new RuntimeException("DataSourceHolder.getDataSource()==null");
        Connection conn = null;
        if (!connectionMap.containsKey(currentDataSourceKey)) {
            DataSource dataSource = this.dynamicDataSource.determineTargetDataSource();
            try {
                conn = dataSource.getConnection();
                conn.setAutoCommit(this.getAutoCommit());//设置是否自动提交
                if (this.transactionLevel != initTransactionLevel) {
                    conn.setTransactionIsolation(this.transactionLevel);//设置事务隔离级别
                }

            } catch (Exception ex) {
                throw new RuntimeException("DataSource.getConnection() error", ex);
            }
            connectionMap.put(currentDataSourceKey, conn);
        }
        conn = connectionMap.get(currentDataSourceKey);
        if (isForCommit) {//记录需要commit 或 rollback的connection
            connSequenceList.remove(conn);
            connSequenceList.addFirst(conn);
        }
        return conn;
    }

    private void clear() {
        this.dynamicDataSource = null;
        this.dataSourceHolder = null;
        this.connectionMap.clear();
        this.connectionMap = null;
        this.connSequenceList.clear();
        this.connSequenceList = null;
    }

    @Override
    public Statement createStatement() throws SQLException {
        return this.getCurrentConnectionForTransaction().createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return this.getCurrentConnectionForTransaction().prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return this.getCurrentConnectionForTransaction().prepareCall(sql);
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        return this.getCurrentConnection().nativeSQL(sql);
    }


    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        this.commitFlag = autoCommit;
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return this.commitFlag;
    }

    @Override
    public void commit() throws SQLException {
        boolean toCommit = true;
        SQLException commitException = null;
        long startTime = System.currentTimeMillis();
        Iterator<Connection> iterator = connSequenceList.iterator();
        int commitIndex = 0;
        while (iterator.hasNext()) {
            Connection conn = iterator.next();
            if (toCommit) {
                commitIndex++;
                try {
                    conn.commit();
                } catch (SQLException ex) {
                    commitFlag = false;//commit 失败，之后的connection rollback
                    logger.error("commit exception ,next connection to rollback  : " + ex.getMessage(), ex);
                    if (commitIndex == 1) {
                        commitException = new SQLException("commit fail at 1st connection,so  have no  dirty data ,no need to clear it", ex);
                    } else {
                        commitException = new SQLException("DirtyDataException: commit fail at " + commitIndex + "th connection,commit success in " + (commitIndex - 1) + " connections, so  have dirty data ,need to clear it", ex);
                    }
                }
            } else {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    logger.warn("Rollback exception (after commit)   " + ex.getMessage(), ex);
                }
            }
        }
        if (commitException != null) {
            throw commitException;
        }
        long useTime = System.currentTimeMillis() - startTime;
        if (logger.isDebugEnabled()) {
            logger.debug("commit success finish, use time:{}", useTime);
        }
    }

    @Override
    public void rollback() throws SQLException {
        SQLException rollbackException = null;
        Iterator<Connection> iterator = connSequenceList.iterator();
        while (iterator.hasNext()) {
            Connection conn = iterator.next();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                rollbackException = ex;
                logger.warn("Rollback exception  " + ex.getMessage(), ex);
            }
        }
        if (rollbackException != null) {
            throw rollbackException;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("rollback ");
        }
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        SQLException rollbackException = null;
        Iterator<Connection> iterator = connSequenceList.iterator();
        while (iterator.hasNext()) {
            Connection conn = iterator.next();
            try {
                conn.rollback(savepoint);
            } catch (SQLException e) {
                rollbackException = e;
                logger.warn("Rollback exception  " + e.getMessage(), e);
            }
        }
        if (rollbackException != null) {
            throw rollbackException;
        }
    }

    @Override
    public void close() throws SQLException {
        Iterator<Connection> iterator = connectionMap.values().iterator();
        while (iterator.hasNext()) {
            Connection conn = iterator.next();
            conn.setAutoCommit(true);
            try {
                conn.close();
            } catch (SQLException e) {
                logger.warn("connection close exception  " + e.getMessage(), e);
            }
        }
        clear();
        closeFlag = true;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return this.closeFlag;
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return this.getCurrentConnection().getMetaData();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        this.getCurrentConnection().setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return this.getCurrentConnection().isReadOnly();
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        this.getCurrentConnection().setCatalog(catalog);
    }

    @Override
    public String getCatalog() throws SQLException {
        return this.getCurrentConnection().getCatalog();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        this.transactionLevel = level;
        Iterator<Connection> iterator = connectionMap.values().iterator();
        while (iterator.hasNext()) {
            Connection conn = iterator.next();
            conn.setTransactionIsolation(level);
        }
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return transactionLevel;
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return this.getCurrentConnection().getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        this.getCurrentConnection().clearWarnings();
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return this.getCurrentConnectionForTransaction().createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return this.getCurrentConnectionForTransaction().prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return this.getCurrentConnectionForTransaction().prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return this.getCurrentConnection().getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        this.getCurrentConnection().setTypeMap(map);
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        this.getCurrentConnection().setHoldability(holdability);
    }

    @Override
    public int getHoldability() throws SQLException {
        return this.getCurrentConnection().getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return this.getCurrentConnection().setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return this.getCurrentConnection().setSavepoint(name);
    }


    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        this.getCurrentConnection().releaseSavepoint(savepoint);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return this.getCurrentConnectionForTransaction().createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return this.getCurrentConnectionForTransaction().prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return this.getCurrentConnectionForTransaction().prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return this.getCurrentConnectionForTransaction().prepareStatement(sql, autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return this.getCurrentConnectionForTransaction().prepareStatement(sql, columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return this.getCurrentConnectionForTransaction().prepareStatement(sql, columnNames);
    }

    @Override
    public Clob createClob() throws SQLException {
        return this.getCurrentConnection().createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        return this.getCurrentConnection().createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return this.getCurrentConnection().createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return this.getCurrentConnection().createSQLXML();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return this.getCurrentConnection().isValid(timeout);
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        this.getCurrentConnection().setClientInfo(name, value);
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        this.getCurrentConnection().setClientInfo(properties);
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return this.getCurrentConnection().getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return this.getCurrentConnection().getClientInfo();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return this.getCurrentConnection().createArrayOf(typeName, elements);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return this.getCurrentConnection().createStruct(typeName, attributes);
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        this.getCurrentConnection().setSchema(schema);
    }

    @Override
    public String getSchema() throws SQLException {
        return this.getCurrentConnection().getSchema();
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        this.getCurrentConnection().abort(executor);
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        this.getCurrentConnection().setNetworkTimeout(executor, milliseconds);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return this.getCurrentConnection().getNetworkTimeout();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return this.getCurrentConnection().unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return this.getCurrentConnection().isWrapperFor(iface);
    }
}
