package com.data;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * Created by Haoweilai on 2016/12/21.
 */
public class GuavaTest {
    public static void main(String []args){
        System.out.println("begin");
        loadCache();
    }
    static  void loadCache(){
        LoadingCache<String,String> cache= CacheBuilder.newBuilder().build(
                new CacheLoader<String, String>() {
                    @Override
                    public String load(String s) throws Exception {
                        return s;
                    }
                }
        );
System.out.println(cache.apply("test"));

    }
}
