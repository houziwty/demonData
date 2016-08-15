package com.data.mongodb;

public interface MongoCacheClient {
	void createCollection(String name);

	void dropDatabase(String name);

	void save(Object o);

	<T> T findOne();

}
