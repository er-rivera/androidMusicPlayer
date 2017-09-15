package com.myapp.waveform.trial1;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class AlbumListDialogAdapter extends ArrayAdapter<Song> {

    private List<Song> songs;

    public AlbumListDialogAdapter(Context context, int resource, List<Song> objects) {
        super(context, resource, objects);
        songs = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.list_item_album_dialog, parent, false);
        }

        Song currSong = songs.get(position);
        TextView nameText = (TextView) convertView.findViewById(R.id.songName_itemList_dialog);
        TextView durationText;

        nameText.setText(currSong.getTitle());
        //isMarqueed(currSong.getTitle(),nameText.getWidth(),nameText);
        convertView.setTag(position);

        return convertView;
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

    private static boolean isMarqueed(String text,int width, TextView txtView){
        Paint paintedArea = new Paint();
        paintedArea.set(txtView.getPaint());
        boolean isMarquee = true;
        if(width > 0){
            int availableWidth = (int) (width - txtView.getPaddingLeft() -
                    txtView.getPaddingRight() - paintedArea.measureText(text));
            System.out.println("Width: " + availableWidth);
            isMarquee = false;
        }
        return isMarquee;
    }
}
