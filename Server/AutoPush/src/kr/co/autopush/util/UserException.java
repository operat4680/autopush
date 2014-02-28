package kr.co.autopush.util;

public class UserException extends Exception{

	private String error;
	public UserException(String error){
		this.error = error;
	}
	public String getMessage(){
		return error;
	}
}
