package com.data.mongodb;

public interface MongoCacheClient {
	void createCollection(String name);

	void dropCollection(String name);
	
	void insert(String name,Object o);

	void save(Object o);

	<T> T findOne();

}
