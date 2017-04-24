package com.myapp.project.trial1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by user on 4/11/17.
 */

public class BitmapResult {
    private BitmapFactory.Options options;

    public Bitmap result(byte[] value){
        if (value != null) {
            options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(value, 0, value.length,options);
            int imageHeight = options.outHeight;
            int imageWidth = options.outWidth;
            Bitmap bitmap = decodeSampledBitmapFromByteArray(value, 0, 100, 100);
            bitmap = Bitmap.createScaledBitmap(bitmap,100,100,true);
            return bitmap;
        } else {
            return null;
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight){
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if(height > reqHeight || width > reqWidth){
            final int halfHeight =  height / 2;
            final int halfWidth = width / 2;

            while((halfHeight / inSampleSize >= reqHeight) && (halfWidth / inSampleSize >= reqWidth)){
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromByteArray(byte[] array, int i , int reqWidth, int reqHeight){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(array, i, array.length, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(array, i , array.length, options);
    }
}
