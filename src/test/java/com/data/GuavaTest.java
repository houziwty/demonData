package com.data;

import com.data.cache.GuavaCache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;

/**
 * Created by Haoweilai on 2016/12/21.
 */
public class GuavaTest {
    public static void main(String []args) throws ExecutionException {
        System.out.println("begin");
        String key="id1";
        String value="1";
        GuavaCache cache=new GuavaCache(value);
        System.out.println(cache.get("id1"));
        for(int i=2;i<10;i++){
            String k="id"+i;
            cache.put(k,i);
        }

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
