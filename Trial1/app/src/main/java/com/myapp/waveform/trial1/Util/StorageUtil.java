package com.myapp.waveform.trial1.Util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.myapp.waveform.trial1.Song;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by user on 9/4/17.
 */

public class StorageUtil {
    private final String STORAGE = " com.myapp.waveform.trial1.STORAGE";
    private SharedPreferences preferences;
    private Context context;

    public StorageUtil(Context context){
        this.context = context;
    }

    public void storeSongs(ArrayList<Song> list){
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString("audioArrayList", json);
        editor.apply();
    }

    public ArrayList<Song> loadSongs(){
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("audioArrayList", null);
        Type type = new TypeToken<ArrayList<Song>>(){
        }.getType();
        return gson.fromJson(json,type);
    }

    public void storeSongIndex(int index){
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("audioIndex",index);
        editor.apply();
    }

    public int loadSongIndex(){
        preferences = context.getSharedPreferences(STORAGE,Context.MODE_PRIVATE);
        return preferences.getInt("audioIndex",-1);
    }

    public void clearCachedAudioPlaylist(){
        preferences = context.getSharedPreferences(STORAGE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
}
