package kr.co.autopush.client;




import java.io.IOException;

import kr.co.autopush.R;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;


public class LoginActivity extends Activity{
	
	private Button loginButton;
	private Button joinButton;
	private EditText id;
	private EditText password;
	private CheckBox check;
	private Handler handler;
	private ProgressDialog dialog;
	private Context context;
	private GoogleCloudMessaging gcm;
	private final String SENDER_ID = "354689143230";
	private String loginId;
	private String loginpwd;
	private String regid;
	private String uid;
	private boolean autoCheck=true;
	String TAG = "auto_push";
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_login);
	    context = getApplicationContext();
	    loginButton = (Button)findViewById(R.id.ImageButton1);
	    check = (CheckBox)findViewById(R.id.checkBox1);
	    id = (EditText)findViewById(R.id.edtText1);
	    password = (EditText)findViewById(R.id.edtText2);
	    
	    loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loginId = id.getText().toString();
				String pwd = password.getText().toString();
				loginpwd = Hashing.sha256().hashString(pwd, Charsets.UTF_8).toString();
				if(loginId.equals("")||loginpwd.equals("")){
					Toast.makeText(getApplicationContext(), "ID, PASSWORD를 입력해주세요", Toast.LENGTH_SHORT).show();
				}
				else{
					
					regid = getGcmKey();
					if(!regid.equals("")){
						checkLogin(loginId,loginpwd,regid);
					}
				}	
			}
		});
	    
	    joinButton = (Button)findViewById(R.id.ImageButton2);
	    joinButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this,JoinActivity.class);
				startActivity(intent);
			}

	    });
	    
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				dialog.dismiss();
				 String message= (String)msg.obj;
				 JSONObject obj=null;
				 String m="";
				try {
					if(message.isEmpty()||message.equals("")){
						
						Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT).show();
					}
						obj = new JSONObject(message);
						if(obj!=null&&obj.length()!=0){
							m = obj.getString("message");
						}
						if(m.equals("gcm")){
							uid = obj.getString("id").toString();
							confirmDialog();
						}
						else if(m.equals("success")){
							if(check.isChecked()&&!autoCheck){
								savePreference(loginId,loginpwd);
							}
							
							nextActivity(obj.get("id").toString());
						}
						else{
							Toast.makeText(getApplicationContext(), "아이디 비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
						}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} //handleMessage
		};// handler
		checkAutoLogin();
		
	}

	private void checkAutoLogin() {
		SharedPreferences prefs = getSharedPreferences("autopush",
                Context.MODE_PRIVATE);
		autoCheck = prefs.getBoolean("auto", false);
		if(autoCheck){
			checkLogin(prefs.getString("loginId", ""),prefs.getString("loginPwd", ""),getGcmKey());
		}
		
	}

	protected void savePreference(String id, String pwd) {
		SharedPreferences prefs = getSharedPreferences("autopush",
                Context.MODE_PRIVATE);
		Editor edit = prefs.edit();
		edit.putString("loginId", id);
		edit.putString("loginPwd", pwd);
		edit.putBoolean("auto", true);
		edit.commit();
	}

	private void checkLogin(String id,String pw,String gcm){
		dialog = ProgressDialog.show(LoginActivity.this, "wait", "Login Request Waiting...",true,true);
		JSONObject json = new JSONObject();
		try {
			json.put("type", "check");
			json.put("userId", id);
			json.put("passwd",pw );
			json.put("gcmId", gcm);
			HttpThread thread = new HttpThread("http://211.189.127.143/AutoPush/LoginServlet",json,handler);
			thread.start();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void nextActivity(String id){
		SharedPreferences prefs = getSharedPreferences("autopush",
                Context.MODE_PRIVATE);
		Editor edit = prefs.edit();
		edit.putString("id", id);
		edit.commit();
		Intent intent = new Intent(LoginActivity.this,TaskActivity.class);
		dialog.dismiss();
		startActivity(intent);
		finish();
	}
	private String getGcmKey(){
		SharedPreferences prefs = getSharedPreferences("autopush",
                Context.MODE_PRIVATE);
		String key = prefs.getString("registration_id", "");
		if(key.isEmpty()){
			registerInBackground();
			return "";
		}
		else{
			return key;
		}
	}
	private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;
                    storeRegistrationId(regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();

                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                
                Log.i("auto_push", "Registration Finish : "+msg);
                checkLogin(loginId,loginpwd,regid);
            }

			
        }.execute(null, null, null);
    }

	protected void storeRegistrationId(String reg) {
		SharedPreferences prefs = getSharedPreferences("autopush",
                Context.MODE_PRIVATE);
		Editor edit = prefs.edit();
		edit.putString("registration_id", reg);
		edit.commit();
	}
	public void confirmDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);    
		
		builder.setTitle("기기 등록")      
		.setMessage("로그인 하려는 아이디와 기기정보가 맞지 않습니다\n이 기기로 다시 저장하시겠습니까?")        
		.setCancelable(true)       
		.setPositiveButton("예", new DialogInterface.OnClickListener(){       
		 
				public void onClick(DialogInterface dialog, int whichButton){
					JSONObject json = new JSONObject();
					try {
						json.put("type", "update");
						json.put("id", uid);
						json.put("gcmId", getGcmKey());
						HttpThread thread = new HttpThread("http://211.189.127.143/AutoPush/LoginServlet",json,handler);
						thread.start();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		})
		.setNegativeButton("아니요", new DialogInterface.OnClickListener(){      
				public void onClick(DialogInterface dialog, int whichButton){
					nextActivity(uid);
				}
		});
		AlertDialog dialog = builder.create();   
		dialog.show();    
	}
	

}
