package com.data.redis;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Demon on 2017/4/18.
 */
public class RedisLock {
    private ReentrantLock globalLock;

    /**
     * redis的一个key，对应一个 RedisLockThread
     */
    private Map<String, RedisLockThread> keyLockMap = new HashMap<String, RedisLockThread>();

    private RedisClusterCacheClient redisClient;

    /**
     * 默认值 false
     * true，公平锁； false，非公平锁
     */
    private boolean fair;

    /**
     * 尝试获取多少次锁后，如果还没有获取锁则挂起线程，默认值 2
     */
    private Integer blockingAfterLockNum = 2;

    /**
     * 参数只对方法 lock，lockInterruptibly 有效
     * 线程挂起时间，默认值 10毫秒
     */
    private long parkThreadMilliscond = 10;

    /**
     * 挂起线程的纳秒数
     */
    private long parkThreadNano = parkThreadMilliscond * 1000 * 1000;

    public RedisLock(RedisClusterCacheClient redisClient) {
        this.redisClient = redisClient;
        this.globalLock = new ReentrantLock(fair);
    }

    public RedisLock(boolean fair, int blockingAfterLockNum, long parkThreadMilliscond, RedisClusterCacheClient redisClient) {
        this.fair = fair;
        this.blockingAfterLockNum = blockingAfterLockNum;
        this.parkThreadMilliscond = parkThreadMilliscond;
        this.parkThreadNano = parkThreadMilliscond * 1000 * 1000;
        this.redisClient = redisClient;
        this.globalLock = new ReentrantLock(fair);
    }

    public boolean isFair() {
        return fair;
    }

    public RedisClusterCacheClient getRedisClient() {
        return redisClient;
    }

    public Integer getBlockingAfterLockNum(){
        return blockingAfterLockNum;
    }
    public long getParkThreadMilliscond() {
        return parkThreadMilliscond;
    }

    /**
     * 不可中断，直到获取锁为止
     * @param key redis的key值
     * @param keyExpireSecond redis key的有效期
     */
    public void lock(String key, int keyExpireSecond){

    }

    /**
     * 如果当前线程未被中断，则直到获取锁为止
     * @param key redis的key值
     * @param keyExpireSecond redis key的有效期
     * @throws InterruptedException
     */
    public void lockInterruptibly(String key, int keyExpireSecond) throws InterruptedException  {

    }

    /**
     * 如果锁可用，则获取锁，并立即返回值 true。如果锁不可用，则此方法将立即返回值 false。
     * @param key redis的key值
     * @param keyExpireSecond redis key的有效期
     * @return 如果获取了锁，则返回 true 否则返回 false。
     */
    public boolean tryLock(String key, int keyExpireSecond) {
        return false;
    }

    /**
     * 如果锁可用，则此方法将立即返回值 true。
     * 如果锁不可用，出于线程调度目的，将挂起当前线程，如果在线程挂起期间中断线程，则排除 InterruptedException
     * @param key redis的key值
     * @param keyExpireSecond redis key的有效期
     * @param timeout
     * @param unit
     * @return
     * @throws InterruptedException
     */
    public boolean tryLock(String key, int keyExpireSecond, long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }

    /**
     * 释放锁
     * @param key redis的key值
     */
    public void unlock(String key) {}

    /**
     * 保证一个key对应一个lock
     * key与lock是一对一的关系，lock与线程是一对多的关系
     * @param key
     * @return
     * @throws InterruptedException
     */
    private RedisLockThread saveLock(String key) {
        return null;
    }

    /**
     * 保证一个key对应一个lock
     * key与lock是一对一的关系，lock与线程是一对多的关系
     * @param key
     * @return
     * @throws InterruptedException
     */
    private RedisLockThread saveLockInterruptibly(String key) throws InterruptedException{
        return null;

    }

    /**
     * 调试使用
     * @return
     */
    public String getContentOfKeyLockMap(String key){
        return null;
    }

}
