package kr.co.autopush.util;

import org.json.JSONObject;



import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class JsonUtil {


	public static DBObject tansferDBObject(JSONObject json){
		Object object = JSON.parse(json.toString());
		DBObject obj = (DBObject)object;
		return obj;
	}
}
