package kr.co.autopush.client;

import kr.co.autopush.bean.ImageStatic;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

public class ZoomActivity extends Activity{
	  ImageView image;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(kr.co.autopush.R.layout.custom_image);
		image = (ImageView)findViewById(kr.co.autopush.R.id.customimage);
//		BitmapFactory.Options option = new BitmapFactory.Options();
//		Bitmap bit = BitmapFactory.decodeByteArray(ImageStatic.img, 0,ImageStatic.img.length);
		image.setImageBitmap(ImageStatic.img2);
	}
	protected void onDestroy(){
//		Drawable d = image.getDrawable();
//        if (d instanceof BitmapDrawable) {
//            Bitmap bi = ((BitmapDrawable)d).getBitmap();
//            bi.recycle();
//        } 
		super.onDestroy();
	}
}
