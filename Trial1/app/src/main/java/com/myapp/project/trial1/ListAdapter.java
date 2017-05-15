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
        BitmapResult bm = new BitmapResult();
        Bitmap b = bm.resizeResult(currSong.getImage(),80,80);
        albumArt.setImageBitmap(b);
        convertView.setTag(position);

        return convertView;
    }

    private boolean isMarqueed(String text,int width, TextView txtView){
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
