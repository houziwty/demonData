package com.data.redis;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Demon on 2017/4/18.
 * redis的一个key，对应一个 RedisLockThread 实例对象
 */
public class RedisLockThread {
    private ReentrantLock lock;

    private volatile Map<Thread, Thread> exclusiveOwnerThread = new HashMap<Thread, Thread>();


    public RedisLockThread(boolean fair) {
        this.lock = new ReentrantLock(fair);
    }

    protected ReentrantLock getLock() {
        return lock;
    }

    protected void saveThreadOfLock(Thread thread) {
        exclusiveOwnerThread.put(thread, thread);
    }

    protected Thread getThreadOfLock(Thread thread) {
        return exclusiveOwnerThread.get(thread);
    }

    protected void removeThreadOfLock(Thread thread) {
        exclusiveOwnerThread.remove(thread);
    }

    protected int getThreadsOfLock() {
        return exclusiveOwnerThread.size();
    }

    @Override
    public String toString() {
        StringBuffer str = new StringBuffer();
        str.append(" thread count of keyLock=").append(exclusiveOwnerThread.size());
        str.append(",keyLock=").append(lock.toString()).append(" ");
        return str.toString();
    }



}
