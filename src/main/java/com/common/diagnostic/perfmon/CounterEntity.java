package com.common.diagnostic.perfmon;

import com.common.diagnostic.obsercation.ObservableUnit;
import com.common.diagnostic.obsercation.ObserverReportSnapshot;

public class CounterEntity implements SmartCounter,ObservableUnit {

	@Override
	public void reset() {
		
	}

	@Override
	public void increase() {
		
	}

	@Override
	public void decrease() {
		
	}

	@Override
	public void increaseBy(long value) {
		
	}

	@Override
	public void setRawValue(long value) {
		
	}

	@Override
	public void increaseRatio(boolean hitted) {
		
	}

	@Override
	public Stopwatch begin() {
		return null;
	}

	@Override
	public ObserverReportSnapshot getObserverSnapshot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInstanceName() {
		// TODO Auto-generated method stub
		return null;
	}

}
