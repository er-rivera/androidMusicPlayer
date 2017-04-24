package com.myapp.project.trial1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private ListView lv;
    private SlidingUpPanelLayout slidingLayout;
    private List<Song> individualArtistList;
    private List<Album> albumList;
    private RecyclerView albumMasterRecycler;
    private LinearLayoutManager llm;
    ArtistDetailViewAdapter masterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle extras = getIntent().getExtras();
        String artistName = extras.getString("artistName");
        individualArtistList = ArtistAdapter.table.getArtistList(artistName);
        albumList = new ArrayList<>();
        Collections.sort(individualArtistList,Song.SongAlbumComparator);
        initAlbumList();

        CollapsingToolbarLayout cToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        cToolbar.setTitle(artistName);
        cToolbar.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.white));
        cToolbar.setCollapsedTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        Context context = this;
        cToolbar.setContentScrimColor(ContextCompat.getColor(context,R.color.colorAccent));
        slidingLayout = MainActivity.slidingLayout;

        albumMasterRecycler = (RecyclerView) findViewById(R.id.artist_detail_recycler);
        albumMasterRecycler.setHasFixedSize(true);
        llm =  new LinearLayoutManager(this);
        albumMasterRecycler.setLayoutManager(new LinearLayoutManager(this));
        /*managerType  = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            managerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }*/

        masterAdapter = new ArtistDetailViewAdapter(this,albumList);//R.layout.artist_item_layout
        albumMasterRecycler.setAdapter(masterAdapter);

        //artwork.setImageBitmap(individualArtistList.get(0).getImage()); need bitmap
        //Song song = getIntent().getParcelableExtra("song");
        //ArrayList<Song> songList = getIntent().getParcelableArrayListExtra("arrayList");
        /*if (song != null && song.getImage() != null) {
            artistTitle = song.getArtist();
            albumTitle.setText(song.getAlbum());
        }
        else{
            artistTitle = "null";
        }*/
        //}
        //List<Song> list = (List<Song>) getIntent().getSerializableExtra("list");
/*        *//*Bundle bundle = getIntent().getExtras();*//*
        Song testSong = bundle.getParcelable("TESTSONG");*/
        //ArrayList<Song> songList = getIntent().getParcelableArrayListExtra("list")

/*        for(int i = 0;i <songList.size(); i++){
            System.out.println(songList.get(i).getArtist());
        }*/

        //getSupportActionBar().setTitle(songID);
/*        lv = (ListView) findViewById(R.id.artistView_albumScroll);
        String[] array = {"one", "two","three","four","five","six","seven","eight","nine","ten","eleven"
                ,"twelve","thirteen","fourteen","fifteen","sixteen","seventeen","eighteen","nineteen","twenty"};
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,android.R.id.text1,array);
        lv.setAdapter(adapter);*/
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initAlbumList(){
        Song song;
        String albumName = "";
        int curAlbum = -1;
        for(int i = 0; i < individualArtistList.size(); i++){
            song = individualArtistList.get(i);
            if(song.getAlbum().equals(albumName)){
                albumList.get(curAlbum).add(song);
            }
            else{
                curAlbum++;
                albumList.add(new Album(song.getImage(),song.getAlbum()));
                albumList.get(curAlbum).add(song);
            }
            albumName = song.getAlbum();
        }
        return;
    }

}
