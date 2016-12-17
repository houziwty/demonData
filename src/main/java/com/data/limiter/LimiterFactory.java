package com.data.limiter;

import java.util.concurrent.ConcurrentHashMap;

public class LimiterFactory {
private static LimiterFactory instance=new LimiterFactory();
private static final ConcurrentHashMap<String,Limiter>limiters=new ConcurrentHashMap<String, Limiter>();
}
