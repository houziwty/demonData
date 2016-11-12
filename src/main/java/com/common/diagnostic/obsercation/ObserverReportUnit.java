package com.common.diagnostic.obsercation;

import java.util.List;


public interface ObserverReportUnit {
	void outputReport(ObserverReportRow row);

	ObserverReportUnit summaryAll(List<ObserverReportUnit> items);
}
