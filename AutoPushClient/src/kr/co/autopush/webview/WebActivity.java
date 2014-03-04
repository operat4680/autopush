package kr.co.autopush.webview;

import kr.co.autopush.R;
import kr.co.autopush.client.HttpThread;
import kr.co.autopush.client.ImageActivity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



public class WebActivity extends Activity implements OnClickListener{
	
	private WebView webView;
	private Handler mHandler;
	private EditText urlText;
	private InputMethodManager mInputMethodManager;
	private PushWebView push;
	private boolean flag = false;
	private boolean loginFlag = false;
	private EditText logId;
	private EditText logPasswd;
	private String loginId;
	private String loginPasswd;
	
	private ProgressBar progressBar; 
	private Handler webHandler;
	private ProgressDialog mProgress;
	
	private final String defaultURL = "http://news.naver.com/main/main.nhn?mode=LSD&mid=shm&sid1=105"; 
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.web_activity);
	    initSetting();
	    settingHandler();
	}
	private void initSetting(){
		webView = (WebView)findViewById(R.id.webview);
	    webView.clearView();
	    webView.clearCache(true);
	    webView.clearHistory();
	    urlText = (EditText)findViewById(R.id.texturl);
	    urlText.setSelectAllOnFocus(true);
	    mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	    progressBar = (ProgressBar)findViewById(R.id.webprogressbar);
	    push = new PushWebView(urlText);
	    CookieManager cookieManager = CookieManager.getInstance();
	    cookieManager.removeSessionCookie();
	    WebSettings setting = webView.getSettings();
	    setting.setUseWideViewPort(true);
	    setting.setSupportZoom(true);
	    setting.setBuiltInZoomControls(true);
	    setting.setJavaScriptEnabled(true);
	    setting.setUserAgent(1);
	    webView.setInitialScale(1);
	    webView.setWebViewClient(push);
	    webView.setWebChromeClient(new WebChromeClient(){
	    	public void onProgressChanged(WebView view, int progress) {
	    		   progressBar.setVisibility(View.VISIBLE);
	    		   progressBar.setProgress(progress);
	    	       if(progress==100)progressBar.setVisibility(View.INVISIBLE);
	    	   } 
	    });
	    webView.loadUrl(defaultURL);
	    webView.requestFocus();
	    Button btn = (Button)findViewById(R.id.resist);
	    btn.setOnClickListener(this);
	    urlText.setOnKeyListener(new OnKeyListener() {
	        public boolean onKey(View view, int keyCode, KeyEvent event) {
	          if (keyCode == KeyEvent.KEYCODE_ENTER) {
	        	mInputMethodManager.hideSoftInputFromWindow(urlText.getWindowToken(), 0);
	            openURL();
	            return true;
	          }
	          return false;
	        }
	      });
	}
	private void settingHandler(){
		mHandler = new Handler(){
	    	@Override
	    	public void handleMessage(Message msg){
	    		if(msg.what ==0){
	    			flag = false;
	    		}
	    	}
	    };
	    webHandler = new Handler(){
	    	public void handleMessage(Message msg){
	    		String m = (String)msg.obj;
	    		Log.i("web","server receive Message : "+m);
	    		mProgress.dismiss();
	    		if(msg==null||m.isEmpty()){
	    			//error handling
	    			Toast.makeText(getApplicationContext(), "Server Connection Error", Toast.LENGTH_SHORT).show();
	    		}
	    		
	    		else if(m.equals("success")){
		    		Intent intent = new Intent(WebActivity.this,ImageActivity.class);
		    		WebActivity.this.startActivity(intent);
//		    		finish();
	    		}
	    		else if(m.equals("loginFail")){
	    			Toast.makeText(getApplicationContext(), "login경로를 찾지 못하였습니다", Toast.LENGTH_SHORT).show();
	    		}
	    		else{
	    			Toast.makeText(getApplicationContext(), m, Toast.LENGTH_SHORT).show();
	    		}
	    		
	    		
	    	}
	    };
	}
	private void openURL(){
		String text = httpInputCheck(urlText.getText().toString());

		if(text!=null){
			webView.loadUrl(text);
			webView.requestFocus();
		}
		
	}
	private String httpInputCheck(String url) {
        if(url.isEmpty()) return null;
         
        if((url.startsWith("http://")||url.startsWith("https://"))){
        	Log.i("auto_test", "testin");
        	return url;
        }
        else return "http://" + url;
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if(keyCode==KeyEvent.KEYCODE_BACK){
			
			if(webView.canGoBack()){
				webView.goBack();
				return false;
			}
			else{
				if(!flag){
					Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
					flag = true;
					mHandler.sendEmptyMessageDelayed(0, 2000);
					return false;
				}else{
					finish();
				}
				
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch(id){
		case R.id.resist :
			
			if(push.isLoginList()){
				AlertDialog.Builder builder = new AlertDialog.Builder(this);    
				builder.setTitle("로그인 여부")        
				.setMessage("현재 사이트가 Login이 필요합니까?")        
				.setCancelable(false)        
				.setPositiveButton("예", new DialogInterface.OnClickListener(){       
				 // 확인 버튼 클릭시 설정
				public void onClick(DialogInterface dialog, int whichButton){
					
					//finish();
					loginFlag=true;
					loginDialog();
					
				}
				})
				.setNegativeButton("아니요", new DialogInterface.OnClickListener(){      
				public void onClick(DialogInterface dialog, int whichButton){
					loginFlag=false;
					confirmDialog();
				}
				});
				AlertDialog dialog = builder.create();    // 알림창 객체 생성
				dialog.show();    // 알림창 띄우기
			}
			else{
				confirmDialog();
			}
			
			break;
		}
	}
	public void loginDialog(){
		Context mContext = getApplicationContext();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.custom_dialog,(ViewGroup) findViewById(R.layout.web_activity));
								
		AlertDialog.Builder aDialog = new AlertDialog.Builder(WebActivity.this);
		aDialog.setTitle("로그인 하려는 사이트 정보");
		aDialog.setView(layout);
		logId = (EditText)layout.findViewById(R.id.webid);
		logPasswd = (EditText)layout.findViewById(R.id.webpasswd);
		TextView logUrl = (TextView)layout.findViewById(R.id.webtexturl);
		logUrl.setText(push.getLoginList().get(push.getLoginList().size()-1)); //Todo urlList가 여러개일때 선택하게?
		aDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if(logId.getText().toString().equals("")||logPasswd.getText().toString().equals("")){
					Toast.makeText(WebActivity.this, "ID,Password를 입력하셔야 합니다", Toast.LENGTH_SHORT).show();
					loginDialog();
				}
				else{
					loginId = logId.getText().toString();
					loginPasswd = logPasswd.getText().toString();
					confirmDialog();
				}
				
			}
		});
		aDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		AlertDialog ad = aDialog.create();
		ad.show();
	}
	public void confirmDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);    
		
		builder.setTitle("등록 확인")      
		.setMessage("현재 페이지를 등록 하시겠습니까?")        
		.setCancelable(true)       
		.setPositiveButton("예", new DialogInterface.OnClickListener(){       
		 
		public void onClick(DialogInterface dialog, int whichButton){
			JSONObject json = new JSONObject();
			try{
				if(loginFlag){
						json.put("login", true);
						json.put("targetURL",push.getCurrentUrl());
						json.put("formURL", push.getLoginList().get(push.getLoginList().size()-1));
						json.put("id",loginId );
						json.put("pwd", loginPasswd);
					
				}
				else{
						json.put("login", false);
						json.put("targetURL",push.getCurrentUrl());
				}
				json.put("userId", getId());
				mProgress = ProgressDialog.show(WebActivity.this, "wait", "서버 처리중..",true,true);
				HttpThread thread = new HttpThread("http://211.189.127.143/AutoPush/PushResist",json,webHandler);
				thread.start();
				} catch (JSONException e) {
					Log.i("web", "json exception");
					e.printStackTrace();
				}
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
	private String getId() {
		SharedPreferences prefs = getSharedPreferences("autopush",
                Context.MODE_PRIVATE);
		String id = prefs.getString("id", "");
		return id;
	}
	

}
