package com.myapp.project.trial1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    private static Handler handler = new Handler();
    private static  Utilities util;
    private LinearLayout playerBarLayout;
    private Window window;
    private static SeekBar seekBar;
    private SlidingUpPanelLayout slidingLayout;
    public static List<Song> individualArtistList;
    private List<Album> albumList;
    private RecyclerView albumMasterRecycler;
    public static boolean usingAlbumSetList = false;
    public static TextView songNameSlidingLayout;
    public static TextView artistAlbumTextSlidingLayout;
    public static TextView artistAlbumBar;
    public static TextView songNameBar;
    public static TextView curDurSlidingLayout;
    public static TextView totalDurSlidingLayout;
    public static ImageView albumArtSlidingLayout;
    public static ImageButton btnPlaySlidingPanel;
    public static ImageButton btnPlayBar;
    public static ImageButton btnNextSlidingPanel;
    public static ImageButton btnPrevSlidingPanel;
    public static ImageButton btnAnalysisSlidingPanel;
    public static ImageButton btnShuffle;
    public static ImageButton btnRepeat;
    public static ImageButton btnMenuBar;

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
        util = new Utilities();
        CollapsingToolbarLayout cToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        cToolbar.setTitle(artistName);
        cToolbar.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.white));
        cToolbar.setCollapsedTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        Context context = this;
        cToolbar.setContentScrimColor(ContextCompat.getColor(context,R.color.colorAccent));
        playerBarLayout = (LinearLayout) findViewById(R.id.slidingContainer);
        slidingLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout_detail);
        slidingLayout.setPanelSlideListener(onSlideListener());
        artistAlbumBar = (TextView) findViewById(R.id.artistAlbum);
        songNameBar = (TextView) findViewById(R.id.name);
        songNameSlidingLayout = (TextView) findViewById(R.id.songName_slidingPanel);
        artistAlbumTextSlidingLayout = (TextView) findViewById(R.id.artistAlbum_slidingPanel);
        totalDurSlidingLayout = (TextView) findViewById(R.id.durationTotal);
        curDurSlidingLayout = (TextView) findViewById(R.id.durationCurrent);
        albumArtSlidingLayout = (ImageView) findViewById(R.id.albumArt_slidingPanel);
        dynamicImageViewAdjustment(albumArtSlidingLayout);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);
        btnPlaySlidingPanel = (ImageButton) findViewById(R.id.btn_play_SlidingPanel);
        btnPlayBar = (ImageButton) findViewById(R.id.btn_play_SlidingBar);
        btnNextSlidingPanel = (ImageButton) findViewById(R.id.btn_next_SlidingPanel);
        btnPrevSlidingPanel = (ImageButton) findViewById(R.id.btn_prev_SlidingPanel);
        btnRepeat = (ImageButton) findViewById(R.id.btn_repeat_SlidingPanel);
        btnShuffle = (ImageButton) findViewById(R.id.btn_shuffle_SlidingPanel);
        btnMenuBar = (ImageButton) findViewById(R.id.btn_menu_SlidingBar);
        btnAnalysisSlidingPanel = (ImageButton) findViewById(R.id.spek_SlidingPanel);
        btnPlaySlidingPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.musicSrv.isPng()){
                    if(MainActivity.musicSrv != null){
                        MainActivity.musicSrv.pausePlayer();
                        btnPlaySlidingPanel.setImageResource(R.drawable.btn_play_panel);
                        btnPlayBar.setImageResource(R.drawable.btn_play);
                    }
                }
                else{
                    if(MainActivity.musicSrv != null){
                        MainActivity.musicSrv.go();
                        btnPlaySlidingPanel.setImageResource(R.drawable.btn_pause_panel);
                        btnPlayBar.setImageResource(R.drawable.btn_pause);
                    }
                }
            }
        });

        btnPlayBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.musicSrv.isPng()){
                    if(MainActivity.musicSrv != null){
                        MainActivity.musicSrv.pausePlayer();
                        btnPlaySlidingPanel.setImageResource(R.drawable.btn_play_panel);
                        btnPlayBar.setImageResource(R.drawable.btn_play);
                    }
                }
                else{
                    if(MainActivity.musicSrv != null){
                        MainActivity.musicSrv.go();
                        btnPlaySlidingPanel.setImageResource(R.drawable.btn_pause_panel);
                        btnPlayBar.setImageResource(R.drawable.btn_pause);
                    }
                }
            }
        });
        btnNextSlidingPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.musicSrv.isPrepared()){
                    MainActivity.musicSrv.playNext();
                    updateSongInformation();
                }
            }
        });

        btnPrevSlidingPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.musicSrv.isPrepared()){
                    MainActivity.musicSrv.playPrev();
                    updateSongInformation();
                }
            }
        });

        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), R.string.UNAVAILABLE, Toast.LENGTH_SHORT).show();
            }
        });

        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), R.string.UNAVAILABLE, Toast.LENGTH_SHORT).show();
            }
        });

        btnMenuBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), R.string.UNAVAILABLE, Toast.LENGTH_SHORT).show();
            }
        });
        btnAnalysisSlidingPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), R.string.UNAVAILABLE, Toast.LENGTH_SHORT).show();
            }
        });



        albumMasterRecycler = (RecyclerView) findViewById(R.id.artist_detail_recycler);
        albumMasterRecycler.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        albumMasterRecycler.setLayoutManager(new LinearLayoutManager(this));
        /*managerType  = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            managerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }*/

        masterAdapter = new ArtistDetailViewAdapter(this,albumList);//R.layout.artist_item_layout
        albumMasterRecycler.setAdapter(masterAdapter);
        if(MainActivity.musicSrv.isPrepared()){
            updateSongInformation();
        }
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(mUpdateTimeTask);
        super.onDestroy();
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

    public static void updateSongInformation(){
        String title = MainActivity.musicSrv.getTitle();
        String artist = MainActivity.musicSrv.getArtist();
        String album = MainActivity.musicSrv.getAlbum();
        String totalDuration = MainActivity.musicSrv.getDuration();
        byte[] albumArt = MainActivity.musicSrv.getArt();
        songNameSlidingLayout.setText(title);
        artistAlbumTextSlidingLayout.setText(artist + " - " + album);
        //totalDurSlidingLayout.setText(totalDuration);
        artistAlbumBar.setText(artist + " - " + album);
        songNameBar.setText(title);
        BitmapResult bitmapResult = new BitmapResult();
        Bitmap bitmap  = bitmapResult.result(albumArt);
        albumArtSlidingLayout.setImageBitmap(bitmap);
        updateProgressBar();
        if(MainActivity.musicSrv.isPng()){
            if(MainActivity.musicSrv != null){
                btnPlaySlidingPanel.setImageResource(R.drawable.btn_pause_panel);
                btnPlayBar.setImageResource(R.drawable.btn_pause);
            }
        }
        else{
            if(MainActivity.musicSrv != null){
                btnPlaySlidingPanel.setImageResource(R.drawable.btn_play_panel);
                btnPlayBar.setImageResource(R.drawable.btn_play);
            }
        }
    }

    public static void setAlbumPlaylist(){
        if(!usingAlbumSetList){
            MainActivity.musicSrv.setList(individualArtistList);
        }
    }

    public static void updateProgressBar(){
        handler.postDelayed(mUpdateTimeTask,100);
    }

    public static Runnable mUpdateTimeTask = new Runnable(){

        @Override
        public void run() {
            long totalDur = MainActivity.musicSrv.getDur();
            long curDur = MainActivity.musicSrv.getPosn();
            totalDurSlidingLayout.setText(util.millisecondsToTimer(totalDur));
            curDurSlidingLayout.setText(util.millisecondsToTimer(curDur));
            int progress = util.getProgressPercentage(curDur,totalDur);
            seekBar.setProgress(progress);
            handler.postDelayed(this,100);
        }
    };

    private SlidingUpPanelLayout.PanelSlideListener onSlideListener(){
        return new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                playerBarLayout.setVisibility(View.VISIBLE);
                playerBarLayout.setPadding(0,0,0,0);
            }

            @Override
            public void onPanelCollapsed(View panel) {

            }

            @Override
            public void onPanelExpanded(View panel) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                }
                playerBarLayout.setVisibility(View.INVISIBLE);
                int paddingPixel = 25;
                float density = getResources().getDisplayMetrics().density;
                int paddingDp = (int)(paddingPixel * density);
                playerBarLayout.setPadding(0,paddingDp,0,0);
            }

            @Override
            public void onPanelAnchored(View panel) {

            }

            @Override
            public void onPanelHidden(View panel) {

            }
        };
    }

    private void dynamicImageViewAdjustment(ImageView view){
        window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        Display display;
        display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = display.getWidth();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        params.height = width;
        view.setLayoutParams(params);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        handler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        handler.removeCallbacks(mUpdateTimeTask);
        int totalDur = MainActivity.musicSrv.getDur();
        int curDur = util.progressToTimer(seekBar.getProgress(), totalDur);
        MainActivity.musicSrv.seek(curDur);
        updateProgressBar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }
}
