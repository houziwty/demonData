package com.common.threading;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecutorFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorFactory.class);

	private static Map<String, Executor> executors = new HashMap<>();

	/**
	 * 
	 * 通过名字获取一个已经创建的线程池
	 * @param name
	 * @return
	 */
	public static Executor getExecutor(String name) {
		return executors.get(name);
	}
	
	/**
	 * 
	 * 新增一个固定大小的线程池
	 * @param name
	 * @param size 固定线程数
	 * @param limit 最大队列长度
	 * @return
	 */
	public synchronized static Executor newFixedExecutor(final String name,int size,int limit){
	
		return getExecutor(name);
	}
}
