package com.myapp.waveform.trial1.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.provider.Telephony;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.myapp.waveform.trial1.BitmapResult;
import com.myapp.waveform.trial1.MainActivity;
import com.myapp.waveform.trial1.MediaNotificationHelper;
import com.myapp.waveform.trial1.PlaybackStatus;
import com.myapp.waveform.trial1.R;
import com.myapp.waveform.trial1.Song;
import com.myapp.waveform.trial1.Util.StorageUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MusicService extends MediaBrowserServiceCompat implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener,AudioManager.OnAudioFocusChangeListener{
    public static MediaPlayer player;
    private List<Song> songs;
    private int resumePosition;
    public static int songPosn;
    //private final IBinder musicBind = new MusicBinder();
    private final IBinder iBinder = new LocalBinder();
    byte[] art;
    String title;
    String artist;
    String album;
    String duration;
    int size;
    private Random random;
    //private Song currentSong;
    private AudioManager audioManager;
    private boolean isShuffle = false;
    private boolean isRepeat = false;
    private boolean ongoingCall = false;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager telephonyManager;
    private ArrayList<Song> audioList;
    private int songIndex = -1;
    private Song activeSong;
    private Context context = this;
    private MediaSessionManager mediaSessionManager;
    private MediaSessionCompat mediaSession;
    private MediaControllerCompat.TransportControls transportControls;
    private static final int NOTIFICATION_ID = 101;

    public static final String ACTION_PLAY = "com.myapp.waveform.trial1.ACTION_PLAY";
    public static final String ACTION_PAUSE = "com.myapp.waveform.trial1.ACTION_PAUSE";
    public static final String ACTION_PREVIOUS = "com.myapp.waveform.trial1.ACTION_PREVIOUS";
    public static final String ACTION_NEXT = "com.myapp.waveform.trial1.ACTION_NEXT";
    public static final String ACTION_STOP = "com.myapp.waveform.trial1.ACTION_STOP";

    //private String mediaFile;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Intent",intent.toString());
        try {
            StorageUtil storage = new StorageUtil(getApplicationContext());
            //storage.loadSongs();
            audioList = MainActivity.songList;
            songIndex = storage.loadSongIndex();
            if (songIndex != -1 && songIndex < audioList.size()) {
                //index is in a valid range
                activeSong = audioList.get(songIndex);
            } else {
                stopSelf();
            }
            //An audio file is passed to the service through putExtra();
        } catch (NullPointerException e) {
            stopSelf();
        }

        if (!requestAudioFocus()) {
            Log.d("Error: ","Could not gain Audio Focus");
            //Could not gain focus
            stopSelf();
        }
        if (mediaSessionManager == null){
            try {
                initMediaSession();
                initMusicPlayer();
            } catch (RemoteException e) {
                e.printStackTrace();
                stopSelf();
            }
            MediaNotificationHelper.buildNotification(PlaybackStatus.PLAYING, context, mediaSession);
        }

        handleIncomingActions(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        callStateListener();
        registerBecomingNoisyReceiver();
        register_playNewAudio();
    }

    public void initMusicPlayer(){
        player = new MediaPlayer();
        songPosn = 0;
        random = new Random();
        //player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnBufferingUpdateListener(this);
        player.setOnSeekCompleteListener(this);
        player.setOnInfoListener(this);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);

        player.reset();

        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        Uri trackUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                activeSong.getId());
        try {
            // Set the data source to the mediaFile location
            player.setDataSource(getApplicationContext(), trackUri);
        } catch (IOException e) {
            e.printStackTrace();
            stopSelf();
        }
        player.prepareAsync();
    }

    public void setShuffle(){
        if(isShuffle)isShuffle = false;
        else isShuffle = true;
    }

    private void initMediaSession() throws RemoteException {
        if(mediaSessionManager != null){
            return;
        }
        mediaSessionManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);
        mediaSession = new MediaSessionCompat(getApplicationContext(), "AudioPlayer");
        transportControls = mediaSession.getController().getTransportControls();
        mediaSession.setActive(true);
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        updateMetaData();
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                super.onPlay();
                resumeMedia();
                MediaNotificationHelper.buildNotification(PlaybackStatus.PLAYING, context, mediaSession);
            }

            @Override
            public void onPause() {
                super.onPause();
                pauseMedia();
                MediaNotificationHelper.buildNotification(PlaybackStatus.PAUSED, context, mediaSession);
            }

            @Override
            public void onSkipToNext() {
                super.onSkipToNext();
                Log.i("IN SKIP","NEXT");
                skipToNext();
                updateMetaData();
                MediaNotificationHelper.buildNotification(PlaybackStatus.PLAYING, context, mediaSession);
            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();
                skipToPrevious();
                updateMetaData();
                MediaNotificationHelper.buildNotification(PlaybackStatus.PLAYING, context, mediaSession);
            }

            @Override
            public void onStop() {
                super.onStop();
                removeNotification();
                //Stop the service
                stopSelf();
            }

            @Override
            public void onSeekTo(long pos) {
                super.onSeekTo(pos);
            }
        });
    }

    private void skipToNext(){
        if(songIndex == audioList.size() - 1){
            songIndex = 0;
            activeSong = audioList.get(songIndex);
        }
        else{
            activeSong = audioList.get(++songIndex);
        }

        new StorageUtil(getApplicationContext()).storeSongIndex(songIndex);
        stopMedia();
        player.reset();
        initMusicPlayer();
    }

    private void skipToPrevious() {

        if (songIndex == 0) {
            //if first in playlist
            //set index to the last of audioList
            songIndex = audioList.size() - 1;
            activeSong = audioList.get(songIndex);
        } else {
            //get previous in playlist
            activeSong = audioList.get(--songIndex);
        }

        //Update stored index
        new StorageUtil(getApplicationContext()).storeSongIndex(songIndex);

        stopMedia();
        //reset mediaPlayer
        player.reset();
        initMusicPlayer();
    }

    private void updateMetaData(){
        BitmapResult bm = new BitmapResult();
        Bitmap albumArt = bm.result(activeSong.getImage());
        mediaSession.setMetadata(new MediaMetadataCompat.Builder()
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART,albumArt)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST,activeSong.getArtist())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM,activeSong.getAlbum())
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE,activeSong.getTitle()).build());
        //MainActivity.updateSongInformation(activeSong);
    }

    private void removeNotification(){
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    public void setList(List<Song> theSongs){
        songs = theSongs;
        size = songs.size();
    }

    private void handleIncomingActions(Intent playbackAction) {
        if (playbackAction == null || playbackAction.getAction() == null) return;

        String actionString = playbackAction.getAction();
        if (actionString.equalsIgnoreCase(ACTION_PLAY)) {
            transportControls.play();
        } else if (actionString.equalsIgnoreCase(ACTION_PAUSE)) {
            transportControls.pause();
        } else if (actionString.equalsIgnoreCase(ACTION_NEXT)) {
            // Next track
            Log.i("VALUE",String.valueOf(songIndex) + " Size: " + String.valueOf(songs.size()));
            transportControls.skipToNext();
        } else if (actionString.equalsIgnoreCase(ACTION_PREVIOUS)) {
            transportControls.skipToPrevious();
        } else if (actionString.equalsIgnoreCase(ACTION_STOP)) {
            transportControls.stop();
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onAudioFocusChange(int i) {
        switch(i){
            case AudioManager.AUDIOFOCUS_GAIN:
                if(player == null){
                    initMusicPlayer();
                }
                else if(!player.isPlaying()){
                    player.start();
                }
                player.setVolume(1.0f,1.0f);
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                if(player.isPlaying()){
                    player.stop();
                }
                player.release();
                player = null;
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                if(player.isPlaying()){
                    player.setVolume(0.1f,0.1f);
                }
                break;
        }
    }

    private boolean requestAudioFocus(){
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    }

    private boolean removeAudioFocus(){
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == audioManager.abandonAudioFocus(this);
    }

    public void playSong(){
        player.reset();
        Song playSong = songs.get(songPosn);
        //currentSong = playSong;
        long currSong = playSong.getId();
        String currTitle = playSong.getTitle();
        byte[] currArt = playSong.getImage();
        String currArtist = playSong.getArtist();
        String currAlbum = playSong.getAlbum();
        String currDuration = playSong.getDurationInString();
        setArt(currArt);
        setTitle(currTitle);
        setAlbum(currAlbum);
        setArtist(currArtist);
        setDuration(currDuration);
        //set URI
        Uri trackUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong);
        try {
            Log.i("Track Uri", trackUri.toString());
            player.setDataSource(getApplicationContext(), trackUri);
        }catch (Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
            stopSelf();
        }
        player.prepareAsync();
    }

    public void playSong(Song s){
        player.reset();
        Song playSong = s;



        long currSong = playSong.getId();
        String currTitle = playSong.getTitle();
        byte[] currArt = playSong.getImage();
        String currArtist = playSong.getArtist();
        String currAlbum = playSong.getAlbum();
        String currDuration = playSong.getDurationInString();
        setArt(currArt);
        setTitle(currTitle);
        setAlbum(currAlbum);
        setArtist(currArtist);
        setDuration(currDuration);
        //set URI
        Uri trackUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong);
        try {
            player.setDataSource(getApplicationContext(), trackUri);
        }catch (Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
        player.prepareAsync();
    }

    public void setSong(int songIndex) {
        Log.i("Index", String.valueOf(songIndex));
        songPosn = songIndex;
    }

    public int getPosition(){
        return player.getCurrentPosition();
    }

    public int getDur(){
        return player.getDuration();
    }

    public static boolean isPng(){
        Log.i("IS IT PLAYING", String.valueOf(player.isPlaying()));
        return player.isPlaying();
    }

    private void pauseMedia() {
        if (player.isPlaying()) {
            player.pause();
            resumePosition = player.getCurrentPosition();
        }
    }

    private void stopMedia() {
        if (player == null) return;
        if (player.isPlaying()) {
            player.stop();
        }
    }

    private void playMedia() {
        if (!player.isPlaying()) {
            player.start();
        }
    }

    private void resumeMedia() {
        if (!player.isPlaying()) {
            player.seekTo(resumePosition);
            player.start();
        }
    }
    public void seek(int posn){
        player.seekTo(posn);
    }

    public void go(){
        player.start();
    }

    public void setArt(byte [] a){
        art = a;
    }

    public byte[] getArt(){
        return art;
    }

    public void setTitle(String t){
        title = t;
    }

    public  String getTitle(){
        return title;
    }

    public void setArtist(String a){
        artist = a;
    }

    public String getArtist(){ return artist; }

    public void setAlbum(String a){ album = a; }

    public String getAlbum(){ return album; }

    public void setDuration(String d){ duration = d; }

    public String getDuration(){ return duration; }

    public Song getSong(){
        return activeSong;
    }

    public int getListSize(){return size;}

    public void playPrev(){
        songPosn--;
        if(songPosn < 0)
            songPosn = songs.size()-1;
        playSong();
    }

    public void playNext(){
//        if(isShuffle){
//            int newSong = songPosn;
//            while(newSong == songPosn){
//                newSong = random.nextInt(songs.size());
//            }
//            songPosn = newSong;
//        }
//        else{
//            songPosn++;
//            if(songPosn >= songs.size())
//                songPosn = 0;
//        }
//        playSong();
        transportControls.skipToNext();
    }

    private BroadcastReceiver becomingNoisyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            pauseMedia();
            MediaNotificationHelper.buildNotification(PlaybackStatus.PAUSED, context, mediaSession);
        }
    };

    private void callStateListener(){
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        phoneStateListener = new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state){
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                    case TelephonyManager.CALL_STATE_RINGING:
                        if(player != null){
                            pauseMedia();
                            ongoingCall = true;
                        }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        if(player != null){
                            if(ongoingCall){
                                ongoingCall = false;
                                resumeMedia();
                            }
                        }
                        break;
                }
            }
        };

        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }


    private void registerBecomingNoisyReceiver(){
        IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(becomingNoisyReceiver,intentFilter);
    }

    @Override
    public boolean onUnbind(Intent intent){
        return  false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(player.getCurrentPosition() > 0){
            mp.reset();
            //playNext();
        }
        if(isRepeat){
            playSong();
        }
        else if(isShuffle){
/*            Random rand = new Random();
            int currentSongIndex = rand.nextInt((MainActivity.musicSrv.size - 1) - 0 + 1) + 0;
            songPosn = currentSongIndex;
            playSong();*/
        }
        else{
            if(songPosn < size - 1){
                songPosn = songPosn + 1;
                playSong();
            }
            else{
                songPosn = 0;
                playSong();
            }
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                Log.d("MediaPlayer Error", "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.d("MediaPlayer Error", "MEDIA ERROR SERVER DIED " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.d("MediaPlayer Error", "MEDIA ERROR UNKNOWN " + extra);
                break;
        }
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //START PLAYBACK
        playMedia();
    }

    public class LocalBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    private BroadcastReceiver playNewSong = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            songIndex = new StorageUtil(getApplicationContext()).loadSongIndex();
            if(songIndex != -1 && songIndex < audioList.size()){
                activeSong = audioList.get(songIndex);
            }
            else{
                stopSelf();
            }

            stopMedia();
            player.reset();
            initMusicPlayer();
            updateMetaData();
            MediaNotificationHelper.buildNotification(PlaybackStatus.PLAYING, context, mediaSession);
        }
    };

    private void register_playNewAudio(){
        IntentFilter intentFilter = new IntentFilter(MainActivity.PLAY_NEW_AUDIO);
        registerReceiver(playNewSong,intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.reset();
            player.release();
        }
        removeAudioFocus();
        if(phoneStateListener != null){
            telephonyManager.listen(phoneStateListener,PhoneStateListener.LISTEN_NONE);
        }
        removeNotification();
        unregisterReceiver(becomingNoisyReceiver);
        unregisterReceiver(playNewSong);
        new StorageUtil(getApplicationContext()).clearCachedAudioPlaylist();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
        return null;
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {

    }
}

