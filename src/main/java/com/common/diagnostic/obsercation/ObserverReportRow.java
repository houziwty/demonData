package com.common.diagnostic.obsercation;

public class ObserverReportRow {
	private int p;
	private String instance;
	private String[] data;

	public String[] getData() {
		return data;
	}

	public String getInstanceName() {
		return instance;
	}

	public ObserverReportRow(int size, String instance) {
		p = 0;
		data = new String[size];
		this.instance = instance;
	}

	protected ObserverReportRow(String[] data, String instance) {
		this.data = data;
		this.instance = instance;
	}

	/**
	 * 
	 * 输出一项的值
	 * 
	 * @param value
	 */
	public void output(String value) {
		if (value == null)
			value = "";
		else if (value.equalsIgnoreCase("NaN"))
			value = "0";
		data[p] = value;
		p++;
	}
}
