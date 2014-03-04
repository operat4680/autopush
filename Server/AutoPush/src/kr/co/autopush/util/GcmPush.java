package kr.co.autopush.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kr.co.autopush.bean.PushData;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

public class GcmPush {
	
	private final static String API_KEY = "AIzaSyAZsGdbhEBTvfHlIOLOwf-9Gjo7Kylmaps";
	
	public static void sendPush(List<PushData> list){
		Sender sender = new Sender(API_KEY); 
		if(list!=null&&list.size()>0){
			try{
				for(PushData p : list){
					System.out.println(p.getUserId()+" "+p.getGcmKey()+" "+p.getName());
					Message msg = new Message.Builder().addData("id", p.getUserId()).addData("name", p.getName()).build();
					Result result = sender.send(msg,p.getGcmKey(), 5);
					if (result.getMessageId() != null) {
						System.out.println("success");
					} 
					else {
							String error = result.getErrorCodeName(); 
							System.out.println(error);
						if (Constants.ERROR_INTERNAL_SERVER_ERROR.equals(error)) {
							// 구글 푸시 서버 에러
							System.out.println("push error");
						}
					}
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
}
