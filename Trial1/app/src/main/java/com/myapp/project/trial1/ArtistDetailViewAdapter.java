package com.myapp.project.trial1;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ArtistDetailViewAdapter extends RecyclerView.Adapter<ArtistDetailViewAdapter.ViewHolder> {

    private List<Album> list;
    private LayoutInflater inflater;


    public ArtistDetailViewAdapter(Context c, List<Album> albumList) {
        list = albumList;
        this.inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.artist_detail_view, parent, false);

        ArtistDetailViewAdapter.ViewHolder vh = new ArtistDetailViewAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Album curAlbum = list.get(position);

        Bitmap bitmap = new BitmapResult().result(curAlbum.getAlbumArt());
        if(bitmap != null){
            holder.imageView.setImageBitmap(bitmap);
        }
        holder.albumName.setText(curAlbum.getAlbumName());
        holder.year.setText("YEAR");
        holder.numOfSongs.setText(curAlbum.getSize() + " Songs");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        public ImageView imageView;
        public TextView albumName;
        public TextView year;
        public TextView numOfSongs;
        public RecyclerView albumSongList;
        private final Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            albumName = (TextView) itemView.findViewById(R.id.albumName_artistPanel);
            imageView = (ImageView) itemView.findViewById(R.id.albumView_artistPanel);
            year = (TextView) itemView.findViewById(R.id.year_artistPanel);
            numOfSongs = (TextView) itemView.findViewById(R.id.numOfSongs_artistPanel);
            /*managerType  = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

            if (savedInstanceState != null) {
                managerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
             }*/
        }

        @Override
        public void onClick(View view) {
            Log.i("onClick", "SUCCESS");
        }
    }
}
