package com.myapp.waveform.trial1;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
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

import com.myapp.waveform.trial1.Services.MusicService;

import com.myapp.waveform.trial1.Util.StorageUtil;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

import android.os.Handler;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener {

    private static final int REQUEST_EXTERNAL = 1;
    private Window window;
    private LinearLayout layoutView;
    private static LinearLayout playerLayout;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public static ArrayList<Song> songList;
    private ArrayList<Bitmap> albumArtList;
    private ArrayList<Album> albumList;
    private Intent playIntent;
    private Context temp = this;
    public static SlidingUpPanelLayout slidingLayout;
    private static SeekBar seekBar;
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private double startTime = 0;
    private double finalTime = 0;
    public static TextView songObj;
    public static TextView songBar;
    public static TextView artistAlbumObj;
    public static TextView artistAlbumBar;
    public static TextView curDurationObj;
    public static TextView totalDurationObj;
    public static ImageView artworkObj;
    public static ImageButton btnPlay;
    public static ImageButton btnPlaySliding;
    public static ImageButton btnPrev;
    public static ImageButton btnForward;
    public static ImageButton btnShuffle;
    public static ImageButton btnRepeat;
    public static ImageButton btnQueueBar;
    public static ImageButton btnAnalysis;
    private static  Utilities util;
    private static int vibrant = 0xFFFFFF;
    private static int vibrantLight = 0x000000;
    private static int vibrantDark = 0xFFFFFF;
    private static int muted = 0x000000;
    private static Handler handler = new Handler();
    private BitmapFactory.Options options;
    //public static MusicService musicSrv;
    //public boolean musicBound = false;
    private static MusicService player;
    private boolean serviceBound = false;
    public static final String PLAY_NEW_AUDIO = "com.myapp.waveform.trial1.PlayNewAudio";
    private String mMediaId;
    private MediaBrowserCompat mMediaBrowser;
    private static final String TAG = MainActivity.class.getSimpleName();
    //private BrowseAdapter mBrowserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionCheck();
        setContentView(R.layout.activity_main);
        songList = new ArrayList<Song>();
        albumArtList = new ArrayList<Bitmap>();
        albumList = new ArrayList<Album>();
        getSongList();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),songList,albumArtList);//albumList();

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        window = getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        playerLayout = (LinearLayout) findViewById(R.id.dragView);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        layoutView = (LinearLayout) findViewById(R.id.slidingContainer);
        slidingLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        slidingLayout.setPanelSlideListener(onSlideListener());
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        songObj = (TextView) findViewById(R.id.songName_slidingPanel);
        songBar = (TextView) findViewById(R.id.name);
        artistAlbumObj = (TextView) findViewById(R.id.artistAlbum_slidingPanel);
        artistAlbumBar = (TextView) findViewById(R.id.artistAlbum);
        btnPlay = (ImageButton) findViewById(R.id.btn_play_SlidingPanel);
        btnForward = (ImageButton) findViewById(R.id.btn_next_SlidingPanel);
        btnPrev = (ImageButton) findViewById(R.id.btn_prev_SlidingPanel);
        btnPlaySliding = (ImageButton) findViewById(R.id.btn_play_SlidingBar);
        btnShuffle = (ImageButton) findViewById(R.id.btn_shuffle_SlidingPanel);
        btnRepeat = (ImageButton) findViewById(R.id.btn_repeat_SlidingPanel);
        btnQueueBar = (ImageButton) findViewById(R.id.btn_menu_SlidingBar);
        artworkObj = (ImageView) findViewById(R.id.albumArt_slidingPanel);
        btnAnalysis = (ImageButton) findViewById(R.id.spek_SlidingPanel);
        dynamicImageViewAdjustment(artworkObj);
        util = new Utilities();
        totalDurationObj = (TextView) findViewById(R.id.durationTotal);
        curDurationObj = (TextView) findViewById(R.id.durationCurrent);
        seekBar.setOnSeekBarChangeListener(this);




        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(player.isPng()){
                    if(player != null){
                        //TODO: player.pauseMedia();
                        
                        btnPlay.setImageResource(R.drawable.btn_play_panel);
                        btnPlaySliding.setImageResource(R.drawable.btn_play);
                    }
                }
                else{
                    if(player != null){
                        //TODO: musicSrv.go();
                        btnPlay.setImageResource(R.drawable.btn_pause_panel);
                        btnPlaySliding.setImageResource(R.drawable.btn_pause);
                    }
                }
            }
        });

        btnPlaySliding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player.isPng()){
                    if(player != null){
                        //TODO: player.pauseMedia();
                        btnPlay.setImageResource(R.drawable.btn_play_panel);
                        btnPlaySliding.setImageResource(R.drawable.btn_play);
                    }
                }
                else{
                    if(player!= null){
                        //TODO: musicSrv.go();
                        btnPlay.setImageResource(R.drawable.btn_pause_panel);
                        btnPlaySliding.setImageResource(R.drawable.btn_pause);
                    }
                }
            }
        });

        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player != null){
                    player.playNext();
                    //TODO: musicSrv.playNext();
                    //updateSongInformation();
                }
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player != null){
                    //TODO: musicSrv.playPrev();
                    //updateSongInformation();
                }
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player != null){
                    //TODO: musicSrv.playPrev();
                    //updateSongInformation();
                }
            }
        });

        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player != null) {
                    //TODO: musicSrv.setShuffle();
                    //updateSongInformation();
                }
            }
        });

        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), R.string.UNAVAILABLE , Toast.LENGTH_SHORT).show();
            }
        });
        btnQueueBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), R.string.UNAVAILABLE, Toast.LENGTH_SHORT).show();
            }
        });

        btnAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), R.string.UNAVAILABLE, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.LocalBinder binder = (MusicService.LocalBinder)service;
            //get service
            player = binder.getService();
            //pass list
            player.setList(songList);
            serviceBound = true;
            Toast.makeText(MainActivity.this, "Service Bound", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

    public static void updateProgressBar(){
        handler.postDelayed(mUpdateTimeTask,100);
    }

    public static Runnable mUpdateTimeTask = new Runnable(){

        @Override
        public void run() {
            long totalDur = player.getDur();
            long curDur = player.getPosition();
            totalDurationObj.setText(util.millisecondsToTimer(totalDur));
            curDurationObj.setText(util.millisecondsToTimer(curDur));
            int progress = util.getProgressPercentage(curDur,totalDur);
            seekBar.setProgress(progress);
            handler.postDelayed(this,100);
        }
    };

    public void songPicked(View view){
        int index = Integer.parseInt(view.getTag().toString());
        playAudio(index);
        //updateSongInformation();
        //TODO(1): updateProgressBar();
        /*if(musicSrv.getListSize() != songList.size()){
            player.setList(songList);
        }
        int index = Integer.parseInt(view.getTag().toString();
        musicSrv.setSong(index);
        playAudio(songList.get(index));
        //musicSrv.playSong();
        btnPlaySliding.setImageResource(R.drawable.btn_pause);
        btnPlay.setImageResource(R.drawable.btn_pause_panel);
        String title = musicSrv.getTitle();
        String artist = musicSrv.getArtist();
        String album = musicSrv.getAlbum();
        String totalDuration = player.getDuration();
        byte[] albumArt = musicSrv.getArt();
        songObj.setText(title);
        artistAlbumObj.setText(artist + " - " + album);
        artistAlbumBar.setText(artist + " - " + album);
        totalDurationObj.setText(totalDuration);
        artistAlbumBar.setText(artist + " - " + album);
        songBar.setText(title);
        updateProgressBar();
        try {
            Bitmap bitmap = BitmapFactory.decodeByteArray(albumArt, 0, albumArt.length);
            Palette palette = Palette.from(bitmap).generate();
            int defaul = 0x000000;
            vibrant = palette.getVibrantColor(defaul);
            vibrantDark = palette.getDarkVibrantColor(defaul);
            muted = palette.getLightMutedColor(defaul);
            if(vibrant < 1000 && vibrant > 0){
                vibrant = 16777215;
            }
            if(vibrantLight < 1000 && vibrantLight > 0){
                vibrantLight = 16777215;
            }
            if(vibrantDark < 1000 && vibrantDark > 0){
                vibrantDark = 16777215;
            }
            //songObj.setTextColor(vibrantDark);
            //playerLayout.setBackgroundColor(vibrant);
            //artistAlbumBar.setTextColor(vibrant);
            //artistAlbumObj.setTextColor(vibrant);
            //playerLayout.setBackgroundColor(muted);

            artworkObj.setImageBitmap(bitmap);
        }catch (Exception e ){
            Log.i("ERROR", "OOPS");
        }*/
    }

    public static void updateSongInformation(Song song){
        if(player != null){
            //Song song  = player.getSong();
            String title = song.getTitle();
            String artist = song.getArtist();
            String album = song.getAlbum();
            String totalDuration = song.getDurationInString();
            byte[] albumArt = song.getImage();
            songObj.setText(title);
            artistAlbumObj.setText(artist + " - " + album);
            totalDurationObj.setText(totalDuration);
            artistAlbumBar.setText(artist + " - " + album);
            songBar.setText(title);
            updateProgressBar();
            try {
                Bitmap bitmap = BitmapFactory.decodeByteArray(albumArt, 0, albumArt.length);
                Palette palette = Palette.from(bitmap).generate();
                int defaul = 0x000000;
                vibrant = palette.getVibrantColor(defaul);
                vibrantLight = palette.getLightVibrantColor(defaul);
                vibrantDark = palette.getDarkVibrantColor(defaul);
                muted = palette.getMutedColor(defaul);
                if(vibrant < 1000 && vibrant > 0){
                    vibrant = R.color.colorPrimaryDark;
                }
                if(vibrantLight < 1000 && vibrantLight > 0){
                    vibrantLight = R.color.colorPrimaryDark;
                }
                if(vibrantDark < 1000 && vibrantDark > 0){
                    vibrantDark = R.color.colorPrimaryDark;
                }
            /*songObj.setTextColor(vibrantDark);
            artistAlbumBar.setTextColor(vibrant);
            artistAlbumObj.setTextColor(vibrant);
            Log.i("VIBRANT", String.valueOf(vibrant));*/
                //playerLayout.setBackgroundColor(muted);
                artworkObj.setImageBitmap(bitmap);

            }catch (Exception e ){
                Log.i("ERROR", "OOPS");
            }
        }

    }

    private void playAudio(int audioIndex){
        if(!serviceBound){
            StorageUtil storage = new StorageUtil(getApplicationContext());
            //storage.storeSongs(songList);
            storage.storeSongIndex(audioIndex);

            playIntent = new Intent(this, MusicService.class);
            //playIntent.putExtra("song", song);
            startService(playIntent);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            Log.i("IN METHOD","IF");
        }
        else{
            Log.i("IN METHOD","ELSE");
            StorageUtil storage = new StorageUtil(getApplicationContext());
            storage.storeSongIndex(audioIndex);

            Intent broadcastIntent = new Intent(PLAY_NEW_AUDIO);
            sendBroadcast(broadcastIntent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMediaBrowser = new MediaBrowserCompat(this,
                new ComponentName(this, MusicService.class),
                mConnectionCallback, null);
        /*if(!serviceBound){
            Intent playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
        else{
            //service is active
            //send with broadcast receiver
        }*/
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putBoolean("ServiceState", serviceBound);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        serviceBound = savedInstanceState.getBoolean("ServiceState");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(mUpdateTimeTask);
        if(serviceBound) {
            unbindService(musicConnection);
            player.stopSelf();
        }
    }

    public void getSongList(){
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();

        if(musicCursor != null & musicCursor.moveToFirst()){
            //get Columns
            int path = musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int titleColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ARTIST);
            int durationColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.DURATION);
            int albumColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);

            do {
                long thisId = musicCursor.getLong(idColumn);
                String thispath = musicCursor.getString(path);
                mmr.setDataSource(thispath);
                byte [] data;
                try{
                    data = mmr.getEmbeddedPicture();
                    ///edit to remove error for embedded picture
                }
                catch (Exception e){
                    data = null;
                }

                if (data != null) {
                    options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeByteArray(data, 0, data.length,options);
                    Bitmap bitmap = decodeSampledBitmapFromByteArray(data, 0, 100, 100);
                    bitmap = Bitmap.createScaledBitmap(bitmap,200,200,true);
                    albumArtList.add(bitmap);
                } else {
                    //OOPS
                }

                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String thisDuration = musicCursor.getString(durationColumn);
                String thisAlbum = musicCursor.getString(albumColumn);
                Song s = new Song(thisId, thisTitle, thisArtist, thisDuration, data, thisAlbum);
                songList.add(s);

            }while (musicCursor.moveToNext());

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public  void permissionCheck(){
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            return;
        }
        else{
            if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(this,"External Storage Permission is needed.", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_EXTERNAL);
        }
    }

    private void dynamicImageViewAdjustment(ImageView view){
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

    private MediaBrowserCompat.ConnectionCallback mConnectionCallback =
            new MediaBrowserCompat.ConnectionCallback(){
                @Override
                public void onConnected() {
                    super.onConnected();
                    Log.e(TAG, "onConnection");
                    Log.d(TAG, "onConnected: session token " + mMediaBrowser.getSessionToken());
                    if (mMediaId == null) {
                        mMediaId = mMediaBrowser.getRoot();
                    }
//                    if (mMediaId == null) {
//                        mMediaId = mMediaBrowser.getRoot();
//                    }
                }

                @Override
                public void onConnectionFailed() {
                    super.onConnectionFailed();
                    Log.e(TAG, "onConnectionFailed");
                }

                @Override
                public void onConnectionSuspended() {
                    super.onConnectionSuspended();
                    Log.e(TAG, "onConnectionSuspended");
                }
            };

    private SlidingUpPanelLayout.PanelSlideListener onSlideListener() {
        return new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {

                layoutView.setVisibility(View.VISIBLE);
                layoutView.setPadding(0,0,0,0);
            }

            @Override
            public void onPanelCollapsed(View view) {

            }

            @Override
            public void onPanelExpanded(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                }
                layoutView.setVisibility(View.INVISIBLE);
                int paddingPixel = 25;
                float density = getResources().getDisplayMetrics().density;
                int paddingDp = (int)(paddingPixel * density);
                layoutView.setPadding(0,paddingDp,0,0);
            }

            @Override
            public void onPanelAnchored(View view) {

            }

            @Override
            public void onPanelHidden(View view) {

            }
        };
    }



    @Override
    public void onBackPressed() {
        if (slidingLayout != null &&
                (slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
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
        int totalDur = player.getDur();
        int curDur = util.progressToTimer(seekBar.getProgress(), totalDur);
        player.seek(curDur);
        updateProgressBar();
    }


}
