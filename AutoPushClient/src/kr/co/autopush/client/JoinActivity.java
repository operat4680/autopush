package kr.co.autopush.client;


import java.io.IOException;

import kr.co.autopush.R;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

public class JoinActivity extends Activity {
	private Context context;
	private Button joinButton;
	private ProgressDialog dialog;
	private EditText rePassword;
	private EditText id;
	private EditText password;
	public String key;
	private GoogleCloudMessaging gcm;
	public static final String PROPERTY_REG_ID = "registration_id";
	private JSONObject result;
	String SENDER_ID = "354689143230";
	String regid="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join);
		joinButton = (Button)findViewById(R.id.ImageButton3);
		context = getApplicationContext();
		
		id = (EditText) findViewById(R.id.edtText5);
		password = (EditText)findViewById(R.id.edtText3);
		rePassword = (EditText) findViewById(R.id.edtText4);
		 joinButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					String JoinId = id.getText().toString();
					String Joinpwd = password.getText().toString();
					String Joinre = rePassword.getText().toString();
					
					if (Joinre.equals("")||Joinpwd.equals("") || JoinId.equals("")) {
						Toast.makeText(getApplicationContext(), "ID, PASSWOR를 입력해 주세요", Toast.LENGTH_SHORT).show();
					}
					else if(!Joinpwd.equals(Joinre)){
						Toast.makeText(getApplicationContext(), "Password가 다릅니다", Toast.LENGTH_SHORT).show();
					}
					else {
						String hashed = Hashing.sha256().hashString(Joinpwd, Charsets.UTF_8).toString();
						result = new JSONObject();
						try {
							result.put("userId", JoinId);
							result.put("passwd", hashed);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						dialog = ProgressDialog.show(JoinActivity.this, "wait", "Join Request Waiting...",true,true);
						gcm = GoogleCloudMessaging.getInstance(JoinActivity.this);
			            regid = getRegistrationId(context);

			            if (regid.isEmpty()) {
			                registerInBackground();
			            }
			            else{
			            	requestServer();
			            }
						//todo gcmResist
					}
				}

		
		});//listener
	
	
	} //end Create
	
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			dialog.dismiss();
			String m = (String)msg.obj;
			if(m.equals("error")){
				Toast.makeText(getApplicationContext(), "Error!!!", Toast.LENGTH_SHORT).show();
			}
			else if(m.equals("success")){
				Toast.makeText(getApplicationContext(), "Join Success", Toast.LENGTH_SHORT).show();
				finish();
			}
			else if(m.equals("duplicate")){
				Toast.makeText(getApplicationContext(), "중복된 아이디가 있습니다", Toast.LENGTH_SHORT).show();
			}
			else{
				Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
			}
		
		} //handleMessage
	};// handler
	private void requestServer() {
		// TODO Auto-generated method stub
		try {
		result.put("gcmId", getRegistrationId(context));
		HttpThread thread = new HttpThread("http://211.189.127.143/AutoPush/JoinServlet",result,handler);
		thread.start();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();

                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                
                Log.i("auto_push", "Registration Finish : "+msg);
                requestServer();
            }

			
        }.execute(null, null, null);
    }
	
	 private void storeRegistrationId(Context context, String regId) {
	        final SharedPreferences prefs = getGcmPreferences(context);
	        SharedPreferences.Editor editor = prefs.edit();
	        editor.putString(PROPERTY_REG_ID, regId);
	        editor.commit();
	    }
	private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i("", "Registration not found.");
            return "";
        }
        return registrationId;
    }
	private SharedPreferences getGcmPreferences(Context context) {
        return getSharedPreferences("autopush",
                Context.MODE_PRIVATE);
    }
	
	} //end Activity



	