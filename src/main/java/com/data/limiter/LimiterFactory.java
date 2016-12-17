package com.data.limiter;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

public class LimiterFactory {
	private static LimiterFactory instance = new LimiterFactory();
	private static final ConcurrentHashMap<String, Limiter> limiters = new ConcurrentHashMap<String, Limiter>();
	private static final ConcurrentHashMap<String, String> prop = new ConcurrentHashMap<String, String>();
	private static Timer timer = new Timer();

	private int maxCount = 4096;
	private int maxCountPerSecond = 512;
	private long period = 60 * 1000;// 1分钟
	private boolean isInit = false;

	public static LimiterFactory getInstance() {
		return instance;
	}

	/**
	 * Properties示例如下：
	 * 
	 * maxCount=2048 maxCountPerSecond=256
	 * 
	 * core-RegisterSipcApp.maxCount=4096
	 * core-RegisterSipcApp.maxCountPerSecond=512
	 * 
	 * @param configs
	 */
	public synchronized void init(Properties configs) {
		if (!isInit) {
			String key = null;
			String val = null;
			Enumeration eunm = configs.propertyNames();
			while (eunm.hasMoreElements()) {
				key = (String) eunm.nextElement();
				val = configs.getProperty(key);
				prop.put(key, val);
			}
			if (prop.containsKey("maxCount"))
				maxCount = Integer.parseInt(prop.get("maxCount"));
			if (prop.containsKey("maxCountPerSecond"))
				maxCountPerSecond = Integer.parseInt(prop.get("maxCountPerSecond"));
			isInit = true;
		}
	}
}
