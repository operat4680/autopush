package kr.co.autopush.adapter;

import java.util.List;
import kr.co.autopush.bean.TaskData;
import kr.co.autopush.listener.TaskImageClickListener;

import org.json.JSONException;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class TaskGridAdapter extends BaseAdapter {

    private List<TaskData> list;
    private Context context;
    private LayoutInflater inflater;
    private int x=230;
    private int y=230;
    public TaskGridAdapter(Context context,List<TaskData> list) throws JSONException {
    	this.context = context;
    	this.inflater = LayoutInflater.from(context);
    	this.list = list;
   
    	Log.i("auto_test", list.size()+"");
    } 
    public int getCount() {
        return list.size()+1;
    } 

    public Object getItem(int position) {
        return list.get(position).getV();
    } 

    public long getItemId(int position) {
        return position;
    } 

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	ImageView imageView = null;
    	ImageView iconView = null;
    	View v = convertView;
    	TextView name;
    	TaskImageClickListener imageViewClickListener;
    	if(position!=list.size()){
    		 if(v == null) {
    	            v = inflater.inflate(kr.co.autopush.R.layout.custom_square, parent, false);
    	            v.setTag(kr.co.autopush.R.id.newanderror,v.findViewById(kr.co.autopush.R.id.newanderror));
    	            v.setTag(kr.co.autopush.R.id.picture, v.findViewById(kr.co.autopush.R.id.picture));
    	            v.setTag(kr.co.autopush.R.id.customtext, v.findViewById(kr.co.autopush.R.id.customtext));
    	       }
    		 	
    	        imageView = (ImageView)v.getTag(kr.co.autopush.R.id.picture);
    	        name = (TextView)v.getTag(kr.co.autopush.R.id.customtext);
    	       
    	        if(list.get(position).getError()>3){
    	        	imageView.setImageResource(kr.co.autopush.R.drawable.icon_21);
    	        }
    	        else{
    	        	Bitmap bit = list.get(position).getV();
            	    imageView.setImageBitmap(bit);
    	        }
    	       
    	        
    	        name.setText(list.get(position).getName());
    	        
    	        imageView.setAdjustViewBounds(true);
    	        
    	        v.setPadding(8,8,8,12);
//    	        v.setLayoutParams(new GridView.LayoutParams(x,y));
//	
//	            
    	        if(list.get(position).isNew()){
    		 		iconView = (ImageView)v.getTag(kr.co.autopush.R.id.newanderror);
    		 		iconView.setImageResource(kr.co.autopush.R.drawable.new_btn);
    	        }
    	        
    	        if(list.get(position).getError()>3){
    	        	
    	        	imageViewClickListener = new TaskImageClickListener(context,list.get(position).getId(),true);
    	        	
    	        }
    	        else{
    	        	imageViewClickListener
	                = new TaskImageClickListener(context,list.get(position).getId(),false);
    	        	
    	        }
    	        imageView.setOnClickListener(imageViewClickListener);
    	        y= v.getLayoutParams().height;
    	        x =v.getLayoutParams().width;
    	        return v;
       
    	}
    	else{
    
		        imageView = new ImageView(context);
	    		imageView.setImageResource(kr.co.autopush.R.drawable.plus1);
	    	
		    	imageView.setAdjustViewBounds(true); 
		    	imageView.setPadding(8,8,8,12);
		    	
		    	imageView.setLayoutParams(new GridView.LayoutParams(x,y));
		    	imageViewClickListener = new TaskImageClickListener(context,null,false);
		    	imageView.setOnClickListener(imageViewClickListener);
		    	return imageView;
    		
    	}
    }
    
    public void reCycleList(){
    	for(int i=0;i<list.size();i++){
    		list.get(i).getV().recycle();
    	}
    	
    }
}
