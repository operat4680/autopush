package kr.co.autopush.listener;

import org.json.JSONException;
import org.json.JSONObject;

import kr.co.autopush.client.HttpThread;
import kr.co.autopush.client.InfoActivity;
import kr.co.autopush.client.TaskActivity;
import kr.co.autopush.webview.WebActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class TaskImageClickListener implements OnClickListener{

	private Context context;
	private String id;
	private boolean error;
	private Handler handler;
	
	public TaskImageClickListener(Context context,String id,boolean error){
		this.context = context;
		this.id = id;
		this.error = error;
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				 String message= (String)msg.obj;
				 if(message.equals("success")){
					 Toast.makeText(TaskImageClickListener.this.context, "삭제되엇습니다", Toast.LENGTH_SHORT).show();
					 ((TaskActivity)TaskImageClickListener.this.context).getData();
				 }
				 else{
					 Toast.makeText(TaskImageClickListener.this.context, "Error", Toast.LENGTH_SHORT).show();
				 }
			} //handleMessage
		};// handler
	}
	
	@Override
	public void onClick(View v) {
		if(id==null){
			Intent intent = new Intent(context,WebActivity.class);
			context.startActivity(intent);
		}
		else if(error){
			AlertDialog.Builder builder = new AlertDialog.Builder(context);    
			Log.i("auto_push", "ininininin!!!!!!");
			builder.setTitle("Detection Error")      
			.setMessage("등록하신 사이트 영역은 동적으로 변하여 추적할 수 없습니다")        
			.setCancelable(false)
			.setPositiveButton("예", new DialogInterface.OnClickListener(){       
			 
					public void onClick(DialogInterface dialog, int whichButton){
						JSONObject json = new JSONObject();
						try {
							json.put("type", "delete");
							json.put("taskId", id);
							HttpThread thread = new HttpThread("http://211.189.127.143/AutoPush/TaskServlet",json,handler);
							thread.start();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
			});
			AlertDialog dialog = builder.create();   
			dialog.show();    
		}
		else{
			Intent intent = new Intent(context,InfoActivity.class);
			intent.putExtra("id", id);
			context.startActivity(intent);
		}
		
	}

}
