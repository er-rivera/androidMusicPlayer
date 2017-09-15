//package com.myapp.waveform.trial1;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.support.annotation.NonNull;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.AccelerateDecelerateInterpolator;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
//import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
//import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
//
//import java.util.List;
//
//public class ArtistDetailViewAdapter extends ExpandableRecyclerAdapter<Album, Song, ArtistDetailViewAdapter.AlbumViewHolder, ArtistDetailViewAdapter.SongViewHolder>{
//    private LayoutInflater inflater;
//    private Context c;
//
//    public ArtistDetailViewAdapter(Context c, List<Album> list){
//        super(list);
//        this.inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        this.c = c;
//    }
//
//    @NonNull
//    @Override
//    public AlbumViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
//        View v = LayoutInflater.from(parentViewGroup.getContext()).
//                inflate(R.layout.artist_detail_view, parentViewGroup, false);
//
//        ArtistDetailViewAdapter.AlbumViewHolder vh = new ArtistDetailViewAdapter.AlbumViewHolder(v);
//        return vh;
//    }
//
//    @NonNull
//    @Override
//    public SongViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
//        View v = LayoutInflater.from(childViewGroup.getContext()).
//                inflate(R.layout.list_item_album_songs, childViewGroup, false);
//
//        ArtistDetailViewAdapter.SongViewHolder vh = new ArtistDetailViewAdapter.SongViewHolder(v);
//        return vh;
//    }
//
//    @Override
//    public void onBindParentViewHolder(@NonNull AlbumViewHolder parentViewHolder, int parentPosition, @NonNull Album parent) {
//        parentViewHolder.bind(parent);
//    }
//
//    @Override
//    public void onBindChildViewHolder(@NonNull SongViewHolder childViewHolder, int parentPosition, int childPosition, @NonNull Song child) {
//        childViewHolder.bind(child);
//    }
//
//
//    public class AlbumViewHolder extends ParentViewHolder{
//        private TextView albumName;
//        private ImageView albumArt;
//        private TextView albumYear;
//        private TextView numOfSongs;
//        private ImageButton collapseButton;
//        private LinearLayout albumDetail;
//        private ImageButton addQueue;
//        private ImageButton shuffle;
//
//        public AlbumViewHolder(View itemView){
//            super(itemView);
//            albumName = (TextView) itemView.findViewById(R.id.albumName_artistPanel);
//            numOfSongs = (TextView) itemView.findViewById(R.id.numOfSongs_artistPanel);
//            albumYear = (TextView) itemView.findViewById(R.id.year_artistPanel);
//            albumArt = (ImageView) itemView.findViewById(R.id.albumArt_artistPanel);
//            collapseButton = (ImageButton) itemView.findViewById(R.id.collapse_button_artistPanel);
//            albumDetail = (LinearLayout) itemView.findViewById(R.id.album_detail);
//            addQueue = (ImageButton) itemView.findViewById(R.id.queue_add_artistPanel);
//            shuffle = (ImageButton) itemView.findViewById(R.id.shuffle_artistPanel);
//        }
//
//        public void bind(Album album){
//
//            albumName.setText(album.getAlbumName());
//            numOfSongs.setText(String.valueOf(album.getSize()) + " Tracks");
//            if(album.getAlbumArt() != null) {
//                int i = album.getAlbumArt().length;
//                Log.i("BITMAP SIZE for " + album.getAlbumName() + " is", String.valueOf(i));
//            }
//            BitmapResult bitmapResult = new BitmapResult();
//            Bitmap bitmap = bitmapResult.resizeResult(album.getAlbumArt(),100,100);
//            albumArt.setImageBitmap(bitmap);
//            albumDetail.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if(isExpanded()){
//                        collapseView();
//                        float deg = collapseButton.getRotation() + 180F;
//                        collapseButton.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
//                    }
//                    else{
//                        expandView();
//                        float deg = collapseButton.getRotation() + 180F;
//                        collapseButton.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
//                    }
//                }
//            });
//            addQueue.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(c.getApplicationContext(), R.string.UNAVAILABLE, Toast.LENGTH_SHORT).show();
//                }
//            });
//            shuffle.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(c.getApplicationContext(), R.string.UNAVAILABLE, Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }
//
//    public class SongViewHolder extends ChildViewHolder{
//        private TextView songName;
//        private LinearLayout container;
//
//        public SongViewHolder(View itemView){
//            super(itemView);
//            songName = (TextView) itemView.findViewById(R.id.songName_itemList);
//            container = (LinearLayout) itemView.findViewById(R.id.song_container_itemList);
//
//        }
//
//        public void bind(final Song song){
//            songName.setText(song.getTitle());
//            container.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    DetailActivity.setAlbumPlaylist();
//                    MainActivity.musicSrv.playSong(song);
//                    MainActivity.updateSongInformation();
//                    DetailActivity.updateSongInformation();
//                    if(MainActivity.musicSrv.isPng()){
//                        if(MainActivity.musicSrv != null){
//                            MainActivity.musicSrv.pauseMedia();
//                        }
//                    }
//                    else{
//                        if(MainActivity.musicSrv != null){
//                            MainActivity.musicSrv.go();
//                        }
//                    }
//                }
//            });
//        }
//    }
//}
//
