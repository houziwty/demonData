package com.data.limiter;

import java.util.concurrent.atomic.AtomicInteger;

///限流器
public class Limiter {
	private int maxCount;
	private AtomicInteger counter;
	private int maxCountPerSecond;
	private AtomicInteger counterPerSecond;

	/**
	 * 构造函数
	 * 
	 * @param maxCount
	 *            最大限流数量-10分钟内积压直
	 * @param maxCountPerSecond
	 *            每秒最大限流数量
	 */
	public Limiter(int maxCount, int maxCountPerSecond) {
		this.maxCount = maxCount;
		this.maxCountPerSecond = maxCountPerSecond;
		counter = new AtomicInteger(0);
		counterPerSecond = new AtomicInteger(0);
	}
	public void increment(){
		counter.incrementAndGet();
		counterPerSecond.incrementAndGet();
	}
	public void decrement(){
		counter.decrementAndGet();
	}
	public int getCount(){
		return counter.get();
	}
	public void setCount(int v){
		counter.set(v);
	}
	public int getCountPerSecond(){
		return counterPerSecond.get();
	}
	
}
