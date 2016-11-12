package com.common.diagnostic.obsercation;

public interface ObservableUnit {
	/*
	 * 生成快照项
	 * */
	ObserverReportSnapshot getObserverSnapshot();
	
	
	/*
	 * 获取实用名字
	 * */
	String getInstanceName();
}
