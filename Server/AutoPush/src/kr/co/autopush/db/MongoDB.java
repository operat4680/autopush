package kr.co.autopush.db;

import java.net.UnknownHostException;

import kr.co.autopush.constant.Constant;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;

public enum MongoDB {
    INSTANCE;

    private Mongo mongo;
    private DB db;

    MongoDB() {

        MongoClientOptions options = MongoClientOptions.builder()
                .connectionsPerHost(100)
                .autoConnectRetry(true)
                .readPreference(ReadPreference.secondaryPreferred())
                .build();

        try {
		    mongo = new MongoClient(Constant.SERVERIP,options);
			db = mongo.getDB("db");
			db.authenticate(Constant.DBID, Constant.DBPASSWD.toCharArray());
		} catch (UnknownHostException e) {

			e.printStackTrace();
		}
        db = mongo.getDB("db");

    }

    public DB getDB() {
        return db;
    }

    public void close(){
        mongo.close();
    }
}