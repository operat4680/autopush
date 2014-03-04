package kr.co.autopush.db;




import java.util.ArrayList;
import java.util.List;

import kr.co.autopush.bean.PushData;
import kr.co.autopush.constant.Constant;
import kr.co.autopush.tabledetection.APDataProcessor;

import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;


public class MongoDAO {
	

	public static boolean insertTempData(DBObject obj){
		DB db = MongoDB.INSTANCE.getDB();
		DBCollection coll = db.getCollection("temp");
		String userId = obj.get("userId").toString();
		obj.removeField("userId");
		obj.put("userId", new ObjectId(userId));
		//coll.remove(new BasicDBObject("", ""));
		coll.remove(new BasicDBObject("userId",new ObjectId(userId)));
		coll.insert(obj);
		return true;
	}
	public static DBObject getTempData(String id){
		DB db = MongoDB.INSTANCE.getDB();
		DBCollection coll = db.getCollection("temp");
		DBObject obj= coll.findOne(new BasicDBObject("userId",new ObjectId(id)));
		return obj;
	}
	public static boolean updateTempData(DBObject obj){
		DB db = MongoDB.INSTANCE.getDB();
		DBCollection coll = db.getCollection("temp");
		String userId = obj.get("userId").toString();
		obj.removeField("userId");
		BasicDBObject query = new BasicDBObject("userId",new ObjectId(userId));
		BasicDBObject update = new BasicDBObject("$set",obj);
		WriteResult wr = coll.update(query, update);
		return true;
	}
	public static DBObject getTempImage(String id){
		DB db = MongoDB.INSTANCE.getDB();
		DBCollection coll = db.getCollection("temp");
		BasicDBObject query = new BasicDBObject();
		BasicDBObject find = new BasicDBObject("userId",new ObjectId(id));
		query.append("imgList", 1);
		DBObject obj= coll.findOne(find,query);
//		DBObject obj= coll.findOne();
		return obj;
	}
	public static boolean insertTask(DBObject obj) throws JSONException{
		String html="";
		String url = obj.get("url").toString();
		String tagPath =obj.get("tagPath").toString();
		DB db = MongoDB.INSTANCE.getDB();
		DBCollection getColl = db.getCollection("temp");
		DBCollection coll = db.getCollection("task");
		JSONObject id = new JSONObject(obj.get("_id").toString());
		BasicDBObject query = new BasicDBObject("_id",new ObjectId(id.get("$oid").toString()));
		DBObject temp = getColl.findOne(query,new BasicDBObject("imgList",0));
		BasicDBObject in = new BasicDBObject("url",url).append("tagPath", tagPath);
		BasicDBObject selector = new BasicDBObject("$elemMatch",in);
		DBObject match = getColl.findOne(query,new BasicDBObject("imgList",selector));
		BasicDBList img= (BasicDBList) match.get("imgList");
		BasicDBList htmlList = (BasicDBList) temp.get("htmlData");
		for(int i=0;i<htmlList.size();i++){
			DBObject htmlData = (DBObject)htmlList.get(i);
			if((htmlData.get("url").toString()).equals(url)){
				html = htmlData.get("html").toString();
				break;
			}	
		}
		APDataProcessor p = new APDataProcessor(html, tagPath);
		List<String> textList = p.getLines();
		BasicDBObject insert = new BasicDBObject();
		//insert.put("html", html);
		insert.put("userId", new ObjectId(obj.get("userId").toString()));
		insert.put("targetUrl",url );
		insert.put("tagPath",tagPath);
		insert.put("name", obj.get("name").toString());
		insert.put("imgData", ((BasicDBObject)img.get(0)).get("imgData"));
		insert.put("remainTime", 5);
		insert.put("limitTime", 5);
		insert.put("isNew", false);
		insert.put("error", 0);
		insert.put("push", true);
		insert.put("textList", textList);
		if(temp.get("loginURL")!=null){
			insert.put("loginURL",temp.get("loginURL").toString());
			insert.put("formPath",temp.get("formPath").toString());
			insert.put("idPath",temp.get("idPath").toString());
			insert.put("passwdPath",temp.get("passwdPath").toString());
			insert.put("id",temp.get("id").toString());
			insert.put("pwd",temp.get("pwd").toString());
			insert.put("pPath", temp.get("pPath"));
		}
		coll.insert(insert);
		return true;
	}
	public static boolean isTaskData(){
		DB db = MongoDB.INSTANCE.getDB();
		DBCollection coll = db.getCollection("task");
		DBObject obj = coll.findOne(new BasicDBObject());
		if(obj==null){
			return false;
		}
		return true;
	}
	public static boolean updateTaskTime(){
		DB db = MongoDB.INSTANCE.getDB();
		DBCollection coll = db.getCollection("task");
		BasicDBObject in  = new BasicDBObject("remainTime",-1);
		BasicDBObject query = new BasicDBObject("$inc",in);
		coll.update(new BasicDBObject("remainTime",new BasicDBObject("$gte",0)), query,false,true);
		return true;
	}
	
