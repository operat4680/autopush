package kr.co.autopush.db;




import java.util.Map;
import java.util.Set;

import org.bson.BSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;


public class MongoDAO {
	

	public static boolean insertTempData(DBObject obj){
		DB db = MongoDB.INSTANCE.getDB();
		DBCollection coll = db.getCollection("temp");
		//coll.remove(new BasicDBObject("", ""));
		coll.remove(new BasicDBObject());
		coll.insert(obj);
		return true;
	}
	public static DBObject getTempData(){
		DB db = MongoDB.INSTANCE.getDB();
		DBCollection coll = db.getCollection("temp");
		DBObject obj= coll.findOne();
		return obj;
	}
	public static boolean updateTempData(DBObject obj){
		DB db = MongoDB.INSTANCE.getDB();
		DBCollection coll = db.getCollection("temp");
		BasicDBObject update = new BasicDBObject("$set",obj);
		WriteResult wr = coll.update(coll.findOne(), update);
		return true;
	}
	public static DBObject getTempImage(){
		DB db = MongoDB.INSTANCE.getDB();
		DBCollection coll = db.getCollection("temp");
		
		BasicDBObject query = new BasicDBObject();
		query.append("imgList", 1);
		DBObject obj= coll.findOne(new BasicDBObject(),query);
//		DBObject obj= coll.findOne();
		return obj;
	}
	
	
	
	
	
}
