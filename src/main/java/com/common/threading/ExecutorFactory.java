package com.common.threading;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecutorFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorFactory.class);

	private static Map<String, Executor> executors = new HashMap<>();

	/**
	 * 
	 * 通过名字获取一个已经创建的线程池
	 * 
	 * @param name
	 * @return
	 */
	public static Executor getExecutor(String name) {
		return executors.get(name);
	}

	/**
	 * 
	 * 新增一个固定大小的线程池
	 * 
	 * @param name
	 * @param size
	 *            固定线程数
	 * @param limit
	 *            最大队列长度
	 * @return
	 */
	public synchronized static Executor newFixedExecutor(final String name, int size, int limit) {
		if (executors.get(name) == null) {
			Executor innerExecutor = Executors.newFixedThreadPool(size, new ThreadFactory() {
				private int count = 0;

				@Override
				public Thread newThread(Runnable r) {
					Thread t = new Thread(r);
					t.setDaemon(false);
					t.setName("p-" + name + "-" + count);
					count++;
					return t;
				}
			});
			FixedObservableExecutor executor = new FixedObservableExecutor(name, innerExecutor, limit, size);
		executors.put(name, executor);
			LOGGER.info("Create FixedExecutor:"+name+" size={} limit={} ", size, limit);
		}
		return getExecutor(name);
	}
	/**
	 *
	 * 新增一个可扩充的线程池
	 * @param name
	 * @return
	 */
	public synchronized static Executor newCachedExecutor(String name){
		if(executors.get(name)==null){
			Executor innerExecutor=Executors.newCachedThreadPool();
			CachedObserverableExecutor executor=new CachedObserverableExecutor(name,innerExecutor);
		}
		return getExecutor(name);
	}
}
