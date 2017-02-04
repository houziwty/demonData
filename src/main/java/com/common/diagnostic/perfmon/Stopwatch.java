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
}
