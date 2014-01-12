package kr.co.autopush.bean;

public class LoginData {
	private String loginUrl;
	private String formIdPath;
	private String formPasswdPath;
	private String formPath;

	public LoginData(String loginUrl, String formPath, String formIdPath,
			String formPasswdPath) {
		super();
		this.loginUrl = loginUrl;
		this.formIdPath = formIdPath;
		this.formPasswdPath = formPasswdPath;
		this.formPath = formPath;
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
