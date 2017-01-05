package com.data.cache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class GuavaCache<K, V> {

    LoadingCache<K, V> graphs;
<<<<<<< HEAD
    Cache<K, V> cache;

    public GuavaCache(){

    }

    public GuavaCache(V value) {
        graphs = CacheBuilder.newBuilder().maximumSize(1000).expireAfterWrite(10, TimeUnit.MINUTES)
=======
    	GuavaCache(K key,V value){
        graphs =CacheBuilder.newBuilder().maximumSize(1000).expireAfterWrite(10, TimeUnit.MINUTES)
>>>>>>> origin/master
                .build(new CacheLoader<K, V>() {
                    @Override
                    public V load(K key) throws Exception {
                        return (V) value;
                    }
                });

    }
   V getCallableCache(final Object key){
	return null;
    }

//    public GuavaCache(K key, V value) {
//        cache=CacheBuilder.newBuilder().maximumSize(1000).build();
//    }


    public void put(K key, V value) {
        graphs.put(key, value);
    }

    public V get(K key) throws ExecutionException {
        return graphs.get(key);
    }


}
