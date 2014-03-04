package kr.co.autopush.client;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import kr.co.autopush.R;
import kr.co.autopush.adapter.ImageGridAdapter;
import kr.co.autopush.adapter.TaskGridAdapter;
import kr.co.autopush.bean.ImageDataList;
import kr.co.autopush.bean.TaskData;
import kr.co.autopush.listener.ImageClickListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class TaskActivity extends Activity {
	
	private Handler handler;
	private ProgressDialog dialog;
	private ImageButton logout;
	private TaskGridAdapter gridAdapter;
	private GridView gridViewImages;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    Log.i("auto_push", "task onCreate");
	    setContentView(R.layout.task_activity);
	    dialog = ProgressDialog.show(this, "wait", "Image Data Loading...",true,true);
	    logout = (ImageButton)findViewById(R.id.logout);
	    logout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(TaskActivity.this);    
				
				builder.setTitle("로그아웃")
				.setMessage("자동로그인이 취소됩니다.\n로그아웃 하시겠습니까?")            
				.setPositiveButton("예", new DialogInterface.OnClickListener(){       
			 
				public void onClick(DialogInterface dialog, int whichButton){
					logout();
					Toast.makeText(TaskActivity.this, "Logout", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(TaskActivity.this,LoginActivity.class);
					startActivity(intent);
					finish();
				
				}
				})
				.setNegativeButton("아니요", new DialogInterface.OnClickListener(){      
						public void onClick(DialogInterface dialog, int whichButton){
							dialog.cancel();
						}
				});
				AlertDialog dialog = builder.create();   
				dialog.show();    
				
				
			}
		});
		handler = new Handler(){
	    	public void handleMessage(Message msg){
	    		String m = (String)msg.obj;
	    		if(msg==null||m.isEmpty()){
	    			//error handling
	    			Toast.makeText(TaskActivity.this, "Server Connection Error", Toast.LENGTH_SHORT).show();
	    			dialog.dismiss();
	    		}
	    		else{
	    			setGridView(m);
	    		}
	    	}
	    };
	   
	    
	}
	public void getData(){
		dialog.show();
		JSONObject obj = new JSONObject();
		try {
			obj.put("type", "get");
			obj.put("userId", getId());	
			HttpThread thread = new HttpThread("http://211.189.127.143/AutoPush/TaskServlet",obj,handler);
			thread.start();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setGridView(String m){
		try {
			JSONObject obj = new JSONObject(m);
			JSONArray array = obj.getJSONArray("taskList");
			gridViewImages = (GridView)findViewById(R.id.taskGridView);
			gridAdapter = new TaskGridAdapter(this,setList(array));
	        gridViewImages.setAdapter(gridAdapter);
	        dialog.dismiss();
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public List<TaskData> setList(JSONArray array) throws JSONException{
		List<TaskData> list = new ArrayList<TaskData>();
	 	for(int i=0;i<array.length();i++){
    		JSONObject obj = array.getJSONObject(i);
    		//ImageView view = new ImageView(this);
    		String data = obj.getString("imgData");
    		JSONObject oid = (JSONObject) obj.get("_id");
    		String id = oid.getString("$oid");
    		String name = obj.getString("name");
    		boolean isNew = obj.getBoolean("isNew");
    		int error = obj.getInt("error");
    		byte[] decodedString = Base64.decode(data, Base64.DEFAULT);
    		BitmapFactory.Options option = new BitmapFactory.Options();
    		option.inSampleSize = 64;
    		option.inPurgeable=true;
    		option.inDither= false;
			Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
			//view.setImageBitmap(bitmap);
			if(bitmap!=null){
				TaskData task = new TaskData(id,name,bitmap,isNew,error);
				list.add(task);
			}
    		
    	}
	 	return list;
	}
	@Override
	protected void onResume() {
		 getData();
		super.onResume();
	}
	protected void onPause(){
		if(gridAdapter!=null){
			gridViewImages.removeAllViewsInLayout();
//			 gridAdapter.reCycleList();
			 
//			 gridAdapter=null;
		 }
		super.onPause();
	}
	private String getId() {
		SharedPreferences prefs = getSharedPreferences("autopush",
                Context.MODE_PRIVATE);
		String id = prefs.getString("id", "");
		return id;
	}
	private void logout(){
		SharedPreferences prefs = getSharedPreferences("autopush",
                Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putBoolean("auto", false);
		editor.commit();
	}
	
}
