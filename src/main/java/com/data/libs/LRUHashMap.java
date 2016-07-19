package com.data.libs;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class LRUHashMap<K, V> extends LinkedHashMap<K, V> {

	/**
	 * @Fields serialVersionUID :
	 */
	private static final long serialVersionUID = -7870008264556171817L;

	private ReentrantLock lock = new ReentrantLock();
	private int maxSize;
	private F.Action2<K, V> onEvict;

	public LRUHashMap(int maxSize, F.Action2<K, V> onEvict) {
		super(16, 0.75f, true);
		this.maxSize = maxSize;
	}

	int getMaxSize() {
		return maxSize;
	}

	void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	@Override
	protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
		boolean ret = size() > maxSize;
		if (onEvict != null) {
			onEvict.invoke(eldest.getKey(), eldest.getValue());
		}
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public LRUHashMap<K, V>clone(){
		return (LRUHashMap<K, V>) super.clone();
	}
	@Override
	public V put(K key,V value){
		lock.lock();
		try{
			return super.put(key, value);
		}finally{
			lock.unlock();
		}
	}
	@Override
	public V get(Object key){
		lock.lock();
		try{
			return super.get(key);
		}finally{
			lock.unlock();
		}
	}
	@Override
	public V remove(Object key) {
		lock.lock();
		try{
			return super.remove(key);
		}finally{
			lock.unlock();
		}
	}

}
