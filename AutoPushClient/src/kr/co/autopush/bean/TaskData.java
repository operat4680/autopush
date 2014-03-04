package kr.co.autopush.bean;

import android.graphics.Bitmap;

public class TaskData {
	private String id;
	private Bitmap v;
	private boolean isNew;
	private int error;
	private String name;
	public TaskData(String id,String name,Bitmap view,boolean isNew ,int error){
		this.id = id;
		this.name = name;
		this.v = view;
		this.isNew = isNew;
		this.error = error;
	}

	public String getName() {
		return name;
	}

	public boolean isNew() {
		return isNew;
	}

	public int getError() {
		return error;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Bitmap getV() {
		return v;
	}


	
}
