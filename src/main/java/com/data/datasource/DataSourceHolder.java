package com.data.datasource;

/**
 * Created by Demon on 2017/6/19.
 */
public abstract class DataSourceHolder {

     public abstract String getDBHost(Object x);

    private final  ThreadLocal<String>dataSources=new ThreadLocal<String>();

    /**
     * 设置数据源
     *
     * @param dataSourceKey
     */
   private void setDataSource(String dataSourceKey){
       dataSources.set(dataSourceKey);
   }
    /**
     * 获取数据源
     *
     * @return
     */
    public String getDataSource(){
        return dataSources.get();
    }

    /**
     * 数据源的设置
     */
    public  void setDBHost(Object dataSourceKey){
        setDataSource(getDBHost(dataSourceKey));
    }
    /**
     * 清除thread local中的数据源
     *
     */
    public  void clearDataSource(){
        dataSources.remove();
    }






}
