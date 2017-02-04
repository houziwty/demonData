package com.common.diagnostic.perfmon.spi;

import com.common.diagnostic.perfmon.CounterEntity;
import com.common.diagnostic.perfmon.SmartCounter;
import com.common.diagnostic.perfmon.Stopwatch;

/**
 * Created by Haoweilai on 2017/2/4.
 */
public class TransactionCounter extends CounterEntity implements SmartCounter,Stopwatch.Watchable {
    @Override
    public void end(long nanos) {

    }

    @Override
    public void fail(long nanos, String message) {

    }

    @Override
    public void fail(long nanos, Throwable error) {

    }
}
