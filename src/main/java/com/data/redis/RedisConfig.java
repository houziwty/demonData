package com.data.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

public final class RedisConfig {

	

	public static ShardedJedisPool shardedJedisPool;  
	  
	    static {  
	        // 读取相关的配置  
	        ResourceBundle resourceBundle = ResourceBundle.getBundle("redis");  
	        int maxActive = Integer.parseInt(resourceBundle.getString("redis.pool.maxActive"));  
	        int maxIdle = Integer.parseInt(resourceBundle.getString("redis.pool.maxIdle"));  
	        int maxWait = Integer.parseInt(resourceBundle.getString("redis.pool.maxWait"));  
	  
	        String ip1 = resourceBundle.getString("redis.zhugexutang.ip1");  
	        int port1 = Integer.parseInt(resourceBundle.getString("redis.zhugexutang.port1"));  
	        String ip2 = resourceBundle.getString("redis.zhugexutang.ip2");  
	        int port2 = Integer.parseInt(resourceBundle.getString("redis.zhugexutang.port2"));   
	        //设置配置  
	        JedisPoolConfig config = new JedisPoolConfig();  
	        config.setMaxTotal(maxActive);  
	        config.setMaxIdle(maxIdle);  
	        config.setMaxWaitMillis(maxWait);  
	          
	        //设置分片元素信息  
	        JedisShardInfo shardInfo1 = new JedisShardInfo(ip1,port1);  
	        JedisShardInfo shardInfo2 = new JedisShardInfo(ip2,port2);  
	        List<JedisShardInfo> list = new ArrayList<JedisShardInfo>();  
	        list.add(shardInfo1);  
	        list.add(shardInfo2);  
	        shardedJedisPool = new ShardedJedisPool(config, list);  
	    }  
	  
	      
	   
	
}
