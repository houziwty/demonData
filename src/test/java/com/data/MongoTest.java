package com.data;

import java.util.ArrayList;
import java.util.List;

import com.data.mongodb.MongoDBCacheClient;

public class MongoTest {

	public static void main(String[] args) {

		MongoDBCacheClient client=new MongoDBCacheClient("127.0.0.1:27017|127.0.0.1:27017","11");
		
		client.createCollection("demo");
		MongoTest t=new MongoTest();
		
		List<Object> o=new ArrayList<Object>();
		o.add(t.setObj(1));
		o.add(t.setObj(2));
	client.insertMany("demo", o);
		
//		client.dropCollection("demo");
		
	}
	
	Object setObj(int i){
		UserModel u=new UserModel();
		u.setAge(20);
		u.setId(1);
		u.setName("test"+i);	
		return u;
	}
	 class UserModel {

		private int id;

		private int age;

		private String name;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	
	 
	 }

}
