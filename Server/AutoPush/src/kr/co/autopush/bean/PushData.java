package kr.co.autopush.bean;

public class PushData {
	private String userId;
	private String name;
	private String gcmKey;
	public String getGcmKey() {
		return gcmKey;
	}
	public void setGcmKey(String gcmKey) {
		this.gcmKey = gcmKey;
	}
	public PushData(){
		
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setName(String name) {
		this.name = name;
	}
	public PushData(String userId, String name) {
		super();
		this.userId = userId;
		this.name = name;
	}
	public String getUserId() {
		return userId;
	}
	public String getName() {
		return name;
	}
	
}
