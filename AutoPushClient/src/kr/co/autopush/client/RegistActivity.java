package kr.co.autopush.client;

import kr.co.autopush.R;
import kr.co.autopush.bean.ImageStatic;
import kr.co.autopush.bean.TempData;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
public class RegistActivity extends Activity{
	private final String url = "http://211.189.127.143/AutoPush/InsertData";
	private ProgressDialog mProgress;
	private ImageView zoomImage;
	private Button save;
	private Button cancle;
	private Handler handler;
	private Context context;
	private TempData tempData;
	
	
	private Dialog updialog;
	private EditText eText;
	private String keyText;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zoom_image);
		zoomImage = (ImageView)findViewById(R.id.zoomimage);
		save = (Button)findViewById(R.id.save);
		cancle = (Button)findViewById(R.id.cancle);
		context = getApplication();
		Intent intent = getIntent();

		zoomImage.setImageBitmap(ImageStatic.img2);
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				regist();
			}
		});
		cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		handler = new Handler(){
	    	public void handleMessage(Message msg){
	    		String m = (String)msg.obj;
	    		mProgress.dismiss();
	    		if(m.equals("success")){
	    			Intent in = new Intent(RegistActivity.this,TaskActivity.class);
	    			in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK 
	    	                | Intent.FLAG_ACTIVITY_CLEAR_TOP 
	    	                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
	    			startActivity(in);
	    			finish();
	    		}
	    		else if(msg==null||m.isEmpty()){
	    			//error handling
	    			mProgress.dismiss();
	    			Toast.makeText(context, "Server Connection Error", Toast.LENGTH_SHORT).show();
	    		}
	    		else{
	    			mProgress.dismiss();
	    			Toast.makeText(context, m, Toast.LENGTH_SHORT).show();
	    		}
	    	}
	    };
	    tempData = new TempData();
	    tempData.set_id(intent.getStringExtra("_id"));
	    tempData.setTagPath(intent.getStringExtra("tagPath"));
	    tempData.setUrl(intent.getStringExtra("url"));
	    tempData.setUserId(intent.getStringExtra("userId"));
	    
	}
	
	private void regist(){
		AlertDialog.Builder builder = new AlertDialog.Builder(RegistActivity.this);    
		
		builder.setTitle("등록 확인")      
		.setMessage("게시판 영역을 등록 하시겠습니까?")        
		.setCancelable(true)       
		.setPositiveButton("예", new DialogInterface.OnClickListener(){       
	 
		public void onClick(DialogInterface dialog, int whichButton){
			
			setNameDialog();

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
	private void setNameDialog(){
		updialog = new Dialog(RegistActivity.this);

        updialog.setContentView(R.layout.block_by_keyword);
        updialog.setTitle("Keyword To Block");
        updialog.setCancelable(true);

        eText=(EditText)updialog.findViewById(R.id.editTextKeywordsToBlock);
        eText.setHint("제목 입력");
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
				keyText = eText.getText().toString();
				if(keyText.isEmpty()){
					Toast.makeText(getApplicationContext(), "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
				}
				else{
					updialog.dismiss();
					tempData.setName(keyText);
					mProgress = ProgressDialog.show(RegistActivity.this, "wait", "서버 처리중..",true,true);
					JSONObject obj = tempData.getObject();
					HttpThread thread = new HttpThread(url, obj, handler);
					thread.start();
				}
			}
			
		});
	}
//	protected void onDestroy(){
//		Drawable d = zoomImage.getDrawable();
//        if (d instanceof BitmapDrawable) {
//            Bitmap bi = ((BitmapDrawable)d).getBitmap();
//            bi.recycle();
//        } 
//        d.setCallback(null);
//		super.onDestroy();
//	}
	

	
}
