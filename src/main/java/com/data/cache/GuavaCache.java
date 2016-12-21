package com.data.cache;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class GuavaCache<K, V> {

    LoadingCache<K, V> graphs;
    GuavaCache(K key,V value){
        graphs =CacheBuilder.newBuilder().maximumSize(1000).expireAfterWrite(10, TimeUnit.MINUTES)
                .build(new CacheLoader<K, V>() {
                    @Override
                    public V load(K key) throws Exception {
                        return (V) value;
                    }
                });
    }

    public void put(K key, V value) {

        graphs.put(key,value);
    }

    public V get(K key, V value) {
        return value;
    }


}
