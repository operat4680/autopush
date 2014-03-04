package kr.co.autopush.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.util.Log;

public class HttpThread extends Thread{
	private String url;
	private String param;
	private Handler handler;
	public HttpThread(String url,JSONObject param,Handler handler) {
		// TODO Auto-generated constructor stub
		this.url = url;
		this.param = param.toString();
		this.handler = handler;
		
	}
	
	public void run(){
		String result = requestServer(url);
		Message message = handler.obtainMessage();
		message.obj = result;
		handler.sendMessage(message);
		
	}
	
	public String requestServer(String address){
		StringBuilder text = new StringBuilder();
		try{
			URL url = new URL(address);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			OutputStream os=null;
			
			
			if(conn!=null){
				conn.setConnectTimeout(10000);
				conn.setUseCaches(false);
//				if(param!=null){
					conn.setRequestMethod("POST");
					conn.setDoInput(true);
					conn.setDoOutput(true);
					conn.setRequestProperty("Accept", "application/text");
					conn.setRequestProperty("Content-Type", "application/json");
				    conn.setRequestProperty("charset", "utf-8");
				    conn.setRequestProperty("Content-Length", "" + Integer.toString(param.getBytes().length));
					os = conn.getOutputStream();
					os.write(param.getBytes("UTF-8"));
					os.flush();
					
//				}
				if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
					BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					for(;;){
						String line = br.readLine();
						if(line==null)break;
						text.append(line);
					}
					br.close();
				}
				if(os!=null)os.close();
				conn.disconnect();
			}
			
		}catch(NetworkOnMainThreadException e){
			e.printStackTrace();
			return "";
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
		return text.toString();
	}
	
}
