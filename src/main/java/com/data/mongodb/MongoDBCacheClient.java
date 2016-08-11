package com.data.mongodb;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;

public class MongoDBCacheClient implements MongoCacheClient {

	private static MongoClient mongoClient;

	private static MongoClientOptions options;
	
	static{
		options= MongoClientOptions.builder()
            .connectionsPerHost(3000)
            .threadsAllowedToBlockForConnectionMultiplier(10)
            .readPreference(ReadPreference.nearest())
            .build();
	}
	
	public MongoDBCacheClient(String servers, String app) {
		String[] hosts = servers.split("\\|");
		List<ServerAddress> mgAddress = new ArrayList<>();
		String[] databaseInfo = app.split(":");
		String userName = databaseInfo[0];
		String password = databaseInfo[1];
		String database = databaseInfo[2];
		MongoCredential mongoCredential = MongoCredential.createMongoCRCredential(userName, database,
				password.toCharArray());
		for (String host : hosts) {
			String[] address = host.split(":");
			ServerAddress serverAddress = new ServerAddress(address[0], Integer.parseInt(address[1]));
			mgAddress.add(serverAddress);
		}
		mongoClient = new MongoClient(mgAddress);
	}

}
