package kr.co.autopush.bean;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class TaskOption {
	private String id;
	private String name;
	private Bitmap imageData;
	private String targetUrl;
	private int limitTime;
	private boolean push;
	private JSONArray keywordList;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TaskOption(){
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Bitmap getImageData() {
		return imageData;
	}

	public void setImageData(String img) {
		byte[] decodedString = Base64.decode(img, Base64.DEFAULT);
		BitmapFactory.Options option = new BitmapFactory.Options();
		option.inSampleSize = 4;
		option.inPurgeable=true;
		option.inDither= true;
		Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
		this.imageData = bitmap;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public int getLimitTime() {
		return limitTime;
	}

	public void setLimitTime(int limitTime) {
		this.limitTime = limitTime;
	}
	public void setLimitTimeByItem(int i){
		if(i==0){
			this.limitTime=5;
		}
		else if(i==1){
			this.limitTime=10;
		}
		else if(i==2){
			this.limitTime=60;
		}
		else if(i==3){
			this.limitTime=360;
		}
		else if(i==4){
			this.limitTime=720;
		}
		else{
			this.limitTime=1440;
		}
	}

	public boolean isPush() {
		return push;
	}

	public void setPush(boolean push) {
		this.push = push;
	}

	public JSONArray getKeywordList() {
		return keywordList;
	}

	public void setKeywordList(JSONArray keywordList) {
		if(keywordList==null||keywordList.length()==0){
			keywordList = new JSONArray();
		}
		this.keywordList = keywordList;
	}
	public void setKeywordList(List<String> kList){
		keywordList = new JSONArray();
		for(String s: kList){
			keywordList.put(s);
		}
	}
	public String returnTime(){
		if(limitTime==5){
			return "5Minute";
		}
		else if(limitTime==10){
			return "10Minute";
		}
		else if(limitTime==60){
			return "1Hour";
		}
		else if(limitTime==360){
			return "6Hours";
		}
		else if(limitTime==720){
			return "12Hours";
		}
		else if(limitTime==1440){
			return "1Day";
		}
		else{
			return "time";
		}
	}
	public JSONObject returnJSONObj(){
		try {
			JSONObject object = new JSONObject();
			object.put("id", id);
			object.put("name", name);
			object.put("limitTime", limitTime);
			object.put("push", push);
			object.put("keywordList", keywordList);
			return object;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

	public void recycleImage() {
		if(imageData!=null){
			imageData.recycle();
			imageData=null;
		}
		
	}
	
	
	
}
