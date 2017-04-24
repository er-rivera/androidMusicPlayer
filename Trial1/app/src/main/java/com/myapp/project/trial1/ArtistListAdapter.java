package com.myapp.project.trial1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by user on 12/22/16.
 */

public class ArtistListAdapter extends ArrayAdapter<Song> {

    private List<Song> songs;
    BitmapFactory.Options options;

    public ArtistListAdapter(Context context, int resource, List<Song> objects) {
        super(context, resource, objects);
        songs = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.artist_item_layout, parent, false);
        }

        Song currSong = songs.get(position);
        TextView artistText = (TextView) convertView.findViewById(R.id.artistName_artistList);
        ImageView albumArt = (ImageView) convertView.findViewById(R.id.albumView_artistList);

        if (currSong.getImage() != null) {
            options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(currSong.getImage(), 0, currSong.getImage().length,options);
            int imageHeight = options.outHeight;
            int imageWidth = options.outWidth;
            Bitmap bitmap = decodeSampledBitmapFromByteArray(currSong.getImage(), 0, 100, 100);
            bitmap = Bitmap.createScaledBitmap(bitmap,100,100,true);
            albumArt.setImageBitmap(bitmap);
        } else {
            //OOPS
        }
        artistText.setText(currSong.getArtist());
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
}
