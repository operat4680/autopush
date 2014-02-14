package kr.co.autopush.bean;

import com.mongodb.DBObject;

public class LoginData {
	private String loginUrl;
	private String formIdPath;
	private String formPasswdPath;
	private String formPath;
	private String id;
	public String getId() {
		return id;
	}
	public String getPasswd() {
		return passwd;
	}



	private String passwd;
	public LoginData(String loginUrl, String formPath, String formIdPath,
			String formPasswdPath) {
		super();
		this.loginUrl = loginUrl;
		this.formIdPath = formIdPath;
		this.formPasswdPath = formPasswdPath;
		this.formPath = formPath;
	}
	public LoginData(DBObject obj){
		this.loginUrl = obj.get("loginURL").toString();
		this.formPath = obj.get("formPath").toString();
		this.formIdPath = obj.get("idPath").toString();
		this.formPasswdPath = obj.get("passwdPath").toString();
		this.id = obj.get("id").toString();
		//TODO decription
		this.passwd = obj.get("pwd").toString();
		//
	}

	public LoginData() {
		
	}
	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getFormIdPath() {
		return formIdPath;
	}

	public void setFormIdPath(String formIdPath) {
		this.formIdPath = formIdPath;
	}

	public String getFormPasswdPath() {
		return formPasswdPath;
	}

	public void setFormPasswdPath(String formPasswdPath) {
		this.formPasswdPath = formPasswdPath;
	}

	public String getFormPath() {
		return formPath;
	}

	public void setFormPath(String formPath) {
		this.formPath = formPath;
	}



	public void print() {
		System.out.println("login URL : " + loginUrl);
		System.out.println("formPath : " + formPath);
		System.out.println("formIDPath : " + formIdPath);
		System.out.println("fromPasswdPath : " + formPasswdPath);
	}
}
