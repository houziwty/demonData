package com.data.redis;

import java.util.HashMap;
import java.util.Map;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
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

    public Integer getBlockingAfterLockNum() {
        return blockingAfterLockNum;
    }

    public long getParkThreadMilliscond() {
        return parkThreadMilliscond;
    }

    /**
     * 不可中断，直到获取锁为止
     *
     * @param key             redis的key值
     * @param keyExpireSecond redis key的有效期
     */
    public void lock(String key, int keyExpireSecond) throws InterruptedException {
        if (isBlank(key) || keyExpireSecond < 0) {
            throw new IllegalArgumentException("param is illegal");
        }
        boolean isSaveLock = false;
        RedisLockThread lockThread = null;
        if (fair) {//公平锁
            lockThread = saveLock(key);//保证一个key对应一个lock
            lockThread.getLock().lock();//线程获取锁
            isSaveLock = true;
        }
        int doNum = 0;
        do {
            long setNx = redisClient.setnx(key, String.valueOf(System.currentTimeMillis()));
            if (setNx == 1) {
                redisClient.expire(key, keyExpireSecond);
                return;
            } else {
                if (!isSaveLock) {
                    lockThread = saveLock(key);//保证一个key对应一个lock
                    lockThread.getLock().lock();//线程获取锁
                    isSaveLock = true;
                }
                doNum++;
                if (doNum == blockingAfterLockNum) {//获取锁几次后，让然获取不到锁，则挂起线程指定的时间。
                    doNum = 0;
                    LockSupport.parkNanos(Thread.currentThread(), parkThreadNano);
                }
            }
        } while (true);
    }

    private boolean isBlank(CharSequence key) {
        int strLen;
        if (key == null || (strLen = key.length()) == 0) return true;
        for (int i = 0; i < strLen; i++)
            if (!Character.isWhitespace(key.charAt(i)))
                return false;
        return true;
    }

    /**
     * 如果当前线程未被中断，则直到获取锁为止
     *
     * @param key             redis的key值
     * @param keyExpireSecond redis key的有效期
     * @throws InterruptedException
     */
    public void lockInterruptibly(String key, int keyExpireSecond) throws InterruptedException {
        if (isBlank(key) || keyExpireSecond < 0) {
            throw new IllegalArgumentException("param is illegal");
        }
        boolean isSaveLock = false;
        RedisLockThread lockThread = null;
        if (fair) {//公平锁
            lockThread = saveLockInterruptibly(key);//保证一个key对应一个lock
            lockThread.getLock().lockInterruptibly();//线程获取锁
            isSaveLock = true;
        }
        int doNum = 0;
        do {
            long setNx = redisClient.setnx(key, String.valueOf(System.currentTimeMillis()));
            if (setNx == 1) {
                redisClient.expire(key, keyExpireSecond);
                return;
            } else {
                if (!isSaveLock) {
                    lockThread = saveLockInterruptibly(key);//保证一个key对应一个lock
                    lockThread.getLock().lockInterruptibly();//线程获取锁
                    isSaveLock = true;
                }
                doNum++;
                if (doNum == blockingAfterLockNum) {//获取锁几次后，让然获取不到锁，则挂起线程指定的时间。
                    doNum = 0;
                    LockSupport.parkNanos(Thread.currentThread(), parkThreadNano);
                    if (Thread.interrupted()) {
                        throw new InterruptedException();
                    }
                }
            }
        } while (true);
    }

    /**
     * 如果锁可用，则获取锁，并立即返回值 true。如果锁不可用，则此方法将立即返回值 false。
     *
     * @param key             redis的key值
     * @param keyExpireSecond redis key的有效期
     * @return 如果获取了锁，则返回 true 否则返回 false。
     */
    public boolean tryLock(String key, int keyExpireSecond) {
        if (isBlank(key) || keyExpireSecond < 0) {
            throw new IllegalArgumentException("param is illegal");
        }
        long setNx = redisClient.setnx(key, String.valueOf(System.currentTimeMillis()));
        if (setNx == 1) {
            redisClient.expire(key, keyExpireSecond);
            return true;
        }
        return false;
    }

    /**
     * 如果锁可用，则此方法将立即返回值 true。
     * 如果锁不可用，出于线程调度目的，将挂起当前线程，
     * 如果在线程挂起期间中断线程，则排除 InterruptedException
     *
     * @param key             redis的key值
     * @param keyExpireSecond redis key的有效期
     * @param timeout
     * @param unit
     * @return
     * @throws InterruptedException
     */
    public boolean tryLock(String key, int keyExpireSecond, long timeout, TimeUnit unit) throws InterruptedException {
        long lastTime = System.nanoTime();
        long nanoTimeOut = unit.toNanos(timeout);
        if (isBlank(key) || keyExpireSecond < 0) {
            throw new IllegalArgumentException("param is illegal");
        }
        boolean isSaveLock = false;
        RedisLockThread lockThread = null;
        if (fair) {//公平锁
            lockThread = saveLockInterruptibly(key);
            boolean isLock = lockThread.getLock().tryLock(timeout, unit);
            if (!isLock) {//没有获取锁
                lockThread.removeThreadOfLock(Thread.currentThread());//删除lock对应的线程
                return isLock;
            } else {
                isSaveLock = true;
            }
        }
        int doNum = 0;//do{}while 循环多少次挂起线程
        do {
            long setNx = redisClient.setnx(key, String.valueOf(System.currentTimeMillis()));
            if (setNx == 1) {
                redisClient.expire(key, keyExpireSecond);
                return true;
            } else {
                if (isSaveLock) {//非公平锁
                    lockThread = saveLockInterruptibly(key);
                    boolean isLock = lockThread.getLock().tryLock(timeout, unit);
                    if (!isLock) {
                        lockThread.removeThreadOfLock(Thread.currentThread());//删除lock对应的线程
                        return isLock;
                    } else {
                        isSaveLock = true;
                    }
                }
                //超时
                if (nanoTimeOut <= 0) {
                    lockThread.removeThreadOfLock(Thread.currentThread());//删除lock对应的线程
                    lockThread.getLock().unlock();//lock释放锁
                    //删除 key 对应的锁
                    globalLock.lock();
                    if (lockThread.getThreadsOfLock() == 0) {
                        //lock对应的线程数为 0，从集合中删除 key对应的lock。
                        keyLockMap.remove(key);
                    }
                    globalLock.unlock();
                    return false;
                }
                doNum++;
                if (doNum == blockingAfterLockNum) {
                    doNum = 0;
                    long now = System.nanoTime();
                    nanoTimeOut -= now - lastTime;
                    LockSupport.parkNanos(Thread.currentThread(), nanoTimeOut);
                    if (Thread.interrupted()) {
                        throw new InterruptedException();
                    }
                }
            }
        } while (true);

    }

    /**
     * 释放锁
     *
     * @param key redis的key值
     */
    public void unlock(String key) {
        globalLock.lock();
        redisClient.del(key);//必须先删除redis的key
        RedisLockThread lockThread = keyLockMap.get(key);//获取key对应的lock
        if (lockThread != null) {
            if (Thread.currentThread() != lockThread.getThreadOfLock(Thread.currentThread())) {
                //当前线程是否在，lock对应的线程集合内
                throw new IllegalMonitorStateException();
            } else {
                lockThread.removeThreadOfLock(Thread.currentThread());//删除lock对应的线程
                lockThread.getLock().unlock();
                if (lockThread.getThreadsOfLock() == 0) {//lock对应的线程数为 0，从集合中删除 key对应的lock。
                    keyLockMap.remove(key);
                }
            }
        }
        globalLock.unlock();
    }

    /**
     * 保证一个key对应一个lock
     * key与lock是一对一的关系，lock与线程是一对多的关系
     *
     * @param key
     * @return
     * @throws InterruptedException
     */
    private RedisLockThread saveLock(String key) {
        globalLock.lock();//保证 saveLock 方法是原子操作
        //保证一个 key 只创建一个 lock
        RedisLockThread lockThread = keyLockMap.get(key);
        if (lockThread == null) {
            lockThread = new RedisLockThread(fair);
            lockThread.saveThreadOfLock(Thread.currentThread());//保存 lock 与 线程的关系
            keyLockMap.put(key, lockThread);
        }
        lockThread.saveThreadOfLock(Thread.currentThread());
        globalLock.unlock();
        return lockThread;
    }

    /**
     * 保证一个key对应一个lock
     * key与lock是一对一的关系，lock与线程是一对多的关系
     *
     * @param key
     * @return
     * @throws InterruptedException
     */
    private RedisLockThread saveLockInterruptibly(String key) throws InterruptedException {
        globalLock.lockInterruptibly();//保证 saveLockInterruptibly 方法是原子操作
        //保证一个 key 只创建一个 lock
        RedisLockThread lockThread = keyLockMap.get(key);
        if (lockThread == null) {
            lockThread = new RedisLockThread(fair);
            lockThread.saveThreadOfLock(Thread.currentThread());//保存 lock 与 线程的关系
            keyLockMap.put(key, lockThread);//保存 key 与  lock 的关系
        }
        lockThread.saveThreadOfLock(Thread.currentThread());//保存 lock 与 线程的关系
        globalLock.unlock();
        return lockThread;

    }

    /**
     * 调试使用
     *
     * @return
     */
    public String getContentOfKeyLockMap(String key) {
        StringBuilder str = new StringBuilder();
        str.append("keyLock count:").append(keyLockMap.size());

        RedisLockThread lockThread = keyLockMap.get(key);
        if(lockThread != null) {
            str.append(",keyLockInfo:").append(lockThread.toString());
        } else {
            str.append(",keyLockInfo:").append("no lock");
        }

        return str.toString();
    }

}
