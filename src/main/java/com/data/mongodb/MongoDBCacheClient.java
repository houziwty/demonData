package com.data.mongodb;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDBCacheClient implements MongoCacheClient {

	private static MongoClient mongoClient;

	private static MongoClientOptions options;

	// private static DBCollection dbCollection;

	@SuppressWarnings("rawtypes")
	private static MongoCollection dbCollection;
	private static MongoDatabase mongoDatabase;
	static {
		options = MongoClientOptions.builder().connectionsPerHost(3000).threadsAllowedToBlockForConnectionMultiplier(10)
				.readPreference(ReadPreference.nearest()).build();
	}

	public MongoDBCacheClient(String servers, String dataBaseInfos) {
		String[] hosts = servers.split("\\|");
		List<ServerAddress> mgAddress = new ArrayList<>();
		boolean isPwd = false;
		MongoCredential mongoCredential;
		String[] databaseInfo = dataBaseInfos.split(":");
		String database;

		database = databaseInfo[0];
		if (databaseInfo.length > 1) {
			String userName = databaseInfo[1];
			String password = databaseInfo[2];
			mongoCredential = MongoCredential.createMongoCRCredential(userName, database, password.toCharArray());
			isPwd = true;
		}

		for (String host : hosts) {
			String[] address = host.split(":");
			ServerAddress serverAddress = new ServerAddress(address[0], Integer.parseInt(address[1]));
			mgAddress.add(serverAddress);
		}
		if (true) {
		}
		mongoClient = new MongoClient(mgAddress);

		mongoDatabase = mongoClient.getDatabase(database);
		// DB db=mongoClient.getDB(database);
	}

	@Override
	public void createCollection(String name) {
		mongoDatabase.createCollection(name);
	}

	@Override
	public void dropCollection(String name) {
		dbCollection = mongoDatabase.getCollection(name);
		dbCollection.drop();
	}

	@Override
	public void save(Object o) {
	}

	@Override
	public <T> T findOne() {
		T result=(T) dbCollection.find();
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void insertOne(String name, Object o) {
		dbCollection = mongoDatabase.getCollection(name);
		dbCollection.insertOne(o);

	}

	@Override
	public void insertMany(String name, List<Object> o) {
		dbCollection = mongoDatabase.getCollection(name);
		dbCollection.insertMany(o);
	}

}
