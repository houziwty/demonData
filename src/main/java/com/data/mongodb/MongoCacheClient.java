package com.data.mongodb;

import java.util.List;

public interface MongoCacheClient {
	void createCollection(String name);

	void dropCollection(String name);
	
	void insertOne(String name,Object o);
	
	void insertMany(String name,List<Object> o);

	void save(Object o);

	<T> T findOne();

}
