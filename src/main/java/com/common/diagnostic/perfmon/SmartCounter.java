package com.common.diagnostic.perfmon;

/*
* @ClassName: SmartCounter 
* @Description: 计数器通用接口类 
* @author wangtianyu 
* @date 2016年3月4日 上午10:49:49 
*  
*/
public interface SmartCounter {
void reset();
void increase();
void decrease();
void increaseBy(long value);
void setRawValue(long value);
void increaseRatio(boolean hitted);
Stopwatch begin();
}
