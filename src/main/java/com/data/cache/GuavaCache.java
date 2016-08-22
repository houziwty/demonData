package com.data.cache;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class GuavaCache<K,V> {
	
	
	public   V put(K key,V  value){
		LoadingCache<K, V>graphs=
		CacheBuilder.newBuilder().maximumSize(1000).expireAfterWrite(10, TimeUnit.MINUTES)
		.build(new CacheLoader<K, V>(){
			@Override
			public V load(K key) throws Exception {
				return  (V) key;
			}
			
		});
		return value;
	}

}
