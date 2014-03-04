package kr.co.autopush.client;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import kr.co.autopush.R;
import kr.co.autopush.adapter.ImageGridAdapter;
import kr.co.autopush.bean.ImageDataList;
import kr.co.autopush.webview.WebActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.common.primitives.Bytes;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ImageActivity extends Activity{
	private Handler webHandler;
	private ProgressDialog dialog;
	private String idObject;
	private ImageGridAdapter imageGridAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid_activity);
		/*
		Button bt = (Button)findViewById(R.id.imagebt1);
		bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				HttpThread thread = new HttpThread("http://211.189.127.143/AutoPush/ImageServlet",new JSONObject(),webHandler);
				thread.start();
			}
		});
		*/
		dialog = ProgressDialog.show(this, "wait", "Image Data Loading...",true,true);
		 webHandler = new Handler(){
		    	public void handleMessage(Message msg){
		    		String m = (String)msg.obj;
		    		Log.i("web","server receive Message : "+m);
		    		
		    		
		    		if(m!=null&&!m.equals("")){
		    		getImage(m);
		    		}
		    		else{
		    			Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();
		    		}
		    		
		    		
		    	}
		    };
		JSONObject obj = new JSONObject();
		try {
			obj.put("userId",getId());
			
			HttpThread thread = new HttpThread("http://211.189.127.143/AutoPush/ImageServlet",obj,webHandler);
			thread.start();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void getImage(String msg){
		try {
			JSONObject json;
			json = new JSONObject(msg);
			idObject = json.getJSONObject("_id").toString();
			JSONArray jarray = json.getJSONArray("imgList");
	//		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
	//		ImageLoader.getInstance().init(config);
		
			
			
			GridView gridViewImages = (GridView)findViewById(R.id.gridViewImages);
			
			
			ImageDataList list = new ImageDataList(this,jarray,idObject);
			
	        imageGridAdapter = new ImageGridAdapter(this,list);
	        
	        gridViewImages.setAdapter(imageGridAdapter);
	       dialog.dismiss();

	        
//			ImageView v = (ImageView)findViewById(R.id.image);
//			v.setImageBitmap(bitmap);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private String getId() {
		SharedPreferences prefs = getSharedPreferences("autopush",
                Context.MODE_PRIVATE);
		String id = prefs.getString("id", "");
		return id;
	}
	protected void onDestroy(){
		imageGridAdapter.reCycleBitmap();
		super.onDestroy();
	}
	/* base64 변환
	byte[] decodedString = Base64.decode(strBase64, Base64.DEFAULT);
	Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length)
	*/
	/* hash 암호화
	 * 
	 * String hashed = Hashing.sha256().hashString("your input", Charsets.UTF_8).toString();
	*/
	 
}