	public static List<DBObject> taskTimeCheck(){
		DB db = MongoDB.INSTANCE.getDB();
		DBCollection coll = db.getCollection("task");
		BasicDBList num = new BasicDBList();
		num.add(0);
		BasicDBObject in = new BasicDBObject("$in",num);
		BasicDBObject query = new BasicDBObject("remainTime",in).append("error", new BasicDBObject("$lte",3));
		DBCursor cursor = coll.find(query,new BasicDBObject("_id",1));
		List<DBObject> list = cursor.toArray();
		return list;
	}
	public static boolean insertQueue(List<DBObject> queueList){
		List<DBObject> list = new ArrayList<DBObject>(queueList);
		DB db = MongoDB.INSTANCE.getDB();
		DBCollection coll = db.getCollection("queue");
		int realSize=0;
		BasicDBObject query = new BasicDBObject("state","open");
		DBObject obj = coll.findAndModify(query,new BasicDBObject("size",1),null,false,new BasicDBObject("$set",new BasicDBObject("state","lock")),false,false);
		if(obj!=null&&obj.get("size")!=null){
			System.out.println("inserQueue class : update Part");
			int emptySize = Constant.QUEUEMAXSIZE-Integer.parseInt(obj.get("size").toString());
			realSize = list.size()-emptySize;
			List<DBObject> objlist = new ArrayList<DBObject>();
			if(realSize>=0){
				for(int i=0;i<emptySize;i++){
					objlist.add(list.remove(0));
					
				}
				BasicDBObject in = new BasicDBObject("jobList",objlist);
				BasicDBObject update = new BasicDBObject("$set",new BasicDBObject("state","full")).append("$pushAll",in).append("$inc",new BasicDBObject("size",objlist.size()));
				coll.update(new BasicDBObject("_id",new ObjectId(obj.get("_id").toString())),update);

			}
			
			else{
				BasicDBObject in = new BasicDBObject("jobList",list);
				BasicDBObject update = new BasicDBObject("$pushAll",in).append("$inc",new BasicDBObject("size",list.size())).append("$set",new BasicDBObject("state","open"));
				coll.update(new BasicDBObject("_id",new ObjectId(obj.get("_id").toString())),update);
			}
		}
		
		if(realSize>=0&&list.size()>0){
			System.out.println("inserQueue class : insert Part");
			int size=list.size();
			int num = (size/(Constant.QUEUEMAXSIZE));
			for(int i=0;i<num;i++){
				List<DBObject> objlist = new ArrayList<DBObject>();
				for(int j=0;j<Constant.QUEUEMAXSIZE;j++){
					objlist.add(list.remove(0));
				}
				BasicDBObject data = new BasicDBObject("state","full").append("size", objlist.size()).append("jobList", objlist);
				coll.insert(data);
			}
			int remain = (size%Constant.QUEUEMAXSIZE);
			if(remain!=0){
				List<DBObject> objlist = new ArrayList<DBObject>();
				for(int j=0;j<remain;j++){
					objlist.add(list.remove(0));
				}
				BasicDBObject data = new BasicDBObject("state","open").append("size", objlist.size()).append("jobList", objlist);
				coll.insert(data);
			}
		}
		//timeReset
		
		return true;
	}
	public static List<DBObject> checkQueue(){
		DB db = MongoDB.INSTANCE.getDB();
		DBCollection coll = db.getCollection("queue");
		BasicDBObject query = new BasicDBObject("state",new BasicDBObject("$ne","close")).append("state", new BasicDBObject("$ne","lock"));
//		BasicDBObject query = new BasicDBObject(new BasicDBObject());
		DBCursor cursor = coll.find(query); 
		List<DBObject> list = (List<DBObject>)cursor.toArray();
		return list;
	}
	
