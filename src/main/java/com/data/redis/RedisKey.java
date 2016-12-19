package com.data.redis;

import java.io.Serializable;

public class RedisKey implements Serializable {

	/** 
	* @Fields serialVersionUID : TODO
	*/
	private static final long serialVersionUID = 1L;
	
	//每个业务不同的
	private String family;
	
	
	private String key;
}
