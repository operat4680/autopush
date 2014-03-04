package kr.co.autopush.client;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import kr.co.autopush.R;
import kr.co.autopush.bean.ImageStatic;
import kr.co.autopush.bean.TaskOption;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



public class InfoActivity extends Activity {
	private Handler handler;
	private ProgressDialog dialog;
	private Dialog updialog;
	private Button showbutton;
	private Button periodbutton;
	private ImageView alarmbutton;
	private TaskOption option;
	private EditText editTextKeywordToBlock;
	private TextView title;
	private ImageView image;
	private TextView urlText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private ArrayList<String> keylist;
	private String keyText;
	private TextView keywordtext;
	private LinearLayout lay;
	private int listPosition;
	private ImageButton deletebutton;
	private ImageButton checkbutton;
	private ImageButton keybutton;
	
	
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_info);
			dialog = ProgressDialog.show(this, "Wait", "Task Data Loading...",true,true);
			title = (TextView)findViewById(R.id.page_title_view);
			showbutton = (Button)findViewById(R.id.show_btn);
			periodbutton = (Button)findViewById(R.id.period_btn);
			deletebutton = (ImageButton)findViewById(R.id.delete_btn);
			keybutton = (ImageButton)findViewById(R.id.key_btn);
			image = (ImageView)findViewById(R.id.capture_image);
			urlText = (TextView)findViewById(R.id.pageurl_text);
			listView = (ListView)findViewById(R.id.KeywordList);
			checkbutton = (ImageButton)findViewById(R.id.check_btn);
			keywordtext = (TextView)findViewById(R.id.keywordtext);
			alarmbutton = (ImageView)findViewById(R.id.alarm_btn);

		
			
			
			checkbutton.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					updateTask();
					
				}
				
			});
			//삭제

			deletebutton.setOnClickListener(new OnClickListener(){
				
				public void onClick(View v){
					
					AlertDialog.Builder builder = new AlertDialog.Builder(InfoActivity.this); 
					
					builder.setTitle("Delete")       
					.setMessage("이 사이트를 삭제하겠습니까?")
					.setCancelable(true)      
					.setPositiveButton("예", new DialogInterface.OnClickListener(){       
				
					public void onClick(DialogInterface dialog, int whichButton){
						deleteTask();
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
			
			
			periodbutton.setOnClickListener(new OnClickListener(){
				
				public void onClick(View v){ 
					
					
					final CharSequence[] items = {"5Minute", "10Minute", "1Hour","6Hours" ,"12Hours", "1Day"};

					AlertDialog.Builder builder = new AlertDialog.Builder(InfoActivity.this);    
					
					builder.setTitle("주기설정")    
					.setItems(items, new DialogInterface.OnClickListener(){    
						public void onClick(DialogInterface dialog, int index){
							
							option.setLimitTimeByItem(index);
							periodbutton.setText(option.returnTime());
						}
					});

					AlertDialog dialog = builder.create();  
					dialog.show(); 
			  }
				
			});
			
			
			keybutton.setOnClickListener(new OnClickListener(){
				public void onClick(View v){
					updialog = new Dialog(InfoActivity.this);

		            updialog.setContentView(R.layout.block_by_keyword);
		            updialog.setTitle("Keyword To Block");
		            updialog.setCancelable(true);

		            editTextKeywordToBlock=(EditText)updialog.findViewById(R.id.editTextKeywordsToBlock);
		            
		            Button btnBlock=(Button)updialog.findViewById(R.id.buttonBlockByKeyword);
		            Button btnCancel=(Button)updialog.findViewById(R.id.buttonCancelBlockKeyword);
		            updialog.show();
		            btnCancel.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							updialog.dismiss();
						}
					});
		            btnBlock.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							keyText = editTextKeywordToBlock.getText().toString();
							if(keyText.isEmpty()){
								Toast.makeText(getApplicationContext(), "keyword를 입력해주세요", Toast.LENGTH_SHORT).show();
							}
							else{
								updialog.dismiss();
								keylist.add(keyText);
								setText(keylist.size());
								adapter.notifyDataSetChanged();
							}
						}
						
					});
				}
				
				
			});
			
			
			
			alarmbutton.setOnClickListener(new OnClickListener(){
				public void onClick(View v){
					if(option.isPush()){
						option.setPush(false);
						alarmbutton.setImageResource(R.drawable.alarm_off_btn);
					}
					else{
						option.setPush(true);
						alarmbutton.setImageResource(R.drawable.alarm_on_btn);
					}
				}
			});
			handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					dialog.dismiss();
					String m = (String)msg.obj;
					if(m==null||m.equals("")||m.equals("error")){
						Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
					}
					else if(m.equals("success")){
						Toast.makeText(getApplicationContext(), "삭제되었습니다", Toast.LENGTH_SHORT).show();
//						Intent intent = new Intent(InfoActivity.this,TaskActivity.class);
//						startActivity(intent);
						finish();
					}
					else if(m.equals("update")){
						Toast.makeText(getApplicationContext(), "수정되었습니다", Toast.LENGTH_SHORT).show();
//						Intent intent = new Intent(InfoActivity.this,TaskActivity.class);
//						startActivity(intent);
						finish();
					}
					else{
						JSONObject obj=null;
						try {
							obj = new JSONObject(m);
						
							setData(obj);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
			};
			Intent intent = getIntent();
			JSONObject json = new JSONObject();
			try {
				json.put("type", "task");
				json.put("id", intent.getStringExtra("id"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			HttpThread thread = new HttpThread("http://211.189.127.143/AutoPush/TaskServlet",json , handler);
			thread.start();
		}
			
		private void setData(JSONObject obj) throws JSONException{
			option = new TaskOption();
			try{
				JSONObject id = obj.getJSONObject("_id");
				option.setId(id.getString("$oid"));
				option.setImageData(obj.get("imgData").toString());
				if(obj.isNull("keywordList")){
					option.setKeywordList(new JSONArray());
				}
				else{
					option.setKeywordList(obj.getJSONArray("keywordList"));
				}
				option.setLimitTime(obj.getInt("limitTime"));
				option.setName(obj.getString("name"));
				option.setPush(obj.getBoolean("push"));
				option.setTargetUrl(obj.getString("targetUrl").toString());
			}catch(JSONException e){
				
				e.printStackTrace();
			}
			title.setText(option.getName());
			image.setImageBitmap(option.getImageData());
			image.setAdjustViewBounds(true);
			image.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(InfoActivity.this,ZoomActivity.class);
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					ImageStatic.img2 = option.getImageData();
//					Bitmap bitmap = option.getImageData();
//					bitmap.compress(CompressFormat.PNG,100,stream);
//					byte[] b = stream.toByteArray();
//					ImageStatic.img = b;
//					Drawable d = image.getDrawable();
//			        if (d instanceof BitmapDrawable) {
//			            Bitmap bi = ((BitmapDrawable)d).getBitmap();
//			            bi.recycle();
//			        } 
//			        d.setCallback(null);
					startActivity(intent);
				}
			});
			int max = 30;
			int len = option.getTargetUrl().length();
			if(max<len){
				urlText.setText(option.getTargetUrl().substring(0, max)+"...");
			}
			else{
				urlText.setText(option.getTargetUrl());
			}
			periodbutton.setText(option.returnTime());
			if(option.isPush()){
				alarmbutton.setImageResource(R.drawable.alarm_on_btn);
			}
			else{
				alarmbutton.setImageResource(R.drawable.alarm_off_btn);
			}
			keylist = new ArrayList<String>();
			JSONArray array = option.getKeywordList();
			if(array!=null){
				for(int i=0;i<array.length();i++){
					keylist.add(array.getString(i).toString());
				}
			}

			showbutton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder = new AlertDialog.Builder(InfoActivity.this); 
					
					builder.setTitle("연결")       
					.setMessage("해당 주소로 연결 하시겠습니까?")
					.setCancelable(true)      
					.setPositiveButton("예", new DialogInterface.OnClickListener(){       
				
					public void onClick(DialogInterface dialog, int whichButton){
						Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(option.getTargetUrl())); 
						startActivity(intent);
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
			adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,keylist);
			listView.setAdapter(adapter);
			setText(keylist.size());
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {
					String str = (String)adapter.getItem(position);
					listPosition = position;
					AlertDialog.Builder builder = new AlertDialog.Builder(InfoActivity.this); 
					
					builder.setTitle("Delete")       
					.setMessage(str + " keyword를 삭제하시겠습니까?")
					.setCancelable(true)      
					.setPositiveButton("예", new DialogInterface.OnClickListener(){       
				
					public void onClick(DialogInterface dialog, int whichButton){
						String str = (String)adapter.getItem(listPosition);
						keylist.remove(listPosition);
						setText(keylist.size());
						adapter.notifyDataSetChanged();
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

		}
		private void deleteTask(){
			try{
				JSONObject obj = new JSONObject();
				obj.put("type","delete");
				obj.put("taskId", option.getId());
				HttpThread thread = new HttpThread("http://211.189.127.143/AutoPush/TaskServlet",obj , handler);
				thread.start();
			}catch(JSONException e){
				e.printStackTrace();
			}
		}
		private void updateTask(){
			try{
				option.setKeywordList(keylist);
				JSONObject obj = option.returnJSONObj();
				obj.put("type","update");
				HttpThread thread = new HttpThread("http://211.189.127.143/AutoPush/TaskServlet",obj , handler);
				thread.start();
			}catch(JSONException e){
				e.printStackTrace();
			}
		}
		private void setText(int i){
			keywordtext.setText("KEYWORD : " + i+"개");
		}
		protected void onDestroy(){
			Drawable d = image.getDrawable();
			if (d instanceof BitmapDrawable) {
				Bitmap bm = ((BitmapDrawable) d).getBitmap();
				if (bm != null)
					bm.recycle();
			}
//			image.setImageDrawable(null);
//			option.recycleImage();
//			d = alarmbutton.getDrawable();
//			if (d instanceof BitmapDrawable) {
//				Bitmap bm = ((BitmapDrawable) d).getBitmap();
//				if (bm != null)
//					bm.recycle();
//			}
//			alarmbutton.setImageDrawable(null);
			System.gc();
			super.onDestroy();
		}
		protected void onResume(){
	
			super.onResume();
		}
		
}
					
		