package kr.co.autopush.util;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

public class GcmPush {
	
	private final static String API_KEY = "AIzaSyAZsGdbhEBTvfHlIOLOwf-9Gjo7Kylmaps";
	
//	public static void sendPush(List<String> list){
//		Sender sender = new Sender(API_KEY); 
//		
//		
//			Message msg = new Message.Builder().addData("id", "show4680").addData("name", "신문").build();
//			Result result = sender.send(msg, gcmkey, 5);
//			if (result.getMessageId() != null) {
//				System.out.println("success");
//			} 
//			else {
//					String error = result.getErrorCodeName(); 
//					System.out.println(error);
//				if (Constants.ERROR_INTERNAL_SERVER_ERROR.equals(error)) {
//					// 구글 푸시 서버 에러
//					System.out.println("push error");
//				}
//			}
//	}
	
}