	public static BasicDBList getTaskDataList(String queueId){
		DB db = MongoDB.INSTANCE.getDB();
		DBCollection coll = db.getCollection("queue");
		DBCollection task = db.getCollection("task");
		DBObject obj = coll.findAndModify(new BasicDBObject("_id",new ObjectId(queueId)),new BasicDBObject("$set",new BasicDBObject("state","close")));
		BasicDBList taskList = (BasicDBList)obj.get("jobList");
		System.out.println(taskList.toString());
		BasicDBList list = new BasicDBList();
		for(int i=0;i<taskList.size();i++){
			BasicDBObject dbo  = (BasicDBObject)taskList.get(i);
			list.add(task.findOne(new BasicDBObject("_id",dbo.get("_id")),new BasicDBObject("imgData",0)));
		}
		return list;
		
	}
	public static DBObject getTaskListById(String id){
		DB db = MongoDB.INSTANCE.getDB();
		DBCollection task = db.getCollection("task");
		BasicDBObject selector = new BasicDBObject("imgData",1).append("error", 1).append("isNew",1).append("name", 1);
		DBCursor cursor = task.find(new BasicDBObject("userId",new ObjectId(id)),selector);
		List<DBObject> list = (List<DBObject>)cursor.toArray();
		DBObject result = new BasicDBObject("taskList",list);
		return result;
	}
	public static boolean deleteQueueById(String id){
		DB db = MongoDB.INSTANCE.getDB();
		DBCollection coll = db.getCollection("queue");
		BasicDBObject query = new BasicDBObject("_id",new ObjectId(id));
		coll.remove(query);
		return true;
	}

//	public static boolean syncedQueue(){
//		DB db = MongoDB.INSTANCE.getDB();
//		DBCollection coll = db.getCollection("queue");
//		BasicDBObject in = new BasicDBObject("$gte",Constant.QUEUEMAXSIZE);
//		BasicDBObject query = new BasicDBObject("size",in);
//		BasicDBObject update = new BasicDBObject("state","full");
//		coll.update(query, update, false, true);
//		return true;
//	}

