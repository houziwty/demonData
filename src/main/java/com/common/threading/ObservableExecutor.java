package com.common.threading;

import java.util.concurrent.Executor;

import com.common.diagnostic.perfmon.spi.NumberCounter;
import com.common.diagnostic.perfmon.spi.TransactionCounter;


/**
 * 
 * <b>描述: </b>这是一个{@link Executor}的包装类，使用装饰者模式将{@link Executor}装饰起来，为其增加计数器功能
 * <p>
 * <b>功能: </b>使用装饰者模式将{@link Executor}装饰起来，为其增加计数器功能
 * <p>
 * <b>用法: </b>该类用于继承使用
 * <p>
 */
public class ObservableExecutor implements Executor {
 private String name;
 private Executor innerExecutor;
 private NumberCounter sizeCounter;
    private TransactionCounter workerCounter;
	@Override
	public void execute(Runnable command) {

	}

}
