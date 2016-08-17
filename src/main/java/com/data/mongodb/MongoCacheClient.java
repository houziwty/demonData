package com.data.mongodb;

import java.util.List;

import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;

public interface MongoCacheClient {
	void createCollection(String name);

	void dropCollection(String name);
	
	void insertOne(String name,Object o);
	
	void insertMany(String name,List<Object> o);

	void save(Object o);

	<T> T findOne(String name,BasicDBObject filter,Class<?> classz);

}
