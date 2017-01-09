package com.data.redis;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.ShardedJedisPipeline;
import redis.clients.jedis.Tuple;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Haoweilai on 2016/12/21.
 */
public class RedisClusterCacheClient implements CacheClient {


    // 默认超时时间
    private static final int DEFAULT_TIMEOUT = 400;

    // 默认最大活动连接数
    private static final int MAX_ACTIVE = 20;

    // 默认最大空闲连接数
    private static final int MAX_IDEL = 10;

    // 默认最大等待
    private static final int MAX_WAIT = 100;


    public RedisClusterCacheClient(String servers, String app) {

        Set<HostAndPort> jedisClusterNode = new HashSet<>();
        try{
            String[] hosts = servers.trim().split("\\|");
        }catch(Exception e){

        }
    }

    @Override
    public void cleanup() {

    }

    @Override
    public Long decr(String key) {
        return null;
    }

    @Override
    public void del(String... keys) {

    }

    @Override
    public Boolean exists(byte[] key) {
        return null;
    }

    @Override
    public Boolean exists(String key) {
        return null;
    }

    @Override
    public Long expire(String key, int seconds) {
        return null;
    }

    @Override
    public byte[] get(byte[] key) {
        return new byte[0];
    }

    @Override
    public String get(String key) {
        return null;
    }

    @Override
    public Long hdel(byte[] key, byte[] field) {
        return null;
    }

    @Override
    public long hdel(String key, String field) {
        return 0;
    }

    @Override
    public Boolean hexists(byte[] key, byte[] field) {
        return null;
    }

    @Override
    public byte[] hget(byte[] key, byte[] field) {
        return new byte[0];
    }

    @Override
    public String hget(String key, String field) {
        return null;
    }

    @Override
    public Map<byte[], byte[]> hgetAll(byte[] key) {
        return null;
    }

    @Override
    public Map<String, String> hgetAll(String key) {
        return null;
    }

    @Override
    public long hincrBy(String key, String field, long value) {
        return 0;
    }

    @Override
    public List<byte[]> hmget(byte[] key, byte[]... fields) {
        return null;
    }

    @Override
    public List<String> hmget(String key, String... fields) {
        return null;
    }

    @Override
    public String hmset(byte[] key, Map<byte[], byte[]> hash) {
        return null;
    }

    @Override
    public String hmset(String key, Map<String, String> hash) {
        return null;
    }

    @Override
    public Long hset(byte[] key, byte[] field, byte[] value) {
        return null;
    }

    @Override
    public Long hset(String key, String field, String value) {
        return null;
    }

    @Override
    public Long incr(String key) {
        return null;
    }

    @Override
    public Set<byte[]> keys(byte[] pattern) {
        return null;
    }

    @Override
    public Set<String> keys(String pattern) {
        return null;
    }

    @Override
    public Long llen(String key) {
        return null;
    }

    @Override
    public Long lpush(byte[] key, byte[] value) {
        return null;
    }

    @Override
    public Long lpush(String key, String value) {
        return null;
    }

    @Override
    public Long lpush(String key, Object[] objs) {
        return null;
    }

    @Override
    public List<byte[]> lrange(byte[] key, int start, int end) {
        return null;
    }

    @Override
    public List<String> lrange(String key, int start, int end) {
        return null;
    }

    @Override
    public List<Object> pipelined(ShardedJedisPipeline shardedJedisPipeline) {
        return null;
    }

    @Override
    public List<Object> pipelined(Pipeline pipeline) {
        return null;
    }

    @Override
    public Long rpush(byte[] key, byte[] value) {
        return null;
    }

    @Override
    public Long sadd(byte[] key, byte[] member) {
        return null;
    }

    @Override
    public Long sadd(String key, String member) {
        return null;
    }

    @Override
    public Long scard(byte[] key) {
        return null;
    }

    @Override
    public Long scard(String key) {
        return null;
    }

    @Override
    public void set(byte[] key, byte[] value) {

    }

    @Override
    public void set(String key, String value) {

    }

    @Override
    public void mset(String... keysvalues) {

    }

    @Override
    public String setex(byte[] key, int seconds, byte[] value) {
        return null;
    }

    @Override
    public Long setnx(byte[] key, byte[] value) {
        return null;
    }

    @Override
    public long setnx(String key, String value) {
        return 0;
    }

    @Override
    public Boolean sismember(byte[] key, byte[] member) {
        return null;
    }

    @Override
    public Boolean sismember(String key, String member) {
        return null;
    }

    @Override
    public Set<byte[]> smembers(byte[] key) {
        return null;
    }

    @Override
    public Set<String> smembers(String key) {
        return null;
    }

    @Override
    public List<String> sort(String key, int start, int pageSize) {
        return null;
    }

    @Override
    public byte[] spop(byte[] key) {
        return new byte[0];
    }

    @Override
    public String spop(String key) {
        return null;
    }

    @Override
    public byte[] srandmember(byte[] key) {
        return new byte[0];
    }

    @Override
    public String srandmember(String key) {
        return null;
    }

    @Override
    public Long srem(byte[] key, byte[] member) {
        return null;
    }

    @Override
    public Long srem(String key, String member) {
        return null;
    }

    @Override
    public void zadd(String key, double score, String member) {

    }

    @Override
    public Long zcard(byte[] key) {
        return null;
    }

    @Override
    public Long zcard(String key) {
        return null;
    }

    @Override
    public Long zcount(byte[] key, long min, long max) {
        return null;
    }

    @Override
    public Long zcount(String key, long min, long max) {
        return null;
    }

    @Override
    public Set<byte[]> zrange(byte[] key, int start, int end) {
        return null;
    }

    @Override
    public Set<String> zrange(String key, int start, int end) {
        return null;
    }

    @Override
    public Set<byte[]> zrangeByScore(byte[] key, long min, long max) {
        return null;
    }

    @Override
    public Set<String> zrangeByScore(String key, long min, long max) {
        return null;
    }

    @Override
    public Set<Tuple> zrangeWithScores(byte[] key, int start, int end) {
        return null;
    }

    @Override
    public Set<Tuple> zrangeWithScores(String key, int start, int end) {
        return null;
    }

    @Override
    public Set<Tuple> zrevRangeWithScores(String key, int start, int end) {
        return null;
    }

    @Override
    public long zrem(byte[] key, byte[] member) {
        return 0;
    }

    @Override
    public long zrem(String key, String member) {
        return 0;
    }

    @Override
    public Long zremrangeByScore(byte[] key, double start, double end) {
        return null;
    }

    @Override
    public Long zremrangeByScore(String key, double start, double end) {
        return null;
    }

    @Override
    public Long zrank(byte[] key, byte[] member) {
        return null;
    }

    @Override
    public Long zrevrank(byte[] key, byte[] member) {
        return null;
    }

    @Override
    public String select(int index) {
        return null;
    }

    @Override
    public void flushall() {

    }
}
