package com.data.cache;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;

public class GuavaCache<K,V> {
	public   V put(K key,V  value){
		LoadingCache<key, value>graphs=
		CacheBuilder.newBuilder().maximumSize(1000).expireAfterWrite(10, TimeUnit.MINUTES)
//		.removalListener(MY)
		return value;

	}

}
