package com.myapp.project.trial1;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;
import java.util.List;
import java.util.Random;



public class MusicService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener{
    public static MediaPlayer player;
    private List<Song> songs;
    public static int songPosn;
    private final IBinder musicBind = new MusicBinder();
    byte[] art;
    String title;
    String artist;
    String album;
    String duration;
    int size;
    private Random random;
    private boolean isShuffle = false;
    private boolean isRepeat = false;
    private boolean isPrepared = false;
    public void onCreate(){
        super.onCreate();
        songPosn = 0;
        random = new Random();
        player = new MediaPlayer();
        initMusicPlayer();
    }

    public void setShuffle(){
        if(isShuffle)isShuffle = false;
        else isShuffle = true;
    }

    public void initMusicPlayer(){
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }



    public void setList(List<Song> theSongs){
        songs = theSongs;
        size = songs.size();
    }

    public boolean isPrepared() {
        return isPrepared;
    }

    public void setPrepared(boolean prepared) {
        isPrepared = prepared;
    }

    public class MusicBinder extends Binder{
        MusicService getService(){
            return MusicService.this;
        }
    }

    public void playSong(){
        player.reset();
        Song playSong = songs.get(songPosn);
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

    public int getPosn(){
        return player.getCurrentPosition();
    }

    public int getDur(){
        return player.getDuration();
    }

    public static boolean isPng(){
        Log.i("IS IT PLAYING", String.valueOf(player.isPlaying()));
        return player.isPlaying();
    }

    public void pausePlayer(){
        player.pause();
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


    public void setListSize(int s){
        size = s;
    }

    public int getListSize(){return size;}

    public void playPrev(){
        songPosn--;
        if(songPosn < 0)
            songPosn = songs.size()-1;
        playSong();
    }

    public void playNext(){
        if(isShuffle){
            int newSong = songPosn;
            while(newSong == songPosn){
                newSong = random.nextInt(songs.size());
            }
            songPosn = newSong;
        }
        else{
            songPosn++;
            if(songPosn >= songs.size())
                songPosn = 0;
        }
        playSong();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
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
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //START PLAYBACK
        mp.start();
        isPrepared = true;
    }

}

