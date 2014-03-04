package kr.co.autopush.webview;

import java.util.ArrayList;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

public class PushWebView extends WebViewClient{
	
	private ArrayList<String> loginList;
	private EditText et;
	private int keyInput=0;
	private String currentURL;
	private String loginURL;
	public PushWebView(EditText et){
		loginList = new ArrayList<String>();
		this.et = et;
		
	}
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		// TODO Auto-generated method stub
		view.loadUrl(url);
		return super.shouldOverrideUrlLoading(view, url);
	}
	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		// TODO Auto-generated method stub
		
		et.setText(url);
		currentURL = url;
		super.onPageStarted(view, url, favicon);
	}
	@Override
	public void onPageFinished(WebView view, String url) {
		// TODO Auto-generated method stub
		
		if(keyInput>=20){
			Log.i("web","insert URL : "+ loginURL);
			if(loginURL!=null){
				loginList.add(loginURL);
			}
		}
		currentURL = url;
		loginURL = url;
		keyInput=0;
		Log.i("web", url+" : loaded");
		//et.setText(url);
		super.onPageFinished(view, url);
	}
	@Override
	public void onReceivedLoginRequest(WebView view, String realm,
			String account, String args) {
	
		Log.i("web", "loginrequest! "+ ", realm : "+realm+"account: "+account+"args : "+ args);
		super.onReceivedLoginRequest(view, realm, account, args);
	}

	@Override
	public void onReceivedError(WebView view, int errorCode,
			String description, String failingUrl) {
		// TODO Auto-generated method stub
		Log.i("web", "receiveError");
		super.onReceivedError(view, errorCode, description, failingUrl);
	}
	@Override
	public void onReceivedHttpAuthRequest(WebView view,
			HttpAuthHandler handler, String host, String realm) {
		Log.i("web", "AuthError");
		super.onReceivedHttpAuthRequest(view, handler, host, realm);
	}
	@Override
	public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.i("web", "keyEvent :" +event.getKeyCode());
		int code = event.getKeyCode();
		if((code>=7&&code<=16)||(code>=29&&code<=54)||(code>=96&&code<=105)){
			keyInput++;
		}
		return super.shouldOverrideKeyEvent(view, event);
	}
	@Override
	public void onReceivedSslError(WebView view, SslErrorHandler handler,
			SslError error) {
		Log.i("web", "SSlError");
		super.onReceivedSslError(view, handler, error);
	}
	public ArrayList<String> getLoginList(){
		if(loginList.size()==0){
			return null;
		}
		else{
			/*
			HashSet set = new HashSet(loginList);
			return new ArrayList<String>(set);
			*/
			removeDuplicate();
			return loginList;
		}
	}
	public boolean isLoginList(){
		if(loginList.size()==0){
			return false;
		}
		else{
			return true;
		}
	}
	public void removeDuplicate(){
		for(int i=0;i<loginList.size()-2;i++){
			for(int j=i+1;j<loginList.size()-1;j++){
				if(loginList.get(i).equals(loginList.get(j))){
					loginList.remove(j);
					j--;
				}
			}
		}
	}
	public String getCurrentUrl() {
		
		return currentURL;
	}
	
	

}
