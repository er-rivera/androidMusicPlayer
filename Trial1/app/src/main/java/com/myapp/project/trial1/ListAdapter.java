package com.myapp.project.trial1;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends ArrayAdapter<Song> {

    private List<Song> songs;
    BitmapFactory.Options options;

    public ListAdapter(Context context, int resource, List<Song> objects) {
        super(context, resource, objects);
        songs = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.song_item_layout, parent, false);
        }

        Song currSong = songs.get(position);
        TextView nameText = (TextView) convertView.findViewById(R.id.songName_songList);
        TextView artistText = (TextView) convertView.findViewById(R.id.artistName_songList);
        ImageView albumArt = (ImageView) convertView.findViewById(R.id.albumView_songList);

        nameText.setText(currSong.getTitle());
        isMarqueed(currSong.getTitle(),nameText.getWidth(),nameText);
        artistText.setText(currSong.getArtist() + " --- " + currSong.getDurationInString());
        if (currSong.getImage() != null) {
            options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(currSong.getImage(), 0, currSong.getImage().length,options);
            int imageHeight = options.outHeight;
            int imageWidth = options.outWidth;
            Bitmap bitmap = decodeSampledBitmapFromByteArray(currSong.getImage(), 0, 80, 80);
            bitmap = Bitmap.createScaledBitmap(bitmap,80,80,true);
            Log.i("IMAGE HEIGHT", String.valueOf(bitmap.getHeight()));
            Log.i("IMAGE WIDTH", String.valueOf(bitmap.getWidth()));
            albumArt.setImageBitmap(bitmap);
        } else {
            //Oops
        }
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
