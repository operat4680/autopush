package kr.co.autopush.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class TempData {

	private String _id;
	private String tagPath;
	private String url;
	private String userId;
	private String name;
	public TempData(){

	}
	public void setName(String name) {
		this.name = name;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public void setTagPath(String tagPath) {
		this.tagPath = tagPath;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public JSONObject getObject(){
		try{
			JSONObject obj = new JSONObject();
			obj.put("_id",_id);
			obj.put("tagPath",tagPath );
			obj.put("url", url);
			obj.put("name", name);
			obj.put("userId", userId);
			return obj;
		}catch(JSONException e){
			e.printStackTrace();
		}
		return null;
		
	}
	
	
}
