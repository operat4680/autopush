package kr.co.autopush.bean;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

public class ImageDataList {
	 ArrayList<Bitmap> imgList;
	 ArrayList<String> imgPath;
	 ArrayList<String> urlList;
	 String id;
	 public ImageDataList(Context context,JSONArray array,String id) throws JSONException{
		   imgList = new ArrayList<Bitmap>();
	       imgPath = new ArrayList<String>();
	       urlList = new ArrayList<String>();
	       this.id = id;
	    	for(int i=0;i<array.length();i++){
	    		String img = array.getJSONObject(i).getString("imgData");
	    		
	    		byte[] decodedString = Base64.decode(img, Base64.DEFAULT);
	    		BitmapFactory.Options option = new BitmapFactory.Options();
	    		option.inSampleSize = 1;
	    		option.inPurgeable=true;
	    		option.inDither= true;
				Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
				imgList.add(bitmap);
				imgPath.add(array.getJSONObject(i).getString("tagPath"));
				urlList.add(array.getJSONObject(i).getString("url"));
			}
	 }
	 public String getId() {
		return id;
	}
	public int getImageViewSize(){
		 return imgList.size();
	 }
	 public int getImagePathSize(){
		 return imgPath.size();
	 }
	public Bitmap getImgData(int i) {
		return imgList.get(i);
	}
	public String getImgPath(int i) {
		return imgPath.get(i);
	}
	public String getUrl(int i){
		return urlList.get(i);
	}
	public int getImgPosition(Bitmap v){
		return imgList.indexOf(v);
	}
	public int size(){
		return imgList.size();
	}
	public void recycleBit(){
		for(Bitmap v : imgList){
			v.recycle();
		}
	}
	
	 
}
