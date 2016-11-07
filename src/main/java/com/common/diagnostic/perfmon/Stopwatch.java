package com.common.diagnostic.perfmon;

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

}
