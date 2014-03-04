package kr.co.autopush.adapter;

import kr.co.autopush.bean.ImageDataList;
import kr.co.autopush.listener.ImageClickListener;

import org.json.JSONException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageGridAdapter extends BaseAdapter {
    
    private ImageDataList list;
    private Context context;
    private LayoutInflater inflater;
    //-----------------------------------------------------------
    // imageIDs는 이미지 파일들의 리소스 ID들을 담는 배열입니다.
    // 이 배열의 원소들은 자식 뷰들인 ImageView 뷰들이 무엇을 보여주는지를
    // 결정하는데 활용될 것입니다.
    
   
   
    public ImageGridAdapter(Context context,ImageDataList list) throws JSONException {
    	this.context = context;
    	this.inflater = LayoutInflater.from(context);
        this.list = list;
    	Log.i("auto_test", "imgListSize: "+ list.getImageViewSize()+ " imgPathSIze : "+ list.getImagePathSize());
    } 
    public int getCount() {
        return list.getImageViewSize();
    } 

    public Object getItem(int position) {
        return list.getImgData(position);
    } 

    public long getItemId(int position) {
        return position;
    } 

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = null;
        View v = convertView;
        if(v == null) {
            v = inflater.inflate(kr.co.autopush.R.layout.custom_gridimage, parent, false);
            v.setTag(kr.co.autopush.R.id.gridcustomimage,v.findViewById(kr.co.autopush.R.id.gridcustomimage));
        }
        
        	imageView = (ImageView)v.getTag(kr.co.autopush.R.id.gridcustomimage);
//        if (null != convertView)
//            imageView = (ImageView)convertView;
//        else {
//        	
        	imageView.setImageBitmap(list.getImgData(position));
//        	LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -1);
//        	imageView.setLayoutParams(layoutParams);
//			imageView.setBackgroundColor(Color.GREEN);
			
			Log.i("auto_test", position+"");

            ImageClickListener imageViewClickListener
                = new ImageClickListener(context,list,list.getImgData(position));
            imageView.setOnClickListener(imageViewClickListener);
            

       
        
        return v;
    }
	public void reCycleBitmap() {
		list.recycleBit();
	}

}
