package com.data.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
//import org.apache.log4j.Logger;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

public class RedisCacheClient implements CacheClient {

	// Logger logger = Logger.getLogger(RedisCacheClient.class);

	// 默认超时时间
	private static final int DEFAULT_TIMEOUT = 400;

	// 默认最大活动连接数
	private static final int MAX_ACTIVE = 20;

	// 默认最大空闲连接数
	private static final int MAX_IDEL = 10;

	// 默认最大等待
	private static final int MAX_WAIT = 100;

	private static ShardedJedisPool pool;

	// private static JedisPool pool;
	private static JedisPoolConfig config;

	static {
		config = new JedisPoolConfig();
		// 最大空闲连接数 默认8个
		config.setMaxIdle(MAX_IDEL);
		// 最大连接数, 默认8个
		config.setMaxTotal(MAX_ACTIVE);

		config.setMaxWaitMillis(MAX_WAIT);
		//
		config.setTestWhileIdle(true);
		//
		config.setTestOnBorrow(false);

		// timeBetweenEvictionRunsMillis
		config.setTimeBetweenEvictionRunsMillis(DEFAULT_TIMEOUT);
		// 逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
		config.setMinEvictableIdleTimeMillis(1800000);

	}

	public RedisCacheClient() {

	}

	public RedisCacheClient(String servers, String app) {
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		try {

			String[] hosts = servers.trim().split("\\|");
			if (hosts.length > 2) {
				for (String host : hosts) {
					String[] address = host.split(":");
					JedisShardInfo shard;
					if (address.length > 2) {
						shard = new JedisShardInfo(address[0], Integer.parseInt(address[1]), address[2]);
					} else {
						shard = new JedisShardInfo(address[0], Integer.parseInt(address[1]));
					}
					shards.add(shard);
				}
			}
			pool = new ShardedJedisPool(config, shards, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
		} catch (Exception ex) {
			// logger.error("App="+app+"<Servers="+servers+"<shards.size="+shards.size()+"<");
		}

	}

	private void close(ShardedJedis redis) {
		if (redis != null)
			redis.close();
	}

	@Override
	public void cleanup() {
		pool.destroy();
	}

	@Override
	public Long decr(String key) {
		ShardedJedis redis = pool.getResource();
		Long result = -1L;
		try {
			result = redis.decr(key);
		} catch (Exception ex) {

		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public void del(String... keys) {
		ShardedJedis redis = pool.getResource();
		try {
			for (String key : keys) {
				redis.del(key);
			}
		} catch (Exception e) {
		} finally {
			this.close(redis);
		}
	}

	@Override
	public Boolean exists(byte[] key) {
		ShardedJedis redis = pool.getResource();
		Boolean result = false;
		try {
			result = redis.exists(key);
		} catch (Exception e) {

		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public Boolean exists(String key) {
		ShardedJedis redis = pool.getResource();
		Boolean result = false;
		try {
			result = redis.exists(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public Long expire(String key, int seconds) {
		ShardedJedis redis = pool.getResource();
		Long result = -1L;
		try {

			result = redis.expire(key, seconds);
		} catch (Exception ex) {
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public byte[] get(byte[] key) {
		ShardedJedis redis = pool.getResource();
		byte[] result = null;
		try {
			result = redis.get(key);
		} catch (Exception ex) {
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public String get(String key) {
		ShardedJedis redis = pool.getResource();
		String result = null;
		try {
			result = redis.get(key);
		} catch (Exception ex) {
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public Long hdel(byte[] key, byte[] field) {
		long result = -1L;
		ShardedJedis redis = pool.getResource();
		try {
			result = redis.hdel(key, field);
		} catch (Exception ex) {
			ex.printStackTrace();
			// logger.error("hexists:", ex);
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public long hdel(String key, String field) {
		long result = -1L;
		ShardedJedis redis = pool.getResource();
		try {
			result = redis.hdel(key, field);
		} catch (Exception ex) {
			ex.printStackTrace();
			// logger.error("hexists:", ex);
		} finally {
			this.close(redis);
		}
		return result;
	}

	public Boolean hexists(String key, String field) {
		ShardedJedis redis = pool.getResource();
		boolean result = false;
		try {
			result = redis.hexists(key, field);
		} catch (Exception ex) {
			ex.printStackTrace();
			// logger.error("hexists:", ex);
		} finally {
			this.close(redis);
		}

		return result;
	}

	@Override
	public Boolean hexists(byte[] key, byte[] field) {
		ShardedJedis redis = pool.getResource();
		boolean result = false;
		try {
			result = redis.hexists(key, field);
		} catch (Exception ex) {
			ex.printStackTrace();
			// logger.error("hexists:", ex);
		} finally {
			this.close(redis);
		}

		return result;
	}

	@Override
	public byte[] hget(byte[] key, byte[] field) {

		ShardedJedis redis = pool.getResource();
		byte[] result = null;
		try {
			result = redis.hget(key, field);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public String hget(String key, String field) {
		ShardedJedis redis = pool.getResource();
		String result = null;
		try {
			result = redis.hget(key, field);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public Map<byte[], byte[]> hgetAll(byte[] key) {
		ShardedJedis redis = pool.getResource();
		Map<byte[], byte[]> result = new HashMap<byte[], byte[]>();
		try {
			result = redis.hgetAll(key);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	// 获取全部哈希值
	@Override
	public Map<String, String> hgetAll(String key) {
		ShardedJedis redis = pool.getResource();
		Map<String, String> result = new HashMap<String, String>();
		try {
			result = redis.hgetAll(key);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public long hincrBy(String key, String field, long value) {
		ShardedJedis redis = pool.getResource();
		long result = 1L;
		try {
			result = redis.hincrBy(key, field, value);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public List<byte[]> hmget(byte[] key, byte[]... fields) {
		ShardedJedis redis = pool.getResource();
		List<byte[]> result = null;
		try {
			result = redis.hmget(key, fields);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public List<String> hmget(String key, String... fields) {
		ShardedJedis redis = pool.getResource();
		List<String> result = null;
		try {
			result = redis.hmget(key, fields);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;

	}

	@Override
	public String hmset(byte[] key, Map<byte[], byte[]> hash) {
		ShardedJedis redis = pool.getResource();
		String result = null;
		try {
			result = redis.hmset(key, hash);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public String hmset(String key, Map<String, String> hash) {
		ShardedJedis redis = pool.getResource();
		String result = null;
		try {
			result = redis.hmset(key, hash);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public Long hset(byte[] key, byte[] field, byte[] value) {
		Long result = -1L;
		ShardedJedis redis = pool.getResource();
		try {
			result = redis.hset(key, field, value);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public Long hset(String key, String field, String value) {
		Long result = -1L;
		ShardedJedis redis = pool.getResource();
		try {
			result = redis.hset(key, field, value);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public Long incr(String key) {
		Long result = -1L;
		ShardedJedis redis = pool.getResource();
		try {
			result = redis.incr(key);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public Set<byte[]> keys(byte[] pattern) {
		ShardedJedis redis = pool.getResource();
		Set<byte[]> result = null;
		try {
			result = redis.hkeys(pattern);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public Set<String> keys(String pattern) {
		ShardedJedis redis = pool.getResource();
		Set<String> result = null;
		try {
			result = redis.hkeys(pattern);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public Long llen(String key) {
		ShardedJedis redis = pool.getResource();
		Long resutl = -1L;
		try {
			redis.llen(key);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return resutl;
	}

	@Override
	public Long lpush(byte[] key, byte[] value) {
		ShardedJedis redis = pool.getResource();
		Long resutl = -1L;
		try {
			redis.lpush(key, value);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return resutl;
	}

	@Override
	public Long lpush(String key, String value) {
		ShardedJedis redis = pool.getResource();
		Long resutl = -1L;
		try {
			redis.lpush(key, value);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return resutl;
	}

	@Override
	public Long lpush(String key, Object[] objs) {
		ShardedJedis redis = pool.getResource();
		ShardedJedisPipeline pipeline = redis.pipelined();
		Long resutl = -1L;
		try {
			for (Object obj : objs)
				redis.lpush(key, obj.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			pipeline.sync();
			this.close(redis);
		}
		return resutl;
	}

	@Override
	public List<byte[]> lrange(byte[] key, int start, int end) {
		ShardedJedis redis = pool.getResource();
		List<byte[]> result = new ArrayList<byte[]>();
		try {
			result = redis.lrange(key, start, end);
		} catch (Exception e) {

		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public List<String> lrange(String key, int start, int end) {
		ShardedJedis redis = pool.getResource();
		List<String> result = new ArrayList<String>();
		try {
			result = redis.lrange(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public List<Object> pipelined(ShardedJedisPipeline shardedJedisPipeline) {
		ShardedJedis redis = pool.getResource();
		List<Object> result = null;
		try {
			result = redis.pipelined(shardedJedisPipeline);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public Long rpush(byte[] key, byte[] value) {
		ShardedJedis redis = pool.getResource();
		Long result = -1L;
		try {
			result = redis.rpush(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public Long sadd(byte[] key, byte[] member) {
		ShardedJedis redis = pool.getResource();
		Long result = -1L;
		try {
			result = redis.sadd(key, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public Long sadd(String key, String member) {
		ShardedJedis redis = pool.getResource();
		Long result = -1L;
		try {
			result = redis.sadd(key, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public Long scard(byte[] key) {
		ShardedJedis redis = pool.getResource();
		Long result = -1L;
		try {
			result = redis.scard(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public Long scard(String key) {
		ShardedJedis redis = pool.getResource();
		Long result = -1L;
		try {
			result = redis.scard(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public void set(byte[] key, byte[] value) {
		ShardedJedis redis = pool.getResource();
		try {
			if (redis != null) {
				redis.set(key, value);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
	}

	@Override
	public void set(String key, String value) {

		ShardedJedis redis = pool.getResource();
		try {
			if (redis != null) {
				redis.set(key, value);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
	}

	@Override
	public String setex(byte[] key, int seconds, byte[] value) {
		ShardedJedis redis = pool.getResource();
		String result = "";
		try {
			if (redis != null) {
				result = redis.setex(key, seconds, value);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;

	}

	@Override
	public Long setnx(byte[] key, byte[] value) {
		ShardedJedis redis = pool.getResource();
		Long result = -1L;

		try {
			if (redis != null) {
				result = redis.setnx(key, value);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public long setnx(String key, String value) {
		ShardedJedis redis = pool.getResource();
		Long result = -1L;

		try {
			if (redis != null) {
				result = redis.setnx(key, value);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public Boolean sismember(byte[] key, byte[] member) {
		Boolean result = false;
		ShardedJedis redis = pool.getResource();
		try {
			if (redis != null) {
				result = redis.sismember(key, member);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;

	}

	@Override
	public Boolean sismember(String key, String member) {
		Boolean result = false;
		ShardedJedis redis = pool.getResource();
		try {
			if (redis != null) {
				result = redis.sismember(key, member);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public Set<byte[]> smembers(byte[] key) {
		Set<byte[]> result = null;
		ShardedJedis redis = pool.getResource();
		try {
			if (redis != null) {
				result = redis.smembers(key);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public Set<String> smembers(String key) {
		Set<String> result = null;
		ShardedJedis redis = pool.getResource();
		try {
			if (redis != null) {
				result = redis.smembers(key);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public List<String> sort(String key, int start, int pageSize) {
		List<String> result = null;
		ShardedJedis redis = pool.getResource();
		try {
			if (redis != null) {
				SortingParams sortingParameters = new SortingParams();
				sortingParameters.desc();
				sortingParameters.limit(start, pageSize);
				result = redis.sort(key, sortingParameters);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public byte[] spop(byte[] key) {
		byte[] result = null;
		ShardedJedis redis = pool.getResource();
		try {
			result = redis.spop(key);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public String spop(String key) {
		String result = null;
		ShardedJedis redis = pool.getResource();
		try {
			result = redis.spop(key);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public byte[] srandmember(byte[] key) {
		byte[] result = null;
		ShardedJedis redis = pool.getResource();
		try {
			result = redis.srandmember(key);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public String srandmember(String key) {
		String result = null;
		ShardedJedis redis = pool.getResource();
		try {
			result = redis.srandmember(key);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public Long srem(byte[] key, byte[] member) {
		Long result = -1L;
		ShardedJedis redis = pool.getResource();
		try {
			result = redis.srem(key);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public Long srem(String key, String member) {
		Long result = -1L;
		ShardedJedis redis = pool.getResource();
		try {
			result = redis.srem(key);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public void zadd(String key, double score, String member) {
		ShardedJedis redis = pool.getResource();
		try {
			Map<String, Double> scoreMembers = new HashMap<String, Double>();
			scoreMembers.put(member, score);
			redis.zadd(key, scoreMembers);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
	}

	@Override
	public Long zcard(byte[] key) {
		Long result = -1L;
		ShardedJedis redis = pool.getResource();
		try {
			result = redis.zcard(key);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public Long zcard(String key) {
		Long result = -1L;
		ShardedJedis redis = pool.getResource();
		try {
			result = redis.zcard(key);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public Long zcount(byte[] key, long min, long max) {
		Long result = -1L;
		ShardedJedis redis = pool.getResource();
		try {
			result = redis.zcount(key, min, max);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public Long zcount(String key, long min, long max) {
		Long result = -1L;
		ShardedJedis redis = pool.getResource();
		try {
			result = redis.zcount(key, min, max);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public Set<byte[]> zrange(byte[] key, int start, int end) {
		Set<byte[]> result = null;
		ShardedJedis redis = pool.getResource();
		try {
			result = redis.zrange(key, start, end);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public Set<String> zrange(String key, int start, int end) {
		Set<String> result = null;
		ShardedJedis redis = pool.getResource();
		try {
			result = redis.zrange(key, start, end);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public Set<byte[]> zrangeByScore(byte[] key, long min, long max) {
		Set<byte[]> result = null;
		ShardedJedis redis = pool.getResource();
		try {
			result = redis.zrangeByScore(key, min, max);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public Set<String> zrangeByScore(String key, long min, long max) {
		Set<String> result = null;
		ShardedJedis redis = pool.getResource();
		try {
			result = redis.zrangeByScore(key, min, max);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public Set<Tuple> zrangeWithScores(byte[] key, int start, int end) {

		Set<Tuple> result = null;
		ShardedJedis redis = pool.getResource();
		try {
			result = redis.zrangeWithScores(key, start, end);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;

	}

	@Override
	public Set<Tuple> zrangeWithScores(String key, int start, int end) {

		Set<Tuple> result = null;
		ShardedJedis redis = pool.getResource();
		try {
			result = redis.zrangeWithScores(key, start, end);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public Set<Tuple> zrevRangeWithScores(String key, int start, int end) {

		Set<Tuple> result = null;
		ShardedJedis redis = pool.getResource();
		try {
			result = redis.zrevrangeWithScores(key, start, end);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public long zrem(byte[] key, byte[] member) {
		long result = -1L;
		ShardedJedis redis = pool.getResource();
		try {
			result = redis.zrem(key, member);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;

	}

	@Override
	public long zrem(String key, String member) {

		long result = -1L;
		ShardedJedis redis = pool.getResource();
		try {
			result = redis.zrem(key, member);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public Long zremrangeByScore(byte[] key, double start, double end) {
		Long result = -1L;
		ShardedJedis redis = pool.getResource();
		try {
			result = redis.zremrangeByScore(key, start, end);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public Long zremrangeByScore(String key, double start, double end) {
		Long result = -1L;
		ShardedJedis redis = pool.getResource();
		try {
			result = redis.zremrangeByScore(key, start, end);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public Long zrank(byte[] key, byte[] member) {
		Long result = -1L;
		ShardedJedis redis = pool.getResource();
		try {
			result = redis.zrank(key, member);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public Long zrevrank(byte[] key, byte[] member) {
		Long result = -1L;
		ShardedJedis redis = pool.getResource();
		try {
			result = redis.zrevrank(key, member);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.close(redis);
		}
		return result;
	}

	@Override
	public void flushall() {
		

	}

	@Override
	public String select(int index) {
		return null;
	}

	@Override
	public List<Object> pipelined(Pipeline pipeline) {
	return null;
	}

	@Override
	public void mset(String... strings) {

	}

}
