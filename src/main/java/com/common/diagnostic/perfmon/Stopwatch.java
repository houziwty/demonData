package com.common.diagnostic.perfmon;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
* @ClassName: Stopwatch 
* @Description: 自动开始的计时秒表, 精度到纳秒1E-9 
* @author wangtianyu 
* @date 2016年3月17日 下午1:31:45 
*  
*/
public class Stopwatch {
	public interface Watchable{
		void end(long nanos);
		void fail(long nanos,String message);
		void fail(long nanos,Throwable error);
	}
private long begin;
	private AtomicBoolean isDone=new AtomicBoolean(false);
	private Watchable watchable;
	public Stopwatch(Watchable watchable){
		this.watchable=watchable;
		begin=System.nanoTime();
	}
	public Stopwatch(){
		begin=System.nanoTime();
	}
	public void update(){
		begin=System.nanoTime();
	}
	public long getBeginNanos(){
		return begin;
	}
	public long getNanos()
	{
		return System.nanoTime() - begin;
	}
	public double getSeconds()
	{
		return (System.nanoTime() - begin) / 1E9;
	}

	public double getMillseconds()
	{
		return (double)(System.nanoTime() - begin) / 1E6;
	}
	public void end(){
		long nanos=System.nanoTime()-begin;
		if(isDone.compareAndSet(false,true)&&watchable!=null){
			watchable.end(nanos);
		}
	}
	public void fail(String message)
	{
		long nanos = System.nanoTime() - begin;
		if (isDone.compareAndSet(false, true) && watchable != null) {
			watchable.fail(nanos, message);
		}
	}

	public void fail(Throwable error)
	{
		long nanos = System.nanoTime() - begin;
		if (isDone.compareAndSet(false, true) && watchable != null) {
			watchable.fail(nanos, error);
		}
	}
}
