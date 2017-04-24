package com.myapp.project.trial1;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by user on 4/11/17.
 */

public class Album {
    private byte[] albumArt;
    private String albumName;
    private ArrayList<Song> albumList;

    public Album(byte[] albumArt, String albumName) {
        this.albumArt = albumArt;
        this.albumName = albumName;
        this.albumList = new ArrayList<>();
    }

    public byte[] getAlbumArt() {
        return albumArt;
    }

    public String getAlbumName() {
        return albumName;
    }

    public int getSize() {
        return albumList.size();
    }

    public ArrayList<Song> getAlbumList() {
        return albumList;
    }

    public void add(Song song){
        albumList.add(song);
    }
}
