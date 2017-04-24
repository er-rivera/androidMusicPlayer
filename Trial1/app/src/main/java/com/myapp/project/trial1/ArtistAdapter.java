package com.myapp.project.trial1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {

    public static HashTable table;
    private static List<Song> artistList;
    public LayoutInflater inflater;

    public ArtistAdapter(Context context, List<Song> songList){
        artistList = songList;
        setTable();
        artistList = table.getNoDuplicatesArtistList();
        Collections.sort(artistList,Song.SongArtistComparator);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ArtistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.artist_item_layout, parent, false);

        ArtistViewHolder vh = new ArtistViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ArtistViewHolder holder, int position) {
        Song currSong = artistList.get(position);

        Bitmap bitmap = new BitmapResult().result(currSong.getImage());
        if (bitmap != null) {
            holder.albumArt.setImageBitmap(bitmap);
        } else {
            //OOPS
        }
        holder.artistText.setText(currSong.getArtist());
    }

    @Override
    public int getItemCount() {
        return artistList.size();
    }

    public static class ArtistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView artistText;
        private ImageView albumArt;
        private final Context context;

        public ArtistViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            artistText = (TextView) itemView.findViewById(R.id.artistName_artistList);
            albumArt = (ImageView) itemView.findViewById(R.id.albumView_artistList);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, DetailActivity.class);
            Song song = artistList.get(getAdapterPosition());
            ArrayList<Song> testingList = table.getArtistList(song.getArtist());
            for(int i = 0; i < testingList.size(); i++){
                Log.i("table contents",testingList.get(i).getTitle());
            }
            //ntent.putExtra("song", song);
            intent.putExtra("artistName",song.getArtist());
            //intent.putExtra("arrayList", testingList);
            //intent.putParcelableArrayListExtra("list", testingList);
            context.startActivity(intent);
        }

        public interface ItemClickListener {
            void onItemClick(View view, int position);
        }
    }

    void setTable(){
        table = new HashTable(artistList.size());
        for(int i =0; i < artistList.size();i++){
            table.insert(artistList.get(i).getArtist(),artistList.get(i));
        }
    }
}
