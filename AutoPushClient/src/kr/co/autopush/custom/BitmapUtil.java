package kr.co.autopush.custom;

import android.graphics.BitmapFactory.Options;

public class BitmapUtil {
	public static Options getBitmapSize(Options options){
        int targetWidth = 0;
        int targetHeight = 0;
         
        if(options.outWidth > options.outHeight){    
            targetWidth = (int)(300 * 1.3);
            targetHeight = 300;
        }else{
            targetWidth = 300;
            targetHeight = (int)(300 * 1.3);
        }
 
        Boolean scaleByHeight = Math.abs(options.outHeight - targetHeight) >= Math.abs(options.outWidth - targetWidth);
        if(options.outHeight * options.outWidth * 2 >= 16384){
            double sampleSize = scaleByHeight
                ? options.outHeight / targetHeight
                : options.outWidth / targetWidth;
            options.inSampleSize = (int) Math.pow(2d, Math.floor(Math.log(sampleSize)/Math.log(2d)));
        }
        options.inJustDecodeBounds = false;
        options.inTempStorage = new byte[16*1024];
        options.inPurgeable=true;
		options.inDither= false;
         
        return options;
    }
}
