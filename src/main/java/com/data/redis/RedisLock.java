package com.data.redis;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Demon on 2017/4/18.
 */
public class RedisLock {
    private ReentrantLock globalLock;
    private Map<String,RedisLockThread>keyLockMap=new HashMap<String, RedisLockThread>();
}
