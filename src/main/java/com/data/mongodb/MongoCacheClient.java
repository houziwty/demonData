package com.data.mongodb;

public interface MongoCacheClient {
	void useDB(String db);

	void dropDatabase(String db);

}
