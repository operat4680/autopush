package kr.co.autopush.listener;

import java.io.ByteArrayOutputStream;

import kr.co.autopush.bean.ImageDataList;
import kr.co.autopush.bean.ImageStatic;
import kr.co.autopush.client.RegistActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ImageClickListener implements OnClickListener{
	private ImageDataList list;
	private Context context;
	private View view;
	private Bitmap bitmap;
	public ImageClickListener(Context context,ImageDataList list,Bitmap v){
		this.context = context;
		this.list = list;
		this.bitmap = v;
	}
	@Override
	public void onClick(View v) {
				view = v;
				Intent intent = new Intent(context,RegistActivity.class);
				int i = list.getImgPosition(bitmap);
				
				Bitmap bit =list.getImgData(i);
				ImageStatic.img2 = bit;
				intent.putExtra("_id",list.getId());
				intent.putExtra("tagPath",list.getImgPath(i) );
				intent.putExtra("url", list.getUrl(i));
				intent.putExtra("userId", getId());
				((Activity)context).startActivity(intent);
	}
	private String getId() {
		SharedPreferences prefs = context.getSharedPreferences("autopush",
                Context.MODE_PRIVATE);
		Editor e = prefs.edit();
		String id = prefs.getString("id", "");
		return id;
	}

	
}