	public static void updateTaskError(BasicDBList errorList) {
		DB db = MongoDB.INSTANCE.getDB();
		DBCollection task = db.getCollection("task");
		for(int i=0;i<errorList.size();i++){
			DBObject obj = (DBObject)errorList.get(i);
			DBObject update = new BasicDBObject("$inc",new BasicDBObject("error",1)).append("$set",new BasicDBObject("remainTime",1));
			task.update(new BasicDBObject("_id",new ObjectId(obj.get("id").toString())),update);
		}
		
	}
	public static void timeReset(BasicDBList list) {
		if(list!=null&&list.size()>0){
			DB db = MongoDB.INSTANCE.getDB();
			DBCollection task = db.getCollection("task");
			for(int i=0;i<list.size();i++){
				DBObject obj = (DBObject)list.get(i);
				DBObject query = new BasicDBObject("_id",new ObjectId(obj.get("id").toString()));
				DBObject t = task.findOne(query,new BasicDBObject("limitTime",1));
				if(t!=null){
					int time = Integer.parseInt(t.get("limitTime").toString());
					DBObject update=null;
//					if(obj.get("textList")!=null){
//						
//						update = new BasicDBObject("$set",in);
//					}
//					else{
//						update = new BasicDBObject("$set",new BasicDBObject("remainTime",time));
//					}
					
					BasicDBObject in;
					in = new BasicDBObject("imgData",obj.get("imgData").toString()).append("textList", obj.get("textList")).append("remainTime",time);
					update = new BasicDBObject("$set",in);
					if(Integer.parseInt(obj.get("textstatus").toString())==1){
						in.append("isNew", true);
					}
					task.update(query,update);
				}
			}
		}
	}
	public static boolean insertUser(DBObject obj){
		DB db = MongoDB.INSTANCE.getDB();
		DBCollection task = db.getCollection("user");
		DBObject find = task.findOne(new BasicDBObject("userId",obj.get("userId").toString()));
		if(find!=null){
			return false;
		}
		else{
			task.insert(obj);
		}
		
		return true;
	}
	public static String checkUser(DBObject userInfo) {
		DBObject query = userInfo;
		DB db = MongoDB.INSTANCE.getDB();
		DBCollection task = db.getCollection("user");
		DBObject obj = task.findOne(query);
		if(obj!=null){
			System.out.println(obj.get("_id").toString());
			return obj.get("_id").toString();
		}
		else{
			return null;
		}
		
	}
	public static String checkGcmKey(DBObject query) {
		DB db = MongoDB.INSTANCE.getDB();
		DBCollection task = db.getCollection("user");
		DBObject obj = task.findOne(query);
		if(obj!=null){
			System.out.println(obj.get("_id").toString());
			return obj.get("_id").toString();
		}
		else{
			return null;
		}
		
	}
	public static String updateGcmKey(DBObject object){
		DB db = MongoDB.INSTANCE.getDB();
		DBCollection task = db.getCollection("user");
		DBObject query = new BasicDBObject("_id",new ObjectId(object.get("id").toString()));
		object.removeField("id");
		DBObject update = new BasicDBObject("$set",object);
		DBObject obj = task.findAndModify(query, update);
		if(obj!=null){
			System.out.println(obj.get("_id").toString());
			return obj.get("_id").toString();
		}
		else{
			return null;
		}
	}
	public static void deleteTaskById(String taskId) {
		DB db = MongoDB.INSTANCE.getDB();
		DBCollection task = db.getCollection("task");
		DBObject query = new BasicDBObject("_id",new ObjectId(taskId));
		task.remove(query);
	}
	public static DBObject getTaskDataById(String id) {
		// TODO Auto-generated method stub
		DB db = MongoDB.INSTANCE.getDB();
		DBCollection task = db.getCollection("task");
		DBObject obj = task.findAndModify(new BasicDBObject("_id",new ObjectId(id)),new BasicDBObject("$set",new BasicDBObject("isNew","false")));
		if(obj==null){
			return null;
		}
		else{
			return obj;
		}
		
	}
	public static void updateTask(DBObject json) {
		DB db = MongoDB.INSTANCE.getDB();
		DBCollection task = db.getCollection("task");
		DBObject query = new BasicDBObject("_id",new ObjectId(json.get("id").toString()));
		json.removeField("id");
		DBObject time = task.findOne(query,new BasicDBObject("remainTime",1));
		int t = Integer.parseInt(time.get("remainTime").toString());
		int limit = Integer.parseInt(json.get("limitTime").toString());
		if(t>limit){
			json.put("remainTime", limit);
		}
		DBObject update = new BasicDBObject("$set",json);
		task.update(query, update);
		
	}
	public static List<String> getTextList(String id) {
		DB db = MongoDB.INSTANCE.getDB();
		DBCollection task = db.getCollection("task");
		DBObject query = new BasicDBObject("_id",new ObjectId(id));
		DBObject result = task.findOne(query,new BasicDBObject("textList",1));
		BasicDBList list = (BasicDBList)result.get("textList");
		List<String> resultList = new ArrayList<String>();
		for(int i=0;i<list.size();i++){
			resultList.add((String)list.get(i));
		}
		return resultList;
		
	}
	public static List<String> getKeyList(String id){
		DB db = MongoDB.INSTANCE.getDB();
		DBCollection task = db.getCollection("task");
		DBObject query = new BasicDBObject("_id",new ObjectId(id));
		DBObject result = task.findOne(query,new BasicDBObject("keywordList",1));
		BasicDBList list = (BasicDBList)result.get("keywordList");
		List<String> resultList = new ArrayList<String>();
		if(list!=null){
			for(int i=0;i<list.size();i++){
				resultList.add((String)list.get(i));
			}
		}
		return resultList;
	}
	public static PushData getPushById(String id) {
		DB db = MongoDB.INSTANCE.getDB();
		DBCollection task = db.getCollection("task");
		PushData push = new PushData();
		DBObject query = new BasicDBObject("_id",new ObjectId(id)).append("push",true);
		DBObject result = task.findOne(query,new BasicDBObject("userId",1).append("_id", 0).append("name", 1));
		if(result!=null){
			push.setName(result.get("name").toString());
			result.removeField("name");
			task = db.getCollection("user");
			System.out.println("");
			query = new BasicDBObject("_id",new ObjectId(result.get("userId").toString()));
			DBObject user = task.findOne(query,new BasicDBObject("_id",0).append("userId", 1).append("gcmId", 1));
			if(user!=null){
				push.setUserId(user.get("userId").toString());
				push.setGcmKey(user.get("gcmId").toString());
			}
			return push;
		}
		else{
			return null;
		}
		
	}
	
}
